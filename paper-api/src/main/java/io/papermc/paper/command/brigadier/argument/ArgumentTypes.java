package io.papermc.paper.command.brigadier.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import io.papermc.paper.command.brigadier.argument.predicate.BlockInWorldPredicate;
import io.papermc.paper.command.brigadier.argument.predicate.ItemStackPredicate;
import io.papermc.paper.command.brigadier.argument.range.DoubleRangeProvider;
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider;
import io.papermc.paper.command.brigadier.argument.resolvers.AngleResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.ColumnBlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.ColumnFinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.RotationResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Axis;
import org.bukkit.GameMode;
import org.bukkit.HeightMap;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.jetbrains.annotations.ApiStatus;

import static io.papermc.paper.command.brigadier.argument.VanillaArgumentProvider.provider;

/**
 * Vanilla Minecraft includes several custom {@link ArgumentType}s that are recognized by the client.
 * Many of these argument types include client-side completions and validation, and some include command signing context.
 *
 * <p>This class allows creating instances of these types for use in plugin commands, with friendly API result types.</p>
 *
 * <p>{@link CustomArgumentType} is provided for customizing parsing or result types server-side, while sending the vanilla argument type to the client.</p>
 */
public final class ArgumentTypes {

    /**
     * Represents a selector that can capture any
     * single entity.
     *
     * @return argument that takes one entity
     */
    public static ArgumentType<EntitySelectorArgumentResolver> entity() {
        return provider().entity();
    }

    /**
     * Represents a selector that can capture multiple
     * entities.
     *
     * @return argument that takes multiple entities
     */
    public static ArgumentType<EntitySelectorArgumentResolver> entities() {
        return provider().entities();
    }

    /**
     * Represents a selector that can capture a
     * singular player entity.
     *
     * @return argument that takes one player
     */
    public static ArgumentType<PlayerSelectorArgumentResolver> player() {
        return provider().player();
    }

    /**
     * Represents a selector that can capture multiple
     * player entities.
     *
     * @return argument that takes multiple players
     */
    public static ArgumentType<PlayerSelectorArgumentResolver> players() {
        return provider().players();
    }

    /**
     * A selector argument that provides a list
     * of player profiles.
     *
     * @return player profile argument
     */
    public static ArgumentType<PlayerProfileListResolver> playerProfiles() {
        return provider().playerProfiles();
    }

    /**
     * A block position argument.
     *
     * @return block position argument
     */
    public static ArgumentType<BlockPositionResolver> blockPosition() {
        return provider().blockPosition();
    }

    /**
     * A column block position argument.
     *
     * @return column block position argument
     */
    @ApiStatus.Experimental
    public static ArgumentType<ColumnBlockPositionResolver> columnBlockPosition() {
        return provider().columnBlockPosition();
    }

    /**
     * A block predicate argument.
     *
     * @return block predicate argument
     */
    @ApiStatus.Experimental
    public static ArgumentType<BlockInWorldPredicate> blockInWorldPredicate() {
        return provider().blockInWorldPredicate();
    }

    /**
     * A fine position argument.
     *
     * @return fine position argument
     * @see #finePosition(boolean) to center whole numbers
     */
    public static ArgumentType<FinePositionResolver> finePosition() {
        return finePosition(false);
    }

    /**
     * A fine position argument.
     *
     * @param centerIntegers if whole numbers should be centered (+0.5)
     * @return fine position argument
     */
    public static ArgumentType<FinePositionResolver> finePosition(final boolean centerIntegers) {
        return provider().finePosition(centerIntegers);
    }

    /**
     * A column fine position argument.
     *
     * @return column fine position argument
     * @see #columnFinePosition(boolean) to center whole numbers
     */
    @ApiStatus.Experimental
    public static ArgumentType<ColumnFinePositionResolver> columnFinePosition() {
        return columnFinePosition(false);
    }

    /**
     * A column fine position argument.
     *
     * @param centerIntegers if whole numbers should be centered (+0.5)
     * @return column fine position argument
     */
    @ApiStatus.Experimental
    public static ArgumentType<ColumnFinePositionResolver> columnFinePosition(final boolean centerIntegers) {
        return provider().columnFinePosition(centerIntegers);
    }

    /**
     * A rotation argument.
     *
     * @return rotation argument
     */
    public static ArgumentType<RotationResolver> rotation() {
        return provider().rotation();
    }

    /**
     * An angle argument.
     *
     * @return angle argument
     */
    @ApiStatus.Experimental
    public static ArgumentType<AngleResolver> angle() {
        return provider().angle();
    }

    /**
     * An argument used to resolve a set of axes.
     *
     * @return a set of axes.
     * @see org.bukkit.Axis
     */
    @ApiStatus.Experimental
    public static ArgumentType<AxisSet> axes() {
        return provider().axes();
    }

    /**
     * A blockstate argument which will provide rich parsing for specifying
     * the specific block variant and then the block entity NBT if applicable.
     *
     * @return argument
     */
    public static ArgumentType<BlockState> blockState() {
        return provider().blockState();
    }

    /**
     * An ItemStack argument which provides rich parsing for
     * specifying item material and item NBT information.
     *
     * @return argument
     */
    public static ArgumentType<ItemStack> itemStack() {
        return provider().itemStack();
    }

    /**
     * An item predicate argument.
     *
     * @return argument
     */
    public static ArgumentType<ItemStackPredicate> itemPredicate() {
        return provider().itemStackPredicate();
    }

    /**
     * An argument for parsing {@link NamedTextColor}s.
     *
     * @return argument
     */
    public static ArgumentType<NamedTextColor> namedColor() {
        return provider().namedColor();
    }

    /**
     * A hex color argument.
     *
     * @return argument
     */
    public static ArgumentType<TextColor> hexColor() {
        return provider().hexColor();
    }

    /**
     * A component argument.
     *
     * @return argument
     */
    public static ArgumentType<Component> component() {
        return provider().component();
    }

    /**
     * A style argument.
     *
     * @return argument
     */
    public static ArgumentType<Style> style() {
        return provider().style();
    }

    /**
     * A signed message argument.
     * This argument can be resolved to retrieve the underlying
     * signed message.
     *
     * @return argument
     */
    public static ArgumentType<SignedMessageResolver> signedMessage() {
        return provider().signedMessage();
    }

    /**
     * A scoreboard display slot argument.
     *
     * @return argument
     */
    public static ArgumentType<DisplaySlot> scoreboardDisplaySlot() {
        return provider().scoreboardDisplaySlot();
    }

    /**
     * A namespaced key argument.
     *
     * @return argument
     */
    public static ArgumentType<NamespacedKey> namespacedKey() {
        return provider().namespacedKey();
    }

    /**
     * A key argument.
     *
     * @return argument
     */
    // include both key types as we are slowly moving to use adventure's key
    public static ArgumentType<Key> key() {
        return provider().key();
    }

    /**
     * An inclusive range of integers that may be unbounded on either end.
     *
     * @return argument
     */
    public static ArgumentType<IntegerRangeProvider> integerRange() {
        return provider().integerRange();
    }

    /**
     * An inclusive range of doubles that may be unbounded on either end.
     *
     * @return argument
     */
    public static ArgumentType<DoubleRangeProvider> doubleRange() {
        return provider().doubleRange();
    }

    /**
     * A world argument.
     *
     * @return argument
     */
    public static ArgumentType<World> world() {
        return provider().world();
    }

    /**
     * A game mode argument.
     *
     * @return argument
     */
    public static ArgumentType<GameMode> gameMode() {
        return provider().gameMode();
    }

    /**
     * An argument for getting a heightmap type.
     *
     * @return argument
     */
    public static ArgumentType<HeightMap> heightMap() {
        return provider().heightMap();
    }

    /**
     * A uuid argument.
     *
     * @return argument
     */
    public static ArgumentType<UUID> uuid() {
        return provider().uuid();
    }

    /**
     * An objective criteria argument
     *
     * @return argument
     */
    public static ArgumentType<Criteria> objectiveCriteria() {
        return provider().objectiveCriteria();
    }

    /**
     * An entity anchor argument.
     *
     * @return argument
     */
    public static ArgumentType<LookAnchor> entityAnchor() {
        return provider().entityAnchor();
    }

    /**
     * A time argument, returning the number of ticks.
     * <p>Examples:
     * <ul>
     * <li> "1d"
     * <li> "5s"
     * <li> "2"
     * <li> "6t"
     * </ul>
     *
     * @return argument
     */
    public static ArgumentType<Integer> time() {
        return time(0);
    }

    /**
     * A time argument, returning the number of ticks.
     * <p>Examples:
     * <ul>
     * <li> "1d"
     * <li> "5s"
     * <li> "2"
     * <li> "6t"
     * </ul>
     *
     * @param mintime The minimum time required for this argument.
     * @return argument
     */
    public static ArgumentType<Integer> time(final int mintime) {
        return provider().time(mintime);
    }

    /**
     * A template mirror argument
     *
     * @return argument
     * @see Mirror
     */
    public static ArgumentType<Mirror> templateMirror() {
        return provider().templateMirror();
    }

    /**
     * A template rotation argument.
     *
     * @return argument
     * @see StructureRotation
     */
    public static ArgumentType<StructureRotation> templateRotation() {
        return provider().templateRotation();
    }

    /**
     * An argument for a resource in a {@link org.bukkit.Registry}.
     *
     * @param registryKey the registry's key
     * @return argument
     * @param <T> the registry value type
     */
    public static <T> ArgumentType<T> resource(final RegistryKey<T> registryKey) {
        return provider().resource(registryKey);
    }

    /**
     * An argument for a typed key for a {@link org.bukkit.Registry}.
     *
     * @param registryKey the registry's key
     * @return argument
     * @param <T> the registry value type
     * @see RegistryArgumentExtractor#getTypedKey(com.mojang.brigadier.context.CommandContext, RegistryKey, String)
     */
    public static <T> ArgumentType<TypedKey<T>> resourceKey(final RegistryKey<T> registryKey) {
        return provider().resourceKey(registryKey);
    }

    private ArgumentTypes() {
    }
}
