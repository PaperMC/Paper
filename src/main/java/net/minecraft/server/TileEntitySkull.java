package net.minecraft.server;

import java.util.UUID;

import net.minecraft.util.com.google.common.collect.Iterables;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;

public class TileEntitySkull extends TileEntity {

    private int a;
    private int i;
    private GameProfile j = null;

    public TileEntitySkull() {}

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setByte("SkullType", (byte) (this.a & 255));
        nbttagcompound.setByte("Rot", (byte) (this.i & 255));
        if (this.j != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            GameProfileSerializer.a(nbttagcompound1, this.j);
            nbttagcompound.set("Owner", nbttagcompound1);
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.a = nbttagcompound.getByte("SkullType");
        this.i = nbttagcompound.getByte("Rot");
        if (this.a == 3) {
            if (nbttagcompound.hasKeyOfType("Owner", 10)) {
                this.j = GameProfileSerializer.a(nbttagcompound.getCompound("Owner"));
            } else if (nbttagcompound.hasKeyOfType("ExtraType", 8) && !UtilColor.b(nbttagcompound.getString("ExtraType"))) {
                this.j = new GameProfile((UUID) null, nbttagcompound.getString("ExtraType"));
                this.d();
            }
        }
    }

    public GameProfile getGameProfile() {
        return this.j;
    }

    public Packet getUpdatePacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.b(nbttagcompound);
        return new PacketPlayOutTileEntityData(this.x, this.y, this.z, 4, nbttagcompound);
    }

    public void setSkullType(int i) {
        this.a = i;
        this.j = null;
    }

    public void setGameProfile(GameProfile gameprofile) {
        this.a = 3;
        this.j = gameprofile;
        this.d();
    }

    private void d() {
        if (this.j != null && !UtilColor.b(this.j.getName())) {
            if (!this.j.isComplete() || !this.j.getProperties().containsKey("textures")) {
                GameProfile gameprofile = MinecraftServer.getServer().getUserCache().a(this.j.getName());

                if (gameprofile != null) {
                    Property property = (Property) Iterables.getFirst(gameprofile.getProperties().get("textures"), null);

                    if (property == null) {
                        gameprofile = MinecraftServer.getServer().av().fillProfileProperties(gameprofile, true);
                    }

                    this.j = gameprofile;
                    this.update();
                }
            }
        }
    }

    public int getSkullType() {
        return this.a;
    }

    public void setRotation(int i) {
        this.i = i;
    }

    // CraftBukkit start - add method
    public int getRotation() {
        return this.i;
    }
    // CraftBukkit end
}
