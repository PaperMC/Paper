package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityCommand;
import org.bukkit.Material;
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
        command = commandBlock.getCommandBlock().getCommand();
        name = commandBlock.getCommandBlock().getName();
    }

    public CraftCommandBlock(final Material material, final TileEntityCommand te) {
        super(material);
        commandBlock = te;
        command = commandBlock.getCommandBlock().getCommand();
        name = commandBlock.getCommandBlock().getName();
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

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            commandBlock.getCommandBlock().setCommand(command);
            commandBlock.getCommandBlock().setName(name);
        }

        return result;
    }

    @Override
    public TileEntityCommand getTileEntity() {
        return commandBlock;
    }
}
