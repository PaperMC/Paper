package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockDamageLevel;
import org.bukkit.Location;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockRightClickEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
// CraftBukkit end

public class NetServerHandler extends NetHandler implements ICommandListener {

    public static Logger a = Logger.getLogger("Minecraft");
    public NetworkManager b;
    public boolean c = false;
    private MinecraftServer d;
    private EntityPlayer e;
    private int f = 0;
    private double g;
    private double h;
    private double i;
    private boolean j = true;
    private Map k = new HashMap();

    public NetServerHandler(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        this.d = minecraftserver;
        this.b = networkmanager;
        networkmanager.a((NetHandler) this);
        this.e = entityplayer;
        entityplayer.a = this;

        // CraftBukkit start
        server = minecraftserver.server;
    }
    private final CraftServer server;

    // Get position of last block hit for BlockDamageLevel.STOPPED
    private int lastX;
    private int lastY;
    private int lastZ;

    // Store the last block right clicked and what type it was
    private CraftBlock lastRightClicked;
    private int lastMaterial;

    public CraftPlayer getPlayer() {
        return (e == null) ? null : (CraftPlayer) e.getBukkitEntity();
    }
    // CraftBukkit end

    public void a() {
        this.b.a();
        if (this.f++ % 20 == 0) {
            this.b.a((Packet) (new Packet0KeepAlive()));
        }
    }

    public void a(String s) {
        // CraftBukkit start
        String leaveMessage = "§e" + this.e.name + " left the game.";
        PlayerKickEvent kickEvent = new PlayerKickEvent(org.bukkit.event.Event.Type.PLAYER_KICK, server.getPlayer(this.e), s, leaveMessage);
        server.getPluginManager().callEvent(kickEvent);
        if (kickEvent.isCancelled()) {
            // Do not kick the player
            return;
        }
        // Send the possibly modified leave message
        this.d.f.a((Packet) (new Packet3Chat( kickEvent.getLeaveMessage() )));
        this.d.f.c(this.e);
        this.b.a((Packet) (new Packet255KickDisconnect( kickEvent.getReason() )));
        // CraftBukkit end

        this.b.c();
        this.c = true;
    }

    public void a(Packet10Flying packet10flying) {
        double d0;

        if (!this.j) {
            d0 = packet10flying.b - this.h;
            if (packet10flying.a == this.g && d0 * d0 < 0.01D && packet10flying.c == this.i) {
                this.j = true;
            }
        }

        // CraftBukkit start
        Player player = getPlayer();
        Location from = new Location(player.getWorld(), g, h, i, this.e.yaw, this.e.pitch);
        Location to = player.getLocation();
        if (!from.equals(to)) {
            PlayerMoveEvent event = new PlayerMoveEvent(Type.PLAYER_MOVE, player, from, to);
            server.getPluginManager().callEvent(event);

            from = event.getFrom();
            to = event.isCancelled() ? from : event.getTo();

            this.e.locX = to.getX();
            this.e.locY = to.getY();
            this.e.locZ = to.getZ();
            this.e.yaw = to.getYaw();
            this.e.pitch = to.getPitch();
        }
        // CraftBukkit end

        if (this.j) {
            double d1;
            double d2;
            double d3;
            double d4;

            if (this.e.vehicle != null) {
                float f = this.e.yaw;
                float f1 = this.e.pitch;

                this.e.vehicle.E();
                d1 = this.e.locX;
                d2 = this.e.locY;
                d3 = this.e.locZ;
                double d5 = 0.0D;

                d4 = 0.0D;
                if (packet10flying.i) {
                    f = packet10flying.e;
                    f1 = packet10flying.f;
                }

                if (packet10flying.h && packet10flying.b == -999.0D && packet10flying.d == -999.0D) {
                    d5 = packet10flying.a;
                    d4 = packet10flying.c;
                }

                this.e.onGround = packet10flying.g;
                this.e.n();
                this.e.c(d5, 0.0D, d4);
                this.e.b(d1, d2, d3, f, f1);
                this.e.motX = d5;
                this.e.motZ = d4;
                if (this.e.vehicle != null) {
                    this.d.e.b(this.e.vehicle, true);
                }

                if (this.e.vehicle != null) {
                    this.e.vehicle.E();
                }

                this.d.f.b(this.e);
                this.g = this.e.locX;
                this.h = this.e.locY;
                this.i = this.e.locZ;
                this.d.e.f(this.e);
                return;
            }

            d0 = this.e.locY;
            this.g = this.e.locX;
            this.h = this.e.locY;
            this.i = this.e.locZ;
            d1 = this.e.locX;
            d2 = this.e.locY;
            d3 = this.e.locZ;
            float f2 = this.e.yaw;
            float f3 = this.e.pitch;

            if (packet10flying.h && packet10flying.b == -999.0D && packet10flying.d == -999.0D) {
                packet10flying.h = false;
            }

            if (packet10flying.h) {
                d1 = packet10flying.a;
                d2 = packet10flying.b;
                d3 = packet10flying.c;
                d4 = packet10flying.d - packet10flying.b;
                if (d4 > 1.65D || d4 < 0.1D) {
                    this.a("Illegal stance");
                    a.warning(this.e.name + " had an illegal stance: " + d4);
                }

                this.e.al = packet10flying.d;
            }

            if (packet10flying.i) {
                f2 = packet10flying.e;
                f3 = packet10flying.f;
            }

            this.e.n();
            this.e.R = 0.0F;
            this.e.b(this.g, this.h, this.i, f2, f3);
            d4 = d1 - this.e.locX;
            double d6 = d2 - this.e.locY;
            double d7 = d3 - this.e.locZ;
            float f4 = 0.0625F;
            boolean flag = this.d.e.a(this.e, this.e.boundingBox.b().e((double) f4, (double) f4, (double) f4)).size() == 0;

            this.e.c(d4, d6, d7);
            d4 = d1 - this.e.locX;
            d6 = d2 - this.e.locY;
            if (d6 > -0.5D || d6 < 0.5D) {
                d6 = 0.0D;
            }

            d7 = d3 - this.e.locZ;
            double d8 = d4 * d4 + d6 * d6 + d7 * d7;
            boolean flag1 = false;

            if (d8 > 0.0625D) {
                flag1 = true;
                a.warning(this.e.name + " moved wrongly!");
                System.out.println("Got position " + d1 + ", " + d2 + ", " + d3);
                System.out.println("Expected " + this.e.locX + ", " + this.e.locY + ", " + this.e.locZ);
            }

            this.e.b(d1, d2, d3, f2, f3);
            boolean flag2 = this.d.e.a(this.e, this.e.boundingBox.b().e((double) f4, (double) f4, (double) f4)).size() == 0;

            if (flag && (flag1 || !flag2)) {
                this.a(this.g, this.h, this.i, f2, f3);
                return;
            }

            this.e.onGround = packet10flying.g;
            this.d.f.b(this.e);
            this.e.b(this.e.locY - d0, packet10flying.g);
        }
    }

    public void a(double d0, double d1, double d2, float f, float f1) {
        // CraftBukkit start
        Player player = getPlayer();
        Location from = player.getLocation();
        Location to = new Location(player.getWorld(), d0, d1, d2, f, f1);
        PlayerMoveEvent event = new PlayerMoveEvent(Type.PLAYER_TELEPORT, player, from, to);
        server.getPluginManager().callEvent(event);

        from = event.getFrom();
        to = event.isCancelled() ? from : event.getTo();

        d0 = to.getX();
        d1 = to.getY();
        d2 = to.getZ();
        f = to.getYaw();
        f1 = to.getPitch();
        // CraftBukkit end

        this.j = false;
        this.g = d0;
        this.h = d1;
        this.i = d2;
        this.e.b(d0, d1, d2, f, f1);
        this.e.a.b((Packet) (new Packet13PlayerLookMove(d0, d1 + 1.6200000047683716D, d1, d2, f, f1, false)));
    }

    public void a(Packet14BlockDig packet14blockdig) {
        if (packet14blockdig.e == 4) {
            this.e.O();
        } else {
            boolean flag = this.d.e.B = this.d.f.g(this.e.name);
            boolean flag1 = false;

            if (packet14blockdig.e == 0) {
                flag1 = true;
            }

            if (packet14blockdig.e == 1) {
                flag1 = true;
            }

            int i = packet14blockdig.a;
            int j = packet14blockdig.b;
            int k = packet14blockdig.c;

            if (flag1) {
                double d0 = this.e.locX - ((double) i + 0.5D);
                double d1 = this.e.locY - ((double) j + 0.5D);
                double d2 = this.e.locZ - ((double) k + 0.5D);
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > 36.0D) {
                    return;
                }

                double d4 = this.e.locY;

                this.e.locY = this.e.al;
                this.e.locY = d4;
            }

            int l = packet14blockdig.d;
            int i1 = (int) MathHelper.e((float) (i - this.d.e.spawnX));
            int j1 = (int) MathHelper.e((float) (k - this.d.e.spawnZ));

            if (i1 > j1) {
                j1 = i1;
            }

            // CraftBukkit start
            CraftPlayer player = getPlayer();
            CraftBlock block = (CraftBlock) player.getWorld().getBlockAt(i, j, k);
            int blockId = block.getTypeId();
            float damage = 0;
            if(Block.byId[blockId] != null) {
                damage = Block.byId[blockId].a(player.getHandle()); //Get amount of damage going to block
            }
            // CraftBukkit end

            if (packet14blockdig.e == 0) {
                if (j1 > 16 || flag) {
                    // CraftBukkit start
                    if(blockId > 0) {
                        BlockDamageEvent event;
                        // If the amount of damage that the player is going to do to the block
                        // is >= 1, then the block is going to break (eg, flowers, torches)
                        if (damage >= 1.0F) {
                            event = new BlockDamageEvent(Type.BLOCK_DAMAGED, block, BlockDamageLevel.BROKEN, player);
                        } else {
                            event = new BlockDamageEvent(Type.BLOCK_DAMAGED, block, BlockDamageLevel.STARTED, player);
                        }
                        server.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            this.e.c.a(i, j, k);
                        }
                    }
                    // CraftBukkit end
                }
            } else if (packet14blockdig.e == 2) {
                // CraftBukkit start - Get last block that the player hit
                // Otherwise the block is a Bedrock @(0,0,0)
                block = (CraftBlock) player.getWorld().getBlockAt(lastX, lastY, lastZ);
                BlockDamageEvent event = new BlockDamageEvent(Type.BLOCK_DAMAGED, block, BlockDamageLevel.STOPPED, player);
                server.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    this.e.c.a();
                }
                // CraftBukkit end
            } else if (packet14blockdig.e == 1) {
                if (j1 > 16 || flag) {
                    // CraftBukkit start
                    BlockDamageEvent event;
                    // If the amount of damage going to the block plus the current amount
                    // of damage is greater than 1, the block is going to break.
                    if (e.c.d + damage  >= 1.0F) {
                        event = new BlockDamageEvent(Type.BLOCK_DAMAGED, block, BlockDamageLevel.BROKEN, player);
                    } else {
                        event = new BlockDamageEvent(Type.BLOCK_DAMAGED, block, BlockDamageLevel.DIGGING, player);
                    }
                    server.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        this.e.c.a(i, j, k, l);
                    } else {
                        e.c.d = 0; // Reset the amount of damage if stopping break.
                    }
                    // CraftBukkit end
                }
            } else if (packet14blockdig.e == 3) {
                double d5 = this.e.locX - ((double) i + 0.5D);
                double d6 = this.e.locY - ((double) j + 0.5D);
                double d7 = this.e.locZ - ((double) k + 0.5D);
                double d8 = d5 * d5 + d6 * d6 + d7 * d7;

                if (d8 < 256.0D) {
                    this.e.a.b((Packet) (new Packet53BlockChange(i, j, k, this.d.e)));
                }
            }

            // CraftBukkit start
            lastX = i;
            lastY = j;
            lastZ = k;
            // CraftBukkit end

            this.d.e.B = false;
        }
    }

    public void a(Packet15Place packet15place) {
        ItemStack itemstack = this.e.inventory.e();
        boolean flag = this.d.e.B = this.d.f.g(this.e.name);

        // CraftBukkit start
        CraftBlock blockClicked = null;
        BlockFace blockFace = null;

        if (packet15place.d == 255) {
            // CraftBukkit ITEM_USE -- if we have a lastRightClicked then it could be a usable location
            if (packet15place.e != null && packet15place.e.id == lastMaterial) {
                blockClicked = lastRightClicked;
            } else if (lastMaterial == 0) {
                blockClicked = lastRightClicked;
            }
            lastRightClicked = null;
            lastMaterial = 0;
        } else {
            // CraftBukkit RIGHTCLICK or BLOCK_PLACE .. or nothing
            blockClicked = (CraftBlock) d.e.getWorld().getBlockAt(packet15place.a, packet15place.b, packet15place.c);
            lastRightClicked = blockClicked;
            lastMaterial = (packet15place.e == null) ? 0 : packet15place.e.id;
        }

        if (blockClicked != null && itemstack != null) {
            blockFace = CraftBlock.notchToBlockFace(packet15place.d);
        } else {
            blockFace = BlockFace.SELF;
        }

        // CraftBukkit if rightclick decremented the item, always send the update packet.
        // this is not here for CraftBukkit's own functionality; rather it is to fix
        // a notch bug where the item doesn't update correctly.
        boolean always = false;
        // CraftBukkit end

        if (packet15place.d == 255) {
            if (itemstack == null) {
                return;
            }

            // CraftBukkit start
            Type eventType = Type.PLAYER_ITEM;
            Player who = (this.e == null) ? null : (Player) this.e.getBukkitEntity();
            org.bukkit.inventory.ItemStack itemInHand = new CraftItemStack(itemstack);

            PlayerItemEvent event = new PlayerItemEvent(eventType, who, itemInHand, blockClicked, blockFace);

            // CraftBukkit We still call this event even in spawn protection.
            // Don't call this event if using Buckets / signs
            switch (itemInHand.getType()) {
                case SIGN:
                case BUCKET:
                case WATER_BUCKET:
                case LAVA_BUCKET:
                    break;
                default:
                    server.getPluginManager().callEvent(event);
            }

            if (!event.isCancelled()) {
                int itemstackAmount = itemstack.count;
                this.e.c.a(this.e, this.d.e, itemstack);

                // CraftBukkit notch decrements the counter by 1 in the above method with food,
                // snowballs and so forth, but he does it in a place that doesnt cause the
                // inventory update packet to get sent
                always = (itemstack.count != itemstackAmount);
            }
            // CraftBukkit end
        } else {
            int i = packet15place.a;
            int j = packet15place.b;
            int k = packet15place.c;
            int l = packet15place.d;
            int i1 = (int) MathHelper.e((float) (i - this.d.e.spawnX));
            int j1 = (int) MathHelper.e((float) (k - this.d.e.spawnZ));

            if (i1 > j1) {
                j1 = i1;
            }

            // CraftBukkit start - spawn protection moved to ItemBlock!!!
            CraftItemStack craftItem = new CraftItemStack(itemstack);
            Player player = getPlayer();
            BlockRightClickEvent event = new BlockRightClickEvent(Type.BLOCK_RIGHTCLICKED, blockClicked, blockFace, craftItem, player);
            server.getPluginManager().callEvent(event);

            this.e.c.a(this.e, this.d.e, itemstack, i, j, k, l);
            // CraftBukkit end

            this.e.a.b((Packet) (new Packet53BlockChange(i, j, k, this.d.e)));
            if (l == 0) {
                --j;
            }

            if (l == 1) {
                ++j;
            }

            if (l == 2) {
                --k;
            }

            if (l == 3) {
                ++k;
            }

            if (l == 4) {
                --i;
            }

            if (l == 5) {
                ++i;
            }

            this.e.a.b((Packet) (new Packet53BlockChange(i, j, k, this.d.e)));
        }

        if (itemstack != null && itemstack.count == 0) {
            this.e.inventory.a[this.e.inventory.c] = null;
        }

        this.e.am = true;
        this.e.inventory.a[this.e.inventory.c] = ItemStack.b(this.e.inventory.a[this.e.inventory.c]);
        Slot slot = this.e.activeContainer.a(this.e.inventory, this.e.inventory.c);

        this.e.activeContainer.a();
        this.e.am = false;

        // CraftBukkit - Boolean flag
        if (!ItemStack.a(this.e.inventory.e(), packet15place.e) || always) {
            this.b((Packet) (new Packet103SetSlot(this.e.activeContainer.f, slot.c, this.e.inventory.e())));
        }

        this.d.e.B = false;
    }

    public void a(String s, Object[] aobject) {
        a.info(this.e.name + " lost connection: " + s);
        this.d.f.a((Packet) (new Packet3Chat("§e" + this.e.name + " left the game.")));
        this.d.f.c(this.e);
        this.c = true;
    }

    public void a(Packet packet) {
        a.warning(this.getClass() + " wasn\'t prepared to deal with a " + packet.getClass());
        this.a("Protocol error, unexpected packet");
    }

    public void b(Packet packet) {
        this.b.a(packet);
    }

    public void a(Packet16BlockItemSwitch packet16blockitemswitch) {
        // Craftbukkit start
        PlayerItemHeldEvent event = new PlayerItemHeldEvent(Type.PLAYER_ITEM_HELD, getPlayer(), e.inventory.c, packet16blockitemswitch.a);
        server.getPluginManager().callEvent(event);
        // Craftbukkit end

        this.e.inventory.c = packet16blockitemswitch.a;
    }

    public void a(Packet3Chat packet3chat) {
        String s = packet3chat.a;

        if (s.length() > 100) {
            this.a("Chat message too long");
        } else {
            s = s.trim();

            for (int i = 0; i < s.length(); ++i) {
                if (FontAllowedCharacters.a.indexOf(s.charAt(i)) < 0) {
                    this.a("Illegal characters in chat");
                    return;
                }
            }

            if (s.startsWith("/")) {
                this.c(s);
            } else {
                // CraftBukkit start
                Player player = getPlayer();
                PlayerChatEvent event = new PlayerChatEvent(Type.PLAYER_CHAT, player, s);
                server.getPluginManager().callEvent(event);
                s = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
                if (event.isCancelled()) {
                    return;
                }
                // CraftBukkit end

                a.info(s);
                this.d.f.a((Packet) (new Packet3Chat(s)));
            }
        }
    }

    private void c(String s) {
        // CraftBukkit start
        CraftPlayer player = getPlayer();
        boolean targetPluginFound = server.dispatchCommand(player, s.substring(1));
        if (targetPluginFound) {
            return;
        }

        PlayerChatEvent event = new PlayerChatEvent(Type.PLAYER_COMMAND, player, s);
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        s = event.getMessage();
        player = (CraftPlayer) event.getPlayer();
        EntityPlayer e = player.getHandle();
        // CraftBukkit stop

        if (s.toLowerCase().startsWith("/me ")) {
            s = "* " + this.e.name + " " + s.substring(s.indexOf(" ")).trim();
            a.info(s);
            this.d.f.a((Packet) (new Packet3Chat(s)));
        } else if (s.toLowerCase().startsWith("/kill")) {
            this.e.a((Entity) null, 1000);
        } else if (s.toLowerCase().startsWith("/tell ")) {
            String[] astring = s.split(" ");

            if (astring.length >= 3) {
                s = s.substring(s.indexOf(" ")).trim();
                s = s.substring(s.indexOf(" ")).trim();
                s = "§7" + this.e.name + " whispers " + s;
                a.info(s + " to " + astring[1]);
                if (!this.d.f.a(astring[1], (Packet) (new Packet3Chat(s)))) {
                    this.b((Packet) (new Packet3Chat("§cThere\'s no player by that name online.")));
                }
            }
        } else {
            String s1;

            if (this.d.f.g(this.e.name)) {
                s1 = s.substring(1);
                a.info(this.e.name + " issued server command: " + s1);
                this.d.a(s1, (ICommandListener) this);
            } else {
                s1 = s.substring(1);
                a.info(this.e.name + " tried command: " + s1);
            }
        }
    }

    public void a(Packet18ArmAnimation packet18armanimation) {
        if (packet18armanimation.b == 1) {
            // CraftBukkit start - Arm swing animation
            Player player = getPlayer();
            PlayerAnimationEvent event = new PlayerAnimationEvent(Type.PLAYER_ANIMATION, player);
            server.getPluginManager().callEvent(event);
            // CraftBukkit end

            this.e.K();
        }
    }

    public void a(Packet19EntityAction packet19entityaction) {
        if (packet19entityaction.b == 1) {
            this.e.b(true);
        } else if (packet19entityaction.b == 2) {
            this.e.b(false);
        }
    }

    public void a(Packet255KickDisconnect packet255kickdisconnect) {
        this.b.a("disconnect.quitting", new Object[0]);
    }

    public int b() {
        return this.b.d();
    }

    public void b(String s) {
        this.b((Packet) (new Packet3Chat("§7" + s)));
    }

    public String c() {
        return this.e.name;
    }

    public void a(Packet7UseEntity packet7useentity) {
        Entity entity = this.d.e.a(packet7useentity.b);

        if (entity != null && this.e.i(entity)) {
            if (packet7useentity.c == 0) {
                this.e.g(entity);
            } else if (packet7useentity.c == 1) {
                this.e.h(entity);
            }
        }
    }

    public void a(Packet9Respawn packet9respawn) {
        if (this.e.health <= 0) {
            this.e = this.d.f.d(this.e);

            // CraftBukkit start
            CraftPlayer player = getPlayer();
            player.setHandle(e);
            // CraftBukkit end
        }
    }

    public void a(Packet101CloseWindow packet101closewindow) {
        this.e.N();
    }

    public void a(Packet102WindowClick packet102windowclick) {
        if (this.e.activeContainer.f == packet102windowclick.a && this.e.activeContainer.c(this.e)) {
            ItemStack itemstack = this.e.activeContainer.a(packet102windowclick.b, packet102windowclick.c, this.e);

            if (ItemStack.a(packet102windowclick.e, itemstack)) {
                this.e.a.b((Packet) (new Packet106Transaction(packet102windowclick.a, packet102windowclick.d, true)));
                this.e.am = true;
                this.e.activeContainer.a();
                this.e.M();
                this.e.am = false;
            } else {
                this.k.put(Integer.valueOf(this.e.activeContainer.f), Short.valueOf(packet102windowclick.d));
                this.e.a.b((Packet) (new Packet106Transaction(packet102windowclick.a, packet102windowclick.d, false)));
                this.e.activeContainer.a(this.e, false);
                ArrayList arraylist = new ArrayList();

                for (int i = 0; i < this.e.activeContainer.e.size(); ++i) {
                    arraylist.add(((Slot) this.e.activeContainer.e.get(i)).c());
                }

                this.e.a(this.e.activeContainer, arraylist);
            }
        }
    }

    public void a(Packet106Transaction packet106transaction) {
        Short oshort = (Short) this.k.get(Integer.valueOf(this.e.activeContainer.f));

        if (oshort != null && packet106transaction.b == oshort.shortValue() && this.e.activeContainer.f == packet106transaction.a && !this.e.activeContainer.c(this.e)) {
            this.e.activeContainer.a(this.e, true);
        }
    }

    public void a(Packet130UpdateSign packet130updatesign) {
        if (this.d.e.f(packet130updatesign.a, packet130updatesign.b, packet130updatesign.c)) {
            TileEntity tileentity = this.d.e.getTileEntity(packet130updatesign.a, packet130updatesign.b, packet130updatesign.c);

            int i;
            int j;

            for (i = 0; i < 4; ++i) {
                boolean flag = true;

                if (packet130updatesign.d[i].length() > 15) {
                    flag = false;
                } else {
                    for (j = 0; j < packet130updatesign.d[i].length(); ++j) {
                        if (FontAllowedCharacters.a.indexOf(packet130updatesign.d[i].charAt(j)) < 0) {
                            flag = false;
                        }
                    }
                }

                if (!flag) {
                    packet130updatesign.d[i] = "!?";
                }
            }

            if (tileentity instanceof TileEntitySign) {
                i = packet130updatesign.a;
                int k = packet130updatesign.b;

                j = packet130updatesign.c;
                TileEntitySign tileentitysign = (TileEntitySign) tileentity;

                for (int l = 0; l < 4; ++l) {
                    tileentitysign.e[l] = packet130updatesign.d[l];
                }

                tileentitysign.d();
                this.d.e.g(i, k, j);
            }
        }
    }
}
