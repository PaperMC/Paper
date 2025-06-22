package io.papermc.paper.registry.data.dialog.action;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import org.jspecify.annotations.Nullable;

record CustomAllActionImpl(Key id, @Nullable BinaryTagHolder additions) implements DialogAction.CustomAllAction {
}
