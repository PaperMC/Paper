package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.adventure.providers.ClickCallbackProviderImpl;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.action.CommandTemplateActionImpl;
import io.papermc.paper.registry.data.dialog.action.CustomClickActionImpl;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.action.StaticActionImpl;
import io.papermc.paper.registry.data.dialog.body.ItemDialogBody;
import io.papermc.paper.registry.data.dialog.body.ItemDialogBodyImpl;
import io.papermc.paper.registry.data.dialog.body.PlainMessageBodyImpl;
import io.papermc.paper.registry.data.dialog.body.PlainMessageDialogBody;
import io.papermc.paper.registry.data.dialog.input.BooleanDialogInput;
import io.papermc.paper.registry.data.dialog.input.BooleanDialogInputImpl;
import io.papermc.paper.registry.data.dialog.input.NumberRangeDialogInput;
import io.papermc.paper.registry.data.dialog.input.NumberRangeDialogInputImpl;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInputImpl;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInputImpl;
import io.papermc.paper.registry.data.dialog.type.ConfirmationType;
import io.papermc.paper.registry.data.dialog.type.ConfirmationTypeImpl;
import io.papermc.paper.registry.data.dialog.type.DialogListType;
import io.papermc.paper.registry.data.dialog.type.DialogListTypeImpl;
import io.papermc.paper.registry.data.dialog.type.MultiActionType;
import io.papermc.paper.registry.data.dialog.type.MultiActionTypeImpl;
import io.papermc.paper.registry.data.dialog.type.NoticeType;
import io.papermc.paper.registry.data.dialog.type.NoticeTypeImpl;
import io.papermc.paper.registry.data.dialog.type.ServerLinksType;
import io.papermc.paper.registry.data.dialog.type.ServerLinksTypeImpl;
import io.papermc.paper.registry.set.RegistrySet;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public final class PaperDialogInstancesProvider implements DialogInstancesProvider {

    @Override
    public DialogBase.Builder dialogBaseBuilder(final Component title) {
        return new DialogBaseImpl.BuilderImpl(title);
    }

    @Override
    public ActionButton.Builder actionButtonBuilder(final Component label) {
        return new ActionButtonImpl.BuilderImpl(label);
    }

    @Override
    public DialogAction.CustomClickAction register(final DialogActionCallback callback, final ClickCallback.Options options) {
        final UUID id = ClickCallbackProviderImpl.DIALOG_CLICK_MANAGER.addCallback(UUID.randomUUID(), callback, options);
        final CompoundTag tag = new CompoundTag();
        tag.store(ClickCallbackProviderImpl.ID_KEY, UUIDUtil.CODEC, id);
        return DialogAction.customClick(ClickCallbackProviderImpl.DIALOG_CLICK_CALLBACK_KEY, BinaryTagHolder.encode(tag, PaperAdventure.NBT_CODEC));
    }

    @Override
    public DialogAction.StaticAction staticAction(final ClickEvent value) {
        return new StaticActionImpl(value);
    }

    @Override
    public DialogAction.CommandTemplateAction commandTemplate(final String template) {
        return new CommandTemplateActionImpl(template);
    }

    @Override
    public DialogAction.CustomClickAction customClick(final Key id, final @Nullable BinaryTagHolder additions) {
        return new CustomClickActionImpl(id, additions);
    }

    @Override
    public ItemDialogBody.Builder itemDialogBodyBuilder(final ItemStack itemStack) {
        return new ItemDialogBodyImpl.BuilderImpl(itemStack);
    }

    @Override
    public PlainMessageDialogBody plainMessageDialogBody(final Component component) {
        return new PlainMessageBodyImpl(component);
    }

    @Override
    public PlainMessageDialogBody plainMessageDialogBody(final Component component, final int width) {
        return new PlainMessageBodyImpl(component, width);
    }

    @Override
    public BooleanDialogInput.Builder booleanBuilder(final String key, final Component label) {
        return new BooleanDialogInputImpl.BuilderImpl(key, label);
    }

    @Override
    public NumberRangeDialogInput.Builder numberRangeBuilder(final String key, final Component label, final float start, final float end) {
        return new NumberRangeDialogInputImpl.BuilderImpl(key, label, start, end);
    }

    @Override
    public SingleOptionDialogInput.Builder singleOptionBuilder(final String key, final Component label, final List<SingleOptionDialogInput.OptionEntry> entries) {
        return new SingleOptionDialogInputImpl.BuilderImpl(key, entries, label);
    }

    @Override
    public SingleOptionDialogInput.OptionEntry singleOptionEntry(final String id, final @Nullable Component display, final boolean initial) {
        return new SingleOptionDialogInputImpl.SingleOptionEntryImpl(id, display, initial);
    }

    @Override
    public TextDialogInput.Builder textBuilder(final String key, final Component label) {
        return new TextDialogInputImpl.BuilderImpl(key, label);
    }

    @Override
    public TextDialogInput.MultilineOptions multilineOptions(final @Nullable Integer maxLines, final @Nullable Integer height) {
        return new TextDialogInputImpl.MultilineOptionsImpl(maxLines, height);
    }

    @Override
    public ConfirmationType confirmation(final ActionButton yesButton, final ActionButton noButton) {
        return new ConfirmationTypeImpl(yesButton, noButton);
    }

    @Override
    public DialogListType.Builder dialogList(final RegistrySet<Dialog> dialogs) {
        return new DialogListTypeImpl.BuilderImpl(dialogs);
    }

    @Override
    public MultiActionType.Builder multiAction(final List<ActionButton> actions) {
        return new MultiActionTypeImpl.BuilderImpl(actions);
    }

    @Override
    public NoticeType notice() {
        return new NoticeTypeImpl();
    }

    @Override
    public NoticeType notice(final ActionButton action) {
        return new NoticeTypeImpl(action);
    }

    @Override
    public ServerLinksType serverLinks(final @Nullable ActionButton exitAction, final int columns, final int buttonWidth) {
        return new ServerLinksTypeImpl(exitAction, columns, buttonWidth);
    }
}
