package net.minecraft.server;

import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadedMailbox<T> implements Mailbox<T>, AutoCloseable, Runnable {

    private static final Logger LOGGER = LogManager.getLogger();
    private final AtomicInteger c = new AtomicInteger(0);
    public final PairedQueue<? super T, ? extends Runnable> a;
    private final Executor d;
    private final String e;

    public static ThreadedMailbox<Runnable> a(Executor executor, String s) {
        return new ThreadedMailbox<>(new PairedQueue.c<>(new ConcurrentLinkedQueue()), executor, s);
    }

    public ThreadedMailbox(PairedQueue<? super T, ? extends Runnable> pairedqueue, Executor executor, String s) {
        this.d = executor;
        this.a = pairedqueue;
        this.e = s;
    }

    private boolean a() {
        int i;

        do {
            i = this.c.get();
            if ((i & 3) != 0) {
                return false;
            }
        } while (!this.c.compareAndSet(i, i | 2));

        return true;
    }

    private void b() {
        int i;

        do {
            i = this.c.get();
        } while (!this.c.compareAndSet(i, i & -3));

    }

    private boolean c() {
        return (this.c.get() & 1) != 0 ? false : !this.a.b();
    }

    @Override
    public void close() {
        int i;

        do {
            i = this.c.get();
        } while (!this.c.compareAndSet(i, i | 1));

    }

    private boolean d() {
        return (this.c.get() & 2) != 0;
    }

    private boolean e() {
        if (!this.d()) {
            return false;
        } else {
            Runnable runnable = (Runnable) this.a.a();

            if (runnable == null) {
                return false;
            } else {
                Thread thread;
                String s;

                if (SharedConstants.d) {
                    thread = Thread.currentThread();
                    s = thread.getName();
                    thread.setName(this.e);
                } else {
                    thread = null;
                    s = null;
                }

                runnable.run();
                if (thread != null) {
                    thread.setName(s);
                }

                return true;
            }
        }
    }

    public void run() {
        try {
            this.a((i) -> {
                return i == 0;
            });
        } finally {
            this.b();
            this.f();
        }

    }

    @Override
    public void a(T t0) {
        this.a.a(t0);
        this.f();
    }

    private void f() {
        if (this.c() && this.a()) {
            try {
                this.d.execute(this);
            } catch (RejectedExecutionException rejectedexecutionexception) {
                try {
                    this.d.execute(this);
                } catch (RejectedExecutionException rejectedexecutionexception1) {
                    ThreadedMailbox.LOGGER.error("Cound not schedule mailbox", rejectedexecutionexception1);
                }
            }
        }

    }

    private int a(Int2BooleanFunction int2booleanfunction) {
        int i;

        for (i = 0; int2booleanfunction.get(i) && this.e(); ++i) {
            ;
        }

        return i;
    }

    public String toString() {
        return this.e + " " + this.c.get() + " " + this.a.b();
    }

    @Override
    public String bi() {
        return this.e;
    }
}
