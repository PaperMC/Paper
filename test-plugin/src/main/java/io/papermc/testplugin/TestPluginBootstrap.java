package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.type.DialogInputType;
import io.papermc.paper.registry.data.dialog.specialty.DialogSpecialty;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.DialogKeys;
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
            event.builder().dialogBase(DialogBase.create(
                text("New Better Title", NamedTextColor.LIGHT_PURPLE),
                oldBase.externalTitle(),
                oldBase.canCloseWithEscape(),
                oldBase.pause(),
                oldBase.afterAction(),
                oldBase.body(),
                oldBase.inputs()
            ));
        }).filter(DialogKeys.SERVER_LINKS));

        manager.registerEventHandler(RegistryEvents.DIALOG.freeze(), event -> {
            event.registry().register(DialogKeys.create(Key.key("mm:test")), builder -> {
                final List<DialogBody> body = List.of(DialogBody.plainMessage(text("Select relative coordinates", NamedTextColor.GREEN), 100));
                final List<DialogInput> inputs = List.of(DialogInput.create("x", DialogInputType.numberRange(100, text("X Coordinate", NamedTextColor.YELLOW), "options.generic_value", -1000f, 1000f, 0f, 1f)),
                    DialogInput.create("y", DialogInputType.numberRange(100, text("Y Coordinate", NamedTextColor.YELLOW), "%s: ~%s", -1000f, 1000f, 0f, 1f)),
                    DialogInput.create("z", DialogInputType.numberRange(100, text("Z Coordinate", NamedTextColor.YELLOW), "%s: ~%s", -1000f, 1000f, 0f, 1f))
                );
                builder.dialogBase(DialogBase.create(text("Teleport somewhere COOL", NamedTextColor.YELLOW), null, true, false, DialogBase.DialogAfterAction.CLOSE, body, inputs));
                final ActionButton cancel = ActionButton.create(text("Cancel", NamedTextColor.RED), null, 50, null);
                final ActionButton teleport = ActionButton.create(text("Teleport", NamedTextColor.GREEN), null, 50, DialogAction.commandTemplate("tp ~$(x) ~$(y) ~$(z)"));
                builder.dialogSpecialty(DialogSpecialty.confirmation(teleport, cancel));
            });
        });
    }

}
