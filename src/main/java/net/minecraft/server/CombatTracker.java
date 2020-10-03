package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

public class CombatTracker {

    private final List<CombatEntry> a = Lists.newArrayList();
    private final EntityLiving b;
    private int c;
    private int d;
    private int e;
    private boolean f;
    private boolean g;
    private String h;

    public CombatTracker(EntityLiving entityliving) {
        this.b = entityliving;
    }

    public void a() {
        this.k();
        Optional<BlockPosition> optional = this.b.dq();

        if (optional.isPresent()) {
            IBlockData iblockdata = this.b.world.getType((BlockPosition) optional.get());

            if (!iblockdata.a(Blocks.LADDER) && !iblockdata.a((Tag) TagsBlock.TRAPDOORS)) {
                if (iblockdata.a(Blocks.VINE)) {
                    this.h = "vines";
                } else if (!iblockdata.a(Blocks.WEEPING_VINES) && !iblockdata.a(Blocks.WEEPING_VINES_PLANT)) {
                    if (!iblockdata.a(Blocks.TWISTING_VINES) && !iblockdata.a(Blocks.TWISTING_VINES_PLANT)) {
                        if (iblockdata.a(Blocks.SCAFFOLDING)) {
                            this.h = "scaffolding";
                        } else {
                            this.h = "other_climbable";
                        }
                    } else {
                        this.h = "twisting_vines";
                    }
                } else {
                    this.h = "weeping_vines";
                }
            } else {
                this.h = "ladder";
            }
        } else if (this.b.isInWater()) {
            this.h = "water";
        }

    }

    public void trackDamage(DamageSource damagesource, float f, float f1) {
        this.g();
        this.a();
        CombatEntry combatentry = new CombatEntry(damagesource, this.b.ticksLived, f, f1, this.h, this.b.fallDistance);

        this.a.add(combatentry);
        this.c = this.b.ticksLived;
        this.g = true;
        if (combatentry.f() && !this.f && this.b.isAlive()) {
            this.f = true;
            this.d = this.b.ticksLived;
            this.e = this.d;
            this.b.enterCombat();
        }

    }

    public IChatBaseComponent getDeathMessage() {
        if (this.a.isEmpty()) {
            return new ChatMessage("death.attack.generic", new Object[]{this.b.getScoreboardDisplayName()});
        } else {
            CombatEntry combatentry = this.j();
            CombatEntry combatentry1 = (CombatEntry) this.a.get(this.a.size() - 1);
            IChatBaseComponent ichatbasecomponent = combatentry1.h();
            Entity entity = combatentry1.a().getEntity();
            Object object;

            if (combatentry != null && combatentry1.a() == DamageSource.FALL) {
                IChatBaseComponent ichatbasecomponent1 = combatentry.h();

                if (combatentry.a() != DamageSource.FALL && combatentry.a() != DamageSource.OUT_OF_WORLD) {
                    if (ichatbasecomponent1 != null && (ichatbasecomponent == null || !ichatbasecomponent1.equals(ichatbasecomponent))) {
                        Entity entity1 = combatentry.a().getEntity();
                        ItemStack itemstack = entity1 instanceof EntityLiving ? ((EntityLiving) entity1).getItemInMainHand() : ItemStack.b;

                        if (!itemstack.isEmpty() && itemstack.hasName()) {
                            object = new ChatMessage("death.fell.assist.item", new Object[]{this.b.getScoreboardDisplayName(), ichatbasecomponent1, itemstack.C()});
                        } else {
                            object = new ChatMessage("death.fell.assist", new Object[]{this.b.getScoreboardDisplayName(), ichatbasecomponent1});
                        }
                    } else if (ichatbasecomponent != null) {
                        ItemStack itemstack1 = entity instanceof EntityLiving ? ((EntityLiving) entity).getItemInMainHand() : ItemStack.b;

                        if (!itemstack1.isEmpty() && itemstack1.hasName()) {
                            object = new ChatMessage("death.fell.finish.item", new Object[]{this.b.getScoreboardDisplayName(), ichatbasecomponent, itemstack1.C()});
                        } else {
                            object = new ChatMessage("death.fell.finish", new Object[]{this.b.getScoreboardDisplayName(), ichatbasecomponent});
                        }
                    } else {
                        object = new ChatMessage("death.fell.killer", new Object[]{this.b.getScoreboardDisplayName()});
                    }
                } else {
                    object = new ChatMessage("death.fell.accident." + this.a(combatentry), new Object[]{this.b.getScoreboardDisplayName()});
                }
            } else {
                object = combatentry1.a().getLocalizedDeathMessage(this.b);
            }

            return (IChatBaseComponent) object;
        }
    }

    @Nullable
    public EntityLiving c() {
        EntityLiving entityliving = null;
        EntityHuman entityhuman = null;
        float f = 0.0F;
        float f1 = 0.0F;
        Iterator iterator = this.a.iterator();

        while (iterator.hasNext()) {
            CombatEntry combatentry = (CombatEntry) iterator.next();

            if (combatentry.a().getEntity() instanceof EntityHuman && (entityhuman == null || combatentry.c() > f1)) {
                f1 = combatentry.c();
                entityhuman = (EntityHuman) combatentry.a().getEntity();
            }

            if (combatentry.a().getEntity() instanceof EntityLiving && (entityliving == null || combatentry.c() > f)) {
                f = combatentry.c();
                entityliving = (EntityLiving) combatentry.a().getEntity();
            }
        }

        if (entityhuman != null && f1 >= f / 3.0F) {
            return entityhuman;
        } else {
            return entityliving;
        }
    }

    @Nullable
    private CombatEntry j() {
        CombatEntry combatentry = null;
        CombatEntry combatentry1 = null;
        float f = 0.0F;
        float f1 = 0.0F;

        for (int i = 0; i < this.a.size(); ++i) {
            CombatEntry combatentry2 = (CombatEntry) this.a.get(i);
            CombatEntry combatentry3 = i > 0 ? (CombatEntry) this.a.get(i - 1) : null;

            if ((combatentry2.a() == DamageSource.FALL || combatentry2.a() == DamageSource.OUT_OF_WORLD) && combatentry2.j() > 0.0F && (combatentry == null || combatentry2.j() > f1)) {
                if (i > 0) {
                    combatentry = combatentry3;
                } else {
                    combatentry = combatentry2;
                }

                f1 = combatentry2.j();
            }

            if (combatentry2.g() != null && (combatentry1 == null || combatentry2.c() > f)) {
                combatentry1 = combatentry2;
                f = combatentry2.c();
            }
        }

        if (f1 > 5.0F && combatentry != null) {
            return combatentry;
        } else if (f > 5.0F && combatentry1 != null) {
            return combatentry1;
        } else {
            return null;
        }
    }

    private String a(CombatEntry combatentry) {
        return combatentry.g() == null ? "generic" : combatentry.g();
    }

    public int f() {
        return this.f ? this.b.ticksLived - this.d : this.e - this.d;
    }

    private void k() {
        this.h = null;
    }

    public void g() {
        int i = this.f ? 300 : 100;

        if (this.g && (!this.b.isAlive() || this.b.ticksLived - this.c > i)) {
            boolean flag = this.f;

            this.g = false;
            this.f = false;
            this.e = this.b.ticksLived;
            if (flag) {
                this.b.exitCombat();
            }

            this.a.clear();
        }

    }

    public EntityLiving h() {
        return this.b;
    }
}
