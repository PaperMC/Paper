package net.minecraft.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EULA {

    private static final Logger LOGGER = LogManager.getLogger();
    private final java.nio.file.Path b;
    private final boolean c;

    public EULA(java.nio.file.Path java_nio_file_path) {
        this.b = java_nio_file_path;
        this.c = SharedConstants.d || this.b();
    }

    private boolean b() {
        try {
            InputStream inputstream = Files.newInputStream(this.b);
            Throwable throwable = null;

            boolean flag;

            try {
                Properties properties = new Properties();

                properties.load(inputstream);
                flag = Boolean.parseBoolean(properties.getProperty("eula", "false"));
            } catch (Throwable throwable1) {
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (inputstream != null) {
                    if (throwable != null) {
                        try {
                            inputstream.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    } else {
                        inputstream.close();
                    }
                }

            }

            return flag;
        } catch (Exception exception) {
            EULA.LOGGER.warn("Failed to load {}", this.b);
            this.c();
            return false;
        }
    }

    public boolean a() {
        return this.c;
    }

    private void c() {
        if (!SharedConstants.d) {
            try {
                OutputStream outputstream = Files.newOutputStream(this.b);
                Throwable throwable = null;

                try {
                    Properties properties = new Properties();

                    properties.setProperty("eula", "false");
                    properties.store(outputstream, "By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
                } catch (Throwable throwable1) {
                    throwable = throwable1;
                    throw throwable1;
                } finally {
                    if (outputstream != null) {
                        if (throwable != null) {
                            try {
                                outputstream.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        } else {
                            outputstream.close();
                        }
                    }

                }
            } catch (Exception exception) {
                EULA.LOGGER.warn("Failed to save {}", this.b, exception);
            }

        }
    }
}
