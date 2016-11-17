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
     * @deprecated check if instanceof {@link ZombieVillager}.
     */
    @Deprecated
    public boolean isVillager();

    /**
     * @param flag
     * @deprecated must spawn {@link ZombieVillager}.
     */
    @Deprecated
    public void setVillager(boolean flag);

    /**
     * @param profession
     * @see ZombieVillager#getVillagerProfession()
     */
    @Deprecated
    public void setVillagerProfession(Villager.Profession profession);

    /**
     * @return profession
     * @see ZombieVillager#getVillagerProfession()
     */
    @Deprecated
    public Villager.Profession getVillagerProfession();
}
