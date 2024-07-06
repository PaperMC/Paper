package org.bukkit.craftbukkit.legacy.enums;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.Registry;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.NotNull;

public class ImposterEnumSet extends AbstractSet<Object> {

    private final Class<?> objectClass;
    private final Set set;

    private static Set createSet(Class<?> clazz) {
        if (clazz.isEnum()) {
            return EnumSet.noneOf((Class<Enum>) clazz);
        } else {
            return new TreeSet();
        }
    }

    public static ImposterEnumSet noneOf(Class<?> clazz) {
        Set set = createSet(clazz);
        return new ImposterEnumSet(set, clazz);
    }

    public static ImposterEnumSet allOf(Class<?> clazz) {
        Set set;
        if (clazz.isEnum()) {
            set = EnumSet.allOf((Class<Enum>) clazz);
        } else {
            set = new HashSet();
            Registry registry = EnumEvil.getRegistry(clazz);
            if (registry == null) {
                throw new IllegalArgumentException("Class " + clazz + " is not an Enum nor an OldEnum");
            }

            for (Object object : registry) {
                set.add(object);
            }
        }
        return new ImposterEnumSet(set, clazz);
    }

    public static ImposterEnumSet copyOf(Set set) {
        Class<?> clazz;
        if (set instanceof ImposterEnumSet imposter) {
            set = imposter.set;
            clazz = imposter.objectClass;
        } else {
            if (!set.isEmpty()) {
                clazz = (Class<?>) set.stream()
                        .filter(val -> val != null)
                        .map(val -> val.getClass())
                        .findAny()
                        .orElse(Object.class);
            } else {
                clazz = Object.class;
            }
        }

        Set newSet = createSet(clazz);
        newSet.addAll(set);

        return new ImposterEnumSet(newSet, clazz);
    }

    public static ImposterEnumSet copyOf(Collection collection) {
        Class<?> clazz;
        if (collection instanceof ImposterEnumSet imposter) {
            collection = imposter.set;
            clazz = imposter.objectClass;
        } else {
            if (!collection.isEmpty()) {
                clazz = (Class<?>) collection.stream()
                        .filter(val -> val != null)
                        .map(val -> val.getClass())
                        .findAny()
                        .orElse(Object.class);
            } else {
                clazz = Object.class;
            }
        }

        Set newSet = createSet(clazz);
        newSet.addAll(collection);

        return new ImposterEnumSet(newSet, clazz);
    }

    public static ImposterEnumSet complementOf(Set set) {
        Class<?> clazz = null;
        if (set instanceof ImposterEnumSet imposter) {
            set = imposter.set;
            clazz = imposter.objectClass;
        }

        if (set instanceof EnumSet<?> enumSet) {
            enumSet = EnumSet.complementOf(enumSet);

            if (clazz != null) {
                return new ImposterEnumSet(enumSet, clazz);
            }

            if (!set.isEmpty()) {
                clazz = (Class<?>) set.stream()
                        .filter(val -> val != null)
                        .map(val -> val.getClass())
                        .findAny()
                        .orElse(Object.class);
            } else {
                clazz = (Class<?>) enumSet.stream()
                        .filter(val -> val != null)
                        .map(val -> val.getClass())
                        .map(val -> (Class) val)
                        .findAny()
                        .orElse(Object.class);
            }

            return new ImposterEnumSet(enumSet, clazz);
        }

        if (set.isEmpty() && clazz == null) {
            throw new IllegalStateException("Class is null and set is empty, cannot get class!");
        }

        if (clazz == null) {
            clazz = (Class<?>) set.stream()
                    .filter(val -> val != null)
                    .map(val -> val.getClass())
                    .findAny()
                    .orElse(Object.class);
        }

        Registry registry = EnumEvil.getRegistry(clazz);
        Set newSet = new HashSet();

        for (Object value : registry) {
            if (set.contains(value)) {
                continue;
            }

            newSet.add(value);
        }

        return new ImposterEnumSet(newSet, clazz);
    }

    public static ImposterEnumSet of(Object e) {
        Set set = createSet(e.getClass());
        set.add(e);

        return new ImposterEnumSet(set, e.getClass());
    }

    public static ImposterEnumSet of(Object e1, Object e2) {
        Set set = createSet(e1.getClass());
        set.add(e1);
        set.add(e2);

        return new ImposterEnumSet(set, e1.getClass());
    }

    public static ImposterEnumSet of(Object e1, Object e2, Object e3) {
        Set set = createSet(e1.getClass());
        set.add(e1);
        set.add(e2);
        set.add(e3);

        return new ImposterEnumSet(set, e1.getClass());
    }


    public static ImposterEnumSet of(Object e1, Object e2, Object e3, Object e4) {
        Set set = createSet(e1.getClass());
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);

        return new ImposterEnumSet(set, e1.getClass());
    }


    public static ImposterEnumSet of(Object e1, Object e2, Object e3, Object e4, Object e5) {
        Set set = createSet(e1.getClass());
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);

        return new ImposterEnumSet(set, e1.getClass());
    }


    public static ImposterEnumSet of(Object e, Object... rest) {
        Set set = createSet(e.getClass());
        set.add(e);

        Collections.addAll(set, rest);

        return new ImposterEnumSet(set, e.getClass());
    }

    public static ImposterEnumSet range(Object from, Object to) {
        Set set;
        if (from.getClass().isEnum()) {
            set = EnumSet.range((Enum) from, (Enum) to);
        } else {
            set = new HashSet();
            Registry registry = EnumEvil.getRegistry(from.getClass());
            for (Object o : registry) {
                if (((OldEnum) o).ordinal() < ((OldEnum) from).ordinal()) {
                    continue;
                }

                if (((OldEnum) o).ordinal() > ((OldEnum) to).ordinal()) {
                    continue;
                }

                set.add(o);
            }
        }

        return new ImposterEnumSet(set, from.getClass());
    }

    private ImposterEnumSet(Set set, Class<?> objectClass) {
        this.set = set;
        this.objectClass = objectClass;
    }

    @Override
    public Iterator<Object> iterator() {
        return set.iterator();
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean equals(Object o) {
        return set.equals(o);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return (T[]) set.toArray(a);
    }

    @Override
    public boolean add(Object o) {
        typeCheck(o);
        return set.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<?> c) {
        if (set instanceof EnumSet<?>) {
            set.addAll(c);
        }

        return super.addAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public String toString() {
        return set.toString();
    }

    public ImposterEnumSet clone() {
        Set newSet;
        if (set instanceof EnumSet<?> enumSet) {
            newSet = enumSet.clone();
        } else {
            newSet = new HashSet();
            newSet.addAll(set);
        }

        return new ImposterEnumSet(newSet, objectClass);
    }

    private void typeCheck(Object object) {
        if (objectClass != DummyEnum.class) {
            if (!objectClass.isAssignableFrom(object.getClass())) {
                throw new ClassCastException(object.getClass() + " != " + objectClass);
            }
        }
    }
}
