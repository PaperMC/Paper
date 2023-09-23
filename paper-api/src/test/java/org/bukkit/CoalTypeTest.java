package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.jupiter.api.Test;

public class CoalTypeTest {
    @Test
    public void getByData() {
        for (CoalType coalType : CoalType.values()) {
            assertThat(CoalType.getByData(coalType.getData()), is(coalType));
        }
    }
}
