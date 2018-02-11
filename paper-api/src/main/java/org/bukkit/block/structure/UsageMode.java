package org.bukkit.block.structure;

/**
 * Represents how a {@link org.bukkit.block.Structure} can be used.
 */
public enum UsageMode {

    /**
     * The mode used when saving a structure.
     */
    SAVE,
    /**
     * The mode used when loading a structure.
     */
    LOAD,
    /**
     * Used when saving a structure for easy size calculation. When using this
     * mode, the Structure name MUST match the name in the second Structure
     * block that is in {@link UsageMode#SAVE}.
     */
    CORNER,
    /**
     * Used to run specific custom functions, which can only be used for certain
     * Structures. The structure block is removed after this function completes.
     * The data tags (functions) can be found on the
     * <a href="http://minecraft.gamepedia.com/Structure_Block#Data">wiki</a>.
     */
    DATA;
}
