package org.bukkit.craftbukkit.block.sign;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignText;
import org.bukkit.DyeColor;
import org.bukkit.block.sign.SignSide;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftSignSide implements SignSide {

    // Lazily initialized only if requested:
    // Paper start
    private java.util.ArrayList<net.kyori.adventure.text.Component> originalLines = null; // ArrayList for RandomAccess
    private java.util.ArrayList<net.kyori.adventure.text.Component> lines = null; // ArrayList for RandomAccess
    // Paper end
    private SignText signText;

    public CraftSignSide(SignText signText) {
        this.signText = signText;
    }

    // Paper start
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
        this.lines = io.papermc.paper.adventure.PaperAdventure.asAdventure(com.google.common.collect.Lists.newArrayList(this.signText.getMessages(false)));
        this.originalLines = new java.util.ArrayList<>(this.lines);
    }
    // Paper end

    @NotNull
    @Override
    public String[] getLines() {
        // Paper start
        this.loadLines();
        return this.lines.stream().map(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection()::serialize).toArray(String[]::new); // Paper
        // Paper end
    }

    @NotNull
    @Override
    public String getLine(int index) throws IndexOutOfBoundsException {
        // Paper start
        this.loadLines();
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.lines.get(index));
        // Paper end
    }

    @Override
    public void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException {
        // Paper start
        this.loadLines();
        this.lines.set(index, line != null ? net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(line) : net.kyori.adventure.text.Component.empty());
        // Paper end
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
            // Paper start
            for (int i = 0; i < this.lines.size(); ++i) {
                net.kyori.adventure.text.Component component = this.lines.get(i);
                net.kyori.adventure.text.Component origComp = this.originalLines.get(i);
                if (component.equals(origComp)) {
                    continue; // The line contents are still the same, skip.
                }
                this.signText = this.signText.setMessage(i, io.papermc.paper.adventure.PaperAdventure.asVanilla(component));
            }
            // Paper end
        }

        return this.signText;
    }
}
