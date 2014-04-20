package net.minecraft.server;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import javax.annotation.Nullable;

// Spigot start
import com.google.common.base.Predicate;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.Futures;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.authlib.Agent;
import com.mojang.authlib.ProfileLookupCallback;
import java.util.concurrent.Callable;
// Spigot end

public class TileEntitySkull extends TileEntity implements ITickable {

    @Nullable
    private static UserCache userCache;
    @Nullable
    private static MinecraftSessionService sessionService;
    @Nullable
    public GameProfile gameProfile;
    private int g;
    private boolean h;
    // Spigot start
    public static final ExecutorService executor = Executors.newFixedThreadPool(3,
            new ThreadFactoryBuilder()
                    .setNameFormat("Head Conversion Thread - %1$d")
                    .build()
    );
    public static final LoadingCache<String, GameProfile> skinCache = CacheBuilder.newBuilder()
            .maximumSize( 5000 )
            .expireAfterAccess( 60, TimeUnit.MINUTES )
            .build( new CacheLoader<String, GameProfile>()
            {
                @Override
                public GameProfile load(String key) throws Exception
                {
                    final GameProfile[] profiles = new GameProfile[1];
                    ProfileLookupCallback gameProfileLookup = new ProfileLookupCallback() {

                        @Override
                        public void onProfileLookupSucceeded(GameProfile gp) {
                            profiles[0] = gp;
                        }

                        @Override
                        public void onProfileLookupFailed(GameProfile gp, Exception excptn) {
                            profiles[0] = gp;
                        }
                    };

                    MinecraftServer.getServer().getGameProfileRepository().findProfilesByNames(new String[] { key }, Agent.MINECRAFT, gameProfileLookup);

                    GameProfile profile = profiles[ 0 ];
                    if (profile == null) {
                        UUID uuid = EntityHuman.a(new GameProfile(null, key));
                        profile = new GameProfile(uuid, key);

                        gameProfileLookup.onProfileLookupSucceeded(profile);
                    } else
                    {

                        Property property = Iterables.getFirst( profile.getProperties().get( "textures" ), null );

                        if ( property == null )
                        {
                            profile = TileEntitySkull.sessionService.fillProfileProperties( profile, true );
                        }
                    }


                    return profile;
                }
            } );
    // Spigot end

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
        // Spigot start
        GameProfile profile = this.gameProfile;
        b(profile, new Predicate<GameProfile>() {

            @Override
            public boolean apply(GameProfile input) {
                gameProfile = input;
                update();
                return false;
            }
        }, false);
        // Spigot end
    }

    // Spigot start - Support async lookups
    public static Future<GameProfile> b(@Nullable final GameProfile gameprofile, final Predicate<GameProfile> callback, boolean sync) {
        if (gameprofile != null && !UtilColor.b(gameprofile.getName())) {
            if (gameprofile.isComplete() && gameprofile.getProperties().containsKey("textures")) {
                callback.apply(gameprofile);
            } else if (MinecraftServer.getServer() == null) {
                callback.apply(gameprofile);
            } else {
                GameProfile profile = skinCache.getIfPresent(gameprofile.getName().toLowerCase(java.util.Locale.ROOT));
                if (profile != null && Iterables.getFirst(profile.getProperties().get("textures"), (Object) null) != null) {
                    callback.apply(profile);

                    return Futures.immediateFuture(profile);
                } else {
                    Callable<GameProfile> callable = new Callable<GameProfile>() {
                        @Override
                        public GameProfile call() {
                            final GameProfile profile = skinCache.getUnchecked(gameprofile.getName().toLowerCase(java.util.Locale.ROOT));
                            MinecraftServer.getServer().processQueue.add(new Runnable() {
                                @Override
                                public void run() {
                                    if (profile == null) {
                                        callback.apply(gameprofile);
                                    } else {
                                        callback.apply(profile);
                                    }
                                }
                            });
                            return profile;
                        }
                    };
                    if (sync) {
                        try {
                            return Futures.immediateFuture(callable.call());
                        } catch (Exception ex) {
                            com.google.common.base.Throwables.throwIfUnchecked(ex);
                            throw new RuntimeException(ex); // Not possible
                        }
                    } else {
                        return executor.submit(callable);
                    }
                }
            }
        } else {
            callback.apply(gameprofile);
        }

        return Futures.immediateFuture(gameprofile);
    }
    // Spigot end
}
