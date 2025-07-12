package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.body.ItemDialogBody;
import io.papermc.paper.registry.data.dialog.body.PlainMessageDialogBody;
import io.papermc.paper.registry.data.dialog.input.BooleanDialogInput;
import io.papermc.paper.registry.data.dialog.input.NumberRangeDialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import io.papermc.paper.registry.data.dialog.type.ConfirmationType;
import io.papermc.paper.registry.data.dialog.type.DialogListType;
import io.papermc.paper.registry.data.dialog.type.MultiActionType;
import io.papermc.paper.registry.data.dialog.type.NoticeType;
import io.papermc.paper.registry.data.dialog.type.ServerLinksType;
import io.papermc.paper.registry.set.RegistrySet;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * @hidden
 */
@SuppressWarnings("MissingJavadoc")
@ApiStatus.Internal
public interface DialogInstancesProvider {

    static DialogInstancesProvider instance() {
        final class Holder {
            static final Optional<DialogInstancesProvider> INSTANCE = ServiceLoader.load(DialogInstancesProvider.class, DialogInstancesProvider.class.getClassLoader()).findFirst();
        }
        return Holder.INSTANCE.orElseThrow();
    }

    DialogBase.Builder dialogBaseBuilder(Component title);

    ActionButton.Builder actionButtonBuilder(Component label);

    // actions
    DialogAction.CustomClickAction register(DialogActionCallback callback, ClickCallback.Options options);

    DialogAction.StaticAction staticAction(ClickEvent value);

    DialogAction.CommandTemplateAction commandTemplate(String template);

    DialogAction.CustomClickAction customClick(Key id, @Nullable BinaryTagHolder additions);

    // bodies
    ItemDialogBody.Builder itemDialogBodyBuilder(ItemStack itemStack);

    PlainMessageDialogBody plainMessageDialogBody(Component component);

    PlainMessageDialogBody plainMessageDialogBody(Component component, int width);

    // inputs
    BooleanDialogInput.Builder booleanBuilder(String key, Component label);

    NumberRangeDialogInput.Builder numberRangeBuilder(String key, Component label, float start, float end);

    SingleOptionDialogInput.Builder singleOptionBuilder(String key, Component label, List<SingleOptionDialogInput.OptionEntry> entries);

    SingleOptionDialogInput.OptionEntry singleOptionEntry(String id, @Nullable Component display, boolean initial);

    TextDialogInput.Builder textBuilder(String key, Component label);

    TextDialogInput.MultilineOptions multilineOptions(@Nullable Integer maxLines, @Nullable Integer height);

    // types
    ConfirmationType confirmation(ActionButton yesButton, ActionButton noButton);

    DialogListType.Builder dialogList(RegistrySet<Dialog> dialogs);

    MultiActionType.Builder multiAction(List<ActionButton> actions);

    NoticeType notice();

    NoticeType notice(ActionButton action);

    ServerLinksType serverLinks(@Nullable ActionButton exitAction, int columns, int buttonWidth);
}
