package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserCache {

    private static final Logger LOGGER = LogManager.getLogger();
    private static boolean b;
    private final Map<String, UserCache.UserCacheEntry> c = Maps.newConcurrentMap();
    private final Map<UUID, UserCache.UserCacheEntry> d = Maps.newConcurrentMap();
    private final GameProfileRepository e;
    private final Gson f = (new GsonBuilder()).create();
    private final File g;
    private final AtomicLong h = new AtomicLong();

    public UserCache(GameProfileRepository gameprofilerepository, File file) {
        this.e = gameprofilerepository;
        this.g = file;
        Lists.reverse(this.a()).forEach(this::a);
    }

    private void a(UserCache.UserCacheEntry usercache_usercacheentry) {
        GameProfile gameprofile = usercache_usercacheentry.a();

        usercache_usercacheentry.a(this.d());
        String s = gameprofile.getName();

        if (s != null) {
            this.c.put(s.toLowerCase(Locale.ROOT), usercache_usercacheentry);
        }

        UUID uuid = gameprofile.getId();

        if (uuid != null) {
            this.d.put(uuid, usercache_usercacheentry);
        }

    }

    @Nullable
    private static GameProfile a(GameProfileRepository gameprofilerepository, String s) {
        final AtomicReference<GameProfile> atomicreference = new AtomicReference();
        ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
            public void onProfileLookupSucceeded(GameProfile gameprofile) {
                atomicreference.set(gameprofile);
            }

            public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                atomicreference.set(null);
            }
        };

        gameprofilerepository.findProfilesByNames(new String[]{s}, Agent.MINECRAFT, profilelookupcallback);
        GameProfile gameprofile = (GameProfile) atomicreference.get();

        if (!c() && gameprofile == null) {
            UUID uuid = EntityHuman.a(new GameProfile((UUID) null, s));

            gameprofile = new GameProfile(uuid, s);
        }

        return gameprofile;
    }

    public static void a(boolean flag) {
        UserCache.b = flag;
    }

    private static boolean c() {
        return UserCache.b;
    }

    public void a(GameProfile gameprofile) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(2, 1);
        Date date = calendar.getTime();
        UserCache.UserCacheEntry usercache_usercacheentry = new UserCache.UserCacheEntry(gameprofile, date);

        this.a(usercache_usercacheentry);
        if( !org.spigotmc.SpigotConfig.saveUserCacheOnStopOnly ) this.b(); // Spigot - skip saving if disabled
    }

    private long d() {
        return this.h.incrementAndGet();
    }

    @Nullable
    public GameProfile getProfile(String s) {
        String s1 = s.toLowerCase(Locale.ROOT);
        UserCache.UserCacheEntry usercache_usercacheentry = (UserCache.UserCacheEntry) this.c.get(s1);
        boolean flag = false;

        if (usercache_usercacheentry != null && (new Date()).getTime() >= usercache_usercacheentry.b.getTime()) {
            this.d.remove(usercache_usercacheentry.a().getId());
            this.c.remove(usercache_usercacheentry.a().getName().toLowerCase(Locale.ROOT));
            flag = true;
            usercache_usercacheentry = null;
        }

        GameProfile gameprofile;

        if (usercache_usercacheentry != null) {
            usercache_usercacheentry.a(this.d());
            gameprofile = usercache_usercacheentry.a();
        } else {
            gameprofile = a(this.e, s); // Spigot - use correct case for offline players
            if (gameprofile != null) {
                this.a(gameprofile);
                flag = false;
            }
        }

        if (flag && !org.spigotmc.SpigotConfig.saveUserCacheOnStopOnly) { // Spigot - skip saving if disabled
            this.b();
        }

        return gameprofile;
    }

    @Nullable
    public GameProfile getProfile(UUID uuid) {
        UserCache.UserCacheEntry usercache_usercacheentry = (UserCache.UserCacheEntry) this.d.get(uuid);

        if (usercache_usercacheentry == null) {
            return null;
        } else {
            usercache_usercacheentry.a(this.d());
            return usercache_usercacheentry.a();
        }
    }

    private static DateFormat e() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    }

    public List<UserCache.UserCacheEntry> a() {
        ArrayList arraylist = Lists.newArrayList();

        try {
            BufferedReader bufferedreader = Files.newReader(this.g, StandardCharsets.UTF_8);
            Throwable throwable = null;

            ArrayList arraylist1;

            try {
                JsonArray jsonarray = (JsonArray) this.f.fromJson(bufferedreader, JsonArray.class);

                if (jsonarray != null) {
                    DateFormat dateformat = e();

                    jsonarray.forEach((jsonelement) -> {
                        UserCache.UserCacheEntry usercache_usercacheentry = a(jsonelement, dateformat);

                        if (usercache_usercacheentry != null) {
                            arraylist.add(usercache_usercacheentry);
                        }

                    });
                    return arraylist;
                }

                arraylist1 = arraylist;
            } catch (Throwable throwable1) {
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (bufferedreader != null) {
                    if (throwable != null) {
                        try {
                            bufferedreader.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    } else {
                        bufferedreader.close();
                    }
                }

            }

            return arraylist1;
        } catch (FileNotFoundException filenotfoundexception) {
            ;
        // Spigot Start
        } catch (com.google.gson.JsonSyntaxException ex) {
            JsonList.LOGGER.warn( "Usercache.json is corrupted or has bad formatting. Deleting it to prevent further issues." );
            this.g.delete();
        // Spigot End
        } catch (JsonParseException | IOException ioexception) {
            UserCache.LOGGER.warn("Failed to load profile cache {}", this.g, ioexception);
        }

        return arraylist;
    }

    public void b() {
        JsonArray jsonarray = new JsonArray();
        DateFormat dateformat = e();

        this.a(org.spigotmc.SpigotConfig.userCacheCap).forEach((usercache_usercacheentry) -> { // Spigot
            jsonarray.add(a(usercache_usercacheentry, dateformat));
        });
        String s = this.f.toJson(jsonarray);

        try {
            BufferedWriter bufferedwriter = Files.newWriter(this.g, StandardCharsets.UTF_8);
            Throwable throwable = null;

            try {
                bufferedwriter.write(s);
            } catch (Throwable throwable1) {
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (bufferedwriter != null) {
                    if (throwable != null) {
                        try {
                            bufferedwriter.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    } else {
                        bufferedwriter.close();
                    }
                }

            }
        } catch (IOException ioexception) {
            ;
        }

    }

    private Stream<UserCache.UserCacheEntry> a(int i) {
        return ImmutableList.copyOf(this.d.values()).stream().sorted(Comparator.comparing(UserCache.UserCacheEntry::c).reversed()).limit((long) i);
    }

    private static JsonElement a(UserCache.UserCacheEntry usercache_usercacheentry, DateFormat dateformat) {
        JsonObject jsonobject = new JsonObject();

        jsonobject.addProperty("name", usercache_usercacheentry.a().getName());
        UUID uuid = usercache_usercacheentry.a().getId();

        jsonobject.addProperty("uuid", uuid == null ? "" : uuid.toString());
        jsonobject.addProperty("expiresOn", dateformat.format(usercache_usercacheentry.b()));
        return jsonobject;
    }

    @Nullable
    private static UserCache.UserCacheEntry a(JsonElement jsonelement, DateFormat dateformat) {
        if (jsonelement.isJsonObject()) {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            JsonElement jsonelement1 = jsonobject.get("name");
            JsonElement jsonelement2 = jsonobject.get("uuid");
            JsonElement jsonelement3 = jsonobject.get("expiresOn");

            if (jsonelement1 != null && jsonelement2 != null) {
                String s = jsonelement2.getAsString();
                String s1 = jsonelement1.getAsString();
                Date date = null;

                if (jsonelement3 != null) {
                    try {
                        date = dateformat.parse(jsonelement3.getAsString());
                    } catch (ParseException parseexception) {
                        ;
                    }
                }

                if (s1 != null && s != null && date != null) {
                    UUID uuid;

                    try {
                        uuid = UUID.fromString(s);
                    } catch (Throwable throwable) {
                        return null;
                    }

                    return new UserCache.UserCacheEntry(new GameProfile(uuid, s1), date);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    static class UserCacheEntry {

        private final GameProfile a;
        private final Date b;
        private volatile long c;

        private UserCacheEntry(GameProfile gameprofile, Date date) {
            this.a = gameprofile;
            this.b = date;
        }

        public GameProfile a() {
            return this.a;
        }

        public Date b() {
            return this.b;
        }

        public void a(long i) {
            this.c = i;
        }

        public long c() {
            return this.c;
        }
    }
}
