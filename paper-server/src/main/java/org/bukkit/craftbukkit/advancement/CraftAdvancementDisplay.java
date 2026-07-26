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
        return CraftChatMessage.fromComponent(this.handle.getTitle());
    }

    @Override
    public String getDescription() {
        return CraftChatMessage.fromComponent(this.handle.getDescription());
    }

    @Override
    public ItemStack getIcon() {
        return CraftItemStack.asBukkitCopy(this.handle.getIcon());
    }

    @Override
    public boolean shouldShowToast() {
        return this.handle.shouldShowToast();
    }

    @Override
    public boolean shouldAnnounceChat() {
        return this.handle.shouldAnnounceChat();
    }

    @Override
    public boolean isHidden() {
        return this.handle.isHidden();
    }

    @Override
    public float getX() {
        return this.handle.getX();
    }

    @Override
    public float getY() {
        return this.handle.getY();
    }

    @Override
    public AdvancementDisplayType getType() {
        return AdvancementDisplayType.values()[this.handle.getType().ordinal()];
    }
}
