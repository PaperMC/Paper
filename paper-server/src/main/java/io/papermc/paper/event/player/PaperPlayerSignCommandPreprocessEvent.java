package io.papermc.paper.event.player;

import java.util.Set;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.craftbukkit.event.player.CraftPlayerCommandPreprocessEvent;
import org.bukkit.entity.Player;

public class PaperPlayerSignCommandPreprocessEvent extends CraftPlayerCommandPreprocessEvent implements PlayerSignCommandPreprocessEvent {

    private final Sign sign;
    private final Side side;

    public PaperPlayerSignCommandPreprocessEvent(final Player player, final String message, final Set<Player> recipients, final Sign sign, final Side side) {
        super(player, message, recipients);
        this.sign = sign;
        this.side = side;
    }

    @Override
    public Sign getSign() {
        return this.sign;
    }

    @Override
    public Side getSide() {
        return this.side;
    }
}
