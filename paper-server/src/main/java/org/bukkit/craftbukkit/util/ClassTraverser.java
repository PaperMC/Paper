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
        return this.next != null;
    }

    @Override
    public Class<?> next() {
        Class<?> clazz = this.next;

        this.visit.add(this.next);

        Set<Class<?>> classes = Sets.newHashSet(clazz.getInterfaces());
        classes.add(clazz.getSuperclass());
        classes.remove(null); // Super class can be null, remove it if this is the case
        classes.removeAll(this.visit);
        this.toVisit.addAll(classes);

        if (this.toVisit.isEmpty()) {
            this.next = null;
            return clazz;
        }

        this.next = this.toVisit.iterator().next();
        this.toVisit.remove(this.next);

        return clazz;
    }
}
