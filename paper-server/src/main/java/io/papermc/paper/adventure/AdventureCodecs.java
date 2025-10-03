package io.papermc.paper.adventure;

import com.google.common.collect.ImmutableListMultimap;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.PaperDialogCodecs;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import net.kyori.adventure.util.Index;
import net.minecraft.commands.arguments.selector.SelectorPattern;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.contents.KeybindContents;
import net.minecraft.network.chat.contents.ScoreContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.intellij.lang.annotations.Subst;

import static com.mojang.serialization.Codec.recursive;
import static com.mojang.serialization.codecs.RecordCodecBuilder.create;
import static com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec;
import static java.util.function.Function.identity;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.TranslationArgument.bool;
import static net.kyori.adventure.text.TranslationArgument.component;
import static net.kyori.adventure.text.TranslationArgument.numeric;

@DefaultQualifier(NonNull.class)
public final class AdventureCodecs {
    public static final Codec<BinaryTagHolder> BINARY_TAG_HOLDER_CODEC = ExtraCodecs.NBT.flatComapMap(tag -> BinaryTagHolder.encode(tag, PaperAdventure.NBT_CODEC), api -> {
        try {
            final Tag tag = api.get(PaperAdventure.NBT_CODEC);
            return DataResult.success(tag);
        } catch (CommandSyntaxException e) {
            return DataResult.error(e::getMessage);
        }
    });
    public static final Codec<Component> COMPONENT_CODEC = recursive("adventure Component", AdventureCodecs::createCodec);
    public static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_COMPONENT_CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(COMPONENT_CODEC);

    static final Codec<ShadowColor> SHADOW_COLOR_CODEC = ExtraCodecs.ARGB_COLOR_CODEC.xmap(ShadowColor::shadowColor, ShadowColor::value);

    static final Codec<TextColor> TEXT_COLOR_CODEC = Codec.STRING.comapFlatMap(s -> {
        if (s.startsWith("#")) {
            @Nullable TextColor value = TextColor.fromHexString(s);
            return value != null ? DataResult.success(value) : DataResult.error(() -> "Cannot convert " + s + " to adventure TextColor");
        } else {
            final @Nullable NamedTextColor value = NamedTextColor.NAMES.value(s);
            return value != null ? DataResult.success(value) : DataResult.error(() -> "Cannot convert " + s + " to adventure NamedTextColor");
        }
    }, textColor -> {
        if (textColor instanceof NamedTextColor named) {
            return NamedTextColor.NAMES.keyOrThrow(named);
        } else {
            return textColor.asHexString();
        }
    });

    public static final Codec<Key> KEY_CODEC = Codec.STRING.comapFlatMap(s -> {
        return Key.parseable(s) ? DataResult.success(Key.key(s)) : DataResult.error(() -> "Cannot convert " + s + " to adventure Key");
    }, Key::asString);

    static final Function<ClickEvent, String> TEXT_PAYLOAD_EXTRACTOR = a -> ((ClickEvent.Payload.Text) a.payload()).value();

    /*
     * Click
     */
    static final MapCodec<ClickEvent> OPEN_URL_CODEC = mapCodec((instance) -> instance.group(
        ExtraCodecs.UNTRUSTED_URI.fieldOf("url").forGetter(a -> {
                final String url = ((ClickEvent.Payload.Text) a.payload()).value();
                return URI.create(!url.contains("://") ? "https://" + url : url);
            }
        )
    ).apply(instance, (url) -> ClickEvent.openUrl(url.toString())));
    static final MapCodec<ClickEvent> OPEN_FILE_CODEC = mapCodec((instance) -> instance.group(
        Codec.STRING.fieldOf("path").forGetter(TEXT_PAYLOAD_EXTRACTOR)
    ).apply(instance, ClickEvent::openFile));
    static final MapCodec<ClickEvent> RUN_COMMAND_CODEC = mapCodec((instance) -> instance.group(
        ExtraCodecs.CHAT_STRING.fieldOf("command").forGetter(TEXT_PAYLOAD_EXTRACTOR)
    ).apply(instance, ClickEvent::runCommand));
    static final MapCodec<ClickEvent> SUGGEST_COMMAND_CODEC = mapCodec((instance) -> instance.group(
        ExtraCodecs.CHAT_STRING.fieldOf("command").forGetter(TEXT_PAYLOAD_EXTRACTOR)
    ).apply(instance, ClickEvent::suggestCommand));
    static final MapCodec<ClickEvent> CHANGE_PAGE_CODEC = mapCodec((instance) -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("page").forGetter(a -> ((ClickEvent.Payload.Int) a.payload()).integer())
    ).apply(instance, ClickEvent::changePage));
    static final MapCodec<ClickEvent> COPY_TO_CLIPBOARD_CODEC = mapCodec((instance) -> instance.group(
        Codec.STRING.fieldOf("value").forGetter(TEXT_PAYLOAD_EXTRACTOR)
    ).apply(instance, ClickEvent::copyToClipboard));
    // needs to be lazy loaded due to depending on PaperDialogCodecs static init
    static final MapCodec<ClickEvent> SHOW_DIALOG_CODEC = MapCodec.recursive("show_dialog", ignored -> mapCodec((instance) -> instance.group(
        PaperDialogCodecs.DIALOG_CODEC.fieldOf("dialog").forGetter(a -> (Dialog) ((ClickEvent.Payload.Dialog) a.payload()).dialog())
    ).apply(instance, ClickEvent::showDialog)));
    static final MapCodec<ClickEvent> CUSTOM_CODEC = mapCodec((instance) -> instance.group(
        KEY_CODEC.fieldOf("id").forGetter(a -> ((ClickEvent.Payload.Custom) a.payload()).key()),
        BINARY_TAG_HOLDER_CODEC.fieldOf("payload").forGetter(a -> ((ClickEvent.Payload.Custom) a.payload()).nbt())
    ).apply(instance, ClickEvent::custom));

    static final ClickEventType OPEN_URL_CLICK_EVENT_TYPE = new ClickEventType(OPEN_URL_CODEC, "open_url");
    static final ClickEventType OPEN_FILE_CLICK_EVENT_TYPE = new ClickEventType(OPEN_FILE_CODEC, "open_file");
    static final ClickEventType RUN_COMMAND_CLICK_EVENT_TYPE = new ClickEventType(RUN_COMMAND_CODEC, "run_command");
    static final ClickEventType SUGGEST_COMMAND_CLICK_EVENT_TYPE = new ClickEventType(SUGGEST_COMMAND_CODEC, "suggest_command");
    static final ClickEventType CHANGE_PAGE_CLICK_EVENT_TYPE = new ClickEventType(CHANGE_PAGE_CODEC, "change_page");
    static final ClickEventType COPY_TO_CLIPBOARD_CLICK_EVENT_TYPE = new ClickEventType(COPY_TO_CLIPBOARD_CODEC, "copy_to_clipboard");
    static final ClickEventType SHOW_DIALOG_CLICK_EVENT_TYPE = new ClickEventType(SHOW_DIALOG_CODEC, "show_dialog");
    static final ClickEventType CUSTOM_CLICK_EVENT_TYPE = new ClickEventType(CUSTOM_CODEC, "custom");
    public static final Supplier<ClickEventType[]> CLICK_EVENT_TYPES = () -> new ClickEventType[]{OPEN_URL_CLICK_EVENT_TYPE, OPEN_FILE_CLICK_EVENT_TYPE, RUN_COMMAND_CLICK_EVENT_TYPE, SUGGEST_COMMAND_CLICK_EVENT_TYPE, CHANGE_PAGE_CLICK_EVENT_TYPE, COPY_TO_CLIPBOARD_CLICK_EVENT_TYPE, SHOW_DIALOG_CLICK_EVENT_TYPE, CUSTOM_CLICK_EVENT_TYPE};
    static final Codec<ClickEventType> CLICK_EVENT_TYPE_CODEC = StringRepresentable.fromValues(CLICK_EVENT_TYPES);

    public record ClickEventType(MapCodec<ClickEvent> codec, String id) implements StringRepresentable {
        @Override
        public String getSerializedName() {
            return this.id;
        }
    }

    public static final Function<ClickEvent, ClickEventType> GET_CLICK_EVENT_TYPE =
        he -> switch (he.action()) {
            case OPEN_URL -> OPEN_URL_CLICK_EVENT_TYPE;
            case OPEN_FILE -> OPEN_FILE_CLICK_EVENT_TYPE;
            case RUN_COMMAND -> RUN_COMMAND_CLICK_EVENT_TYPE;
            case SUGGEST_COMMAND -> SUGGEST_COMMAND_CLICK_EVENT_TYPE;
            case CHANGE_PAGE -> CHANGE_PAGE_CLICK_EVENT_TYPE;
            case COPY_TO_CLIPBOARD -> COPY_TO_CLIPBOARD_CLICK_EVENT_TYPE;
            case SHOW_DIALOG -> SHOW_DIALOG_CLICK_EVENT_TYPE;
            case CUSTOM -> CUSTOM_CLICK_EVENT_TYPE;
        };

    static final Codec<ClickEvent> CLICK_EVENT_CODEC = CLICK_EVENT_TYPE_CODEC.dispatch("action", GET_CLICK_EVENT_TYPE, ClickEventType::codec);

    /*
     * HOVER
     */
    static final MapCodec<HoverEvent<Component>> SHOW_TEXT_CODEC = mapCodec((instance) -> instance.group(
        COMPONENT_CODEC.fieldOf("value").forGetter(HoverEvent::value)
    ).apply(instance, HoverEvent::showText));

    static final MapCodec<HoverEvent<HoverEvent.ShowEntity>> SHOW_ENTITY_CODEC = mapCodec((instance) -> instance.group(
        KEY_CODEC.fieldOf("id").forGetter(a -> a.value().type()),
        UUIDUtil.LENIENT_CODEC.fieldOf("uuid").forGetter(a -> a.value().id()),
        COMPONENT_CODEC.lenientOptionalFieldOf("name").forGetter(a -> Optional.ofNullable(a.value().name()))
    ).apply(instance, (key, uuid, component) -> HoverEvent.showEntity(key, uuid, component.orElse(null))));

    static final MapCodec<HoverEvent<HoverEvent.ShowItem>> SHOW_ITEM_CODEC = net.minecraft.network.chat.HoverEvent.ShowItem.CODEC.xmap(internal -> {
        @Subst("key") final String typeKey = internal.item().getItemHolder().unwrapKey().orElseThrow().location().toString();
        return HoverEvent.showItem(Key.key(typeKey), internal.item().getCount(), PaperAdventure.asAdventure(internal.item().getComponentsPatch()));
    }, adventure -> {
        final Item itemType = BuiltInRegistries.ITEM.getValue(PaperAdventure.asVanilla(adventure.value().item()));
        final Map<Key, DataComponentValue> dataComponentsMap = adventure.value().dataComponents();
        final ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.wrapAsHolder(itemType), adventure.value().count(), PaperAdventure.asVanilla(dataComponentsMap));
        return new net.minecraft.network.chat.HoverEvent.ShowItem(stack);
    });

    static final HoverEventType<HoverEvent.ShowEntity> SHOW_ENTITY_HOVER_EVENT_TYPE = new HoverEventType<>(SHOW_ENTITY_CODEC, "show_entity");
    static final HoverEventType<HoverEvent.ShowItem> SHOW_ITEM_HOVER_EVENT_TYPE = new HoverEventType<>(SHOW_ITEM_CODEC, "show_item");
    static final HoverEventType<Component> SHOW_TEXT_HOVER_EVENT_TYPE = new HoverEventType<>(SHOW_TEXT_CODEC, "show_text");
    static final Codec<HoverEventType<?>> HOVER_EVENT_TYPE_CODEC = StringRepresentable.fromValues(() -> new HoverEventType<?>[]{SHOW_ENTITY_HOVER_EVENT_TYPE, SHOW_ITEM_HOVER_EVENT_TYPE, SHOW_TEXT_HOVER_EVENT_TYPE});

    record HoverEventType<V>(MapCodec<HoverEvent<V>> codec, String id) implements StringRepresentable {
        @Override
        public String getSerializedName() {
            return this.id;
        }
    }

    private static final Function<HoverEvent<?>, HoverEventType<?>> GET_HOVER_EVENT_TYPE = he -> {
        if (he.action() == HoverEvent.Action.SHOW_ENTITY) {
            return SHOW_ENTITY_HOVER_EVENT_TYPE;
        } else if (he.action() == HoverEvent.Action.SHOW_ITEM) {
            return SHOW_ITEM_HOVER_EVENT_TYPE;
        } else if (he.action() == HoverEvent.Action.SHOW_TEXT) {
            return SHOW_TEXT_HOVER_EVENT_TYPE;
        } else {
            throw new IllegalStateException();
        }
    };

    static final Codec<HoverEvent<?>> HOVER_EVENT_CODEC = HOVER_EVENT_TYPE_CODEC.dispatch("action", GET_HOVER_EVENT_TYPE, HoverEventType::codec);

    /*
     * Style
     */
    public static final MapCodec<Style> STYLE_MAP_CODEC = mapCodec((instance) -> {
        return instance.group(
            TEXT_COLOR_CODEC.optionalFieldOf("color").forGetter(nullableGetter(Style::color)),
            SHADOW_COLOR_CODEC.optionalFieldOf("shadow_color").forGetter(nullableGetter(Style::shadowColor)),
            Codec.BOOL.optionalFieldOf("bold").forGetter(decorationGetter(TextDecoration.BOLD)),
            Codec.BOOL.optionalFieldOf("italic").forGetter(decorationGetter(TextDecoration.ITALIC)),
            Codec.BOOL.optionalFieldOf("underlined").forGetter(decorationGetter(TextDecoration.UNDERLINED)),
            Codec.BOOL.optionalFieldOf("strikethrough").forGetter(decorationGetter(TextDecoration.STRIKETHROUGH)),
            Codec.BOOL.optionalFieldOf("obfuscated").forGetter(decorationGetter(TextDecoration.OBFUSCATED)),
            CLICK_EVENT_CODEC.optionalFieldOf("click_event").forGetter(nullableGetter(Style::clickEvent)),
            HOVER_EVENT_CODEC.optionalFieldOf("hover_event").forGetter(nullableGetter(Style::hoverEvent)),
            Codec.STRING.optionalFieldOf("insertion").forGetter(nullableGetter(Style::insertion)),
            KEY_CODEC.optionalFieldOf("font").forGetter(nullableGetter(Style::font))
        ).apply(instance, (textColor, shadowColor, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font) -> {
            return Style.style(builder -> {
                textColor.ifPresent(builder::color);
                shadowColor.ifPresent(builder::shadowColor);
                bold.ifPresent(styleBooleanConsumer(builder, TextDecoration.BOLD));
                italic.ifPresent(styleBooleanConsumer(builder, TextDecoration.ITALIC));
                underlined.ifPresent(styleBooleanConsumer(builder, TextDecoration.UNDERLINED));
                strikethrough.ifPresent(styleBooleanConsumer(builder, TextDecoration.STRIKETHROUGH));
                obfuscated.ifPresent(styleBooleanConsumer(builder, TextDecoration.OBFUSCATED));
                clickEvent.ifPresent(builder::clickEvent);
                hoverEvent.ifPresent(builder::hoverEvent);
                insertion.ifPresent(builder::insertion);
                font.ifPresent(builder::font);
            });
        });
    });

    /*
     * Misc
     */
    static Consumer<Boolean> styleBooleanConsumer(final Style.Builder builder, final TextDecoration decoration) {
        return b -> builder.decoration(decoration, b);
    }

    static Function<Style, Optional<Boolean>> decorationGetter(final TextDecoration decoration) {
        return style -> Optional.ofNullable(style.decoration(decoration) == TextDecoration.State.NOT_SET ? null : style.decoration(decoration) == TextDecoration.State.TRUE);
    }

    static <R, T> Function<R, Optional<T>> nullableGetter(final Function<R, @Nullable T> getter) {
        return style -> Optional.ofNullable(getter.apply(style));
    }

    static final MapCodec<TextComponent> TEXT_COMPONENT_MAP_CODEC = mapCodec((instance) -> {
        return instance.group(Codec.STRING.fieldOf("text").forGetter(TextComponent::content)).apply(instance, Component::text);
    });
    static final Codec<Object> PRIMITIVE_ARG_CODEC = ExtraCodecs.JAVA.validate(TranslatableContents::filterAllowedArguments);
    static final Codec<TranslationArgument> ARG_CODEC = Codec.either(PRIMITIVE_ARG_CODEC, COMPONENT_CODEC).flatXmap((primitiveOrComponent) -> {
        return primitiveOrComponent.map(o -> {
            final TranslationArgument arg;
            if (o instanceof String s) {
                arg = component(text(s));
            } else if (o instanceof Boolean bool) {
                arg = bool(bool);
            } else if (o instanceof Number num) {
                arg = numeric(num);
            } else {
                return DataResult.error(() -> o + " is not a valid translation argument primitive");
            }
            return DataResult.success(arg);
        }, component -> DataResult.success(component(component)));
    }, translationArgument -> {
        if (translationArgument.value() instanceof Number || translationArgument.value() instanceof Boolean) {
            return DataResult.success(Either.left(translationArgument.value()));
        }
        final Component component = translationArgument.asComponent();
        final @Nullable String collapsed = tryCollapseToString(component);
        if (collapsed != null) {
            return DataResult.success(Either.left(collapsed)); // attempt to collapse all text components to strings
        }
        return DataResult.success(Either.right(component));
    });
    static final MapCodec<TranslatableComponent> TRANSLATABLE_COMPONENT_MAP_CODEC = mapCodec((instance) -> {
        return instance.group(
            Codec.STRING.fieldOf("translate").forGetter(TranslatableComponent::key),
            Codec.STRING.lenientOptionalFieldOf("fallback").forGetter(nullableGetter(TranslatableComponent::fallback)),
            ARG_CODEC.listOf().optionalFieldOf("with").forGetter(c -> c.arguments().isEmpty() ? Optional.empty() : Optional.of(c.arguments()))
        ).apply(instance, (key, fallback, components) -> {
            return Component.translatable(key, components.orElse(Collections.emptyList())).fallback(fallback.orElse(null));
        });
    });

    static final MapCodec<KeybindComponent> KEYBIND_COMPONENT_MAP_CODEC = KeybindContents.MAP_CODEC.xmap(k -> Component.keybind(k.getName()), k -> new KeybindContents(k.keybind()));

    static final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends ObjectComponent>> OBJECT_CONTENTS_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    static final MapCodec<ObjectComponent> SPRITE_OBJECT_CODEC = mapCodec(instance -> instance.group(
            KEY_CODEC.optionalFieldOf("atlas", SpriteObjectContents.DEFAULT_ATLAS).forGetter(obj -> ((SpriteObjectContents) obj.contents()).atlas()),
            KEY_CODEC.fieldOf("sprite").forGetter(obj -> ((SpriteObjectContents) obj.contents()).sprite())
        )
        .apply(instance, (atlas, sprite) -> {
            return Component.object(ObjectContents.sprite(atlas, sprite));
        }));
    static final Codec<ObjectComponent> PLAYER_OBJECT_PLAYER_CODEC = create(instance -> instance.group(
        Codec.mapEither(ExtraCodecs.STORED_GAME_PROFILE, ResolvableProfile.Partial.MAP_CODEC).xmap(
            either -> {
                return either.map(gameProfile -> {
                    return Component.object(
                        ObjectContents.playerHead()
                            .name(gameProfile.name())
                            .id(gameProfile.id())
                            .profileProperties(
                                gameProfile.properties().entries().stream()
                                    .map(entry -> PlayerHeadObjectContents.property(entry.getValue().name(), entry.getValue().value(), entry.getValue().signature()))
                                    .toList()
                            )
                            .build()
                    );
                }, partial -> {
                    return Component.object(
                        ObjectContents.playerHead()
                            .name(partial.name().orElse(null))
                            .id(partial.id().orElse(null))
                            .profileProperties(
                                partial.properties().entries().stream()
                                    .map(entry -> PlayerHeadObjectContents.property(entry.getValue().name(), entry.getValue().value(), entry.getValue().signature()))
                                    .toList()
                            )
                            .build()
                    );
                });
            },
            objectComponent -> {
                final PlayerHeadObjectContents contents = (PlayerHeadObjectContents) objectComponent.contents();
                return Either.right(new ResolvableProfile.Partial(
                    Optional.ofNullable(contents.name()),
                    Optional.ofNullable(contents.id()),
                    new PropertyMap(contents.profileProperties().stream()
                        .map(prop -> new Property(prop.name(), prop.value(), prop.signature()))
                        .collect(ImmutableListMultimap.toImmutableListMultimap(Property::name, Function.identity())))
                ));
            }
        ).forGetter(obj -> obj),
        KEY_CODEC.optionalFieldOf("texture").forGetter(obj -> Optional.ofNullable(((PlayerHeadObjectContents) obj.contents()).texture()))
    ).apply(instance, (player, texture) -> player.contents(
        ((PlayerHeadObjectContents) player.contents()).toBuilder()
            .texture(texture.orElse(null))
            .build()
    )));
    static final MapCodec<ObjectComponent> PLAYER_OBJECT_CODEC = mapCodec(instance -> instance.group(
        PLAYER_OBJECT_PLAYER_CODEC.fieldOf("player").forGetter(obj -> obj),
        Codec.BOOL.optionalFieldOf("hat", true).forGetter(obj -> ((PlayerHeadObjectContents) obj.contents()).hat())
    ).apply(instance, (player, hat) -> player.contents(
        ((PlayerHeadObjectContents) player.contents()).toBuilder()
            .hat(hat)
            .build()
    )));
    static {
        OBJECT_CONTENTS_MAPPER.put("atlas", SPRITE_OBJECT_CODEC);
        OBJECT_CONTENTS_MAPPER.put("player", PLAYER_OBJECT_CODEC);
    }
    static final MapCodec<ObjectComponent> OBJECT_COMPONENT_MAP_CODEC = ComponentSerialization.createLegacyComponentMatcher(OBJECT_CONTENTS_MAPPER, objectComponent -> {
        if (objectComponent.contents() instanceof SpriteObjectContents) {
            return SPRITE_OBJECT_CODEC;
        } else if (objectComponent.contents() instanceof PlayerHeadObjectContents) {
            return PLAYER_OBJECT_CODEC;
        } else {
            throw new IllegalArgumentException("Unknown ObjectContents type " + objectComponent.contents().getClass());
        }
    }, "object");

    static final MapCodec<ScoreComponent> SCORE_COMPONENT_INNER_MAP_CODEC = ScoreContents.INNER_CODEC.xmap(
        s -> Component.score(s.name().map(SelectorPattern::pattern, identity()), s.objective()),
        s -> new ScoreContents(SelectorPattern.parse(s.name()).<Either<SelectorPattern, String>>map(Either::left).result().orElse(Either.right(s.name())), s.objective())
    ); // TODO we might want to ask adventure for a nice way we can avoid parsing and flattening the SelectorPattern on every conversion.
    static final MapCodec<ScoreComponent> SCORE_COMPONENT_MAP_CODEC = SCORE_COMPONENT_INNER_MAP_CODEC.fieldOf("score");
    static final MapCodec<SelectorComponent> SELECTOR_COMPONENT_MAP_CODEC = mapCodec((instance) -> {
        return instance.group(
            Codec.STRING.fieldOf("selector").forGetter(SelectorComponent::pattern),
            COMPONENT_CODEC.optionalFieldOf("separator").forGetter(nullableGetter(SelectorComponent::separator))
        ).apply(instance, (selector, component) -> Component.selector(selector, component.orElse(null)));
    });

    interface NbtComponentDataSource {
        NBTComponentBuilder<?, ?> builder();

        DataSourceType<?> type();
    }

    record StorageDataSource(Key storage) implements NbtComponentDataSource {
        @Override
        public NBTComponentBuilder<?, ?> builder() {
            return Component.storageNBT().storage(this.storage());
        }

        @Override
        public DataSourceType<?> type() {
            return STORAGE_DATA_SOURCE_TYPE;
        }
    }

    record BlockDataSource(String posPattern) implements NbtComponentDataSource {
        @Override
        public NBTComponentBuilder<?, ?> builder() {
            return Component.blockNBT().pos(BlockNBTComponent.Pos.fromString(this.posPattern));
        }

        @Override
        public DataSourceType<?> type() {
            return BLOCK_DATA_SOURCE_TYPE;
        }
    }

    record EntityDataSource(String selectorPattern) implements NbtComponentDataSource {
        @Override
        public NBTComponentBuilder<?, ?> builder() {
            return Component.entityNBT().selector(this.selectorPattern());
        }

        @Override
        public DataSourceType<?> type() {
            return ENTITY_DATA_SOURCE_TYPE;
        }
    }

    static final DataSourceType<StorageDataSource> STORAGE_DATA_SOURCE_TYPE = new DataSourceType<>(mapCodec((instance) -> instance.group(KEY_CODEC.fieldOf("storage").forGetter(StorageDataSource::storage)).apply(instance, StorageDataSource::new)), "storage");
    static final DataSourceType<BlockDataSource> BLOCK_DATA_SOURCE_TYPE = new DataSourceType<>(mapCodec((instance) -> instance.group(Codec.STRING.fieldOf("block").forGetter(BlockDataSource::posPattern)).apply(instance, BlockDataSource::new)), "block");
    static final DataSourceType<EntityDataSource> ENTITY_DATA_SOURCE_TYPE = new DataSourceType<>(mapCodec((instance) -> instance.group(Codec.STRING.fieldOf("entity").forGetter(EntityDataSource::selectorPattern)).apply(instance, EntityDataSource::new)), "entity");

    private static final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends NbtComponentDataSource>> NBT_COMPONENT_DATA_SOURCE_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    static {
        for (final DataSourceType<?> type : List.of(ENTITY_DATA_SOURCE_TYPE, BLOCK_DATA_SOURCE_TYPE, STORAGE_DATA_SOURCE_TYPE)) {
            NBT_COMPONENT_DATA_SOURCE_MAPPER.put(type.id(), type.codec());
        }
    }
    static final MapCodec<NbtComponentDataSource> NBT_COMPONENT_DATA_SOURCE_CODEC = ComponentSerialization.createLegacyComponentMatcher(NBT_COMPONENT_DATA_SOURCE_MAPPER, source -> source.type().codec(), "source");

    record DataSourceType<D extends NbtComponentDataSource>(MapCodec<D> codec, String id) implements StringRepresentable {
        @Override
        public String getSerializedName() {
            return this.id();
        }
    }

    static final MapCodec<NBTComponent<?, ?>> NBT_COMPONENT_MAP_CODEC = mapCodec((instance) -> {
        return instance.group(
            Codec.STRING.fieldOf("nbt").forGetter(NBTComponent::nbtPath),
            Codec.BOOL.lenientOptionalFieldOf("interpret", false).forGetter(NBTComponent::interpret),
            COMPONENT_CODEC.lenientOptionalFieldOf("separator").forGetter(nullableGetter(NBTComponent::separator)),
            NBT_COMPONENT_DATA_SOURCE_CODEC.forGetter(nbtComponent -> {
                if (nbtComponent instanceof final EntityNBTComponent entityNBTComponent) {
                    return new EntityDataSource(entityNBTComponent.selector());
                } else if (nbtComponent instanceof final BlockNBTComponent blockNBTComponent) {
                    return new BlockDataSource(blockNBTComponent.pos().asString());
                } else if (nbtComponent instanceof final StorageNBTComponent storageNBTComponent) {
                    return new StorageDataSource(storageNBTComponent.storage());
                } else {
                    throw new IllegalArgumentException(nbtComponent + " isn't a valid nbt component");
                }
            })
        ).apply(instance, (nbtPath, interpret, separator, dataSource) -> {
            return dataSource.builder().nbtPath(nbtPath).interpret(interpret).separator(separator.orElse(null)).build();
        });
    });

    @SuppressWarnings("NonExtendableApiUsage")
    record ComponentType<C extends Component>(MapCodec<C> codec, Predicate<Component> test, String id) implements StringRepresentable {
        @Override
        public String getSerializedName() {
            return this.id;
        }
    }

    static final ComponentType<TextComponent> PLAIN = new ComponentType<>(TEXT_COMPONENT_MAP_CODEC, TextComponent.class::isInstance, "text");
    static final ComponentType<TranslatableComponent> TRANSLATABLE = new ComponentType<>(TRANSLATABLE_COMPONENT_MAP_CODEC, TranslatableComponent.class::isInstance, "translatable");
    static final ComponentType<KeybindComponent> KEYBIND = new ComponentType<>(KEYBIND_COMPONENT_MAP_CODEC, KeybindComponent.class::isInstance, "keybind");
    static final ComponentType<ObjectComponent> OBJECT = new ComponentType<>(OBJECT_COMPONENT_MAP_CODEC, ObjectComponent.class::isInstance, "object");
    static final ComponentType<ScoreComponent> SCORE = new ComponentType<>(SCORE_COMPONENT_MAP_CODEC, ScoreComponent.class::isInstance, "score");
    static final ComponentType<SelectorComponent> SELECTOR = new ComponentType<>(SELECTOR_COMPONENT_MAP_CODEC, SelectorComponent.class::isInstance, "selector");
    static final ComponentType<NBTComponent<?, ?>> NBT = new ComponentType<>(NBT_COMPONENT_MAP_CODEC, NBTComponent.class::isInstance, "nbt");

    static Codec<Component> createCodec(final Codec<Component> selfCodec) {
        final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends Component>> lateBoundIdMapper = new ExtraCodecs.LateBoundIdMapper<>();
        final ComponentType<?>[] types = new ComponentType<?>[]{PLAIN, TRANSLATABLE, KEYBIND, OBJECT, SCORE, SELECTOR, NBT};
        for (final ComponentType<?> type : types) {
            lateBoundIdMapper.put(type.id(), type.codec());
        }
        final MapCodec<Component> legacyCodec = ComponentSerialization.createLegacyComponentMatcher(lateBoundIdMapper, component -> {
            for (final ComponentType<?> type : types) {
                if (type.test().test(component)) {
                    return type.codec();
                }
            }
            throw new IllegalStateException("Unexpected component type " + component);
        }, "type");

        final Codec<Component> directCodec = RecordCodecBuilder.create((instance) -> {
            return instance.group(
                legacyCodec.forGetter(identity()),
                ExtraCodecs.nonEmptyList(selfCodec.listOf()).optionalFieldOf("extra", List.of()).forGetter(Component::children),
                STYLE_MAP_CODEC.forGetter(Component::style)
            ).apply(instance, (component, children, style) -> {
                return component.style(style).children(children);
            });
        });

        return Codec.either(Codec.either(Codec.STRING, ExtraCodecs.nonEmptyList(selfCodec.listOf())), directCodec).xmap((stringOrListOrComponent) -> {
            return stringOrListOrComponent.map((stringOrList) -> stringOrList.map(Component::text, AdventureCodecs::createFromList), identity());
        }, (text) -> {
            final @Nullable String string = tryCollapseToString(text);
            return string != null ? Either.left(Either.left(string)) : Either.right(text);
        });
    }

    public static @Nullable String tryCollapseToString(final Component component) {
        if (component instanceof final TextComponent textComponent) {
            if (component.children().isEmpty() && component.style().isEmpty()) {
                return textComponent.content();
            }
        }
        return null;
    }

    static Component createFromList(final List<? extends Component> components) {
        Component component = components.get(0);
        for (int i = 1; i < components.size(); i++) {
            component = component.append(components.get(i));
        }
        return component;
    }

    public static final Codec<BinaryTagHolder> BINARY_TAG_HOLDER_COMPOUND_CODEC = CompoundTag.CODEC.flatComapMap(PaperAdventure::asBinaryTagHolder, api -> {
        try {
            final Tag tag = api.get(PaperAdventure.NBT_CODEC);
            if (!(tag instanceof final CompoundTag compoundTag)) {
                return DataResult.error(() -> "Expected a CompoundTag, but got " + tag.getClass().getSimpleName());
            }
            return DataResult.success(compoundTag);
        } catch (CommandSyntaxException e) {
            return DataResult.error(e::getMessage);
        }
    });

    public static <T> Codec<T> indexCodec(final Index<String, T> index) {
        return Codec.of(Codec.STRING.comap(index::keyOrThrow), Codec.STRING.map(index::valueOrThrow));

    }

    private AdventureCodecs() {
    }
}
