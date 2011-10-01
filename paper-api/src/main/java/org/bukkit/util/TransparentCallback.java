package org.bukkit.util;

import java.util.Set;

import org.bukkit.block.Block;

/**
 * Default transparent call back. This class as callback acts like the normal
 * line of sight methods.
 *
 * To implement own handler override {@link TransparentCallback#call(Block)} and
 * call it via super.
 */
public class TransparentCallback implements Callback<Boolean, Block> {

    private final Set<Byte> transparent;

    /**
     * Creates a new callback class which returns by default for every block in
     * the transparent list true. Otherwise false. Could be expanded by override
     * the {@link Callback#call(Block)} method.
     *
     * @param transparent
     *            The list of transparent blocks.
     */
    public TransparentCallback(Set<Byte> transparent) {
        this.transparent = transparent;
    }

    public Boolean call(Block parameter) {
        return this.transparent.contains((byte) parameter.getTypeId());
    }

}
