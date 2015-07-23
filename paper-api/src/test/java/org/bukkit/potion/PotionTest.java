package org.bukkit.potion;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.junit.BeforeClass;
import org.junit.Test;

public class PotionTest {

    @BeforeClass
    public static void setup() {
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
        PotionEffectType.registerPotionEffectType(new PotionEffectType(6){
            @Override
            public double getDurationModifier() {
                return 1;
            }

            @Override
            public String getName() {
                return "Heal";
            }

            @Override
            public boolean isInstant() {
                return false;
            }
        });
    }

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

    @Test(expected=IllegalArgumentException.class)
    public void nullType() {
        new Potion(null, 2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void maxLevelConstruct() {
        new Potion(PotionType.POISON, 3);
    }

    @Test(expected=IllegalArgumentException.class)
    public void maxLevelSet() {
        Potion potion = new Potion(PotionType.POISON);
        potion.setLevel(3);
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullStack() {
        Potion potion = new Potion(PotionType.POISON);
        potion.apply((ItemStack) null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullEntity() {
        Potion potion = new Potion(PotionType.POISON);
        potion.apply((LivingEntity) null);
    }

    @Test
    public void water() {
        Potion potion = new Potion(PotionType.WATER);
        assertEquals(0, potion.getLevel());
        assertFalse(potion.isSplash());
        assertFalse(potion.hasExtendedDuration());
        assertEquals(0, potion.toDamageValue());
    }

    @Test
    public void mundane() {
        Potion potion = new Potion(0);
        assertFalse(potion.getType() == PotionType.WATER);
        assertFalse(potion.toDamageValue() == 0);
        assertEquals(8192, potion.toDamageValue());
        Potion potion2 = Potion.fromDamage(8192);
        assertEquals(potion, potion2);
        assertEquals(0, potion.getLevel());
    }

    @Test
    public void awkward() {
        Potion potion = new Potion(16);
        assertEquals(16, potion.getNameId());
        assertFalse(potion.isSplash());
        assertFalse(potion.hasExtendedDuration());
        assertNull(potion.getType());
        assertEquals(16, potion.toDamageValue());
    }

    private static final int EXTENDED_BIT = 0x40;
    private static final int SPLASH_BIT = 0x4000;
}
