package io.papermc.generator.types.goal;

import com.google.common.base.CaseFormat;
import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.DataFiles;
import io.papermc.generator.resources.data.EntityClassData;
import io.papermc.generator.types.Types;
import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.util.ClassHelper;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.apache.commons.lang3.math.NumberUtils;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MobGoalNames { // todo sync with MobGoalHelper ideally this should not be duplicated

    private static final Map<Class<? extends Goal>, EntityClassData> GENERIC_TYPE_CACHE = new HashMap<>();

    // TODO these kinda should be checked on each release, in case nested classes changes
    private static final Map<String, String> RENAMES = Util.make(new HashMap<>(), map -> {
        map.put("AbstractSkeleton$1", "AbstractSkeletonMelee");

        // remove duplicate
        map.put("TraderLlama$TraderLlamaDefendWanderingTraderGoal", "TraderLlamaDefendWanderingTraderGoal");
        map.put("AbstractIllager$RaiderOpenDoorGoal", "RaiderOpenDoorGoal");
    });

    private static String getPathName(EntityClassData type, Class<?> holderClass, String name) {
        String pathName = name.substring(name.lastIndexOf('.') + 1);
        boolean needRename = false;

        // inner classes
        int firstInnerDelimiter = pathName.indexOf('$');
        if (firstInnerDelimiter != -1) {
            String innerClassNames = pathName.substring(firstInnerDelimiter + 1);
            for (String innerClassName : innerClassNames.split("\\$")) {
                if (NumberUtils.isDigits(innerClassName)) {
                    needRename = true;
                    break;
                }
            }
            if (!needRename && !RENAMES.containsKey(pathName)) {
                pathName = innerClassNames;
            }
        }

        if (!RENAMES.containsKey(pathName)) {
            if (needRename) {
                throw new IllegalStateException("need to map " + name + " (" + pathName + ")");
            }
            String prefix = null;
            if (type.hasSpecifier()) {
                prefix = type.name().simpleName();
            } else if (!net.minecraft.world.entity.Mob.class.isAssignableFrom(holderClass)) {
                prefix = holderClass.getSimpleName();
            }
            if (prefix != null && !pathName.startsWith(prefix)) {
                pathName = prefix + pathName;
            }
        } else {
            pathName = RENAMES.get(pathName);
        }

        pathName = Formatting.stripInitialWord(pathName, "Abstract");
        pathName = pathName.replaceAll("TargetGoal|Goal", "");
        pathName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, pathName);

        return pathName;
    }

    static GoalKey getKey(Class<? extends Goal> goalClass) {
        EntityClassData classData = getGenericType(goalClass);
        Class<?> holderClass = ClassHelper.getTopLevelClass(goalClass);
        String name = getPathName(classData, holderClass, goalClass.getName());
        return new GoalKey(classData.name(), ResourceLocation.withDefaultNamespace(name));
    }

    private static final Int2BooleanFunction[] VISIBILITY_SEARCH_STEP = {
        Modifier::isPublic,
        Modifier::isProtected,
        mod -> (mod & 0b111) == 0, // package-private
        Modifier::isPrivate,
    };

    private static final Comparator<Constructor<?>> VISIBILITY_ORDER = Comparator.comparingInt(constructor -> {
        int mod = constructor.getModifiers();
        for (int i = 0; i < VISIBILITY_SEARCH_STEP.length; i++) {
            Int2BooleanFunction visibility = VISIBILITY_SEARCH_STEP[i];
            if (visibility.test(mod)) {
                return i;
            }
        }
        throw new UnsupportedOperationException("Unknown visibility: " + mod);
    });

    private static EntityClassData getGenericType(Class<? extends Goal> goalClass) {
        return GENERIC_TYPE_CACHE.computeIfAbsent(goalClass, key -> {
            Constructor<?>[] constructors = key.getDeclaredConstructors();
            Arrays.sort(constructors, VISIBILITY_ORDER);

            for (Constructor<?> constructor : constructors) {
                for (Class<?> param : constructor.getParameterTypes()) {
                    if (net.minecraft.world.entity.Mob.class.isAssignableFrom(param)) {
                        //noinspection unchecked
                        return toBukkitClassData((Class<? extends net.minecraft.world.entity.Mob>) param);
                    } else if (RangedAttackMob.class.isAssignableFrom(param)) {
                        return new EntityClassData(Types.RANGED_ENTITY, false); // todo move outside
                    }
                }
            }
            throw new IllegalStateException("Can't figure out applicable entity for mob goal " + goalClass); // maybe just return Mob?
        });
    }

    private static EntityClassData toBukkitClassData(Class<? extends net.minecraft.world.entity.Mob> internalClass) {
        EntityClassData data = DataFileLoader.get(DataFiles.ENTITY_CLASS_NAMES).get(internalClass);
        if (data == null) {
            throw new IllegalStateException("Can't figure out applicable bukkit entity for internal entity " + internalClass); // maybe just return Mob?
        }
        return data;
    }
}
