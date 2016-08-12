package org.bukkit.entity;

public interface Guardian extends Monster {

    /**
     * Check if the Guardian is an elder Guardian
     * 
     * @return true if the Guardian is an Elder Guardian, false if not
     * @deprecated Entity subtypes will be separate entities in a future Minecraft release
     */
    @Deprecated
    public boolean isElder();

    /**
     * Set the Guardian to an elder Guardian or not
     *
     * @param shouldBeElder True if this Guardian should be a elder Guardian, false if not
     * @deprecated Entity subtypes will be separate entities in a future Minecraft release
     */
    @Deprecated
    public void setElder(boolean shouldBeElder);
}
