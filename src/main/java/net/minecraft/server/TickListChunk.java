package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TickListChunk<T> implements TickList<T> {

    private final List<TickListChunk.a<T>> a;
    private final Function<T, MinecraftKey> b;

    public TickListChunk(Function<T, MinecraftKey> function, List<NextTickListEntry<T>> list, long i) {
        this(function, (List) list.stream().map((nextticklistentry) -> {
            return new TickListChunk.a<>(nextticklistentry.b(), nextticklistentry.a, (int) (nextticklistentry.b - i), nextticklistentry.c);
        }).collect(Collectors.toList()));
    }

    private TickListChunk(Function<T, MinecraftKey> function, List<TickListChunk.a<T>> list) {
        this.a = list;
        this.b = function;
    }

    @Override
    public boolean a(BlockPosition blockposition, T t0) {
        return false;
    }

    @Override
    public void a(BlockPosition blockposition, T t0, int i, TickListPriority ticklistpriority) {
        this.a.add(new TickListChunk.a<>(t0, blockposition, i, ticklistpriority));
    }

    @Override
    public boolean b(BlockPosition blockposition, T t0) {
        return false;
    }

    public NBTTagList b() {
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.a.iterator();

        while (iterator.hasNext()) {
            TickListChunk.a<T> ticklistchunk_a = (TickListChunk.a) iterator.next();
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            nbttagcompound.setString("i", ((MinecraftKey) this.b.apply(ticklistchunk_a.d)).toString());
            nbttagcompound.setInt("x", ticklistchunk_a.a.getX());
            nbttagcompound.setInt("y", ticklistchunk_a.a.getY());
            nbttagcompound.setInt("z", ticklistchunk_a.a.getZ());
            nbttagcompound.setInt("t", ticklistchunk_a.b);
            nbttagcompound.setInt("p", ticklistchunk_a.c.a());
            nbttaglist.add(nbttagcompound);
        }

        return nbttaglist;
    }

    public static <T> TickListChunk<T> a(NBTTagList nbttaglist, Function<T, MinecraftKey> function, Function<MinecraftKey, T> function1) {
        List<TickListChunk.a<T>> list = Lists.newArrayList();

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
            T t0 = function1.apply(new MinecraftKey(nbttagcompound.getString("i")));

            if (t0 != null) {
                BlockPosition blockposition = new BlockPosition(nbttagcompound.getInt("x"), nbttagcompound.getInt("y"), nbttagcompound.getInt("z"));

                list.add(new TickListChunk.a<>(t0, blockposition, nbttagcompound.getInt("t"), TickListPriority.a(nbttagcompound.getInt("p"))));
            }
        }

        return new TickListChunk<>(function, list);
    }

    public void a(TickList<T> ticklist) {
        this.a.forEach((ticklistchunk_a) -> {
            ticklist.a(ticklistchunk_a.a, ticklistchunk_a.d, ticklistchunk_a.b, ticklistchunk_a.c);
        });
    }

    static class a<T> {

        private final T d;
        public final BlockPosition a;
        public final int b;
        public final TickListPriority c;

        private a(T t0, BlockPosition blockposition, int i, TickListPriority ticklistpriority) {
            this.d = t0;
            this.a = blockposition;
            this.b = i;
            this.c = ticklistpriority;
        }

        public String toString() {
            return this.d + ": " + this.a + ", " + this.b + ", " + this.c;
        }
    }
}
