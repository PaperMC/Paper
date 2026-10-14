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
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
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

@ApiStatus.Internal
interface VanillaArgumentProvider {

    Optional<VanillaArgumentProvider> PROVIDER = ServiceLoader.load(VanillaArgumentProvider.class, VanillaArgumentProvider.class.getClassLoader()).findFirst();

    static VanillaArgumentProvider provider() {
        return PROVIDER.orElseThrow();
    }

    ArgumentType<EntitySelectorArgumentResolver> entity();

    ArgumentType<PlayerSelectorArgumentResolver> player();

    ArgumentType<EntitySelectorArgumentResolver> entities();

    ArgumentType<PlayerSelectorArgumentResolver> players();

    ArgumentType<PlayerProfileListResolver> playerProfiles();

    ArgumentType<BlockPositionResolver> blockPosition();

    ArgumentType<ColumnBlockPositionResolver> columnBlockPosition();

    ArgumentType<FinePositionResolver> finePosition(boolean centerIntegers);

    ArgumentType<ColumnFinePositionResolver> columnFinePosition(boolean centerIntegers);

    ArgumentType<RotationResolver> rotation();

    ArgumentType<AngleResolver> angle();

    ArgumentType<AxisSet> axes();

    ArgumentType<BlockState> blockState();

    ArgumentType<BlockInWorldPredicate> blockInWorldPredicate();

    ArgumentType<ItemStack> itemStack();

    ArgumentType<ItemStackPredicate> itemStackPredicate();

    ArgumentType<NamedTextColor> namedColor();

    ArgumentType<TextColor> hexColor();

    ArgumentType<Component> component();

    ArgumentType<Style> style();

    ArgumentType<SignedMessageResolver> signedMessage();

    ArgumentType<DisplaySlot> scoreboardDisplaySlot();

    ArgumentType<NamespacedKey> namespacedKey();

    // include both key types as we are slowly moving to use adventure's key
    ArgumentType<Key> key();

    ArgumentType<IntegerRangeProvider> integerRange();

    ArgumentType<DoubleRangeProvider> doubleRange();

    ArgumentType<World> world();

    ArgumentType<GameMode> gameMode();

    ArgumentType<HeightMap> heightMap();

    ArgumentType<UUID> uuid();

    ArgumentType<Criteria> objectiveCriteria();

    ArgumentType<LookAnchor> entityAnchor();

    ArgumentType<Integer> time(int minTicks);

    ArgumentType<Mirror> templateMirror();

    ArgumentType<StructureRotation> templateRotation();

    <T> ArgumentType<TypedKey<T>> resourceKey(RegistryKey<T> registryKey);

    <T> ArgumentType<T> resource(RegistryKey<T> registryKey);
}
