package io.papermc.paper.registry.data.dialog.action;

import java.util.Optional;
import java.util.ServiceLoader;
import net.kyori.adventure.text.event.ClickCallback;

interface DialogActionProvider {

    Optional<DialogActionProvider> INSTANCE = ServiceLoader.load(DialogActionProvider.class).findFirst();

    DialogAction.CustomClickAction register(DialogActionCallback callback, ClickCallback.Options options);
}
