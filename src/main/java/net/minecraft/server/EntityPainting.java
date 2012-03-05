package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Painting;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;
import org.bukkit.event.painting.PaintingBreakEvent;
// CraftBukkit end

public class EntityPainting extends Entity {

    private int f;
    public int direction;
    public int x;
    public int y;
    public int z;
    public EnumArt art;

    public EntityPainting(World world) {
        super(world);
        this.f = 0;
        this.direction = 0;
        this.height = 0.0F;
        this.b(0.5F, 0.5F);
        this.art = EnumArt.values()[this.random.nextInt(EnumArt.values().length)]; // CraftBukkit - generate a non-null painting
    }

    public EntityPainting(World world, int i, int j, int k, int l) {
        this(world);
        this.x = i;
        this.y = j;
        this.z = k;
        ArrayList arraylist = new ArrayList();
        EnumArt[] aenumart = EnumArt.values();
        int i1 = aenumart.length;

        for (int j1 = 0; j1 < i1; ++j1) {
            EnumArt enumart = aenumart[j1];

            this.art = enumart;
            this.setDirection(l);
            if (this.survives()) {
                arraylist.add(enumart);
            }
        }

        if (arraylist.size() > 0) {
            this.art = (EnumArt) arraylist.get(this.random.nextInt(arraylist.size()));
        }

        this.setDirection(l);
    }

    protected void b() {}

    public void setDirection(int i) {
        this.direction = i;
        this.lastYaw = this.yaw = (float) (i * 90);
        float f = (float) this.art.B;
        float f1 = (float) this.art.C;
        float f2 = (float) this.art.B;

        if (i != 0 && i != 2) {
            f = 0.5F;
        } else {
            f2 = 0.5F;
        }

        f /= 32.0F;
        f1 /= 32.0F;
        f2 /= 32.0F;
        float f3 = (float) this.x + 0.5F;
        float f4 = (float) this.y + 0.5F;
        float f5 = (float) this.z + 0.5F;
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
            f3 -= this.c(this.art.B);
        }

        if (i == 1) {
            f5 += this.c(this.art.B);
        }

        if (i == 2) {
            f3 += this.c(this.art.B);
        }

        if (i == 3) {
            f5 -= this.c(this.art.B);
        }

        f4 += this.c(this.art.C);
        this.setPosition((double) f3, (double) f4, (double) f5);
        float f7 = -0.00625F;

        this.boundingBox.c((double) (f3 - f - f7), (double) (f4 - f1 - f7), (double) (f5 - f2 - f7), (double) (f3 + f + f7), (double) (f4 + f1 + f7), (double) (f5 + f2 + f7));
    }

    private float c(int i) {
        return i == 32 ? 0.5F : (i == 64 ? 0.5F : 0.0F);
    }

    public void G_() {
        if (this.f++ == 100 && !this.world.isStatic) {
            this.f = 0;
            if (!this.survives()) {
                // CraftBukkit start
                List<org.bukkit.inventory.ItemStack> drops = new ArrayList<org.bukkit.inventory.ItemStack>();
                drops.add(new CraftItemStack(Item.PAINTING.id, 1));
                Material material = this.world.getMaterial((int) this.locX, (int) this.locY, (int) this.locZ);
                RemoveCause cause;
                if (material.equals(Material.WATER)) {
                    cause = RemoveCause.WATER;
                } else if (!material.equals(Material.AIR)) {
                    // TODO: This feels insufficient to catch 100% of suffocation cases
                    cause = RemoveCause.OBSTRUCTION;
                } else {
                    cause = RemoveCause.PHYSICS;
                }
                PaintingBreakEvent event = new PaintingBreakEvent((Painting) this.getBukkitEntity(), cause, drops);
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled() || dead) {
                    return;
                }
                // CraftBukkit end

                this.die();
                // CraftBukkit start - replace following line with the loop
                //this.world.addEntity(new EntityItem(this.world, this.locX, this.locY, this.locZ, new ItemStack(Item.PAINTING)));
                for (org.bukkit.inventory.ItemStack stack : drops) {
                    this.world.addEntity(new EntityItem(this.world, this.locX, this.locY, this.locZ, CraftItemStack.createNMSItemStack(stack)));
                }
                // CraftBukkit end
            }
        }
    }

    public boolean survives() {
        if (this.world.getCubes(this, this.boundingBox).size() > 0) {
            return false;
        } else {
            int i = this.art.B / 16;
            int j = this.art.C / 16;
            int k = this.x;
            int l = this.y;
            int i1 = this.z;

            if (this.direction == 0) {
                k = MathHelper.floor(this.locX - (double) ((float) this.art.B / 32.0F));
            }

            if (this.direction == 1) {
                i1 = MathHelper.floor(this.locZ - (double) ((float) this.art.B / 32.0F));
            }

            if (this.direction == 2) {
                k = MathHelper.floor(this.locX - (double) ((float) this.art.B / 32.0F));
            }

            if (this.direction == 3) {
                i1 = MathHelper.floor(this.locZ - (double) ((float) this.art.B / 32.0F));
            }

            l = MathHelper.floor(this.locY - (double) ((float) this.art.C / 32.0F));

            int j1;

            for (int k1 = 0; k1 < i; ++k1) {
                for (j1 = 0; j1 < j; ++j1) {
                    Material material;

                    if (this.direction != 0 && this.direction != 2) {
                        material = this.world.getMaterial(this.x, l + j1, i1 + k1);
                    } else {
                        material = this.world.getMaterial(k + k1, l + j1, this.z);
                    }

                    if (!material.isBuildable()) {
                        return false;
                    }
                }
            }

            List list = this.world.getEntities(this, this.boundingBox);

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

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (!this.dead && !this.world.isStatic) {
            // CraftBukkit start
            List<org.bukkit.inventory.ItemStack> drops = new ArrayList<org.bukkit.inventory.ItemStack>();
            drops.add(new CraftItemStack(Item.PAINTING.id, 1));
            PaintingBreakEvent event = null;
            if (damagesource.getEntity() != null) {
                event = new PaintingBreakByEntityEvent((Painting) this.getBukkitEntity(), damagesource.getEntity().getBukkitEntity(), drops);
            } else {
                if (damagesource == DamageSource.FIRE) {
                    event = new PaintingBreakEvent((Painting) this.getBukkitEntity(), RemoveCause.FIRE, drops);
                }
                // TODO: Could put other stuff here?
            }
            if (event != null) {
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return true;
                }
            }

            if (dead) return true;
            // CraftBukkit end

            this.die();
            this.aV();
            // CraftBukkit start - replace following line with the loop
            //this.world.addEntity(new EntityItem(this.world, this.locX, this.locY, this.locZ, new ItemStack(Item.PAINTING)));
            for (org.bukkit.inventory.ItemStack stack : drops) {
                this.world.addEntity(new EntityItem(this.world, this.locX, this.locY, this.locZ, CraftItemStack.createNMSItemStack(stack)));
            }
            // CraftBukkit end
        }

        return true;
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Dir", (byte) this.direction);
        nbttagcompound.setString("Motive", this.art.A);
        nbttagcompound.setInt("TileX", this.x);
        nbttagcompound.setInt("TileY", this.y);
        nbttagcompound.setInt("TileZ", this.z);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.direction = nbttagcompound.getByte("Dir");
        this.x = nbttagcompound.getInt("TileX");
        this.y = nbttagcompound.getInt("TileY");
        this.z = nbttagcompound.getInt("TileZ");
        String s = nbttagcompound.getString("Motive");
        EnumArt[] aenumart = EnumArt.values();
        int i = aenumart.length;

        for (int j = 0; j < i; ++j) {
            EnumArt enumart = aenumart[j];

            if (enumart.A.equals(s)) {
                this.art = enumart;
            }
        }

        if (this.art == null) {
            this.art = EnumArt.KEBAB;
        }

        this.setDirection(this.direction);
    }

    public void move(double d0, double d1, double d2) {
        if (!this.world.isStatic && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) {
            if (dead) return; // CraftBukkit

            this.die();
            this.world.addEntity(new EntityItem(this.world, this.locX, this.locY, this.locZ, new ItemStack(Item.PAINTING)));
        }
    }

    public void b_(double d0, double d1, double d2) {
        /* CraftBukkit start - not needed for paintings
        if (!this.world.isStatic && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) {
            this.die();
            this.world.addEntity(new EntityItem(this.world, this.locX, this.locY, this.locZ, new ItemStack(Item.PAINTING)));
        }
        // CraftBukkit end */
    }
}
