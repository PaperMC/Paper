package net.minecraft.server;

import java.io.IOException;

class NetworkWriterThread extends Thread {

    final NetworkManager a;

    NetworkWriterThread(NetworkManager networkmanager, String s) {
        super(s);
        this.a = networkmanager;
    }

    public void run() {
        NetworkManager.b.getAndIncrement();

        try {
            while (NetworkManager.a(this.a)) {
                boolean flag;

                for (flag = false; NetworkManager.d(this.a); flag = true) {
                    ;
                }

                try {
                    if (flag && NetworkManager.e(this.a) != null) {
                        NetworkManager.e(this.a).flush();
                    }
                } catch (IOException ioexception) {
                    if (!NetworkManager.f(this.a)) {
                        NetworkManager.a(this.a, (Exception) ioexception);
                    }

                    // ioexception.printStackTrace(); // CraftBukkit - Don't spam console on unexpected disconnect
                }

                try {
                    sleep(2L);
                } catch (InterruptedException interruptedexception) {
                    ;
                }
            }
        } finally {
            NetworkManager.b.getAndDecrement();
        }
    }
}
