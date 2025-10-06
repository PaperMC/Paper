package io.papermc.paper.generator;

public enum PackType {
    VANILLA(true),
    PAPER(true); // always run vanilla gen since paper pack depends on it for tag provider TODO allow to run without?

    public final boolean requireVanillaProviders;

    PackType(boolean requireVanillaProviders) {
        this.requireVanillaProviders = requireVanillaProviders;
    }
}
