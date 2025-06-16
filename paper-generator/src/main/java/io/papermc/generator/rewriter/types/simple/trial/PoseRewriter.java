package io.papermc.generator.rewriter.types.simple.trial;

import io.papermc.typewriter.parser.Lexer;
import io.papermc.typewriter.parser.sequence.SequenceTokens;
import io.papermc.typewriter.parser.sequence.TokenTaskBuilder;
import io.papermc.typewriter.parser.token.CharSequenceBlockToken;
import io.papermc.typewriter.parser.token.CharSequenceToken;
import io.papermc.typewriter.parser.token.TokenType;
import io.papermc.typewriter.preset.EnumCloneRewriter;
import io.papermc.typewriter.preset.model.EnumValue;
import io.papermc.typewriter.replace.SearchMetadata;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.entity.Pose;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

public class PoseRewriter extends EnumCloneRewriter<Pose> {

    public PoseRewriter() {
        super(Pose.class);
    }

    private static final Set<TokenType> FORMAT_TOKENS = EnumSet.of(
        TokenType.COMMENT,
        TokenType.SINGLE_COMMENT
    );

    private static final Set<TokenType> END_VALUE_MARKERS = EnumSet.of(
        TokenType.CO,
        TokenType.SECO
    );

    private @MonotonicNonNull Map<String, CharSequenceBlockToken> javadocsPerConstant;

    private Map<String, CharSequenceBlockToken> parseConstantJavadocs(String content) {
        Map<String, CharSequenceBlockToken> map = new HashMap<>();

        Lexer lex = new Lexer(content.toCharArray());
        lex.checkMarkdownDocComments = !this.sourcesMetadata.canSkipMarkdownDocComments();
        SequenceTokens.wrap(lex, FORMAT_TOKENS)
            .group(action -> {
                ProtoConstant constant = new ProtoConstant();
                action
                    .map(TokenType.JAVADOC, token -> { // /** */
                        constant.javadocs(((CharSequenceBlockToken) token));
                    }, TokenTaskBuilder::asOptional)
                    .map(TokenType.IDENTIFIER, token -> { // <name>
                        constant.name(((CharSequenceToken) token).value());
                    })
                    .skipClosure(TokenType.LPAREN, TokenType.RPAREN, true, TokenTaskBuilder::asOptional) // (*)?
                    .skipClosure(TokenType.LSCOPE, TokenType.RSCOPE, true, TokenTaskBuilder::asOptional) // {*}?
                    .map(END_VALUE_MARKERS::contains, $ -> { // ;|,
                        // this part will fail for the last entry for enum without end (,;)
                        if (constant.isComplete()) {
                            map.put(constant.name(), constant.javadocs());
                        }
                    });
            }, TokenTaskBuilder::asRepeatable)
            .executeOrThrow();

        return map;
    }

    private @MonotonicNonNull SearchMetadata metadata;

    private static final Map<String, String> RENAMES = Map.of(
        Pose.CROUCHING.name(), "SNEAKING"
    );

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        this.javadocsPerConstant = parseConstantJavadocs(metadata.replacedContent());
        this.metadata = metadata;
        super.insert(metadata, builder);
    }

    @Override
    protected EnumValue.Builder rewriteEnumValue(Pose item) {
        return super.rewriteEnumValue(item).rename(name -> RENAMES.getOrDefault(name, name));
    }

    @Override
    protected void appendEnumValue(Pose item, StringBuilder builder, String indent, boolean reachEnd) {
        String constantName = RENAMES.getOrDefault(item.name(), item.name());
        if (this.javadocsPerConstant.containsKey(constantName)) {
            CharSequenceBlockToken token = this.javadocsPerConstant.get(constantName);
            builder.append(indent).append(this.metadata.replacedContent(), token.pos(), token.endPos()).append('\n');
        }
        super.appendEnumValue(item, builder, indent, reachEnd);
    }
}
