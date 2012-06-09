package net.minecraft.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class RemoteControlSession extends RemoteConnectionThread {

    private boolean g = false;
    private Socket h;
    private byte[] i = new byte[1460];
    private String j;

    RemoteControlSession(IMinecraftServer iminecraftserver, Socket socket) {
        super(iminecraftserver);
        this.h = socket;
        this.j = iminecraftserver.a("rcon.password", "");
        this.info("Rcon connection from: " + socket.getInetAddress());
    }

    public void run() {
        // while (true) { // CraftBukkit - moved down
            try {
                while (true) { // CraftBukkit - moved from above
                if (!this.running) {
                    return;
                }

                try {
                    BufferedInputStream bufferedinputstream = new BufferedInputStream(this.h.getInputStream());
                    int i = bufferedinputstream.read(this.i, 0, 1460);

                    if (10 <= i) {
                        byte b0 = 0;
                        int j = StatusChallengeUtils.b(this.i, 0, i);

                        if (j != i - 4) {
                            return;
                        }

                        int k = b0 + 4;
                        int l = StatusChallengeUtils.b(this.i, k, i);

                        k += 4;
                        int i1 = StatusChallengeUtils.a(this.i, k);

                        k += 4;
                        switch (i1) {
                        case 2:
                            if (this.g) {
                                String s = StatusChallengeUtils.a(this.i, k, i);

                                try {
                                    this.a(l, this.server.d(s));
                                } catch (Exception exception) {
                                    this.a(l, "Error executing: " + s + " (" + exception.getMessage() + ")");
                                }
                                continue;
                            }

                            this.e();
                            continue;

                        case 3:
                            String s1 = StatusChallengeUtils.a(this.i, k, i);
                            int j1 = k + s1.length();

                            if (0 != s1.length() && s1.equals(this.j)) {
                                this.g = true;
                                this.a(l, 2, "");
                                continue;
                            }

                            this.g = false;
                            this.e();
                            continue;

                        default:
                            this.a(l, String.format("Unknown request %s", new Object[] { Integer.toHexString(i1)}));
                            continue;
                        }
                    }
                } catch (SocketTimeoutException sockettimeoutexception) {
                    return; // CraftBukkit - shut down the thread after hitting an exception.
                } catch (IOException ioexception) {
                    if (this.running) {
                        this.info("IO: " + ioexception.getMessage());
                    }
                    return; // CraftBukkit - shut down the thread after hitting an exception.
                }
                } // CraftBukkit - Loop shift
            } catch (Exception exception1) {
                System.out.println(exception1);
                return;
            } finally {
                this.f();
            }
        // CraftBukkit - Loop shift
    }

    private void a(int i, int j, String s) throws IOException { // CraftBukkit - throws IOException
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(1248);
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

        dataoutputstream.writeInt(Integer.reverseBytes(s.length() + 10));
        dataoutputstream.writeInt(Integer.reverseBytes(i));
        dataoutputstream.writeInt(Integer.reverseBytes(j));
        dataoutputstream.writeBytes(s);
        dataoutputstream.write(0);
        dataoutputstream.write(0);
        this.h.getOutputStream().write(bytearrayoutputstream.toByteArray());
    }

    private void e() throws IOException { // CraftBukkit - throws IOException
        this.a(-1, 2, "");
    }

    private void a(int i, String s) throws IOException { // CraftBukkit - throws IOException
        int j = s.length();

        do {
            int k = 4096 <= j ? 4096 : j;

            this.a(i, 0, s.substring(0, k));
            s = s.substring(k);
            j = s.length();
        } while (0 != j);

    }

    private void f() {
        if (null != this.h) {
            try {
                this.h.close();
            } catch (IOException ioexception) {
                this.warning("IO: " + ioexception.getMessage());
            }

            this.h = null;
        }
    }
}
