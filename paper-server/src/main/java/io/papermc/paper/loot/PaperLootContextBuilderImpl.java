package io.papermc.paper.loot;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Server-side implementation of LootContextBuilder.
 */
public class PaperLootContextBuilderImpl implements LootContextBuilder {
    
    private Location location;
    private float luck = 0.0f;
    private Entity lootedEntity;
    private HumanEntity killer;
    private ItemStack tool;
    private NamespacedKey damageSource;
    private float explosionRadius = 0.0f;
    private final Map<String, Object> parameters = new HashMap<>();

    public PaperLootContextBuilderImpl(Location location) {
        this.location = Preconditions.checkNotNull(location, "Location cannot be null").clone();
        Preconditions.checkNotNull(location.getWorld(), "Location world cannot be null");
    }

    @Override
    public LootContextBuilder location(Location location) {
        Preconditions.checkNotNull(location, "Location cannot be null");
        Preconditions.checkNotNull(location.getWorld(), "Location world cannot be null");
        this.location = location.clone();
        return this;
    }

    @Override
    public LootContextBuilder luck(float luck) {
        this.luck = luck;
        return this;
    }

    @Override
    public LootContextBuilder lootedEntity(Entity entity) {
        this.lootedEntity = entity;
        return this;
    }

    @Override
    public LootContextBuilder killer(HumanEntity killer) {
        this.killer = killer;
        return this;
    }

    @Override
    public LootContextBuilder tool(ItemStack tool) {
        this.tool = tool != null ? tool.clone() : null;
        return this;
    }

    @Override
    public LootContextBuilder damageSource(NamespacedKey damageSource) {
        this.damageSource = damageSource;
        return this;
    }

    @Override
    public LootContextBuilder explosionRadius(float radius) {
        this.explosionRadius = radius;
        return this;
    }

    @Override
    public LootContextBuilder parameter(String key, Object value) {
        Preconditions.checkNotNull(key, "Parameter key cannot be null");
        if (value == null) {
            this.parameters.remove(key);
        } else {
            this.parameters.put(key, value);
        }
        return this;
    }

    @Override
    public LootContext build() {
        Preconditions.checkNotNull(location, "Location must be set");
        return new PaperLootContextImpl(
            location,
            luck,
            lootedEntity,
            killer,
            tool,
            damageSource,
            explosionRadius,
            new HashMap<>(parameters)
        );
    }

    @Override
    public LootContextBuilder validateFor(LootTable lootTable) {
        Preconditions.checkNotNull(lootTable, "LootTable cannot be null");
        Preconditions.checkNotNull(location, "Location must be set for loot table validation");
        
        // Create a generator to check requirements
        LootGenerator generator = new PaperLootGeneratorImpl(lootTable);
        LootContext testContext = build();
        
        if (!generator.canGenerateWith(testContext)) {
            throw new IllegalStateException("LootContext is missing required parameters for the specified loot table. " +
                "Required: " + generator.getRequiredContextTypes() + 
                ", Optional: " + generator.getOptionalContextTypes());
        }
        
        return this;
    }

    @Override
    public Location getLocation() {
        return location != null ? location.clone() : null;
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
}
