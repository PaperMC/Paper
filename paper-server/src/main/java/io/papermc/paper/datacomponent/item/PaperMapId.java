package io.papermc.paper.datacomponent.item;

import org.bukkit.craftbukkit.util.Handleable;

public record PaperMapId(
    net.minecraft.world.level.saveddata.maps.MapId impl
) implements MapId, Handleable<net.minecraft.world.level.saveddata.maps.MapId> {

    @Override
    public net.minecraft.world.level.saveddata.maps.MapId getHandle() {
        return this.impl;
    }

    @Override
    public int id() {
        return this.impl.id();
    }

}
