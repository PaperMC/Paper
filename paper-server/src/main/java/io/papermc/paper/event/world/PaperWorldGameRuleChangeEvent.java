package io.papermc.paper.event.world;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftGameRule;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PaperWorldGameRuleChangeEvent extends WorldGameRuleChangeEvent {

    public PaperWorldGameRuleChangeEvent(final World world, final @Nullable CommandSender commandSender, final GameRule<?> gameRule, final String value) {
        super(world, commandSender, gameRule, value);
    }

    @Override
    public void setValue(final String value) {
        ((CraftGameRule<?>) this.gameRule).getHandle().deserialize(value).ifError(error -> {
            throw CraftGameRule.INVALID_VALUE.apply(value, error);
        });
        super.setValue(value);
    }
}
