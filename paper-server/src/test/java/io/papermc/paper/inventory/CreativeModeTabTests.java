package io.papermc.paper.inventory;

import com.google.common.collect.Iterables;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.raid.Raid;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@AllFeatures
public class CreativeModeTabTests {
    @Test
    void testOminousBannerTabs() {
        // The api should be able to handle components on items, such as with the ominous banner
        final ItemStack ominousBanner = Raid.getOminousBannerInstance(RegistryHelper.getRegistry().lookupOrThrow(Registries.BANNER_PATTERN)).asBukkitCopy();

        final Collection<CreativeModeTab> ominousBannerTabs = ominousBanner.getCreativeModeTabs();
        assertEquals(1, ominousBannerTabs.size());
        assertEquals(CreativeModeTab.FUNCTIONAL_BLOCKS, Iterables.getFirst(ominousBannerTabs, null));

        final ItemStack whiteBanner = ItemStack.of(Material.WHITE_BANNER);
        assertEquals(whiteBanner.getType(), ominousBanner.getType());

        final Collection<CreativeModeTab> whiteBannerTabs = whiteBanner.getCreativeModeTabs();
        assertEquals(2, whiteBannerTabs.size());
        assertTrue(whiteBannerTabs.contains(CreativeModeTab.FUNCTIONAL_BLOCKS));
        assertTrue(whiteBannerTabs.contains(CreativeModeTab.COLORED_BLOCKS));
    }

    @Test
    void testMultipleCountStack() {
        final ItemStack dirt = ItemStack.of(Material.DIRT, 10);

        assertFalse(dirt.getCreativeModeTabs().isEmpty());
    }

    @Test
    void searchContainsItems() {
        assertTrue(Iterables.size(CreativeModeTab.SEARCH) > 0);
    }
}
