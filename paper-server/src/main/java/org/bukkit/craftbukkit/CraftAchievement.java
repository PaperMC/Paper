package org.bukkit.craftbukkit;

import org.bukkit.Achievement;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

public class CraftAchievement {
    private static final BiMap<String, Achievement> achievements;
    static {
        ImmutableMap<String, Achievement> specialCases = ImmutableMap.<String, Achievement>builder()
                .put("achievement.buildWorkBench", Achievement.BUILD_WORKBENCH)
                .put("achievement.diamonds", Achievement.GET_DIAMONDS)
                .put("achievement.portal", Achievement.NETHER_PORTAL)
                .put("achievement.ghast", Achievement.GHAST_RETURN)
                .put("achievement.theEnd", Achievement.END_PORTAL)
                .put("achievement.theEnd2", Achievement.THE_END)
                .put("achievement.blazeRod", Achievement.GET_BLAZE_ROD)
                .put("achievement.potion", Achievement.BREW_POTION)
                .build();

        ImmutableBiMap.Builder<String, Achievement> builder = ImmutableBiMap.<String, Achievement>builder();
        for (Achievement achievement : Achievement.values()) {
            if (specialCases.values().contains(achievement)) {
                continue;
            }
            builder.put("achievement."+CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, achievement.name()), achievement);
        }

        builder.putAll(specialCases);

        achievements = builder.build();
    }

    public static String getAchievementName(Achievement material) {
        return achievements.inverse().get(material);
    }

    public static Achievement getAchievement(String name) {
        return achievements.get(name);
    }
}
