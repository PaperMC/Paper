package org.bukkit.entity;

/**
 * Represents a Zombie.
 */
public interface Zombie extends Monster {

    /**
     * Gets whether the zombie is a baby
     *
     * @return Whether the zombie is a baby
     */
    public boolean isBaby();

    /**
     * Sets whether the zombie is a baby
     *
     * @param flag Whether the zombie is a baby
     */
    public void setBaby(boolean flag);

    /**
     * Gets whether the zombie is a villager
     *
     * @return Whether the zombie is a villager
     * @deprecated Entity subtypes will be separate entities in a future Minecraft release
     */
    @Deprecated
    public boolean isVillager();

    /**
     * Sets whether the zombie is a villager
     *
     * @param flag Whether the zombie is a villager
     * @deprecated Entity subtypes will be separate entities in a future Minecraft release
     */
    @Deprecated
    public void setVillager(boolean flag);

    /**
     * Sets whether the zombie is a villager
     *
     * @param profession the profession of the villager or null to clear
     * @deprecated Entity subtypes will be separate entities in a future Minecraft release
     */
    @Deprecated
    public void setVillagerProfession(Villager.Profession profession);

    /**
     * Returns the villager profession of the zombie if the
     * zombie is a villager
     *
     * @return the profession or null
     * @deprecated Entity subtypes will be separate entities in a future Minecraft release
     */
    @Deprecated
    public Villager.Profession getVillagerProfession();
}
