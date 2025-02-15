package io.papermc.paper.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.NotNull;

public class PaperPoiType implements PoiType, Handleable<net.minecraft.world.entity.ai.village.poi.PoiType> {

    public static PaperPoiType minecraftToBukkit(net.minecraft.world.entity.ai.village.poi.PoiType minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.POINT_OF_INTEREST_TYPE);
    }

    public static PaperPoiType minecraftHolderToBukkit(Holder<net.minecraft.world.entity.ai.village.poi.PoiType> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.POINT_OF_INTEREST_TYPE);
    }

    public static net.minecraft.world.entity.ai.village.poi.PoiType bukkitToMinecraft(PoiType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.entity.ai.village.poi.PoiType> bukkitToMinecraftHolder(PoiType bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.POINT_OF_INTEREST_TYPE);
    }

    private final NamespacedKey key;
    private final net.minecraft.world.entity.ai.village.poi.PoiType handle;

    public PaperPoiType(final NamespacedKey key, final net.minecraft.world.entity.ai.village.poi.PoiType handle) {
        this.key = key;
        this.handle = handle;
    }

    @Override
    public boolean is(final @NotNull BlockData data) {
        return this.handle.is(((CraftBlockData) data).getState());
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public net.minecraft.world.entity.ai.village.poi.PoiType getHandle() {
        return this.handle;
    }

    public static class PaperOccupancy implements Occupancy, Handleable<PoiManager.Occupancy> {

        public static PoiManager.Occupancy bukkitToMinecraft(Occupancy bukkit) {
            Preconditions.checkArgument(bukkit != null);

            return ((PaperOccupancy) bukkit).getHandle();
        }

        public static Occupancy minecraftToBukkit(PoiManager.Occupancy minecraft) {
            Preconditions.checkArgument(minecraft != null);

            return switch (minecraft) {
                case ANY -> Occupancy.ANY;
                case HAS_SPACE -> Occupancy.HAS_SPACE;
                case IS_OCCUPIED -> Occupancy.IS_OCCUPIED;
            };
        }

        private final PoiManager.Occupancy handle;

        public PaperOccupancy(final PoiManager.Occupancy handle) {
            this.handle = handle;
        }

        @Override
        public PoiManager.Occupancy getHandle() {
            return this.handle;
        }
    }
}
