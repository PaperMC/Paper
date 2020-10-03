package net.minecraft.server;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import javax.annotation.Nullable;

public class TileEntitySign extends TileEntity {

    public final IChatBaseComponent[] lines;
    public boolean isEditable;
    private EntityHuman c;
    private final FormattedString[] g;
    private EnumColor color;

    public TileEntitySign() {
        super(TileEntityTypes.SIGN);
        this.lines = new IChatBaseComponent[]{ChatComponentText.d, ChatComponentText.d, ChatComponentText.d, ChatComponentText.d};
        this.isEditable = true;
        this.g = new FormattedString[4];
        this.color = EnumColor.BLACK;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);

        for (int i = 0; i < 4; ++i) {
            String s = IChatBaseComponent.ChatSerializer.a(this.lines[i]);

            nbttagcompound.setString("Text" + (i + 1), s);
        }

        nbttagcompound.setString("Color", this.color.c());
        return nbttagcompound;
    }

    @Override
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        this.isEditable = false;
        super.load(iblockdata, nbttagcompound);
        this.color = EnumColor.a(nbttagcompound.getString("Color"), EnumColor.BLACK);

        for (int i = 0; i < 4; ++i) {
            String s = nbttagcompound.getString("Text" + (i + 1));
            IChatMutableComponent ichatmutablecomponent = IChatBaseComponent.ChatSerializer.a(s.isEmpty() ? "\"\"" : s);

            if (this.world instanceof WorldServer) {
                try {
                    this.lines[i] = ChatComponentUtils.filterForDisplay(this.a((EntityPlayer) null), ichatmutablecomponent, (Entity) null, 0);
                } catch (CommandSyntaxException commandsyntaxexception) {
                    this.lines[i] = ichatmutablecomponent;
                }
            } else {
                this.lines[i] = ichatmutablecomponent;
            }

            this.g[i] = null;
        }

    }

    public void a(int i, IChatBaseComponent ichatbasecomponent) {
        this.lines[i] = ichatbasecomponent;
        this.g[i] = null;
    }

    @Nullable
    @Override
    public PacketPlayOutTileEntityData getUpdatePacket() {
        return new PacketPlayOutTileEntityData(this.position, 9, this.b());
    }

    @Override
    public NBTTagCompound b() {
        return this.save(new NBTTagCompound());
    }

    @Override
    public boolean isFilteredNBT() {
        return true;
    }

    public boolean d() {
        return this.isEditable;
    }

    public void a(EntityHuman entityhuman) {
        this.c = entityhuman;
    }

    public EntityHuman f() {
        return this.c;
    }

    public boolean b(EntityHuman entityhuman) {
        IChatBaseComponent[] aichatbasecomponent = this.lines;
        int i = aichatbasecomponent.length;

        for (int j = 0; j < i; ++j) {
            IChatBaseComponent ichatbasecomponent = aichatbasecomponent[j];
            ChatModifier chatmodifier = ichatbasecomponent == null ? null : ichatbasecomponent.getChatModifier();

            if (chatmodifier != null && chatmodifier.getClickEvent() != null) {
                ChatClickable chatclickable = chatmodifier.getClickEvent();

                if (chatclickable.a() == ChatClickable.EnumClickAction.RUN_COMMAND) {
                    entityhuman.getMinecraftServer().getCommandDispatcher().a(this.a((EntityPlayer) entityhuman), chatclickable.b());
                }
            }
        }

        return true;
    }

    public CommandListenerWrapper a(@Nullable EntityPlayer entityplayer) {
        String s = entityplayer == null ? "Sign" : entityplayer.getDisplayName().getString();
        Object object = entityplayer == null ? new ChatComponentText("Sign") : entityplayer.getScoreboardDisplayName();

        return new CommandListenerWrapper(ICommandListener.DUMMY, Vec3D.a((BaseBlockPosition) this.position), Vec2F.a, (WorldServer) this.world, 2, s, (IChatBaseComponent) object, this.world.getMinecraftServer(), entityplayer);
    }

    public EnumColor getColor() {
        return this.color;
    }

    public boolean setColor(EnumColor enumcolor) {
        if (enumcolor != this.getColor()) {
            this.color = enumcolor;
            this.update();
            this.world.notify(this.getPosition(), this.getBlock(), this.getBlock(), 3);
            return true;
        } else {
            return false;
        }
    }
}
