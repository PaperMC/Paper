package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class InstrumentTest {
    @Test
    public void getByType() {
        for (Instrument instrument : Instrument.values()) {
            assertThat(Instrument.getByType(instrument.getType()), is(instrument));
        }
    }
}
