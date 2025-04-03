package io.papermc.generator.utils;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Javadocs {

    public static String getVersionDependentClassHeader(String objectIdentifier, String headerIdentifier) {
        return """
            Vanilla %s for %s.
            
            @apiNote The fields provided here are a direct representation of
            what is available from the vanilla game source. They may be
            changed (including removals) on any Minecraft version
            bump, so cross-version compatibility is not provided on the
            same level as it is on most of the other API.
            """.formatted(objectIdentifier, headerIdentifier);
    }

    public static String getVersionDependentField(String headerIdentifier) {
        return """
            %s
            
            @apiNote This field is version-dependant and may be removed in future Minecraft versions
            """.formatted(headerIdentifier);
    }

    public static final String CREATE_TYPED_KEY_JAVADOC = """
        Creates a typed key for {@link $T} in the registry {@code $L}.
        
        @param key the value's key in the registry
        @return a new typed key
        """;

    public static final String CREATED_TAG_KEY_JAVADOC = """
        Creates a tag key for {@link $T} in the registry {@code $L}.
        
        @param key the tag key's key
        @return a new tag key
        """;

    private Javadocs() {
    }
}
