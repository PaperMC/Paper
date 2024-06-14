package org.bukkit.support;

import static org.mockito.Mockito.*;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidType;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.tag.CraftBlockTag;
import org.bukkit.craftbukkit.tag.CraftEntityTag;
import org.bukkit.craftbukkit.tag.CraftFluidTag;
import org.bukkit.craftbukkit.tag.CraftItemTag;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Versioning;
import org.mockito.stubbing.Answer;

public final class DummyServer {

    public static final Map<Class<?>, Registry<?>> registers = new HashMap<>();

    static {
        try {
            Server instance = mock(withSettings().stubOnly());

            when(instance.getItemFactory()).thenAnswer(mock -> CraftItemFactory.instance());

            when(instance.getName()).thenReturn(DummyServer.class.getName());

            when(instance.getVersion()).thenReturn(DummyServer.class.getPackage().getImplementationVersion());

            when(instance.getBukkitVersion()).thenReturn(Versioning.getBukkitVersion());

            when(instance.getLogger()).thenReturn(Logger.getLogger(DummyServer.class.getCanonicalName()));

            when(instance.getUnsafe()).then(mock -> CraftMagicNumbers.INSTANCE);

            when(instance.createBlockData(any(Material.class))).then(mock -> CraftBlockData.newData(((Material) mock.getArgument(0)).asBlockType(), null));

            when(instance.getLootTable(any())).then(mock -> new CraftLootTable(mock.getArgument(0),
                    AbstractTestingBase.DATA_PACK.fullRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, CraftNamespacedKey.toMinecraft(mock.getArgument(0))))));

            when(instance.getRegistry(any())).then((Answer<Registry<?>>) mock -> {
                Class<? extends Keyed> aClass = mock.getArgument(0);
                return registers.computeIfAbsent(aClass, key -> CraftRegistry.createRegistry(aClass, AbstractTestingBase.REGISTRY_CUSTOM));
            });

            when(instance.getTag(any(), any(), any())).then(mock -> {
                String registry = mock.getArgument(0);
                Class<?> clazz = mock.getArgument(2);
                MinecraftKey key = CraftNamespacedKey.toMinecraft(mock.getArgument(1));

                switch (registry) {
                    case org.bukkit.Tag.REGISTRY_BLOCKS -> {
                        Preconditions.checkArgument(clazz == org.bukkit.Material.class, "Block namespace must have block type");
                        TagKey<Block> blockTagKey = TagKey.create(Registries.BLOCK, key);
                        if (BuiltInRegistries.BLOCK.getTag(blockTagKey).isPresent()) {
                            return new CraftBlockTag(BuiltInRegistries.BLOCK, blockTagKey);
                        }
                    }
                    case org.bukkit.Tag.REGISTRY_ITEMS -> {
                        Preconditions.checkArgument(clazz == org.bukkit.Material.class, "Item namespace must have item type");
                        TagKey<Item> itemTagKey = TagKey.create(Registries.ITEM, key);
                        if (BuiltInRegistries.ITEM.getTag(itemTagKey).isPresent()) {
                            return new CraftItemTag(BuiltInRegistries.ITEM, itemTagKey);
                        }
                    }
                    case org.bukkit.Tag.REGISTRY_FLUIDS -> {
                        Preconditions.checkArgument(clazz == org.bukkit.Fluid.class, "Fluid namespace must have fluid type");
                        TagKey<FluidType> fluidTagKey = TagKey.create(Registries.FLUID, key);
                        if (BuiltInRegistries.FLUID.getTag(fluidTagKey).isPresent()) {
                            return new CraftFluidTag(BuiltInRegistries.FLUID, fluidTagKey);
                        }
                    }
                    case org.bukkit.Tag.REGISTRY_ENTITY_TYPES -> {
                        Preconditions.checkArgument(clazz == org.bukkit.entity.EntityType.class, "Entity type namespace must have entity type");
                        TagKey<EntityTypes<?>> entityTagKey = TagKey.create(Registries.ENTITY_TYPE, key);
                        if (BuiltInRegistries.ENTITY_TYPE.getTag(entityTagKey).isPresent()) {
                            return new CraftEntityTag(BuiltInRegistries.ENTITY_TYPE, entityTagKey);
                        }
                    }
                    default -> throw new IllegalArgumentException();
                }

                return null;
            });

            Bukkit.setServer(instance);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    public static void setup() {}

    private DummyServer() {};
}
