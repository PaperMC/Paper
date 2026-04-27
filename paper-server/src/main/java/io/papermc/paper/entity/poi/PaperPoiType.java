package io.papermc.paper.entity.poi;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperPoiType extends HolderableBase<net.minecraft.world.entity.ai.village.poi.PoiType> implements PoiType {

    public static PaperPoiType minecraftHolderToBukkit(final Holder<net.minecraft.world.entity.ai.village.poi.PoiType> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.POINT_OF_INTEREST_TYPE);
    }

    public static Holder<net.minecraft.world.entity.ai.village.poi.PoiType> bukkitToMinecraftHolder(final PoiType bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public PaperPoiType(final Holder<net.minecraft.world.entity.ai.village.poi.PoiType> holder) {
        super(holder);
    }

    @Override
    public boolean is(final BlockData data) {
        return this.getHandle().is(((CraftBlockData) data).getState());
    }

    @Override
    public boolean hasOccupants() {
        return this.getHandle().maxTickets() != 0;
    }

    public record PaperOccupancy(PoiManager.Occupancy handle) implements Occupancy {
        public static PoiManager.Occupancy bukkitToMinecraft(final Occupancy occupancy) {
            return ((PaperOccupancy) occupancy).handle();
        }

        public static Occupancy minecraftToBukkit(final PoiManager.Occupancy occupancy) {
            return switch (occupancy) {
                case ANY -> Occupancy.ANY;
                case HAS_SPACE -> Occupancy.HAS_SPACE;
                case IS_OCCUPIED -> Occupancy.IS_OCCUPIED;
            };
        }
    }
}
