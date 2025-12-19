package com.destroystokyo.paper;

import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

// this is currently only testing the mutable impl, but the getters have identical code
@Normal
class PaperSkinPartsTest {

    @Test
    void testDefault() {
        SkinParts parts = SkinParts.allParts();
        assertTrue(parts.hasCapeEnabled());
        assertTrue(parts.hasJacketEnabled());
        assertTrue(parts.hasLeftSleeveEnabled());
        assertTrue(parts.hasRightSleeveEnabled());
        assertTrue(parts.hasLeftPantsEnabled());
        assertTrue(parts.hasRightPantsEnabled());
        assertTrue(parts.hasHatsEnabled());

        // Test the -> new contract
        SkinParts parts1 = SkinParts.allParts();
        assertNotSame(parts, parts1);
    }

    @Test
    void testAllOff() {
        SkinParts.Mutable parts = SkinParts.allParts();
        parts.setCapeEnabled(false);
        parts.setJacketEnabled(false);
        parts.setLeftSleeveEnabled(false);
        parts.setRightSleeveEnabled(false);
        parts.setLeftPantsEnabled(false);
        parts.setRightPantsEnabled(false);
        parts.setHatsEnabled(false);

        assertFalse(parts.hasCapeEnabled());
        assertFalse(parts.hasJacketEnabled());
        assertFalse(parts.hasLeftSleeveEnabled());
        assertFalse(parts.hasRightSleeveEnabled());
        assertFalse(parts.hasLeftPantsEnabled());
        assertFalse(parts.hasRightPantsEnabled());
        assertFalse(parts.hasHatsEnabled());
    }

    @Test
    void testIndividualOff() {
        SkinParts.Mutable parts = SkinParts.allParts();
        parts.setCapeEnabled(false);
        assertFalse(parts.hasCapeEnabled());

        parts = SkinParts.allParts();
        parts.setJacketEnabled(false);
        assertFalse(parts.hasJacketEnabled());

        parts = SkinParts.allParts();
        parts.setLeftSleeveEnabled(false);
        assertFalse(parts.hasLeftSleeveEnabled());

        parts = SkinParts.allParts();
        parts.setRightSleeveEnabled(false);
        assertFalse(parts.hasRightSleeveEnabled());

        parts = SkinParts.allParts();
        parts.setLeftPantsEnabled(false);
        assertFalse(parts.hasLeftPantsEnabled());

        parts = SkinParts.allParts();
        parts.setRightPantsEnabled(false);
        assertFalse(parts.hasRightPantsEnabled());

        parts = SkinParts.allParts();
        parts.setHatsEnabled(false);
        assertFalse(parts.hasHatsEnabled());
    }

    @Test
    void testNoSleeves() {
        SkinParts.Mutable parts = SkinParts.allParts();
        parts.setLeftSleeveEnabled(false);
        parts.setRightSleeveEnabled(false);

        assertFalse(parts.hasLeftSleeveEnabled());
        assertFalse(parts.hasRightSleeveEnabled());
        assertTrue(parts.hasCapeEnabled());
        assertTrue(parts.hasJacketEnabled());
        assertTrue(parts.hasLeftPantsEnabled());
        assertTrue(parts.hasRightPantsEnabled());
        assertTrue(parts.hasHatsEnabled());
    }
}
