package io.papermc.paper.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.util.Holderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperPoiType extends HolderableBase<net.minecraft.world.entity.ai.village.poi.PoiType> implements PoiType {

    public static PoiType minecraftToBukkit(final net.minecraft.world.entity.ai.village.poi.PoiType minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.POINT_OF_INTEREST_TYPE);
    }

    public static PaperPoiType minecraftHolderToBukkit(final Holder<net.minecraft.world.entity.ai.village.poi.PoiType> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.POINT_OF_INTEREST_TYPE);
    }

    public static net.minecraft.world.entity.ai.village.poi.PoiType bukkitToMinecraft(final PoiType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.entity.ai.village.poi.PoiType> bukkitToMinecraftHolder(final PoiType bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public PaperPoiType(final Holder<net.minecraft.world.entity.ai.village.poi.PoiType> holder) {
        super(holder);
    }

    @Override
    public boolean is(final @NotNull BlockData data) {
        return this.getHandle().is(((CraftBlockData) data).getState());
    }

    @Override
    public boolean hasOccupants() {
        return this.getHandle().maxTickets() != 0;
    }

    public record PaperOccupancy(PoiManager.Occupancy handle) implements Occupancy, Handleable<PoiManager.Occupancy> {
        public static PoiManager.Occupancy bukkitToMinecraft(final Occupancy bukkit) {
            Preconditions.checkArgument(bukkit != null);

            return ((PaperOccupancy) bukkit).handle();
        }

        public static Occupancy minecraftToBukkit(final PoiManager.Occupancy minecraft) {
            Preconditions.checkArgument(minecraft != null);

            return switch (minecraft) {
                case ANY -> Occupancy.ANY;
                case HAS_SPACE -> Occupancy.HAS_SPACE;
                case IS_OCCUPIED -> Occupancy.IS_OCCUPIED;
            };
        }

        @Override
        public PoiManager.Occupancy getHandle() {
            return this.handle();
        }
    }
}
