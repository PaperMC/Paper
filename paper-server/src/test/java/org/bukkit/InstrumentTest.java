package org.bukkit;

import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.bukkit.support.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@AllFeatures
public class InstrumentTest { // Paper - moved to internals as this test now access the sound registry.

    @Test
    public void getByType() {
        for (Instrument instrument : Instrument.values()) {
            // Paper - byte magic values are still used

            assertThat(Instrument.getByType(instrument.getType()), is(instrument));
        }
    }
}
