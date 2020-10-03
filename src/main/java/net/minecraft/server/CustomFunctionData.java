package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CustomFunctionData {

    private static final MinecraftKey a = new MinecraftKey("tick");
    private static final MinecraftKey b = new MinecraftKey("load");
    private final MinecraftServer server;
    private boolean d;
    private final ArrayDeque<CustomFunctionData.a> e = new ArrayDeque();
    private final List<CustomFunctionData.a> f = Lists.newArrayList();
    private final List<CustomFunction> g = Lists.newArrayList();
    private boolean h;
    private CustomFunctionManager i;

    public CustomFunctionData(MinecraftServer minecraftserver, CustomFunctionManager customfunctionmanager) {
        this.server = minecraftserver;
        this.i = customfunctionmanager;
        this.b(customfunctionmanager);
    }

    public int b() {
        return this.server.getGameRules().getInt(GameRules.MAX_COMMAND_CHAIN_LENGTH);
    }

    public com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> getCommandDispatcher() {
        return this.server.getCommandDispatcher().a();
    }

    public void tick() {
        this.a((Collection) this.g, CustomFunctionData.a);
        if (this.h) {
            this.h = false;
            Collection<CustomFunction> collection = this.i.b().b(CustomFunctionData.b).getTagged();

            this.a((Collection) collection, CustomFunctionData.b);
        }

    }

    private void a(Collection<CustomFunction> collection, MinecraftKey minecraftkey) {
        this.server.getMethodProfiler().a(minecraftkey::toString);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            CustomFunction customfunction = (CustomFunction) iterator.next();

            this.a(customfunction, this.e());
        }

        this.server.getMethodProfiler().exit();
    }

    public int a(CustomFunction customfunction, CommandListenerWrapper commandlistenerwrapper) {
        int i = this.b();

        if (this.d) {
            if (this.e.size() + this.f.size() < i) {
                this.f.add(new CustomFunctionData.a(this, commandlistenerwrapper, new CustomFunction.d(customfunction)));
            }

            return 0;
        } else {
            int j;

            try {
                this.d = true;
                int k = 0;
                CustomFunction.c[] acustomfunction_c = customfunction.b();

                for (j = acustomfunction_c.length - 1; j >= 0; --j) {
                    this.e.push(new CustomFunctionData.a(this, commandlistenerwrapper, acustomfunction_c[j]));
                }

                do {
                    if (this.e.isEmpty()) {
                        j = k;
                        return j;
                    }

                    try {
                        CustomFunctionData.a customfunctiondata_a = (CustomFunctionData.a) this.e.removeFirst();

                        this.server.getMethodProfiler().a(customfunctiondata_a::toString);
                        customfunctiondata_a.a(this.e, i);
                        if (!this.f.isEmpty()) {
                            List list = Lists.reverse(this.f);
                            ArrayDeque arraydeque = this.e;

                            this.e.getClass();
                            list.forEach(arraydeque::addFirst);
                            this.f.clear();
                        }
                    } finally {
                        this.server.getMethodProfiler().exit();
                    }

                    ++k;
                } while (k < i);

                j = k;
            } finally {
                this.e.clear();
                this.f.clear();
                this.d = false;
            }

            return j;
        }
    }

    public void a(CustomFunctionManager customfunctionmanager) {
        this.i = customfunctionmanager;
        this.b(customfunctionmanager);
    }

    private void b(CustomFunctionManager customfunctionmanager) {
        this.g.clear();
        this.g.addAll(customfunctionmanager.b().b(CustomFunctionData.a).getTagged());
        this.h = true;
    }

    public CommandListenerWrapper e() {
        return this.server.getServerCommandListener().a(2).a();
    }

    public Optional<CustomFunction> a(MinecraftKey minecraftkey) {
        return this.i.a(minecraftkey);
    }

    public Tag<CustomFunction> b(MinecraftKey minecraftkey) {
        return this.i.b(minecraftkey);
    }

    public Iterable<MinecraftKey> f() {
        return this.i.a().keySet();
    }

    public Iterable<MinecraftKey> g() {
        return this.i.b().b();
    }

    public static class a {

        private final CustomFunctionData a;
        private final CommandListenerWrapper b;
        private final CustomFunction.c c;

        public a(CustomFunctionData customfunctiondata, CommandListenerWrapper commandlistenerwrapper, CustomFunction.c customfunction_c) {
            this.a = customfunctiondata;
            this.b = commandlistenerwrapper;
            this.c = customfunction_c;
        }

        public void a(ArrayDeque<CustomFunctionData.a> arraydeque, int i) {
            try {
                this.c.a(this.a, this.b, arraydeque, i);
            } catch (Throwable throwable) {
                ;
            }

        }

        public String toString() {
            return this.c.toString();
        }
    }
}
