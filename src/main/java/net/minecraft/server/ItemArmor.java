package net.minecraft.server;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.UUID;

public class ItemArmor extends Item implements ItemWearable {

    private static final UUID[] j = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    public static final IDispenseBehavior a = new DispenseBehaviorItem() {
        @Override
        protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
            return ItemArmor.a(isourceblock, itemstack) ? itemstack : super.a(isourceblock, itemstack);
        }
    };
    protected final EnumItemSlot b;
    private final int k;
    private final float l;
    protected final float c;
    protected final ArmorMaterial d;
    private final Multimap<AttributeBase, AttributeModifier> m;

    public static boolean a(ISourceBlock isourceblock, ItemStack itemstack) {
        BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
        List<EntityLiving> list = isourceblock.getWorld().a(EntityLiving.class, new AxisAlignedBB(blockposition), IEntitySelector.g.and(new IEntitySelector.EntitySelectorEquipable(itemstack)));

        if (list.isEmpty()) {
            return false;
        } else {
            EntityLiving entityliving = (EntityLiving) list.get(0);
            EnumItemSlot enumitemslot = EntityInsentient.j(itemstack);
            ItemStack itemstack1 = itemstack.cloneAndSubtract(1);

            entityliving.setSlot(enumitemslot, itemstack1);
            if (entityliving instanceof EntityInsentient) {
                ((EntityInsentient) entityliving).a(enumitemslot, 2.0F);
                ((EntityInsentient) entityliving).setPersistent();
            }

            return true;
        }
    }

    public ItemArmor(ArmorMaterial armormaterial, EnumItemSlot enumitemslot, Item.Info item_info) {
        super(item_info.b(armormaterial.a(enumitemslot)));
        this.d = armormaterial;
        this.b = enumitemslot;
        this.k = armormaterial.b(enumitemslot);
        this.l = armormaterial.e();
        this.c = armormaterial.f();
        BlockDispenser.a((IMaterial) this, ItemArmor.a);
        Builder<AttributeBase, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ItemArmor.j[enumitemslot.b()];

        builder.put(GenericAttributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", (double) this.k, AttributeModifier.Operation.ADDITION));
        builder.put(GenericAttributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", (double) this.l, AttributeModifier.Operation.ADDITION));
        if (armormaterial == EnumArmorMaterial.NETHERITE) {
            builder.put(GenericAttributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", (double) this.c, AttributeModifier.Operation.ADDITION));
        }

        this.m = builder.build();
    }

    public EnumItemSlot b() {
        return this.b;
    }

    @Override
    public int c() {
        return this.d.a();
    }

    public ArmorMaterial ab_() {
        return this.d;
    }

    @Override
    public boolean a(ItemStack itemstack, ItemStack itemstack1) {
        return this.d.c().test(itemstack1) || super.a(itemstack, itemstack1);
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        EnumItemSlot enumitemslot = EntityInsentient.j(itemstack);
        ItemStack itemstack1 = entityhuman.getEquipment(enumitemslot);

        if (itemstack1.isEmpty()) {
            entityhuman.setSlot(enumitemslot, itemstack.cloneItemStack());
            itemstack.setCount(0);
            return InteractionResultWrapper.a(itemstack, world.s_());
        } else {
            return InteractionResultWrapper.fail(itemstack);
        }
    }

    @Override
    public Multimap<AttributeBase, AttributeModifier> a(EnumItemSlot enumitemslot) {
        return enumitemslot == this.b ? this.m : super.a(enumitemslot);
    }

    public int e() {
        return this.k;
    }

    public float f() {
        return this.l;
    }
}
