package org.bukkit;

import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.bukkit.support.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@Normal
@Deprecated
public class InstrumentTest {

    @Test
    public void getByType() {
        for (Instrument instrument : Instrument.values()) {
            assertThat(Instrument.getByType(instrument.getType()), is(instrument));
        }
    }
}
