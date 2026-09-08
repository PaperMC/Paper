package io.papermc.paper.event.player;

import com.google.common.base.Suppliers;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.event.player.CraftPlayerCommandPreprocessEvent;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.entity.Player;

public class PaperPlayerSignCommandPreprocessEvent extends CraftPlayerCommandPreprocessEvent implements PlayerSignCommandPreprocessEvent {

    private final Sign sign;
    private final Side side;

    public PaperPlayerSignCommandPreprocessEvent(final Player player, final String command, final Supplier<Set<Player>> recipients, final Sign sign, final Side side) {
        super(player, command, recipients);
        this.sign = sign;
        this.side = side;
    }

    public PaperPlayerSignCommandPreprocessEvent(
        final net.minecraft.world.entity.player.Player player, final String command, final MinecraftServer server, final SignBlockEntity sign, final boolean isFrontText
    ) {
        this(
            (Player) player.getBukkitEntity(),
            command,
            Suppliers.memoize(() -> LazyPlayerSet.makePlayerSet(server)),
            (Sign) CraftBlockStates.snapshotOf(sign),
            isFrontText ? Side.FRONT : Side.BACK
        );
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
