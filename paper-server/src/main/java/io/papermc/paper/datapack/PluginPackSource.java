package io.papermc.paper.datapack;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackSource;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PluginPackSource implements PackSource {

    static final PackSource INSTANCE = new PluginPackSource();

    private PluginPackSource() {
    }

    @Override
    public Component decorate(final Component packDisplayName) {
        return Component.translatable("pack.nameAndSource", packDisplayName, "plugin").withStyle(ChatFormatting.GRAY);
    }

    @Override
    public boolean shouldAddAutomatically() {
        return true;
    }
}
