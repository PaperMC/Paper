package net.minecraft.server;

import java.util.Random;

public class ItemArmorStand extends Item {

    public ItemArmorStand(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        EnumDirection enumdirection = itemactioncontext.getClickedFace();

        if (enumdirection == EnumDirection.DOWN) {
            return EnumInteractionResult.FAIL;
        } else {
            World world = itemactioncontext.getWorld();
            BlockActionContext blockactioncontext = new BlockActionContext(itemactioncontext);
            BlockPosition blockposition = blockactioncontext.getClickPosition();
            ItemStack itemstack = itemactioncontext.getItemStack();
            Vec3D vec3d = Vec3D.c((BaseBlockPosition) blockposition);
            AxisAlignedBB axisalignedbb = EntityTypes.ARMOR_STAND.l().a(vec3d.getX(), vec3d.getY(), vec3d.getZ());

            if (world.b((Entity) null, axisalignedbb, (entity) -> {
                return true;
            }) && world.getEntities((Entity) null, axisalignedbb).isEmpty()) {
                if (world instanceof WorldServer) {
                    WorldServer worldserver = (WorldServer) world;
                    EntityArmorStand entityarmorstand = (EntityArmorStand) EntityTypes.ARMOR_STAND.createCreature(worldserver, itemstack.getTag(), (IChatBaseComponent) null, itemactioncontext.getEntity(), blockposition, EnumMobSpawn.SPAWN_EGG, true, true);

                    if (entityarmorstand == null) {
                        return EnumInteractionResult.FAIL;
                    }

                    worldserver.addAllEntities(entityarmorstand);
                    float f = (float) MathHelper.d((MathHelper.g(itemactioncontext.h() - 180.0F) + 22.5F) / 45.0F) * 45.0F;

                    entityarmorstand.setPositionRotation(entityarmorstand.locX(), entityarmorstand.locY(), entityarmorstand.locZ(), f, 0.0F);
                    this.a(entityarmorstand, world.random);
                    world.addEntity(entityarmorstand);
                    world.playSound((EntityHuman) null, entityarmorstand.locX(), entityarmorstand.locY(), entityarmorstand.locZ(), SoundEffects.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                }

                itemstack.subtract(1);
                return EnumInteractionResult.a(world.isClientSide);
            } else {
                return EnumInteractionResult.FAIL;
            }
        }
    }

    private void a(EntityArmorStand entityarmorstand, Random random) {
        Vector3f vector3f = entityarmorstand.r();
        float f = random.nextFloat() * 5.0F;
        float f1 = random.nextFloat() * 20.0F - 10.0F;
        Vector3f vector3f1 = new Vector3f(vector3f.getX() + f, vector3f.getY() + f1, vector3f.getZ());

        entityarmorstand.setHeadPose(vector3f1);
        vector3f = entityarmorstand.t();
        f = random.nextFloat() * 10.0F - 5.0F;
        vector3f1 = new Vector3f(vector3f.getX(), vector3f.getY() + f, vector3f.getZ());
        entityarmorstand.setBodyPose(vector3f1);
    }
}
