package io.papermc.paper.event.world;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftGameRule;
import org.bukkit.craftbukkit.event.world.CraftWorldEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperWorldGameRuleChangeEvent extends CraftWorldEvent implements WorldGameRuleChangeEvent {

    private final @Nullable CommandSender commandSender;
    protected final GameRule<?> gameRule;
    private String value;
    private boolean cancelled;

    public PaperWorldGameRuleChangeEvent(final World world, final @Nullable CommandSender commandSender, final GameRule<?> gameRule, final String value) {
        super(world);
        this.commandSender = commandSender;
        this.gameRule = gameRule;
        this.value = value;
    }

    @Override
    public @Nullable CommandSender getCommandSender() {
        return this.commandSender;
    }

    @Override
    public GameRule<?> getGameRule() {
        return this.gameRule;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(final String value) {
        ((CraftGameRule<?>) this.gameRule).getHandle().deserialize(value).ifError(error -> {
            throw CraftGameRule.INVALID_VALUE.apply(value, error);
        });
        this.value = value;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return WorldGameRuleChangeEvent.getHandlerList();
    }
}
