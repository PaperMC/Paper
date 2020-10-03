package net.minecraft.server;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class ItemSkullPlayer extends ItemBlockWallable {

    public ItemSkullPlayer(Block block, Block block1, Item.Info item_info) {
        super(block, block1, item_info);
    }

    @Override
    public IChatBaseComponent h(ItemStack itemstack) {
        if (itemstack.getItem() == Items.PLAYER_HEAD && itemstack.hasTag()) {
            String s = null;
            NBTTagCompound nbttagcompound = itemstack.getTag();

            if (nbttagcompound.hasKeyOfType("SkullOwner", 8)) {
                s = nbttagcompound.getString("SkullOwner");
            } else if (nbttagcompound.hasKeyOfType("SkullOwner", 10)) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("SkullOwner");

                if (nbttagcompound1.hasKeyOfType("Name", 8)) {
                    s = nbttagcompound1.getString("Name");
                }
            }

            if (s != null) {
                return new ChatMessage(this.getName() + ".named", new Object[]{s});
            }
        }

        return super.h(itemstack);
    }

    @Override
    public boolean b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.getString("SkullOwner"))) {
            GameProfile gameprofile = new GameProfile((UUID) null, nbttagcompound.getString("SkullOwner"));

            gameprofile = TileEntitySkull.b(gameprofile);
            nbttagcompound.set("SkullOwner", GameProfileSerializer.serialize(new NBTTagCompound(), gameprofile));
            return true;
        } else {
            // CraftBukkit start
            NBTTagList textures = nbttagcompound.getCompound("SkullOwner").getCompound("Properties").getList("textures", 10); // Safe due to method contracts
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i) instanceof NBTTagCompound && !((NBTTagCompound) textures.get(i)).hasKeyOfType("Signature", 8) && ((NBTTagCompound) textures.get(i)).getString("Value").trim().isEmpty()) {
                    nbttagcompound.remove("SkullOwner");
                    break;
                }
            }
            // CraftBukkit end
            return false;
        }
    }
}
