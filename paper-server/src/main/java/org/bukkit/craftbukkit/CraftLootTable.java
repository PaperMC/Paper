package org.bukkit.craftbukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.IInventory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameter;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameterSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameters;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

public class CraftLootTable implements org.bukkit.loot.LootTable {

    private final LootTable handle;
    private final NamespacedKey key;

    public CraftLootTable(NamespacedKey key, LootTable handle) {
        this.handle = handle;
        this.key = key;
    }

    public LootTable getHandle() {
        return handle;
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext context) {
        LootTableInfo nmsContext = convertContext(context, random);
        List<net.minecraft.world.item.ItemStack> nmsItems = handle.getRandomItems(nmsContext);
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
    public void fillInventory(Inventory inventory, Random random, LootContext context) {
        LootTableInfo nmsContext = convertContext(context, random);
        CraftInventory craftInventory = (CraftInventory) inventory;
        IInventory handle = craftInventory.getInventory();

        // TODO: When events are added, call event here w/ custom reason?
        getHandle().fill(handle, nmsContext);
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    private LootTableInfo convertContext(LootContext context, Random random) {
        Location loc = context.getLocation();
        WorldServer handle = ((CraftWorld) loc.getWorld()).getHandle();

        LootTableInfo.Builder builder = new LootTableInfo.Builder(handle);
        if (random != null) {
            builder = builder.withRandom(new RandomSourceWrapper(random));
        }
        setMaybe(builder, LootContextParameters.ORIGIN, new Vec3D(loc.getX(), loc.getY(), loc.getZ()));
        if (getHandle() != LootTable.EMPTY) {
            // builder.luck(context.getLuck());

            if (context.getLootedEntity() != null) {
                Entity nmsLootedEntity = ((CraftEntity) context.getLootedEntity()).getHandle();
                setMaybe(builder, LootContextParameters.THIS_ENTITY, nmsLootedEntity);
                setMaybe(builder, LootContextParameters.DAMAGE_SOURCE, DamageSource.GENERIC);
                setMaybe(builder, LootContextParameters.ORIGIN, nmsLootedEntity.position());
            }

            if (context.getKiller() != null) {
                EntityHuman nmsKiller = ((CraftHumanEntity) context.getKiller()).getHandle();
                setMaybe(builder, LootContextParameters.KILLER_ENTITY, nmsKiller);
                // If there is a player killer, damage source should reflect that in case loot tables use that information
                setMaybe(builder, LootContextParameters.DAMAGE_SOURCE, DamageSource.playerAttack(nmsKiller));
                setMaybe(builder, LootContextParameters.LAST_DAMAGE_PLAYER, nmsKiller); // SPIGOT-5603 - Set minecraft:killed_by_player
                setMaybe(builder, LootContextParameters.TOOL, nmsKiller.getUseItem()); // SPIGOT-6925 - Set minecraft:match_tool
            }

            // SPIGOT-5603 - Use LootContext#lootingModifier
            if (context.getLootingModifier() != LootContext.DEFAULT_LOOT_MODIFIER) {
                setMaybe(builder, LootContextParameters.LOOTING_MOD, context.getLootingModifier());
            }
        }

        // SPIGOT-5603 - Avoid IllegalArgumentException in LootTableInfo#build()
        LootContextParameterSet.Builder nmsBuilder = new LootContextParameterSet.Builder();
        for (LootContextParameter<?> param : getHandle().getParamSet().getRequired()) {
            nmsBuilder.required(param);
        }
        for (LootContextParameter<?> param : getHandle().getParamSet().getAllowed()) {
            if (!getHandle().getParamSet().getRequired().contains(param)) {
                nmsBuilder.optional(param);
            }
        }
        nmsBuilder.optional(LootContextParameters.LOOTING_MOD);

        return builder.create(nmsBuilder.build());
    }

    private <T> void setMaybe(LootTableInfo.Builder builder, LootContextParameter<T> param, T value) {
        if (getHandle().getParamSet().getRequired().contains(param) || getHandle().getParamSet().getAllowed().contains(param)) {
            builder.withParameter(param, value);
        }
    }

    public static LootContext convertContext(LootTableInfo info) {
        Vec3D position = info.getParamOrNull(LootContextParameters.ORIGIN);
        if (position == null) {
            position = info.getParamOrNull(LootContextParameters.THIS_ENTITY).position(); // Every vanilla context has origin or this_entity, see LootContextParameterSets
        }
        Location location = new Location(info.getLevel().getWorld(), position.x(), position.y(), position.z());
        LootContext.Builder contextBuilder = new LootContext.Builder(location);

        if (info.hasParam(LootContextParameters.KILLER_ENTITY)) {
            CraftEntity killer = info.getParamOrNull(LootContextParameters.KILLER_ENTITY).getBukkitEntity();
            if (killer instanceof CraftHumanEntity) {
                contextBuilder.killer((CraftHumanEntity) killer);
            }
        }

        if (info.hasParam(LootContextParameters.THIS_ENTITY)) {
            contextBuilder.lootedEntity(info.getParamOrNull(LootContextParameters.THIS_ENTITY).getBukkitEntity());
        }

        if (info.hasParam(LootContextParameters.LOOTING_MOD)) {
            contextBuilder.lootingModifier(info.getParamOrNull(LootContextParameters.LOOTING_MOD));
        }

        contextBuilder.luck(info.getLuck());
        return contextBuilder.build();
    }

    @Override
    public String toString() {
        return getKey().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof org.bukkit.loot.LootTable)) {
            return false;
        }

        org.bukkit.loot.LootTable table = (org.bukkit.loot.LootTable) obj;
        return table.getKey().equals(this.getKey());
    }
}
