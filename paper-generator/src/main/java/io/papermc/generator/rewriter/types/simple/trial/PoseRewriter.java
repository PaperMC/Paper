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
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.Pose;
import org.jspecify.annotations.Nullable;

public class PoseRewriter extends EnumCloneRewriter<Pose> {

    private static final Map<String, String> RENAMES = Map.of(
        Pose.CROUCHING.name(), "SNEAKING"
    );

    private @Nullable Map<String, CharSequenceBlockToken> javadocsPerConstant;

    private @Nullable SearchMetadata metadata;

    public PoseRewriter() {
        super(Pose.class);
    }

    private Map<String, CharSequenceBlockToken> parseConstantJavadocs(String content) {
        Map<String, CharSequenceBlockToken> map = new HashMap<>();

        Lexer lex = new Lexer(content.toCharArray());
        lex.checkMarkdownDocComments = !this.sourcesMetadata.canSkipMarkdownDocComments();
        SequenceTokens.wrap(lex, TokenTypeSets.COMMENT_TOKENS)
            .group(action -> {
                ProtoConstant constant = new ProtoConstant();
                action
                    .map(TokenTypeSets.JAVADOC_TOKENS::contains, token -> { // /** */
                        constant.javadocs(((CharSequenceBlockToken) token));
                    }, TokenTaskBuilder::asOptional)
                    .map(TokenType.IDENTIFIER, token -> { // <name>
                        constant.name(((CharSequenceToken) token).value());
                    })
                    .skipClosure(TokenType.LPAREN, TokenType.RPAREN, true, TokenTaskBuilder::asOptional) // (*)?
                    .skipClosure(TokenType.LSCOPE, TokenType.RSCOPE, true, TokenTaskBuilder::asOptional) // {*}?
                    .map(TokenTypeSets.ENUM_END_MARKER_TOKENS::contains, _ -> { // ;|,
                        // this part will fail for the last entry for enum without end (,;)
                        if (constant.isComplete()) {
                            map.put(constant.name(), constant.javadocs());
                        }
                    });
            }, TokenTaskBuilder::asRepeatable)
            .executeOrThrow();

        return map;
    }

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        this.javadocsPerConstant = this.parseConstantJavadocs(metadata.replacedContent());
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
