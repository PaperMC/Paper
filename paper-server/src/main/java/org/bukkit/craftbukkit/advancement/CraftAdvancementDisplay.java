package org.bukkit.craftbukkit.advancement;

import net.minecraft.advancements.AdvancementDisplay;
import org.bukkit.advancement.AdvancementDisplayType;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;

public class CraftAdvancementDisplay implements org.bukkit.advancement.AdvancementDisplay {

    private final AdvancementDisplay handle;

    public CraftAdvancementDisplay(AdvancementDisplay handle) {
        this.handle = handle;
    }

    public AdvancementDisplay getHandle() {
        return handle;
    }

    @Override
    public String getTitle() {
        return CraftChatMessage.fromComponent(handle.getTitle());
    }

    @Override
    public String getDescription() {
        return CraftChatMessage.fromComponent(handle.getDescription());
    }

    @Override
    public ItemStack getIcon() {
        return CraftItemStack.asBukkitCopy(handle.getIcon());
    }

    @Override
    public boolean shouldShowToast() {
        return handle.shouldShowToast();
    }

    @Override
    public boolean shouldAnnounceChat() {
        return handle.shouldAnnounceChat();
    }

    @Override
    public boolean isHidden() {
        return handle.isHidden();
    }

    @Override
    public float getX() {
        return handle.getX();
    }

    @Override
    public float getY() {
        return handle.getY();
    }

    @Override
    public AdvancementDisplayType getType() {
        return AdvancementDisplayType.values()[handle.getType().ordinal()];
    }
}
