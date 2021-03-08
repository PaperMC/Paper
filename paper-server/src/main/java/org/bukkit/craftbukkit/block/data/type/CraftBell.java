package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Bell;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftBell extends CraftBlockData implements Bell {

    private static final net.minecraft.server.BlockStateEnum<?> ATTACHMENT = getEnum("attachment");

    @Override
    public org.bukkit.block.data.type.Bell.Attachment getAttachment() {
        return get(ATTACHMENT, org.bukkit.block.data.type.Bell.Attachment.class);
    }

    @Override
    public void setAttachment(org.bukkit.block.data.type.Bell.Attachment leaves) {
        set(ATTACHMENT, leaves);
    }
}
