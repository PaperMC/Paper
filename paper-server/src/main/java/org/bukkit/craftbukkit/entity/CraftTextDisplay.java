package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.Display;
import org.bukkit.Color;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;

public class CraftTextDisplay extends CraftDisplay implements TextDisplay {

    public CraftTextDisplay(CraftServer server, net.minecraft.world.entity.Display.TextDisplay entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.Display.TextDisplay getHandle() {
        return (net.minecraft.world.entity.Display.TextDisplay) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftTextDisplay";
    }

    @Override
    public EntityType getType() {
        return EntityType.TEXT_DISPLAY;
    }

    @Override
    public String getText() {
        return CraftChatMessage.fromComponent(getHandle().getText());
    }

    @Override
    public void setText(String text) {
        getHandle().setText(CraftChatMessage.fromString(text, true)[0]);
    }

    @Override
    public int getLineWidth() {
        return getHandle().getLineWidth();
    }

    @Override
    public void setLineWidth(int width) {
        getHandle().setLineWidth(width);
    }

    @Override
    public Color getBackgroundColor() {
        int color = getHandle().getBackgroundColor();

        return (color == -1) ? null : Color.fromARGB(color);
    }

    @Override
    public void setBackgroundColor(Color color) {
        if (color == null) {
            getHandle().setBackgroundColor(-1);
        } else {
            getHandle().setBackgroundColor(color.asARGB());
        }
    }

    @Override
    public byte getTextOpacity() {
        return getHandle().getTextOpacity();
    }

    @Override
    public void setTextOpacity(byte opacity) {
        getHandle().setTextOpacity(opacity);
    }

    @Override
    public boolean isShadowed() {
        return getFlag(Display.TextDisplay.FLAG_SHADOW);
    }

    @Override
    public void setShadowed(boolean shadow) {
        setFlag(Display.TextDisplay.FLAG_SHADOW, shadow);
    }

    @Override
    public boolean isSeeThrough() {
        return getFlag(Display.TextDisplay.FLAG_SEE_THROUGH);
    }

    @Override
    public void setSeeThrough(boolean seeThrough) {
        setFlag(Display.TextDisplay.FLAG_SEE_THROUGH, seeThrough);
    }

    @Override
    public boolean isDefaultBackground() {
        return getFlag(Display.TextDisplay.FLAG_USE_DEFAULT_BACKGROUND);
    }

    @Override
    public void setDefaultBackground(boolean defaultBackground) {
        setFlag(Display.TextDisplay.FLAG_USE_DEFAULT_BACKGROUND, defaultBackground);
    }

    @Override
    public TextAligment getAlignment() {
        Display.TextDisplay.Align nms = Display.TextDisplay.getAlign(getHandle().getFlags());
        return TextAligment.valueOf(nms.name());
    }

    @Override
    public void setAlignment(TextAligment alignment) {
        Preconditions.checkArgument(alignment != null, "Alignment cannot be null");

        switch (alignment) {
            case LEFT:
                setFlag(Display.TextDisplay.FLAG_ALIGN_LEFT, true);
                setFlag(Display.TextDisplay.FLAG_ALIGN_RIGHT, false);
                break;
            case RIGHT:
                setFlag(Display.TextDisplay.FLAG_ALIGN_LEFT, false);
                setFlag(Display.TextDisplay.FLAG_ALIGN_RIGHT, true);
                break;
            case CENTER:
                setFlag(Display.TextDisplay.FLAG_ALIGN_LEFT, false);
                setFlag(Display.TextDisplay.FLAG_ALIGN_RIGHT, false);
                break;
            default:
                throw new IllegalArgumentException("Unknown alignment " + alignment);
        }
    }

    private boolean getFlag(int flag) {
        return (getHandle().getFlags() & flag) != 0;
    }

    private void setFlag(int flag, boolean set) {
        byte flagBits = getHandle().getFlags();

        if (set) {
            flagBits |= flag;
        } else {
            flagBits &= ~flag;
        }

        getHandle().setFlags(flagBits);
    }
}
