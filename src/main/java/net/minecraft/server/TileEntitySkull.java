package net.minecraft.server;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import javax.annotation.Nullable;

public class TileEntitySkull extends TileEntity implements ITickable {

    @Nullable
    private static UserCache userCache;
    @Nullable
    private static MinecraftSessionService sessionService;
    @Nullable
    public GameProfile gameProfile;
    private int g;
    private boolean h;

    public TileEntitySkull() {
        super(TileEntityTypes.SKULL);
    }

    public static void a(UserCache usercache) {
        TileEntitySkull.userCache = usercache;
    }

    public static void a(MinecraftSessionService minecraftsessionservice) {
        TileEntitySkull.sessionService = minecraftsessionservice;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        if (this.gameProfile != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            GameProfileSerializer.serialize(nbttagcompound1, this.gameProfile);
            nbttagcompound.set("SkullOwner", nbttagcompound1);
        }

        return nbttagcompound;
    }

    @Override
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        super.load(iblockdata, nbttagcompound);
        if (nbttagcompound.hasKeyOfType("SkullOwner", 10)) {
            this.setGameProfile(GameProfileSerializer.deserialize(nbttagcompound.getCompound("SkullOwner")));
        } else if (nbttagcompound.hasKeyOfType("ExtraType", 8)) {
            String s = nbttagcompound.getString("ExtraType");

            if (!UtilColor.b(s)) {
                this.setGameProfile(new GameProfile((UUID) null, s));
            }
        }

    }

    @Override
    public void tick() {
        IBlockData iblockdata = this.getBlock();

        if (iblockdata.a(Blocks.DRAGON_HEAD) || iblockdata.a(Blocks.DRAGON_WALL_HEAD)) {
            if (this.world.isBlockIndirectlyPowered(this.position)) {
                this.h = true;
                ++this.g;
            } else {
                this.h = false;
            }
        }

    }

    @Nullable
    @Override
    public PacketPlayOutTileEntityData getUpdatePacket() {
        return new PacketPlayOutTileEntityData(this.position, 4, this.b());
    }

    @Override
    public NBTTagCompound b() {
        return this.save(new NBTTagCompound());
    }

    public void setGameProfile(@Nullable GameProfile gameprofile) {
        this.gameProfile = gameprofile;
        this.f();
    }

    private void f() {
        this.gameProfile = b(this.gameProfile);
        this.update();
    }

    @Nullable
    public static GameProfile b(@Nullable GameProfile gameprofile) {
        if (gameprofile != null && !UtilColor.b(gameprofile.getName())) {
            if (gameprofile.isComplete() && gameprofile.getProperties().containsKey("textures")) {
                return gameprofile;
            } else if (TileEntitySkull.userCache != null && TileEntitySkull.sessionService != null) {
                GameProfile gameprofile1 = TileEntitySkull.userCache.getProfile(gameprofile.getName());

                if (gameprofile1 == null) {
                    return gameprofile;
                } else {
                    Property property = (Property) Iterables.getFirst(gameprofile1.getProperties().get("textures"), (Object) null);

                    if (property == null) {
                        gameprofile1 = TileEntitySkull.sessionService.fillProfileProperties(gameprofile1, true);
                    }

                    return gameprofile1;
                }
            } else {
                return gameprofile;
            }
        } else {
            return gameprofile;
        }
    }
}
