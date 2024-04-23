package org.bukkit.craftbukkit.util;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ClassTraverser implements Iterator<Class<?>> {

    private final Set<Class<?>> visit = new HashSet<>();
    private final Set<Class<?>> toVisit = new HashSet<>();

    private Class<?> next;

    public ClassTraverser(Class<?> next) {
        this.next = next;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public Class<?> next() {
        Class<?> clazz = next;

        visit.add(next);

        Set<Class<?>> classes = Sets.newHashSet(clazz.getInterfaces());
        classes.add(clazz.getSuperclass());
        classes.remove(null); // Super class can be null, remove it if this is the case
        classes.removeAll(visit);
        toVisit.addAll(classes);

        if (toVisit.isEmpty()) {
            next = null;
            return clazz;
        }

        next = toVisit.iterator().next();
        toVisit.remove(next);

        return clazz;
    }
}
