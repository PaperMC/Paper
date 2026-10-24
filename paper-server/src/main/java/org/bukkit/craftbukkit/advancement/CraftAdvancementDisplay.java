package org.bukkit.craftbukkit.advancement;

import net.minecraft.advancements.DisplayInfo;
import org.bukkit.advancement.AdvancementDisplayType;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;

@Deprecated
public class CraftAdvancementDisplay implements org.bukkit.advancement.AdvancementDisplay {

    private final DisplayInfo handle;

    public CraftAdvancementDisplay(DisplayInfo handle) {
        this.handle = handle;
    }

    public DisplayInfo getHandle() {
        return this.handle;
    }

    @Override
    public String getTitle() {
        return CraftChatMessage.fromComponent(this.handle.title());
    }

    @Override
    public String getDescription() {
        return CraftChatMessage.fromComponent(this.handle.description());
    }

    @Override
    public ItemStack getIcon() {
        return CraftItemStack.asBukkitCopy(this.handle.icon());
    }

    @Override
    public boolean shouldShowToast() {
        return this.handle.showToast();
    }

    @Override
    public boolean shouldAnnounceChat() {
        return this.handle.announceToChat();
    }

    @Override
    public boolean isHidden() {
        return this.handle.hidden();
    }

    @Override
    public AdvancementDisplayType getType() {
        return AdvancementDisplayType.values()[this.handle.type().ordinal()];
    }
}
