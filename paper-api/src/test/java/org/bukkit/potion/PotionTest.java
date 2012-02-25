package org.bukkit.potion;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
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
        assertTrue(potion.getType() == PotionType.INSTANT_HEAL && potion.getLevel() == 2);
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
        PotionEffectType.registerPotionEffectType(new PotionEffectType(19){
            @Override
            public double getDurationModifier() {
                return 1;
            }

            @Override
            public String getName() {
                return "Poison";
            }

            @Override
            public boolean isInstant() {
                return false;
            }
        });
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
    public void setLevel() {
        Potion potion = new Potion(PotionType.POISON);
        assertEquals(1, potion.getLevel());
        potion.setLevel(2);
        assertEquals(2, potion.getLevel());
        assertTrue((potion.toDamageValue() & 0x3F) == (PotionType.POISON.getDamageValue() | 0x20));
    }

    @Test
    public void useNulls() {
        try {
            new Potion(null, 2);
            fail("cannot use null type in constructor with a level");
        } catch (IllegalArgumentException ex) {
        }

        try {
            new Potion(PotionType.POISON, 3);
            fail("level must be less than 3 in constructor");
        } catch (IllegalArgumentException ex) {
        }

        Potion potion = new Potion(PotionType.POISON);
        try {
            potion.setLevel(3);
            fail("level must be set less than 3");
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
