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
public class LongHashset<V> extends LongHash<V> {
    long values[][][] = new long[256][][];
    int count = 0;
    
    public boolean isEmpty() {
        return count == 0;
    }

    public void add(long key, V value) {
        int idx1 = (int) (key & 255);
        int idx2 = (int) ((key >> 32) & 255);
        long obj1[][] = values[idx1], obj2[];
        if(obj1 == null) values[idx1] = obj1 = new long[256][];
        obj2 = obj1[idx2];
        if(obj2 == null) {
            obj1[idx2] = obj2 = new long[1];
            obj2[0] = key;
            count++;
        }
        else {
            int i;
            for(i = 0; i < obj2.length; i++) {
                if(obj2[i] == key) {
                    return;
                }
            }
            obj2 = Arrays.copyOf(obj2, i+1);
            obj2[i] = key;
            count++;
        }
    }
    
    public boolean containsKey(long key) {
        int idx1 = (int) (key & 255);
        int idx2 = (int) ((key >> 32) & 255);
        long obj1[][] = values[idx1], obj2[];
        if(obj1 == null) return false;
        obj2 = obj1[idx2];
        if(obj2 == null) return false;
        else {
            for(long entry : obj2) {
                if(entry == key) return true;
            }
            return false;
        }
    }
    
    public void remove(long key) {
        int idx1 = (int) (key & 255);
        int idx2 = (int) ((key >> 32) & 255);
        long obj1[][] = values[idx1], obj2[];
        if(obj1 == null) return;
        obj2 = obj1[idx2];
        if(obj2 == null) return;
        else {
            int max = obj2.length - 1;
            for(int i = 0; i <= max; i++) {
                if(obj2[i] == key) {
                    count--;
                    if(i != max) {
                        obj2[i] = obj2[max];
                    }
                    obj2 = Arrays.copyOf(obj2, max);
                }
            }
        }        
    }
    
    public long popFirst() {
        for(long[][] outer : values) {
            if(outer == null) continue;
            for(long[] inner : outer) {
                if(inner == null) continue;
                long ret = inner[inner.length - 1];
                inner = Arrays.copyOf(inner, inner.length - 1);
                return ret;
            }
        }
        return 0;
    }

    public long[] keys() {
        int index = 0;
        long ret[] = new long[count];
        for(long[][] outer : values) {
            if(outer == null) continue;
            for(long[] inner : outer) {
                if(inner == null) continue;
                for(long entry : inner) {
                    ret[index++] = entry;
                }
            }
        }
        return ret;
    }
}