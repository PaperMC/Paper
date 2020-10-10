package org.bukkit.craftbukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.LootContextParameter;
import net.minecraft.server.LootContextParameterSet;
import net.minecraft.server.LootContextParameters;
import net.minecraft.server.LootTable;
import net.minecraft.server.LootTableInfo;
import net.minecraft.server.Vec3D;
import net.minecraft.server.WorldServer;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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
        LootTableInfo nmsContext = convertContext(context);
        List<net.minecraft.server.ItemStack> nmsItems = handle.populateLoot(nmsContext);
        Collection<ItemStack> bukkit = new ArrayList<>(nmsItems.size());

        for (net.minecraft.server.ItemStack item : nmsItems) {
            if (item.isEmpty()) {
                continue;
            }
            bukkit.add(CraftItemStack.asBukkitCopy(item));
        }

        return bukkit;
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext context) {
        LootTableInfo nmsContext = convertContext(context);
        CraftInventory craftInventory = (CraftInventory) inventory;
        IInventory handle = craftInventory.getInventory();

        // TODO: When events are added, call event here w/ custom reason?
        getHandle().fillInventory(handle, nmsContext);
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    private LootTableInfo convertContext(LootContext context) {
        Location loc = context.getLocation();
        WorldServer handle = ((CraftWorld) loc.getWorld()).getHandle();

        LootTableInfo.Builder builder = new LootTableInfo.Builder(handle);
        setMaybe(builder, LootContextParameters.ORIGIN, new Vec3D(loc.getX(), loc.getY(), loc.getZ()));
        if (getHandle() != LootTable.EMPTY) {
            // builder.luck(context.getLuck());

            if (context.getLootedEntity() != null) {
                Entity nmsLootedEntity = ((CraftEntity) context.getLootedEntity()).getHandle();
                setMaybe(builder, LootContextParameters.THIS_ENTITY, nmsLootedEntity);
                setMaybe(builder, LootContextParameters.DAMAGE_SOURCE, DamageSource.GENERIC);
                setMaybe(builder, LootContextParameters.ORIGIN, nmsLootedEntity.getPositionVector());
            }

            if (context.getKiller() != null) {
                EntityHuman nmsKiller = ((CraftHumanEntity) context.getKiller()).getHandle();
                setMaybe(builder, LootContextParameters.KILLER_ENTITY, nmsKiller);
                // If there is a player killer, damage source should reflect that in case loot tables use that information
                setMaybe(builder, LootContextParameters.DAMAGE_SOURCE, DamageSource.playerAttack(nmsKiller));
                setMaybe(builder, LootContextParameters.LAST_DAMAGE_PLAYER, nmsKiller); // SPIGOT-5603 - Set minecraft:killed_by_player
            }

            // SPIGOT-5603 - Use LootContext#lootingModifier
            if (context.getLootingModifier() != LootContext.DEFAULT_LOOT_MODIFIER) {
                setMaybe(builder, LootContextParameters.LOOTING_MOD, context.getLootingModifier());
            }
        }

        // SPIGOT-5603 - Avoid IllegalArgumentException in LootTableInfo#build()
        LootContextParameterSet.Builder nmsBuilder = new LootContextParameterSet.Builder();
        for (LootContextParameter<?> param : getHandle().getLootContextParameterSet().getRequired()) {
            nmsBuilder.addRequired(param);
        }
        for (LootContextParameter<?> param : getHandle().getLootContextParameterSet().getOptional()) {
            if (!getHandle().getLootContextParameterSet().getRequired().contains(param)) {
                nmsBuilder.addOptional(param);
            }
        }
        nmsBuilder.addOptional(LootContextParameters.LOOTING_MOD);

        return builder.build(nmsBuilder.build());
    }

    private <T> void setMaybe(LootTableInfo.Builder builder, LootContextParameter<T> param, T value) {
        if (getHandle().getLootContextParameterSet().getRequired().contains(param) || getHandle().getLootContextParameterSet().getOptional().contains(param)) {
            builder.set(param, value);
        }
    }

    public static LootContext convertContext(LootTableInfo info) {
        Vec3D position = info.getContextParameter(LootContextParameters.ORIGIN);
        if (position == null) {
            position = info.getContextParameter(LootContextParameters.THIS_ENTITY).getPositionVector(); // Every vanilla context has origin or this_entity, see LootContextParameterSets
        }
        Location location = new Location(info.getWorld().getWorld(), position.getX(), position.getY(), position.getZ());
        LootContext.Builder contextBuilder = new LootContext.Builder(location);

        if (info.hasContextParameter(LootContextParameters.KILLER_ENTITY)) {
            CraftEntity killer = info.getContextParameter(LootContextParameters.KILLER_ENTITY).getBukkitEntity();
            if (killer instanceof CraftHumanEntity) {
                contextBuilder.killer((CraftHumanEntity) killer);
            }
        }

        if (info.hasContextParameter(LootContextParameters.THIS_ENTITY)) {
            contextBuilder.lootedEntity(info.getContextParameter(LootContextParameters.THIS_ENTITY).getBukkitEntity());
        }

        if (info.hasContextParameter(LootContextParameters.LOOTING_MOD)) {
            contextBuilder.lootingModifier(info.getContextParameter(LootContextParameters.LOOTING_MOD));
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
