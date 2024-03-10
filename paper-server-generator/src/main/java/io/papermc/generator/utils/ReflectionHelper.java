package io.papermc.generator.utils;

import io.papermc.generator.types.EntityMetaWatcherGenerator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public final class ReflectionHelper {
    private ReflectionHelper(){}

    public static List<Field> getAllForAllParents(Class<?> clazz, Predicate<Field> filter) {
        List<Field> allClasses = new ArrayList<>(forClass(clazz, filter));
        for (final Class<?> aClass : allParents(clazz)) {
            allClasses.addAll(forClass(aClass, filter));
        }
        return allClasses;
    }

    public static List<Class<?>> allParents(Class<?> clazz){
        List<Class<?>> allClasses = new ArrayList<>();
        Class<?> current = clazz;
        while (current.getSuperclass() != null) {
            var toAdd = current.getSuperclass();
            if (net.minecraft.world.entity.Entity.class.isAssignableFrom(toAdd)) {
                allClasses.add(toAdd);
            }
            current = toAdd;
        }
        Collections.reverse(allClasses);
        return allClasses;
    }

    public static List<Field> forClass(Class<?> clazz, Predicate<Field> filter) {
        return Arrays.stream(clazz.getDeclaredFields()).filter(filter).toList();
    }
}
