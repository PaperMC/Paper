package net.minecraft.server;

public class LootContextParameters {

    public static final LootContextParameter<Entity> THIS_ENTITY = a("this_entity");
    public static final LootContextParameter<EntityHuman> LAST_DAMAGE_PLAYER = a("last_damage_player");
    public static final LootContextParameter<DamageSource> DAMAGE_SOURCE = a("damage_source");
    public static final LootContextParameter<Entity> KILLER_ENTITY = a("killer_entity");
    public static final LootContextParameter<Entity> DIRECT_KILLER_ENTITY = a("direct_killer_entity");
    public static final LootContextParameter<Vec3D> ORIGIN = a("origin");
    public static final LootContextParameter<IBlockData> BLOCK_STATE = a("block_state");
    public static final LootContextParameter<TileEntity> BLOCK_ENTITY = a("block_entity");
    public static final LootContextParameter<ItemStack> TOOL = a("tool");
    public static final LootContextParameter<Float> EXPLOSION_RADIUS = a("explosion_radius");
    public static final LootContextParameter<Integer> LOOTING_MOD = new LootContextParameter<>(new MinecraftKey("bukkit:looting_mod")); // CraftBukkit

    private static <T> LootContextParameter<T> a(String s) {
        return new LootContextParameter<>(new MinecraftKey(s));
    }
}
