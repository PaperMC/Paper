package io.papermc.paper.event.player;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.dialog.PaperDialogResponseView;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPlayerCustomClickEvent extends CraftEvent implements PlayerCustomClickEvent {

    private final Key identifier;
    private final PlayerCommonConnection commonConnection;
    private final @Nullable Tag payload;
    private @Nullable BinaryTagHolder apiPayload;

    private @Nullable DialogResponseView rawResponse;

    public PaperPlayerCustomClickEvent(final Key identifier, final PlayerCommonConnection commonConnection, final @Nullable Tag payload) {
        this.identifier = identifier;
        this.commonConnection = commonConnection;
        this.payload = payload;
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
    public PlayerCommonConnection getCommonConnection() {
        return this.commonConnection;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerCustomClickEvent.getHandlerList();
    }
}
