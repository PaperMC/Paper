package io.papermc.paper.loot;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.util.MCUtil;
import io.papermc.paper.util.converter.Converter;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.util.Unit;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.loot.LootContext;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.converter.Converter.direct;
import static io.papermc.paper.util.converter.Converter.identity;
import static io.papermc.paper.util.converter.Converters.entity;
import static io.papermc.paper.util.converter.Converters.unvalued;
import static io.papermc.paper.util.converter.Converters.wrapper;

@NullMarked
public final class PaperLootContext {

    private static final Map<ContextKey<?>, Converter<?, ?>> CONVERTER_BY_KEY = new IdentityHashMap<>();

    private PaperLootContext() {
    }

    public static LootParams toVanilla(final LootContext context, final ContextKeySet params) {
        final LootParams.Builder builder = new LootParams.Builder(((CraftWorld) context.getWorld()).getHandle())
            .withLuck(context.getLuck());
        for (final LootContextKey key : context.keySet()) {
            final ContextKey<?> param = Key.CONVERTER.toVanilla(key);
            if (params.allowed().contains(param)) {
                applyToBuilder(builder, param, key instanceof final LootContextKey.Valued<?> valued ? context.get(valued) : null);
            }
        }

        return builder.create(params);
    }

    public static LootContext fromVanilla(final net.minecraft.world.level.storage.loot.LootContext context) {
        final LootContext.Builder builder = LootContext.builder(context.getLevel().getWorld())
            .withRandom(new RandomSourceWrapper.RandomWrapper(context.getRandom()))
            .luck(context.getLuck());
        for (final ContextKey<?> key : Key.BRIDGE.keySet()) { // not ideal
            final Object value = context.getOptionalParameter(key);
            if (value != null) {
                applyToApiBuilder(builder, key, value);
            }
        }

        return builder.build();
    }

    @SuppressWarnings("unchecked")
    public static <M, A> void applyToBuilder(final LootParams.Builder builder, final ContextKey<M> param, final @Nullable A value) {
        if (value == null) {
            builder.withParameter((ContextKey<Unit>) param, Unit.INSTANCE);
        } else {
            builder.withParameter(param, ((Converter<M, A>) CONVERTER_BY_KEY.get(param)).toVanilla(value));
        }
    }

    @SuppressWarnings("unchecked")
    public static <M, A> void applyToApiBuilder(final LootContext.Builder builder, final ContextKey<M> param, final Object value) {
        final LootContextKey key = Key.CONVERTER.fromVanilla(param);
        if (value == Unit.INSTANCE) {
            builder.with((LootContextKey.NonValued) key);
        } else {
            builder.with((LootContextKey.Valued<A>) key, ((Converter<M, A>) CONVERTER_BY_KEY.get(param)).fromVanilla((M) value));
        }
    }

    public static class Key {
        private static final BiMap<ContextKey<?>, LootContextKey> BRIDGE = HashBiMap.create();

        public static final Converter<ContextKey<?>, LootContextKey> CONVERTER = Converter.direct(BRIDGE::get, BRIDGE.inverse()::get);
        public static final Converter<Set<ContextKey<?>>, Set<LootContextKey>> SET_CONVERTER = CONVERTER.setOf();

        static {
            final Converter<Entity, CraftEntity> entity = entity(Entity.class, org.bukkit.craftbukkit.entity.CraftEntity.class);
            register(LootContextParams.THIS_ENTITY, entity);
            register(LootContextParams.INTERACTING_ENTITY, entity);
            register(LootContextParams.TARGET_ENTITY, entity);
            register(LootContextParams.LAST_DAMAGE_PLAYER, entity(Player.class, org.bukkit.craftbukkit.entity.CraftHumanEntity.class));
            register(LootContextParams.DAMAGE_SOURCE, wrapper(CraftDamageSource::new));
            register(LootContextParams.ATTACKING_ENTITY, entity);
            register(LootContextParams.DIRECT_ATTACKING_ENTITY, entity);
            register(LootContextParams.ORIGIN, direct(MCUtil::toPosition, MCUtil::toVec3));
            register(LootContextParams.BLOCK_STATE, direct(BlockState::asBlockData, CraftBlockData::getState));
            register(LootContextParams.BLOCK_ENTITY, direct(CraftBlockStates::getTileState, state -> ((CraftBlockEntityState<?>) state).getSnapshot()));
            register(LootContextParams.TOOL, direct(CraftItemStack::asBukkitCopy, CraftItemStack::asNMSCopy));
            register(LootContextParams.EXPLOSION_RADIUS, identity());
            register(LootContextParams.ENCHANTMENT_LEVEL, identity());
            register(LootContextParams.ENCHANTMENT_ACTIVE, identity());
            register(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED, unvalued());
        }

        private static <M, A> void register(final ContextKey<M> key, final Converter<M, A> converter) {
            BRIDGE.put(key, LootContextKeys.all().get(PaperAdventure.asAdventure(key.name())));
            CONVERTER_BY_KEY.put(key, converter);
        }
    }
}
