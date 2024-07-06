package org.bukkit.craftbukkit.legacy.enums;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * The "I can't believe it works" map.
 * It replaces every EnumMap with the ImposterEnumMap and uses a HashMap instead of an object array.
 * Used so that plugins which use an EnumMap still work.
 */
public class ImposterEnumMap extends AbstractMap<Object, Object> {

    private final Class<?> objectClass;
    private final Map map;

    public ImposterEnumMap(Class<?> objectClass) {
        this.objectClass = objectClass;
        this.map = getMap(objectClass);
    }

    public ImposterEnumMap(EnumMap enumMap) {
        this.objectClass = DummyEnum.class;
        this.map = enumMap.clone();
    }

    public ImposterEnumMap(Map map) {
        if (map instanceof ImposterEnumMap) {
            this.objectClass = ((ImposterEnumMap) map).objectClass;
            this.map = getMap(objectClass);
        } else {
            this.objectClass = DummyEnum.class;
            this.map = new TreeMap();
        }

        this.map.putAll(map);
    }

    private static Map getMap(Class<?> objectClass) {
        // Since we replace every enum map we might also replace some maps which are for real enums.
        // If this is the case use a EnumMap instead of a HashMap
        if (objectClass.isEnum()) {
            return new EnumMap(objectClass);
        } else {
            return new HashMap();
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        typeCheck(key);
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends Object, ?> m) {
        if (map instanceof EnumMap<?, ?>) {
            map.putAll(m);
        }

        super.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<Object> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public ImposterEnumMap clone() {
        ImposterEnumMap enumMap = new ImposterEnumMap(objectClass);
        enumMap.putAll(map);
        return enumMap;
    }

    private void typeCheck(Object object) {
        if (objectClass != DummyEnum.class) {
            if (!objectClass.isAssignableFrom(object.getClass())) {
                throw new ClassCastException(object.getClass() + " != " + objectClass);
            }
        }
    }
}
