package net.minecraft.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrashReport {

    private final String a;
    private final Throwable b;
    private final Map c = new LinkedHashMap();
    private File d = null;

    public CrashReport(String s, Throwable throwable) {
        this.a = s;
        this.b = throwable;
        this.g();
    }

    private void g() {
        this.a("Minecraft Version", (Callable) (new CrashReportVersion(this)));
        this.a("Operating System", (Callable) (new CrashReportOperatingSystem(this)));
        this.a("Java Version", (Callable) (new CrashReportJavaVersion(this)));
        this.a("Java VM Version", (Callable) (new CrashReportJavaVMVersion(this)));
        this.a("Memory", (Callable) (new CrashReportMemory(this)));
        this.a("JVM Flags", (Callable) (new CrashReportJVMFlags(this)));
        this.a("AABB Pool Size", (Callable) (new CrashReportAABBPoolSize(this)));
        this.a("CraftBukkit Information", (Callable) (new org.bukkit.craftbukkit.CraftCrashReport())); // CraftBukkit
    }

    public void a(String s, Callable callable) {
        try {
            this.a(s, callable.call());
        } catch (Throwable throwable) {
            this.a(s, throwable);
        }
    }

    public void a(String s, Object object) {
        this.c.put(s, object == null ? "null" : object.toString());
    }

    public void a(String s, Throwable throwable) {
        this.a(s, ("~ERROR~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage()));
    }

    public String a() {
        return this.a;
    }

    public Throwable b() {
        return this.b;
    }

    public void a(StringBuilder stringbuilder) {
        boolean flag = true;

        for (Iterator iterator = this.c.entrySet().iterator(); iterator.hasNext(); flag = false) {
            Entry entry = (Entry) iterator.next();

            if (!flag) {
                stringbuilder.append("\n");
            }

            stringbuilder.append("- ");
            stringbuilder.append((String) entry.getKey());
            stringbuilder.append(": ");
            stringbuilder.append((String) entry.getValue());
        }
    }

    public String d() {
        StringWriter stringwriter = null;
        PrintWriter printwriter = null;
        String s = this.b.toString();

        try {
            stringwriter = new StringWriter();
            printwriter = new PrintWriter(stringwriter);
            this.b.printStackTrace(printwriter);
            s = stringwriter.toString();
        } finally {
            try {
                if (stringwriter != null) {
                    stringwriter.close();
                }

                if (printwriter != null) {
                    printwriter.close();
                }
            } catch (IOException ioexception) {
                ;
            }
        }

        return s;
    }

    public String e() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("---- Minecraft Crash Report ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(h());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append((new SimpleDateFormat()).format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(this.a);
        stringbuilder.append("\n\n");
        stringbuilder.append(this.d());
        stringbuilder.append("\n");
        stringbuilder.append("Relevant Details:");
        stringbuilder.append("\n");
        this.a(stringbuilder);
        return stringbuilder.toString();
    }

    public boolean a(File file1) {
        if (this.d != null) {
            return false;
        } else {
            if (file1.getParentFile() != null) {
                file1.getParentFile().mkdirs();
            }

            try {
                FileWriter filewriter = new FileWriter(file1);

                filewriter.write(this.e());
                filewriter.close();
                this.d = file1;
                return true;
            } catch (Throwable throwable) {
                Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not save crash report to " + file1, throwable);
                return false;
            }
        }
    }

    private static String h() {
        String[] astring = new String[] { "Who set us up the TNT?", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!", "Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!", "Why is it breaking :("};

        try {
            return astring[(int) (System.nanoTime() % (long) astring.length)];
        } catch (Throwable throwable) {
            return "Witty comment unavailable :(";
        }
    }
}
