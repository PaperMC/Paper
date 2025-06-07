package io.papermc.paper.registry.data.dialog.action;

import net.kyori.adventure.text.event.ClickEvent;

record StaticActionImpl(ClickEvent value) implements DialogAction.StaticAction {
}
