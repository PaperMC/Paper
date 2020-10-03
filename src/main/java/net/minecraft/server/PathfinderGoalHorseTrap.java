package net.minecraft.server;

public class PathfinderGoalHorseTrap extends PathfinderGoal {

    private final EntityHorseSkeleton a;

    public PathfinderGoalHorseTrap(EntityHorseSkeleton entityhorseskeleton) {
        this.a = entityhorseskeleton;
    }

    @Override
    public boolean a() {
        return this.a.world.isPlayerNearby(this.a.locX(), this.a.locY(), this.a.locZ(), 10.0D);
    }

    @Override
    public void e() {
        WorldServer worldserver = (WorldServer) this.a.world;
        DifficultyDamageScaler difficultydamagescaler = worldserver.getDamageScaler(this.a.getChunkCoordinates());

        this.a.t(false);
        this.a.setTamed(true);
        this.a.setAgeRaw(0);
        EntityLightning entitylightning = (EntityLightning) EntityTypes.LIGHTNING_BOLT.a((World) worldserver);

        entitylightning.teleportAndSync(this.a.locX(), this.a.locY(), this.a.locZ());
        entitylightning.setEffect(true);
        worldserver.strikeLightning(entitylightning, org.bukkit.event.weather.LightningStrikeEvent.Cause.TRAP); // CraftBukkit
        EntitySkeleton entityskeleton = this.a(difficultydamagescaler, this.a);

        if (entityskeleton != null) entityskeleton.startRiding(this.a); // CraftBukkit
        worldserver.addAllEntities(entityskeleton, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.TRAP); // CraftBukkit

        for (int i = 0; i < 3; ++i) {
            EntityHorseAbstract entityhorseabstract = this.a(difficultydamagescaler);
            if (entityhorseabstract == null) continue; // CraftBukkit
            EntitySkeleton entityskeleton1 = this.a(difficultydamagescaler, entityhorseabstract);

            if (entityskeleton1 != null) entityskeleton1.startRiding(entityhorseabstract); // CraftBukkit
            entityhorseabstract.i(this.a.getRandom().nextGaussian() * 0.5D, 0.0D, this.a.getRandom().nextGaussian() * 0.5D);
            worldserver.addAllEntities(entityhorseabstract, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.JOCKEY); // CraftBukkit
        }

    }

    private EntityHorseAbstract a(DifficultyDamageScaler difficultydamagescaler) {
        EntityHorseSkeleton entityhorseskeleton = (EntityHorseSkeleton) EntityTypes.SKELETON_HORSE.a(this.a.world);

        entityhorseskeleton.prepare((WorldServer) this.a.world, difficultydamagescaler, EnumMobSpawn.TRIGGERED, (GroupDataEntity) null, (NBTTagCompound) null);
        entityhorseskeleton.setPosition(this.a.locX(), this.a.locY(), this.a.locZ());
        entityhorseskeleton.noDamageTicks = 60;
        entityhorseskeleton.setPersistent();
        entityhorseskeleton.setTamed(true);
        entityhorseskeleton.setAgeRaw(0);
        return entityhorseskeleton;
    }

    private EntitySkeleton a(DifficultyDamageScaler difficultydamagescaler, EntityHorseAbstract entityhorseabstract) {
        EntitySkeleton entityskeleton = (EntitySkeleton) EntityTypes.SKELETON.a(entityhorseabstract.world);

        entityskeleton.prepare((WorldServer) entityhorseabstract.world, difficultydamagescaler, EnumMobSpawn.TRIGGERED, (GroupDataEntity) null, (NBTTagCompound) null);
        entityskeleton.setPosition(entityhorseabstract.locX(), entityhorseabstract.locY(), entityhorseabstract.locZ());
        entityskeleton.noDamageTicks = 60;
        entityskeleton.setPersistent();
        if (entityskeleton.getEquipment(EnumItemSlot.HEAD).isEmpty()) {
            entityskeleton.setSlot(EnumItemSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        }

        entityskeleton.setSlot(EnumItemSlot.MAINHAND, EnchantmentManager.a(entityskeleton.getRandom(), this.a(entityskeleton.getItemInMainHand()), (int) (5.0F + difficultydamagescaler.d() * (float) entityskeleton.getRandom().nextInt(18)), false));
        entityskeleton.setSlot(EnumItemSlot.HEAD, EnchantmentManager.a(entityskeleton.getRandom(), this.a(entityskeleton.getEquipment(EnumItemSlot.HEAD)), (int) (5.0F + difficultydamagescaler.d() * (float) entityskeleton.getRandom().nextInt(18)), false));
        return entityskeleton;
    }

    private ItemStack a(ItemStack itemstack) {
        itemstack.removeTag("Enchantments");
        return itemstack;
    }
}
