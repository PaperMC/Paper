package net.minecraft.server;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public class TileEntityBanner extends TileEntity implements INamableTileEntity {

    @Nullable
    private IChatBaseComponent a;
    @Nullable
    public EnumColor color;
    @Nullable
    public NBTTagList patterns;
    private boolean g;
    @Nullable
    private List<Pair<EnumBannerPatternType, EnumColor>> h;

    public TileEntityBanner() {
        super(TileEntityTypes.BANNER);
        this.color = EnumColor.WHITE;
    }

    public TileEntityBanner(EnumColor enumcolor) {
        this();
        this.color = enumcolor;
    }

    @Override
    public IChatBaseComponent getDisplayName() {
        return (IChatBaseComponent) (this.a != null ? this.a : new ChatMessage("block.minecraft.banner"));
    }

    @Nullable
    @Override
    public IChatBaseComponent getCustomName() {
        return this.a;
    }

    public void a(IChatBaseComponent ichatbasecomponent) {
        this.a = ichatbasecomponent;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        if (this.patterns != null) {
            nbttagcompound.set("Patterns", this.patterns);
        }

        if (this.a != null) {
            nbttagcompound.setString("CustomName", IChatBaseComponent.ChatSerializer.a(this.a));
        }

        return nbttagcompound;
    }

    @Override
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        super.load(iblockdata, nbttagcompound);
        if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
            this.a = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("CustomName"));
        }

        if (this.hasWorld()) {
            this.color = ((BlockBannerAbstract) this.getBlock().getBlock()).getColor();
        } else {
            this.color = null;
        }

        this.patterns = nbttagcompound.getList("Patterns", 10);
        this.h = null;
        this.g = true;
    }

    @Nullable
    @Override
    public PacketPlayOutTileEntityData getUpdatePacket() {
        return new PacketPlayOutTileEntityData(this.position, 6, this.b());
    }

    @Override
    public NBTTagCompound b() {
        return this.save(new NBTTagCompound());
    }

    public static int b(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.b("BlockEntityTag");

        return nbttagcompound != null && nbttagcompound.hasKey("Patterns") ? nbttagcompound.getList("Patterns", 10).size() : 0;
    }

    public static void c(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.b("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.hasKeyOfType("Patterns", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getList("Patterns", 10);

            if (!nbttaglist.isEmpty()) {
                nbttaglist.remove(nbttaglist.size() - 1);
                if (nbttaglist.isEmpty()) {
                    itemstack.removeTag("BlockEntityTag");
                }

            }
        }
    }

    public EnumColor a(Supplier<IBlockData> supplier) {
        if (this.color == null) {
            this.color = ((BlockBannerAbstract) ((IBlockData) supplier.get()).getBlock()).getColor();
        }

        return this.color;
    }
}
