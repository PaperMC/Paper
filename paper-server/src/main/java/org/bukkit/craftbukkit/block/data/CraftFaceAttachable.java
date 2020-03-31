package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.FaceAttachable;

public abstract class CraftFaceAttachable extends CraftBlockData implements FaceAttachable {

    private static final net.minecraft.server.BlockStateEnum<?> ATTACH_FACE = getEnum("face");

    @Override
    public AttachedFace getAttachedFace() {
        return get(ATTACH_FACE, AttachedFace.class);
    }

    @Override
    public void setAttachedFace(AttachedFace face) {
        set(ATTACH_FACE, face);
    }
}
