package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Represents a complex living entity - one that is made up of various smaller
 * parts
 */
public interface ComplexLivingEntity extends LivingEntity {
    /**
     * Gets a list of parts that belong to this complex entity
     *
     * @return List of parts
     */
    @NotNull
    public Set<ComplexEntityPart> getParts();
}
