package io.papermc.generator.rewriter.types.simple.trial;

import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import io.papermc.typewriter.parser.Lexer;
import io.papermc.typewriter.parser.sequence.SequenceTokens;
import io.papermc.typewriter.parser.sequence.TokenTaskBuilder;
import io.papermc.typewriter.parser.token.CharSequenceBlockToken;
import io.papermc.typewriter.parser.token.CharSequenceToken;
import io.papermc.typewriter.parser.token.TokenType;
import io.papermc.typewriter.replace.SearchMetadata;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

public class VillagerProfessionRewriter extends RegistryFieldRewriter<VillagerProfession> {

    public VillagerProfessionRewriter() {
        super(Registries.VILLAGER_PROFESSION, "getProfession");
    }

    private static final Set<TokenType> FORMAT_TOKENS = EnumSet.of(
        TokenType.COMMENT,
        TokenType.SINGLE_COMMENT
    );

    private @MonotonicNonNull Map<String, CharSequenceBlockToken> javadocsPerConstant;

    private Map<String, CharSequenceBlockToken> parseConstantJavadocs(String content) {
        if (content.isBlank()) {
            return Map.of();
        }

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
                    .skip(TokenType.IDENTIFIER) // Profession
                    .map(TokenType.IDENTIFIER, token -> { // <name>
                        constant.name(((CharSequenceToken) token).value());
                    })
                    .skip(TokenType.IDENTIFIER) // getProfession
                    .skipClosure(TokenType.LPAREN, TokenType.RPAREN, true) // (*)
                    .map(TokenType.SECO, $ -> { // ;
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
        this.javadocsPerConstant = parseConstantJavadocs(metadata.replacedContent());
        super.insert(metadata, builder);
    }

    @Override
    protected void rewriteJavadocs(Holder.Reference<VillagerProfession> reference, String replacedContent, String indent, StringBuilder builder) {
        String constantName = this.rewriteFieldName(reference);
        if (this.javadocsPerConstant.containsKey(constantName)) {
            CharSequenceBlockToken token = this.javadocsPerConstant.get(constantName);
            builder.append(indent).append(replacedContent, token.pos(), token.endPos()).append('\n');
        }
    }
}
