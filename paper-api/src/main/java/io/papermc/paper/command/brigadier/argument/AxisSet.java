package io.papermc.paper.command.brigadier.argument;

import com.mojang.brigadier.context.CommandContext;
import org.bukkit.Axis;
import java.util.Set;

/// A set of [Axis] intended for use in [CommandContext#getArgument(String, Class)] instead of a raw [Set].
public interface AxisSet extends Set<Axis> {

}
