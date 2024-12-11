package io.papermc.paper.configuration.type.fallback;

import java.util.OptionalInt;

final class Util {

    static OptionalInt negToDef(int value) {
        return value < 0 ?  OptionalInt.empty() : OptionalInt.of(value);
    }
}
