package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

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
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
        // CraftBukkit end
    }

    void load(DataInput datainput) {
        this.map.clear();

        NBTBase nbtbase;

        while ((nbtbase = NBTBase.b(datainput)).getTypeId() != 0) {
            this.map.put(nbtbase.getName(), nbtbase);
        }
    }

    public Collection c() {
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

    public void setIntArray(String s, int[] aint) {
        this.map.put(s, new NBTTagIntArray(s, aint));
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
        try {
            return !this.map.containsKey(s) ? 0 : ((NBTTagByte) this.map.get(s)).data;
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 1, classcastexception));
        }
    }

    public short getShort(String s) {
        try {
            return !this.map.containsKey(s) ? 0 : ((NBTTagShort) this.map.get(s)).data;
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 2, classcastexception));
        }
    }

    public int getInt(String s) {
        try {
            return !this.map.containsKey(s) ? 0 : ((NBTTagInt) this.map.get(s)).data;
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 3, classcastexception));
        }
    }

    public long getLong(String s) {
        try {
            return !this.map.containsKey(s) ? 0L : ((NBTTagLong) this.map.get(s)).data;
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 4, classcastexception));
        }
    }

    public float getFloat(String s) {
        try {
            return !this.map.containsKey(s) ? 0.0F : ((NBTTagFloat) this.map.get(s)).data;
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 5, classcastexception));
        }
    }

    public double getDouble(String s) {
        try {
            return !this.map.containsKey(s) ? 0.0D : ((NBTTagDouble) this.map.get(s)).data;
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 6, classcastexception));
        }
    }

    public String getString(String s) {
        try {
            return !this.map.containsKey(s) ? "" : ((NBTTagString) this.map.get(s)).data;
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 8, classcastexception));
        }
    }

    public byte[] getByteArray(String s) {
        try {
            return !this.map.containsKey(s) ? new byte[0] : ((NBTTagByteArray) this.map.get(s)).data;
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 7, classcastexception));
        }
    }

    public int[] getIntArray(String s) {
        try {
            return !this.map.containsKey(s) ? new int[0] : ((NBTTagIntArray) this.map.get(s)).data;
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 11, classcastexception));
        }
    }

    public NBTTagCompound getCompound(String s) {
        try {
            return !this.map.containsKey(s) ? new NBTTagCompound(s) : (NBTTagCompound) this.map.get(s);
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 10, classcastexception));
        }
    }

    public NBTTagList getList(String s) {
        try {
            return !this.map.containsKey(s) ? new NBTTagList(s) : (NBTTagList) this.map.get(s);
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 9, classcastexception));
        }
    }

    public boolean getBoolean(String s) {
        return this.getByte(s) != 0;
    }

    public void o(String s) {
        this.map.remove(s);
    }

    public String toString() {
        return "" + this.map.size() + " entries";
    }

    public boolean d() {
        return this.map.isEmpty();
    }

    private CrashReport a(String s, int i, ClassCastException classcastexception) {
        CrashReport crashreport = CrashReport.a(classcastexception, "Reading NBT data");
        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Corrupt NBT tag", 1);

        crashreportsystemdetails.a("Tag type found", (Callable) (new CrashReportCorruptNBTTag(this, s)));
        crashreportsystemdetails.a("Tag type expected", (Callable) (new CrashReportCorruptNBTTag2(this, i)));
        crashreportsystemdetails.a("Tag name", s);
        if (this.getName() != null && this.getName().length() > 0) {
            crashreportsystemdetails.a("Tag parent", this.getName());
        }

        return crashreport;
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

    public int hashCode() {
        return super.hashCode() ^ this.map.hashCode();
    }

    static Map a(NBTTagCompound nbttagcompound) {
        return nbttagcompound.map;
    }
}
