package io.papermc.paper.event.world;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftGameRule;
import org.bukkit.craftbukkit.event.world.CraftWorldEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperWorldGameRuleChangeEvent<T> extends CraftWorldEvent implements WorldGameRuleChangeEvent {

    private final @Nullable CommandSender commandSender;
    private final GameRule<T> rule;
    private T value;
    private boolean cancelled;

    private org.bukkit.@Nullable GameRule<?> apiRule;
    private @Nullable String valueStr;

    public PaperWorldGameRuleChangeEvent(final Level level, final @Nullable CommandSender commandSender, final GameRule<T> rule, final T value) {
        super(level.getWorld());
        this.commandSender = commandSender;
        this.rule = rule;
        this.value = value;
    }

    @Override
    public @Nullable CommandSender getCommandSender() {
        return this.commandSender;
    }

    @Override
    public org.bukkit.GameRule<?> getGameRule() {
        if (this.apiRule == null) {
            this.apiRule = CraftGameRule.minecraftToBukkit(this.rule);
        }
        return this.apiRule;
    }

    @Override
    public String getValue() {
        if (this.valueStr == null) {
            this.valueStr = this.rule.serialize(this.value);
        }
        return this.valueStr;
    }

    @Override
    public void setValue(final String value) {
        this.value = this.rule.deserialize(value).getOrThrow(error -> CraftGameRule.INVALID_VALUE.apply(value, error));
        this.valueStr = value;
    }

    public T newValue() {
        return this.value;
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
