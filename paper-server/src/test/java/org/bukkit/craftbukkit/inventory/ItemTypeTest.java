package org.bukkit.craftbukkit.inventory;

import static org.bukkit.support.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.lang.reflect.Field;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ItemTypeTest extends AbstractTestingBase {

    // Ensures all ItemType constants have the correct generics
    @Test
    public void testItemMetaClasses() throws Exception {
        for (Field f : ItemType.class.getDeclaredFields()) {
            ItemType type = (ItemType) f.get(null);
            if (type == ItemType.AIR) {
                continue;
            }

            ItemMeta meta = new ItemStack(type.asMaterial()).getItemMeta();
            Class<?> internal = meta == null ? CraftMetaItem.class : meta.getClass();
            Class<?>[] interfaces = internal.getInterfaces();
            Class<?> expected;
            if (interfaces.length > 0) {
                expected = interfaces[0];
            } else {
                expected = ItemMeta.class;
            }

            Class<?> actual = type.getItemMetaClass();
            assertThat(actual, is(expected));
        }

        assertThrows(UnsupportedOperationException.class, () -> ItemType.AIR.getItemMetaClass());
    }

    @Test
    public void testStaticItemTypeUsage() {
        final ItemStack itemStack = ItemType.DIAMOND.createItemStack();
        Assertions.assertEquals(itemStack.getType(), Material.DIAMOND);
        Assertions.assertEquals(itemStack.getType().asItemType(), ItemType.DIAMOND);
    }

    @Test
    public void testStaticItemTypeUsageBuilder() {
        final ItemStack armor = ItemType.DIAMOND_LEGGINGS.createItemStack(a ->
            a.setTrim(new ArmorTrim(TrimMaterial.EMERALD, TrimPattern.COAST))
        );

        final ItemMeta itemMeta = armor.getItemMeta();
        Assertions.assertInstanceOf(ArmorMeta.class, itemMeta);

        final ArmorTrim trim = ((ArmorMeta) itemMeta).getTrim();
        Assertions.assertEquals(trim.getMaterial(), TrimMaterial.EMERALD);
        Assertions.assertEquals(trim.getPattern(), TrimPattern.COAST);
    }

    @Test
    public void testRetyping() {
        final ItemType itemType = ItemType.ENCHANTED_BOOK;
        Assertions.assertDoesNotThrow(() -> itemType.typed());
        Assertions.assertDoesNotThrow(() -> itemType.typed(EnchantmentStorageMeta.class));
        Assertions.assertThrows(IllegalArgumentException.class, () -> itemType.typed(ArmorMeta.class));
    }
}
