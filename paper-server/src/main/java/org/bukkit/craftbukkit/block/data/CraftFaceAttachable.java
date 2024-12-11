package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.FaceAttachable;

public abstract class CraftFaceAttachable extends CraftBlockData implements FaceAttachable {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> ATTACH_FACE = getEnum("face");

    @Override
    public org.bukkit.block.data.FaceAttachable.AttachedFace getAttachedFace() {
        return this.get(CraftFaceAttachable.ATTACH_FACE, org.bukkit.block.data.FaceAttachable.AttachedFace.class);
    }

    @Override
    public void setAttachedFace(org.bukkit.block.data.FaceAttachable.AttachedFace face) {
        this.set(CraftFaceAttachable.ATTACH_FACE, face);
    }
}
