package org.bukkit.support;

import static org.mockito.Mockito.*;
import com.google.common.base.Preconditions;
import java.util.logging.Logger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.tag.CraftBlockTag;
import org.bukkit.craftbukkit.tag.CraftEntityTag;
import org.bukkit.craftbukkit.tag.CraftFluidTag;
import org.bukkit.craftbukkit.tag.CraftGameEventTag;
import org.bukkit.craftbukkit.tag.CraftItemTag;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Versioning;

public final class DummyServerHelper {

    public static Server setup() {
        Server instance = mock(withSettings().stubOnly());

        when(instance.getItemFactory()).thenAnswer(mock -> CraftItemFactory.instance());

        when(instance.getName()).thenReturn(DummyServerHelper.class.getName());

        when(instance.getVersion()).thenReturn(DummyServerHelper.class.getPackage().getImplementationVersion());

        when(instance.getBukkitVersion()).thenReturn(Versioning.getBukkitVersion());

        when(instance.getLogger()).thenReturn(Logger.getLogger(DummyServerHelper.class.getCanonicalName()));

        when(instance.getUnsafe()).then(mock -> CraftMagicNumbers.INSTANCE);

        when(instance.createBlockData(any(Material.class))).then(mock -> CraftBlockData.newData(((Material) mock.getArgument(0)).asBlockType(), null));

        when(instance.getLootTable(any())).then(mock -> new CraftLootTable(mock.getArgument(0),
                RegistryHelper.getDataPack().fullRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, CraftNamespacedKey.toMinecraft(mock.getArgument(0))))));

        when(instance.getTag(any(), any(), any())).then(mock -> {
            String registry = mock.getArgument(0);
            Class<?> clazz = mock.getArgument(2);
            net.minecraft.resources.ResourceLocation key = CraftNamespacedKey.toMinecraft(mock.getArgument(1)); // Paper - address remapping issues

            switch (registry) {
                case org.bukkit.Tag.REGISTRY_BLOCKS -> {
                    Preconditions.checkArgument(clazz == Material.class, "Block namespace must have block type");
                    TagKey<Block> blockTagKey = TagKey.create(Registries.BLOCK, key);
                    if (BuiltInRegistries.BLOCK.get(blockTagKey).isPresent()) {
                        return new CraftBlockTag(BuiltInRegistries.BLOCK, blockTagKey);
                    }
                }
                case org.bukkit.Tag.REGISTRY_ITEMS -> {
                    Preconditions.checkArgument(clazz == Material.class, "Item namespace must have item type");
                    TagKey<Item> itemTagKey = TagKey.create(Registries.ITEM, key);
                    if (BuiltInRegistries.ITEM.get(itemTagKey).isPresent()) {
                        return new CraftItemTag(BuiltInRegistries.ITEM, itemTagKey);
                    }
                }
                case org.bukkit.Tag.REGISTRY_FLUIDS -> {
                    Preconditions.checkArgument(clazz == org.bukkit.Fluid.class, "Fluid namespace must have fluid type");
                    TagKey<net.minecraft.world.level.material.Fluid> fluidTagKey = TagKey.create(Registries.FLUID, key); // Paper - address remapping issues
                    if (BuiltInRegistries.FLUID.get(fluidTagKey).isPresent()) {
                        return new CraftFluidTag(BuiltInRegistries.FLUID, fluidTagKey);
                    }
                }
                case org.bukkit.Tag.REGISTRY_ENTITY_TYPES -> {
                    Preconditions.checkArgument(clazz == org.bukkit.entity.EntityType.class, "Entity type namespace must have entity type");
                    TagKey<net.minecraft.world.entity.EntityType<?>> entityTagKey = TagKey.create(Registries.ENTITY_TYPE, key); // Paper - address remapping issues
                    if (BuiltInRegistries.ENTITY_TYPE.get(entityTagKey).isPresent()) {
                        return new CraftEntityTag(BuiltInRegistries.ENTITY_TYPE, entityTagKey);
                    }
                }
                case org.bukkit.Tag.REGISTRY_GAME_EVENTS -> {
                    Preconditions.checkArgument(clazz == org.bukkit.GameEvent.class, "Game event namespace must have game event");
                    TagKey<net.minecraft.world.level.gameevent.GameEvent> gameEventKey = TagKey.create(Registries.GAME_EVENT, key); // Paper - address remapping issues
                    if (BuiltInRegistries.GAME_EVENT.get(gameEventKey).isPresent()) {
                        return new CraftGameEventTag(BuiltInRegistries.GAME_EVENT, gameEventKey);
                    }
                }
                default -> new io.papermc.paper.util.EmptyTag(); // Paper - testing additions
            }

            return null;
        });

        // Paper start - testing additions
        final Thread currentThread = Thread.currentThread();
        when(instance.isPrimaryThread()).thenAnswer(ignored -> Thread.currentThread().equals(currentThread));
        final org.bukkit.plugin.PluginManager pluginManager = new  io.papermc.paper.plugin.manager.PaperPluginManagerImpl(instance, new org.bukkit.command.SimpleCommandMap(instance, new java.util.HashMap<>()), null);
        when(instance.getPluginManager()).thenReturn(pluginManager);
        // Paper end - testing additions

        io.papermc.paper.configuration.GlobalConfigTestingBase.setupGlobalConfigForTest(RegistryHelper.getRegistry()); // Paper - configuration files - setup global configuration test base

        // Paper start - add test for recipe conversion
        when(instance.recipeIterator()).thenAnswer(ignored ->
            com.google.common.collect.Iterators.transform(
                RegistryHelper.getDataPack().getRecipeManager().recipes.byType.entries().iterator(),
                input -> input.getValue().toBukkitRecipe()
            )
        );
        // Paper end - add test for recipe conversion
        return instance;
    }
}
