package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.data.type.Jukebox;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftJukebox extends CraftBlockData implements Jukebox {
    private static final BooleanProperty HAS_RECORD = JukeboxBlock.HAS_RECORD;

    public CraftJukebox(BlockState state) {
        super(state);
    }

    @Override
    public boolean hasRecord() {
        return this.get(HAS_RECORD);
    }

    @Override
    public void setHasRecord(final boolean hasRecord) {
        this.set(HAS_RECORD, hasRecord);
    }
}
