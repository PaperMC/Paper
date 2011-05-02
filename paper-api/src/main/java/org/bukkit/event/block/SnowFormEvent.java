package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;

/**
 * Called on snow formed by weather
 */
public class SnowFormEvent extends BlockEvent implements Cancellable {
    private Material material;
    private byte data;
    private boolean cancel;

    public SnowFormEvent(Block block) {
        super(Type.SNOW_FORM, block);
        this.material = Material.SNOW;
        this.cancel = false;
    }

    /**
     * Gets the material being placed on a block during snowfall
     *
     * @return the material being placed by a snowfall
     */
    public Material getMaterial(){
        return material;
    }

    /**
     * Sets the material to be placed on a block during a snowfall
     *
     * @param material the material to be placed during a snowfall
     */
    public void setMaterial(Material material){
        this.material = material;
    }

    /**
     * Gets the block data of a block involved in a snowfall
     *
     * @return the data of the block being placed by a snowfall
     */
    public byte getData(){
        return data;
    }

    /**
     * Sets the block data of a block involved in a snowfall
     *
     * @param data
     */
    public void setData(byte data){
        this.data = data;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel snow from forming during a snowfall
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}