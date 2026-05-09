package io.papermc.paper.loot;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.papermc.paper.util.MCUtil;
import io.papermc.paper.util.converter.Converter;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.util.Unit;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.loot.LootContext;
import org.jetbrains.annotations.VisibleForTesting;
import org.jspecify.annotations.NullMarked;

import static io.papermc.paper.util.converter.Converter.direct;
import static io.papermc.paper.util.converter.Converter.identity;
import static io.papermc.paper.util.converter.Converters.entity;
import static io.papermc.paper.util.converter.Converters.unvalued;
import static io.papermc.paper.util.converter.Converters.wrapper;

@NullMarked
public final class PaperLootContextKey {

    @VisibleForTesting
    public static final BiMap<ContextKey<?>, LootContextKey> KEY_BRIDGE = HashBiMap.create();
    private static final Map<ContextKey<?>, Converter<?, ?>> CONVERTER_BY_KEY = new IdentityHashMap<>();
    private static final Map<LootContextKey, Converter<?, ?>> CONVERTER_BY_API_KEY = new IdentityHashMap<>();

    static {
        final Converter<Entity, CraftEntity> entity = entity(Entity.class, org.bukkit.craftbukkit.entity.CraftEntity.class);
        register(LootContextParams.THIS_ENTITY, LootContextKeys.THIS_ENTITY, entity);
        register(LootContextParams.INTERACTING_ENTITY, LootContextKeys.INTERACTING_ENTITY, entity);
        register(LootContextParams.TARGET_ENTITY, LootContextKeys.TARGET_ENTITY, entity);
        register(LootContextParams.LAST_DAMAGE_PLAYER, LootContextKeys.LAST_DAMAGE_PLAYER, entity(Player.class, org.bukkit.craftbukkit.entity.CraftHumanEntity.class));
        register(LootContextParams.DAMAGE_SOURCE, LootContextKeys.DAMAGE_SOURCE, wrapper(CraftDamageSource::new));
        register(LootContextParams.ATTACKING_ENTITY, LootContextKeys.ATTACKING_ENTITY, entity);
        register(LootContextParams.DIRECT_ATTACKING_ENTITY, LootContextKeys.DIRECT_ATTACKING_ENTITY, entity);
        register(LootContextParams.ORIGIN, LootContextKeys.ORIGIN, direct(MCUtil::toPosition, MCUtil::toVec3));
        register(LootContextParams.BLOCK_STATE, LootContextKeys.BLOCK_DATA, direct(BlockState::asBlockData, CraftBlockData::getState));
        register(LootContextParams.BLOCK_ENTITY, LootContextKeys.BLOCK_ENTITY, direct(CraftBlockStates::getTileState, state -> ((CraftBlockEntityState<?>) state).getSnapshot()));
        register(LootContextParams.TOOL, LootContextKeys.TOOL, direct(CraftItemStack::asBukkitCopy, CraftItemStack::asNMSCopy));
        register(LootContextParams.EXPLOSION_RADIUS, LootContextKeys.EXPLOSION_RADIUS, identity());
        register(LootContextParams.ENCHANTMENT_LEVEL, LootContextKeys.ENCHANTMENT_LEVEL, identity());
        register(LootContextParams.ENCHANTMENT_ACTIVE, LootContextKeys.ENCHANTMENT_ACTIVE, identity());
        register(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED, LootContextKeys.ADDITIONAL_COST_COMPONENT_ALLOWED, unvalued());
    }

    private static <M, A> void register(final ContextKey<M> key, final LootContextKey.Valued<A> apiKey, final Converter<M, ? extends A> converter) {
        registerInternal(key, apiKey, converter);
    }

    private static <M, A> void register(final ContextKey<M> key, final LootContextKey.NonValued apiKey, final Converter<M, ?> converter) {
        registerInternal(key, apiKey, converter);
    }

    private static <M, A> void registerInternal(final ContextKey<M> key, final LootContextKey apiKey, final Converter<?, ?> converter) {
        KEY_BRIDGE.put(key, apiKey);
        CONVERTER_BY_KEY.put(key, converter);
        CONVERTER_BY_API_KEY.put(apiKey, converter);
    }

    private PaperLootContextKey() {
    }

    @SuppressWarnings("unchecked")
    public static <API> void applyToBuilder(final Set<ContextKey<?>> allowedKeys, final LootParams.Builder builder, final LootContextKey key, final Optional<?> value) {
        final ContextKey<Object> param = (ContextKey<Object>) KEY_BRIDGE.inverse().get(key);
        if (allowedKeys.contains(param)) {
            builder.withParameter(param, value.map(v -> ((Converter<Object, API>) CONVERTER_BY_API_KEY.get(key)).toVanilla((API) v)).orElse(Unit.INSTANCE));
        }
    }

    @SuppressWarnings("unchecked")
    public static <API, MINECRAFT> void applyToApiBuilder(final LootContext.Builder builder, final ContextKey<MINECRAFT> key, final Object object) {
        if (object == Unit.INSTANCE) {
            builder.with((LootContextKey.NonValued) KEY_BRIDGE.get(key));
        } else {
            builder.with((LootContextKey.Valued<API>) KEY_BRIDGE.get(key), ((Converter<MINECRAFT, API>) CONVERTER_BY_KEY.get(key)).fromVanilla((MINECRAFT) object));
        }
    }
}
