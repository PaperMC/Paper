package io.papermc.paper.inventory;

import java.util.Collection;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.raid.Raid;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@VanillaFeature
public class CreativeModeTabTest {

    @Test
    void testOminousBannerTabs() {
        // The api should be able to handle components on items, such as with the ominous banner
        final ItemStack ominousBanner = Raid.getOminousBannerInstance(CraftRegistry.getMinecraftRegistry(Registries.BANNER_PATTERN)).asBukkitCopy();

        final Collection<CreativeModeTab> ominousBannerCategories = ominousBanner.getCreativeCategories();
        assertEquals(1, ominousBannerCategories.size());
        assertEquals(CreativeModeTabs.FUNCTIONAL_BLOCKS, ominousBannerCategories.iterator().next());

        final ItemStack whiteBanner = ItemStack.of(Material.WHITE_BANNER);
        assertEquals(whiteBanner.getType(), ominousBanner.getType());

        final Collection<CreativeModeTab> whiteBannerCategories = whiteBanner.getCreativeCategories();
        assertEquals(2, whiteBannerCategories.size());
        assertTrue(whiteBannerCategories.contains(CreativeModeTabs.FUNCTIONAL_BLOCKS));
        assertTrue(whiteBannerCategories.contains(CreativeModeTabs.COLORED_BLOCKS));
    }

    @Test
    void testMultipleCountStack() {
        final ItemStack dirt = ItemStack.of(Material.DIRT, 10);

        assertFalse(dirt.getCreativeCategories().isEmpty());
    }

    @Test
    void testSpecialTabs() {
        assertFalse(CreativeModeTabs.SEARCH.getContents().isEmpty());
        assertTrue(CreativeModeTabs.INVENTORY.getContents().isEmpty());
        assertTrue(CreativeModeTabs.HOTBAR.getContents().isEmpty());
    }
}
