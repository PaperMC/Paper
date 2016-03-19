package net.minecraft.server;

import java.io.IOException;

public class PacketPlayOutUpdateTime implements Packet<PacketListenerPlayOut> {

    // World Age in ticks
    // Not changed by server commands
    // World Age must not be negative
    private long a;
    // Time of Day in ticks
    // If negative the sun will stop moving at the Math.abs of the time
    // Displayed in the debug screen (F3)
    private long b;

    public PacketPlayOutUpdateTime() {}

    public PacketPlayOutUpdateTime(long i, long j, boolean flag) {
        this.a = i;
        this.b = j;
        if (!flag) {
            this.b = -this.b;
            if (this.b == 0L) {
                this.b = -1L;
            }
        }

        // Paper start
        this.a = this.a % 192000;
        // Paper end
    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readLong();
        this.b = packetdataserializer.readLong();
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.a);
        packetdataserializer.writeLong(this.b);
    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }
}
