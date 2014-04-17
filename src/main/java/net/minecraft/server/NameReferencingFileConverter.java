package net.minecraft.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import net.minecraft.util.com.google.common.base.Charsets;
import net.minecraft.util.com.google.common.collect.Iterators;
import net.minecraft.util.com.google.common.collect.Lists;
import net.minecraft.util.com.google.common.collect.Maps;
import net.minecraft.util.com.google.common.io.Files;
import net.minecraft.util.com.mojang.authlib.Agent;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.ProfileLookupCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit - Imported for package private static methods

public class NameReferencingFileConverter {

    private static final Logger e = LogManager.getLogger();
    public static final File a = new File("banned-ips.txt");
    public static final File b = new File("banned-players.txt");
    public static final File c = new File("ops.txt");
    public static final File d = new File("white-list.txt");

    static List a(File file1, Map map) throws IOException { // CraftBukkit - Added throws
        List list = Files.readLines(file1, Charsets.UTF_8);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            s = s.trim();
            if (!s.startsWith("#") && s.length() >= 1) {
                String[] astring = s.split("\\|");

                map.put(astring[0].toLowerCase(Locale.ROOT), astring);
            }
        }

        return list;
    }

    private static void a(MinecraftServer minecraftserver, Collection collection, ProfileLookupCallback profilelookupcallback) {
        String[] astring = (String[]) Iterators.toArray(Iterators.filter(collection.iterator(), new PredicateEmptyList()), String.class);

        if (minecraftserver.getOnlineMode()) {
            minecraftserver.getGameProfileRepository().findProfilesByNames(astring, Agent.MINECRAFT, profilelookupcallback);
        } else {
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];
                UUID uuid = EntityHuman.a(new GameProfile((UUID) null, s));
                GameProfile gameprofile = new GameProfile(uuid, s);

                profilelookupcallback.onProfileLookupSucceeded(gameprofile);
            }
        }
    }

    public static boolean a(MinecraftServer minecraftserver) {
        GameProfileBanList gameprofilebanlist = new GameProfileBanList(PlayerList.a);

        if (b.exists() && b.isFile()) {
            if (gameprofilebanlist.c().exists()) {
                try {
                    gameprofilebanlist.load();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacetrace
                } catch (IOException filenotfoundexception) {
                    e.warn("Could not load existing file " + gameprofilebanlist.c().getName() + ", " + filenotfoundexception.getMessage());
                }
                // CraftBukkit end
            }

            try {
                HashMap hashmap = Maps.newHashMap();

                a(b, (Map) hashmap);
                GameProfileBanListEntryConverter gameprofilebanlistentryconverter = new GameProfileBanListEntryConverter(minecraftserver, hashmap, gameprofilebanlist);

                a(minecraftserver, hashmap.keySet(), gameprofilebanlistentryconverter);
                gameprofilebanlist.save();
                c(b);
                return true;
            } catch (IOException ioexception) {
                e.warn("Could not read old user banlist to convert it!", ioexception);
                return false;
            } catch (FileConversionException fileconversionexception) {
                e.error("Conversion failed, please try again later", fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean b(MinecraftServer minecraftserver) {
        IpBanList ipbanlist = new IpBanList(PlayerList.b);

        if (a.exists() && a.isFile()) {
            if (ipbanlist.c().exists()) {
                try {
                    ipbanlist.load();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacetrace
                } catch (IOException filenotfoundexception) {
                    e.warn("Could not load existing file " + ipbanlist.c().getName() + ", " + filenotfoundexception.getMessage());
                }
                // CraftBukkit end
            }

            try {
                HashMap hashmap = Maps.newHashMap();

                a(a, (Map) hashmap);
                Iterator iterator = hashmap.keySet().iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    String[] astring = (String[]) hashmap.get(s);
                    Date date = astring.length > 1 ? b(astring[1], (Date) null) : null;
                    String s1 = astring.length > 2 ? astring[2] : null;
                    Date date1 = astring.length > 3 ? b(astring[3], (Date) null) : null;
                    String s2 = astring.length > 4 ? astring[4] : null;

                    ipbanlist.add(new IpBanEntry(s, date, s1, date1, s2));
                }

                ipbanlist.save();
                c(a);
                return true;
            } catch (IOException ioexception) {
                e.warn("Could not parse old ip banlist to convert it!", ioexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean c(MinecraftServer minecraftserver) {
        OpList oplist = new OpList(PlayerList.c);

        if (c.exists() && c.isFile()) {
            if (oplist.c().exists()) {
                try {
                    oplist.load();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacetrace
                } catch (IOException filenotfoundexception) {
                    e.warn("Could not load existing file " + oplist.c().getName() + ", " + filenotfoundexception.getMessage());
                }
                // CraftBukkit end
            }

            try {
                List list = Files.readLines(c, Charsets.UTF_8);
                OpListProfileCallback oplistprofilecallback = new OpListProfileCallback(minecraftserver, oplist);

                a(minecraftserver, list, oplistprofilecallback);
                oplist.save();
                c(c);
                return true;
            } catch (IOException ioexception) {
                e.warn("Could not read old oplist to convert it!", ioexception);
                return false;
            } catch (FileConversionException fileconversionexception) {
                e.error("Conversion failed, please try again later", fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean d(MinecraftServer minecraftserver) {
        WhiteList whitelist = new WhiteList(PlayerList.d);

        if (d.exists() && d.isFile()) {
            if (whitelist.c().exists()) {
                try {
                    whitelist.load();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacetrace
                } catch (IOException filenotfoundexception) {
                    e.warn("Could not load existing file " + whitelist.c().getName() + ", " + filenotfoundexception.getMessage());
                }
                // CraftBukkit end
            }

            try {
                List list = Files.readLines(d, Charsets.UTF_8);
                WhiteListProfileCallback whitelistprofilecallback = new WhiteListProfileCallback(minecraftserver, whitelist);

                a(minecraftserver, list, whitelistprofilecallback);
                whitelist.save();
                c(d);
                return true;
            } catch (IOException ioexception) {
                e.warn("Could not read old whitelist to convert it!", ioexception);
                return false;
            } catch (FileConversionException fileconversionexception) {
                e.error("Conversion failed, please try again later", fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static String a(String s) {
        if (!UtilColor.b(s) && s.length() <= 16) {
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            GameProfile gameprofile = minecraftserver.getUserCache().a(s);

            if (gameprofile != null && gameprofile.getId() != null) {
                return gameprofile.getId().toString();
            } else if (!minecraftserver.N() && minecraftserver.getOnlineMode()) {
                ArrayList arraylist = Lists.newArrayList();
                GameProfileLookupCallback gameprofilelookupcallback = new GameProfileLookupCallback(minecraftserver, arraylist);

                a(minecraftserver, Lists.newArrayList(new String[] { s}), gameprofilelookupcallback);
                return arraylist.size() > 0 && ((GameProfile) arraylist.get(0)).getId() != null ? ((GameProfile) arraylist.get(0)).getId().toString() : "";
            } else {
                return EntityHuman.a(new GameProfile((UUID) null, s)).toString();
            }
        } else {
            return s;
        }
    }

    public static boolean a(DedicatedServer dedicatedserver, PropertyManager propertymanager) {
        File file1 = d(propertymanager);
        File file2 = new File(file1.getParentFile(), "playerdata");
        File file3 = new File(file1.getParentFile(), "unknownplayers");

        if (file1.exists() && file1.isDirectory()) {
            File[] afile = file1.listFiles();
            ArrayList arraylist = Lists.newArrayList();
            File[] afile1 = afile;
            int i = afile.length;

            for (int j = 0; j < i; ++j) {
                File file4 = afile1[j];
                String s = file4.getName();

                if (s.toLowerCase(Locale.ROOT).endsWith(".dat")) {
                    String s1 = s.substring(0, s.length() - ".dat".length());

                    if (s1.length() > 0) {
                        arraylist.add(s1);
                    }
                }
            }

            try {
                String[] astring = (String[]) arraylist.toArray(new String[arraylist.size()]);
                PlayerDatFileConverter playerdatfileconverter = new PlayerDatFileConverter(dedicatedserver, file2, file3, file1, astring);

                a(dedicatedserver, Lists.newArrayList(astring), playerdatfileconverter);
                return true;
            } catch (FileConversionException fileconversionexception) {
                e.error("Conversion failed, please try again later", fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    private static void b(File file1) {
        if (file1.exists()) {
            if (!file1.isDirectory()) {
                throw new FileConversionException("Can\'t create directory " + file1.getName() + " in world save directory.", (PredicateEmptyList) null);
            }
        } else if (!file1.mkdirs()) {
            throw new FileConversionException("Can\'t create directory " + file1.getName() + " in world save directory.", (PredicateEmptyList) null);
        }
    }

    public static boolean a(PropertyManager propertymanager) {
        boolean flag = b(propertymanager);

        flag = flag && c(propertymanager);
        return flag;
    }

    private static boolean b(PropertyManager propertymanager) {
        boolean flag = false;

        if (b.exists() && b.isFile()) {
            flag = true;
        }

        boolean flag1 = false;

        if (a.exists() && a.isFile()) {
            flag1 = true;
        }

        boolean flag2 = false;

        if (c.exists() && c.isFile()) {
            flag2 = true;
        }

        boolean flag3 = false;

        if (d.exists() && d.isFile()) {
            flag3 = true;
        }

        if (!flag && !flag1 && !flag2 && !flag3) {
            return true;
        } else {
            e.warn("**** FAILED TO START THE SERVER AFTER ACCOUNT CONVERSION!");
            e.warn("** please remove the following files and restart the server:");
            if (flag) {
                e.warn("* " + b.getName());
            }

            if (flag1) {
                e.warn("* " + a.getName());
            }

            if (flag2) {
                e.warn("* " + c.getName());
            }

            if (flag3) {
                e.warn("* " + d.getName());
            }

            return false;
        }
    }

    private static boolean c(PropertyManager propertymanager) {
        File file1 = d(propertymanager);

        if (file1.exists() && file1.isDirectory()) {
            String[] astring = file1.list(new DatFilenameFilter());

            if (astring.length > 0) {
                e.warn("**** DETECTED OLD PLAYER FILES IN THE WORLD SAVE");
                e.warn("**** THIS USUALLY HAPPENS WHEN THE AUTOMATIC CONVERSION FAILED IN SOME WAY");
                e.warn("** please restart the server and if the problem persists, remove the directory \'{}\'", new Object[] { file1.getPath()});
                return false;
            }
        }

        return true;
    }

    private static File d(PropertyManager propertymanager) {
        String s = propertymanager.getString("level-name", "world");
        File file1 = new File(MinecraftServer.getServer().server.getWorldContainer(), s); // CraftBukkit - Respect container setting

        return new File(file1, "players");
    }

    private static void c(File file1) {
        File file2 = new File(file1.getName() + ".converted");

        file1.renameTo(file2);
    }

    private static Date b(String s, Date date) {
        Date date1;

        try {
            date1 = ExpirableListEntry.a.parse(s);
        } catch (ParseException parseexception) {
            date1 = date;
        }

        return date1;
    }

    static Logger a() {
        return e;
    }

    static Date a(String s, Date date) {
        return b(s, date);
    }

    static void a(File file1) {
        b(file1);
    }
}
