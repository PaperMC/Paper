package io.papermc.paper.inventory;

import com.google.common.collect.Iterables;
import java.util.Collection;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.raid.Raid;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllFeatures
public class CreativeModeTabTests {

    @Test
    void testOminousBannerTabs() {
        // The api should be able to handle components on items, such as with the ominous banner
        final ItemStack ominousBanner = Raid.getOminousBannerInstance(RegistryHelper.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)).asBukkitCopy();

        final Collection<CreativeModeTab> ominousBannerTabs = ominousBanner.getCreativeModeTabs();
        assertEquals(2, ominousBannerTabs.size()); // 1 category + search
        assertEquals(CreativeModeTabs.FUNCTIONAL_BLOCKS, Iterables.getFirst(ominousBannerTabs, null));

        final ItemStack whiteBanner = ItemStack.of(Material.WHITE_BANNER);
        assertEquals(whiteBanner.getType(), ominousBanner.getType());

        final Collection<CreativeModeTab> whiteBannerTabs = whiteBanner.getCreativeModeTabs();
        assertEquals(3, whiteBannerTabs.size()); // 2 categories + search
        assertTrue(whiteBannerTabs.contains(CreativeModeTabs.FUNCTIONAL_BLOCKS));
        assertTrue(whiteBannerTabs.contains(CreativeModeTabs.COLORED_BLOCKS));
    }

    @Test
    void testMultipleCountStack() {
        final ItemStack dirt = ItemStack.of(Material.DIRT, 10);

        assertFalse(dirt.getCreativeModeTabs().isEmpty());
    }

    @Test
    void searchContainsItems() {
        assertFalse(CreativeModeTabs.SEARCH.getContents().isEmpty());
    }
}
