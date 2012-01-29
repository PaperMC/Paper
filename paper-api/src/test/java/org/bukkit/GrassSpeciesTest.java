package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class GrassSpeciesTest {
    @Test
    public void getByData() {
        for (GrassSpecies grassSpecies : GrassSpecies.values()) {
            assertThat(GrassSpecies.getByData(grassSpecies.getData()), is(grassSpecies));
        }
    }
}
