package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityCommand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;

public class CraftCommandBlock extends CraftBlockEntityState<TileEntityCommand> implements CommandBlock {

    private String command;
    private String name;

    public CraftCommandBlock(Block block) {
        super(block, TileEntityCommand.class);
    }

    public CraftCommandBlock(final Material material, final TileEntityCommand te) {
        super(material, te);
    }

    @Override
    public void load(TileEntityCommand commandBlock) {
        super.load(commandBlock);

        command = commandBlock.getCommandBlock().getCommand();
        name = commandBlock.getCommandBlock().getName();
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void setCommand(String command) {
        this.command = command != null ? command : "";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name != null ? name : "@";
    }

    @Override
    public void applyTo(TileEntityCommand commandBlock) {
        super.applyTo(commandBlock);

        commandBlock.getCommandBlock().setCommand(command);
        commandBlock.getCommandBlock().setName(name);
    }
}
