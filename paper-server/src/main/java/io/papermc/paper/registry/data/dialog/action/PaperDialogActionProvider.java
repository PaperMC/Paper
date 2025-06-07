package io.papermc.paper.registry.data.dialog.action;

import io.papermc.paper.adventure.providers.ClickCallbackProviderImpl;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickCallback;

public class PaperDialogActionProvider implements DialogActionProvider {

    @Override
    public DialogAction.CustomClickAction register(final Key key, final DialogActionCallback callback, final ClickCallback.Options options) {
        ClickCallbackProviderImpl.DIALOG_CLICK_MANAGER.addCallback(key, callback, options);
        return DialogAction.customClick(key, null);
    }
}
