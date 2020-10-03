package net.minecraft.server;

import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;
// CraftBukkit end

public class BlockPortalShape {

    private static final BlockBase.e a = (iblockdata, iblockaccess, blockposition) -> {
        return iblockdata.a(Blocks.OBSIDIAN);
    };
    private final GeneratorAccess b;
    private final EnumDirection.EnumAxis c;
    private final EnumDirection d;
    private int e;
    @Nullable
    private BlockPosition position;
    private int height;
    private int width;
    java.util.List<org.bukkit.block.BlockState> blocks = new java.util.ArrayList<org.bukkit.block.BlockState>(); // CraftBukkit - add field

    public static Optional<BlockPortalShape> a(GeneratorAccess generatoraccess, BlockPosition blockposition, EnumDirection.EnumAxis enumdirection_enumaxis) {
        return a(generatoraccess, blockposition, (blockportalshape) -> {
            return blockportalshape.a() && blockportalshape.e == 0;
        }, enumdirection_enumaxis);
    }

    public static Optional<BlockPortalShape> a(GeneratorAccess generatoraccess, BlockPosition blockposition, Predicate<BlockPortalShape> predicate, EnumDirection.EnumAxis enumdirection_enumaxis) {
        Optional<BlockPortalShape> optional = Optional.of(new BlockPortalShape(generatoraccess, blockposition, enumdirection_enumaxis)).filter(predicate);

        if (optional.isPresent()) {
            return optional;
        } else {
            EnumDirection.EnumAxis enumdirection_enumaxis1 = enumdirection_enumaxis == EnumDirection.EnumAxis.X ? EnumDirection.EnumAxis.Z : EnumDirection.EnumAxis.X;

            return Optional.of(new BlockPortalShape(generatoraccess, blockposition, enumdirection_enumaxis1)).filter(predicate);
        }
    }

    public BlockPortalShape(GeneratorAccess generatoraccess, BlockPosition blockposition, EnumDirection.EnumAxis enumdirection_enumaxis) {
        this.b = generatoraccess;
        this.c = enumdirection_enumaxis;
        this.d = enumdirection_enumaxis == EnumDirection.EnumAxis.X ? EnumDirection.WEST : EnumDirection.SOUTH;
        this.position = this.a(blockposition);
        if (this.position == null) {
            this.position = blockposition;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.d();
            if (this.width > 0) {
                this.height = this.e();
            }
        }

    }

    @Nullable
    private BlockPosition a(BlockPosition blockposition) {
        for (int i = Math.max(0, blockposition.getY() - 21); blockposition.getY() > i && a(this.b.getType(blockposition.down())); blockposition = blockposition.down()) {
            ;
        }

        EnumDirection enumdirection = this.d.opposite();
        int j = this.a(blockposition, enumdirection) - 1;

        return j < 0 ? null : blockposition.shift(enumdirection, j);
    }

    private int d() {
        int i = this.a(this.position, this.d);

        return i >= 2 && i <= 21 ? i : 0;
    }

    private int a(BlockPosition blockposition, EnumDirection enumdirection) {
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

        for (int i = 0; i <= 21; ++i) {
            blockposition_mutableblockposition.g(blockposition).c(enumdirection, i);
            IBlockData iblockdata = this.b.getType(blockposition_mutableblockposition);

            if (!a(iblockdata)) {
                if (BlockPortalShape.a.test(iblockdata, this.b, blockposition_mutableblockposition)) {
                    blocks.add(CraftBlock.at(this.b, blockposition_mutableblockposition).getState()); // CraftBukkit
                    return i;
                }
                break;
            }

            IBlockData iblockdata1 = this.b.getType(blockposition_mutableblockposition.c(EnumDirection.DOWN));

            if (!BlockPortalShape.a.test(iblockdata1, this.b, blockposition_mutableblockposition)) {
                break;
            }
        }

        return 0;
    }

    private int e() {
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        int i = this.a(blockposition_mutableblockposition);

        return i >= 3 && i <= 21 && this.a(blockposition_mutableblockposition, i) ? i : 0;
    }

    private boolean a(BlockPosition.MutableBlockPosition blockposition_mutableblockposition, int i) {
        for (int j = 0; j < this.width; ++j) {
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition1 = blockposition_mutableblockposition.g(this.position).c(EnumDirection.UP, i).c(this.d, j);

            if (!BlockPortalShape.a.test(this.b.getType(blockposition_mutableblockposition1), this.b, blockposition_mutableblockposition1)) {
                return false;
            }
        }

        return true;
    }

    private int a(BlockPosition.MutableBlockPosition blockposition_mutableblockposition) {
        for (int i = 0; i < 21; ++i) {
            blockposition_mutableblockposition.g(this.position).c(EnumDirection.UP, i).c(this.d, -1);
            if (!BlockPortalShape.a.test(this.b.getType(blockposition_mutableblockposition), this.b, blockposition_mutableblockposition)) {
                return i;
            }

            blockposition_mutableblockposition.g(this.position).c(EnumDirection.UP, i).c(this.d, this.width);
            if (!BlockPortalShape.a.test(this.b.getType(blockposition_mutableblockposition), this.b, blockposition_mutableblockposition)) {
                return i;
            }

            for (int j = 0; j < this.width; ++j) {
                blockposition_mutableblockposition.g(this.position).c(EnumDirection.UP, i).c(this.d, j);
                IBlockData iblockdata = this.b.getType(blockposition_mutableblockposition);

                if (!a(iblockdata)) {
                    return i;
                }

                if (iblockdata.a(Blocks.NETHER_PORTAL)) {
                    ++this.e;
                }
            }
        }

        return 21;
    }

    private static boolean a(IBlockData iblockdata) {
        return iblockdata.isAir() || iblockdata.a((Tag) TagsBlock.FIRE) || iblockdata.a(Blocks.NETHER_PORTAL);
    }

    public boolean a() {
        return this.position != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    // CraftBukkit start - return boolean
    public boolean createPortal() {
        org.bukkit.World bworld = this.b.getMinecraftWorld().getWorld();

        // Copy below for loop
        IBlockData iblockdata = (IBlockData) Blocks.NETHER_PORTAL.getBlockData().set(BlockPortal.AXIS, this.c);

        BlockPosition.a(this.position, this.position.shift(EnumDirection.UP, this.height - 1).shift(this.d, this.width - 1)).forEach((blockposition) -> {
            CraftBlockState state = CraftBlockState.getBlockState(this.b.getMinecraftWorld(), blockposition, 18);
            state.setData(iblockdata);
            blocks.add(state);
        });

        PortalCreateEvent event = new PortalCreateEvent(blocks, bworld, null, PortalCreateEvent.CreateReason.FIRE);
        this.b.getMinecraftWorld().getMinecraftServer().server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }
        // CraftBukkit end
        BlockPosition.a(this.position, this.position.shift(EnumDirection.UP, this.height - 1).shift(this.d, this.width - 1)).forEach((blockposition) -> {
            this.b.setTypeAndData(blockposition, iblockdata, 18);
        });
        return true; // CraftBukkit
    }

    public boolean c() {
        return this.a() && this.e == this.width * this.height;
    }

    public static Vec3D a(BlockUtil.Rectangle blockutil_rectangle, EnumDirection.EnumAxis enumdirection_enumaxis, Vec3D vec3d, EntitySize entitysize) {
        double d0 = (double) blockutil_rectangle.side1 - (double) entitysize.width;
        double d1 = (double) blockutil_rectangle.side2 - (double) entitysize.height;
        BlockPosition blockposition = blockutil_rectangle.origin;
        double d2;

        if (d0 > 0.0D) {
            float f = (float) blockposition.a(enumdirection_enumaxis) + entitysize.width / 2.0F;

            d2 = MathHelper.a(MathHelper.c(vec3d.a(enumdirection_enumaxis) - (double) f, 0.0D, d0), 0.0D, 1.0D);
        } else {
            d2 = 0.5D;
        }

        EnumDirection.EnumAxis enumdirection_enumaxis1;
        double d3;

        if (d1 > 0.0D) {
            enumdirection_enumaxis1 = EnumDirection.EnumAxis.Y;
            d3 = MathHelper.a(MathHelper.c(vec3d.a(enumdirection_enumaxis1) - (double) blockposition.a(enumdirection_enumaxis1), 0.0D, d1), 0.0D, 1.0D);
        } else {
            d3 = 0.0D;
        }

        enumdirection_enumaxis1 = enumdirection_enumaxis == EnumDirection.EnumAxis.X ? EnumDirection.EnumAxis.Z : EnumDirection.EnumAxis.X;
        double d4 = vec3d.a(enumdirection_enumaxis1) - ((double) blockposition.a(enumdirection_enumaxis1) + 0.5D);

        return new Vec3D(d2, d3, d4);
    }

    public static ShapeDetectorShape a(WorldServer worldserver, BlockUtil.Rectangle blockutil_rectangle, EnumDirection.EnumAxis enumdirection_enumaxis, Vec3D vec3d, EntitySize entitysize, Vec3D vec3d1, float f, float f1, CraftPortalEvent portalEventInfo) { // CraftBukkit // PAIL rename toDetectorShape
        BlockPosition blockposition = blockutil_rectangle.origin;
        IBlockData iblockdata = worldserver.getType(blockposition);
        EnumDirection.EnumAxis enumdirection_enumaxis1 = (EnumDirection.EnumAxis) iblockdata.get(BlockProperties.E);
        double d0 = (double) blockutil_rectangle.side1;
        double d1 = (double) blockutil_rectangle.side2;
        int i = enumdirection_enumaxis == enumdirection_enumaxis1 ? 0 : 90;
        Vec3D vec3d2 = enumdirection_enumaxis == enumdirection_enumaxis1 ? vec3d1 : new Vec3D(vec3d1.z, vec3d1.y, -vec3d1.x);
        double d2 = (double) entitysize.width / 2.0D + (d0 - (double) entitysize.width) * vec3d.getX();
        double d3 = (d1 - (double) entitysize.height) * vec3d.getY();
        double d4 = 0.5D + vec3d.getZ();
        boolean flag = enumdirection_enumaxis1 == EnumDirection.EnumAxis.X;
        Vec3D vec3d3 = new Vec3D((double) blockposition.getX() + (flag ? d2 : d4), (double) blockposition.getY() + d3, (double) blockposition.getZ() + (flag ? d4 : d2));

        return new ShapeDetectorShape(vec3d3, vec3d2, f + (float) i, f1, worldserver, portalEventInfo); // CraftBukkit
    }
}
