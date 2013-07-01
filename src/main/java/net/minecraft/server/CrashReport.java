package net.minecraft.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

public class CrashReport {

    private final String a;
    private final Throwable b;
    private final CrashReportSystemDetails c = new CrashReportSystemDetails(this, "System Details");
    private final List d = new ArrayList();
    private File e;
    private boolean f = true;
    private StackTraceElement[] g = new StackTraceElement[0];

    public CrashReport(String s, Throwable throwable) {
        this.a = s;
        this.b = throwable;
        this.h();
    }

    private void h() {
        this.c.a("Minecraft Version", (Callable) (new CrashReportVersion(this)));
        this.c.a("Operating System", (Callable) (new CrashReportOperatingSystem(this)));
        this.c.a("Java Version", (Callable) (new CrashReportJavaVersion(this)));
        this.c.a("Java VM Version", (Callable) (new CrashReportJavaVMVersion(this)));
        this.c.a("Memory", (Callable) (new CrashReportMemory(this)));
        this.c.a("JVM Flags", (Callable) (new CrashReportJVMFlags(this)));
        this.c.a("AABB Pool Size", (Callable) (new CrashReportAABBPoolSize(this)));
        this.c.a("Suspicious classes", (Callable) (new CrashReportSuspiciousClasses(this)));
        this.c.a("IntCache", (Callable) (new CrashReportIntCacheSize(this)));
        this.c.a("CraftBukkit Information", (Callable) (new org.bukkit.craftbukkit.CraftCrashReport())); // CraftBukkit
    }

    public String a() {
        return this.a;
    }

    public Throwable b() {
        return this.b;
    }

    public void a(StringBuilder stringbuilder) {
        if (this.g != null && this.g.length > 0) {
            stringbuilder.append("-- Head --\n");
            stringbuilder.append("Stacktrace:\n");
            StackTraceElement[] astacktraceelement = this.g;
            int i = astacktraceelement.length;

            for (int j = 0; j < i; ++j) {
                StackTraceElement stacktraceelement = astacktraceelement[j];

                stringbuilder.append("\t").append("at ").append(stacktraceelement.toString());
                stringbuilder.append("\n");
            }

            stringbuilder.append("\n");
        }

        Iterator iterator = this.d.iterator();

        while (iterator.hasNext()) {
            CrashReportSystemDetails crashreportsystemdetails = (CrashReportSystemDetails) iterator.next();

            crashreportsystemdetails.a(stringbuilder);
            stringbuilder.append("\n\n");
        }

        this.c.a(stringbuilder);
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
        stringbuilder.append(i());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append((new SimpleDateFormat()).format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(this.a);
        stringbuilder.append("\n\n");
        stringbuilder.append(this.d());
        stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

        for (int i = 0; i < 87; ++i) {
            stringbuilder.append("-");
        }

        stringbuilder.append("\n\n");
        this.a(stringbuilder);
        return stringbuilder.toString();
    }

    public boolean a(File file1, IConsoleLogManager iconsolelogmanager) {
        if (this.e != null) {
            return false;
        } else {
            if (file1.getParentFile() != null) {
                file1.getParentFile().mkdirs();
            }

            try {
                FileWriter filewriter = new FileWriter(file1);

                filewriter.write(this.e());
                filewriter.close();
                this.e = file1;
                return true;
            } catch (Throwable throwable) {
                iconsolelogmanager.severe("Could not save crash report to " + file1, throwable);
                return false;
            }
        }
    }

    public CrashReportSystemDetails g() {
        return this.c;
    }

    public CrashReportSystemDetails a(String s) {
        return this.a(s, 1);
    }

    public CrashReportSystemDetails a(String s, int i) {
        CrashReportSystemDetails crashreportsystemdetails = new CrashReportSystemDetails(this, s);

        if (this.f) {
            int j = crashreportsystemdetails.a(i);
            StackTraceElement[] astacktraceelement = this.b.getStackTrace();
            StackTraceElement stacktraceelement = null;
            StackTraceElement stacktraceelement1 = null;

            if (astacktraceelement != null && astacktraceelement.length - j < astacktraceelement.length) {
                stacktraceelement = astacktraceelement[astacktraceelement.length - j];
                if (astacktraceelement.length + 1 - j < astacktraceelement.length) {
                    stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - j];
                }
            }

            this.f = crashreportsystemdetails.a(stacktraceelement, stacktraceelement1);
            if (j > 0 && !this.d.isEmpty()) {
                CrashReportSystemDetails crashreportsystemdetails1 = (CrashReportSystemDetails) this.d.get(this.d.size() - 1);

                crashreportsystemdetails1.b(j);
            } else if (astacktraceelement != null && astacktraceelement.length >= j) {
                this.g = new StackTraceElement[astacktraceelement.length - j];
                System.arraycopy(astacktraceelement, 0, this.g, 0, this.g.length);
            } else {
                this.f = false;
            }
        }

        this.d.add(crashreportsystemdetails);
        return crashreportsystemdetails;
    }

    private static String i() {
        String[] astring = new String[] { "Who set us up the TNT?", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!", "Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!", "Why is it breaking :(", "Don\'t do that.", "Ouch. That hurt :(", "You\'re mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!"};

        try {
            return astring[(int) (System.nanoTime() % (long) astring.length)];
        } catch (Throwable throwable) {
            return "Witty comment unavailable :(";
        }
    }

    public static CrashReport a(Throwable throwable, String s) {
        CrashReport crashreport;

        if (throwable instanceof ReportedException) {
            crashreport = ((ReportedException) throwable).a();
        } else {
            crashreport = new CrashReport(s, throwable);
        }

        return crashreport;
    }
}
