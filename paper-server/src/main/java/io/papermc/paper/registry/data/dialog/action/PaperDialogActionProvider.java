package io.papermc.paper.registry.data.dialog.action;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.adventure.providers.ClickCallbackProviderImpl;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickCallback;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;

public class PaperDialogActionProvider implements DialogActionProvider {

    @Override
    public DialogAction.CustomClickAction register(final Key key, final DialogActionCallback callback, final ClickCallback.Options options) {
        final ClickCallbackProviderImpl.DialogClickKey callbackKey = new ClickCallbackProviderImpl.DialogClickKey(key, UUID.randomUUID());
        ClickCallbackProviderImpl.DIALOG_CLICK_MANAGER.addCallback(callbackKey, callback, options);
        final CompoundTag tag = new CompoundTag();
        tag.store(ClickCallbackProviderImpl.ID_KEY, UUIDUtil.CODEC, callbackKey.uuid());
        return DialogAction.customClick(key, BinaryTagHolder.encode(tag, PaperAdventure.NBT_CODEC));
    }
}
