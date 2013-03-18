package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityCommand;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftCommandBlock extends CraftBlockState implements CommandBlock {
    private final TileEntityCommand commandBlock;
    private String command;
    private String name;

    public CraftCommandBlock(Block block) {
        super(block);

        CraftWorld world = (CraftWorld) block.getWorld();
        commandBlock = (TileEntityCommand) world.getTileEntityAt(getX(), getY(), getZ());
        command = commandBlock.b;
        name = commandBlock.getName();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command != null ? command : "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "@";
    }

    public boolean update(boolean forced) {
        boolean result = super.update(forced);

        if (result) {
            commandBlock.b(command);
            commandBlock.c(name);
        }

        return result;
    }
}
