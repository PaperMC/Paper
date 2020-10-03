package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class EntityPainting extends EntityHanging {

    public Paintings art;

    public EntityPainting(EntityTypes<? extends EntityPainting> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntityPainting(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        super(EntityTypes.PAINTING, world, blockposition);
        List<Paintings> list = Lists.newArrayList();
        int i = 0;
        Iterator iterator = IRegistry.MOTIVE.iterator();

        Paintings paintings;

        while (iterator.hasNext()) {
            paintings = (Paintings) iterator.next();
            this.art = paintings;
            this.setDirection(enumdirection);
            if (this.survives()) {
                list.add(paintings);
                int j = paintings.getWidth() * paintings.getHeight();

                if (j > i) {
                    i = j;
                }
            }
        }

        if (!list.isEmpty()) {
            iterator = list.iterator();

            while (iterator.hasNext()) {
                paintings = (Paintings) iterator.next();
                if (paintings.getWidth() * paintings.getHeight() < i) {
                    iterator.remove();
                }
            }

            this.art = (Paintings) list.get(this.random.nextInt(list.size()));
        }

        this.setDirection(enumdirection);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Motive", IRegistry.MOTIVE.getKey(this.art).toString());
        nbttagcompound.setByte("Facing", (byte) this.direction.get2DRotationValue());
        super.saveData(nbttagcompound);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        this.art = (Paintings) IRegistry.MOTIVE.get(MinecraftKey.a(nbttagcompound.getString("Motive")));
        this.direction = EnumDirection.fromType2(nbttagcompound.getByte("Facing"));
        super.loadData(nbttagcompound);
        this.setDirection(this.direction);
    }

    @Override
    public int getHangingWidth() {
        return this.art == null ? 1 : this.art.getWidth();
    }

    @Override
    public int getHangingHeight() {
        return this.art == null ? 1 : this.art.getHeight();
    }

    @Override
    public void a(@Nullable Entity entity) {
        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            this.playSound(SoundEffects.ENTITY_PAINTING_BREAK, 1.0F, 1.0F);
            if (entity instanceof EntityHuman) {
                EntityHuman entityhuman = (EntityHuman) entity;

                if (entityhuman.abilities.canInstantlyBuild) {
                    return;
                }
            }

            this.a((IMaterial) Items.PAINTING);
        }
    }

    @Override
    public void playPlaceSound() {
        this.playSound(SoundEffects.ENTITY_PAINTING_PLACE, 1.0F, 1.0F);
    }

    @Override
    public void setPositionRotation(double d0, double d1, double d2, float f, float f1) {
        this.setPosition(d0, d1, d2);
    }

    @Override
    public Packet<?> P() {
        return new PacketPlayOutSpawnEntityPainting(this);
    }
}
