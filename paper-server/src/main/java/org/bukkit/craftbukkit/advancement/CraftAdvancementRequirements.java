package org.bukkit.craftbukkit.advancement;

import java.util.List;
import net.minecraft.advancements.AdvancementRequirements;
import org.bukkit.advancement.AdvancementRequirement;
import org.jetbrains.annotations.NotNull;

public class CraftAdvancementRequirements implements org.bukkit.advancement.AdvancementRequirements {

    private final AdvancementRequirements requirements;

    public CraftAdvancementRequirements(AdvancementRequirements requirements) {
        this.requirements = requirements;
    }

    @NotNull
    @Override
    public List<AdvancementRequirement> getRequirements() {
        return this.requirements.requirements().stream().map((requirement) -> (AdvancementRequirement) new CraftAdvancementRequirement(requirement)).toList();
    }
}
