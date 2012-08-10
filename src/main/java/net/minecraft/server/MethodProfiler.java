package net.minecraft.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MethodProfiler {

    private final List b = new ArrayList();
    private final List c = new ArrayList();
    public boolean a = false;
    private String d = "";
    private final Map e = new HashMap();

    public MethodProfiler() {}

    public void a() {
        this.e.clear();
        this.d = "";
        this.b.clear();
    }

    public void a(String s) {
        if (this.a) {
            if (this.d.length() > 0) {
                this.d = this.d + ".";
            }

            this.d = this.d + s;
            this.b.add(this.d);
            this.c.add(Long.valueOf(System.nanoTime()));
        }
    }

    public void b() {
        if (this.a) {
            long i = System.nanoTime();
            long j = ((Long) this.c.remove(this.c.size() - 1)).longValue();

            this.b.remove(this.b.size() - 1);
            long k = i - j;

            if (this.e.containsKey(this.d)) {
                this.e.put(this.d, Long.valueOf(((Long) this.e.get(this.d)).longValue() + k));
            } else {
                this.e.put(this.d, Long.valueOf(k));
            }

            if (k > 100000000L) {
                System.out.println("Something\'s taking too long! \'" + this.d + "\' took aprox " + (double) k / 1000000.0D + " ms");
            }

            this.d = !this.b.isEmpty() ? (String) this.b.get(this.b.size() - 1) : "";
        }
    }

    public List b(String s) {
        if (!this.a) {
            return null;
        } else {
            long i = this.e.containsKey("root") ? ((Long) this.e.get("root")).longValue() : 0L;
            long j = this.e.containsKey(s) ? ((Long) this.e.get(s)).longValue() : -1L;
            ArrayList arraylist = new ArrayList();

            if (s.length() > 0) {
                s = s + ".";
            }

            long k = 0L;
            Iterator iterator = this.e.keySet().iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();

                if (s1.length() > s.length() && s1.startsWith(s) && s1.indexOf(".", s.length() + 1) < 0) {
                    k += ((Long) this.e.get(s1)).longValue();
                }
            }

            float f = (float) k;

            if (k < j) {
                k = j;
            }

            if (i < k) {
                i = k;
            }

            Iterator iterator1 = this.e.keySet().iterator();

            String s2;

            while (iterator1.hasNext()) {
                s2 = (String) iterator1.next();
                if (s2.length() > s.length() && s2.startsWith(s) && s2.indexOf(".", s.length() + 1) < 0) {
                    long l = ((Long) this.e.get(s2)).longValue();
                    double d0 = (double) l * 100.0D / (double) k;
                    double d1 = (double) l * 100.0D / (double) i;
                    String s3 = s2.substring(s.length());

                    arraylist.add(new ProfilerInfo(s3, d0, d1));
                }
            }

            iterator1 = this.e.keySet().iterator();

            while (iterator1.hasNext()) {
                s2 = (String) iterator1.next();
                this.e.put(s2, Long.valueOf(((Long) this.e.get(s2)).longValue() * 999L / 1000L));
            }

            if ((float) k > f) {
                arraylist.add(new ProfilerInfo("unspecified", (double) ((float) k - f) * 100.0D / (double) k, (double) ((float) k - f) * 100.0D / (double) i));
            }

            Collections.sort(arraylist);
            arraylist.add(0, new ProfilerInfo(s, 100.0D, (double) k * 100.0D / (double) i));
            return arraylist;
        }
    }

    public void c(String s) {
        this.b();
        this.a(s);
    }

    public String c() {
        return this.b.size() == 0 ? "[UNKNOWN]" : (String) this.b.get(this.b.size() - 1);
    }
}
