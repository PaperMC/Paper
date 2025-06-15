package io.papermc.testplugin;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.type.DialogInputType;
import io.papermc.paper.registry.data.dialog.specialty.DialogSpecialty;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.DialogKeys;
import io.papermc.paper.registry.set.RegistryValueSetBuilder;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // io.papermc.testplugin.brigtests.Registration.registerViaBootstrap(context);

        final LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();
        manager.registerEventHandler(RegistryEvents.DIALOG.entryAdd().newHandler(event -> {
            final DialogBase oldBase = event.builder().dialogBase();
            event.builder().dialogBase(DialogBase.create(text("New Better Title", NamedTextColor.LIGHT_PURPLE), oldBase.externalTitle(), oldBase.canCloseWithEscape(), oldBase.pause(), oldBase.afterAction(), oldBase.body(), oldBase.inputs()));
        }).filter(DialogKeys.SERVER_LINKS));

        final TypedKey<Dialog> listDialog = DialogKeys.create(Key.key("mm:list"));
        manager.registerEventHandler(RegistryEvents.DIALOG.compose(), event -> {
            event.registry().register(listDialog, listBuilder -> {
                final RegistryValueSetBuilder<Dialog, DialogRegistryEntry.Builder> valueSetBuilder = listBuilder.registryValueSetBuilder();
                valueSetBuilder.add(b -> {
                    final DialogRegistryEntry.Builder builder = b.empty();
                    final List<DialogBody> body = List.of(DialogBody.plainMessage(text("Select relative coordinates", NamedTextColor.GREEN), 100));
                    final List<DialogInput> inputs = List.of(DialogInput.create("x", DialogInputType.numberRange(100, text("X Coordinate", NamedTextColor.YELLOW), "options.generic_value", -1000f, 1000f, 0f, 1f)), DialogInput.create("y", DialogInputType.numberRange(100, text("Y Coordinate", NamedTextColor.YELLOW), "%s: ~%s", -1000f, 1000f, 0f, 1f)), DialogInput.create("z", DialogInputType.numberRange(100, text("Z Coordinate", NamedTextColor.YELLOW), "%s: ~%s", -1000f, 1000f, 0f, 1f)));
                    builder.dialogBase(DialogBase.create(text("Teleport somewhere COOL", NamedTextColor.YELLOW), null, true, false, DialogBase.DialogAfterAction.CLOSE, body, inputs));
                    final ActionButton cancel = ActionButton.create(text("Cancel", NamedTextColor.RED), null, 50, null);
                    final ActionButton teleport = ActionButton.create(text("Teleport", NamedTextColor.GREEN), null, 50, DialogAction.commandTemplate("tp ~$(x) ~$(y) ~$(z)"));
                    builder.dialogSpecialty(DialogSpecialty.confirmation(teleport, cancel));
                }).add(b -> {
                    final DialogRegistryEntry.Builder builder = b.empty();
                    final List<DialogBody> body = List.of(DialogBody.plainMessage(text("Enter a message to say", NamedTextColor.GREEN), 100));
                    final List<DialogInput> inputs = List.of(DialogInput.create("msg", DialogInputType.text(100, text("Message"), true, "", 1000, null)));
                    builder.dialogBase(DialogBase.create(text("Send a message"), null, true, false, DialogBase.DialogAfterAction.CLOSE, body, inputs));
                    final ActionButton cancel = ActionButton.create(text("Cancel", NamedTextColor.RED), null, 50, null);
                    final ActionButton say = ActionButton.create(text("Say the thing!", NamedTextColor.GREEN), null, 50, DialogAction.commandTemplate("say $(msg)")); // can't actually run this command cause signed args
                    builder.dialogSpecialty(DialogSpecialty.confirmation(say, cancel));
                });
                listBuilder.dialogBase(DialogBase.create(text("Select an epic dialog", NamedTextColor.RED), null, true, false, DialogBase.DialogAfterAction.CLOSE, Collections.emptyList(), Collections.emptyList()));
                listBuilder.dialogSpecialty(DialogSpecialty.dialogList(valueSetBuilder.build(), null, 1, 200));
            });
        });
    }

}
