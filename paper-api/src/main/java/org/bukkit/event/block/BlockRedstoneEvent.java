/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.BlockFace;

/**
 *
 * @author Nathan
 */
public class BlockRedstoneEvent extends BlockFromToEvent {
    private int oldCurrent;
    private int newCurrent;
    
    public BlockRedstoneEvent(Block block, BlockFace face, int oldCurrent, int newCurrent) {
        super(Type.REDSTONE_CHANGE, block, face);
        this.oldCurrent = oldCurrent;
        this.newCurrent = newCurrent;
    }

    /**
     * @return the oldCurrent
     */
    public int getOldCurrent() {
        return oldCurrent;
    }

    /**
     * @return the newCurrent
     */
    public int getNewCurrent() {
        return newCurrent;
    }

    /**
     * @param newCurrent the newCurrent to set
     */
    public void setNewCurrent(int newCurrent) {
        this.newCurrent = newCurrent;
    }

}
