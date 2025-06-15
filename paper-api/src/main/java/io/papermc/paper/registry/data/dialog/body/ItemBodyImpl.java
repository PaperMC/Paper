package io.papermc.paper.registry.data.dialog.body;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

record ItemBodyImpl(ItemStack item, DialogBody.@Nullable PlainMessageBody description, boolean showDecorations, boolean showTooltip, int width, int height) implements DialogBody.ItemBody {
}
