package io.papermc.paper.event.player;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemType;

public class PaperPlayerItemCooldownEvent extends PaperPlayerItemGroupCooldownEvent implements PlayerItemCooldownEvent {

    private final Material type;

    public PaperPlayerItemCooldownEvent(final ServerPlayer player, final Item item, final Identifier cooldownGroup, final int cooldown) {
        super(player, cooldownGroup, cooldown);
        this.type = CraftItemType.minecraftToBukkit(item);
    }

    @Override
    public Material getType() {
        return this.type;
    }
}
