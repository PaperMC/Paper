package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown whenever a {@link Player} dies
 */
public class PlayerDeathEvent extends EntityDeathEvent {
    private int newExp = 0;
    private String deathMessage = "";
    private int newLevel = 0;
    private int newTotalExp = 0;
    private boolean keepLevel = false;
    private boolean keepInventory = false;

    public PlayerDeathEvent(@NotNull final Player player, @NotNull final List<ItemStack> drops, final int droppedExp, @Nullable final String deathMessage) {
        this(player, drops, droppedExp, 0, deathMessage);
    }

    public PlayerDeathEvent(@NotNull final Player player, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, @Nullable final String deathMessage) {
        this(player, drops, droppedExp, newExp, 0, 0, deathMessage);
    }

    public PlayerDeathEvent(@NotNull final Player player, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, @Nullable final String deathMessage) {
        super(player, drops, droppedExp);
        this.newExp = newExp;
        this.newTotalExp = newTotalExp;
        this.newLevel = newLevel;
        this.deathMessage = deathMessage;
    }

    @NotNull
    @Override
    public Player getEntity() {
        return (Player) entity;
    }

    /**
     * Set the death message that will appear to everyone on the server.
     *
     * @param deathMessage Message to appear to other players on the server.
     */
    public void setDeathMessage(@Nullable String deathMessage) {
        this.deathMessage = deathMessage;
    }

    /**
     * Get the death message that will appear to everyone on the server.
     *
     * @return Message to appear to other players on the server.
     */
    @Nullable
    public String getDeathMessage() {
        return deathMessage;
    }

    /**
     * Gets how much EXP the Player should have at respawn.
     * <p>
     * This does not indicate how much EXP should be dropped, please see
     * {@link #getDroppedExp()} for that.
     *
     * @return New EXP of the respawned player
     */
    public int getNewExp() {
        return newExp;
    }

    /**
     * Sets how much EXP the Player should have at respawn.
     * <p>
     * This does not indicate how much EXP should be dropped, please see
     * {@link #setDroppedExp(int)} for that.
     *
     * @param exp New EXP of the respawned player
     */
    public void setNewExp(int exp) {
        newExp = exp;
    }

    /**
     * Gets the Level the Player should have at respawn.
     *
     * @return New Level of the respawned player
     */
    public int getNewLevel() {
        return newLevel;
    }

    /**
     * Sets the Level the Player should have at respawn.
     *
     * @param level New Level of the respawned player
     */
    public void setNewLevel(int level) {
        newLevel = level;
    }

    /**
     * Gets the Total EXP the Player should have at respawn.
     *
     * @return New Total EXP of the respawned player
     */
    public int getNewTotalExp() {
        return newTotalExp;
    }

    /**
     * Sets the Total EXP the Player should have at respawn.
     *
     * @param totalExp New Total EXP of the respawned player
     */
    public void setNewTotalExp(int totalExp) {
        newTotalExp = totalExp;
    }

    /**
     * Gets if the Player should keep all EXP at respawn.
     * <p>
     * This flag overrides other EXP settings
     *
     * @return True if Player should keep all pre-death exp
     */
    public boolean getKeepLevel() {
        return keepLevel;
    }

    /**
     * Sets if the Player should keep all EXP at respawn.
     * <p>
     * This overrides all other EXP settings
     * <p>
     * <b>This doesn't prevent the EXP from dropping.
     * {@link #setDroppedExp(int)} should be used stop the
     * EXP from dropping.</b>
     *
     * @param keepLevel True to keep all current value levels
     */
    public void setKeepLevel(boolean keepLevel) {
        this.keepLevel = keepLevel;
    }

    /**
     * Sets if the Player keeps inventory on death.
     * <p>
     * <b>This doesn't prevent the items from dropping.
     * {@code getDrops().clear()} should be used stop the
     * items from dropping.</b>
     *
     * @param keepInventory True to keep the inventory
     */
    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    /**
     * Gets if the Player keeps inventory on death.
     *
     * @return True if the player keeps inventory on death
     */
    public boolean getKeepInventory() {
        return keepInventory;
    }
}
