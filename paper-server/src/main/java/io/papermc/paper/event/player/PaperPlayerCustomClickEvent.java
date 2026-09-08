package io.papermc.paper.event.player;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.dialog.PaperDialogResponseView;
import io.papermc.paper.event.network.connection.PaperConnectionEvent;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPlayerCustomClickEvent extends PaperConnectionEvent implements PlayerCustomClickEvent {

    private final Key identifier;
    private final @Nullable Tag payload;
    private @Nullable BinaryTagHolder apiPayload;

    private @Nullable DialogResponseView rawResponse;

    public PaperPlayerCustomClickEvent(final Identifier identifier, final PlayerCommonConnection connection, final Optional<Tag> payload) {
        super(connection);
        this.identifier = PaperAdventure.asAdventure(identifier);
        this.payload = payload.orElse(null);
    }

    @Override
    public Key getIdentifier() {
        return this.identifier;
    }

    @Override
    public @Nullable BinaryTagHolder getTag() {
        if (this.apiPayload == null && this.payload != null) {
            this.apiPayload = BinaryTagHolder.encode(this.payload, PaperAdventure.NBT_CODEC);
        }
        return this.apiPayload;
    }

    @Override
    public @Nullable DialogResponseView getDialogResponseView() {
        if (this.payload == null || !(this.payload instanceof final CompoundTag compoundPayload)) {
            return null;
        }
        if (this.rawResponse == null) {
            this.rawResponse = PaperDialogResponseView.createUnvalidatedResponse(compoundPayload);
        }
        return this.rawResponse;
    }

    @Override
    public PlayerCommonConnection getConnection() {
        return (PlayerCommonConnection) this.connection;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerCustomClickEvent.getHandlerList();
    }
}
