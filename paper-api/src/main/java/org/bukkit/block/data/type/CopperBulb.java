package org.bukkit.block.data.type;

import org.bukkit.MinecraftExperimental;
import org.bukkit.MinecraftExperimental.Requires;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Powerable;
import org.jetbrains.annotations.ApiStatus;

@MinecraftExperimental(Requires.UPDATE_1_21)
@ApiStatus.Experimental
public interface CopperBulb extends Lightable, Powerable {
}
