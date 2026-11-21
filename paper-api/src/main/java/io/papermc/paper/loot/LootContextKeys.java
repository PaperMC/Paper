package io.papermc.paper.loot;

import io.papermc.paper.math.Position;
import java.util.Map;
import net.kyori.adventure.key.Key;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import static io.papermc.paper.loot.LootContextKeyImpl.unvalued;
import static io.papermc.paper.loot.LootContextKeyImpl.valued;

/**
 * All loot context keys.
 */
@SuppressWarnings("unused")
public final class LootContextKeys {

    /**
     * The entity the context is about, for example the entity that died or the player that opened a loot container.
     */
    public static final LootContextKey.Valued<Entity> THIS_ENTITY = valued("this_entity");

    /**
     * The entity interacting with a block or with the {@link #TARGET_ENTITY target entity}, for example the player shearing a sheep.
     */
    public static final LootContextKey.Valued<Entity> INTERACTING_ENTITY = valued("interacting_entity");

    /**
     * The entity being interacted with, for example the sheep being sheared.
     */
    public static final LootContextKey.Valued<Entity> TARGET_ENTITY = valued("target_entity");

    /**
     * The player that last damaged the entity that died.
     */
    public static final LootContextKey.Valued<HumanEntity> LAST_DAMAGE_PLAYER = valued("last_damage_player");

    /**
     * The source of the damage, for example the damage that killed an entity.
     */
    public static final LootContextKey.Valued<DamageSource> DAMAGE_SOURCE = valued("damage_source");

    /**
     * The entity responsible for the damage, for example the player that shot an arrow.
     */
    public static final LootContextKey.Valued<Entity> ATTACKING_ENTITY = valued("attacking_entity");

    /**
     * The entity that directly dealt the damage, for example the arrow that hit.
     */
    public static final LootContextKey.Valued<Entity> DIRECT_ATTACKING_ENTITY = valued("direct_attacking_entity");

    /**
     * The position the loot is generated at.
     */
    public static final LootContextKey.Valued<Position> ORIGIN = valued("origin");

    /**
     * The block involved, for example the block that was broken.
     */
    public static final LootContextKey.Valued<BlockData> BLOCK_DATA = valued("block_state");

    /**
     * The block entity involved, for example the block entity of the block that was broken.
     */
    public static final LootContextKey.Valued<TileState> BLOCK_ENTITY = valued("block_entity");

    /**
     * The item used, for example the tool used to break a block.
     */
    public static final LootContextKey.Valued<ItemStack> TOOL = valued("tool");

    /**
     * The radius of the explosion that destroyed a block, which decides how many of its drops survive.
     */
    public static final LootContextKey.Valued<Float> EXPLOSION_RADIUS = valued("explosion_radius");

    /**
     * The level of the enchantment whose effect is being evaluated.
     */
    public static final LootContextKey.Valued<Integer> ENCHANTMENT_LEVEL = valued("enchantment_level");

    /**
     * Whether the location-based enchantment effect being evaluated is currently active.
     */
    public static final LootContextKey.Valued<Boolean> ENCHANTMENT_ACTIVE = valued("enchantment_active");

    /**
     * Present when generating villager trades, so enchanting functions can add a trade cost to the item.
     */
    public static final LootContextKey.NonValued ADDITIONAL_COST_COMPONENT_ALLOWED = unvalued("additional_cost_component_allowed");

    private LootContextKeys() {
    }

    static Map<Key, LootContextKey> all() {
        return LootContextKeyImpl.KEYS;
    }
}
