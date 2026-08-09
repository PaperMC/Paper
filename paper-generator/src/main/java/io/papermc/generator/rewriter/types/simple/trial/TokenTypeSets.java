package io.papermc.generator.rewriter.types.simple.trial;

import com.google.common.collect.Sets;
import io.papermc.typewriter.parser.token.TokenType;
import java.util.Set;

public final class TokenTypeSets {

    static final Set<TokenType> COMMENT_TOKENS = of(
        TokenType.COMMENT,
        TokenType.SINGLE_COMMENT
    );

    static final Set<TokenType> JAVADOC_TOKENS = of(
        TokenType.JAVADOC,
        TokenType.MARKDOWN_JAVADOC
    );

    static final Set<TokenType> ENUM_END_MARKER_TOKENS = of(
        TokenType.CO,
        TokenType.SECO
    );

    private static Set<TokenType> of(TokenType first, TokenType... rest) {
        return Sets.immutableEnumSet(first, rest);
    }

    private TokenTypeSets() {
    }
}
