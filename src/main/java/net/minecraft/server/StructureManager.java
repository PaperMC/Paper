package net.minecraft.server;

import com.mojang.datafixers.DataFixUtils;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class StructureManager {

    private final GeneratorAccess a;
    private final GeneratorSettings b;

    public StructureManager(GeneratorAccess generatoraccess, GeneratorSettings generatorsettings) {
        this.a = generatoraccess;
        this.b = generatorsettings;
    }

    public StructureManager a(RegionLimitedWorldAccess regionlimitedworldaccess) {
        if (regionlimitedworldaccess.getMinecraftWorld() != this.a) {
            throw new IllegalStateException("Using invalid feature manager (source level: " + regionlimitedworldaccess.getMinecraftWorld() + ", region: " + regionlimitedworldaccess);
        } else {
            return new StructureManager(regionlimitedworldaccess, this.b);
        }
    }

    public Stream<? extends StructureStart<?>> a(SectionPosition sectionposition, StructureGenerator<?> structuregenerator) {
        return this.a.getChunkAt(sectionposition.a(), sectionposition.c(), ChunkStatus.STRUCTURE_REFERENCES).b(structuregenerator).stream().map((olong) -> {
            return SectionPosition.a(new ChunkCoordIntPair(olong), 0);
        }).map((sectionposition1) -> {
            return this.a(sectionposition1, structuregenerator, this.a.getChunkAt(sectionposition1.a(), sectionposition1.c(), ChunkStatus.STRUCTURE_STARTS));
        }).filter((structurestart) -> {
            return structurestart != null && structurestart.e();
        });
    }

    @Nullable
    public StructureStart<?> a(SectionPosition sectionposition, StructureGenerator<?> structuregenerator, IStructureAccess istructureaccess) {
        return istructureaccess.a(structuregenerator);
    }

    public void a(SectionPosition sectionposition, StructureGenerator<?> structuregenerator, StructureStart<?> structurestart, IStructureAccess istructureaccess) {
        istructureaccess.a(structuregenerator, structurestart);
    }

    public void a(SectionPosition sectionposition, StructureGenerator<?> structuregenerator, long i, IStructureAccess istructureaccess) {
        istructureaccess.a(structuregenerator, i);
    }

    public boolean a() {
        return this.b.shouldGenerateMapFeatures();
    }

    public StructureStart<?> a(BlockPosition blockposition, boolean flag, StructureGenerator<?> structuregenerator) {
        return (StructureStart) DataFixUtils.orElse(this.a(SectionPosition.a(blockposition), structuregenerator).filter((structurestart) -> {
            return structurestart.c().b((BaseBlockPosition) blockposition);
        }).filter((structurestart) -> {
            return !flag || structurestart.d().stream().anyMatch((structurepiece) -> {
                return structurepiece.g().b((BaseBlockPosition) blockposition);
            });
        }).findFirst(), StructureStart.a);
    }
}
