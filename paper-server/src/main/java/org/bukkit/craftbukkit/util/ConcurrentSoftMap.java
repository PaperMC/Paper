package org.bukkit.craftbukkit.util;

import java.util.Map;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * Creates a map that uses soft reference.  This indicates to the garbage collector
 * that they can be removed if necessary
 *
 * A minimum number of strong references can be set.  These most recent N objects added
 * to the map will not be removed by the garbage collector.
 *
 * Objects will never be removed if they are referenced strongly from somewhere else

 * Note: While data corruption won't happen, the garbage collector is potentially async
 *       This could lead to the return values from containsKey() and similar methods being
 *       out of date by the time they are used.  The class could return null when the object
 *       is retrieved by a .get() call directly after a .containsKey() call returned true
 *
 * @author raphfrk
 */

public class ConcurrentSoftMap<K,V> {

    private final ConcurrentHashMap<K,SoftMapReference<K,V>> map = new ConcurrentHashMap<K,SoftMapReference<K,V>>();
    private final ReferenceQueue<SoftMapReference> queue = new ReferenceQueue<SoftMapReference>();
    private final LinkedList<V> strongReferenceQueue = new LinkedList<V>();
    private final int strongReferenceSize;

    public ConcurrentSoftMap() {
        this(20);
    }

    public ConcurrentSoftMap(int size) {
        strongReferenceSize = size;
    }

    // When a soft reference is deleted by the garbage collector, it is set to reference null
    // and added to the queue
    //
    // However, these null references still exist in the ConcurrentHashMap as keys.  This method removes these keys.
    //
    // It is called whenever there is a method call of the map.

    private void emptyQueue() {
        SoftMapReference ref;
        while ((ref=(SoftMapReference) queue.poll()) != null) {
            map.remove(ref.key);
        }
    }

    public void clear() {
         synchronized(strongReferenceQueue) {
             strongReferenceQueue.clear();
         }
         map.clear();
         emptyQueue();
    }

    // Shouldn't support this, since the garbage collection is async

    public boolean containsKey(K key) {
        emptyQueue();
        return map.containsKey(key);
    }

    // Shouldn't support this, since the garbage collection is async

    public boolean containsValue(V value) {
        emptyQueue();
        return map.containsValue(value);
    }

    // Shouldn't support this since it would create strong references to all the entries

    public Set entrySet() {
        emptyQueue();
        throw new UnsupportedOperationException("SoftMap does not support this operation, since it creates potentially stong references");
    }

    // Doesn't support these either

    public boolean equals(Object o) {
         emptyQueue();
         throw new UnsupportedOperationException("SoftMap doesn't support equals checks");
    }

    // This operation returns null if the entry is not in the map

    public V get(K key) {
        emptyQueue();
        return fastGet(key);
    }

    private V fastGet(K key) {
        SoftMapReference<K,V> ref = map.get(key);
        if (ref==null) {
            return null;
        }
        V value = ref.get();
        if (value!=null) {
            synchronized(strongReferenceQueue) {
                strongReferenceQueue.addFirst(value);
                if (strongReferenceQueue.size() > strongReferenceSize) {
                    strongReferenceQueue.removeLast();
                }
            }
        }
        return value;
    }

    // Doesn't support this either

    public int hashCode() {
         emptyQueue();
         throw new UnsupportedOperationException("SoftMap doesn't support hashCode");
    }

    // This is another risky method, since again, garbage collection is async

    public boolean isEmpty() {
        emptyQueue();
        return map.isEmpty();
    }

    // Return all the keys, again could go out of date

    public Set keySet() {
        emptyQueue();
        return map.keySet();
    }

    // Adds the mapping to the map

    public V put(K key, V value) {
        emptyQueue();
        V old = fastGet(key);
        fastPut(key, value);
        return old;
    }

    private void fastPut(K key, V value) {
        map.put(key, new SoftMapReference<K,V>(key, value, queue));
        synchronized(strongReferenceQueue) {
            strongReferenceQueue.addFirst(value);
            if (strongReferenceQueue.size() > strongReferenceSize) {
                strongReferenceQueue.removeLast();
            }
        }
    }

    public V putIfAbsent(K key, V value) {
        emptyQueue();
        return fastPutIfAbsent(key, value);
    }

     private V fastPutIfAbsent(K key, V value) {
        V ret = null;

        if (map.containsKey(key)) {
             SoftMapReference<K,V> current = map.get(key);
             if (current != null) {
                 ret = current.get();
             }
        }

        if (ret == null) {
            SoftMapReference<K,V> newValue = new SoftMapReference<K,V>(key, value, queue);
            boolean success = false;
            while (!success) {
                SoftMapReference<K,V> oldValue = map.putIfAbsent(key, newValue);

                if (oldValue == null) { // put was successful (key didn't exist)
                    ret = null;
                    success = true;
                } else {
                    ret = oldValue.get();
                    if (ret == null) { // key existed, but referenced null
                        success = map.replace(key, oldValue, newValue); // try to swap old for new
                    } else { // key existed, and referenced a valid object
                        success = true;
                    }
                }
            }
        }

        if (ret == null) {
            synchronized(strongReferenceQueue) {
                strongReferenceQueue.addFirst(value);
                if (strongReferenceQueue.size() > strongReferenceSize) {
                    strongReferenceQueue.removeLast();
                }
            }
        }

        return ret;
    }

    // Adds the mappings to the map

    public void putAll(Map other) {
        emptyQueue();
        Iterator<K> itr = other.keySet().iterator();
        while (itr.hasNext()) {
            K key = itr.next();
            fastPut(key, (V) other.get(key));
        }
    }

    // Remove object

    public V remove(K key) {
        emptyQueue();
        SoftMapReference<K,V> ref = map.remove(key);
        if (ref != null) {
            return ref.get();
        }
        return null;
    }

    // Returns size, could go out of date

    public int size() {
        emptyQueue();
        return map.size();
    }

    // Shouldn't support this since it would create strong references to all the entries

    public Collection values() {
        emptyQueue();
        throw new UnsupportedOperationException("SoftMap does not support this operation, since it creates potentially stong references");
    }


    private static class SoftMapReference<K,V> extends SoftReference<V> {
        K key;

        SoftMapReference(K key, V value, ReferenceQueue queue) {
            super(value, queue);
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof SoftMapReference)) {
                return false;
            }
            SoftMapReference other = (SoftMapReference) o;
            return other.get() == get();
        }
    }
}
