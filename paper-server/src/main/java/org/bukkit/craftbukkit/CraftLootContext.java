package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.loot.LootContext;

public final class CraftLootContext {

    public static LootParams createLootParams(final LootContext context, final ContextKeySet paramSet, final boolean empty) {
        Preconditions.checkArgument(context != null, "LootContext cannot be null");
        final Location loc = context.getLocation();
        Preconditions.checkArgument(loc.getWorld() != null, "LootContext.getLocation#getWorld cannot be null");
        final ServerLevel handle = ((CraftWorld) loc.getWorld()).getHandle();

        final LootParams.Builder builder = new LootParams.Builder(handle);
        setMaybe(builder, paramSet, LootContextParams.ORIGIN, CraftLocation.toVec3(loc));

        final BlockState blockState = ((CraftBlockState) loc.getBlock().getState()).getHandle();
        setMaybe(builder, paramSet, LootContextParams.BLOCK_STATE, blockState);
        if (blockState.hasBlockEntity()) {
            setMaybe(builder, paramSet, LootContextParams.BLOCK_ENTITY, handle.getBlockEntity(CraftLocation.toBlockPos(loc)));
        }

        if (!empty) {
            builder.withLuck(context.getLuck());

            if (context.getLootedEntity() instanceof CraftEntity craftLootedEntity) {
                Entity nmsLootedEntity = craftLootedEntity.getHandle();
                setMaybe(builder, paramSet, LootContextParams.THIS_ENTITY, nmsLootedEntity);
                setMaybe(builder, paramSet, LootContextParams.DAMAGE_SOURCE, handle.damageSources().generic());
                setMaybe(builder, paramSet, LootContextParams.ORIGIN, nmsLootedEntity.position());
            }

            if (context.getKiller() instanceof CraftHumanEntity craftHumanKiller) {
                Player nmsKiller = craftHumanKiller.getHandle();
                setMaybe(builder, paramSet, LootContextParams.ATTACKING_ENTITY, nmsKiller);
                // If there is a player killer, damage source should reflect that in case loot tables use that information
                setMaybe(builder, paramSet, LootContextParams.DAMAGE_SOURCE, handle.damageSources().playerAttack(nmsKiller));
                setMaybe(builder, paramSet, LootContextParams.LAST_DAMAGE_PLAYER, nmsKiller); // SPIGOT-5603 - Set minecraft:killed_by_player
                setMaybe(builder, paramSet, LootContextParams.TOOL, nmsKiller.getUseItem()); // SPIGOT-6925 - Set minecraft:match_tool
            }
        }

        // SPIGOT-5603 - Avoid IllegalArgumentException in ContextKeySet.Builder#create
        final ContextKeySet.Builder nmsBuilder = new ContextKeySet.Builder();
        for (final ContextKey<?> param : paramSet.required()) {
            nmsBuilder.required(param);
        }
        for (final ContextKey<?> param : paramSet.allowed()) {
            if (!paramSet.required().contains(param)) {
                nmsBuilder.optional(param);
            }
        }

        return builder.create(paramSet);
    }

    private static <T> void setMaybe(final LootParams.Builder builder, final ContextKeySet paramSet, final ContextKey<T> param, final T value) {
        if (paramSet.required().contains(param) || paramSet.allowed().contains(param)) {
            builder.withParameter(param, value);
        }
    }

    public static LootContext toBukkit(final net.minecraft.world.level.storage.loot.LootContext info) {
        final Entity entity = info.getOptional(LootContextParams.THIS_ENTITY);

        Vec3 position = info.getOptional(LootContextParams.ORIGIN);
        // Every vanilla context has origin or this_entity, see LootContextParamSets but can be Optional so need to check for null
        if (position == null && entity != null) {
            position = entity.position();
        }
        final Location location = CraftLocation.toBukkit(position, info.getLevel());

        final LootContext.Builder contextBuilder = new LootContext.Builder(location);

        final Entity killer = info.getOptional(LootContextParams.ATTACKING_ENTITY);
        if (killer != null && killer.getBukkitEntity() instanceof CraftHumanEntity craftHumanKiller) {
            contextBuilder.killer(craftHumanKiller);
        }

        if (entity != null) {
            contextBuilder.lootedEntity(entity.getBukkitEntity());
        }

        contextBuilder.luck(info.getLuck());
        return contextBuilder.build();
    }

    private CraftLootContext() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

}
