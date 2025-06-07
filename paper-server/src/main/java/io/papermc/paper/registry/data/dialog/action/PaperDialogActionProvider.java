package io.papermc.paper.registry.data.dialog.action;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.adventure.providers.ClickCallbackProviderImpl;
import java.util.UUID;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickCallback;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;

public class PaperDialogActionProvider implements DialogActionProvider {

    @Override
    public DialogAction.CustomClickAction register(final DialogActionCallback callback, final ClickCallback.Options options) {
        final UUID id = ClickCallbackProviderImpl.DIALOG_CLICK_MANAGER.addCallback(UUID.randomUUID(), callback, options);
        final CompoundTag tag = new CompoundTag();
        tag.store(ClickCallbackProviderImpl.ID_KEY, UUIDUtil.CODEC, id);
        return DialogAction.customClick(ClickCallbackProviderImpl.DIALOG_CLICK_CALLBACK_KEY, BinaryTagHolder.encode(tag, PaperAdventure.NBT_CODEC));
    }
}
