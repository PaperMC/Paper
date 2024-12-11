package io.papermc.paper.entity;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.sound.Sound;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public interface PaperShearable extends io.papermc.paper.entity.Shearable {

    Shearable getHandle();

    @Override
    default boolean readyToBeSheared() {
        return this.getHandle().readyForShearing();
    }

    @Override
    default void shear(@NotNull Sound.Source source) {
        if (!(this.getHandle().level() instanceof final ServerLevel serverLevel)) return;
        this.getHandle().shear(serverLevel, PaperAdventure.asVanilla(source), new ItemStack(Items.SHEARS));
    }
}
