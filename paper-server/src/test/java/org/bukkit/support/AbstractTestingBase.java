package org.bukkit.support;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.SharedConstants;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.server.DispenserRegistry;
import net.minecraft.server.packs.EnumResourcePackType;
import net.minecraft.server.packs.ResourcePackVanilla;
import net.minecraft.server.packs.repository.ResourcePackSourceVanilla;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagRegistry;
import net.minecraft.util.Unit;
import net.minecraft.world.level.storage.loot.LootPredicateManager;
import net.minecraft.world.level.storage.loot.LootTableRegistry;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.junit.Assert;

/**
 *  If you are getting: java.lang.ExceptionInInitializerError
 *    at net.minecraft.server.StatisticList.&lt;clinit&gt;(SourceFile:58)
 *    at net.minecraft.server.Item.&lt;clinit&gt;(SourceFile:252)
 *    at net.minecraft.server.Block.&lt;clinit&gt;(Block.java:577)
 *
 *  extend this class to solve it.
 */
public abstract class AbstractTestingBase {
    // Materials that only exist in block form (or are legacy)
    public static final List<Material> INVALIDATED_MATERIALS;

    public static final LootTableRegistry LOOT_TABLE_REGISTRY;
    public static final TagRegistry TAG_REGISTRY;

    static {
        SharedConstants.a();
        DispenserRegistry.init();
        // Set up resource manager
        ResourceManager resourceManager = new ResourceManager(EnumResourcePackType.SERVER_DATA);
        // add tags and loot tables for unit tests
        resourceManager.a(TAG_REGISTRY = new TagRegistry(IRegistryCustom.a()));
        resourceManager.a(LOOT_TABLE_REGISTRY = new LootTableRegistry(new LootPredicateManager()));
        // Register vanilla pack
        resourceManager.a(MoreExecutors.directExecutor(), MoreExecutors.directExecutor(), Collections.singletonList(new ResourcePackVanilla(ResourcePackSourceVanilla.BUILT_IN_METADATA, "minecraft")), CompletableFuture.completedFuture(Unit.INSTANCE)).join();
        // Bind tags
        TAG_REGISTRY.a().bind();

        DummyServer.setup();
        DummyEnchantments.setup();

        ImmutableList.Builder<Material> builder = ImmutableList.builder();
        for (Material m : Material.values()) {
            if (m.isLegacy() || CraftMagicNumbers.getItem(m) == null) {
                builder.add(m);
            }
        }
        INVALIDATED_MATERIALS = builder.build();
        Assert.assertEquals("Expected 590 invalidated materials (got " + INVALIDATED_MATERIALS.size() + ")", 590, INVALIDATED_MATERIALS.size());
    }
}
