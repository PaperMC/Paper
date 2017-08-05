package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.SpawnEggMeta;

/**
 * Represents a spawn egg that can be used to spawn mobs
 * @deprecated use {@link SpawnEggMeta}
 */
@Deprecated
public class SpawnEgg extends MaterialData {

    public SpawnEgg() {
        super(Material.MONSTER_EGG);
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public SpawnEgg(int type, byte data) {
        super(type, data);
    }

    /**
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public SpawnEgg(byte data) {
        super(Material.MONSTER_EGG, data);
    }

    public SpawnEgg(EntityType type) {
        this();
        setSpawnedType(type);
    }

    /**
     * Get the type of entity this egg will spawn.
     *
     * @return The entity type.
     * @deprecated This is now stored in {@link SpawnEggMeta}.
     */
    @Deprecated
    public EntityType getSpawnedType() {
        return EntityType.fromId(getData());
    }

    /**
     * Set the type of entity this egg will spawn.
     *
     * @param type The entity type.
     * @deprecated This is now stored in {@link SpawnEggMeta}.
     */
    @Deprecated
    public void setSpawnedType(EntityType type) {
        setData((byte) type.getTypeId());
    }

    @Override
    public String toString() {
        return "SPAWN EGG{" + getSpawnedType() + "}";
    }

    @Override
    public SpawnEgg clone() {
        return (SpawnEgg) super.clone();
    }
}
