package io.papermc.paper.loot;

import com.google.common.base.Preconditions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Server-side implementation of LootContext.
 */
public class PaperLootContextImpl implements LootContext {
    
    private final Location location;
    private final float luck;
    private final Entity lootedEntity;
    private final HumanEntity killer;
    private final ItemStack tool;
    private final NamespacedKey damageSource;
    private final float explosionRadius;
    private final Map<String, Object> parameters;

    public PaperLootContextImpl(
            Location location,
            float luck,
            Entity lootedEntity,
            HumanEntity killer,
            ItemStack tool,
            NamespacedKey damageSource,
            float explosionRadius,
            Map<String, Object> parameters
    ) {
        this.location = location.clone();
        this.luck = luck;
        this.lootedEntity = lootedEntity;
        this.killer = killer;
        this.tool = tool != null ? tool.clone() : null;
        this.damageSource = damageSource;
        this.explosionRadius = explosionRadius;
        this.parameters = Collections.unmodifiableMap(new HashMap<>(parameters));
    }

    @Override
    public Location getLocation() {
        return location.clone();
    }

    @Override
    public float getLuck() {
        return luck;
    }

    @Override
    public Entity getLootedEntity() {
        return lootedEntity;
    }

    @Override
    public HumanEntity getKiller() {
        return killer;
    }

    @Override
    public ItemStack getTool() {
        return tool != null ? tool.clone() : null;
    }

    @Override
    public NamespacedKey getDamageSource() {
        return damageSource;
    }

    @Override
    public float getExplosionRadius() {
        return explosionRadius;
    }

    @Override
    public Object getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public LootContextBuilder toBuilder() {
        LootContextBuilder builder = LootContextBuilder.create(location)
            .luck(luck)
            .lootedEntity(lootedEntity)
            .killer(killer)
            .tool(tool)
            .damageSource(damageSource)
            .explosionRadius(explosionRadius);
        
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            builder.parameter(entry.getKey(), entry.getValue());
        }
        
        return builder;
    }

    /**
     * Converts this Paper loot context to a Minecraft LootParams for internal use.
     * 
     * @param parameterSet the required parameter set for the loot table
     * @return the converted LootParams
     */
    public LootParams toNmsLootParams(net.minecraft.util.context.ContextKeySet parameterSet) {
        Preconditions.checkNotNull(location.getWorld(), "LootContext world cannot be null");
        ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
        
        LootParams.Builder builder = new LootParams.Builder(serverLevel);
        
        // Set origin location
        builder.withParameter(LootContextParams.ORIGIN, CraftLocation.toVec3(location));
        
        // Set luck
        builder.withLuck(luck);
        
        // Set entities if present
        if (lootedEntity != null) {
            setParameterIfSupported(builder, parameterSet, LootContextParams.THIS_ENTITY, 
                ((CraftEntity) lootedEntity).getHandle());
        }
        
        if (killer != null) {
            setParameterIfSupported(builder, parameterSet, LootContextParams.ATTACKING_ENTITY, 
                ((CraftHumanEntity) killer).getHandle());
            setParameterIfSupported(builder, parameterSet, LootContextParams.LAST_DAMAGE_PLAYER, 
                ((CraftHumanEntity) killer).getHandle());
        }
        
        // Set tool if present
        if (tool != null) {
            setParameterIfSupported(builder, parameterSet, LootContextParams.TOOL, 
                CraftItemStack.asNMSCopy(tool));
        }
        
        // Set damage source if present
        if (damageSource != null && killer != null) {
            // Create damage source - this is simplified, real implementation would be more complex
            net.minecraft.world.entity.player.Player nmsPlayer = ((CraftHumanEntity) killer).getHandle();
            setParameterIfSupported(builder, parameterSet, LootContextParams.DAMAGE_SOURCE, 
                serverLevel.damageSources().playerAttack(nmsPlayer));
        }
        
        // Set explosion radius if present
        if (explosionRadius > 0) {
            setParameterIfSupported(builder, parameterSet, LootContextParams.EXPLOSION_RADIUS, 
                explosionRadius);
        }
        
        return builder.create(parameterSet);
    }
    
    private <T> void setParameterIfSupported(LootParams.Builder builder, 
                                           net.minecraft.util.context.ContextKeySet parameterSet,
                                           ContextKey<T> param, T value) {
        if (parameterSet.required().contains(param) || parameterSet.allowed().contains(param)) {
            builder.withParameter(param, value);
        }
    }
}
