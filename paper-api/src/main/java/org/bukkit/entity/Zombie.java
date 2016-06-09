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
     */
    public boolean isVillager();

    /**
     * Sets whether the zombie is a villager
     *
     * @param flag Whether the zombie is a villager
     * @deprecated Defaults to a {@link Villager.Profession#NORMAL}
     */
    @Deprecated
    public void setVillager(boolean flag);

    /**
     * Sets whether the zombie is a villager
     *
     * @param profession the profession of the villager or null to clear
     */
    public void setVillagerProfession(Villager.Profession profession);

    /**
     * Returns the villager profession of the zombie if the
     * zombie is a villager
     *
     * @return the profession or null
     */
    public Villager.Profession getVillagerProfession();
}
