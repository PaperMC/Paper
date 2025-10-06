package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.entity.PaperShearable;
import io.papermc.paper.world.WeatheringCopperState;
import net.minecraft.world.level.block.WeatheringCopper;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.CopperGolem;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftCopperGolem extends CraftGolem implements CopperGolem, PaperShearable {
    public CraftCopperGolem(final CraftServer server, final net.minecraft.world.entity.animal.coppergolem.CopperGolem entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.coppergolem.CopperGolem getHandle() {
        return (net.minecraft.world.entity.animal.coppergolem.CopperGolem) this.entity;
    }

    @Override
    public WeatheringCopperState getWeatheringState() {
        return WeatheringCopperState.valueOf(this.getHandle().getWeatherState().name());
    }

    @Override
    public void setWeatheringState(final WeatheringCopperState state) {
        Preconditions.checkArgument(state != null, "state cannot be null");
        this.getHandle().setWeatherState(WeatheringCopper.WeatherState.valueOf(state.name()));
    }

    @Override
    public Oxidizing getOxidizing() {
        long value = this.getHandle().nextWeatheringTick;
        if (value == net.minecraft.world.entity.animal.coppergolem.CopperGolem.UNSET_WEATHERING_TICK) {
            return Oxidizing.unset();
        } else if (value == net.minecraft.world.entity.animal.coppergolem.CopperGolem.IGNORE_WEATHERING_TICK) {
            return Oxidizing.waxed();
        }
        if (value < 0) {
            // If someone set the entity data negative externally - the behavior is effectively the same as 0 (check is <= gameTime)
            value = 0;
        }
        return Oxidizing.atTime(value);
    }

    @Override
    public void setOxidizing(final Oxidizing oxidizing) {
        Preconditions.checkArgument(oxidizing != null, "oxidizing cannot be null");
        switch (oxidizing) {
            case Oxidizing.Waxed waxed -> this.getHandle().nextWeatheringTick = net.minecraft.world.entity.animal.coppergolem.CopperGolem.IGNORE_WEATHERING_TICK;
            case Oxidizing.Unset unset -> this.getHandle().nextWeatheringTick = net.minecraft.world.entity.animal.coppergolem.CopperGolem.UNSET_WEATHERING_TICK;
            case Oxidizing.AtTime atTime -> this.getHandle().nextWeatheringTick = atTime.time();
            default -> throw new IllegalStateException("Unexpected value: " + oxidizing);
        }
    }
}
