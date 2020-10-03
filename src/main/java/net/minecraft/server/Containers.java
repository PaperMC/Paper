package net.minecraft.server;

public class Containers<T extends Container> {

    public static final Containers<ContainerChest> GENERIC_9X1 = a("generic_9x1", ContainerChest::a);
    public static final Containers<ContainerChest> GENERIC_9X2 = a("generic_9x2", ContainerChest::b);
    public static final Containers<ContainerChest> GENERIC_9X3 = a("generic_9x3", ContainerChest::c);
    public static final Containers<ContainerChest> GENERIC_9X4 = a("generic_9x4", ContainerChest::d);
    public static final Containers<ContainerChest> GENERIC_9X5 = a("generic_9x5", ContainerChest::e);
    public static final Containers<ContainerChest> GENERIC_9X6 = a("generic_9x6", ContainerChest::f);
    public static final Containers<ContainerDispenser> GENERIC_3X3 = a("generic_3x3", ContainerDispenser::new);
    public static final Containers<ContainerAnvil> ANVIL = a("anvil", ContainerAnvil::new);
    public static final Containers<ContainerBeacon> BEACON = a("beacon", ContainerBeacon::new);
    public static final Containers<ContainerBlastFurnace> BLAST_FURNACE = a("blast_furnace", ContainerBlastFurnace::new);
    public static final Containers<ContainerBrewingStand> BREWING_STAND = a("brewing_stand", ContainerBrewingStand::new);
    public static final Containers<ContainerWorkbench> CRAFTING = a("crafting", ContainerWorkbench::new);
    public static final Containers<ContainerEnchantTable> ENCHANTMENT = a("enchantment", ContainerEnchantTable::new);
    public static final Containers<ContainerFurnaceFurnace> FURNACE = a("furnace", ContainerFurnaceFurnace::new);
    public static final Containers<ContainerGrindstone> GRINDSTONE = a("grindstone", ContainerGrindstone::new);
    public static final Containers<ContainerHopper> HOPPER = a("hopper", ContainerHopper::new);
    public static final Containers<ContainerLectern> LECTERN = a("lectern", (i, playerinventory) -> {
        return new ContainerLectern(i, playerinventory); // CraftBukkit
    });
    public static final Containers<ContainerLoom> LOOM = a("loom", ContainerLoom::new);
    public static final Containers<ContainerMerchant> MERCHANT = a("merchant", ContainerMerchant::new);
    public static final Containers<ContainerShulkerBox> SHULKER_BOX = a("shulker_box", ContainerShulkerBox::new);
    public static final Containers<ContainerSmithing> SMITHING = a("smithing", ContainerSmithing::new);
    public static final Containers<ContainerSmoker> SMOKER = a("smoker", ContainerSmoker::new);
    public static final Containers<ContainerCartography> CARTOGRAPHY_TABLE = a("cartography_table", ContainerCartography::new);
    public static final Containers<ContainerStonecutter> STONECUTTER = a("stonecutter", ContainerStonecutter::new);
    private final Containers.Supplier<T> y;

    private static <T extends Container> Containers<T> a(String s, Containers.Supplier<T> containers_supplier) {
        return (Containers) IRegistry.a(IRegistry.MENU, s, (new Containers<>(containers_supplier))); // CraftBukkit - decompile error
    }

    private Containers(Containers.Supplier<T> containers_supplier) {
        this.y = containers_supplier;
    }

    // CraftBukkit start
    interface Supplier<T extends Container> {

        T supply(int id, PlayerInventory playerinventory);
    }
    // CraftBukkit end
}
