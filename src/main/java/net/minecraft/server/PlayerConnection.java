package net.minecraft.server;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import it.unimi.dsi.fastutil.ints.Int2ShortMap;
import it.unimi.dsi.fastutil.ints.Int2ShortOpenHashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerConnection implements PacketListenerPlayIn {

    private static final Logger LOGGER = LogManager.getLogger();
    public final NetworkManager networkManager;
    private final MinecraftServer minecraftServer;
    public EntityPlayer player;
    private int e;
    private long lastKeepAlive;
    private boolean awaitingKeepAlive;
    private long h;
    private int chatThrottle;
    private int j;
    private final Int2ShortMap k = new Int2ShortOpenHashMap();
    private double l;
    private double m;
    private double n;
    private double o;
    private double p;
    private double q;
    private Entity r;
    private double s;
    private double t;
    private double u;
    private double v;
    private double w;
    private double x;
    private Vec3D teleportPos;
    private int teleportAwait;
    private int A;
    private boolean B;
    private int C;
    private boolean D;
    private int E;
    private int receivedMovePackets;
    private int processedMovePackets;

    public PlayerConnection(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        this.minecraftServer = minecraftserver;
        this.networkManager = networkmanager;
        networkmanager.setPacketListener(this);
        this.player = entityplayer;
        entityplayer.playerConnection = this;
    }

    public void tick() {
        this.syncPosition();
        this.player.lastX = this.player.locX();
        this.player.lastY = this.player.locY();
        this.player.lastZ = this.player.locZ();
        this.player.playerTick();
        this.player.setLocation(this.l, this.m, this.n, this.player.yaw, this.player.pitch);
        ++this.e;
        this.processedMovePackets = this.receivedMovePackets;
        if (this.B && !this.player.isSleeping()) {
            if (++this.C > 80) {
                PlayerConnection.LOGGER.warn("{} was kicked for floating too long!", this.player.getDisplayName().getString());
                this.disconnect(new ChatMessage("multiplayer.disconnect.flying"));
                return;
            }
        } else {
            this.B = false;
            this.C = 0;
        }

        this.r = this.player.getRootVehicle();
        if (this.r != this.player && this.r.getRidingPassenger() == this.player) {
            this.s = this.r.locX();
            this.t = this.r.locY();
            this.u = this.r.locZ();
            this.v = this.r.locX();
            this.w = this.r.locY();
            this.x = this.r.locZ();
            if (this.D && this.player.getRootVehicle().getRidingPassenger() == this.player) {
                if (++this.E > 80) {
                    PlayerConnection.LOGGER.warn("{} was kicked for floating a vehicle too long!", this.player.getDisplayName().getString());
                    this.disconnect(new ChatMessage("multiplayer.disconnect.flying"));
                    return;
                }
            } else {
                this.D = false;
                this.E = 0;
            }
        } else {
            this.r = null;
            this.D = false;
            this.E = 0;
        }

        this.minecraftServer.getMethodProfiler().enter("keepAlive");
        long i = SystemUtils.getMonotonicMillis();

        if (i - this.lastKeepAlive >= 15000L) {
            if (this.awaitingKeepAlive) {
                this.disconnect(new ChatMessage("disconnect.timeout"));
            } else {
                this.awaitingKeepAlive = true;
                this.lastKeepAlive = i;
                this.h = i;
                this.sendPacket(new PacketPlayOutKeepAlive(this.h));
            }
        }

        this.minecraftServer.getMethodProfiler().exit();
        if (this.chatThrottle > 0) {
            --this.chatThrottle;
        }

        if (this.j > 0) {
            --this.j;
        }

        if (this.player.F() > 0L && this.minecraftServer.getIdleTimeout() > 0 && SystemUtils.getMonotonicMillis() - this.player.F() > (long) (this.minecraftServer.getIdleTimeout() * 1000 * 60)) {
            this.disconnect(new ChatMessage("multiplayer.disconnect.idling"));
        }

    }

    public void syncPosition() {
        this.l = this.player.locX();
        this.m = this.player.locY();
        this.n = this.player.locZ();
        this.o = this.player.locX();
        this.p = this.player.locY();
        this.q = this.player.locZ();
    }

    @Override
    public NetworkManager a() {
        return this.networkManager;
    }

    private boolean isExemptPlayer() {
        return this.minecraftServer.a(this.player.getProfile());
    }

    public void disconnect(IChatBaseComponent ichatbasecomponent) {
        this.networkManager.sendPacket(new PacketPlayOutKickDisconnect(ichatbasecomponent), (future) -> {
            this.networkManager.close(ichatbasecomponent);
        });
        this.networkManager.stopReading();
        MinecraftServer minecraftserver = this.minecraftServer;
        NetworkManager networkmanager = this.networkManager;

        this.networkManager.getClass();
        minecraftserver.executeSync(networkmanager::handleDisconnection);
    }

    @Override
    public void a(PacketPlayInSteerVehicle packetplayinsteervehicle) {
        PlayerConnectionUtils.ensureMainThread(packetplayinsteervehicle, this, this.player.getWorldServer());
        this.player.a(packetplayinsteervehicle.b(), packetplayinsteervehicle.c(), packetplayinsteervehicle.d(), packetplayinsteervehicle.e());
    }

    private static boolean b(PacketPlayInFlying packetplayinflying) {
        return Doubles.isFinite(packetplayinflying.a(0.0D)) && Doubles.isFinite(packetplayinflying.b(0.0D)) && Doubles.isFinite(packetplayinflying.c(0.0D)) && Floats.isFinite(packetplayinflying.b(0.0F)) && Floats.isFinite(packetplayinflying.a(0.0F)) ? Math.abs(packetplayinflying.a(0.0D)) > 3.0E7D || Math.abs(packetplayinflying.b(0.0D)) > 3.0E7D || Math.abs(packetplayinflying.c(0.0D)) > 3.0E7D : true;
    }

    private static boolean b(PacketPlayInVehicleMove packetplayinvehiclemove) {
        return !Doubles.isFinite(packetplayinvehiclemove.getX()) || !Doubles.isFinite(packetplayinvehiclemove.getY()) || !Doubles.isFinite(packetplayinvehiclemove.getZ()) || !Floats.isFinite(packetplayinvehiclemove.getPitch()) || !Floats.isFinite(packetplayinvehiclemove.getYaw());
    }

    @Override
    public void a(PacketPlayInVehicleMove packetplayinvehiclemove) {
        PlayerConnectionUtils.ensureMainThread(packetplayinvehiclemove, this, this.player.getWorldServer());
        if (b(packetplayinvehiclemove)) {
            this.disconnect(new ChatMessage("multiplayer.disconnect.invalid_vehicle_movement"));
        } else {
            Entity entity = this.player.getRootVehicle();

            if (entity != this.player && entity.getRidingPassenger() == this.player && entity == this.r) {
                WorldServer worldserver = this.player.getWorldServer();
                double d0 = entity.locX();
                double d1 = entity.locY();
                double d2 = entity.locZ();
                double d3 = packetplayinvehiclemove.getX();
                double d4 = packetplayinvehiclemove.getY();
                double d5 = packetplayinvehiclemove.getZ();
                float f = packetplayinvehiclemove.getYaw();
                float f1 = packetplayinvehiclemove.getPitch();
                double d6 = d3 - this.s;
                double d7 = d4 - this.t;
                double d8 = d5 - this.u;
                double d9 = entity.getMot().g();
                double d10 = d6 * d6 + d7 * d7 + d8 * d8;

                if (d10 - d9 > 100.0D && !this.isExemptPlayer()) {
                    PlayerConnection.LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", entity.getDisplayName().getString(), this.player.getDisplayName().getString(), d6, d7, d8);
                    this.networkManager.sendPacket(new PacketPlayOutVehicleMove(entity));
                    return;
                }

                boolean flag = worldserver.getCubes(entity, entity.getBoundingBox().shrink(0.0625D));

                d6 = d3 - this.v;
                d7 = d4 - this.w - 1.0E-6D;
                d8 = d5 - this.x;
                entity.move(EnumMoveType.PLAYER, new Vec3D(d6, d7, d8));
                double d11 = d7;

                d6 = d3 - entity.locX();
                d7 = d4 - entity.locY();
                if (d7 > -0.5D || d7 < 0.5D) {
                    d7 = 0.0D;
                }

                d8 = d5 - entity.locZ();
                d10 = d6 * d6 + d7 * d7 + d8 * d8;
                boolean flag1 = false;

                if (d10 > 0.0625D) {
                    flag1 = true;
                    PlayerConnection.LOGGER.warn("{} (vehicle of {}) moved wrongly! {}", entity.getDisplayName().getString(), this.player.getDisplayName().getString(), Math.sqrt(d10));
                }

                entity.setLocation(d3, d4, d5, f, f1);
                boolean flag2 = worldserver.getCubes(entity, entity.getBoundingBox().shrink(0.0625D));

                if (flag && (flag1 || !flag2)) {
                    entity.setLocation(d0, d1, d2, f, f1);
                    this.networkManager.sendPacket(new PacketPlayOutVehicleMove(entity));
                    return;
                }

                this.player.getWorldServer().getChunkProvider().movePlayer(this.player);
                this.player.checkMovement(this.player.locX() - d0, this.player.locY() - d1, this.player.locZ() - d2);
                this.D = d11 >= -0.03125D && !this.minecraftServer.getAllowFlight() && this.a(entity);
                this.v = entity.locX();
                this.w = entity.locY();
                this.x = entity.locZ();
            }

        }
    }

    private boolean a(Entity entity) {
        return entity.world.a(entity.getBoundingBox().g(0.0625D).b(0.0D, -0.55D, 0.0D)).allMatch(BlockBase.BlockData::isAir);
    }

    @Override
    public void a(PacketPlayInTeleportAccept packetplayinteleportaccept) {
        PlayerConnectionUtils.ensureMainThread(packetplayinteleportaccept, this, this.player.getWorldServer());
        if (packetplayinteleportaccept.b() == this.teleportAwait) {
            this.player.setLocation(this.teleportPos.x, this.teleportPos.y, this.teleportPos.z, this.player.yaw, this.player.pitch);
            this.o = this.teleportPos.x;
            this.p = this.teleportPos.y;
            this.q = this.teleportPos.z;
            if (this.player.H()) {
                this.player.I();
            }

            this.teleportPos = null;
        }

    }

    @Override
    public void a(PacketPlayInRecipeDisplayed packetplayinrecipedisplayed) {
        PlayerConnectionUtils.ensureMainThread(packetplayinrecipedisplayed, this, this.player.getWorldServer());
        Optional optional = this.minecraftServer.getCraftingManager().getRecipe(packetplayinrecipedisplayed.b());
        RecipeBookServer recipebookserver = this.player.getRecipeBook();

        optional.ifPresent(recipebookserver::e);
    }

    @Override
    public void a(PacketPlayInRecipeSettings packetplayinrecipesettings) {
        PlayerConnectionUtils.ensureMainThread(packetplayinrecipesettings, this, this.player.getWorldServer());
        this.player.getRecipeBook().a(packetplayinrecipesettings.b(), packetplayinrecipesettings.c(), packetplayinrecipesettings.d());
    }

    @Override
    public void a(PacketPlayInAdvancements packetplayinadvancements) {
        PlayerConnectionUtils.ensureMainThread(packetplayinadvancements, this, this.player.getWorldServer());
        if (packetplayinadvancements.c() == PacketPlayInAdvancements.Status.OPENED_TAB) {
            MinecraftKey minecraftkey = packetplayinadvancements.d();
            Advancement advancement = this.minecraftServer.getAdvancementData().a(minecraftkey);

            if (advancement != null) {
                this.player.getAdvancementData().a(advancement);
            }
        }

    }

    @Override
    public void a(PacketPlayInTabComplete packetplayintabcomplete) {
        PlayerConnectionUtils.ensureMainThread(packetplayintabcomplete, this, this.player.getWorldServer());
        StringReader stringreader = new StringReader(packetplayintabcomplete.c());

        if (stringreader.canRead() && stringreader.peek() == '/') {
            stringreader.skip();
        }

        ParseResults<CommandListenerWrapper> parseresults = this.minecraftServer.getCommandDispatcher().a().parse(stringreader, this.player.getCommandListener());

        this.minecraftServer.getCommandDispatcher().a().getCompletionSuggestions(parseresults).thenAccept((suggestions) -> {
            this.networkManager.sendPacket(new PacketPlayOutTabComplete(packetplayintabcomplete.b(), suggestions));
        });
    }

    @Override
    public void a(PacketPlayInSetCommandBlock packetplayinsetcommandblock) {
        PlayerConnectionUtils.ensureMainThread(packetplayinsetcommandblock, this, this.player.getWorldServer());
        if (!this.minecraftServer.getEnableCommandBlock()) {
            this.player.sendMessage(new ChatMessage("advMode.notEnabled"), SystemUtils.b);
        } else if (!this.player.isCreativeAndOp()) {
            this.player.sendMessage(new ChatMessage("advMode.notAllowed"), SystemUtils.b);
        } else {
            CommandBlockListenerAbstract commandblocklistenerabstract = null;
            TileEntityCommand tileentitycommand = null;
            BlockPosition blockposition = packetplayinsetcommandblock.b();
            TileEntity tileentity = this.player.world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityCommand) {
                tileentitycommand = (TileEntityCommand) tileentity;
                commandblocklistenerabstract = tileentitycommand.getCommandBlock();
            }

            String s = packetplayinsetcommandblock.c();
            boolean flag = packetplayinsetcommandblock.d();

            if (commandblocklistenerabstract != null) {
                TileEntityCommand.Type tileentitycommand_type = tileentitycommand.m();
                EnumDirection enumdirection = (EnumDirection) this.player.world.getType(blockposition).get(BlockCommand.a);
                IBlockData iblockdata;

                switch (packetplayinsetcommandblock.g()) {
                    case SEQUENCE:
                        iblockdata = Blocks.CHAIN_COMMAND_BLOCK.getBlockData();
                        this.player.world.setTypeAndData(blockposition, (IBlockData) ((IBlockData) iblockdata.set(BlockCommand.a, enumdirection)).set(BlockCommand.b, packetplayinsetcommandblock.e()), 2);
                        break;
                    case AUTO:
                        iblockdata = Blocks.REPEATING_COMMAND_BLOCK.getBlockData();
                        this.player.world.setTypeAndData(blockposition, (IBlockData) ((IBlockData) iblockdata.set(BlockCommand.a, enumdirection)).set(BlockCommand.b, packetplayinsetcommandblock.e()), 2);
                        break;
                    case REDSTONE:
                    default:
                        iblockdata = Blocks.COMMAND_BLOCK.getBlockData();
                        this.player.world.setTypeAndData(blockposition, (IBlockData) ((IBlockData) iblockdata.set(BlockCommand.a, enumdirection)).set(BlockCommand.b, packetplayinsetcommandblock.e()), 2);
                }

                tileentity.r();
                this.player.world.setTileEntity(blockposition, tileentity);
                commandblocklistenerabstract.setCommand(s);
                commandblocklistenerabstract.a(flag);
                if (!flag) {
                    commandblocklistenerabstract.b((IChatBaseComponent) null);
                }

                tileentitycommand.b(packetplayinsetcommandblock.f());
                if (tileentitycommand_type != packetplayinsetcommandblock.g()) {
                    tileentitycommand.h();
                }

                commandblocklistenerabstract.e();
                if (!UtilColor.b(s)) {
                    this.player.sendMessage(new ChatMessage("advMode.setCommand.success", new Object[]{s}), SystemUtils.b);
                }
            }

        }
    }

    @Override
    public void a(PacketPlayInSetCommandMinecart packetplayinsetcommandminecart) {
        PlayerConnectionUtils.ensureMainThread(packetplayinsetcommandminecart, this, this.player.getWorldServer());
        if (!this.minecraftServer.getEnableCommandBlock()) {
            this.player.sendMessage(new ChatMessage("advMode.notEnabled"), SystemUtils.b);
        } else if (!this.player.isCreativeAndOp()) {
            this.player.sendMessage(new ChatMessage("advMode.notAllowed"), SystemUtils.b);
        } else {
            CommandBlockListenerAbstract commandblocklistenerabstract = packetplayinsetcommandminecart.a(this.player.world);

            if (commandblocklistenerabstract != null) {
                commandblocklistenerabstract.setCommand(packetplayinsetcommandminecart.b());
                commandblocklistenerabstract.a(packetplayinsetcommandminecart.c());
                if (!packetplayinsetcommandminecart.c()) {
                    commandblocklistenerabstract.b((IChatBaseComponent) null);
                }

                commandblocklistenerabstract.e();
                this.player.sendMessage(new ChatMessage("advMode.setCommand.success", new Object[]{packetplayinsetcommandminecart.b()}), SystemUtils.b);
            }

        }
    }

    @Override
    public void a(PacketPlayInPickItem packetplayinpickitem) {
        PlayerConnectionUtils.ensureMainThread(packetplayinpickitem, this, this.player.getWorldServer());
        this.player.inventory.c(packetplayinpickitem.b());
        this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-2, this.player.inventory.itemInHandIndex, this.player.inventory.getItem(this.player.inventory.itemInHandIndex)));
        this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-2, packetplayinpickitem.b(), this.player.inventory.getItem(packetplayinpickitem.b())));
        this.player.playerConnection.sendPacket(new PacketPlayOutHeldItemSlot(this.player.inventory.itemInHandIndex));
    }

    @Override
    public void a(PacketPlayInItemName packetplayinitemname) {
        PlayerConnectionUtils.ensureMainThread(packetplayinitemname, this, this.player.getWorldServer());
        if (this.player.activeContainer instanceof ContainerAnvil) {
            ContainerAnvil containeranvil = (ContainerAnvil) this.player.activeContainer;
            String s = SharedConstants.a(packetplayinitemname.b());

            if (s.length() <= 35) {
                containeranvil.a(s);
            }
        }

    }

    @Override
    public void a(PacketPlayInBeacon packetplayinbeacon) {
        PlayerConnectionUtils.ensureMainThread(packetplayinbeacon, this, this.player.getWorldServer());
        if (this.player.activeContainer instanceof ContainerBeacon) {
            ((ContainerBeacon) this.player.activeContainer).c(packetplayinbeacon.b(), packetplayinbeacon.c());
        }

    }

    @Override
    public void a(PacketPlayInStruct packetplayinstruct) {
        PlayerConnectionUtils.ensureMainThread(packetplayinstruct, this, this.player.getWorldServer());
        if (this.player.isCreativeAndOp()) {
            BlockPosition blockposition = packetplayinstruct.b();
            IBlockData iblockdata = this.player.world.getType(blockposition);
            TileEntity tileentity = this.player.world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityStructure) {
                TileEntityStructure tileentitystructure = (TileEntityStructure) tileentity;

                tileentitystructure.setUsageMode(packetplayinstruct.d());
                tileentitystructure.setStructureName(packetplayinstruct.e());
                tileentitystructure.b(packetplayinstruct.f());
                tileentitystructure.c(packetplayinstruct.g());
                tileentitystructure.b(packetplayinstruct.h());
                tileentitystructure.b(packetplayinstruct.i());
                tileentitystructure.b(packetplayinstruct.j());
                tileentitystructure.a(packetplayinstruct.k());
                tileentitystructure.d(packetplayinstruct.l());
                tileentitystructure.e(packetplayinstruct.m());
                tileentitystructure.a(packetplayinstruct.n());
                tileentitystructure.a(packetplayinstruct.o());
                if (tileentitystructure.g()) {
                    String s = tileentitystructure.getStructureName();

                    if (packetplayinstruct.c() == TileEntityStructure.UpdateType.SAVE_AREA) {
                        if (tileentitystructure.D()) {
                            this.player.a((IChatBaseComponent) (new ChatMessage("structure_block.save_success", new Object[]{s})), false);
                        } else {
                            this.player.a((IChatBaseComponent) (new ChatMessage("structure_block.save_failure", new Object[]{s})), false);
                        }
                    } else if (packetplayinstruct.c() == TileEntityStructure.UpdateType.LOAD_AREA) {
                        if (!tileentitystructure.F()) {
                            this.player.a((IChatBaseComponent) (new ChatMessage("structure_block.load_not_found", new Object[]{s})), false);
                        } else if (tileentitystructure.a(this.player.getWorldServer())) {
                            this.player.a((IChatBaseComponent) (new ChatMessage("structure_block.load_success", new Object[]{s})), false);
                        } else {
                            this.player.a((IChatBaseComponent) (new ChatMessage("structure_block.load_prepare", new Object[]{s})), false);
                        }
                    } else if (packetplayinstruct.c() == TileEntityStructure.UpdateType.SCAN_AREA) {
                        if (tileentitystructure.C()) {
                            this.player.a((IChatBaseComponent) (new ChatMessage("structure_block.size_success", new Object[]{s})), false);
                        } else {
                            this.player.a((IChatBaseComponent) (new ChatMessage("structure_block.size_failure")), false);
                        }
                    }
                } else {
                    this.player.a((IChatBaseComponent) (new ChatMessage("structure_block.invalid_structure_name", new Object[]{packetplayinstruct.e()})), false);
                }

                tileentitystructure.update();
                this.player.world.notify(blockposition, iblockdata, iblockdata, 3);
            }

        }
    }

    @Override
    public void a(PacketPlayInSetJigsaw packetplayinsetjigsaw) {
        PlayerConnectionUtils.ensureMainThread(packetplayinsetjigsaw, this, this.player.getWorldServer());
        if (this.player.isCreativeAndOp()) {
            BlockPosition blockposition = packetplayinsetjigsaw.b();
            IBlockData iblockdata = this.player.world.getType(blockposition);
            TileEntity tileentity = this.player.world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityJigsaw) {
                TileEntityJigsaw tileentityjigsaw = (TileEntityJigsaw) tileentity;

                tileentityjigsaw.a(packetplayinsetjigsaw.c());
                tileentityjigsaw.b(packetplayinsetjigsaw.d());
                tileentityjigsaw.c(packetplayinsetjigsaw.e());
                tileentityjigsaw.a(packetplayinsetjigsaw.f());
                tileentityjigsaw.a(packetplayinsetjigsaw.g());
                tileentityjigsaw.update();
                this.player.world.notify(blockposition, iblockdata, iblockdata, 3);
            }

        }
    }

    @Override
    public void a(PacketPlayInJigsawGenerate packetplayinjigsawgenerate) {
        PlayerConnectionUtils.ensureMainThread(packetplayinjigsawgenerate, this, this.player.getWorldServer());
        if (this.player.isCreativeAndOp()) {
            BlockPosition blockposition = packetplayinjigsawgenerate.b();
            TileEntity tileentity = this.player.world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityJigsaw) {
                TileEntityJigsaw tileentityjigsaw = (TileEntityJigsaw) tileentity;

                tileentityjigsaw.a(this.player.getWorldServer(), packetplayinjigsawgenerate.c(), packetplayinjigsawgenerate.d());
            }

        }
    }

    @Override
    public void a(PacketPlayInTrSel packetplayintrsel) {
        PlayerConnectionUtils.ensureMainThread(packetplayintrsel, this, this.player.getWorldServer());
        int i = packetplayintrsel.b();
        Container container = this.player.activeContainer;

        if (container instanceof ContainerMerchant) {
            ContainerMerchant containermerchant = (ContainerMerchant) container;

            containermerchant.d(i);
            containermerchant.g(i);
        }

    }

    @Override
    public void a(PacketPlayInBEdit packetplayinbedit) {
        PlayerConnectionUtils.ensureMainThread(packetplayinbedit, this, this.player.getWorldServer());
        ItemStack itemstack = packetplayinbedit.b();

        if (!itemstack.isEmpty()) {
            if (ItemBookAndQuill.a(itemstack.getTag())) {
                ItemStack itemstack1 = this.player.b(packetplayinbedit.d());

                if (itemstack.getItem() == Items.WRITABLE_BOOK && itemstack1.getItem() == Items.WRITABLE_BOOK) {
                    if (packetplayinbedit.c()) {
                        ItemStack itemstack2 = new ItemStack(Items.WRITTEN_BOOK);
                        NBTTagCompound nbttagcompound = itemstack1.getTag();

                        if (nbttagcompound != null) {
                            itemstack2.setTag(nbttagcompound.clone());
                        }

                        itemstack2.a("author", (NBTBase) NBTTagString.a(this.player.getDisplayName().getString()));
                        itemstack2.a("title", (NBTBase) NBTTagString.a(itemstack.getTag().getString("title")));
                        NBTTagList nbttaglist = itemstack.getTag().getList("pages", 8);

                        for (int i = 0; i < nbttaglist.size(); ++i) {
                            String s = nbttaglist.getString(i);
                            ChatComponentText chatcomponenttext = new ChatComponentText(s);

                            s = IChatBaseComponent.ChatSerializer.a((IChatBaseComponent) chatcomponenttext);
                            nbttaglist.set(i, (NBTBase) NBTTagString.a(s));
                        }

                        itemstack2.a("pages", (NBTBase) nbttaglist);
                        this.player.a(packetplayinbedit.d(), itemstack2);
                    } else {
                        itemstack1.a("pages", (NBTBase) itemstack.getTag().getList("pages", 8));
                    }
                }

            }
        }
    }

    @Override
    public void a(PacketPlayInEntityNBTQuery packetplayinentitynbtquery) {
        PlayerConnectionUtils.ensureMainThread(packetplayinentitynbtquery, this, this.player.getWorldServer());
        if (this.player.k(2)) {
            Entity entity = this.player.getWorldServer().getEntity(packetplayinentitynbtquery.c());

            if (entity != null) {
                NBTTagCompound nbttagcompound = entity.save(new NBTTagCompound());

                this.player.playerConnection.sendPacket(new PacketPlayOutNBTQuery(packetplayinentitynbtquery.b(), nbttagcompound));
            }

        }
    }

    @Override
    public void a(PacketPlayInTileNBTQuery packetplayintilenbtquery) {
        PlayerConnectionUtils.ensureMainThread(packetplayintilenbtquery, this, this.player.getWorldServer());
        if (this.player.k(2)) {
            TileEntity tileentity = this.player.getWorldServer().getTileEntity(packetplayintilenbtquery.c());
            NBTTagCompound nbttagcompound = tileentity != null ? tileentity.save(new NBTTagCompound()) : null;

            this.player.playerConnection.sendPacket(new PacketPlayOutNBTQuery(packetplayintilenbtquery.b(), nbttagcompound));
        }
    }

    @Override
    public void a(PacketPlayInFlying packetplayinflying) {
        PlayerConnectionUtils.ensureMainThread(packetplayinflying, this, this.player.getWorldServer());
        if (b(packetplayinflying)) {
            this.disconnect(new ChatMessage("multiplayer.disconnect.invalid_player_movement"));
        } else {
            WorldServer worldserver = this.player.getWorldServer();

            if (!this.player.viewingCredits) {
                if (this.e == 0) {
                    this.syncPosition();
                }

                if (this.teleportPos != null) {
                    if (this.e - this.A > 20) {
                        this.A = this.e;
                        this.a(this.teleportPos.x, this.teleportPos.y, this.teleportPos.z, this.player.yaw, this.player.pitch);
                    }

                } else {
                    this.A = this.e;
                    if (this.player.isPassenger()) {
                        this.player.setLocation(this.player.locX(), this.player.locY(), this.player.locZ(), packetplayinflying.a(this.player.yaw), packetplayinflying.b(this.player.pitch));
                        this.player.getWorldServer().getChunkProvider().movePlayer(this.player);
                    } else {
                        double d0 = this.player.locX();
                        double d1 = this.player.locY();
                        double d2 = this.player.locZ();
                        double d3 = this.player.locY();
                        double d4 = packetplayinflying.a(this.player.locX());
                        double d5 = packetplayinflying.b(this.player.locY());
                        double d6 = packetplayinflying.c(this.player.locZ());
                        float f = packetplayinflying.a(this.player.yaw);
                        float f1 = packetplayinflying.b(this.player.pitch);
                        double d7 = d4 - this.l;
                        double d8 = d5 - this.m;
                        double d9 = d6 - this.n;
                        double d10 = this.player.getMot().g();
                        double d11 = d7 * d7 + d8 * d8 + d9 * d9;

                        if (this.player.isSleeping()) {
                            if (d11 > 1.0D) {
                                this.a(this.player.locX(), this.player.locY(), this.player.locZ(), packetplayinflying.a(this.player.yaw), packetplayinflying.b(this.player.pitch));
                            }

                        } else {
                            ++this.receivedMovePackets;
                            int i = this.receivedMovePackets - this.processedMovePackets;

                            if (i > 5) {
                                PlayerConnection.LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.getDisplayName().getString(), i);
                                i = 1;
                            }

                            if (!this.player.H() && (!this.player.getWorldServer().getGameRules().getBoolean(GameRules.DISABLE_ELYTRA_MOVEMENT_CHECK) || !this.player.isGliding())) {
                                float f2 = this.player.isGliding() ? 300.0F : 100.0F;

                                if (d11 - d10 > (double) (f2 * (float) i) && !this.isExemptPlayer()) {
                                    PlayerConnection.LOGGER.warn("{} moved too quickly! {},{},{}", this.player.getDisplayName().getString(), d7, d8, d9);
                                    this.a(this.player.locX(), this.player.locY(), this.player.locZ(), this.player.yaw, this.player.pitch);
                                    return;
                                }
                            }

                            AxisAlignedBB axisalignedbb = this.player.getBoundingBox();

                            d7 = d4 - this.o;
                            d8 = d5 - this.p;
                            d9 = d6 - this.q;
                            boolean flag = d8 > 0.0D;

                            if (this.player.isOnGround() && !packetplayinflying.b() && flag) {
                                this.player.jump();
                            }

                            this.player.move(EnumMoveType.PLAYER, new Vec3D(d7, d8, d9));
                            double d12 = d8;

                            d7 = d4 - this.player.locX();
                            d8 = d5 - this.player.locY();
                            if (d8 > -0.5D || d8 < 0.5D) {
                                d8 = 0.0D;
                            }

                            d9 = d6 - this.player.locZ();
                            d11 = d7 * d7 + d8 * d8 + d9 * d9;
                            boolean flag1 = false;

                            if (!this.player.H() && d11 > 0.0625D && !this.player.isSleeping() && !this.player.playerInteractManager.isCreative() && this.player.playerInteractManager.getGameMode() != EnumGamemode.SPECTATOR) {
                                flag1 = true;
                                PlayerConnection.LOGGER.warn("{} moved wrongly!", this.player.getDisplayName().getString());
                            }

                            this.player.setLocation(d4, d5, d6, f, f1);
                            if (!this.player.noclip && !this.player.isSleeping() && (flag1 && worldserver.getCubes(this.player, axisalignedbb) || this.a((IWorldReader) worldserver, axisalignedbb))) {
                                this.a(d0, d1, d2, f, f1);
                            } else {
                                this.B = d12 >= -0.03125D && this.player.playerInteractManager.getGameMode() != EnumGamemode.SPECTATOR && !this.minecraftServer.getAllowFlight() && !this.player.abilities.canFly && !this.player.hasEffect(MobEffects.LEVITATION) && !this.player.isGliding() && this.a((Entity) this.player);
                                this.player.getWorldServer().getChunkProvider().movePlayer(this.player);
                                this.player.a(this.player.locY() - d3, packetplayinflying.b());
                                this.player.setOnGround(packetplayinflying.b());
                                if (flag) {
                                    this.player.fallDistance = 0.0F;
                                }

                                this.player.checkMovement(this.player.locX() - d0, this.player.locY() - d1, this.player.locZ() - d2);
                                this.o = this.player.locX();
                                this.p = this.player.locY();
                                this.q = this.player.locZ();
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean a(IWorldReader iworldreader, AxisAlignedBB axisalignedbb) {
        Stream<VoxelShape> stream = iworldreader.d(this.player, this.player.getBoundingBox().shrink(9.999999747378752E-6D), (entity) -> {
            return true;
        });
        VoxelShape voxelshape = VoxelShapes.a(axisalignedbb.shrink(9.999999747378752E-6D));

        return stream.anyMatch((voxelshape1) -> {
            return !VoxelShapes.c(voxelshape1, voxelshape, OperatorBoolean.AND);
        });
    }

    public void a(double d0, double d1, double d2, float f, float f1) {
        this.a(d0, d1, d2, f, f1, Collections.emptySet());
    }

    public void a(double d0, double d1, double d2, float f, float f1, Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> set) {
        double d3 = set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X) ? this.player.locX() : 0.0D;
        double d4 = set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y) ? this.player.locY() : 0.0D;
        double d5 = set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z) ? this.player.locZ() : 0.0D;
        float f2 = set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT) ? this.player.yaw : 0.0F;
        float f3 = set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT) ? this.player.pitch : 0.0F;

        this.teleportPos = new Vec3D(d0, d1, d2);
        if (++this.teleportAwait == Integer.MAX_VALUE) {
            this.teleportAwait = 0;
        }

        this.A = this.e;
        this.player.setLocation(d0, d1, d2, f, f1);
        this.player.playerConnection.sendPacket(new PacketPlayOutPosition(d0 - d3, d1 - d4, d2 - d5, f - f2, f1 - f3, set, this.teleportAwait));
    }

    @Override
    public void a(PacketPlayInBlockDig packetplayinblockdig) {
        PlayerConnectionUtils.ensureMainThread(packetplayinblockdig, this, this.player.getWorldServer());
        BlockPosition blockposition = packetplayinblockdig.b();

        this.player.resetIdleTimer();
        PacketPlayInBlockDig.EnumPlayerDigType packetplayinblockdig_enumplayerdigtype = packetplayinblockdig.d();

        switch (packetplayinblockdig_enumplayerdigtype) {
            case SWAP_ITEM_WITH_OFFHAND:
                if (!this.player.isSpectator()) {
                    ItemStack itemstack = this.player.b(EnumHand.OFF_HAND);

                    this.player.a(EnumHand.OFF_HAND, this.player.b(EnumHand.MAIN_HAND));
                    this.player.a(EnumHand.MAIN_HAND, itemstack);
                    this.player.clearActiveItem();
                }

                return;
            case DROP_ITEM:
                if (!this.player.isSpectator()) {
                    this.player.dropItem(false);
                }

                return;
            case DROP_ALL_ITEMS:
                if (!this.player.isSpectator()) {
                    this.player.dropItem(true);
                }

                return;
            case RELEASE_USE_ITEM:
                this.player.releaseActiveItem();
                return;
            case START_DESTROY_BLOCK:
            case ABORT_DESTROY_BLOCK:
            case STOP_DESTROY_BLOCK:
                this.player.playerInteractManager.a(blockposition, packetplayinblockdig_enumplayerdigtype, packetplayinblockdig.c(), this.minecraftServer.getMaxBuildHeight());
                return;
            default:
                throw new IllegalArgumentException("Invalid player action");
        }
    }

    private static boolean a(EntityPlayer entityplayer, ItemStack itemstack) {
        if (itemstack.isEmpty()) {
            return false;
        } else {
            Item item = itemstack.getItem();

            return (item instanceof ItemBlock || item instanceof ItemBucket) && !entityplayer.getCooldownTracker().hasCooldown(item);
        }
    }

    @Override
    public void a(PacketPlayInUseItem packetplayinuseitem) {
        PlayerConnectionUtils.ensureMainThread(packetplayinuseitem, this, this.player.getWorldServer());
        WorldServer worldserver = this.player.getWorldServer();
        EnumHand enumhand = packetplayinuseitem.b();
        ItemStack itemstack = this.player.b(enumhand);
        MovingObjectPositionBlock movingobjectpositionblock = packetplayinuseitem.c();
        BlockPosition blockposition = movingobjectpositionblock.getBlockPosition();
        EnumDirection enumdirection = movingobjectpositionblock.getDirection();

        this.player.resetIdleTimer();
        if (blockposition.getY() < this.minecraftServer.getMaxBuildHeight()) {
            if (this.teleportPos == null && this.player.h((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D) < 64.0D && worldserver.a((EntityHuman) this.player, blockposition)) {
                EnumInteractionResult enuminteractionresult = this.player.playerInteractManager.a(this.player, worldserver, itemstack, enumhand, movingobjectpositionblock);

                if (enumdirection == EnumDirection.UP && !enuminteractionresult.a() && blockposition.getY() >= this.minecraftServer.getMaxBuildHeight() - 1 && a(this.player, itemstack)) {
                    IChatMutableComponent ichatmutablecomponent = (new ChatMessage("build.tooHigh", new Object[]{this.minecraftServer.getMaxBuildHeight()})).a(EnumChatFormat.RED);

                    this.player.playerConnection.sendPacket(new PacketPlayOutChat(ichatmutablecomponent, ChatMessageType.GAME_INFO, SystemUtils.b));
                } else if (enuminteractionresult.b()) {
                    this.player.swingHand(enumhand, true);
                }
            }
        } else {
            IChatMutableComponent ichatmutablecomponent1 = (new ChatMessage("build.tooHigh", new Object[]{this.minecraftServer.getMaxBuildHeight()})).a(EnumChatFormat.RED);

            this.player.playerConnection.sendPacket(new PacketPlayOutChat(ichatmutablecomponent1, ChatMessageType.GAME_INFO, SystemUtils.b));
        }

        this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition));
        this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition.shift(enumdirection)));
    }

    @Override
    public void a(PacketPlayInBlockPlace packetplayinblockplace) {
        PlayerConnectionUtils.ensureMainThread(packetplayinblockplace, this, this.player.getWorldServer());
        WorldServer worldserver = this.player.getWorldServer();
        EnumHand enumhand = packetplayinblockplace.b();
        ItemStack itemstack = this.player.b(enumhand);

        this.player.resetIdleTimer();
        if (!itemstack.isEmpty()) {
            EnumInteractionResult enuminteractionresult = this.player.playerInteractManager.a(this.player, worldserver, itemstack, enumhand);

            if (enuminteractionresult.b()) {
                this.player.swingHand(enumhand, true);
            }

        }
    }

    @Override
    public void a(PacketPlayInSpectate packetplayinspectate) {
        PlayerConnectionUtils.ensureMainThread(packetplayinspectate, this, this.player.getWorldServer());
        if (this.player.isSpectator()) {
            Iterator iterator = this.minecraftServer.getWorlds().iterator();

            while (iterator.hasNext()) {
                WorldServer worldserver = (WorldServer) iterator.next();
                Entity entity = packetplayinspectate.a(worldserver);

                if (entity != null) {
                    this.player.a(worldserver, entity.locX(), entity.locY(), entity.locZ(), entity.yaw, entity.pitch);
                    return;
                }
            }
        }

    }

    @Override
    public void a(PacketPlayInResourcePackStatus packetplayinresourcepackstatus) {}

    @Override
    public void a(PacketPlayInBoatMove packetplayinboatmove) {
        PlayerConnectionUtils.ensureMainThread(packetplayinboatmove, this, this.player.getWorldServer());
        Entity entity = this.player.getVehicle();

        if (entity instanceof EntityBoat) {
            ((EntityBoat) entity).a(packetplayinboatmove.b(), packetplayinboatmove.c());
        }

    }

    @Override
    public void a(IChatBaseComponent ichatbasecomponent) {
        PlayerConnection.LOGGER.info("{} lost connection: {}", this.player.getDisplayName().getString(), ichatbasecomponent.getString());
        this.minecraftServer.invalidatePingSample();
        this.minecraftServer.getPlayerList().sendMessage((new ChatMessage("multiplayer.player.left", new Object[]{this.player.getScoreboardDisplayName()})).a(EnumChatFormat.YELLOW), ChatMessageType.SYSTEM, SystemUtils.b);
        this.player.p();
        this.minecraftServer.getPlayerList().disconnect(this.player);
        if (this.isExemptPlayer()) {
            PlayerConnection.LOGGER.info("Stopping singleplayer server as player logged out");
            this.minecraftServer.safeShutdown(false);
        }

    }

    public void sendPacket(Packet<?> packet) {
        this.a(packet, (GenericFutureListener) null);
    }

    public void a(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {
        if (packet instanceof PacketPlayOutChat) {
            PacketPlayOutChat packetplayoutchat = (PacketPlayOutChat) packet;
            EnumChatVisibility enumchatvisibility = this.player.getChatFlags();

            if (enumchatvisibility == EnumChatVisibility.HIDDEN && packetplayoutchat.d() != ChatMessageType.GAME_INFO) {
                return;
            }

            if (enumchatvisibility == EnumChatVisibility.SYSTEM && !packetplayoutchat.c()) {
                return;
            }
        }

        try {
            this.networkManager.sendPacket(packet, genericfuturelistener);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Sending packet");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Packet being sent");

            crashreportsystemdetails.a("Packet class", () -> {
                return packet.getClass().getCanonicalName();
            });
            throw new ReportedException(crashreport);
        }
    }

    @Override
    public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot) {
        PlayerConnectionUtils.ensureMainThread(packetplayinhelditemslot, this, this.player.getWorldServer());
        if (packetplayinhelditemslot.b() >= 0 && packetplayinhelditemslot.b() < PlayerInventory.getHotbarSize()) {
            if (this.player.inventory.itemInHandIndex != packetplayinhelditemslot.b() && this.player.getRaisedHand() == EnumHand.MAIN_HAND) {
                this.player.clearActiveItem();
            }

            this.player.inventory.itemInHandIndex = packetplayinhelditemslot.b();
            this.player.resetIdleTimer();
        } else {
            PlayerConnection.LOGGER.warn("{} tried to set an invalid carried item", this.player.getDisplayName().getString());
        }
    }

    @Override
    public void a(PacketPlayInChat packetplayinchat) {
        PlayerConnectionUtils.ensureMainThread(packetplayinchat, this, this.player.getWorldServer());
        if (this.player.getChatFlags() == EnumChatVisibility.HIDDEN) {
            this.sendPacket(new PacketPlayOutChat((new ChatMessage("chat.cannotSend")).a(EnumChatFormat.RED), ChatMessageType.SYSTEM, SystemUtils.b));
        } else {
            this.player.resetIdleTimer();
            String s = StringUtils.normalizeSpace(packetplayinchat.b());

            for (int i = 0; i < s.length(); ++i) {
                if (!SharedConstants.isAllowedChatCharacter(s.charAt(i))) {
                    this.disconnect(new ChatMessage("multiplayer.disconnect.illegal_characters"));
                    return;
                }
            }

            if (s.startsWith("/")) {
                this.handleCommand(s);
            } else {
                ChatMessage chatmessage = new ChatMessage("chat.type.text", new Object[]{this.player.getScoreboardDisplayName(), s});

                this.minecraftServer.getPlayerList().sendMessage(chatmessage, ChatMessageType.CHAT, this.player.getUniqueID());
            }

            this.chatThrottle += 20;
            if (this.chatThrottle > 200 && !this.minecraftServer.getPlayerList().isOp(this.player.getProfile())) {
                this.disconnect(new ChatMessage("disconnect.spam"));
            }

        }
    }

    private void handleCommand(String s) {
        this.minecraftServer.getCommandDispatcher().a(this.player.getCommandListener(), s);
    }

    @Override
    public void a(PacketPlayInArmAnimation packetplayinarmanimation) {
        PlayerConnectionUtils.ensureMainThread(packetplayinarmanimation, this, this.player.getWorldServer());
        this.player.resetIdleTimer();
        this.player.swingHand(packetplayinarmanimation.b());
    }

    @Override
    public void a(PacketPlayInEntityAction packetplayinentityaction) {
        PlayerConnectionUtils.ensureMainThread(packetplayinentityaction, this, this.player.getWorldServer());
        this.player.resetIdleTimer();
        IJumpable ijumpable;

        switch (packetplayinentityaction.c()) {
            case PRESS_SHIFT_KEY:
                this.player.setSneaking(true);
                break;
            case RELEASE_SHIFT_KEY:
                this.player.setSneaking(false);
                break;
            case START_SPRINTING:
                this.player.setSprinting(true);
                break;
            case STOP_SPRINTING:
                this.player.setSprinting(false);
                break;
            case STOP_SLEEPING:
                if (this.player.isSleeping()) {
                    this.player.wakeup(false, true);
                    this.teleportPos = this.player.getPositionVector();
                }
                break;
            case START_RIDING_JUMP:
                if (this.player.getVehicle() instanceof IJumpable) {
                    ijumpable = (IJumpable) this.player.getVehicle();
                    int i = packetplayinentityaction.d();

                    if (ijumpable.P_() && i > 0) {
                        ijumpable.b(i);
                    }
                }
                break;
            case STOP_RIDING_JUMP:
                if (this.player.getVehicle() instanceof IJumpable) {
                    ijumpable = (IJumpable) this.player.getVehicle();
                    ijumpable.c();
                }
                break;
            case OPEN_INVENTORY:
                if (this.player.getVehicle() instanceof EntityHorseAbstract) {
                    ((EntityHorseAbstract) this.player.getVehicle()).f((EntityHuman) this.player);
                }
                break;
            case START_FALL_FLYING:
                if (!this.player.eC()) {
                    this.player.stopGliding();
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid client command!");
        }

    }

    @Override
    public void a(PacketPlayInUseEntity packetplayinuseentity) {
        PlayerConnectionUtils.ensureMainThread(packetplayinuseentity, this, this.player.getWorldServer());
        WorldServer worldserver = this.player.getWorldServer();
        Entity entity = packetplayinuseentity.a((World) worldserver);

        this.player.resetIdleTimer();
        this.player.setSneaking(packetplayinuseentity.e());
        if (entity != null) {
            double d0 = 36.0D;

            if (this.player.h(entity) < 36.0D) {
                EnumHand enumhand = packetplayinuseentity.c();
                ItemStack itemstack = enumhand != null ? this.player.b(enumhand).cloneItemStack() : ItemStack.b;
                Optional<EnumInteractionResult> optional = Optional.empty();

                if (packetplayinuseentity.b() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
                    optional = Optional.of(this.player.a(entity, enumhand));
                } else if (packetplayinuseentity.b() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
                    optional = Optional.of(entity.a((EntityHuman) this.player, packetplayinuseentity.d(), enumhand));
                } else if (packetplayinuseentity.b() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                    if (entity instanceof EntityItem || entity instanceof EntityExperienceOrb || entity instanceof EntityArrow || entity == this.player) {
                        this.disconnect(new ChatMessage("multiplayer.disconnect.invalid_entity_attacked"));
                        PlayerConnection.LOGGER.warn("Player {} tried to attack an invalid entity", this.player.getDisplayName().getString());
                        return;
                    }

                    this.player.attack(entity);
                }

                if (optional.isPresent() && ((EnumInteractionResult) optional.get()).a()) {
                    CriterionTriggers.P.a(this.player, itemstack, entity);
                    if (((EnumInteractionResult) optional.get()).b()) {
                        this.player.swingHand(enumhand, true);
                    }
                }
            }
        }

    }

    @Override
    public void a(PacketPlayInClientCommand packetplayinclientcommand) {
        PlayerConnectionUtils.ensureMainThread(packetplayinclientcommand, this, this.player.getWorldServer());
        this.player.resetIdleTimer();
        PacketPlayInClientCommand.EnumClientCommand packetplayinclientcommand_enumclientcommand = packetplayinclientcommand.b();

        switch (packetplayinclientcommand_enumclientcommand) {
            case PERFORM_RESPAWN:
                if (this.player.viewingCredits) {
                    this.player.viewingCredits = false;
                    this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, true);
                    CriterionTriggers.v.a(this.player, World.THE_END, World.OVERWORLD);
                } else {
                    if (this.player.getHealth() > 0.0F) {
                        return;
                    }

                    this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, false);
                    if (this.minecraftServer.isHardcore()) {
                        this.player.a(EnumGamemode.SPECTATOR);
                        ((GameRules.GameRuleBoolean) this.player.getWorldServer().getGameRules().get(GameRules.SPECTATORS_GENERATE_CHUNKS)).a(false, this.minecraftServer);
                    }
                }
                break;
            case REQUEST_STATS:
                this.player.getStatisticManager().a(this.player);
        }

    }

    @Override
    public void a(PacketPlayInCloseWindow packetplayinclosewindow) {
        PlayerConnectionUtils.ensureMainThread(packetplayinclosewindow, this, this.player.getWorldServer());
        this.player.o();
    }

    @Override
    public void a(PacketPlayInWindowClick packetplayinwindowclick) {
        PlayerConnectionUtils.ensureMainThread(packetplayinwindowclick, this, this.player.getWorldServer());
        this.player.resetIdleTimer();
        if (this.player.activeContainer.windowId == packetplayinwindowclick.b() && this.player.activeContainer.c(this.player)) {
            if (this.player.isSpectator()) {
                NonNullList<ItemStack> nonnulllist = NonNullList.a();

                for (int i = 0; i < this.player.activeContainer.slots.size(); ++i) {
                    nonnulllist.add(((Slot) this.player.activeContainer.slots.get(i)).getItem());
                }

                this.player.a(this.player.activeContainer, nonnulllist);
            } else {
                ItemStack itemstack = this.player.activeContainer.a(packetplayinwindowclick.c(), packetplayinwindowclick.d(), packetplayinwindowclick.g(), this.player);

                if (ItemStack.matches(packetplayinwindowclick.f(), itemstack)) {
                    this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.b(), packetplayinwindowclick.e(), true));
                    this.player.e = true;
                    this.player.activeContainer.c();
                    this.player.broadcastCarriedItem();
                    this.player.e = false;
                } else {
                    this.k.put(this.player.activeContainer.windowId, packetplayinwindowclick.e());
                    this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.b(), packetplayinwindowclick.e(), false));
                    this.player.activeContainer.a(this.player, false);
                    NonNullList<ItemStack> nonnulllist1 = NonNullList.a();

                    for (int j = 0; j < this.player.activeContainer.slots.size(); ++j) {
                        ItemStack itemstack1 = ((Slot) this.player.activeContainer.slots.get(j)).getItem();

                        nonnulllist1.add(itemstack1.isEmpty() ? ItemStack.b : itemstack1);
                    }

                    this.player.a(this.player.activeContainer, nonnulllist1);
                }
            }
        }

    }

    @Override
    public void a(PacketPlayInAutoRecipe packetplayinautorecipe) {
        PlayerConnectionUtils.ensureMainThread(packetplayinautorecipe, this, this.player.getWorldServer());
        this.player.resetIdleTimer();
        if (!this.player.isSpectator() && this.player.activeContainer.windowId == packetplayinautorecipe.b() && this.player.activeContainer.c(this.player) && this.player.activeContainer instanceof ContainerRecipeBook) {
            this.minecraftServer.getCraftingManager().getRecipe(packetplayinautorecipe.c()).ifPresent((irecipe) -> {
                ((ContainerRecipeBook) this.player.activeContainer).a(packetplayinautorecipe.d(), irecipe, this.player);
            });
        }
    }

    @Override
    public void a(PacketPlayInEnchantItem packetplayinenchantitem) {
        PlayerConnectionUtils.ensureMainThread(packetplayinenchantitem, this, this.player.getWorldServer());
        this.player.resetIdleTimer();
        if (this.player.activeContainer.windowId == packetplayinenchantitem.b() && this.player.activeContainer.c(this.player) && !this.player.isSpectator()) {
            this.player.activeContainer.a((EntityHuman) this.player, packetplayinenchantitem.c());
            this.player.activeContainer.c();
        }

    }

    @Override
    public void a(PacketPlayInSetCreativeSlot packetplayinsetcreativeslot) {
        PlayerConnectionUtils.ensureMainThread(packetplayinsetcreativeslot, this, this.player.getWorldServer());
        if (this.player.playerInteractManager.isCreative()) {
            boolean flag = packetplayinsetcreativeslot.b() < 0;
            ItemStack itemstack = packetplayinsetcreativeslot.getItemStack();
            NBTTagCompound nbttagcompound = itemstack.b("BlockEntityTag");

            if (!itemstack.isEmpty() && nbttagcompound != null && nbttagcompound.hasKey("x") && nbttagcompound.hasKey("y") && nbttagcompound.hasKey("z")) {
                BlockPosition blockposition = new BlockPosition(nbttagcompound.getInt("x"), nbttagcompound.getInt("y"), nbttagcompound.getInt("z"));
                TileEntity tileentity = this.player.world.getTileEntity(blockposition);

                if (tileentity != null) {
                    NBTTagCompound nbttagcompound1 = tileentity.save(new NBTTagCompound());

                    nbttagcompound1.remove("x");
                    nbttagcompound1.remove("y");
                    nbttagcompound1.remove("z");
                    itemstack.a("BlockEntityTag", (NBTBase) nbttagcompound1);
                }
            }

            boolean flag1 = packetplayinsetcreativeslot.b() >= 1 && packetplayinsetcreativeslot.b() <= 45;
            boolean flag2 = itemstack.isEmpty() || itemstack.getDamage() >= 0 && itemstack.getCount() <= 64 && !itemstack.isEmpty();

            if (flag1 && flag2) {
                if (itemstack.isEmpty()) {
                    this.player.defaultContainer.setItem(packetplayinsetcreativeslot.b(), ItemStack.b);
                } else {
                    this.player.defaultContainer.setItem(packetplayinsetcreativeslot.b(), itemstack);
                }

                this.player.defaultContainer.a(this.player, true);
                this.player.defaultContainer.c();
            } else if (flag && flag2 && this.j < 200) {
                this.j += 20;
                this.player.drop(itemstack, true);
            }
        }

    }

    @Override
    public void a(PacketPlayInTransaction packetplayintransaction) {
        PlayerConnectionUtils.ensureMainThread(packetplayintransaction, this, this.player.getWorldServer());
        int i = this.player.activeContainer.windowId;

        if (i == packetplayintransaction.b() && this.k.getOrDefault(i, (short) (packetplayintransaction.c() + 1)) == packetplayintransaction.c() && !this.player.activeContainer.c(this.player) && !this.player.isSpectator()) {
            this.player.activeContainer.a(this.player, true);
        }

    }

    @Override
    public void a(PacketPlayInUpdateSign packetplayinupdatesign) {
        PlayerConnectionUtils.ensureMainThread(packetplayinupdatesign, this, this.player.getWorldServer());
        this.player.resetIdleTimer();
        WorldServer worldserver = this.player.getWorldServer();
        BlockPosition blockposition = packetplayinupdatesign.b();

        if (worldserver.isLoaded(blockposition)) {
            IBlockData iblockdata = worldserver.getType(blockposition);
            TileEntity tileentity = worldserver.getTileEntity(blockposition);

            if (!(tileentity instanceof TileEntitySign)) {
                return;
            }

            TileEntitySign tileentitysign = (TileEntitySign) tileentity;

            if (!tileentitysign.d() || tileentitysign.f() != this.player) {
                PlayerConnection.LOGGER.warn("Player {} just tried to change non-editable sign", this.player.getDisplayName().getString());
                return;
            }

            String[] astring = packetplayinupdatesign.c();

            for (int i = 0; i < astring.length; ++i) {
                tileentitysign.a(i, new ChatComponentText(EnumChatFormat.a(astring[i])));
            }

            tileentitysign.update();
            worldserver.notify(blockposition, iblockdata, iblockdata, 3);
        }

    }

    @Override
    public void a(PacketPlayInKeepAlive packetplayinkeepalive) {
        if (this.awaitingKeepAlive && packetplayinkeepalive.b() == this.h) {
            int i = (int) (SystemUtils.getMonotonicMillis() - this.lastKeepAlive);

            this.player.ping = (this.player.ping * 3 + i) / 4;
            this.awaitingKeepAlive = false;
        } else if (!this.isExemptPlayer()) {
            this.disconnect(new ChatMessage("disconnect.timeout"));
        }

    }

    @Override
    public void a(PacketPlayInAbilities packetplayinabilities) {
        PlayerConnectionUtils.ensureMainThread(packetplayinabilities, this, this.player.getWorldServer());
        this.player.abilities.isFlying = packetplayinabilities.isFlying() && this.player.abilities.canFly;
    }

    @Override
    public void a(PacketPlayInSettings packetplayinsettings) {
        PlayerConnectionUtils.ensureMainThread(packetplayinsettings, this, this.player.getWorldServer());
        this.player.a(packetplayinsettings);
    }

    @Override
    public void a(PacketPlayInCustomPayload packetplayincustompayload) {}

    @Override
    public void a(PacketPlayInDifficultyChange packetplayindifficultychange) {
        PlayerConnectionUtils.ensureMainThread(packetplayindifficultychange, this, this.player.getWorldServer());
        if (this.player.k(2) || this.isExemptPlayer()) {
            this.minecraftServer.a(packetplayindifficultychange.b(), false);
        }
    }

    @Override
    public void a(PacketPlayInDifficultyLock packetplayindifficultylock) {
        PlayerConnectionUtils.ensureMainThread(packetplayindifficultylock, this, this.player.getWorldServer());
        if (this.player.k(2) || this.isExemptPlayer()) {
            this.minecraftServer.b(packetplayindifficultylock.b());
        }
    }
}
