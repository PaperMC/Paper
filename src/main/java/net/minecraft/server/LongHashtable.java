/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.minecraft.server;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Nathan
 */
public class LongHashtable<V> extends LongHash {
    Object values[][][] = new Object[256][][];
    Entry cache = null;
    
    public void put(int msw, int lsw, V value) {
        put(toLong(msw, lsw), value);
    }

    public V get(int msw, int lsw) {
        return get(toLong(msw, lsw));
    }

    public void put(long key, V value) {
        int idx1 = (int) (key & 255);
        int idx2 = (int) ((key >> 32) & 255);
        Object obj1[][] = values[idx1], obj2[];
        if(obj1 == null) values[idx1] = obj1 = new Object[256][];
        obj2 = obj1[idx2];
        if(obj2 == null) {
            obj1[idx2] = obj2 = new Object[5];
            obj2[0] = cache = new Entry(key, value);
        }
        else {
            int i;
            for(i = 0; i < obj2.length; i++) {
                if(obj2[i] == null || ((Entry)obj2[i]).key == key) {
                    obj2[i] = cache = new Entry(key, value);
                    return;
                }
            }
            obj2 = Arrays.copyOf(obj2, i+i);
            obj2[i] = new Entry(key, value);
        }
    }
    
    public V get(long key) {
        return containsKey(key) ? (V) cache.value : null;
    }
    
    public boolean containsKey(long key) {
        if(cache != null && cache.key == key) return true;
        int idx1 = (int) (key & 255);
        int idx2 = (int) ((key >> 32) & 255);
        Object obj1[][] = values[idx1], obj2[];
        if(obj1 == null) return false;
        obj2 = obj1[idx2];
        if(obj2 == null) return false;
        else {
            for(int i = 0; i < obj2.length; i++) {
                Entry e = (Entry)obj2[i];
                if(e == null) return false;
                else if(e.key == key) {
                    cache = e;
                    return true;
                }
            }
            return false;
        }
    }
    
    public void remove(long key) {
        
    }
    public ArrayList<V> values() {
        ArrayList<V> ret = new ArrayList<V>();
        for(Object[][] outer : values) {
            if(outer == null) continue;
            for(Object[] inner : outer) {
                if(inner == null) continue;
                for(Object entry : inner) {
                    if(entry == null) break;
                    ret.add((V)((Entry)entry).value);
                }
            }
        }
        return ret;
    }
    
    private class Entry {
        long key;
        Object value;
        Entry(long k, Object v) {
            key = k;
            value = v;
        }
    }    
}
