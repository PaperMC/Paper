package org.bukkit.craftbukkit.entity.memory;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.memory.MemoryKey;

public final class CraftMemoryKey {

    private CraftMemoryKey() {}

    public static <T, U> MemoryKey<U> minecraftToBukkit(MemoryModuleType<T> minecraft) {
        if (minecraft == null) {
            return null;
        }

        net.minecraft.core.Registry<MemoryModuleType<?>> registry = CraftRegistry.getMinecraftRegistry(Registries.MEMORY_MODULE_TYPE);
        MemoryKey<U> bukkit = (MemoryKey<U>) Registry.MEMORY_MODULE_TYPE.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().identifier()));

        return bukkit;
    }

    public static <T, U> MemoryModuleType<U> bukkitToMinecraft(MemoryKey<T> bukkit) {
        if (bukkit == null) {
            return null;
        }

        return (MemoryModuleType<U>) CraftRegistry.getMinecraftRegistry(Registries.MEMORY_MODULE_TYPE)
                .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }

    @FunctionalInterface
    public interface TypeVisitor extends Brain.Visitor {

        <U> void visitType(MemoryModuleType<U> type);

        @Override
        default  <U> void acceptEmpty(final MemoryModuleType<U> type) {
            this.visitType(type);
        }

        @Override
        default <U> void accept(final MemoryModuleType<U> type, final U value) {
            this.visitType(type);
        }

        @Override
        default <U> void accept(final MemoryModuleType<U> type, final U value, final long timeToLive) {
            this.visitType(type);
        }
    }
}
