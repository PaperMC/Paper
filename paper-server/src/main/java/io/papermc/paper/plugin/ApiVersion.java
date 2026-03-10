package io.papermc.paper.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedType;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.bukkit.craftbukkit.util.Versioning;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

@NullMarked
public sealed interface ApiVersion extends io.papermc.asm.versioned.ApiVersion<ApiVersion> permits ApiVersion.Legacy, ApiVersion.Modern, ApiVersion.None {

    Serializer SERIALIZER = new Serializer();

    ApiVersion CURRENT = getOrCreateVersion(Versioning.getCurrentApiVersion());
    ApiVersion FLATTENING = getOrCreateVersion("1.13");
    ApiVersion FIELD_NAME_PARITY = getOrCreateVersion("1.20.5");
    ApiVersion ABSTRACT_COW = getOrCreateVersion("1.21.5");
    ApiVersion NONE = getOrCreateVersion("none");

    String getVersionString();

    @Override
    default int compareTo(final ApiVersion o) {
        return switch (o) {
            case final Legacy oLegacy -> switch (this) {
                case final Legacy thisLegacy -> thisLegacy.compareSameType(oLegacy);
                case final Modern ignored -> 1;
                case final None ignored -> -1;
            };
            case final Modern oModern -> switch (this) {
                case final Legacy ignored -> -1;
                case final Modern thisModern -> thisModern.compareSameType(oModern);
                case final None ignored -> -1;
            };
            case final None ignored -> switch (this) {
                case final Legacy ignored2 -> 1;
                case final Modern ignored2 -> 1;
                case final None ignored2 -> 0;
            };
        };
    }

    record Legacy(int major, int minor, int patch) implements ApiVersion {

        private static final Comparator<Legacy> COMPARATOR = Comparator
            .comparingInt(Legacy::major)
            .thenComparingInt(Legacy::minor)
            .thenComparingInt(Legacy::patch);

        int compareSameType(final Legacy other) {
            return COMPARATOR.compare(this, other);
        }

        @Override
        public String getVersionString() {
            return this.major() + "." + this.minor() + '.' + this.patch();
        }

        @Override
        public String toString() {
            return this.getVersionString();
        }
    }

    record Modern(int year, int major, int minor) implements ApiVersion {

        private static final Comparator<Modern> COMPARATOR = Comparator
            .comparingInt(Modern::year)
            .thenComparingInt(Modern::major)
            .thenComparingInt(Modern::minor);

        int compareSameType(final Modern other) {
            return COMPARATOR.compare(this, other);
        }

        @Override
        public String getVersionString() {
            return this.year() + "." + this.major() + '.' + this.minor();
        }

        @Override
        public String toString() {
            return this.getVersionString();
        }
    }

    record None() implements ApiVersion {

        private static final None INSTANCE = new None();

        @Override
        public String getVersionString() {
            return "none";
        }

        @Override
        public String toString() {
            return this.getVersionString();
        }
    }

    final class Serializer extends ScalarSerializer.Annotated<ApiVersion> {

        Serializer() {
            super(ApiVersion.class);
        }

        @Override
        public ApiVersion deserialize(final AnnotatedType type, final Object obj) throws SerializationException {
            try {
                final ApiVersion version = getOrCreateVersion(obj.toString());
                final Minimum min = type.getAnnotation(Minimum.class);
                if (min != null) {
                    final ApiVersion minVersion = getOrCreateVersion(min.value());
                    if (version.isOlderThan(minVersion)) {
                        throw new SerializationException(ApiVersion.class, version + " is too old for a paper plugin!");
                    }
                }
                return version;
            } catch (final IllegalArgumentException ex) {
                throw new SerializationException(ApiVersion.class, "Could not parse version string", ex);
            }
        }

        @Override
        protected Object serialize(final AnnotatedType type, final ApiVersion item, final Predicate<Class<?>> typeSupported) {
            return item.getVersionString();
        }
    }

    static ApiVersion getOrCreateVersion(final @Nullable String versionString) {
        class Holder {
            private static final Map<String, ApiVersion> cache = new ConcurrentHashMap<>();
        }
        if (versionString == null || versionString.isBlank() || versionString.equalsIgnoreCase("none")) {
            return None.INSTANCE;
        }
        final ApiVersion parsed = parse(versionString);
        Holder.cache.putIfAbsent(parsed.getVersionString(), parsed);
        return Holder.cache.get(parsed.getVersionString());
    }

    private static ApiVersion parse(final String versionString) {
        final String[] parts = versionString.split("\\.");

        if (parts.length < 2 || parts.length > 3) {
            throw new IllegalArgumentException("Version string must have 2 or 3 numbers");
        }

        final int first = Integer.parseInt(parts[0]);
        final int second = Integer.parseInt(parts[1]);
        final int third = parts.length == 3 ? Integer.parseInt(parts[2]) : 0;

        if (first == 1) {
            if (second > 21) {
                throw new IllegalArgumentException("Legacy version string must be 1.21.xx or lower");
            }
            return new Legacy(first, second, third);
        } else {
            if (first < 26) {
                throw new IllegalArgumentException("Modern version string must be 26.xx.xx or higher");
            }
            return new Modern(first, second, third);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Minimum {
        String value();
    }
}
