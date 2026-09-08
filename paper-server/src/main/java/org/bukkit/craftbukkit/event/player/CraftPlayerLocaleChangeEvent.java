package org.bukkit.craftbukkit.event.player;

import java.util.Locale;
import java.util.Objects;
import net.kyori.adventure.translation.Translator;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

public class CraftPlayerLocaleChangeEvent extends CraftPlayerEvent implements PlayerLocaleChangeEvent {

    private final String locale;
    private final Locale adventure$locale;

    public CraftPlayerLocaleChangeEvent(final Player player, final String locale) {
        super(player);
        this.locale = locale;
        this.adventure$locale = Objects.requireNonNullElse(Translator.parseLocale(locale), Locale.US);
    }

    public CraftPlayerLocaleChangeEvent(final ServerPlayer player, final String locale) {
        this(player.getBukkitEntity(), locale);
    }

    @Override
    @Deprecated
    public String getLocale() {
        return this.locale;
    }

    @Override
    public Locale locale() {
        return this.adventure$locale;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerLocaleChangeEvent.getHandlerList();
    }
}
