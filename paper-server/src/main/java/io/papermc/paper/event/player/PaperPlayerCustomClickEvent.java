package io.papermc.paper.event.player;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.dialog.PaperDialogResponseView;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public class PaperPlayerCustomClickEvent extends PlayerCustomClickEvent {

    private final @Nullable Tag payload;
    private @Nullable BinaryTagHolder apiPayload;

    private @Nullable DialogResponseView rawResponse;

    public PaperPlayerCustomClickEvent(final Key key, final PlayerCommonConnection commonConnection, final @Nullable Tag payload) {
        super(key, commonConnection);
        this.payload = payload;
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
}
