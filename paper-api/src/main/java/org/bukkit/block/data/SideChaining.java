package org.bukkit.block.data;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface SideChaining extends BlockData {
    ChainPart getSideChain();

    void setSideChain(ChainPart part);

    enum ChainPart {
        UNCONNECTED,
        RIGHT,
        CENTER,
        LEFT
    }
}
