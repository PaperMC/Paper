package net.minecraft.server;

import java.util.Arrays;
import java.util.Comparator;

public class ItemFireworks extends Item {

    public ItemFireworks(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        World world = itemactioncontext.getWorld();

        if (!world.isClientSide) {
            ItemStack itemstack = itemactioncontext.getItemStack();
            Vec3D vec3d = itemactioncontext.getPos();
            EnumDirection enumdirection = itemactioncontext.getClickedFace();
            EntityFireworks entityfireworks = new EntityFireworks(world, itemactioncontext.getEntity(), vec3d.x + (double) enumdirection.getAdjacentX() * 0.15D, vec3d.y + (double) enumdirection.getAdjacentY() * 0.15D, vec3d.z + (double) enumdirection.getAdjacentZ() * 0.15D, itemstack);

            world.addEntity(entityfireworks);
            itemstack.subtract(1);
        }

        return EnumInteractionResult.a(world.isClientSide);
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        if (entityhuman.isGliding()) {
            ItemStack itemstack = entityhuman.b(enumhand);

            if (!world.isClientSide) {
                world.addEntity(new EntityFireworks(world, itemstack, entityhuman));
                if (!entityhuman.abilities.canInstantlyBuild) {
                    itemstack.subtract(1);
                }
            }

            return InteractionResultWrapper.a(entityhuman.b(enumhand), world.s_());
        } else {
            return InteractionResultWrapper.pass(entityhuman.b(enumhand));
        }
    }

    public static enum EffectType {

        SMALL_BALL(0, "small_ball"), LARGE_BALL(1, "large_ball"), STAR(2, "star"), CREEPER(3, "creeper"), BURST(4, "burst");

        private static final ItemFireworks.EffectType[] f = (ItemFireworks.EffectType[]) Arrays.stream(values()).sorted(Comparator.comparingInt((itemfireworks_effecttype) -> {
            return itemfireworks_effecttype.g;
        })).toArray((i) -> {
            return new ItemFireworks.EffectType[i];
        });
        private final int g;
        private final String h;

        private EffectType(int i, String s) {
            this.g = i;
            this.h = s;
        }

        public int a() {
            return this.g;
        }
    }
}
