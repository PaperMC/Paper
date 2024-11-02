package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class InstrumentTest extends AbstractTestingBase {
    @Test
    public void getByType() {
        for (Instrument instrument : Instrument.values()) {
            if (instrument.getType() < 0) {
                continue;
            }

            assertThat(Instrument.getByType(instrument.getType()), is(instrument));
        }
    }
}
