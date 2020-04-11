package net.minecraft.server;

import java.util.Objects;

public final class Ticket<T> implements Comparable<Ticket<?>> {

    private final TicketType<T> a;
    private final int b;
    public final T identifier; public final T getObjectReason() { return this.identifier; } // Paper - OBFHELPER
    private long d; public final long getCreationTick() { return this.d; } // Paper - OBFHELPER
    public int priority = 0; // Paper

    protected Ticket(TicketType<T> tickettype, int i, T t0) {
        this.a = tickettype;
        this.b = i;
        this.identifier = t0;
    }

    public int compareTo(Ticket<?> ticket) {
        int i = Integer.compare(this.b, ticket.b);

        if (i != 0) {
            return i;
        } else {
            int j = Integer.compare(System.identityHashCode(this.a), System.identityHashCode(ticket.a));

            return j != 0 ? j : this.a.a().compare(this.identifier, (T)ticket.identifier); // Paper - decompile fix
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Ticket)) {
            return false;
        } else {
            Ticket<?> ticket = (Ticket) object;

            return this.b == ticket.b && Objects.equals(this.a, ticket.a) && Objects.equals(this.identifier, ticket.identifier);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.a, this.b, this.identifier});
    }

    public String toString() {
        return "Ticket[" + this.a + " " + this.b + " (" + this.identifier + ")] at " + this.d;
    }

    public TicketType<T> getTicketType() {
        return this.a;
    }

    public final int getTicketLevel() { return this.b(); } // Paper - OBFHELPER
    public int b() {
        return this.b;
    }

    public final void setCurrentTick(long i) { this.a(i); } // Paper - OBFHELPER
    protected void a(long i) {
        this.d = i;
    }

    protected boolean b(long i) {
        long j = this.a.b();

        return j != 0L && i - this.d > j;
    }
}
