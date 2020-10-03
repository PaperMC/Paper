package net.minecraft.server;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagCompound implements NBTBase {

    public static final Codec<NBTTagCompound> a = Codec.PASSTHROUGH.comapFlatMap((dynamic) -> {
        NBTBase nbtbase = (NBTBase) dynamic.convert(DynamicOpsNBT.a).getValue();

        return nbtbase instanceof NBTTagCompound ? DataResult.success((NBTTagCompound) nbtbase) : DataResult.error("Not a compound tag: " + nbtbase);
    }, (nbttagcompound) -> {
        return new Dynamic(DynamicOpsNBT.a, nbttagcompound);
    });
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern h = Pattern.compile("[A-Za-z0-9._+-]+");
    public static final NBTTagType<NBTTagCompound> b = new NBTTagType<NBTTagCompound>() {
        @Override
        public NBTTagCompound b(DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) throws IOException {
            nbtreadlimiter.a(384L);
            if (i > 512) {
                throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
            } else {
                HashMap hashmap = Maps.newHashMap();

                byte b0;

                while ((b0 = NBTTagCompound.c(datainput, nbtreadlimiter)) != 0) {
                    String s = NBTTagCompound.d(datainput, nbtreadlimiter);

                    nbtreadlimiter.a((long) (224 + 16 * s.length()));
                    NBTBase nbtbase = NBTTagCompound.b(NBTTagTypes.a(b0), s, datainput, i + 1, nbtreadlimiter);

                    if (hashmap.put(s, nbtbase) != null) {
                        nbtreadlimiter.a(288L);
                    }
                }

                return new NBTTagCompound(hashmap);
            }
        }

        @Override
        public String a() {
            return "COMPOUND";
        }

        @Override
        public String b() {
            return "TAG_Compound";
        }
    };
    private final Map<String, NBTBase> map;

    protected NBTTagCompound(Map<String, NBTBase> map) {
        this.map = map;
    }

    public NBTTagCompound() {
        this(Maps.newHashMap());
    }

    @Override
    public void write(DataOutput dataoutput) throws IOException {
        Iterator iterator = this.map.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            NBTBase nbtbase = (NBTBase) this.map.get(s);

            a(s, nbtbase, dataoutput);
        }

        dataoutput.writeByte(0);
    }

    public Set<String> getKeys() {
        return this.map.keySet();
    }

    @Override
    public byte getTypeId() {
        return 10;
    }

    @Override
    public NBTTagType<NBTTagCompound> b() {
        return NBTTagCompound.b;
    }

    public int e() {
        return this.map.size();
    }

    @Nullable
    public NBTBase set(String s, NBTBase nbtbase) {
        return (NBTBase) this.map.put(s, nbtbase);
    }

    public void setByte(String s, byte b0) {
        this.map.put(s, NBTTagByte.a(b0));
    }

    public void setShort(String s, short short0) {
        this.map.put(s, NBTTagShort.a(short0));
    }

    public void setInt(String s, int i) {
        this.map.put(s, NBTTagInt.a(i));
    }

    public void setLong(String s, long i) {
        this.map.put(s, NBTTagLong.a(i));
    }

    public void a(String s, UUID uuid) {
        this.map.put(s, GameProfileSerializer.a(uuid));
    }

    public UUID a(String s) {
        return GameProfileSerializer.a(this.get(s));
    }

    public boolean b(String s) {
        NBTBase nbtbase = this.get(s);

        return nbtbase != null && nbtbase.b() == NBTTagIntArray.a && ((NBTTagIntArray) nbtbase).getInts().length == 4;
    }

    public void setFloat(String s, float f) {
        this.map.put(s, NBTTagFloat.a(f));
    }

    public void setDouble(String s, double d0) {
        this.map.put(s, NBTTagDouble.a(d0));
    }

    public void setString(String s, String s1) {
        this.map.put(s, NBTTagString.a(s1));
    }

    public void setByteArray(String s, byte[] abyte) {
        this.map.put(s, new NBTTagByteArray(abyte));
    }

    public void setIntArray(String s, int[] aint) {
        this.map.put(s, new NBTTagIntArray(aint));
    }

    public void b(String s, List<Integer> list) {
        this.map.put(s, new NBTTagIntArray(list));
    }

    public void a(String s, long[] along) {
        this.map.put(s, new NBTTagLongArray(along));
    }

    public void c(String s, List<Long> list) {
        this.map.put(s, new NBTTagLongArray(list));
    }

    public void setBoolean(String s, boolean flag) {
        this.map.put(s, NBTTagByte.a(flag));
    }

    @Nullable
    public NBTBase get(String s) {
        return (NBTBase) this.map.get(s);
    }

    public byte d(String s) {
        NBTBase nbtbase = (NBTBase) this.map.get(s);

        return nbtbase == null ? 0 : nbtbase.getTypeId();
    }

    public boolean hasKey(String s) {
        return this.map.containsKey(s);
    }

    public boolean hasKeyOfType(String s, int i) {
        byte b0 = this.d(s);

        return b0 == i ? true : (i != 99 ? false : b0 == 1 || b0 == 2 || b0 == 3 || b0 == 4 || b0 == 5 || b0 == 6);
    }

    public byte getByte(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asByte();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0;
    }

    public short getShort(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asShort();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0;
    }

    public int getInt(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asInt();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0;
    }

    public long getLong(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asLong();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0L;
    }

    public float getFloat(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asFloat();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0.0F;
    }

    public double getDouble(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asDouble();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0.0D;
    }

    public String getString(String s) {
        try {
            if (this.hasKeyOfType(s, 8)) {
                return ((NBTBase) this.map.get(s)).asString();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return "";
    }

    public byte[] getByteArray(String s) {
        try {
            if (this.hasKeyOfType(s, 7)) {
                return ((NBTTagByteArray) this.map.get(s)).getBytes();
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, NBTTagByteArray.a, classcastexception));
        }

        return new byte[0];
    }

    public int[] getIntArray(String s) {
        try {
            if (this.hasKeyOfType(s, 11)) {
                return ((NBTTagIntArray) this.map.get(s)).getInts();
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, NBTTagIntArray.a, classcastexception));
        }

        return new int[0];
    }

    public long[] getLongArray(String s) {
        try {
            if (this.hasKeyOfType(s, 12)) {
                return ((NBTTagLongArray) this.map.get(s)).getLongs();
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, NBTTagLongArray.a, classcastexception));
        }

        return new long[0];
    }

    public NBTTagCompound getCompound(String s) {
        try {
            if (this.hasKeyOfType(s, 10)) {
                return (NBTTagCompound) this.map.get(s);
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, NBTTagCompound.b, classcastexception));
        }

        return new NBTTagCompound();
    }

    public NBTTagList getList(String s, int i) {
        try {
            if (this.d(s) == 9) {
                NBTTagList nbttaglist = (NBTTagList) this.map.get(s);

                if (!nbttaglist.isEmpty() && nbttaglist.d_() != i) {
                    return new NBTTagList();
                }

                return nbttaglist;
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, NBTTagList.a, classcastexception));
        }

        return new NBTTagList();
    }

    public boolean getBoolean(String s) {
        return this.getByte(s) != 0;
    }

    public void remove(String s) {
        this.map.remove(s);
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("{");
        Collection<String> collection = this.map.keySet();

        if (NBTTagCompound.LOGGER.isDebugEnabled()) {
            List<String> list = Lists.newArrayList(this.map.keySet());

            Collections.sort(list);
            collection = list;
        }

        String s;

        for (Iterator iterator = ((Collection) collection).iterator(); iterator.hasNext(); stringbuilder.append(s(s)).append(':').append(this.map.get(s))) {
            s = (String) iterator.next();
            if (stringbuilder.length() != 1) {
                stringbuilder.append(',');
            }
        }

        return stringbuilder.append('}').toString();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    private CrashReport a(String s, NBTTagType<?> nbttagtype, ClassCastException classcastexception) {
        CrashReport crashreport = CrashReport.a(classcastexception, "Reading NBT data");
        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Corrupt NBT tag", 1);

        crashreportsystemdetails.a("Tag type found", () -> {
            return ((NBTBase) this.map.get(s)).b().a();
        });
        crashreportsystemdetails.a("Tag type expected", nbttagtype::a);
        crashreportsystemdetails.a("Tag name", (Object) s);
        return crashreport;
    }

    @Override
    public NBTTagCompound clone() {
        Map<String, NBTBase> map = Maps.newHashMap(Maps.transformValues(this.map, NBTBase::clone));

        return new NBTTagCompound(map);
    }

    public boolean equals(Object object) {
        return this == object ? true : object instanceof NBTTagCompound && Objects.equals(this.map, ((NBTTagCompound) object).map);
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    private static void a(String s, NBTBase nbtbase, DataOutput dataoutput) throws IOException {
        dataoutput.writeByte(nbtbase.getTypeId());
        if (nbtbase.getTypeId() != 0) {
            dataoutput.writeUTF(s);
            nbtbase.write(dataoutput);
        }
    }

    private static byte c(DataInput datainput, NBTReadLimiter nbtreadlimiter) throws IOException {
        return datainput.readByte();
    }

    private static String d(DataInput datainput, NBTReadLimiter nbtreadlimiter) throws IOException {
        return datainput.readUTF();
    }

    private static NBTBase b(NBTTagType<?> nbttagtype, String s, DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) {
        try {
            return nbttagtype.b(datainput, i, nbtreadlimiter);
        } catch (IOException ioexception) {
            CrashReport crashreport = CrashReport.a(ioexception, "Loading NBT data");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("NBT Tag");

            crashreportsystemdetails.a("Tag name", (Object) s);
            crashreportsystemdetails.a("Tag type", (Object) nbttagtype.a());
            throw new ReportedException(crashreport);
        }
    }

    public NBTTagCompound a(NBTTagCompound nbttagcompound) {
        Iterator iterator = nbttagcompound.map.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            NBTBase nbtbase = (NBTBase) nbttagcompound.map.get(s);

            if (nbtbase.getTypeId() == 10) {
                if (this.hasKeyOfType(s, 10)) {
                    NBTTagCompound nbttagcompound1 = this.getCompound(s);

                    nbttagcompound1.a((NBTTagCompound) nbtbase);
                } else {
                    this.set(s, nbtbase.clone());
                }
            } else {
                this.set(s, nbtbase.clone());
            }
        }

        return this;
    }

    protected static String s(String s) {
        return NBTTagCompound.h.matcher(s).matches() ? s : NBTTagString.b(s);
    }

    protected static IChatBaseComponent t(String s) {
        if (NBTTagCompound.h.matcher(s).matches()) {
            return (new ChatComponentText(s)).a(NBTTagCompound.d);
        } else {
            String s1 = NBTTagString.b(s);
            String s2 = s1.substring(0, 1);
            IChatMutableComponent ichatmutablecomponent = (new ChatComponentText(s1.substring(1, s1.length() - 1))).a(NBTTagCompound.d);

            return (new ChatComponentText(s2)).addSibling(ichatmutablecomponent).c(s2);
        }
    }

    @Override
    public IChatBaseComponent a(String s, int i) {
        if (this.map.isEmpty()) {
            return new ChatComponentText("{}");
        } else {
            ChatComponentText chatcomponenttext = new ChatComponentText("{");
            Collection<String> collection = this.map.keySet();

            if (NBTTagCompound.LOGGER.isDebugEnabled()) {
                List<String> list = Lists.newArrayList(this.map.keySet());

                Collections.sort(list);
                collection = list;
            }

            if (!s.isEmpty()) {
                chatcomponenttext.c("\n");
            }

            IChatMutableComponent ichatmutablecomponent;

            for (Iterator iterator = ((Collection) collection).iterator(); iterator.hasNext(); chatcomponenttext.addSibling(ichatmutablecomponent)) {
                String s1 = (String) iterator.next();

                ichatmutablecomponent = (new ChatComponentText(Strings.repeat(s, i + 1))).addSibling(t(s1)).c(String.valueOf(':')).c(" ").addSibling(((NBTBase) this.map.get(s1)).a(s, i + 1));
                if (iterator.hasNext()) {
                    ichatmutablecomponent.c(String.valueOf(',')).c(s.isEmpty() ? " " : "\n");
                }
            }

            if (!s.isEmpty()) {
                chatcomponenttext.c("\n").c(Strings.repeat(s, i));
            }

            chatcomponenttext.c("}");
            return chatcomponenttext;
        }
    }

    protected Map<String, NBTBase> h() {
        return Collections.unmodifiableMap(this.map);
    }
}
