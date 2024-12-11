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
        this.map = ImposterEnumMap.getMap(objectClass);
    }

    public ImposterEnumMap(EnumMap enumMap) {
        this.objectClass = DummyEnum.class;
        this.map = enumMap.clone();
    }

    public ImposterEnumMap(Map map) {
        if (map instanceof ImposterEnumMap) {
            this.objectClass = ((ImposterEnumMap) map).objectClass;
            this.map = ImposterEnumMap.getMap(this.objectClass);
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
        return this.map.size();
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public Object get(Object key) {
        return this.map.get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        this.typeCheck(key);
        return this.map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public void putAll(Map<? extends Object, ?> m) {
        if (this.map instanceof EnumMap<?, ?>) {
            this.map.putAll(m);
        }

        super.putAll(m);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<Object> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return this.map.values();
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return this.map.equals(o);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public ImposterEnumMap clone() {
        ImposterEnumMap enumMap = new ImposterEnumMap(this.objectClass);
        enumMap.putAll(this.map);
        return enumMap;
    }

    private void typeCheck(Object object) {
        if (this.objectClass != DummyEnum.class) {
            if (!this.objectClass.isAssignableFrom(object.getClass())) {
                throw new ClassCastException(object.getClass() + " != " + this.objectClass);
            }
        }
    }
}
