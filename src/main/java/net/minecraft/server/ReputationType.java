package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public enum ReputationType {

    MAJOR_NEGATIVE("major_negative", -5, 100, 10, 10), MINOR_NEGATIVE("minor_negative", -1, 200, 20, 20), MINOR_POSITIVE("minor_positive", 1, 200, 1, 5), MAJOR_POSITIVE("major_positive", 5, 100, 0, 100), TRADING("trading", 1, 25, 2, 20);

    public final String f;
    public final int g;
    public final int h;
    public final int i;
    public final int j;
    private static final Map<String, ReputationType> k = (Map) Stream.of(values()).collect(ImmutableMap.toImmutableMap((reputationtype) -> {
        return reputationtype.f;
    }, Function.identity()));

    private ReputationType(String s, int i, int j, int k, int l) {
        this.f = s;
        this.g = i;
        this.h = j;
        this.i = k;
        this.j = l;
    }

    @Nullable
    public static ReputationType a(String s) {
        return (ReputationType) ReputationType.k.get(s);
    }
}
