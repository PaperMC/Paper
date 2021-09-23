package io.papermc.paper.commands;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.command.CommandBlockHolder;
import net.kyori.adventure.text.Component;
import net.minecraft.world.level.BaseCommandBlock;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface PaperCommandBlockHolder extends CommandBlockHolder {

    BaseCommandBlock getCommandBlockHandle();

    @Override
    default Component lastOutput() {
        return PaperAdventure.asAdventure(this.getCommandBlockHandle().getLastOutput());
    }

    @Override
    default void lastOutput(final @Nullable Component lastOutput) {
        this.getCommandBlockHandle().setLastOutput(PaperAdventure.asVanilla(lastOutput));
    }

    @Override
    default int getSuccessCount() {
        return this.getCommandBlockHandle().getSuccessCount();
    }

    @Override
    default void setSuccessCount(final int successCount) {
        this.getCommandBlockHandle().setSuccessCount(successCount);
    }
}
