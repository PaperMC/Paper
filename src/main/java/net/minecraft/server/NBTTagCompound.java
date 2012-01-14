package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException; // CraftBukkit
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level; // CraftBukkit
import java.util.logging.Logger; // CraftBukkit

public class NBTTagCompound extends NBTBase {

    private Map map = new HashMap();

    public NBTTagCompound() {
        super("");
    }

    public NBTTagCompound(String s) {
        super(s);
    }

    void write(DataOutput dataoutput) {
        Iterator iterator = this.map.values().iterator();

        while (iterator.hasNext()) {
            NBTBase nbtbase = (NBTBase) iterator.next();

            NBTBase.a(nbtbase, dataoutput);
        }

        // CraftBukkit start
        try {
            dataoutput.writeByte(0);
        } catch (IOException ex) {
            Logger.getLogger(NBTTagCompound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void remove(String name) {
        map.remove(name);
    }

    // CraftBukkit end

    void load(DataInput datainput) {
        this.map.clear();

        NBTBase nbtbase;

        while ((nbtbase = NBTBase.b(datainput)).getTypeId() != 0) {
            this.map.put(nbtbase.getName(), nbtbase);
        }
    }

    public Collection d() {
        return this.map.values();
    }

    public byte getTypeId() {
        return (byte) 10;
    }

    public void set(String s, NBTBase nbtbase) {
        this.map.put(s, nbtbase.setName(s));
    }

    public void setByte(String s, byte b0) {
        this.map.put(s, new NBTTagByte(s, b0));
    }

    public void setShort(String s, short short1) {
        this.map.put(s, new NBTTagShort(s, short1));
    }

    public void setInt(String s, int i) {
        this.map.put(s, new NBTTagInt(s, i));
    }

    public void setLong(String s, long i) {
        this.map.put(s, new NBTTagLong(s, i));
    }

    public void setFloat(String s, float f) {
        this.map.put(s, new NBTTagFloat(s, f));
    }

    public void setDouble(String s, double d0) {
        this.map.put(s, new NBTTagDouble(s, d0));
    }

    public void setString(String s, String s1) {
        this.map.put(s, new NBTTagString(s, s1));
    }

    public void setByteArray(String s, byte[] abyte) {
        this.map.put(s, new NBTTagByteArray(s, abyte));
    }

    public void setCompound(String s, NBTTagCompound nbttagcompound) {
        this.map.put(s, nbttagcompound.setName(s));
    }

    public void setBoolean(String s, boolean flag) {
        this.setByte(s, (byte) (flag ? 1 : 0));
    }

    public NBTBase get(String s) {
        return (NBTBase) this.map.get(s);
    }

    public boolean hasKey(String s) {
        return this.map.containsKey(s);
    }

    public byte getByte(String s) {
        return !this.map.containsKey(s) ? 0 : ((NBTTagByte) this.map.get(s)).data;
    }

    public short getShort(String s) {
        return !this.map.containsKey(s) ? 0 : ((NBTTagShort) this.map.get(s)).data;
    }

    public int getInt(String s) {
        return !this.map.containsKey(s) ? 0 : ((NBTTagInt) this.map.get(s)).data;
    }

    public long getLong(String s) {
        return !this.map.containsKey(s) ? 0L : ((NBTTagLong) this.map.get(s)).data;
    }

    public float getFloat(String s) {
        return !this.map.containsKey(s) ? 0.0F : ((NBTTagFloat) this.map.get(s)).data;
    }

    public double getDouble(String s) {
        return !this.map.containsKey(s) ? 0.0D : ((NBTTagDouble) this.map.get(s)).data;
    }

    public String getString(String s) {
        return !this.map.containsKey(s) ? "" : ((NBTTagString) this.map.get(s)).data;
    }

    public byte[] getByteArray(String s) {
        return !this.map.containsKey(s) ? new byte[0] : ((NBTTagByteArray) this.map.get(s)).data;
    }

    public NBTTagCompound getCompound(String s) {
        return !this.map.containsKey(s) ? new NBTTagCompound(s) : (NBTTagCompound) this.map.get(s);
    }

    public NBTTagList getList(String s) {
        return !this.map.containsKey(s) ? new NBTTagList(s) : (NBTTagList) this.map.get(s);
    }

    public boolean getBoolean(String s) {
        return this.getByte(s) != 0;
    }

    public String toString() {
        return "" + this.map.size() + " entries";
    }

    public NBTBase clone() {
        NBTTagCompound nbttagcompound = new NBTTagCompound(this.getName());
        Iterator iterator = this.map.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            nbttagcompound.set(s, ((NBTBase) this.map.get(s)).clone());
        }

        return nbttagcompound;
    }

    public boolean equals(Object object) {
        if (super.equals(object)) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) object;

            return this.map.entrySet().equals(nbttagcompound.map.entrySet());
        } else {
            return false;
        }
    }
}
