package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.level.block.entity.TileEntitySign;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.craftbukkit.block.sign.CraftSignSide;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CraftSign<T extends TileEntitySign> extends CraftBlockEntityState<T> implements Sign {

    private final CraftSignSide front;

    public CraftSign(World world, T tileEntity) {
        super(world, tileEntity);
        this.front = new CraftSignSide(this.getSnapshot());
    }

    @Override
    public String[] getLines() {
        return front.getLines();
    }

    @Override
    public String getLine(int index) throws IndexOutOfBoundsException {
        return front.getLine(index);
    }

    @Override
    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        front.setLine(index, line);
    }

    @Override
    public boolean isEditable() {
        return getSnapshot().isEditable;
    }

    @Override
    public void setEditable(boolean editable) {
        getSnapshot().isEditable = editable;
    }

    @Override
    public boolean isGlowingText() {
        return front.isGlowingText();
    }

    @Override
    public void setGlowingText(boolean glowing) {
        front.setGlowingText(glowing);
    }

    @NotNull
    @Override
    public SignSide getSide(Side side) {
        Preconditions.checkArgument(side != null, "side == null");

        return front;
    }

    @Override
    public DyeColor getColor() {
        return front.getColor();
    }

    @Override
    public void setColor(DyeColor color) {
        front.setColor(color);
    }

    @Override
    public void applyTo(T sign) {
        front.applyLegacyStringToSignSide();

        super.applyTo(sign);
    }

    public static void openSign(Sign sign, Player player) {
        Preconditions.checkArgument(sign != null, "sign == null");
        Preconditions.checkArgument(sign.isPlaced(), "Sign must be placed");
        Preconditions.checkArgument(sign.getWorld() == player.getWorld(), "Sign must be in same world as Player");

        TileEntitySign handle = ((CraftSign<?>) sign).getTileEntity();
        handle.isEditable = true;

        ((CraftPlayer) player).getHandle().openTextEdit(handle);
    }

    public static IChatBaseComponent[] sanitizeLines(String[] lines) {
        IChatBaseComponent[] components = new IChatBaseComponent[4];

        for (int i = 0; i < 4; i++) {
            if (i < lines.length && lines[i] != null) {
                components[i] = CraftChatMessage.fromString(lines[i])[0];
            } else {
                components[i] = IChatBaseComponent.empty();
            }
        }

        return components;
    }

    public static String[] revertComponents(IChatBaseComponent[] components) {
        String[] lines = new String[components.length];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = revertComponent(components[i]);
        }
        return lines;
    }

    private static String revertComponent(IChatBaseComponent component) {
        return CraftChatMessage.fromComponent(component);
    }
}
