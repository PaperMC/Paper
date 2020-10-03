package net.minecraft.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class PacketPlayOutWindowItems implements Packet<PacketListenerPlayOut> {

    private int a;
    private List<ItemStack> b;

    public PacketPlayOutWindowItems() {}

    public PacketPlayOutWindowItems(int i, NonNullList<ItemStack> nonnulllist) {
        this.a = i;
        this.b = NonNullList.a(nonnulllist.size(), ItemStack.b);

        for (int j = 0; j < this.b.size(); ++j) {
            this.b.set(j, ((ItemStack) nonnulllist.get(j)).cloneItemStack());
        }

    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readUnsignedByte();
        short short0 = packetdataserializer.readShort();

        this.b = NonNullList.a(short0, ItemStack.b);

        for (int i = 0; i < short0; ++i) {
            this.b.set(i, packetdataserializer.n());
        }

    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.a);
        packetdataserializer.writeShort(this.b.size());
        Iterator iterator = this.b.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            packetdataserializer.a(itemstack);
        }

    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }
}
