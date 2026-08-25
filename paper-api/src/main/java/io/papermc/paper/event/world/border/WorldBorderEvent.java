package io.papermc.paper.event.world.border;

import org.bukkit.WorldBorder;
import org.bukkit.event.world.WorldEvent;

public interface WorldBorderEvent extends WorldEvent { // todo javadocs?

    WorldBorder getWorldBorder();
}
