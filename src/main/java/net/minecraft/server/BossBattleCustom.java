package net.minecraft.server;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

// CraftBukkit start
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.craftbukkit.boss.CraftKeyedBossbar;
// CraftBukkit end

public class BossBattleCustom extends BossBattleServer {

    private final MinecraftKey h;
    private final Set<UUID> i = Sets.newHashSet();
    private int j;
    private int k = 100;
    // CraftBukkit start
    private KeyedBossBar bossBar;

    public KeyedBossBar getBukkitEntity() {
        if (bossBar == null) {
            bossBar = new CraftKeyedBossbar(this);
        }
        return bossBar;
    }
    // CraftBukkit end

    public BossBattleCustom(MinecraftKey minecraftkey, IChatBaseComponent ichatbasecomponent) {
        super(ichatbasecomponent, BossBattle.BarColor.WHITE, BossBattle.BarStyle.PROGRESS);
        this.h = minecraftkey;
        this.setProgress(0.0F);
    }

    public MinecraftKey getKey() {
        return this.h;
    }

    @Override
    public void addPlayer(EntityPlayer entityplayer) {
        super.addPlayer(entityplayer);
        this.i.add(entityplayer.getUniqueID());
    }

    public void a(UUID uuid) {
        this.i.add(uuid);
    }

    @Override
    public void removePlayer(EntityPlayer entityplayer) {
        super.removePlayer(entityplayer);
        this.i.remove(entityplayer.getUniqueID());
    }

    @Override
    public void b() {
        super.b();
        this.i.clear();
    }

    public int c() {
        return this.j;
    }

    public int d() {
        return this.k;
    }

    public void a(int i) {
        this.j = i;
        this.setProgress(MathHelper.a((float) i / (float) this.k, 0.0F, 1.0F));
    }

    public void b(int i) {
        this.k = i;
        this.setProgress(MathHelper.a((float) this.j / (float) i, 0.0F, 1.0F));
    }

    public final IChatBaseComponent e() {
        return ChatComponentUtils.a(this.j()).format((chatmodifier) -> {
            return chatmodifier.setColor(this.l().a()).setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, new ChatComponentText(this.getKey().toString()))).setInsertion(this.getKey().toString());
        });
    }

    public boolean a(Collection<EntityPlayer> collection) {
        Set<UUID> set = Sets.newHashSet();
        Set<EntityPlayer> set1 = Sets.newHashSet();
        Iterator iterator = this.i.iterator();

        UUID uuid;
        boolean flag;
        Iterator iterator1;

        while (iterator.hasNext()) {
            uuid = (UUID) iterator.next();
            flag = false;
            iterator1 = collection.iterator();

            while (true) {
                if (iterator1.hasNext()) {
                    EntityPlayer entityplayer = (EntityPlayer) iterator1.next();

                    if (!entityplayer.getUniqueID().equals(uuid)) {
                        continue;
                    }

                    flag = true;
                }

                if (!flag) {
                    set.add(uuid);
                }
                break;
            }
        }

        iterator = collection.iterator();

        EntityPlayer entityplayer1;

        while (iterator.hasNext()) {
            entityplayer1 = (EntityPlayer) iterator.next();
            flag = false;
            iterator1 = this.i.iterator();

            while (true) {
                if (iterator1.hasNext()) {
                    UUID uuid1 = (UUID) iterator1.next();

                    if (!entityplayer1.getUniqueID().equals(uuid1)) {
                        continue;
                    }

                    flag = true;
                }

                if (!flag) {
                    set1.add(entityplayer1);
                }
                break;
            }
        }

        iterator = set.iterator();

        while (iterator.hasNext()) {
            uuid = (UUID) iterator.next();
            Iterator iterator2 = this.getPlayers().iterator();

            while (true) {
                if (iterator2.hasNext()) {
                    EntityPlayer entityplayer2 = (EntityPlayer) iterator2.next();

                    if (!entityplayer2.getUniqueID().equals(uuid)) {
                        continue;
                    }

                    this.removePlayer(entityplayer2);
                }

                this.i.remove(uuid);
                break;
            }
        }

        iterator = set1.iterator();

        while (iterator.hasNext()) {
            entityplayer1 = (EntityPlayer) iterator.next();
            this.addPlayer(entityplayer1);
        }

        return !set.isEmpty() || !set1.isEmpty();
    }

    public NBTTagCompound f() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setString("Name", IChatBaseComponent.ChatSerializer.a(this.title));
        nbttagcompound.setBoolean("Visible", this.g());
        nbttagcompound.setInt("Value", this.j);
        nbttagcompound.setInt("Max", this.k);
        nbttagcompound.setString("Color", this.l().b());
        nbttagcompound.setString("Overlay", this.m().a());
        nbttagcompound.setBoolean("DarkenScreen", this.isDarkenSky());
        nbttagcompound.setBoolean("PlayBossMusic", this.isPlayMusic());
        nbttagcompound.setBoolean("CreateWorldFog", this.isCreateFog());
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.i.iterator();

        while (iterator.hasNext()) {
            UUID uuid = (UUID) iterator.next();

            nbttaglist.add(GameProfileSerializer.a(uuid));
        }

        nbttagcompound.set("Players", nbttaglist);
        return nbttagcompound;
    }

    public static BossBattleCustom a(NBTTagCompound nbttagcompound, MinecraftKey minecraftkey) {
        BossBattleCustom bossbattlecustom = new BossBattleCustom(minecraftkey, IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("Name")));

        bossbattlecustom.setVisible(nbttagcompound.getBoolean("Visible"));
        bossbattlecustom.a(nbttagcompound.getInt("Value"));
        bossbattlecustom.b(nbttagcompound.getInt("Max"));
        bossbattlecustom.a(BossBattle.BarColor.a(nbttagcompound.getString("Color")));
        bossbattlecustom.a(BossBattle.BarStyle.a(nbttagcompound.getString("Overlay")));
        bossbattlecustom.setDarkenSky(nbttagcompound.getBoolean("DarkenScreen"));
        bossbattlecustom.setPlayMusic(nbttagcompound.getBoolean("PlayBossMusic"));
        bossbattlecustom.setCreateFog(nbttagcompound.getBoolean("CreateWorldFog"));
        NBTTagList nbttaglist = nbttagcompound.getList("Players", 11);

        for (int i = 0; i < nbttaglist.size(); ++i) {
            bossbattlecustom.a(GameProfileSerializer.a(nbttaglist.get(i)));
        }

        return bossbattlecustom;
    }

    public void c(EntityPlayer entityplayer) {
        if (this.i.contains(entityplayer.getUniqueID())) {
            this.addPlayer(entityplayer);
        }

    }

    public void d(EntityPlayer entityplayer) {
        super.removePlayer(entityplayer);
    }
}
