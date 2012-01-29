package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TreeSpeciesTest {
    @Test
    public void getByData() {
        for (TreeSpecies treeSpecies : TreeSpecies.values()) {
            assertThat(TreeSpecies.getByData(treeSpecies.getData()), is(treeSpecies));
        }
    }
}
