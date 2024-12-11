package org.bukkit.craftbukkit.scoreboard;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.RenderType;

public final class CraftCriteria implements Criteria {
    static final Map<String, CraftCriteria> DEFAULTS;
    static final CraftCriteria DUMMY;

    static {
        ImmutableMap.Builder<String, CraftCriteria> defaults = ImmutableMap.builder();

        for (Map.Entry<String, ObjectiveCriteria> entry : ObjectiveCriteria.CRITERIA_CACHE.entrySet()) {
            String name = entry.getKey();
            ObjectiveCriteria criteria = entry.getValue();

            defaults.put(name, new CraftCriteria(criteria));
        }

        DEFAULTS = defaults.build();
        DUMMY = DEFAULTS.get("dummy");
    }

    final ObjectiveCriteria criteria;
    final String bukkitName;

    private CraftCriteria(String bukkitName) {
        this.bukkitName = bukkitName;
        this.criteria = CraftCriteria.DUMMY.criteria;
    }

    private CraftCriteria(ObjectiveCriteria criteria) {
        this.criteria = criteria;
        this.bukkitName = criteria.getName();
    }

    @Override
    public String getName() {
        return this.bukkitName;
    }

    @Override
    public boolean isReadOnly() {
        return this.criteria.isReadOnly();
    }

    @Override
    public RenderType getDefaultRenderType() {
        return RenderType.values()[this.criteria.getDefaultRenderType().ordinal()];
    }

    static CraftCriteria getFromNMS(Objective objective) {
        return CraftCriteria.DEFAULTS.get(objective.getCriteria().getName());
    }

    public static CraftCriteria getFromBukkit(String name) {
        CraftCriteria criteria = CraftCriteria.DEFAULTS.get(name);
        if (criteria != null) {
            return criteria;
        }

        return ObjectiveCriteria.byName(name).map(CraftCriteria::new).orElseGet(() -> new CraftCriteria(name));
    }

    @Override
    public boolean equals(Object that) {
        if (!(that instanceof CraftCriteria)) {
            return false;
        }
        return ((CraftCriteria) that).bukkitName.equals(this.bukkitName);
    }

    @Override
    public int hashCode() {
        return this.bukkitName.hashCode() ^ CraftCriteria.class.hashCode();
    }
}
