package io.papermc.paper.advancement;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record PaperAdvancementDisplay(DisplayInfo handle) implements AdvancementDisplay {

    @Override
    public @NotNull Frame frame() {
        return asPaperFrame(this.handle.getType());
    }

    @Override
    public @NotNull Component title() {
        return PaperAdventure.asAdventure(this.handle.getTitle());
    }

    @Override
    public @NotNull Component description() {
        return PaperAdventure.asAdventure(this.handle.getDescription());
    }

    @Override
    public @NotNull ItemStack icon() {
        return CraftItemStack.asBukkitCopy(this.handle.getIcon());
    }

    @Override
    public boolean doesShowToast() {
        return this.handle.shouldShowToast();
    }

    @Override
    public boolean doesAnnounceToChat() {
        return this.handle.shouldAnnounceChat();
    }

    @Override
    public boolean isHidden() {
        return this.handle.isHidden();
    }

    @Override
    public @Nullable NamespacedKey backgroundPath() {
        return this.handle.getBackground().map(asset -> CraftNamespacedKey.fromMinecraft(asset.id())).orElse(null);
    }

    @Override
    public @NotNull Component displayName() {
        return PaperAdventure.asAdventure(Advancement.decorateName(java.util.Objects.requireNonNull(this.handle, "cannot build display name for null handle, invalid state")));
    }

    public static @NotNull Frame asPaperFrame(final @NotNull AdvancementType frameType) {
        return switch (frameType) {
            case TASK -> Frame.TASK;
            case CHALLENGE -> Frame.CHALLENGE;
            case GOAL -> Frame.GOAL;
        };
    }
}
