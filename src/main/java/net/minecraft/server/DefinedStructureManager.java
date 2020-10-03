package net.minecraft.server;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefinedStructureManager {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<MinecraftKey, DefinedStructure> b = Maps.newConcurrentMap(); // SPIGOT-5287
    private final DataFixer c;
    private IResourceManager d;
    private final java.nio.file.Path e;

    public DefinedStructureManager(IResourceManager iresourcemanager, Convertable.ConversionSession convertable_conversionsession, DataFixer datafixer) {
        this.d = iresourcemanager;
        this.c = datafixer;
        this.e = convertable_conversionsession.getWorldFolder(SavedFile.GENERATED).normalize();
    }

    public DefinedStructure a(MinecraftKey minecraftkey) {
        DefinedStructure definedstructure = this.b(minecraftkey);

        if (definedstructure == null) {
            definedstructure = new DefinedStructure();
            this.b.put(minecraftkey, definedstructure);
        }

        return definedstructure;
    }

    @Nullable
    public DefinedStructure b(MinecraftKey minecraftkey) {
        return (DefinedStructure) this.b.computeIfAbsent(minecraftkey, (minecraftkey1) -> {
            DefinedStructure definedstructure = this.f(minecraftkey1);

            return definedstructure != null ? definedstructure : this.e(minecraftkey1);
        });
    }

    public void a(IResourceManager iresourcemanager) {
        this.d = iresourcemanager;
        this.b.clear();
    }

    @Nullable
    private DefinedStructure e(MinecraftKey minecraftkey) {
        MinecraftKey minecraftkey1 = new MinecraftKey(minecraftkey.getNamespace(), "structures/" + minecraftkey.getKey() + ".nbt");

        try {
            IResource iresource = this.d.a(minecraftkey1);
            Throwable throwable = null;

            DefinedStructure definedstructure;

            try {
                definedstructure = this.a(iresource.b());
            } catch (Throwable throwable1) {
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (iresource != null) {
                    if (throwable != null) {
                        try {
                            iresource.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    } else {
                        iresource.close();
                    }
                }

            }

            return definedstructure;
        } catch (FileNotFoundException filenotfoundexception) {
            return null;
        } catch (Throwable throwable3) {
            DefinedStructureManager.LOGGER.error("Couldn't load structure {}: {}", minecraftkey, throwable3.toString());
            return null;
        }
    }

    @Nullable
    private DefinedStructure f(MinecraftKey minecraftkey) {
        if (!this.e.toFile().isDirectory()) {
            return null;
        } else {
            java.nio.file.Path java_nio_file_path = this.b(minecraftkey, ".nbt");

            try {
                FileInputStream fileinputstream = new FileInputStream(java_nio_file_path.toFile());
                Throwable throwable = null;

                DefinedStructure definedstructure;

                try {
                    definedstructure = this.a((InputStream) fileinputstream);
                } catch (Throwable throwable1) {
                    throwable = throwable1;
                    throw throwable1;
                } finally {
                    if (fileinputstream != null) {
                        if (throwable != null) {
                            try {
                                fileinputstream.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        } else {
                            fileinputstream.close();
                        }
                    }

                }

                return definedstructure;
            } catch (FileNotFoundException filenotfoundexception) {
                return null;
            } catch (IOException ioexception) {
                DefinedStructureManager.LOGGER.error("Couldn't load structure from {}", java_nio_file_path, ioexception);
                return null;
            }
        }
    }

    private DefinedStructure a(InputStream inputstream) throws IOException {
        NBTTagCompound nbttagcompound = NBTCompressedStreamTools.a(inputstream);

        return this.a(nbttagcompound);
    }

    public DefinedStructure a(NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.hasKeyOfType("DataVersion", 99)) {
            nbttagcompound.setInt("DataVersion", 500);
        }

        DefinedStructure definedstructure = new DefinedStructure();

        definedstructure.b(GameProfileSerializer.a(this.c, DataFixTypes.STRUCTURE, nbttagcompound, nbttagcompound.getInt("DataVersion")));
        return definedstructure;
    }

    public boolean c(MinecraftKey minecraftkey) {
        DefinedStructure definedstructure = (DefinedStructure) this.b.get(minecraftkey);

        if (definedstructure == null) {
            return false;
        } else {
            java.nio.file.Path java_nio_file_path = this.b(minecraftkey, ".nbt");
            java.nio.file.Path java_nio_file_path1 = java_nio_file_path.getParent();

            if (java_nio_file_path1 == null) {
                return false;
            } else {
                try {
                    Files.createDirectories(Files.exists(java_nio_file_path1, new LinkOption[0]) ? java_nio_file_path1.toRealPath() : java_nio_file_path1);
                } catch (IOException ioexception) {
                    DefinedStructureManager.LOGGER.error("Failed to create parent directory: {}", java_nio_file_path1);
                    return false;
                }

                NBTTagCompound nbttagcompound = definedstructure.a(new NBTTagCompound());

                try {
                    FileOutputStream fileoutputstream = new FileOutputStream(java_nio_file_path.toFile());
                    Throwable throwable = null;

                    try {
                        NBTCompressedStreamTools.a(nbttagcompound, (OutputStream) fileoutputstream);
                    } catch (Throwable throwable1) {
                        throwable = throwable1;
                        throw throwable1;
                    } finally {
                        if (fileoutputstream != null) {
                            if (throwable != null) {
                                try {
                                    fileoutputstream.close();
                                } catch (Throwable throwable2) {
                                    throwable.addSuppressed(throwable2);
                                }
                            } else {
                                fileoutputstream.close();
                            }
                        }

                    }

                    return true;
                } catch (Throwable throwable3) {
                    return false;
                }
            }
        }
    }

    public java.nio.file.Path a(MinecraftKey minecraftkey, String s) {
        try {
            java.nio.file.Path java_nio_file_path = this.e.resolve(minecraftkey.getNamespace());
            java.nio.file.Path java_nio_file_path1 = java_nio_file_path.resolve("structures");

            return FileUtils.b(java_nio_file_path1, minecraftkey.getKey(), s);
        } catch (InvalidPathException invalidpathexception) {
            throw new ResourceKeyInvalidException("Invalid resource path: " + minecraftkey, invalidpathexception);
        }
    }

    private java.nio.file.Path b(MinecraftKey minecraftkey, String s) {
        if (minecraftkey.getKey().contains("//")) {
            throw new ResourceKeyInvalidException("Invalid resource path: " + minecraftkey);
        } else {
            java.nio.file.Path java_nio_file_path = this.a(minecraftkey, s);

            if (java_nio_file_path.startsWith(this.e) && FileUtils.a(java_nio_file_path) && FileUtils.b(java_nio_file_path)) {
                return java_nio_file_path;
            } else {
                throw new ResourceKeyInvalidException("Invalid resource path: " + java_nio_file_path);
            }
        }
    }

    public void d(MinecraftKey minecraftkey) {
        this.b.remove(minecraftkey);
    }
}
