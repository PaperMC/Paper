package net.minecraft.server;

// CraftBukkit - package-private import
class GroupDataZombie implements GroupDataEntity {

    public boolean a;
    public boolean b;
    final EntityZombie c;

    private GroupDataZombie(EntityZombie entityzombie, boolean flag, boolean flag1) {
        this.c = entityzombie;
        this.a = false;
        this.b = false;
        this.a = flag;
        this.b = flag1;
    }

    GroupDataZombie(EntityZombie entityzombie, boolean flag, boolean flag1, EmptyClassZombie emptyclasszombie) {
        this(entityzombie, flag, flag1);
    }
}
