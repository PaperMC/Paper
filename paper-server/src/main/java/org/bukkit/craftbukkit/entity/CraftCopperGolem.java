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
}
