package io.papermc.paper.registry.data.dialog.action;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jspecify.annotations.Nullable;

public record CustomClickActionImpl(Key id, @Nullable BinaryTagHolder additions) implements DialogAction.CustomClickAction {

    public CustomClickActionImpl {
        if (additions != null) {
            try {
                final Tag tag = additions.get(PaperAdventure.NBT_CODEC);
                Preconditions.checkArgument(tag instanceof CompoundTag, "Additions must be a compound tag");
            } catch (final CommandSyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
