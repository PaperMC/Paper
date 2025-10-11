package io.papermc.paper.adventure;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.JavaOps;
import io.netty.util.AttributeKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.DataComponentValueConverterRegistry;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.util.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.Filterable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WrittenBookContent;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public final class PaperAdventure {
    private static final Pattern LOCALIZATION_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?s");
    public static final ComponentFlattener FLATTENER = ComponentFlattener.basic().toBuilder()
        .complexMapper(TranslatableComponent.class, (translatable, consumer) -> {
            final Language language = Language.getInstance();
            final @Nullable String fallback = translatable.fallback();
            if (!language.has(translatable.key()) && (fallback == null || !language.has(fallback))) {
                if (GlobalTranslator.translator().canTranslate(translatable.key(), Locale.US)) {
                    consumer.accept(GlobalTranslator.render(translatable, Locale.US));
                    return;
                }
            }
            final @NotNull String translated = Language.getInstance().getOrDefault(translatable.key(), fallback != null ? fallback : translatable.key());

            final Matcher matcher = LOCALIZATION_PATTERN.matcher(translated);
            final List<TranslationArgument> args = translatable.arguments();
            int argPosition = 0;
            int lastIdx = 0;
            while (matcher.find()) {
                // append prior
                if (lastIdx < matcher.start()) {
                    consumer.accept(Component.text(translated.substring(lastIdx, matcher.start())));
                }
                lastIdx = matcher.end();

                final @Nullable String argIdx = matcher.group(1);
                // calculate argument position
                if (argIdx != null) {
                    try {
                        final int idx = Integer.parseInt(argIdx) - 1;
                        if (idx < args.size()) {
                            consumer.accept(args.get(idx).asComponent());
                        }
                    } catch (final NumberFormatException ex) {
                        // ignore, drop the format placeholder
                    }
                } else {
                    final int idx = argPosition++;
                    if (idx < args.size()) {
                        consumer.accept(args.get(idx).asComponent());
                    }
                }
            }

            // append tail
            if (lastIdx < translated.length()) {
                consumer.accept(Component.text(translated.substring(lastIdx)));
            }
        })
        .build();
    public static final AttributeKey<Locale> LOCALE_ATTRIBUTE = AttributeKey.valueOf("adventure:locale"); // init after FLATTENER because classloading triggered here might create a logger
    @Deprecated
    public static final PlainComponentSerializer PLAIN = PlainComponentSerializer.builder().flattener(FLATTENER).build();
    public static final ANSIComponentSerializer ANSI_SERIALIZER = ANSIComponentSerializer.builder().flattener(FLATTENER).build();
    private static final TagParser<Tag> NBT_PARSER = TagParser.create(NbtOps.INSTANCE);
    public static final Codec<Tag, String, CommandSyntaxException, RuntimeException> NBT_CODEC = new Codec<>() {
        @Override
        public @NotNull Tag decode(final @NotNull String encoded) throws CommandSyntaxException {
            return NBT_PARSER.parseFully(encoded);
        }

        @Override
        public @NotNull String encode(final @NotNull Tag decoded) {
            return decoded.toString();
        }
    };
    public static final ComponentSerializer<Component, Component, net.minecraft.network.chat.Component> WRAPPER_AWARE_SERIALIZER = new WrapperAwareSerializer(() -> CraftRegistry.getMinecraftRegistry().createSerializationContext(JavaOps.INSTANCE));

    private PaperAdventure() {
    }

    // Key

    public static Key asAdventure(final ResourceLocation key) {
        return Key.key(key.getNamespace(), key.getPath());
    }

    public static ResourceLocation asVanilla(final Key key) {
        return ResourceLocation.fromNamespaceAndPath(key.namespace(), key.value());
    }

    public static <T> ResourceKey<T> asVanilla(
        final ResourceKey<? extends net.minecraft.core.Registry<T>> registry,
        final Key key
    ) {
        return ResourceKey.create(registry, asVanilla(key));
    }

    public static Key asAdventureKey(final ResourceKey<?> key) {
        return asAdventure(key.location());
    }

    public static @Nullable ResourceLocation asVanillaNullable(final Key key) {
        if (key == null) {
            return null;
        }
        return asVanilla(key);
    }

    public static Holder<SoundEvent> resolveSound(final Key key) {
        ResourceLocation id = asVanilla(key);
        Optional<Holder.Reference<SoundEvent>> vanilla = BuiltInRegistries.SOUND_EVENT.get(id);
        if (vanilla.isPresent()) {
            return vanilla.get();
        }

        // sound is not known so not in the registry but might be used by the client with a resource pack
        return Holder.direct(SoundEvent.createVariableRangeEvent(id));
    }

    // Component

    public static @NotNull Component asAdventure(@Nullable final net.minecraft.network.chat.Component component) {
        return component == null ? Component.empty() : WRAPPER_AWARE_SERIALIZER.deserialize(component);
    }

    public static List<Component> asAdventure(final List<? extends net.minecraft.network.chat.Component> vanillas) {
        final List<Component> adventures = new ArrayList<>(vanillas.size());
        for (final net.minecraft.network.chat.Component vanilla : vanillas) {
            adventures.add(asAdventure(vanilla));
        }
        return adventures;
    }

    public static List<Component> asAdventure(final net.minecraft.network.chat.Component[] vanillas) {
        final List<Component> adventures = new ArrayList<>(vanillas.length);
        for (final net.minecraft.network.chat.Component vanilla : vanillas) {
            adventures.add(asAdventure(vanilla));
        }
        return adventures;
    }

    public static net.minecraft.network.chat.@NotNull Component asVanillaNullToEmpty(final @Nullable Component component) {
        if (component == null) return net.minecraft.network.chat.CommonComponents.EMPTY;
        return asVanilla(component);
    }

    @Contract("null -> null; !null -> !null")
    public static net.minecraft.network.chat.Component asVanilla(final @Nullable Component component) {
        if (component == null) return null;
        if (true) return new AdventureComponent(component);
        return WRAPPER_AWARE_SERIALIZER.serialize(component);
    }

    public static List<net.minecraft.network.chat.Component> asVanilla(final List<? extends Component> adventures) {
        final List<net.minecraft.network.chat.Component> vanillas = new ArrayList<>(adventures.size());
        for (final Component adventure : adventures) {
            vanillas.add(asVanilla(adventure));
        }
        return vanillas;
    }

    public static String asJsonString(final Component component, final Locale locale) {
        return GsonComponentSerializer.gson().serialize(translated(component, locale));
    }

    public static boolean hasAnyTranslations() {
        return StreamSupport.stream(GlobalTranslator.translator().sources().spliterator(), false)
            .anyMatch(t -> t.hasAnyTranslations().toBooleanOrElse(true));
    }

    private static final Map<Locale, com.mojang.serialization.Codec<Component>> LOCALIZED_CODECS = new ConcurrentHashMap<>();

    public static com.mojang.serialization.Codec<Component> localizedCodec(final @Nullable Locale l) {
        if (l == null) {
            return AdventureCodecs.COMPONENT_CODEC;
        }
        return LOCALIZED_CODECS.computeIfAbsent(l, locale -> AdventureCodecs.COMPONENT_CODEC.xmap(
            component -> component, // decode
            component -> translated(component, locale) // encode
        ));
    }

    public static String asPlain(final Component component, final Locale locale) {
        return PlainTextComponentSerializer.plainText().serialize(translated(component, locale));
    }

    private static Component translated(final Component component, final Locale locale) {
        //noinspection ConstantValue
        return GlobalTranslator.render(
            component,
            // play it safe
            locale != null
                ? locale
                : Locale.US
        );
    }

    public static Component resolveWithContext(final @NotNull Component component, final @Nullable CommandSender context, final @Nullable org.bukkit.entity.Entity scoreboardSubject, final boolean bypassPermissions) throws IOException {
        final CommandSourceStack css = context != null ? VanillaCommandWrapper.getListener(context) : null;
        Boolean previous = null;
        if (css != null && bypassPermissions) {
            previous = css.bypassSelectorPermissions;
            css.bypassSelectorPermissions = true;
        }
        try {
            return asAdventure(ComponentUtils.updateForEntity(css, asVanilla(component), scoreboardSubject == null ? null : ((CraftEntity) scoreboardSubject).getHandle(), 0));
        } catch (final CommandSyntaxException e) {
            throw new IOException(e);
        } finally {
            if (css != null && previous != null) {
                css.bypassSelectorPermissions = previous;
            }
        }
    }

    // BossBar

    public static BossEvent.BossBarColor asVanilla(final BossBar.Color color) {
        return switch (color) {
            case PINK -> BossEvent.BossBarColor.PINK;
            case BLUE -> BossEvent.BossBarColor.BLUE;
            case RED -> BossEvent.BossBarColor.RED;
            case GREEN -> BossEvent.BossBarColor.GREEN;
            case YELLOW -> BossEvent.BossBarColor.YELLOW;
            case PURPLE -> BossEvent.BossBarColor.PURPLE;
            case WHITE -> BossEvent.BossBarColor.WHITE;
        };
    }

    public static BossBar.Color asAdventure(final BossEvent.BossBarColor color) {
        return switch (color) {
            case PINK -> BossBar.Color.PINK;
            case BLUE -> BossBar.Color.BLUE;
            case RED -> BossBar.Color.RED;
            case GREEN -> BossBar.Color.GREEN;
            case YELLOW -> BossBar.Color.YELLOW;
            case PURPLE -> BossBar.Color.PURPLE;
            case WHITE -> BossBar.Color.WHITE;
        };
    }

    public static BossEvent.BossBarOverlay asVanilla(final BossBar.Overlay overlay) {
        return switch (overlay) {
            case PROGRESS -> BossEvent.BossBarOverlay.PROGRESS;
            case NOTCHED_6 -> BossEvent.BossBarOverlay.NOTCHED_6;
            case NOTCHED_10 -> BossEvent.BossBarOverlay.NOTCHED_10;
            case NOTCHED_12 -> BossEvent.BossBarOverlay.NOTCHED_12;
            case NOTCHED_20 -> BossEvent.BossBarOverlay.NOTCHED_20;
        };
    }

    public static BossBar.Overlay asAdventure(final BossEvent.BossBarOverlay overlay) {
        return switch (overlay) {
            case PROGRESS -> BossBar.Overlay.PROGRESS;
            case NOTCHED_6 -> BossBar.Overlay.NOTCHED_6;
            case NOTCHED_10 -> BossBar.Overlay.NOTCHED_10;
            case NOTCHED_12 -> BossBar.Overlay.NOTCHED_12;
            case NOTCHED_20 -> BossBar.Overlay.NOTCHED_20;
        };
    }

    public static void setFlag(final BossBar bar, final BossBar.Flag flag, final boolean value) {
        if (value) {
            bar.addFlag(flag);
        } else {
            bar.removeFlag(flag);
        }
    }

    // Book

    public static ItemStack asItemStack(final Book book, final Locale locale) {
        final ItemStack item = new ItemStack(net.minecraft.world.item.Items.WRITTEN_BOOK, 1);
        item.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(
            Filterable.passThrough(validateField(asPlain(book.title(), locale), WrittenBookContent.TITLE_MAX_LENGTH, "title")),
            asPlain(book.author(), locale),
            0,
            book.pages().stream().map(c -> Filterable.passThrough(PaperAdventure.asVanilla(c))).toList(), // TODO should we validate length?
            false
        ));
        return item;
    }

    private static String validateField(final String content, final int length, final String name) {
        final int actual = content.length();
        if (actual > length) {
            throw new IllegalArgumentException("Field '" + name + "' has a maximum length of " + length + " but was passed '" + content + "', which was " + actual + " characters long.");
        }
        return content;
    }

    // Sounds

    public static SoundSource asVanilla(final Sound.Source source) {
        return switch (source) {
            case MASTER -> SoundSource.MASTER;
            case MUSIC -> SoundSource.MUSIC;
            case RECORD -> SoundSource.RECORDS;
            case WEATHER -> SoundSource.WEATHER;
            case BLOCK -> SoundSource.BLOCKS;
            case HOSTILE -> SoundSource.HOSTILE;
            case NEUTRAL -> SoundSource.NEUTRAL;
            case PLAYER -> SoundSource.PLAYERS;
            case AMBIENT -> SoundSource.AMBIENT;
            case VOICE -> SoundSource.VOICE;
            case UI -> SoundSource.UI;
        };
    }

    public static @Nullable SoundSource asVanillaNullable(final Sound.@Nullable Source source) {
        if (source == null) {
            return null;
        }
        return asVanilla(source);
    }

    public static Packet<?> asSoundPacket(final Sound sound, final double x, final double y, final double z, final long seed, @Nullable BiConsumer<Packet<?>, Float> packetConsumer) {
        final ResourceLocation name = asVanilla(sound.name());
        final Optional<SoundEvent> soundEvent = BuiltInRegistries.SOUND_EVENT.getOptional(name);
        final SoundSource source = asVanilla(sound.source());

        final Holder<SoundEvent> soundEventHolder = soundEvent.map(BuiltInRegistries.SOUND_EVENT::wrapAsHolder).orElseGet(() -> Holder.direct(SoundEvent.createVariableRangeEvent(name)));
        final Packet<?> packet = new ClientboundSoundPacket(soundEventHolder, source, x, y, z, sound.volume(), sound.pitch(), seed);
        if (packetConsumer != null) {
            packetConsumer.accept(packet, soundEventHolder.value().getRange(sound.volume()));
        }
        return packet;
    }

    public static Packet<?> asSoundPacket(final Sound sound, final Entity emitter, final long seed, @Nullable BiConsumer<Packet<?>, Float> packetConsumer) {
        final ResourceLocation name = asVanilla(sound.name());
        final Optional<SoundEvent> soundEvent = BuiltInRegistries.SOUND_EVENT.getOptional(name);
        final SoundSource source = asVanilla(sound.source());

        final Holder<SoundEvent> soundEventHolder = soundEvent.map(BuiltInRegistries.SOUND_EVENT::wrapAsHolder).orElseGet(() -> Holder.direct(SoundEvent.createVariableRangeEvent(name)));
        final Packet<?> packet = new ClientboundSoundEntityPacket(soundEventHolder, source, emitter, sound.volume(), sound.pitch(), seed);
        if (packetConsumer != null) {
            packetConsumer.accept(packet, soundEventHolder.value().getRange(sound.volume()));
        }
        return packet;
    }

    // NBT

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<Key, ? extends DataComponentValue> asAdventure(
        final DataComponentPatch patch
    ) {
        if (patch.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<Key, DataComponentValue> map = new HashMap<>();
        for (final Map.Entry<DataComponentType<?>, Optional<?>> entry : patch.entrySet()) {
            if (entry.getKey().isTransient()) continue;
            @Subst("key:value") final String typeKey = requireNonNull(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(entry.getKey())).toString();
            if (entry.getValue().isEmpty()) {
                   map.put(Key.key(typeKey), DataComponentValue.removed());
            } else {
                map.put(Key.key(typeKey), new DataComponentValueImpl(entry.getKey().codec(), entry.getValue().get()));
            }
        }
        return map;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DataComponentPatch asVanilla(final Map<? extends Key, ? extends DataComponentValue> map) {
        if (map.isEmpty()) {
            return DataComponentPatch.EMPTY;
        }
        final DataComponentPatch.Builder builder = DataComponentPatch.builder();
        map.forEach((key, dataComponentValue) -> {
            final DataComponentType<?> type = requireNonNull(BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(asVanilla(key)));
            if (dataComponentValue instanceof DataComponentValue.Removed) {
                builder.remove(type);
                return;
            }
            final DataComponentValueImpl<?> converted = DataComponentValueConverterRegistry.convert(DataComponentValueImpl.class, key, dataComponentValue);
            builder.set((DataComponentType) type, (Object) converted.value());
        });
        return builder.build();
    }

    public record DataComponentValueImpl<T>(com.mojang.serialization.Codec<T> codec, T value) implements DataComponentValue.TagSerializable {

        @Override
        public @NotNull BinaryTagHolder asBinaryTag() {
            return BinaryTagHolder.encode(this.codec.encodeStart(CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE), this.value).getOrThrow(IllegalArgumentException::new), NBT_CODEC);
        }
    }

    public static @Nullable BinaryTagHolder asBinaryTagHolder(final @Nullable CompoundTag tag) {
        if (tag == null) {
            return null;
        }
        return BinaryTagHolder.encode(tag, NBT_CODEC);
    }

    // Colors

    public static @NotNull TextColor asAdventure(final ChatFormatting formatting) {
        final Integer color = formatting.getColor();
        if (color == null) {
            throw new IllegalArgumentException("Not a valid color");
        }
        return TextColor.color(color);
    }

    public static @Nullable ChatFormatting asVanilla(final TextColor color) {
        return ChatFormatting.getByHexValue(color.value());
    }

    // Style

    public static net.minecraft.network.chat.Style asVanilla(final Style style) {
        final RegistryOps<Object> ops = CraftRegistry.getMinecraftRegistry().createSerializationContext(JavaOps.INSTANCE);
        final Object encoded = AdventureCodecs.STYLE_MAP_CODEC.codec()
            .encodeStart(ops, style).getOrThrow(IllegalStateException::new);

        return net.minecraft.network.chat.Style.Serializer.CODEC
            .parse(ops, encoded).getOrThrow(IllegalStateException::new);
    }

    public static Style asAdventure(final net.minecraft.network.chat.Style style) {
        final RegistryOps<Object> ops = CraftRegistry.getMinecraftRegistry().createSerializationContext(JavaOps.INSTANCE);
        final Object encoded = net.minecraft.network.chat.Style.Serializer.CODEC
            .encodeStart(ops, style).getOrThrow(IllegalStateException::new);

        return AdventureCodecs.STYLE_MAP_CODEC.codec()
            .parse(ops, encoded).getOrThrow(IllegalStateException::new);
    }

}
