package io.papermc.paper.dialog;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.nbt.CompoundTag;
import org.jspecify.annotations.Nullable;

public class PaperDialogResponseView implements DialogResponseView {

    private final CompoundTag payload;

    private PaperDialogResponseView(final CompoundTag payload) {
        this.payload = payload;
    }

    public static DialogResponseView createUnvalidatedResponse(final CompoundTag tag) {
        return new PaperDialogResponseView(tag);
    }

    @Override
    public BinaryTagHolder payload() {
        return BinaryTagHolder.encode(this.payload, PaperAdventure.NBT_CODEC);
    }

    @Override
    public @Nullable String getText(final String key) {
        if (!this.payload.contains(key)) {
            return null;
        }
        return this.payload.getString(key).orElse(null);
    }

    @Override
    public @Nullable Boolean getBoolean(final String key) {
        if (!this.payload.contains(key)) {
            return null;
        }
        return this.payload.getBoolean(key).orElse(null);
    }

    @Override
    public @Nullable Float getFloat(final String key) {
        if (!this.payload.contains(key)) {
            return null;
        }
        return this.payload.getFloat(key).orElse(null);
    }
}
