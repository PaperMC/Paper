package io.papermc.paper.loot;

import io.papermc.paper.math.Position;
import net.kyori.adventure.key.Keyed;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import static io.papermc.paper.loot.LootContextKeyImpl.create;

/**
 * A key to a possible value in a {@link org.bukkit.loot.LootContext}.
 *
 * @param <T> the value type
 */
@NullMarked
@SuppressWarnings("unused")
public interface LootContextKey<T> extends Keyed {

    // todo: generate? but name doesn't match
    LootContextKey<Entity> THIS_ENTITY = create("this_entity");
    LootContextKey<Entity> INTERACTING_ENTITY = create("interacting_entity");
    LootContextKey<Entity> TARGET_ENTITY = create("target_entity");
    LootContextKey<Player> LAST_DAMAGE_PLAYER = create("last_damage_player");
    LootContextKey<DamageSource> DAMAGE_SOURCE = create("damage_source");
    LootContextKey<Entity> ATTACKING_ENTITY = create("attacking_entity");
    LootContextKey<Entity> DIRECT_ATTACKING_ENTITY = create("direct_attacking_entity");
    LootContextKey<Position> ORIGIN = create("origin");
    LootContextKey<BlockData> BLOCK_DATA = create("block_state");
    LootContextKey<TileState> TILE_STATE = create("block_entity");
    LootContextKey<ItemStack> TOOL = create("tool");
    LootContextKey<Float> EXPLOSION_RADIUS = create("explosion_radius");
    LootContextKey<Integer> ENCHANTMENT_LEVEL = create("enchantment_level");
    LootContextKey<Boolean> ENCHANTMENT_ACTIVE = create("enchantment_active");
}
