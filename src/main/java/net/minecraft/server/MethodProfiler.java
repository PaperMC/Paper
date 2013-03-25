package net.minecraft.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// CraftBukkit start - Strip down to empty methods, performance cost
public class MethodProfiler {
    public boolean a = false;

    public final void a() { }
    public final void a(String s) { }
    public final void b() { }
    public final List b(String s) { return null; }
    public final void c(String s) { }
    public final String c() { return null; }
}
// CraftBukkit end
