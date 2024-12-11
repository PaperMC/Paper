package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.CommandBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftCommandBlock extends CraftBlockEntityState<CommandBlockEntity> implements CommandBlock {

    public CraftCommandBlock(World world, CommandBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftCommandBlock(CraftCommandBlock state, Location location) {
        super(state, location);
    }

    @Override
    public String getCommand() {
        return this.getSnapshot().getCommandBlock().getCommand();
    }

    @Override
    public void setCommand(String command) {
        this.getSnapshot().getCommandBlock().setCommand(command != null ? command : "");
    }

    @Override
    public String getName() {
        return CraftChatMessage.fromComponent(this.getSnapshot().getCommandBlock().getName());
    }

    @Override
    public void setName(String name) {
        this.getSnapshot().getCommandBlock().setCustomName(CraftChatMessage.fromStringOrNull(name != null ? name : "@"));
    }

    @Override
    public CraftCommandBlock copy() {
        return new CraftCommandBlock(this, null);
    }

    @Override
    public CraftCommandBlock copy(Location location) {
        return new CraftCommandBlock(this, location);
    }
}
