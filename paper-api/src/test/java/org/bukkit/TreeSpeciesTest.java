package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.jupiter.api.Test;

public class TreeSpeciesTest {
    @Test
    public void getByData() {
        for (TreeSpecies treeSpecies : TreeSpecies.values()) {
            assertThat(TreeSpecies.getByData(treeSpecies.getData()), is(treeSpecies));
        }
    }
}
