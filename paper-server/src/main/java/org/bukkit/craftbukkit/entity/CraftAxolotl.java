package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Axolotl;

public class CraftAxolotl extends CraftAnimals implements Axolotl {

    public CraftAxolotl(CraftServer server, net.minecraft.world.entity.animal.axolotl.Axolotl entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.axolotl.Axolotl getHandle() {
        return (net.minecraft.world.entity.animal.axolotl.Axolotl) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftAxolotl";
    }

    @Override
    public boolean isPlayingDead() {
        return this.getHandle().isPlayingDead();
    }

    @Override
    public void setPlayingDead(boolean playingDead) {
        this.getHandle().setPlayingDead(playingDead);
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[this.getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        this.getHandle().setVariant(net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.byId(variant.ordinal()));
    }
}
