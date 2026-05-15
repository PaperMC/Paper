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

@SuppressWarnings("unused")
public final class LootContextKeys {

    public static final LootContextKey.Valued<Entity> THIS_ENTITY = valued("this_entity");
    public static final LootContextKey.Valued<Entity> INTERACTING_ENTITY = valued("interacting_entity");
    public static final LootContextKey.Valued<Entity> TARGET_ENTITY = valued("target_entity");
    public static final LootContextKey.Valued<HumanEntity> LAST_DAMAGE_PLAYER = valued("last_damage_player");
    public static final LootContextKey.Valued<DamageSource> DAMAGE_SOURCE = valued("damage_source");
    public static final LootContextKey.Valued<Entity> ATTACKING_ENTITY = valued("attacking_entity");
    public static final LootContextKey.Valued<Entity> DIRECT_ATTACKING_ENTITY = valued("direct_attacking_entity");
    public static final LootContextKey.Valued<Position> ORIGIN = valued("origin");
    public static final LootContextKey.Valued<BlockData> BLOCK_DATA = valued("block_state");
    public static final LootContextKey.Valued<TileState> BLOCK_ENTITY = valued("block_entity");
    public static final LootContextKey.Valued<ItemStack> TOOL = valued("tool");
    public static final LootContextKey.Valued<Float> EXPLOSION_RADIUS = valued("explosion_radius");
    public static final LootContextKey.Valued<Integer> ENCHANTMENT_LEVEL = valued("enchantment_level");
    public static final LootContextKey.Valued<Boolean> ENCHANTMENT_ACTIVE = valued("enchantment_active");
    public static final LootContextKey.NonValued ADDITIONAL_COST_COMPONENT_ALLOWED = unvalued("additional_cost_component_allowed");

    static Map<Key, LootContextKey> all() {
        return LootContextKeyImpl.KEYS;
    }
}
