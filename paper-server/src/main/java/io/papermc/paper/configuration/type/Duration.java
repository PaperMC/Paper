package io.papermc.paper.configuration.type;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public final class Duration {

    private static final Pattern SPACE = Pattern.compile("\\s+");
    private static final Pattern PLAIN_NUMBER = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final Pattern DURATION = Pattern.compile("(\\d+(?:\\.\\d+)?)([dhms])", Pattern.CASE_INSENSITIVE);
    public static final ScalarSerializer<Duration> SERIALIZER = new Serializer();

    private final long seconds;
    private final String value;

    private Duration(String value) {
        this.value = value;
        this.seconds = getSeconds(value);
    }

    public long seconds() {
        return this.seconds;
    }

    public long ticks() {
        return this.seconds * 20;
    }

    public String value() {
        return this.value;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duration duration = (Duration) o;
        return seconds == duration.seconds && this.value.equals(duration.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.seconds, this.value);
    }

    @Override
    public String toString() {
        return "Duration{" +
                "seconds=" + this.seconds +
                ", value='" + this.value + '\'' +
                '}';
    }

    public static Duration of(String time) {
        return new Duration(time);
    }

    private static long getSeconds(String str) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException("Duration value must not be empty: '" + str + "'");
        }
        str = SPACE.matcher(str).replaceAll("").toLowerCase(Locale.ROOT);
        if (str.isEmpty()) {
            throw new IllegalArgumentException("Duration value must not be empty");
        }

        // A plain number (optionally negative or decimal) is interpreted as seconds.
        if (PLAIN_NUMBER.matcher(str).matches()) {
            return (long) Double.parseDouble(str);
        }

        long totalSeconds = 0;
        final Matcher matcher = DURATION.matcher(str);
        int lastEnd = 0;
        boolean matched = false;
        while (matcher.find()) {
            if (matcher.start() != lastEnd) {
                throw new IllegalArgumentException("Invalid duration value: '" + str + "'");
            }
            final double amount = Double.parseDouble(matcher.group(1));
            totalSeconds += (long) (amount * switch (matcher.group(2).charAt(0)) {
                case 'd' -> 86400.0;
                case 'h' -> 3600.0;
                case 'm' -> 60.0;
                case 's' -> 1.0;
                default -> throw new IllegalStateException("Unreachable");
            });
            lastEnd = matcher.end();
            matched = true;
        }
        if (!matched || lastEnd != str.length()) {
            throw new IllegalArgumentException("Invalid duration value: '" + str + "'");
        }
        return totalSeconds;
    }

    static final class Serializer extends ScalarSerializer<Duration> {
        private Serializer() {
            super(Duration.class);
        }

        @Override
        public Duration deserialize(Type type, Object obj) throws SerializationException {
            try {
                return new Duration(obj.toString());
            } catch (final IllegalArgumentException ex) {
                throw new SerializationException(type, ex);
            }
        }

        @Override
        protected Object serialize(Duration item, Predicate<Class<?>> typeSupported) {
            return item.value();
        }
    }
}
