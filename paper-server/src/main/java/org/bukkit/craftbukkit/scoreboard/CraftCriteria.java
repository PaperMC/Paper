package org.bukkit.craftbukkit.scoreboard;

import com.google.common.collect.ImmutableMap;
import io.papermc.paper.statistic.PaperStatistics;
import java.util.Map;
import java.util.Objects;
import net.minecraft.stats.Stat;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.RenderType;

public final class CraftCriteria implements Criteria {
    private static final Map<String, Criteria> DEFAULTS;
    private static final CraftCriteria DUMMY;

    static {
        ImmutableMap.Builder<String, Criteria> defaults = ImmutableMap.builder();

        for (Map.Entry<String, ObjectiveCriteria> entry : ObjectiveCriteria.CRITERIA_CACHE.entrySet()) {
            String name = entry.getKey();
            ObjectiveCriteria criteria = entry.getValue();

            defaults.put(name, convertFromNms(criteria));
        }

        DEFAULTS = defaults.build();
        DUMMY = (CraftCriteria) DEFAULTS.get(ObjectiveCriteria.DUMMY.getName());
    }

    final ObjectiveCriteria criteria;
    final String name;

    private CraftCriteria(String name) {
        this.name = name;
        this.criteria = CraftCriteria.DUMMY.criteria;
    }

    private CraftCriteria(ObjectiveCriteria criteria) {
        this.criteria = criteria;
        this.name = criteria.getName();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isReadOnly() {
        return this.criteria.isReadOnly();
    }

    @Override
    public RenderType getDefaultRenderType() {
        return RenderType.values()[this.criteria.getDefaultRenderType().ordinal()];
    }

    public static Criteria getFromNMS(ObjectiveCriteria criteria) {
        return Objects.requireNonNullElseGet(CraftCriteria.DEFAULTS.get(criteria.getName()), () -> convertFromNms(criteria));
    }

    static Criteria convertFromNms(ObjectiveCriteria criteria) {
        return criteria instanceof Stat<?> stat ? PaperStatistics.getPaperStatistic(stat) : new CraftCriteria(criteria);
    }

    public static Criteria getFromNMS(Objective objective) {
        return getFromNMS(objective.getCriteria());
    }

    public static Criteria getFromBukkit(String name) {
        Criteria criteria = CraftCriteria.DEFAULTS.get(name);
        if (criteria != null) {
            return criteria;
        }

        return ObjectiveCriteria.byName(name).map(CraftCriteria::convertFromNms).orElseGet(() -> new CraftCriteria(name));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final CraftCriteria craftCriteria)) {
            return false;
        }

        return craftCriteria.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() ^ CraftCriteria.class.hashCode();
    }
}
