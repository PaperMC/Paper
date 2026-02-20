package io.papermc.paper.loot;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.papermc.paper.util.MCUtil;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperLootContextKey {

    public static final BiMap<ContextKey<?>, LootContextKey<?>> KEY_BI_MAP = HashBiMap.create();
    private static final Set<Converter<?, ?>> CONVERTERS = new HashSet<>();
    private static final Map<ContextKey<?>, Converter<?, ?>> NMS_KEY_MAP = new IdentityHashMap<>();
    private static final Map<LootContextKey<?>, Converter<?, ?>> API_KEY_MAP = new IdentityHashMap<>();

    static {
        CONVERTERS.add(entity(LootContextParams.THIS_ENTITY, LootContextKey.THIS_ENTITY));
        CONVERTERS.add(entity(LootContextParams.INTERACTING_ENTITY, LootContextKey.INTERACTING_ENTITY));
        CONVERTERS.add(entity(LootContextParams.TARGET_ENTITY, LootContextKey.TARGET_ENTITY));
        CONVERTERS.add(entity(LootContextParams.LAST_DAMAGE_PLAYER, LootContextKey.LAST_DAMAGE_PLAYER));
        CONVERTERS.add(new LambdaConverter<>(LootContextParams.DAMAGE_SOURCE, LootContextKey.DAMAGE_SOURCE, ds -> ((CraftDamageSource) ds).getHandle(), CraftDamageSource::new));
        CONVERTERS.add(entity(LootContextParams.ATTACKING_ENTITY, LootContextKey.ATTACKING_ENTITY));
        CONVERTERS.add(entity(LootContextParams.DIRECT_ATTACKING_ENTITY, LootContextKey.DIRECT_ATTACKING_ENTITY));
        CONVERTERS.add(new LambdaConverter<>(LootContextParams.ORIGIN, LootContextKey.ORIGIN, MCUtil::toVec3, MCUtil::toPosition));
        CONVERTERS.add(new LambdaConverter<>(LootContextParams.BLOCK_STATE, LootContextKey.BLOCK_DATA, bd -> ((CraftBlockData) bd).getState(), BlockState::createCraftBlockData));
        CONVERTERS.add(new LambdaConverter<>(LootContextParams.BLOCK_ENTITY, LootContextKey.TILE_STATE, ts -> ((CraftBlockEntityState<?>) ts).getBlockEntity(), CraftBlockStates::getTileState));
        CONVERTERS.add(new LambdaConverter<>(LootContextParams.TOOL, LootContextKey.TOOL, CraftItemStack::asNMSCopy, net.minecraft.world.item.ItemStack::asBukkitCopy));
        CONVERTERS.add(identity(LootContextParams.EXPLOSION_RADIUS, LootContextKey.EXPLOSION_RADIUS));
        CONVERTERS.add(identity(LootContextParams.ENCHANTMENT_LEVEL, LootContextKey.ENCHANTMENT_LEVEL));
        CONVERTERS.add(identity(LootContextParams.ENCHANTMENT_ACTIVE, LootContextKey.ENCHANTMENT_ACTIVE));
        for (final Converter<?, ?> converter : CONVERTERS) {
            KEY_BI_MAP.put(converter.nmsKey, converter.apiKey);
            NMS_KEY_MAP.put(converter.nmsKey, converter);
            API_KEY_MAP.put(converter.apiKey, converter);
        }
    }

    private PaperLootContextKey() {
    }

    @SuppressWarnings("unchecked")
    public static <API, MINECRAFT> void applyToNmsBuilder(final ContextKeySet paramSet, final LootParams.Builder builder, final LootContextKey<API> apiKey, final Object object) {
        final ContextKey<MINECRAFT> nmsParam = (ContextKey<MINECRAFT>) KEY_BI_MAP.inverse().get(apiKey);
        if (paramSet.allowed().contains(nmsParam) || paramSet.required().contains(nmsParam)) {
            builder.withOptionalParameter(nmsParam, ((Converter<MINECRAFT, API>) API_KEY_MAP.get(apiKey)).toMinecraft((API) object));
        }
    }

    @SuppressWarnings("unchecked")
    public static <API, MINECRAFT> void applyToApiBuilder(final org.bukkit.loot.LootContext.Builder builder, final ContextKey<MINECRAFT> nmsKey, final Object object) {
        builder.with(((LootContextKey<API>) KEY_BI_MAP.get(nmsKey)), ((Converter<MINECRAFT, API>) NMS_KEY_MAP.get(nmsKey)).toApi((MINECRAFT) object));
    }

    abstract static class Converter<MINECRAFT, API> {

        final ContextKey<MINECRAFT> nmsKey;
        final LootContextKey<API> apiKey;

        private Converter(final ContextKey<MINECRAFT> nmsKey, final LootContextKey<API> apiKey) {
            this.nmsKey = nmsKey;
            this.apiKey = apiKey;
        }

        protected abstract MINECRAFT toMinecraft(API api);

        protected abstract API toApi(MINECRAFT minecraft);
    }

    static class LambdaConverter<MINECRAFT, API> extends Converter<MINECRAFT, API> {

        private final Function<API, MINECRAFT> toMinecraft;
        private final Function<MINECRAFT, API> toApi;

        private LambdaConverter(final ContextKey<MINECRAFT> nmsKey, final LootContextKey<API> apiKey, final Function<API, MINECRAFT> toMinecraft, final Function<MINECRAFT, API> toApi) {
            super(nmsKey, apiKey);
            this.toMinecraft = toMinecraft;
            this.toApi = toApi;
        }

        @Override
        protected MINECRAFT toMinecraft(final API api) {
            return this.toMinecraft.apply(api);
        }

        @Override
        protected API toApi(final MINECRAFT minecraft) {
            return this.toApi.apply(minecraft);
        }
    }

    private static <T> LambdaConverter<T, T> identity(final ContextKey<T> nmsKey, final LootContextKey<T> apiKey) {
        return new LambdaConverter<>(nmsKey, apiKey, Function.identity(), Function.identity());
    }

    @SuppressWarnings("unchecked")
    private static <MINECRAFT extends net.minecraft.world.entity.Entity, API extends Entity> LambdaConverter<MINECRAFT, API> entity(final ContextKey<MINECRAFT> nmsKey, final LootContextKey<API> apiKey) {
        return new LambdaConverter<>(nmsKey, apiKey, e -> (MINECRAFT) ((CraftEntity) e).getHandle(), e -> (API) e.getBukkitEntity());
    }
}
