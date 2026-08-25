package io.papermc.paper.event.world.border;

import org.bukkit.WorldBorder;
import org.bukkit.event.world.WorldEventNew;

public interface WorldBorderEvent extends WorldEventNew {

    WorldBorder getWorldBorder();
}
