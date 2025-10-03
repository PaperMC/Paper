package io.papermc.paper.command.brigadier.argument;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.google.common.collect.Collections2;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.command.brigadier.PaperCommands;
import io.papermc.paper.command.brigadier.argument.predicate.BlockInWorldPredicate;
import io.papermc.paper.command.brigadier.argument.predicate.ItemStackPredicate;
import io.papermc.paper.command.brigadier.argument.range.DoubleRangeProvider;
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider;
import io.papermc.paper.command.brigadier.argument.range.RangeProvider;
import io.papermc.paper.command.brigadier.argument.resolvers.AngleResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.ColumnBlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.ColumnFinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.RotationResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.command.brigadier.position.ColumnBlockPositionImpl;
import io.papermc.paper.command.brigadier.position.ColumnFinePositionImpl;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.math.Rotation;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.util.MCUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.HeightmapTypeArgument;
import net.minecraft.commands.arguments.HexColorArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.ScoreboardSlotArgument;
import net.minecraft.commands.arguments.StyleArgument;
import net.minecraft.commands.arguments.TemplateMirrorArgument;
import net.minecraft.commands.arguments.TemplateRotationArgument;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Axis;
import org.bukkit.GameMode;
import org.bukkit.HeightMap;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.craftbukkit.CraftHeightMap;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.scoreboard.CraftCriteria;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboardTranslations;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import static java.util.Objects.requireNonNull;

@DefaultQualifier(NonNull.class)
public class VanillaArgumentProviderImpl implements VanillaArgumentProvider {

    @Override
    public ArgumentType<EntitySelectorArgumentResolver> entity() {
        return this.wrap(EntityArgument.entity(), (result) -> sourceStack -> {
            return List.of(result.findSingleEntity((CommandSourceStack) sourceStack).getBukkitEntity());
        });
    }

    @Override
    public ArgumentType<EntitySelectorArgumentResolver> entities() {
        return this.wrap(EntityArgument.entities(), (result) -> sourceStack -> {
            return Lists.transform(result.findEntities((CommandSourceStack) sourceStack), net.minecraft.world.entity.Entity::getBukkitEntity);
        });
    }

    @Override
    public ArgumentType<PlayerSelectorArgumentResolver> player() {
        return this.wrap(EntityArgument.player(), (result) -> sourceStack -> {
            return List.of(result.findSinglePlayer((CommandSourceStack) sourceStack).getBukkitEntity());
        });
    }

    @Override
    public ArgumentType<PlayerSelectorArgumentResolver> players() {
        return this.wrap(EntityArgument.players(), (result) -> sourceStack -> {
            return Lists.transform(result.findPlayers((CommandSourceStack) sourceStack), ServerPlayer::getBukkitEntity);
        });
    }

    @Override
    public ArgumentType<PlayerProfileListResolver> playerProfiles() {
        return this.wrap(GameProfileArgument.gameProfile(), result -> {
            if (result instanceof GameProfileArgument.SelectorResult) {
                return sourceStack -> Collections.unmodifiableCollection(Collections2.transform(result.getNames((CommandSourceStack) sourceStack), CraftPlayerProfile::new));
            } else {
                return sourceStack -> Collections.unmodifiableCollection(Collections2.transform(result.getNames((CommandSourceStack) sourceStack), CraftPlayerProfile::new));
            }
        });
    }

    @Override
    public ArgumentType<BlockPositionResolver> blockPosition() {
        return this.wrap(BlockPosArgument.blockPos(), (result) -> sourceStack -> {
            final BlockPos pos = result.getBlockPos((CommandSourceStack) sourceStack);

            return MCUtil.toPosition(pos);
        });
    }

    @Override
    public ArgumentType<ColumnBlockPositionResolver> columnBlockPosition() {
        return this.wrap(ColumnPosArgument.columnPos(), (result) -> sourceStack -> {
            final BlockPos pos = result.getBlockPos((CommandSourceStack) sourceStack);

            return new ColumnBlockPositionImpl(pos.getX(), pos.getZ());
        });
    }

    @Override
    public ArgumentType<BlockInWorldPredicate> blockInWorldPredicate() {
        return this.wrap(BlockPredicateArgument.blockPredicate(PaperCommands.INSTANCE.getBuildContext()),
            result -> (block, loadChunk) -> {
                final BlockInWorld blockInWorld = new BlockInWorld(
                    ((CraftWorld) block.getWorld()).getHandle(),
                    CraftLocation.toBlockPosition(block.getLocation()),
                    loadChunk
                );
                // Get state lazy loads the state, will remain null if chunk is unloaded.
                if (blockInWorld.getState() == null) return BlockInWorldPredicate.Result.UNLOADED_CHUNK;

                return result.test(blockInWorld) ? BlockInWorldPredicate.Result.TRUE : BlockInWorldPredicate.Result.FALSE;
            }
        );
    }

    @Override
    public ArgumentType<FinePositionResolver> finePosition(final boolean centerIntegers) {
        return this.wrap(Vec3Argument.vec3(centerIntegers), (result) -> sourceStack -> {
            final Vec3 vec3 = result.getPosition((CommandSourceStack) sourceStack);

            return MCUtil.toPosition(vec3);
        });
    }

    @Override
    public ArgumentType<ColumnFinePositionResolver> columnFinePosition(final boolean centerIntegers) {
        return this.wrap(Vec2Argument.vec2(centerIntegers), (result) -> sourceStack -> {
            final Vec3 vec3 = result.getPosition((CommandSourceStack) sourceStack);

            return new ColumnFinePositionImpl(vec3.x(), vec3.z());
        });
    }

    @Override
    public ArgumentType<RotationResolver> rotation() {
        return this.wrap(RotationArgument.rotation(), (result) -> sourceStack -> {
            final Vec2 vec2 = result.getRotation((CommandSourceStack) sourceStack);

            return Rotation.rotation(vec2.y, vec2.x);
        });
    }

    @Override
    public ArgumentType<AngleResolver> angle() {
        return this.wrap(AngleArgument.angle(), (result) -> sourceStack -> result.getAngle((CommandSourceStack) sourceStack));
    }

    @Override
    public ArgumentType<AxisSet> axes() {
        final class AxisSetImpl extends ForwardingSet<Axis> implements AxisSet {
            private final EnumSet<Axis> inner;

            public AxisSetImpl(final EnumSet<Axis> inner) {
                this.inner = inner;
            }

            @Override
            protected Set<Axis> delegate() {
                return this.inner;
            }
        }

        return this.wrap(SwizzleArgument.swizzle(), (result) -> {
            final EnumSet<Axis> bukkitAxes = EnumSet.noneOf(Axis.class);
            for (final Direction.Axis nmsAxis : result) {
                bukkitAxes.add(CraftBlockData.toBukkit(nmsAxis, Axis.class));
            }
            return new AxisSetImpl(bukkitAxes);
        });
    }

    @Override
    public ArgumentType<BlockState> blockState() {
        return this.wrap(BlockStateArgument.block(PaperCommands.INSTANCE.getBuildContext()), (result) -> {
            final BlockState snapshot = CraftBlockStates.getBlockState(CraftRegistry.getMinecraftRegistry(), BlockPos.ZERO, result.getState(), null);
            if (result.tag != null && snapshot instanceof final CraftBlockEntityState<?> blockEntitySnapshot) {
                blockEntitySnapshot.loadData(result.tag);
            }
            return snapshot;
        });
    }

    @Override
    public ArgumentType<ItemStack> itemStack() {
        return this.wrap(ItemArgument.item(PaperCommands.INSTANCE.getBuildContext()), (result) -> {
            return CraftItemStack.asBukkitCopy(result.createItemStack(1, true));
        });
    }

    @Override
    public ArgumentType<ItemStackPredicate> itemStackPredicate() {
        return this.wrap(ItemPredicateArgument.itemPredicate(PaperCommands.INSTANCE.getBuildContext()), type -> itemStack -> type.test(CraftItemStack.asNMSCopy(itemStack)));
    }

    @Override
    public ArgumentType<NamedTextColor> namedColor() {
        return this.wrap(ColorArgument.color(), result ->
            requireNonNull(
                NamedTextColor.namedColor(
                    requireNonNull(result.getColor(), () -> result + " didn't have a color")
                ),
                () -> result.getColor() + " didn't map to an adventure named color"
            )
        );
    }

    @Override
    public ArgumentType<TextColor> hexColor() {
        return this.wrap(HexColorArgument.hexColor(), TextColor::color);
    }

    @Override
    public ArgumentType<Component> component() {
        return this.wrap(ComponentArgument.textComponent(PaperCommands.INSTANCE.getBuildContext()), PaperAdventure::asAdventure);
    }

    @Override
    public ArgumentType<Style> style() {
        return this.wrap(StyleArgument.style(PaperCommands.INSTANCE.getBuildContext()), PaperAdventure::asAdventure);
    }

    @Override
    public ArgumentType<SignedMessageResolver> signedMessage() {
        return this.wrap(MessageArgument.message(), SignedMessageResolverImpl::new);
    }

    @Override
    public ArgumentType<DisplaySlot> scoreboardDisplaySlot() {
        return this.wrap(ScoreboardSlotArgument.displaySlot(), CraftScoreboardTranslations::toBukkitSlot);
    }

    @Override
    public ArgumentType<NamespacedKey> namespacedKey() {
        return this.wrap(ResourceLocationArgument.id(), CraftNamespacedKey::fromMinecraft);
    }

    @Override
    public ArgumentType<Key> key() {
        return this.wrap(ResourceLocationArgument.id(), CraftNamespacedKey::fromMinecraft);
    }

    @Override
    public ArgumentType<IntegerRangeProvider> integerRange() {
        return this.wrap(RangeArgument.intRange(), type -> VanillaArgumentProviderImpl.convertToRange(type, integerRange -> () -> integerRange));
    }

    @Override
    public ArgumentType<DoubleRangeProvider> doubleRange() {
        return this.wrap(RangeArgument.floatRange(), type -> VanillaArgumentProviderImpl.convertToRange(type, doubleRange -> () -> doubleRange));
    }

    private static <C extends Number & Comparable<C>, T extends RangeProvider<C>> T convertToRange(final MinMaxBounds<C> bounds, final Function<Range<C>, T> converter) {
        if (bounds.isAny()) {
            return converter.apply(Range.all());
        } else if (bounds.min().isPresent() && bounds.max().isPresent()) {
            return converter.apply(Range.closed(bounds.min().get(), bounds.max().get()));
        } else if (bounds.max().isPresent()) {
            return converter.apply(Range.atMost(bounds.max().get()));
        } else if (bounds.min().isPresent()) {
            return converter.apply(Range.atLeast(bounds.min().get()));
        }
        throw new IllegalStateException("This is a bug: " + bounds);
    }

    @Override
    public ArgumentType<World> world() {
        return this.wrap(DimensionArgument.dimension(), dimensionLocation -> {
            // based on DimensionArgument#getDimension
            final ResourceKey<Level> resourceKey = ResourceKey.create(Registries.DIMENSION, dimensionLocation);
            final @Nullable ServerLevel serverLevel = MinecraftServer.getServer().getLevel(resourceKey);
            if (serverLevel == null) {
                throw DimensionArgument.ERROR_INVALID_VALUE.create(dimensionLocation);
            } else {
                return serverLevel.getWorld();
            }
        });
    }

    @Override
    public ArgumentType<GameMode> gameMode() {
        return this.wrap(GameModeArgument.gameMode(), type -> requireNonNull(GameMode.getByValue(type.getId())));
    }

    @Override
    public ArgumentType<HeightMap> heightMap() {
        return this.wrap(HeightmapTypeArgument.heightmap(), CraftHeightMap::fromNMS);
    }

    @Override
    public ArgumentType<UUID> uuid() {
        return this.wrap(UuidArgument.uuid());
    }

    @Override
    public ArgumentType<Criteria> objectiveCriteria() {
        return this.wrap(ObjectiveCriteriaArgument.criteria(), CraftCriteria::getFromNMS);
    }

    @Override
    public ArgumentType<LookAnchor> entityAnchor() {
        return this.wrap(EntityAnchorArgument.anchor(), type -> LookAnchor.valueOf(type.name()));
    }

    @Override
    public ArgumentType<Integer> time(final int minTicks) {
        return this.wrap(TimeArgument.time(minTicks));
    }

    @Override
    public ArgumentType<Mirror> templateMirror() {
        return this.wrap(TemplateMirrorArgument.templateMirror(), mirror -> Mirror.valueOf(mirror.name()));
    }

    @Override
    public ArgumentType<StructureRotation> templateRotation() {
        return this.wrap(TemplateRotationArgument.templateRotation(), mirror -> StructureRotation.valueOf(mirror.name()));
    }

    @Override
    public <T> ArgumentType<TypedKey<T>> resourceKey(final RegistryKey<T> registryKey) {
        return this.wrap(
            ResourceKeyArgument.key(PaperRegistries.registryToNms(registryKey)),
            nmsRegistryKey -> TypedKey.create(registryKey, CraftNamespacedKey.fromMinecraft(nmsRegistryKey.location()))
        );
    }

    @Override
    public <T> ArgumentType<T> resource(final RegistryKey<T> registryKey) {
        return this.resourceRaw(registryKey);
    }

    @SuppressWarnings({"unchecked", "rawtypes", "UnnecessaryLocalVariable"})
    private <T, K extends Keyed> ArgumentType<T> resourceRaw(final RegistryKey registryKeyRaw) { // TODO remove Keyed
        final RegistryKey<K> registryKey = registryKeyRaw;
        return (ArgumentType<T>) this.wrap(
            ResourceArgument.resource(PaperCommands.INSTANCE.getBuildContext(), PaperRegistries.registryToNms(registryKey)),
            resource -> requireNonNull(
                RegistryAccess.registryAccess()
                    .getRegistry(registryKey)
                    .get(CraftNamespacedKey.fromMinecraft(resource.key().location()))
            )
        );
    }

    private <T> ArgumentType<T> wrap(final ArgumentType<T> base) {
        return this.wrap(base, identity -> identity);
    }

    private <B, C> ArgumentType<C> wrap(final ArgumentType<B> base, final ResultConverter<B, C> converter) {
        return new NativeWrapperArgumentType<>(base, converter);
    }

    @FunctionalInterface
    interface ResultConverter<T, R> {

        R convert(T type) throws CommandSyntaxException;
    }

    public static final class NativeWrapperArgumentType<M, P> implements ArgumentType<P> {

        private final ArgumentType<M> nmsBase;
        private final ResultConverter<M, P> converter;

        private NativeWrapperArgumentType(final ArgumentType<M> nmsBase, final ResultConverter<M, P> converter) {
            this.nmsBase = nmsBase;
            this.converter = converter;
        }

        public ArgumentType<M> nativeNmsArgumentType() {
            return this.nmsBase;
        }

        @Override
        public P parse(final StringReader reader) throws CommandSyntaxException {
            return this.converter.convert(this.nmsBase.parse(reader));
        }

        @Override
        public <S> P parse(final StringReader reader, final S source) throws CommandSyntaxException {
            return this.converter.convert(this.nmsBase.parse(reader, source));
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
            return this.nmsBase.listSuggestions(context, builder);
        }

        @Override
        public Collection<String> getExamples() {
            return this.nmsBase.getExamples();
        }
    }
}
