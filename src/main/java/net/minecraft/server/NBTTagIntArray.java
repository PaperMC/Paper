package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class NBTTagIntArray extends NBTList<NBTTagInt> {

    public static final NBTTagType<NBTTagIntArray> a = new NBTTagType<NBTTagIntArray>() {
        @Override
        public NBTTagIntArray b(DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) throws IOException {
            nbtreadlimiter.a(192L);
            int j = datainput.readInt();

            nbtreadlimiter.a(32L * (long) j);
            int[] aint = new int[j];

            for (int k = 0; k < j; ++k) {
                aint[k] = datainput.readInt();
            }

            return new NBTTagIntArray(aint);
        }

        @Override
        public String a() {
            return "INT[]";
        }

        @Override
        public String b() {
            return "TAG_Int_Array";
        }
    };
    private int[] data;

    public NBTTagIntArray(int[] aint) {
        this.data = aint;
    }

    public NBTTagIntArray(List<Integer> list) {
        this(a(list));
    }

    private static int[] a(List<Integer> list) {
        int[] aint = new int[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Integer integer = (Integer) list.get(i);

            aint[i] = integer == null ? 0 : integer;
        }

        return aint;
    }

    @Override
    public void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeInt(this.data.length);
        int[] aint = this.data;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint[j];

            dataoutput.writeInt(k);
        }

    }

    @Override
    public byte getTypeId() {
        return 11;
    }

    @Override
    public NBTTagType<NBTTagIntArray> b() {
        return NBTTagIntArray.a;
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[I;");

        for (int i = 0; i < this.data.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.data[i]);
        }

        return stringbuilder.append(']').toString();
    }

    @Override
    public NBTTagIntArray clone() {
        int[] aint = new int[this.data.length];

        System.arraycopy(this.data, 0, aint, 0, this.data.length);
        return new NBTTagIntArray(aint);
    }

    public boolean equals(Object object) {
        return this == object ? true : object instanceof NBTTagIntArray && Arrays.equals(this.data, ((NBTTagIntArray) object).data);
    }

    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    public int[] getInts() {
        return this.data;
    }

    @Override
    public IChatBaseComponent a(String s, int i) {
        IChatMutableComponent ichatmutablecomponent = (new ChatComponentText("I")).a(NBTTagIntArray.g);
        IChatMutableComponent ichatmutablecomponent1 = (new ChatComponentText("[")).addSibling(ichatmutablecomponent).c(";");

        for (int j = 0; j < this.data.length; ++j) {
            ichatmutablecomponent1.c(" ").addSibling((new ChatComponentText(String.valueOf(this.data[j]))).a(NBTTagIntArray.f));
            if (j != this.data.length - 1) {
                ichatmutablecomponent1.c(",");
            }
        }

        ichatmutablecomponent1.c("]");
        return ichatmutablecomponent1;
    }

    public int size() {
        return this.data.length;
    }

    public NBTTagInt get(int i) {
        return NBTTagInt.a(this.data[i]);
    }

    public NBTTagInt set(int i, NBTTagInt nbttagint) {
        int j = this.data[i];

        this.data[i] = nbttagint.asInt();
        return NBTTagInt.a(j);
    }

    public void add(int i, NBTTagInt nbttagint) {
        this.data = ArrayUtils.add(this.data, i, nbttagint.asInt());
    }

    @Override
    public boolean a(int i, NBTBase nbtbase) {
        if (nbtbase instanceof NBTNumber) {
            this.data[i] = ((NBTNumber) nbtbase).asInt();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean b(int i, NBTBase nbtbase) {
        if (nbtbase instanceof NBTNumber) {
            this.data = ArrayUtils.add(this.data, i, ((NBTNumber) nbtbase).asInt());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public NBTTagInt remove(int i) {
        int j = this.data[i];

        this.data = ArrayUtils.remove(this.data, i);
        return NBTTagInt.a(j);
    }

    @Override
    public byte d_() {
        return 3;
    }

    public void clear() {
        this.data = new int[0];
    }
}
