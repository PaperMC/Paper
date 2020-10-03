package net.minecraft.server;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldPersistentData {

    private static final Logger LOGGER = LogManager.getLogger();
    public final Map<String, PersistentBase> data = Maps.newHashMap();
    private final DataFixer c;
    private final File d;

    public WorldPersistentData(File file, DataFixer datafixer) {
        this.c = datafixer;
        this.d = file;
    }

    private File a(String s) {
        return new File(this.d, s + ".dat");
    }

    public <T extends PersistentBase> T a(Supplier<T> supplier, String s) {
        T t0 = this.b(supplier, s);

        if (t0 != null) {
            return t0;
        } else {
            T t1 = (PersistentBase) supplier.get();

            this.a(t1);
            return t1;
        }
    }

    @Nullable
    public <T extends PersistentBase> T b(Supplier<T> supplier, String s) {
        PersistentBase persistentbase = (PersistentBase) this.data.get(s);

        if (persistentbase == null && !this.data.containsKey(s)) {
            persistentbase = this.c(supplier, s);
            this.data.put(s, persistentbase);
        }

        return persistentbase;
    }

    @Nullable
    private <T extends PersistentBase> T c(Supplier<T> supplier, String s) {
        try {
            File file = this.a(s);

            if (file.exists()) {
                T t0 = (PersistentBase) supplier.get();
                NBTTagCompound nbttagcompound = this.a(s, SharedConstants.getGameVersion().getWorldVersion());

                t0.a(nbttagcompound.getCompound("data"));
                return t0;
            }
        } catch (Exception exception) {
            WorldPersistentData.LOGGER.error("Error loading saved data: {}", s, exception);
        }

        return null;
    }

    public void a(PersistentBase persistentbase) {
        this.data.put(persistentbase.getId(), persistentbase);
    }

    public NBTTagCompound a(String s, int i) throws IOException {
        File file = this.a(s);
        FileInputStream fileinputstream = new FileInputStream(file);
        Throwable throwable = null;

        Object object;

        try {
            PushbackInputStream pushbackinputstream = new PushbackInputStream(fileinputstream, 2);
            Throwable throwable1 = null;

            try {
                NBTTagCompound nbttagcompound;

                if (this.a(pushbackinputstream)) {
                    nbttagcompound = NBTCompressedStreamTools.a((InputStream) pushbackinputstream);
                } else {
                    DataInputStream datainputstream = new DataInputStream(pushbackinputstream);

                    object = null;

                    try {
                        nbttagcompound = NBTCompressedStreamTools.a((DataInput) datainputstream);
                    } catch (Throwable throwable2) {
                        object = throwable2;
                        throw throwable2;
                    } finally {
                        if (datainputstream != null) {
                            if (object != null) {
                                try {
                                    datainputstream.close();
                                } catch (Throwable throwable3) {
                                    ((Throwable) object).addSuppressed(throwable3);
                                }
                            } else {
                                datainputstream.close();
                            }
                        }

                    }
                }

                int j = nbttagcompound.hasKeyOfType("DataVersion", 99) ? nbttagcompound.getInt("DataVersion") : 1343;

                object = GameProfileSerializer.a(this.c, DataFixTypes.SAVED_DATA, nbttagcompound, j, i);
            } catch (Throwable throwable4) {
                throwable1 = throwable4;
                throw throwable4;
            } finally {
                if (pushbackinputstream != null) {
                    if (throwable1 != null) {
                        try {
                            pushbackinputstream.close();
                        } catch (Throwable throwable5) {
                            throwable1.addSuppressed(throwable5);
                        }
                    } else {
                        pushbackinputstream.close();
                    }
                }

            }
        } catch (Throwable throwable6) {
            throwable = throwable6;
            throw throwable6;
        } finally {
            if (fileinputstream != null) {
                if (throwable != null) {
                    try {
                        fileinputstream.close();
                    } catch (Throwable throwable7) {
                        throwable.addSuppressed(throwable7);
                    }
                } else {
                    fileinputstream.close();
                }
            }

        }

        return (NBTTagCompound) object;
    }

    private boolean a(PushbackInputStream pushbackinputstream) throws IOException {
        byte[] abyte = new byte[2];
        boolean flag = false;
        int i = pushbackinputstream.read(abyte, 0, 2);

        if (i == 2) {
            int j = (abyte[1] & 255) << 8 | abyte[0] & 255;

            if (j == 35615) {
                flag = true;
            }
        }

        if (i != 0) {
            pushbackinputstream.unread(abyte, 0, i);
        }

        return flag;
    }

    public void a() {
        Iterator iterator = this.data.values().iterator();

        while (iterator.hasNext()) {
            PersistentBase persistentbase = (PersistentBase) iterator.next();

            if (persistentbase != null) {
                persistentbase.a(this.a(persistentbase.getId()));
            }
        }

    }
}
