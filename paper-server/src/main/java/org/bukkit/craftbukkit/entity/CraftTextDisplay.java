package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.Display;
import org.bukkit.Color;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
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
    public String getText() {
        return CraftChatMessage.fromComponent(this.getHandle().getText());
    }

    @Override
    public void setText(String text) {
        this.getHandle().setText(CraftChatMessage.fromString(text, true)[0]);
    }
    // Paper start
    @Override
    public net.kyori.adventure.text.Component text() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.getHandle().getText());
    }

    @Override
    public void text(net.kyori.adventure.text.Component text) {
        this.getHandle().setText(text == null ? net.minecraft.network.chat.Component.empty() : io.papermc.paper.adventure.PaperAdventure.asVanilla(text));
    }
    // Paper end

    @Override
    public int getLineWidth() {
        return this.getHandle().getLineWidth();
    }

    @Override
    public void setLineWidth(int width) {
        this.getHandle().getEntityData().set(Display.TextDisplay.DATA_LINE_WIDTH_ID, width);
    }

    @Override
    public Color getBackgroundColor() {
        int color = this.getHandle().getBackgroundColor();

        return (color == -1) ? null : Color.fromARGB(color);
    }

    @Override
    public void setBackgroundColor(Color color) {
        if (color == null) {
            this.getHandle().getEntityData().set(Display.TextDisplay.DATA_BACKGROUND_COLOR_ID, -1);
        } else {
            this.getHandle().getEntityData().set(Display.TextDisplay.DATA_BACKGROUND_COLOR_ID, color.asARGB());
        }
    }

    @Override
    public byte getTextOpacity() {
        return this.getHandle().getTextOpacity();
    }

    @Override
    public void setTextOpacity(byte opacity) {
        this.getHandle().setTextOpacity(opacity);
    }

    @Override
    public boolean isShadowed() {
        return this.getFlag(Display.TextDisplay.FLAG_SHADOW);
    }

    @Override
    public void setShadowed(boolean shadow) {
        this.setFlag(Display.TextDisplay.FLAG_SHADOW, shadow);
    }

    @Override
    public boolean isSeeThrough() {
        return this.getFlag(Display.TextDisplay.FLAG_SEE_THROUGH);
    }

    @Override
    public void setSeeThrough(boolean seeThrough) {
        this.setFlag(Display.TextDisplay.FLAG_SEE_THROUGH, seeThrough);
    }

    @Override
    public boolean isDefaultBackground() {
        return this.getFlag(Display.TextDisplay.FLAG_USE_DEFAULT_BACKGROUND);
    }

    @Override
    public void setDefaultBackground(boolean defaultBackground) {
        this.setFlag(Display.TextDisplay.FLAG_USE_DEFAULT_BACKGROUND, defaultBackground);
    }

    @Override
    public TextAlignment getAlignment() {
        Display.TextDisplay.Align nms = Display.TextDisplay.getAlign(this.getHandle().getFlags());
        return TextAlignment.valueOf(nms.name());
    }

    @Override
    public void setAlignment(TextAlignment alignment) {
        Preconditions.checkArgument(alignment != null, "Alignment cannot be null");

        switch (alignment) {
            case LEFT:
                this.setFlag(Display.TextDisplay.FLAG_ALIGN_LEFT, true);
                this.setFlag(Display.TextDisplay.FLAG_ALIGN_RIGHT, false);
                break;
            case RIGHT:
                this.setFlag(Display.TextDisplay.FLAG_ALIGN_LEFT, false);
                this.setFlag(Display.TextDisplay.FLAG_ALIGN_RIGHT, true);
                break;
            case CENTER:
                this.setFlag(Display.TextDisplay.FLAG_ALIGN_LEFT, false);
                this.setFlag(Display.TextDisplay.FLAG_ALIGN_RIGHT, false);
                break;
            default:
                throw new IllegalArgumentException("Unknown alignment " + alignment);
        }
    }

    private boolean getFlag(int flag) {
        return (this.getHandle().getFlags() & flag) != 0;
    }

    private void setFlag(int flag, boolean set) {
        byte flagBits = this.getHandle().getFlags();

        if (set) {
            flagBits |= flag;
        } else {
            flagBits &= ~flag;
        }

        this.getHandle().setFlags(flagBits);
    }
}
