package net.minecraft.server;

import java.util.function.Predicate;
import javax.annotation.Nullable;

public interface DataPalette<T> {

    int a(T t0);

    boolean a(Predicate<T> predicate);

    @Nullable
    T a(int i);

    void b(PacketDataSerializer packetdataserializer);

    int a();

    void a(NBTTagList nbttaglist);
}
