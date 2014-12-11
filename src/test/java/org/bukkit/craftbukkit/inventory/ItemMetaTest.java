package org.bukkit.craftbukkit.inventory;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.inventory.ItemStackTest.StackProvider;
import org.bukkit.craftbukkit.inventory.ItemStackTest.StackWrapper;
import org.bukkit.craftbukkit.inventory.ItemStackTest.BukkitWrapper;
import org.bukkit.craftbukkit.inventory.ItemStackTest.CraftWrapper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class ItemMetaTest extends AbstractTestingBase {

    static final int MAX_FIREWORK_POWER = 127; // Please update ItemStackFireworkTest if/when this gets changed.

    @Test(expected=IllegalArgumentException.class)
    public void testPowerLimitExact() {
        newFireworkMeta().setPower(MAX_FIREWORK_POWER + 1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPowerLimitMax() {
        newFireworkMeta().setPower(Integer.MAX_VALUE);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPowerLimitMin() {
        newFireworkMeta().setPower(Integer.MIN_VALUE);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPowerLimitNegative() {
        newFireworkMeta().setPower(-1);
    }

    @Test
    public void testPowers() {
        for (int i = 0; i <= MAX_FIREWORK_POWER; i++) {
            FireworkMeta firework = newFireworkMeta();
            firework.setPower(i);
            assertThat(String.valueOf(i), firework.getPower(), is(i));
        }
    }

    @Test
    public void testConflictingEnchantment() {
        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(Material.DIAMOND_PICKAXE);
        assertThat(itemMeta.hasConflictingEnchant(Enchantment.DURABILITY), is(false));

        itemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, false);
        assertThat(itemMeta.hasConflictingEnchant(Enchantment.DURABILITY), is(false));
        assertThat(itemMeta.hasConflictingEnchant(Enchantment.LOOT_BONUS_BLOCKS), is(true));
        assertThat(itemMeta.hasConflictingEnchant(null), is(false));
    }

    @Test
    public void testConflictingStoredEnchantment() {
        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) Bukkit.getItemFactory().getItemMeta(Material.ENCHANTED_BOOK);
        assertThat(itemMeta.hasConflictingStoredEnchant(Enchantment.DURABILITY), is(false));

        itemMeta.addStoredEnchant(Enchantment.SILK_TOUCH, 1, false);
        assertThat(itemMeta.hasConflictingStoredEnchant(Enchantment.DURABILITY), is(false));
        assertThat(itemMeta.hasConflictingStoredEnchant(Enchantment.LOOT_BONUS_BLOCKS), is(true));
        assertThat(itemMeta.hasConflictingStoredEnchant(null), is(false));
    }

    @Test
    public void testConflictingEnchantments() {
        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(Material.DIAMOND_PICKAXE);
        itemMeta.addEnchant(Enchantment.DURABILITY, 6, true);
        itemMeta.addEnchant(Enchantment.DIG_SPEED, 6, true);
        assertThat(itemMeta.hasConflictingEnchant(Enchantment.LOOT_BONUS_BLOCKS), is(false));

        itemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, false);
        assertThat(itemMeta.hasConflictingEnchant(Enchantment.LOOT_BONUS_BLOCKS), is(true));
        assertThat(itemMeta.hasConflictingEnchant(null), is(false));
    }

    @Test
    public void testConflictingStoredEnchantments() {
        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) Bukkit.getItemFactory().getItemMeta(Material.ENCHANTED_BOOK);
        itemMeta.addStoredEnchant(Enchantment.DURABILITY, 6, true);
        itemMeta.addStoredEnchant(Enchantment.DIG_SPEED, 6, true);
        assertThat(itemMeta.hasConflictingStoredEnchant(Enchantment.LOOT_BONUS_BLOCKS), is(false));

        itemMeta.addStoredEnchant(Enchantment.SILK_TOUCH, 1, false);
        assertThat(itemMeta.hasConflictingStoredEnchant(Enchantment.LOOT_BONUS_BLOCKS), is(true));
        assertThat(itemMeta.hasConflictingStoredEnchant(null), is(false));
    }

    private static FireworkMeta newFireworkMeta() {
        return ((FireworkMeta) Bukkit.getItemFactory().getItemMeta(Material.FIREWORK));
    }

    @Test
    public void testCrazyEquality() {
        CraftItemStack craft = CraftItemStack.asCraftCopy(new ItemStack(1));
        craft.setItemMeta(craft.getItemMeta());
        ItemStack bukkit = new ItemStack(craft);
        assertThat(craft, is(bukkit));
        assertThat(bukkit, is((ItemStack) craft));
    }

    @Test
    public void testEachExtraData() {
        final List<StackProvider> providers = Arrays.asList(
            new StackProvider(Material.BOOK_AND_QUILL) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                    meta.setAuthor("Some author");
                    meta.setPages("Page 1", "Page 2");
                    meta.setTitle("A title");
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.WRITTEN_BOOK) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                    meta.setAuthor("Some author");
                    meta.setPages("Page 1", "Page 2");
                    meta.setTitle("A title");
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            /* Skulls rely on a running server instance
            new StackProvider(Material.SKULL_ITEM) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                    meta.setOwner("Notch");
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            */
            new StackProvider(Material.MAP) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final MapMeta meta = (MapMeta) cleanStack.getItemMeta();
                    meta.setScaling(true);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.LEATHER_BOOTS) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final LeatherArmorMeta meta = (LeatherArmorMeta) cleanStack.getItemMeta();
                    meta.setColor(Color.FUCHSIA);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.POTION) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                    meta.addCustomEffect(PotionEffectType.CONFUSION.createEffect(1, 1), false);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.FIREWORK) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                    meta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).withFade(Color.OLIVE).with(Type.BALL_LARGE).build());
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.ENCHANTED_BOOK) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) cleanStack.getItemMeta();
                    meta.addStoredEnchant(Enchantment.ARROW_FIRE, 1, true);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.FIREWORK_CHARGE) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final FireworkEffectMeta meta = (FireworkEffectMeta) cleanStack.getItemMeta();
                    meta.setEffect(FireworkEffect.builder().withColor(Color.MAROON, Color.BLACK).with(Type.CREEPER).withFlicker().build());
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.BANNER) {                    
                @Override ItemStack operate(ItemStack cleanStack) {
                    final BannerMeta meta = (BannerMeta) cleanStack.getItemMeta();
                    meta.setBaseColor(DyeColor.CYAN);
                    meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BRICKS));
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            }
        );

        assertThat("Forgotten test?", providers, hasSize(ItemStackTest.COMPOUND_MATERIALS.length - 3/* Normal item meta, skulls and tile entities */));

        for (final StackProvider provider : providers) {
            downCastTest(new BukkitWrapper(provider));
            downCastTest(new CraftWrapper(provider));
        }
    }

    private void downCastTest(final StackWrapper provider) {
        final String name = provider.toString();
        final ItemStack blank = new ItemStack(1);
        final ItemStack craftBlank = CraftItemStack.asCraftCopy(blank);

        downCastTest(name, provider.stack(), blank);
        blank.setItemMeta(blank.getItemMeta());
        downCastTest(name, provider.stack(), blank);

        downCastTest(name, provider.stack(), craftBlank);
        craftBlank.setItemMeta(craftBlank.getItemMeta());
        downCastTest(name, provider.stack(), craftBlank);
    }

    private void downCastTest(final String name, final ItemStack stack, final ItemStack blank) {
        assertThat(name, stack, is(not(blank)));
        assertThat(name, stack.getItemMeta(), is(not(blank.getItemMeta())));

        stack.setTypeId(1);

        assertThat(name, stack, is(blank));
    }
}
