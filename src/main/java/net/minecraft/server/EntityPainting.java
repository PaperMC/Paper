package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakByWorldEvent;
import org.bukkit.entity.Painting;
// CraftBukkit end

public class EntityPainting extends Entity {

    private int f;
    public int a;
    public int b;
    public int c;
    public int d;
    public EnumArt e;

    public EntityPainting(World world) {
        super(world);
        this.f = 0;
        this.a = 0;
        this.height = 0.0F;
        this.b(0.5F, 0.5F);
    }

    public EntityPainting(World world, int i, int j, int k, int l) {
        this(world);
        this.b = i;
        this.c = j;
        this.d = k;
        ArrayList arraylist = new ArrayList();
        EnumArt[] aenumart = EnumArt.values();
        int i1 = aenumart.length;

        for (int j1 = 0; j1 < i1; ++j1) {
            EnumArt enumart = aenumart[j1];

            this.e = enumart;
            this.b(l);
            if (this.h()) {
                arraylist.add(enumart);
            }
        }

        if (arraylist.size() > 0) {
            this.e = (EnumArt) arraylist.get(this.random.nextInt(arraylist.size()));
        }

        this.b(l);
    }

    protected void b() {}

    public void b(int i) {
        this.a = i;
        this.lastYaw = this.yaw = (float) (i * 90);
        float f = (float) this.e.B;
        float f1 = (float) this.e.C;
        float f2 = (float) this.e.B;

        if (i != 0 && i != 2) {
            f = 0.5F;
        } else {
            f2 = 0.5F;
        }

        f /= 32.0F;
        f1 /= 32.0F;
        f2 /= 32.0F;
        float f3 = (float) this.b + 0.5F;
        float f4 = (float) this.c + 0.5F;
        float f5 = (float) this.d + 0.5F;
        float f6 = 0.5625F;

        if (i == 0) {
            f5 -= f6;
        }

        if (i == 1) {
            f3 -= f6;
        }

        if (i == 2) {
            f5 += f6;
        }

        if (i == 3) {
            f3 += f6;
        }

        if (i == 0) {
            f3 -= this.c(this.e.B);
        }

        if (i == 1) {
            f5 += this.c(this.e.B);
        }

        if (i == 2) {
            f3 += this.c(this.e.B);
        }

        if (i == 3) {
            f5 -= this.c(this.e.B);
        }

        f4 += this.c(this.e.C);
        this.setPosition((double) f3, (double) f4, (double) f5);
        float f7 = -0.00625F;

        this.boundingBox.c((double) (f3 - f - f7), (double) (f4 - f1 - f7), (double) (f5 - f2 - f7), (double) (f3 + f + f7), (double) (f4 + f1 + f7), (double) (f5 + f2 + f7));
    }

    private float c(int i) {
        return i == 32 ? 0.5F : (i == 64 ? 0.5F : 0.0F);
    }

    public void p_() {
        if (this.f++ == 100 && !this.world.isStatic) {
            this.f = 0;
            if (!this.h()) {

                // CraftBukkit start
                Painting painting = (Painting) getBukkitEntity();
                PaintingBreakByWorldEvent event = new PaintingBreakByWorldEvent(painting);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return;
                }
                // CraftBukkit end

                this.die();
                this.world.addEntity(new EntityItem(this.world, this.locX, this.locY, this.locZ, new ItemStack(Item.PAINTING)));
            }
        }
    }

    public boolean h() {
        if (this.world.getEntities(this, this.boundingBox).size() > 0) {
            return false;
        } else {
            int i = this.e.B / 16;
            int j = this.e.C / 16;
            int k = this.b;
            int l = this.c;
            int i1 = this.d;

            if (this.a == 0) {
                k = MathHelper.floor(this.locX - (double) ((float) this.e.B / 32.0F));
            }

            if (this.a == 1) {
                i1 = MathHelper.floor(this.locZ - (double) ((float) this.e.B / 32.0F));
            }

            if (this.a == 2) {
                k = MathHelper.floor(this.locX - (double) ((float) this.e.B / 32.0F));
            }

            if (this.a == 3) {
                i1 = MathHelper.floor(this.locZ - (double) ((float) this.e.B / 32.0F));
            }

            l = MathHelper.floor(this.locY - (double) ((float) this.e.C / 32.0F));

            int j1;

            for (int k1 = 0; k1 < i; ++k1) {
                for (j1 = 0; j1 < j; ++j1) {
                    Material material;

                    if (this.a != 0 && this.a != 2) {
                        material = this.world.getMaterial(this.b, l + j1, i1 + k1);
                    } else {
                        material = this.world.getMaterial(k + k1, l + j1, this.d);
                    }

                    if (!material.isBuildable()) {
                        return false;
                    }
                }
            }

            List list = this.world.b((Entity) this, this.boundingBox);

            for (j1 = 0; j1 < list.size(); ++j1) {
                if (list.get(j1) instanceof EntityPainting) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean o_() {
        return true;
    }

    public boolean damageEntity(Entity entity, int i) {
        if (!this.dead && !this.world.isStatic) {

            // CraftBukkit start
            Painting painting = (Painting) getBukkitEntity();
            PaintingBreakByEntityEvent event = new PaintingBreakByEntityEvent(painting,entity.getBukkitEntity());
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return true;
            }
            // CraftBukkit end

            this.die();
            this.ab();
            this.world.addEntity(new EntityItem(this.world, this.locX, this.locY, this.locZ, new ItemStack(Item.PAINTING)));
        }

        return true;
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Dir", (byte) this.a);
        nbttagcompound.setString("Motive", this.e.A);
        nbttagcompound.a("TileX", this.b);
        nbttagcompound.a("TileY", this.c);
        nbttagcompound.a("TileZ", this.d);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.a = nbttagcompound.c("Dir");
        this.b = nbttagcompound.e("TileX");
        this.c = nbttagcompound.e("TileY");
        this.d = nbttagcompound.e("TileZ");
        String s = nbttagcompound.getString("Motive");
        EnumArt[] aenumart = EnumArt.values();
        int i = aenumart.length;

        for (int j = 0; j < i; ++j) {
            EnumArt enumart = aenumart[j];

            if (enumart.A.equals(s)) {
                this.e = enumart;
            }
        }

        if (this.e == null) {
            this.e = EnumArt.KEBAB;
        }

        this.b(this.a);
    }
}
