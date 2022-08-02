package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.Message;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.ComponentUtils;
import org.jetbrains.annotations.NotNull;

public final class MessageComponentSerializerImpl implements MessageComponentSerializer {

    @Override
    public @NotNull Component deserialize(@NotNull Message input) {
        return PaperAdventure.asAdventure(ComponentUtils.fromMessage(input));
    }

    @Override
    public @NotNull Message serialize(@NotNull Component component) {
        return PaperAdventure.asVanilla(component);
    }
}
