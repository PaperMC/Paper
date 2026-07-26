package org.bukkit.craftbukkit.block.sign;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.minecraft.world.level.block.entity.SignText;
import org.bukkit.DyeColor;
import org.bukkit.block.sign.SignSide;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftSignSide implements SignSide {

    // Lazily initialized only if requested:
    private List<Component> originalLines = null; // ArrayList for RandomAccess
    private List<net.kyori.adventure.text.Component> lines = null; // ArrayList for RandomAccess
    private SignText signText;

    public CraftSignSide(SignText signText) {
        this.signText = signText;
    }

    @Override
    public java.util.@NotNull List<net.kyori.adventure.text.Component> lines() {
        this.loadLines();
        return this.lines;
    }

    @Override
    public net.kyori.adventure.text.@NotNull Component line(final int index) throws IndexOutOfBoundsException {
        this.loadLines();
        return this.lines.get(index);
    }

    @Override
    public void line(final int index, final net.kyori.adventure.text.@NotNull Component line) throws IndexOutOfBoundsException {
        com.google.common.base.Preconditions.checkArgument(line != null, "Line cannot be null");
        this.loadLines();
        this.lines.set(index, line);
    }

    private void loadLines() {
        if (this.lines != null) {
            return;
        }
        // Lazy initialization:
        this.lines = io.papermc.paper.adventure.PaperAdventure.asAdventure(this.signText.getMessages(false));
        this.originalLines = new java.util.ArrayList<>(this.lines);
    }

    @NotNull
    @Override
    public String[] getLines() {
        this.loadLines();
        return this.lines.stream().map(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection()::serialize).toArray(String[]::new); // Paper
    }

    @NotNull
    @Override
    public String getLine(int index) throws IndexOutOfBoundsException {
        this.loadLines();
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.lines.get(index));
    }

    @Override
    public void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException {
        this.loadLines();
        this.lines.set(index, net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(line));
    }

    @Override
    public boolean isGlowingText() {
        return this.signText.hasGlowingText();
    }

    @Override
    public void setGlowingText(boolean glowing) {
        this.signText = this.signText.setHasGlowingText(glowing);
    }

    @Nullable
    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) this.signText.getColor().getId());
    }

    @Override
    public void setColor(@NotNull DyeColor color) {
        this.signText = this.signText.setColor(net.minecraft.world.item.DyeColor.byId(color.getWoolData()));
    }

    public SignText applyLegacyStringToSignSide() {
        if (this.lines != null) {
            for (int i = 0; i < this.lines.size(); ++i) {
                net.kyori.adventure.text.Component component = this.lines.get(i);
                net.kyori.adventure.text.Component origComp = this.originalLines.get(i);
                if (component.equals(origComp)) {
                    continue; // The line contents are still the same, skip.
                }
                this.signText = this.signText.setMessage(i, io.papermc.paper.adventure.PaperAdventure.asVanilla(component));
            }
        }

        return this.signText;
    }
}
