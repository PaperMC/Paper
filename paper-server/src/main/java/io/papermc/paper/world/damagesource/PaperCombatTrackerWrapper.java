package io.papermc.paper.world.damagesource;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public record PaperCombatTrackerWrapper(net.minecraft.world.damagesource.CombatTracker handle) implements CombatTracker {

    @Override
    public LivingEntity getEntity() {
        return this.handle.mob.getBukkitLivingEntity();
    }

    @Override
    public List<CombatEntry> getCombatEntries() {
        List<CombatEntry> combatEntries = new ArrayList<>();
        this.handle.entries.forEach(combatEntry -> combatEntries.add(new PaperCombatEntryWrapper(combatEntry)));
        return combatEntries;
    }

    @Override
    public void setCombatEntries(List<CombatEntry> combatEntries) {
        this.handle.entries.clear();
        combatEntries.forEach(combatEntry -> this.handle.entries.add(((PaperCombatEntryWrapper) combatEntry).handle()));
    }

    @Override
    public @Nullable CombatEntry getMostSignificantFall() {
        net.minecraft.world.damagesource.CombatEntry combatEntry = this.handle.getMostSignificantFall();
        return combatEntry == null ? null : new PaperCombatEntryWrapper(combatEntry);
    }

    @Override
    public boolean isInCombat() {
        return this.handle.inCombat;
    }

    @Override
    public boolean isTakingDamage() {
        return this.handle.takingDamage;
    }

    @Override
    public int getCombatDuration() {
        return this.handle.getCombatDuration();
    }

    @Override
    public void addCombatEntry(CombatEntry combatEntry) {
        net.minecraft.world.damagesource.CombatEntry entry = ((PaperCombatEntryWrapper) combatEntry).handle();
        this.handle.recordDamageAndCheckCombatState(entry);
    }

    @Override
    public Component getDeathMessage() {
        return PaperAdventure.asAdventure(this.handle.getDeathMessage());
    }

    @Override
    public void resetCombatState() {
        this.handle.resetCombatState();
    }

    public static net.minecraft.world.damagesource.FallLocation paperToMinecraft(FallLocation fallLocation) {
        return switch (fallLocation) {
            case FallLocation fl when fl == FallLocation.GENERIC -> net.minecraft.world.damagesource.FallLocation.GENERIC;
            case FallLocation fl when fl == FallLocation.LADDER -> net.minecraft.world.damagesource.FallLocation.LADDER;
            case FallLocation fl when fl == FallLocation.VINES -> net.minecraft.world.damagesource.FallLocation.VINES;
            case FallLocation fl when fl == FallLocation.WEEPING_VINES -> net.minecraft.world.damagesource.FallLocation.WEEPING_VINES;
            case FallLocation fl when fl == FallLocation.TWISTING_VINES -> net.minecraft.world.damagesource.FallLocation.TWISTING_VINES;
            case FallLocation fl when fl == FallLocation.SCAFFOLDING -> net.minecraft.world.damagesource.FallLocation.SCAFFOLDING;
            case FallLocation fl when fl == FallLocation.OTHER_CLIMBABLE -> net.minecraft.world.damagesource.FallLocation.OTHER_CLIMBABLE;
            case FallLocation fl when fl == FallLocation.WATER -> net.minecraft.world.damagesource.FallLocation.WATER;
            default -> throw new IllegalArgumentException("Unknown fall location: " + fallLocation.id());
        };
    }

    public static FallLocation minecraftToPaper(net.minecraft.world.damagesource.FallLocation fallLocation) {
        return switch (fallLocation) {
            case
                net.minecraft.world.damagesource.FallLocation fl when fl == net.minecraft.world.damagesource.FallLocation.GENERIC ->
                FallLocation.GENERIC;
            case
                net.minecraft.world.damagesource.FallLocation fl when fl == net.minecraft.world.damagesource.FallLocation.LADDER ->
                FallLocation.LADDER;
            case
                net.minecraft.world.damagesource.FallLocation fl when fl == net.minecraft.world.damagesource.FallLocation.VINES ->
                FallLocation.VINES;
            case
                net.minecraft.world.damagesource.FallLocation fl when fl == net.minecraft.world.damagesource.FallLocation.WEEPING_VINES ->
                FallLocation.WEEPING_VINES;
            case
                net.minecraft.world.damagesource.FallLocation fl when fl == net.minecraft.world.damagesource.FallLocation.TWISTING_VINES ->
                FallLocation.TWISTING_VINES;
            case
                net.minecraft.world.damagesource.FallLocation fl when fl == net.minecraft.world.damagesource.FallLocation.SCAFFOLDING ->
                FallLocation.SCAFFOLDING;
            case
                net.minecraft.world.damagesource.FallLocation fl when fl == net.minecraft.world.damagesource.FallLocation.OTHER_CLIMBABLE ->
                FallLocation.OTHER_CLIMBABLE;
            case
                net.minecraft.world.damagesource.FallLocation fl when fl == net.minecraft.world.damagesource.FallLocation.WATER ->
                FallLocation.WATER;
            default -> throw new IllegalArgumentException("Unknown fall location: " + fallLocation.id());
        };
    }

}
