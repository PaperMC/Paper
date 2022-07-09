package org.bukkit.block;

/**
 * Represents a level of support a block can give on one of its faces.
 * <p>
 * Any given face on a block may support anywhere between none and all three of the
 * values in this enum. The top face of a grass block for instance can support blocks
 * that require a full, center, or rigid face. On the contrary, all sides except the
 * bottom of a camp fire cannot support any blocks, while the bottom face can support
 * blocks that require a full or center face (such as a ceiling button).
 */
public enum BlockSupport {

    /**
     * The face is treated as a full block. For example, the side of a stair is
     * <strong>not</strong> a full face and cannot support a wall torch, whereas the
     * back and bottom of a stair are considered full.
     */
    FULL,
    /**
     * The face is capable of supporting blocks towards the center. For example, a
     * wall or a fence post can support a standing torch as there is a solid component
     * in the middle of the block.
     */
    CENTER,
    /**
     * The face is capable of supporting fragile blocks such as rails. Most
     * full-supportable top faces are rigid, unlike walls and posts, or the side of a
     * stone block, none of which are rigid.
     */
    RIGID;

}
