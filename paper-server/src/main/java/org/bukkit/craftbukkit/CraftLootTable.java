package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.jspecify.annotations.Nullable;

public class CraftLootTable extends HolderableBase<LootTable> implements org.bukkit.loot.LootTable {

    private static final org.bukkit.loot.LootTable EMPTY = new CraftLootTable(Holder.direct(LootTable.EMPTY)); // todo expose?

    public CraftLootTable(final Holder<LootTable> holder) {
        super(holder);
    }

    public static org.bukkit.loot.LootTable minecraftKeyToBukkit(@Nullable ResourceKey<LootTable> minecraft) {
        return (minecraft == null) ? null : RegistryAccess.registryAccess().getRegistry(RegistryKey.LOOT_TABLE).get(PaperAdventure.asAdventureKey(minecraft));
    }

    public static ResourceKey<LootTable> bukkitToMinecraftKey(org.bukkit.loot.@Nullable LootTable bukkit) {
        return (bukkit == null || bukkit == EMPTY) ? null : PaperAdventure.asVanilla(Registries.LOOT_TABLE, bukkit.getKey());
    }

    public static org.bukkit.loot.LootTable minecraftToBukkit(LootTable minecraft) {
        if (minecraft == LootTable.EMPTY) {
            return EMPTY;
        }
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.LOOT_TABLE);
    }

    public static LootTable bukkitToMinecraft(org.bukkit.loot.LootTable bukkit) {
        if (bukkit == EMPTY) {
            return LootTable.EMPTY;
        }
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    @Override
    public Collection<ItemStack> populateLoot(@Nullable Random random, LootContext context) {
        Preconditions.checkArgument(context != null, "LootContext cannot be null");
        LootParams nmsContext = this.convertContext(context);
        List<net.minecraft.world.item.ItemStack> nmsItems = this.getHandle().getRandomItems(nmsContext, random == null ? null : new RandomSourceWrapper(random));
        Collection<ItemStack> bukkit = new ArrayList<>(nmsItems.size());

        for (net.minecraft.world.item.ItemStack item : nmsItems) {
            if (item.isEmpty()) {
                continue;
            }
            bukkit.add(CraftItemStack.asBukkitCopy(item));
        }

        return bukkit;
    }

    @Override
    public void fillInventory(Inventory inventory, @Nullable Random random, LootContext context) {
        Preconditions.checkArgument(inventory != null, "Inventory cannot be null");
        Preconditions.checkArgument(context != null, "LootContext cannot be null");
        LootParams nmsContext = this.convertContext(context);
        CraftInventory craftInventory = (CraftInventory) inventory;
        Container handle = craftInventory.getInventory();

        // TODO: When events are added, call event here w/ custom reason?
        this.getHandle().fill(handle, nmsContext, random == null ? null : new RandomSourceWrapper(random), true);
    }

    private LootParams convertContext(LootContext context) {
        Preconditions.checkArgument(context != null, "LootContext cannot be null");
        Location loc = context.getLocation();
        Preconditions.checkArgument(loc.getWorld() != null, "LootContext.getLocation#getWorld cannot be null");
        ServerLevel handle = ((CraftWorld) loc.getWorld()).getHandle();

        LootParams.Builder builder = new LootParams.Builder(handle);
        this.setMaybe(builder, LootContextParams.ORIGIN, CraftLocation.toVec3(loc));
        if (this.getHandle() != LootTable.EMPTY) {
            builder.withLuck(context.getLuck());

            if (context.getLootedEntity() != null) {
                Entity nmsLootedEntity = ((CraftEntity) context.getLootedEntity()).getHandle();
                this.setMaybe(builder, LootContextParams.THIS_ENTITY, nmsLootedEntity);
                this.setMaybe(builder, LootContextParams.DAMAGE_SOURCE, handle.damageSources().generic());
                this.setMaybe(builder, LootContextParams.ORIGIN, nmsLootedEntity.position());
            }

            if (context.getKiller() != null) {
                Player nmsKiller = ((CraftHumanEntity) context.getKiller()).getHandle();
                this.setMaybe(builder, LootContextParams.ATTACKING_ENTITY, nmsKiller);
                // If there is a player killer, damage source should reflect that in case loot tables use that information
                this.setMaybe(builder, LootContextParams.DAMAGE_SOURCE, handle.damageSources().playerAttack(nmsKiller));
                this.setMaybe(builder, LootContextParams.LAST_DAMAGE_PLAYER, nmsKiller); // SPIGOT-5603 - Set minecraft:killed_by_player
                this.setMaybe(builder, LootContextParams.TOOL, nmsKiller.getUseItem()); // SPIGOT-6925 - Set minecraft:match_tool
            }
        }

        // SPIGOT-5603 - Avoid IllegalArgumentException in ContextKeySet.Builder#create
        ContextKeySet.Builder nmsBuilder = new ContextKeySet.Builder();
        for (ContextKey<?> param : this.getHandle().getParamSet().required()) {
            nmsBuilder.required(param);
        }
        for (ContextKey<?> param : this.getHandle().getParamSet().allowed()) {
            if (!this.getHandle().getParamSet().required().contains(param)) {
                nmsBuilder.optional(param);
            }
        }

        return builder.create(this.getHandle().getParamSet());
    }

    private <T> void setMaybe(LootParams.Builder builder, ContextKey<T> param, T value) {
        if (this.getHandle().getParamSet().required().contains(param) || this.getHandle().getParamSet().allowed().contains(param)) {
            builder.withParameter(param, value);
        }
    }

    public static LootContext convertContext(net.minecraft.world.level.storage.loot.LootContext info) {
        Vec3 position = info.getOptionalParameter(LootContextParams.ORIGIN);
        if (position == null) {
            position = info.getOptionalParameter(LootContextParams.THIS_ENTITY).position(); // Every vanilla context has origin or this_entity, see LootContextParamSets
        }
        Location location = CraftLocation.toBukkit(position, info.getLevel());
        LootContext.Builder contextBuilder = new LootContext.Builder(location);

        if (info.hasParameter(LootContextParams.ATTACKING_ENTITY)) {
            CraftEntity killer = info.getOptionalParameter(LootContextParams.ATTACKING_ENTITY).getBukkitEntity();
            if (killer instanceof CraftHumanEntity) {
                contextBuilder.killer((CraftHumanEntity) killer);
            }
        }

        if (info.hasParameter(LootContextParams.THIS_ENTITY)) {
            contextBuilder.lootedEntity(info.getOptionalParameter(LootContextParams.THIS_ENTITY).getBukkitEntity());
        }

        contextBuilder.luck(info.getLuck());
        return contextBuilder.build();
    }
}
