package io.papermc.paper.event.player;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class PaperPlayerItemCooldownEvent extends PaperPlayerItemGroupCooldownEvent implements PlayerItemCooldownEvent {

    private final Material type;

    public PaperPlayerItemCooldownEvent(final Player player, final Material type, final NamespacedKey cooldownGroup, final int cooldown) {
        super(player, cooldownGroup, cooldown);
        this.type = type;
    }

    @Override
    public Material getType() {
        return this.type;
    }
}
