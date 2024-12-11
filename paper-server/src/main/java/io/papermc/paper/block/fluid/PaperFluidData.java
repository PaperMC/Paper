package io.papermc.paper.block.fluid;

import com.google.common.base.Preconditions;
import io.papermc.paper.block.fluid.type.PaperFallingFluidData;
import io.papermc.paper.block.fluid.type.PaperFlowingFluidData;
import io.papermc.paper.util.MCUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraft.world.level.material.WaterFluid;
import org.bukkit.Fluid;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftFluid;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class PaperFluidData implements FluidData {

    private final FluidState state;

    protected PaperFluidData(final FluidState state) {
        this.state = state;
    }

    /**
     * Provides the internal server representation of this fluid data.
     * @return the fluid state.
     */
    public FluidState getState() {
        return this.state;
    }

    @Override
    public final @NotNull Fluid getFluidType() {
        return CraftFluid.minecraftToBukkit(this.state.getType());
    }

    @Override
    public @NotNull PaperFluidData clone() {
        try {
            return (PaperFluidData) super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError("Clone not supported", ex);
        }
    }

    @Override
    public @NotNull Vector computeFlowDirection(final Location location) {
        Preconditions.checkArgument(location.getWorld() != null, "Cannot compute flow direction on world-less location");
        return CraftVector.toBukkit(this.state.getFlow(
            ((CraftWorld) location.getWorld()).getHandle(),
            MCUtil.toBlockPosition(location)
        ));
    }

    @Override
    public int getLevel() {
        return this.state.getAmount();
    }

    @Override
    public float computeHeight(@NotNull final Location location) {
        Preconditions.checkArgument(location.getWorld() != null, "Cannot compute height on world-less location");
        return this.state.getHeight(((CraftWorld) location.getWorld()).getHandle(), MCUtil.toBlockPos(location));
    }

    @Override
    public boolean isSource() {
        return this.state.isSource();
    }

    @Override
    public int hashCode() {
        return this.state.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof final PaperFluidData paperFluidData && this.state.equals(paperFluidData.state);
    }

    @Override
    public String toString() {
        return "PaperFluidData{" + this.state + "}";
    }

    /* Registry */
    private static final Map<Class<? extends net.minecraft.world.level.material.Fluid>, Function<FluidState, PaperFluidData>> MAP = new HashMap<>();
    static {
        //<editor-fold desc="PaperFluidData Registration" defaultstate="collapsed">
        register(LavaFluid.Source.class, PaperFallingFluidData::new);
        register(WaterFluid.Source.class, PaperFallingFluidData::new);
        register(LavaFluid.Flowing.class, PaperFlowingFluidData::new);
        register(WaterFluid.Flowing.class, PaperFlowingFluidData::new);
        //</editor-fold>
    }

    static void register(final Class<? extends net.minecraft.world.level.material.Fluid> fluid, final Function<FluidState, PaperFluidData> creator) {
        Preconditions.checkState(MAP.put(fluid, creator) == null, "Duplicate mapping %s->%s", fluid, creator);
        MAP.put(fluid, creator);
    }

    public static PaperFluidData createData(final FluidState state) {
        return MAP.getOrDefault(state.getType().getClass(), PaperFluidData::new).apply(state);
    }
}
