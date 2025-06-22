package io.papermc.testplugin;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.event.player.ChatEvent;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.specialty.DialogSpecialty;
import java.net.URI;
import java.util.List;
import net.kyori.adventure.text.TextComponent;
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

        final TextComponent display = text("Test Link", NamedTextColor.GOLD);
        final URI link = URI.create("https://www.google.com");
        this.getServer().getServerLinks().addLink(display, link);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void onChat(final ChatEvent event) {
        final Dialog dialog = Dialog.create(factory -> factory.empty()
            .dialogBase(DialogBase
                            .builder(text("Test Dialog"))
                            .body(List.of(DialogBody.plainMessage(text("Teleport 10 blocks up"), 100)))
                            .build()
            ).dialogSpecialty(DialogSpecialty.confirmation(
                ActionButton.create(
                    text("TELEPORT", NamedTextColor.GREEN),
                    null,
                    100,
                    DialogAction.staticAction(ClickEvent.runCommand("tp @s ~ ~10 ~"))),
                ActionButton.create(text("CANCEL", NamedTextColor.RED), null, 100, null)
            )));
        event.getPlayer().tempShowDialog(dialog);
    }
}
