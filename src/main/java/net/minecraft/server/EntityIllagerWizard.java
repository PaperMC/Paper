package net.minecraft.server;

import java.util.EnumSet;
import javax.annotation.Nullable;

public abstract class EntityIllagerWizard extends EntityIllagerAbstract {

    private static final DataWatcherObject<Byte> bo = DataWatcher.a(EntityIllagerWizard.class, DataWatcherRegistry.a);
    protected int b;
    private EntityIllagerWizard.Spell bp;

    protected EntityIllagerWizard(EntityTypes<? extends EntityIllagerWizard> entitytypes, World world) {
        super(entitytypes, world);
        this.bp = EntityIllagerWizard.Spell.NONE;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityIllagerWizard.bo, (byte) 0);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.b = nbttagcompound.getInt("SpellTicks");
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("SpellTicks", this.b);
    }

    public boolean eW() {
        return this.world.isClientSide ? (Byte) this.datawatcher.get(EntityIllagerWizard.bo) > 0 : this.b > 0;
    }

    public void setSpell(EntityIllagerWizard.Spell entityillagerwizard_spell) {
        this.bp = entityillagerwizard_spell;
        this.datawatcher.set(EntityIllagerWizard.bo, (byte) entityillagerwizard_spell.g);
    }

    public EntityIllagerWizard.Spell getSpell() {
        return !this.world.isClientSide ? this.bp : EntityIllagerWizard.Spell.a((Byte) this.datawatcher.get(EntityIllagerWizard.bo));
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (this.b > 0) {
            --this.b;
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClientSide && this.eW()) {
            EntityIllagerWizard.Spell entityillagerwizard_spell = this.getSpell();
            double d0 = entityillagerwizard_spell.h[0];
            double d1 = entityillagerwizard_spell.h[1];
            double d2 = entityillagerwizard_spell.h[2];
            float f = this.aA * 0.017453292F + MathHelper.cos((float) this.ticksLived * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);

            this.world.addParticle(Particles.ENTITY_EFFECT, this.locX() + (double) f1 * 0.6D, this.locY() + 1.8D, this.locZ() + (double) f2 * 0.6D, d0, d1, d2);
            this.world.addParticle(Particles.ENTITY_EFFECT, this.locX() - (double) f1 * 0.6D, this.locY() + 1.8D, this.locZ() - (double) f2 * 0.6D, d0, d1, d2);
        }

    }

    protected int eY() {
        return this.b;
    }

    protected abstract SoundEffect getSoundCastSpell();

    public static enum Spell {

        NONE(0, 0.0D, 0.0D, 0.0D), SUMMON_VEX(1, 0.7D, 0.7D, 0.8D), FANGS(2, 0.4D, 0.3D, 0.35D), WOLOLO(3, 0.7D, 0.5D, 0.2D), DISAPPEAR(4, 0.3D, 0.3D, 0.8D), BLINDNESS(5, 0.1D, 0.1D, 0.2D);

        private final int g;
        private final double[] h;

        private Spell(int i, double d0, double d1, double d2) {
            this.g = i;
            this.h = new double[]{d0, d1, d2};
        }

        public static EntityIllagerWizard.Spell a(int i) {
            EntityIllagerWizard.Spell[] aentityillagerwizard_spell = values();
            int j = aentityillagerwizard_spell.length;

            for (int k = 0; k < j; ++k) {
                EntityIllagerWizard.Spell entityillagerwizard_spell = aentityillagerwizard_spell[k];

                if (i == entityillagerwizard_spell.g) {
                    return entityillagerwizard_spell;
                }
            }

            return EntityIllagerWizard.Spell.NONE;
        }
    }

    public abstract class PathfinderGoalCastSpell extends PathfinderGoal {

        protected int b;
        protected int c;

        protected PathfinderGoalCastSpell() {}

        @Override
        public boolean a() {
            EntityLiving entityliving = EntityIllagerWizard.this.getGoalTarget();

            return entityliving != null && entityliving.isAlive() ? (EntityIllagerWizard.this.eW() ? false : EntityIllagerWizard.this.ticksLived >= this.c) : false;
        }

        @Override
        public boolean b() {
            EntityLiving entityliving = EntityIllagerWizard.this.getGoalTarget();

            return entityliving != null && entityliving.isAlive() && this.b > 0;
        }

        @Override
        public void c() {
            this.b = this.m();
            EntityIllagerWizard.this.b = this.g();
            this.c = EntityIllagerWizard.this.ticksLived + this.h();
            SoundEffect soundeffect = this.k();

            if (soundeffect != null) {
                EntityIllagerWizard.this.playSound(soundeffect, 1.0F, 1.0F);
            }

            EntityIllagerWizard.this.setSpell(this.getCastSpell());
        }

        @Override
        public void e() {
            --this.b;
            if (this.b == 0) {
                // CraftBukkit start
                if (!org.bukkit.craftbukkit.event.CraftEventFactory.handleEntitySpellCastEvent(EntityIllagerWizard.this, this.getCastSpell())) {
                    return;
                }
                // CraftBukkit end
                this.j();
                EntityIllagerWizard.this.playSound(EntityIllagerWizard.this.getSoundCastSpell(), 1.0F, 1.0F);
            }

        }

        protected abstract void j();

        protected int m() {
            return 20;
        }

        protected abstract int g();

        protected abstract int h();

        @Nullable
        protected abstract SoundEffect k();

        protected abstract EntityIllagerWizard.Spell getCastSpell();
    }

    public class b extends PathfinderGoal {

        public b() {
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        }

        @Override
        public boolean a() {
            return EntityIllagerWizard.this.eY() > 0;
        }

        @Override
        public void c() {
            super.c();
            EntityIllagerWizard.this.navigation.o();
        }

        @Override
        public void d() {
            super.d();
            EntityIllagerWizard.this.setSpell(EntityIllagerWizard.Spell.NONE);
        }

        @Override
        public void e() {
            if (EntityIllagerWizard.this.getGoalTarget() != null) {
                EntityIllagerWizard.this.getControllerLook().a(EntityIllagerWizard.this.getGoalTarget(), (float) EntityIllagerWizard.this.eo(), (float) EntityIllagerWizard.this.O());
            }

        }
    }
}
