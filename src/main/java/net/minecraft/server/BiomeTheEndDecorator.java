package net.minecraft.server;

public class BiomeTheEndDecorator extends BiomeDecorator {

    protected WorldGenerator J;

    public BiomeTheEndDecorator() {
        this.J = new WorldGenEnder(Blocks.WHITESTONE);
    }

    protected void a(BiomeBase biomebase) {
        this.a();
        if (this.b.nextInt(5) == 0) {
            int i = this.c + this.b.nextInt(16) + 8;
            int j = this.d + this.b.nextInt(16) + 8;
            int k = this.a.i(i, j);

            this.J.generate(this.a, this.b, i, k, j);
        }

        if (this.c == 0 && this.d == 0) {
            EntityEnderDragon entityenderdragon = new EntityEnderDragon(this.a);

            entityenderdragon.setPositionRotation(0.0D, 128.0D, 0.0D, this.b.nextFloat() * 360.0F, 0.0F);
            this.a.addEntity(entityenderdragon, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CHUNK_GEN); // CraftBukkit - add SpawnReason
        }
    }
}
