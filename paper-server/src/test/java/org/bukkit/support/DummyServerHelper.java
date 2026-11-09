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
        final Server instance = mock(withSettings().stubOnly());

        when(instance.getItemFactory()).thenAnswer(_ -> CraftItemFactory.instance());

        when(instance.getName()).thenReturn(DummyServerHelper.class.getName());

        when(instance.getVersion()).thenReturn(DummyServerHelper.class.getPackage().getImplementationVersion());

        when(instance.getBukkitVersion()).thenReturn(Versioning.getBukkitVersion());

        when(instance.getLogger()).thenReturn(Logger.getLogger(DummyServerHelper.class.getCanonicalName()));

        when(instance.getUnsafe()).then(_ -> CraftMagicNumbers.INSTANCE);

        when(instance.createBlockData(any(Material.class))).then(mock -> CraftBlockData.fromString(((Material) mock.getArgument(0)).asBlockType(), null));

        when(instance.getLootTable(any())).then(mock -> new CraftLootTable(mock.getArgument(0),
                RegistryHelper.context().datapack().fullRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, CraftNamespacedKey.toMinecraft(mock.getArgument(0))))));

        when(instance.getTag(any(), any(), any())).then(mock -> {
            final String registry = mock.getArgument(0);
            final Class<?> clazz = mock.getArgument(2);
            final net.minecraft.resources.Identifier key = CraftNamespacedKey.toMinecraft(mock.getArgument(1)); // Paper - address remapping issues

            switch (registry) {
                case org.bukkit.Tag.REGISTRY_BLOCKS -> {
                    Preconditions.checkArgument(clazz == Material.class, "Block namespace must have block type");
                    final TagKey<Block> blockTagKey = TagKey.create(Registries.BLOCK, key);
                    if (BuiltInRegistries.BLOCK.get(blockTagKey).isPresent()) {
                        return new CraftBlockTag(BuiltInRegistries.BLOCK, blockTagKey);
                    }
                }
                case org.bukkit.Tag.REGISTRY_ITEMS -> {
                    Preconditions.checkArgument(clazz == Material.class, "Item namespace must have item type");
                    final TagKey<Item> itemTagKey = TagKey.create(Registries.ITEM, key);
                    if (BuiltInRegistries.ITEM.get(itemTagKey).isPresent()) {
                        return new CraftItemTag(BuiltInRegistries.ITEM, itemTagKey);
                    }
                }
                case org.bukkit.Tag.REGISTRY_FLUIDS -> {
                    Preconditions.checkArgument(clazz == org.bukkit.Fluid.class, "Fluid namespace must have fluid type");
                    final TagKey<net.minecraft.world.level.material.Fluid> fluidTagKey = TagKey.create(Registries.FLUID, key); // Paper - address remapping issues
                    if (BuiltInRegistries.FLUID.get(fluidTagKey).isPresent()) {
                        return new CraftFluidTag(BuiltInRegistries.FLUID, fluidTagKey);
                    }
                }
                case org.bukkit.Tag.REGISTRY_ENTITY_TYPES -> {
                    Preconditions.checkArgument(clazz == org.bukkit.entity.EntityType.class, "Entity type namespace must have entity type");
                    final TagKey<net.minecraft.world.entity.EntityType<?>> entityTagKey = TagKey.create(Registries.ENTITY_TYPE, key); // Paper - address remapping issues
                    if (BuiltInRegistries.ENTITY_TYPE.get(entityTagKey).isPresent()) {
                        return new CraftEntityTag(BuiltInRegistries.ENTITY_TYPE, entityTagKey);
                    }
                }
                case org.bukkit.Tag.REGISTRY_GAME_EVENTS -> {
                    Preconditions.checkArgument(clazz == org.bukkit.GameEvent.class, "Game event namespace must have game event");
                    final TagKey<net.minecraft.world.level.gameevent.GameEvent> gameEventKey = TagKey.create(Registries.GAME_EVENT, key); // Paper - address remapping issues
                    if (BuiltInRegistries.GAME_EVENT.get(gameEventKey).isPresent()) {
                        return new CraftGameEventTag(BuiltInRegistries.GAME_EVENT, gameEventKey);
                    }
                }
                case org.bukkit.tag.DamageTypeTags.REGISTRY_DAMAGE_TYPES -> {
                    Preconditions.checkArgument(clazz == org.bukkit.damage.DamageType.class, "Damage type namespace must have damage type");
                    final TagKey<net.minecraft.world.damagesource.DamageType> damageTagKey = TagKey.create(Registries.DAMAGE_TYPE, key);
                    final net.minecraft.core.Registry<net.minecraft.world.damagesource.DamageType> damageRegistry = org.bukkit.craftbukkit.CraftRegistry.getMinecraftRegistry(Registries.DAMAGE_TYPE);
                    if (damageRegistry.get(damageTagKey).isPresent()) {
                        return new org.bukkit.craftbukkit.tag.CraftDamageTag(damageRegistry, damageTagKey);
                    }
                }
                default -> new io.papermc.paper.util.EmptyTag(); // Paper - testing additions
            }

            return null;
        });

        final Thread currentThread = Thread.currentThread();
        when(instance.isPrimaryThread()).thenAnswer(_ -> Thread.currentThread().equals(currentThread));
        final org.bukkit.plugin.PluginManager pluginManager = new io.papermc.paper.plugin.manager.PaperPluginManagerImpl(instance, new org.bukkit.command.SimpleCommandMap(instance, new java.util.HashMap<>()), null);
        when(instance.getPluginManager()).thenReturn(pluginManager);

        io.papermc.paper.configuration.GlobalConfigTestingBase.setupGlobalConfigForTest(RegistryHelper.registryAccess()); // Paper - configuration files - setup global configuration test base

        when(instance.recipeIterator()).thenAnswer(_ ->
            com.google.common.collect.Iterators.transform(
                RegistryHelper.context().datapack().getRecipeManager().recipes.byType.entries().iterator(),
                input -> input.getValue().toBukkitRecipe()
            )
        );
        return instance;
    }
}
