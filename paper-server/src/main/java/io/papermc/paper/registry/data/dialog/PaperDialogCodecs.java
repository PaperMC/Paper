package io.papermc.paper.registry.data.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.papermc.paper.adventure.AdventureCodecs;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.type.BooleanDialogInputType;
import io.papermc.paper.registry.data.dialog.input.type.DialogInputType;
import io.papermc.paper.registry.data.dialog.input.type.NumberRangeDialogInputType;
import io.papermc.paper.registry.data.dialog.input.type.SingleOptionDialogInputType;
import io.papermc.paper.registry.data.dialog.input.type.TextDialogInputType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dialog.CommonButtonData;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.dialog.action.ParsedTemplate;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

public final class PaperDialogCodecs {

    private PaperDialogCodecs() {
    }

    // dialog actions
    private static final MapCodec<DialogAction.CommandTemplateAction> COMMAND_TEMPLATE_ACTION_CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(Codec.STRING.fieldOf("template").forGetter(DialogAction.CommandTemplateAction::template))
            .apply(instance, DialogAction::commandTemplate)
    );
    private static final MapCodec<DialogAction.CustomAllAction> CUSTOM_ALL_ACTION_CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(AdventureCodecs.KEY_CODEC.fieldOf("id").forGetter(DialogAction.CustomAllAction::id), AdventureCodecs.BINARY_TAG_HOLDER_COMPOUND_CODEC.optionalFieldOf("additions").forGetter(action -> Optional.ofNullable(action.additions())))
            .apply(instance, (key, binaryTagHolder) -> DialogAction.customAll(key, binaryTagHolder.orElse(null)))
    );
    private static final Map<AdventureCodecs.ClickEventType, MapCodec<DialogAction.StaticAction>> STATIC_ACTION_CODECS = Arrays.stream(AdventureCodecs.CLICK_EVENT_TYPES.get()).collect(Collectors.toMap(Function.identity(), type -> type.codec().xmap(DialogAction::staticAction, DialogAction.StaticAction::value)));
    private static final Registry<MapCodec<? extends DialogAction>> DIALOG_ACTION_TYPES = Util.make(() -> {
        final MappedRegistry<MapCodec<? extends DialogAction>> registry = new MappedRegistry<>(ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ResourceLocation.PAPER_NAMESPACE, "dialog_action_type")), Lifecycle.experimental());
        STATIC_ACTION_CODECS.forEach((clickType, actionCodec) -> {
            Registry.register(registry, clickType.getSerializedName(), actionCodec);
        });
        Registry.register(registry, "dynamic/run_command", COMMAND_TEMPLATE_ACTION_CODEC);
        Registry.register(registry, "dynamic/custom", CUSTOM_ALL_ACTION_CODEC);
        return registry.freeze();
    });
    private static final Function<DialogAction, MapCodec<? extends DialogAction>> GET_DIALOG_ACTION_TYPE = dialogAction -> switch (dialogAction) {
        case DialogAction.CommandTemplateAction $ -> COMMAND_TEMPLATE_ACTION_CODEC;
        case DialogAction.CustomAllAction $ -> CUSTOM_ALL_ACTION_CODEC;
        case DialogAction.StaticAction action -> STATIC_ACTION_CODECS.get(AdventureCodecs.GET_CLICK_EVENT_TYPE.apply(action.value()));
    };
    private static final Codec<DialogAction> DIALOG_ACTION_CODEC = DIALOG_ACTION_TYPES.byNameCodec().dispatch(GET_DIALOG_ACTION_TYPE, Function.identity());

    // buttons
    public static final Codec<ActionButton> ACTION_BUTTON_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        AdventureCodecs.COMPONENT_CODEC.fieldOf("label").forGetter(ActionButton::label),
        AdventureCodecs.COMPONENT_CODEC.optionalFieldOf("tooltip").forGetter(button -> Optional.ofNullable(button.tooltip())),
        Dialog.WIDTH_CODEC.optionalFieldOf("width", CommonButtonData.DEFAULT_WIDTH).forGetter(ActionButton::width),
        DIALOG_ACTION_CODEC.optionalFieldOf("action").forGetter(button -> Optional.ofNullable(button.action()))
    ).apply(instance, (label, tooltip, width, action) -> ActionButton.create(label, tooltip.orElse(null), width, action.orElse(null))));

    // dialog bodies
    private static final MapCodec<DialogBody.PlainMessageBody> PLAIN_MESSAGE_BODY_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AdventureCodecs.COMPONENT_CODEC.fieldOf("contents").forGetter(DialogBody.PlainMessageBody::contents),
            Dialog.WIDTH_CODEC.optionalFieldOf("width", PlainMessage.DEFAULT_WIDTH).forGetter(DialogBody.PlainMessageBody::width)
        ).apply(instance, DialogBody::plainMessage)
    );
    private static final Codec<DialogBody.PlainMessageBody> SIMPLE_PLAIN_MESSAGE_BODY_CODEC = Codec.withAlternative(PLAIN_MESSAGE_BODY_CODEC.codec(), AdventureCodecs.COMPONENT_CODEC, component -> DialogBody.plainMessage(component, PlainMessage.DEFAULT_WIDTH));
    private static final MapCodec<DialogBody.ItemBody> ITEM_BODY_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.STRICT_CODEC.xmap(CraftItemStack::asBukkitCopy, CraftItemStack::asNMSCopy).fieldOf("item").forGetter(DialogBody.ItemBody::item),
            SIMPLE_PLAIN_MESSAGE_BODY_CODEC.optionalFieldOf("description").forGetter(body -> Optional.ofNullable(body.description())),
            Codec.BOOL.optionalFieldOf("show_decorations", true).forGetter(DialogBody.ItemBody::showDecorations),
            Codec.BOOL.optionalFieldOf("show_tooltip", true).forGetter(DialogBody.ItemBody::showTooltip),
            Dialog.WIDTH_CODEC.optionalFieldOf("width", 16).forGetter(DialogBody.ItemBody::width),
            Dialog.WIDTH_CODEC.optionalFieldOf("height", 16).forGetter(DialogBody.ItemBody::height)
        ).apply(instance, (itemStack, plainMessageBody, showDecorations, showTooltip, width, height) -> DialogBody.item(itemStack, plainMessageBody.orElse(null), showDecorations, showTooltip, width, height))
    );
    private static final Registry<MapCodec<? extends DialogBody>> DIALOG_BODY_TYPES = Util.make(() -> {
        final MappedRegistry<MapCodec<? extends DialogBody>> registry = new MappedRegistry<>(ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ResourceLocation.PAPER_NAMESPACE, "dialog_body_type")), Lifecycle.experimental());
        Registry.register(registry, "item", ITEM_BODY_CODEC);
        Registry.register(registry, "plain_message", PLAIN_MESSAGE_BODY_CODEC);
        return registry.freeze();
    });
    private static final Function<DialogBody, MapCodec<? extends DialogBody>> GET_DIALOG_BODY_TYPE = dialogAction -> switch (dialogAction) {
        case DialogBody.PlainMessageBody $ -> PLAIN_MESSAGE_BODY_CODEC;
        case DialogBody.ItemBody $ -> ITEM_BODY_CODEC;
    };
    private static final Codec<DialogBody> DIALOG_BODY_CODEC = DIALOG_BODY_TYPES.byNameCodec().dispatch(GET_DIALOG_BODY_TYPE, Function.identity());
    private static final Codec<List<DialogBody>> DIALOG_BODY_LIST_CODEC = ExtraCodecs.compactListCodec(DIALOG_BODY_CODEC);

    // input types
    private static final MapCodec<BooleanDialogInputType> BOOLEAN_DIALOG_INPUT_TYPE_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AdventureCodecs.COMPONENT_CODEC.fieldOf("label").forGetter(BooleanDialogInputType::label),
            Codec.BOOL.optionalFieldOf("initial", false).forGetter(BooleanDialogInputType::initial),
            Codec.STRING.optionalFieldOf("on_true", "true").forGetter(BooleanDialogInputType::onTrue),
            Codec.STRING.optionalFieldOf("on_false", "false").forGetter(BooleanDialogInputType::onFalse)
        ).apply(instance, DialogInputType::bool)
    );
    private static final MapCodec<NumberRangeDialogInputType> NUMBER_RANGE_INPUT_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Dialog.WIDTH_CODEC.optionalFieldOf("width", PlainMessage.DEFAULT_WIDTH).forGetter(NumberRangeDialogInputType::width),
            AdventureCodecs.COMPONENT_CODEC.fieldOf("label").forGetter(NumberRangeDialogInputType::label),
            Codec.STRING.optionalFieldOf("label_format", "options.generic_value").forGetter(NumberRangeDialogInputType::labelFormat),
            Codec.FLOAT.fieldOf("start").forGetter(NumberRangeDialogInputType::start),
            Codec.FLOAT.fieldOf("end").forGetter(NumberRangeDialogInputType::end),
            Codec.FLOAT.optionalFieldOf("initial").forGetter(type -> Optional.ofNullable(type.initial())),
            ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("step").forGetter(type -> Optional.ofNullable(type.step()))
        ).apply(instance, (width, label, labelFormat, start, end, initial, step) -> DialogInputType.numberRange(width, label, labelFormat, start, end, initial.orElse(null), step.orElse(null)))
    );
    private static final Codec<SingleOptionDialogInputType.OptionEntry> SINGLE_OPTION_DIALOG_INPUT_ENTRY_FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(SingleOptionDialogInputType.OptionEntry::id),
            AdventureCodecs.COMPONENT_CODEC.optionalFieldOf("display").forGetter(entry -> Optional.ofNullable(entry.display())),
            Codec.BOOL.optionalFieldOf("initial", false).forGetter(SingleOptionDialogInputType.OptionEntry::initial)
        ).apply(instance, (id, display, initial) -> SingleOptionDialogInputType.OptionEntry.create(id, display.orElse(null), initial))
    );
    private static final Codec<SingleOptionDialogInputType.OptionEntry> SINGLE_OPTION_DIALOG_INPUT_ENTRY_CODEC = Codec.withAlternative(
        SINGLE_OPTION_DIALOG_INPUT_ENTRY_FULL_CODEC, Codec.STRING, string -> SingleOptionDialogInputType.OptionEntry.create(string, null, false)
    );
    private static final MapCodec<SingleOptionDialogInputType> SINGLE_OPTION_DIALOG_INPUT_TYPE_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Dialog.WIDTH_CODEC.optionalFieldOf("width", PlainMessage.DEFAULT_WIDTH).forGetter(SingleOptionDialogInputType::width),
            ExtraCodecs.nonEmptyList(SINGLE_OPTION_DIALOG_INPUT_ENTRY_CODEC.listOf()).fieldOf("options").forGetter(SingleOptionDialogInputType::entries),
            AdventureCodecs.COMPONENT_CODEC.fieldOf("label").forGetter(SingleOptionDialogInputType::label),
            Codec.BOOL.optionalFieldOf("label_visible", true).forGetter(SingleOptionDialogInputType::labelVisible)
        ).apply(instance, DialogInputType::singleOption)
    );
    private static final Codec<TextDialogInputType.MultilineOptions> TEXT_DIALOG_INPUT_MULTILINE_OPTIONS_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_lines").forGetter(options -> Optional.ofNullable(options.maxLines())),
            ExtraCodecs.intRange(1, 512).optionalFieldOf("height").forGetter(options -> Optional.ofNullable(options.height()))
        ).apply(instance, (maxLines, height) -> TextDialogInputType.MultilineOptions.create(maxLines.orElse(null), height.orElse(null)))
    );
    private static final MapCodec<TextDialogInputType> TEXT_DIALOG_INPUT_TYPE_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Dialog.WIDTH_CODEC.optionalFieldOf("width", PlainMessage.DEFAULT_WIDTH).forGetter(TextDialogInputType::width),
            AdventureCodecs.COMPONENT_CODEC.fieldOf("label").forGetter(TextDialogInputType::label),
            Codec.BOOL.optionalFieldOf("label_visible", true).forGetter(TextDialogInputType::labelVisible),
            Codec.STRING.optionalFieldOf("initial", "").forGetter(TextDialogInputType::initial),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_length", 32).forGetter(TextDialogInputType::maxLength),
            TEXT_DIALOG_INPUT_MULTILINE_OPTIONS_CODEC.optionalFieldOf("multiline").forGetter(inputType -> Optional.ofNullable(inputType.multiline()))
        ).apply(instance, (width, label, labelVisible, initial, maxLength, multilineOptions) ->
            DialogInputType.text(width, label, labelVisible, initial, maxLength, multilineOptions.orElse(null))
        )
    );
    private static final Registry<MapCodec<? extends DialogInputType>> DIALOG_INPUT_TYPES = Util.make(() -> {
        final MappedRegistry<MapCodec<? extends DialogInputType>> registry = new MappedRegistry<>(ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ResourceLocation.PAPER_NAMESPACE, "dialog_input_type")), Lifecycle.experimental());
        Registry.register(registry, "boolean", BOOLEAN_DIALOG_INPUT_TYPE_MAP_CODEC);
        Registry.register(registry, "number_range", NUMBER_RANGE_INPUT_MAP_CODEC);
        Registry.register(registry, "single_option", SINGLE_OPTION_DIALOG_INPUT_TYPE_MAP_CODEC);
        Registry.register(registry, "text", TEXT_DIALOG_INPUT_TYPE_MAP_CODEC);
        return registry.freeze();
    });
    private static final Function<DialogInputType, MapCodec<? extends DialogInputType>> GET_DIALOG_INPUT_TYPE_TYPE = dialogAction -> switch (dialogAction) {
        case TextDialogInputType $ -> TEXT_DIALOG_INPUT_TYPE_MAP_CODEC;
        case SingleOptionDialogInputType $ -> SINGLE_OPTION_DIALOG_INPUT_TYPE_MAP_CODEC;
        case NumberRangeDialogInputType $ -> NUMBER_RANGE_INPUT_MAP_CODEC;
        case BooleanDialogInputType $ -> BOOLEAN_DIALOG_INPUT_TYPE_MAP_CODEC;
    };
    private static final MapCodec<DialogInputType> DIALOG_INPUT_TYPE_MAP_CODEC = DIALOG_INPUT_TYPES.byNameCodec().dispatchMap(GET_DIALOG_INPUT_TYPE_TYPE, Function.identity());
    private static final Codec<DialogInput> DIALOG_INPUT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ParsedTemplate.VARIABLE_CODEC.fieldOf("key").forGetter(DialogInput::key),
        DIALOG_INPUT_TYPE_MAP_CODEC.forGetter(DialogInput::inputType)
    ).apply(instance, DialogInput::create));

    // dialog base / common dialog data
    public static final MapCodec<DialogBase> DIALOG_BASE_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AdventureCodecs.COMPONENT_CODEC.fieldOf("title").forGetter(DialogBase::title),
            AdventureCodecs.COMPONENT_CODEC.optionalFieldOf("external_title").forGetter(base -> Optional.ofNullable(base.externalTitle())),
            Codec.BOOL.optionalFieldOf("can_close_with_escape", true).forGetter(DialogBase::canCloseWithEscape),
            Codec.BOOL.optionalFieldOf("pause", true).forGetter(DialogBase::pause),
            AdventureCodecs.indexCodec(DialogBase.DialogAfterAction.NAMES).optionalFieldOf("after_action", DialogBase.DialogAfterAction.CLOSE).forGetter(DialogBase::afterAction),
            DIALOG_BODY_LIST_CODEC.optionalFieldOf("body", List.of()).forGetter(DialogBase::body),
            DIALOG_INPUT_CODEC.listOf().optionalFieldOf("inputs", List.of()).forGetter(DialogBase::inputs)
        ).apply(instance, (title, externalTitle, canCloseWithEsc, pause, afterAction, body, inputs) -> DialogBase.create(title, externalTitle.orElse(null), canCloseWithEsc, pause, afterAction, body, inputs))
    );
}
