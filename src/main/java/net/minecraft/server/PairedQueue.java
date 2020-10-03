package net.minecraft.server;

import com.google.common.collect.Queues;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;

public interface PairedQueue<T, F> {

    @Nullable
    F a();

    boolean a(T t0);

    boolean b();

    public static final class a implements PairedQueue<PairedQueue.b, Runnable> {

        private final List<Queue<Runnable>> a;

        public a(int i) {
            this.a = (List) IntStream.range(0, i).mapToObj((j) -> {
                return Queues.newConcurrentLinkedQueue();
            }).collect(Collectors.toList());
        }

        @Nullable
        @Override
        public Runnable a() {
            Iterator iterator = this.a.iterator();

            Runnable runnable;

            do {
                if (!iterator.hasNext()) {
                    return null;
                }

                Queue<Runnable> queue = (Queue) iterator.next();

                runnable = (Runnable) queue.poll();
            } while (runnable == null);

            return runnable;
        }

        public boolean a(PairedQueue.b pairedqueue_b) {
            int i = pairedqueue_b.a();

            ((Queue) this.a.get(i)).add(pairedqueue_b);
            return true;
        }

        @Override
        public boolean b() {
            return this.a.stream().allMatch(Collection::isEmpty);
        }
    }

    public static final class b implements Runnable {

        private final int a;
        private final Runnable b;

        public b(int i, Runnable runnable) {
            this.a = i;
            this.b = runnable;
        }

        public void run() {
            this.b.run();
        }

        public int a() {
            return this.a;
        }
    }

    public static final class c<T> implements PairedQueue<T, T> {

        private final Queue<T> a;

        public c(Queue<T> queue) {
            this.a = queue;
        }

        @Nullable
        @Override
        public T a() {
            return this.a.poll();
        }

        @Override
        public boolean a(T t0) {
            return this.a.add(t0);
        }

        @Override
        public boolean b() {
            return this.a.isEmpty();
        }
    }
}
