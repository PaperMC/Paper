package org.bukkit.potion;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion.Tier;
import org.junit.Test;

public class PotionTest {
    @Test
    public void applyToItemStack() {
        Potion potion = new Potion(PotionType.POISON);
        ItemStack stack = new ItemStack(Material.POTION, 1);
        potion.apply(stack);
        assertTrue(stack.getDurability() == potion.toDamageValue());
    }

    @Test
    public void fromDamage() {
        Potion potion = Potion.fromDamage(PotionType.POISON.getDamageValue());
        assertTrue(potion.getType() == PotionType.POISON);
        potion = Potion.fromDamage(PotionType.POISON.getDamageValue() | SPLASH_BIT);
        assertTrue(potion.getType() == PotionType.POISON && potion.isSplash());
        potion = Potion.fromDamage(0x25 /* Potion of Healing II */);
        assertTrue(potion.getType() == PotionType.INSTANT_HEAL && potion.getTier() == Tier.TWO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalApplyToItemStack() {
        Potion potion = new Potion(PotionType.POISON);
        potion.apply(new ItemStack(Material.AIR, 1));
    }

    @Test
    public void ItemStackConversion() {
        Potion potion = new Potion(PotionType.POISON);
        ItemStack itemstack = potion.toItemStack(1);
        assertThat(itemstack.getType(), is(Material.POTION));
        assertTrue(itemstack.getAmount() == 1);
        assertTrue(itemstack.getDurability() == potion.toDamageValue());
    }

    @Test
    public void setExtended() {
        Potion potion = new Potion(PotionType.POISON);
        assertFalse(potion.hasExtendedDuration());
        potion.setHasExtendedDuration(true);
        assertTrue(potion.hasExtendedDuration());
        assertTrue((potion.toDamageValue() & EXTENDED_BIT) != 0);
    }

    @Test
    public void setSplash() {
        Potion potion = new Potion(PotionType.POISON);
        assertFalse(potion.isSplash());
        potion.setSplash(true);
        assertTrue(potion.isSplash());
        assertTrue((potion.toDamageValue() & SPLASH_BIT) != 0);
    }

    @Test
    public void setTier() {
        Potion potion = new Potion(PotionType.POISON);
        assertThat(potion.getTier(), is(Tier.ONE));
        potion.setTier(Tier.TWO);
        assertThat(potion.getTier(), is(Tier.TWO));
        assertTrue(potion.toDamageValue() == (PotionType.POISON.getDamageValue() | potion.getTier().getDamageBit()));
    }

    @Test
    public void useNulls() {
        try {
            new Potion(null);
            fail("cannot use null type in constructor");
        } catch (IllegalArgumentException ex) {
        }

        try {
            new Potion(PotionType.POISON, null);
            fail("cannot use null tier in constructor");
        } catch (IllegalArgumentException ex) {
        }

        Potion potion = new Potion(PotionType.POISON);
        try {
            potion.setTier(null);
            fail("cannot set a null tier");
        } catch (IllegalArgumentException ex) {
        }

        try {
            potion.apply((ItemStack) null);
            fail("cannot apply to a null itemstack");
        } catch (IllegalArgumentException ex) {
        }

        try {
            potion.apply((LivingEntity) null);
            fail("cannot apply to a null entity");
        } catch (IllegalArgumentException ex) {
        }
    }

    private static final int EXTENDED_BIT = 0x40;
    private static final int SPLASH_BIT = 0x4000;
}
