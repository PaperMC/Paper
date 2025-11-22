package io.papermc.paper.adventure;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.JsonOps;
import io.papermc.paper.util.MethodParameterSource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junitpioneer.jupiter.cartesian.CartesianTest;

import static io.papermc.paper.adventure.AdventureCodecs.CLICK_EVENT_CODEC;
import static io.papermc.paper.adventure.AdventureCodecs.COMPONENT_CODEC;
import static io.papermc.paper.adventure.AdventureCodecs.HOVER_EVENT_CODEC;
import static io.papermc.paper.adventure.AdventureCodecs.KEY_CODEC;
import static io.papermc.paper.adventure.AdventureCodecs.STYLE_MAP_CODEC;
import static io.papermc.paper.adventure.AdventureCodecs.TEXT_COLOR_CODEC;
import static java.util.Objects.requireNonNull;
import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.text.Component.blockNBT;
import static net.kyori.adventure.text.Component.entityNBT;
import static net.kyori.adventure.text.Component.keybind;
import static net.kyori.adventure.text.Component.object;
import static net.kyori.adventure.text.Component.score;
import static net.kyori.adventure.text.Component.selector;
import static net.kyori.adventure.text.Component.storageNBT;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.TranslationArgument.numeric;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.event.ClickEvent.suggestCommand;
import static net.kyori.adventure.text.event.HoverEvent.showEntity;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.kyori.adventure.text.object.ObjectContents.playerHead;
import static net.kyori.adventure.text.object.ObjectContents.sprite;
import static net.kyori.adventure.text.object.PlayerHeadObjectContents.property;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@VanillaFeature
class AdventureCodecsTest {

    static final String PARAMETERIZED_NAME = "[{index}] {displayName}: {arguments}";

    @Test
    void testTextColor() {
        final TextColor color = color(0x1d38df);
        final Tag result = TEXT_COLOR_CODEC.encodeStart(NbtOps.INSTANCE, color).result().orElseThrow();
        assertEquals("\"" + color.asHexString() + "\"", result.toString());
        final net.minecraft.network.chat.TextColor nms = net.minecraft.network.chat.TextColor.CODEC.decode(NbtOps.INSTANCE, result).result().orElseThrow().getFirst();
        assertEquals(color.value(), nms.getValue());
    }

    @Test
    void testNamedTextColor() {
        final NamedTextColor color = NamedTextColor.BLUE;
        final Tag result = TEXT_COLOR_CODEC.encodeStart(NbtOps.INSTANCE, color).result().orElseThrow();
        assertEquals("\"" + NamedTextColor.NAMES.keyOrThrow(color) + "\"", result.toString());
        final net.minecraft.network.chat.TextColor nms = net.minecraft.network.chat.TextColor.CODEC.decode(NbtOps.INSTANCE, result).result().orElseThrow().getFirst();
        assertEquals(color.value(), nms.getValue());
    }

    @Test
    void testKey() {
        final Key key = key("hello", "there");
        final Tag result = KEY_CODEC.encodeStart(NbtOps.INSTANCE, key).result().orElseThrow();
        assertEquals("\"" + key.asString() + "\"", result.toString());
        final ResourceLocation location = ResourceLocation.CODEC.decode(NbtOps.INSTANCE, result).result().orElseThrow().getFirst();
        assertEquals(key.asString(), location.toString());
    }

    @ParameterizedTest(name = PARAMETERIZED_NAME)
    @EnumSource(value = ClickEvent.Action.class, mode = EnumSource.Mode.EXCLUDE, names = {"OPEN_FILE", "SHOW_DIALOG", "CUSTOM"})
    void testClickEvent(final ClickEvent.Action action) {
        final ClickEvent event = switch (action) {
            case OPEN_URL -> openUrl("https://google.com");
            case RUN_COMMAND -> ClickEvent.runCommand("/say hello");
            case SUGGEST_COMMAND -> suggestCommand("/suggest hello");
            case CHANGE_PAGE -> ClickEvent.changePage(2);
            case COPY_TO_CLIPBOARD -> ClickEvent.copyToClipboard("clipboard content");
            case CUSTOM -> ClickEvent.custom(key("test"), BinaryTagHolder.binaryTagHolder("3"));
            case SHOW_DIALOG, OPEN_FILE -> throw new IllegalArgumentException();
        };
        final Tag result = CLICK_EVENT_CODEC.encodeStart(NbtOps.INSTANCE, event).result().orElseThrow(() -> new RuntimeException("Failed to encode ClickEvent: " + event));
        final net.minecraft.network.chat.ClickEvent nms = net.minecraft.network.chat.ClickEvent.CODEC.decode(NbtOps.INSTANCE, result).result().orElseThrow().getFirst();
        assertEquals(event.action().toString(), nms.action().getSerializedName());
        switch (nms) {
            case net.minecraft.network.chat.ClickEvent.OpenUrl(URI uri) ->
                assertEquals(((ClickEvent.Payload.Text) event.payload()).value(), uri.toString());
            case net.minecraft.network.chat.ClickEvent.SuggestCommand(String command) ->
                assertEquals(((ClickEvent.Payload.Text) event.payload()).value(), command);
            case net.minecraft.network.chat.ClickEvent.RunCommand(String command) ->
                assertEquals(((ClickEvent.Payload.Text) event.payload()).value(), command);
            case net.minecraft.network.chat.ClickEvent.CopyToClipboard(String value) ->
                assertEquals(((ClickEvent.Payload.Text) event.payload()).value(), value);
            case net.minecraft.network.chat.ClickEvent.ChangePage(int page) ->
                assertEquals(((ClickEvent.Payload.Int) event.payload()).integer(), page);
            case net.minecraft.network.chat.ClickEvent.Custom(ResourceLocation id, Optional<Tag> payload) -> {
                assertEquals(((ClickEvent.Payload.Custom) event.payload()).key().toString(), id.toString());
                assertEquals(((ClickEvent.Payload.Custom) event.payload()).nbt(), payload.orElseThrow().asString());
            }
            default -> throw new AssertionError("Unexpected ClickEvent type: " + nms.getClass());
        }
    }

    @Test
    void testShowTextHoverEvent() {
        final HoverEvent<Component> hoverEvent = HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, text("hello"));
        final Tag result = HOVER_EVENT_CODEC.encodeStart(NbtOps.INSTANCE, hoverEvent).result().orElseThrow();
        final net.minecraft.network.chat.HoverEvent.ShowText nms = (net.minecraft.network.chat.HoverEvent.ShowText) net.minecraft.network.chat.HoverEvent.CODEC.decode(NbtOps.INSTANCE, result).result().orElseThrow().getFirst();
        assertEquals(hoverEvent.action().toString(), nms.action().getSerializedName());
        assertEquals("hello", nms.value().getString());
    }

    @Test
    void testShowItemHoverEvent() {
        final ItemStack stack = new ItemStack(Items.PUMPKIN, 3);
        stack.set(DataComponents.CUSTOM_NAME, net.minecraft.network.chat.Component.literal("NAME"));
        final HoverEvent<HoverEvent.ShowItem> hoverEvent = HoverEvent.showItem(key("minecraft:pumpkin"), 3, PaperAdventure.asAdventure(stack.getComponentsPatch()));
        final Tag result = HOVER_EVENT_CODEC.encodeStart(NbtOps.INSTANCE, hoverEvent).result().orElseThrow();
        final DataResult<Pair<net.minecraft.network.chat.HoverEvent, Tag>> dataResult = net.minecraft.network.chat.HoverEvent.CODEC.decode(NbtOps.INSTANCE, result);
        assertTrue(dataResult.result().isPresent(), () -> dataResult + " result is not present");
        final net.minecraft.network.chat.HoverEvent.ShowItem nms = (net.minecraft.network.chat.HoverEvent.ShowItem) dataResult.result().orElseThrow().getFirst();
        assertEquals(hoverEvent.action().toString(), nms.action().getSerializedName());
        final ItemStack item = nms.item();
        assertNotNull(item);
        assertEquals(hoverEvent.value().count(), item.getCount());
        assertEquals(hoverEvent.value().item().asString(), item.getItem().toString());
        assertEquals(stack.getComponentsPatch(), item.getComponentsPatch());
    }

    @Test
    void testShowEntityHoverEvent() {
        UUID uuid = UUID.randomUUID();
        final HoverEvent<HoverEvent.ShowEntity> hoverEvent = showEntity(key("minecraft:wolf"), uuid, text("NAME"));
        final Tag result = HOVER_EVENT_CODEC.encodeStart(NbtOps.INSTANCE, hoverEvent).result().orElseThrow();
        final DataResult<Pair<net.minecraft.network.chat.HoverEvent, Tag>> dataResult = net.minecraft.network.chat.HoverEvent.CODEC.decode(NbtOps.INSTANCE, result);
        assertTrue(dataResult.result().isPresent(), () -> dataResult + " result is not present");
        final net.minecraft.network.chat.HoverEvent.ShowEntity nms = (net.minecraft.network.chat.HoverEvent.ShowEntity) dataResult.result().orElseThrow().getFirst();
        assertEquals(hoverEvent.action().toString(), nms.action().getSerializedName());
        final net.minecraft.network.chat.HoverEvent.EntityTooltipInfo value = nms.entity();
        assertNotNull(value);
        assertEquals(hoverEvent.value().type().asString(), BuiltInRegistries.ENTITY_TYPE.getKey(value.type).toString());
        assertEquals(hoverEvent.value().id(), value.uuid);
        assertEquals("NAME", value.name.orElseThrow().getString());
    }

    @Test
    void testSimpleStyle() {
        final Style style = style().decorate(TextDecoration.BOLD).color(NamedTextColor.RED).build();
        final Tag result = STYLE_MAP_CODEC.codec().encodeStart(NbtOps.INSTANCE, style).result().orElseThrow();
        final DataResult<Pair<net.minecraft.network.chat.Style, Tag>> dataResult = net.minecraft.network.chat.Style.Serializer.CODEC.decode(NbtOps.INSTANCE, result);
        assertTrue(dataResult.result().isPresent(), () -> dataResult + " result is not present");
        final net.minecraft.network.chat.Style nms = dataResult.result().get().getFirst();
        assertTrue(nms.isBold());
        assertEquals(requireNonNull(style.color()).value(), requireNonNull(nms.getColor()).getValue());
    }

    @CartesianTest(name = PARAMETERIZED_NAME)
    void testDirectRoundTripStyle(
        @MethodParameterSource("dynamicOps") final DynamicOps<?> dynamicOps,
        @MethodParameterSource("testStyles") final Style style
    ) {
        testDirectRoundTrip(dynamicOps, STYLE_MAP_CODEC.codec(), style);
    }

    @CartesianTest(name = PARAMETERIZED_NAME)
    void testMinecraftRoundTripStyle(
        @MethodParameterSource("dynamicOps") final DynamicOps<?> dynamicOps,
        @MethodParameterSource("testStyles") final Style style
    ) {
        testMinecraftRoundTrip(dynamicOps, STYLE_MAP_CODEC.codec(), net.minecraft.network.chat.Style.Serializer.CODEC, style);
    }

    @CartesianTest(name = PARAMETERIZED_NAME)
    void testDirectRoundTripComponent(
        @MethodParameterSource("dynamicOps") final DynamicOps<?> dynamicOps,
        @TestComponents final Component component
    ) {
        testDirectRoundTrip(dynamicOps, COMPONENT_CODEC, component);
    }

    @CartesianTest(name = PARAMETERIZED_NAME)
    void testMinecraftRoundTripComponent(
        @MethodParameterSource("dynamicOps") final DynamicOps<?> dynamicOps,
        @TestComponents final Component component
    ) {
        testMinecraftRoundTrip(dynamicOps, COMPONENT_CODEC, ComponentSerialization.CODEC, component);
    }

    static List<? extends DynamicOps<?>> dynamicOps() {
        return Stream.of(
            NbtOps.INSTANCE,
            JavaOps.INSTANCE,
            JsonOps.INSTANCE
        )
            .map(ops -> RegistryHelper.getRegistry().createSerializationContext(ops))
            .toList();
    }

    @ParameterizedTest(name = PARAMETERIZED_NAME)
    @MethodSource({"invalidData"})
    void invalidThrows(final Tag input) {
        assertThrows(RuntimeException.class, () -> {
            require(
                COMPONENT_CODEC.decode(NbtOps.INSTANCE, input),
                msg -> "Failed to decode " + input + ": " + msg
            );
        });
    }

    static <A, O> void testDirectRoundTrip(final DynamicOps<O> ops, final Codec<A> codec, final A adventure) {
        final O encoded = require(
            codec.encodeStart(ops, adventure),
            msg -> "Failed to encode " + adventure + ": " + msg
        );
        final Pair<A, O> roundTripResult = require(
            codec.decode(ops, encoded),
            msg -> "Failed to decode " + encoded + ": " + msg
        );
        assertEquals(adventure, roundTripResult.getFirst());
    }

    static <A, M, O> void testMinecraftRoundTrip(final DynamicOps<O> ops, final Codec<A> adventureCodec, final Codec<M> minecraftCodec, final A adventure) {
        final O encoded = require(
            adventureCodec.encodeStart(ops, adventure),
            msg -> "Failed to encode " + adventure + ": " + msg
        );
        final M minecraftResult = require(
            minecraftCodec.decode(ops, encoded),
            msg -> "Failed to decode to Minecraft: " + encoded + "; " + msg
        ).getFirst();
        final O minecraftReEncoded = require(
            minecraftCodec.encodeStart(ops, minecraftResult),
            msg -> "Failed to re-encode Minecraft: " + minecraftResult + "; " + msg
        );
        final Pair<A, O> roundTripResult = require(
            adventureCodec.decode(ops, minecraftReEncoded),
            msg -> "Failed to decode " + minecraftReEncoded + ": " + msg
        );
        assertEquals(adventure, roundTripResult.getFirst());
    }

    static <R> R require(final DataResult<R> result, final Function<String, String> errorMessage) {
        return result.getOrThrow(s -> new RuntimeException(errorMessage.apply(s)));
    }

    static List<Tag> invalidData() {
        return List.of(
            IntTag.valueOf(-1),
            ByteTag.ZERO,
            new CompoundTag(),
            new ListTag()
        );
    }

    static List<Style> testStyles() {
        return List.of(
            Style.empty(),
            style(color(0x0a1ab9)),
            style(NamedTextColor.LIGHT_PURPLE),
            style(TextDecoration.BOLD),
            style(TextDecoration.BOLD.withState(false)),
            style(TextDecoration.BOLD.withState(TextDecoration.State.NOT_SET)),
            style()
                .font(key("kyori", "kittens"))
                .shadowColor(ShadowColor.fromHexString("#FF00AAFF"))
                .color(NamedTextColor.RED)
                .decoration(TextDecoration.BOLD, true)
                .clickEvent(openUrl("https://github.com"))
                .build(),
            style()
                .hoverEvent(showEntity(HoverEvent.ShowEntity.showEntity(
                    key(Key.MINECRAFT_NAMESPACE, "pig"),
                    UUID.randomUUID(),
                    text("Dolores", color(0x0a1ab9))
                )))
                .build()
        );
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
    @MethodParameterSource({
        "testTexts", "testTranslatables", "testKeybinds", "testScores",
        "testSelectors", "testBlockNbts", "testEntityNbts", "testStorageNbts",
        "testObjects"
    })
    @interface TestComponents {
    }

    static List<Component> testTexts() {
        return List.of(
            Component.empty(),
            text("Hello, world."),
            text().content("c")
                .color(NamedTextColor.GOLD)
                .append(text("o", NamedTextColor.DARK_AQUA))
                .append(text("l", NamedTextColor.LIGHT_PURPLE))
                .append(text("o", NamedTextColor.DARK_PURPLE))
                .append(text("u", NamedTextColor.BLUE))
                .append(text("r", NamedTextColor.DARK_GREEN))
                .append(text("s", NamedTextColor.RED))
                .build(),
            text().content("This is a test.")
                .color(NamedTextColor.DARK_PURPLE)
                .hoverEvent(HoverEvent.showText(text("A test.")))
                .append(text(" "))
                .append(text("A what?", NamedTextColor.DARK_AQUA))
                .build(),
            text().append(text("Hello")).build(),
            miniMessage().deserialize("<rainbow>|||||||||||||||||||||||<bold>|||||||||||||</bold>|||||||||")
        );
    }

    static List<Component> testTranslatables() {
        final String key = "multiplayer.player.left";
        final UUID id = UUID.fromString("eb121687-8b1a-4944-bd4d-e0a818d9dfe2");
        final String name = "kashike";
        final String command = String.format("/msg %s ", name);

        return List.of(
            translatable(key),
            translatable()
                .key("thisIsA")
                .fallback("This is a test.")
                .build(),
            translatable(key, numeric(Integer.MAX_VALUE), text("HEY")), // boolean doesn't work in vanilla, can't test here
            translatable(
                key,
                text().content(name)
                    .clickEvent(suggestCommand(command))
                    .hoverEvent(showEntity(HoverEvent.ShowEntity.showEntity(
                        key("minecraft", "player"),
                        id,
                        text(name)
                    )))
                    .build()
            ).color(NamedTextColor.YELLOW)
        );
    }

    static List<Component> testKeybinds() {
        return List.of(keybind("key.jump"));
    }

    static List<Component> testScores() {
        final String name = "abc";
        final String objective = "def";

        return List.of(score(name, objective));
    }

    static List<Component> testSelectors() {
        final String selector = "@p";

        return List.of(
            selector(selector),
            selector(selector, text(','))
        );
    }

    static List<Component> testBlockNbts() {
        return List.of(
            blockNBT().nbtPath("abc").localPos(1.23d, 2.0d, 3.89d).build(),
            blockNBT().nbtPath("xyz").absoluteWorldPos(4, 5, 6).interpret(true).build(),
            blockNBT().nbtPath("eeee").relativeWorldPos(7, 83, 900)
                .separator(text(';'))
                .build(),
            blockNBT().nbtPath("qwert").worldPos(
                BlockNBTComponent.WorldPos.Coordinate.absolute(12),
                BlockNBTComponent.WorldPos.Coordinate.relative(3),
                BlockNBTComponent.WorldPos.Coordinate.absolute(1200)
            ).build()
        );
    }

    static List<Component> testEntityNbts() {
        return List.of(
            entityNBT().nbtPath("abc").selector("test").build(),
            entityNBT().nbtPath("abc").selector("test").separator(text(',')).build(),
            entityNBT().nbtPath("abc").selector("test").interpret(true).build()
        );
    }

    static List<Component> testStorageNbts() {
        return List.of(
            storageNBT().nbtPath("abc").storage(key("doom:apple")).build(),
            storageNBT().nbtPath("abc").storage(key("doom:apple")).separator(text(", ")).build(),
            storageNBT().nbtPath("abc").storage(key("doom:apple")).interpret(true).build()
        );
    }

    static List<Component> testObjects() {
        return List.of(
            object(sprite(key("block/fire_0"))),
            object(sprite(key("ae2", "blocks"), key("block/controller"))),
            object(sprite(key("blocks"), key("block/fire_0"))),
            object(playerHead("pig")),
            object(playerHead().build()),
            object(playerHead().name("sheep").id(UUID.randomUUID()).build()),
            object(playerHead(UUID.randomUUID())),
            object(playerHead().profileProperties(List.of(property("textures", "abc"))).build()),
            object(playerHead().texture(key("apples")).build())
        );
    }
}
