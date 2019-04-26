package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Bell;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftBell extends CraftBlockData implements Bell {

    private static final net.minecraft.server.BlockStateEnum<?> ATTACHMENT = getEnum("attachment");

    @Override
    public Attachment getAttachment() {
        return get(ATTACHMENT, Attachment.class);
    }

    @Override
    public void setAttachment(Attachment leaves) {
        set(ATTACHMENT, leaves);
    }
}
