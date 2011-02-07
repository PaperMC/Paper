/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.minecraft.server;

/**
 *
 * @author Nathan
 */
public abstract class LongHash<V> {
    static long toLong(int msw, int lsw) {
        return ((long)msw << 32) + lsw - Integer.MIN_VALUE;
    }
    
    static int msw(long l) {
        return (int) (l >> 32);
    }
    
    static int lsw(long l) {
        return (int) (l & 0xFFFFFFFF) + Integer.MIN_VALUE;
    }

    public boolean containsKey(int msw, int lsw) {
        return containsKey(toLong(msw, lsw));
    }

    public void remove(int msw, int lsw) {
        remove(toLong(msw, lsw));
    }
    
    public abstract boolean containsKey(long key);
    
    public abstract void remove(long key);
}
