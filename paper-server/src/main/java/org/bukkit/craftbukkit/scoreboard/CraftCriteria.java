package org.bukkit.craftbukkit.scoreboard;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.RenderType;

public final class CraftCriteria implements Criteria {
    static final Map<String, Criteria> DEFAULTS; // Paper - stats api
    static final CraftCriteria DUMMY;

    static {
        ImmutableMap.Builder<String, Criteria> defaults = ImmutableMap.builder(); // Paper - stats api

        for (Map.Entry<String, ObjectiveCriteria> entry : ObjectiveCriteria.CRITERIA_CACHE.entrySet()) {
            String name = entry.getKey();
            ObjectiveCriteria criteria = entry.getValue();

            defaults.put(name, convertFromNms(criteria)); // Paper - stats api
        }

        DEFAULTS = defaults.build();
        DUMMY = (CraftCriteria) DEFAULTS.get("dummy"); // Paper - stats api
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

    // Paper start
    public static Criteria getFromNMS(ObjectiveCriteria criteria) { // Paper - stats api
        return java.util.Objects.requireNonNullElseGet(CraftCriteria.DEFAULTS.get(criteria.getName()), () -> convertFromNms(criteria));
    }
    // Paper end

    // Paper start - stats api
    static Criteria convertFromNms(ObjectiveCriteria criteria) {
        return criteria instanceof net.minecraft.stats.Stat<?> stat ? io.papermc.paper.statistic.PaperStatistics.getPaperStatistic(stat) : new CraftCriteria(criteria);
    }
    // Paper end - stats api

    public static Criteria getFromNMS(Objective objective) { // Paper - stats api
        return getFromNMS(objective.getCriteria()); // Paper - stats api
    }

    public static Criteria getFromBukkit(String name) { // Paper - stats api
        Criteria criteria = CraftCriteria.DEFAULTS.get(name); // Paper - stats api
        if (criteria != null) {
            return criteria;
        }

        return ObjectiveCriteria.byName(name).map(CraftCriteria::convertFromNms).orElseGet(() -> new CraftCriteria(name)); // Paper - stats api
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
