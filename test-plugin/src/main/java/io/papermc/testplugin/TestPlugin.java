package io.papermc.testplugin;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.event.player.ChatEvent;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.type.DialogInputConfig;
import io.papermc.paper.registry.data.dialog.specialty.DialogSpecialty;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static net.kyori.adventure.text.Component.text;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        final DialogActionCallback callback = (response, audience) -> {
            audience.sendMessage(text("You responded: " + response.getFloat("key")));
        };
        final Dialog dialog = Dialog.create(b -> b.empty()
            .dialogBase(
                DialogBase.builder(text("CUSTOM dialog inlined"))
                    .inputs(List.of(DialogInput.create("key", DialogInputConfig.numberRange(text("number"), 1, 10).build())))
                    .afterAction(DialogBase.DialogAfterAction.CLOSE)
                    .build()
            ).dialogSpecialty(DialogSpecialty.confirmation(
                ActionButton.builder(text("yes")).action(DialogAction.customClick(Key.key("hello:there"), callback, ClickCallback.Options.builder().uses(1).build())).build(),
                ActionButton.builder(text("no")).build()
            ))
        );
        final TextComponent clickHere = text()
            .append(text("Here is a fun dialog to click: ", NamedTextColor.AQUA))
            .append(text("Click Here", NamedTextColor.GREEN).clickEvent(ClickEvent.showDialog(dialog)))
            .build();
        event.getPlayer().sendMessage(clickHere);
    }
}
