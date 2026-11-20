package io.papermc.generator.rewriter.types.simple.trial;

import io.papermc.typewriter.parser.token.CharSequenceBlockToken;
import org.jspecify.annotations.Nullable;

class ProtoConstant {

    private @Nullable String name;
    private @Nullable CharSequenceBlockToken token;

    public void name(String name) {
        this.name = name;
    }

    public void javadocs(CharSequenceBlockToken token) {
        this.token = token;
    }

    public String name() {
        return this.name;
    }

    public CharSequenceBlockToken javadocs() {
        return this.token;
    }

    public boolean isComplete() {
        return this.name != null && this.token != null;
    }
}
