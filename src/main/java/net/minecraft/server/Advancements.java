package net.minecraft.server;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Advancements {

    private static final Logger LOGGER = LogManager.getLogger();
    public final Map<MinecraftKey, Advancement> advancements = Maps.newHashMap();
    private final Set<Advancement> c = Sets.newLinkedHashSet();
    private final Set<Advancement> d = Sets.newLinkedHashSet();
    private Advancements.a e;

    public Advancements() {}

    public void a(Map<MinecraftKey, Advancement.SerializedAdvancement> map) {
        Function function = Functions.forMap(this.advancements, (Object) null);

        label42:
        while (!map.isEmpty()) {
            boolean flag = false;
            Iterator iterator = map.entrySet().iterator();

            Entry entry;

            while (iterator.hasNext()) {
                entry = (Entry) iterator.next();
                MinecraftKey minecraftkey = (MinecraftKey) entry.getKey();
                Advancement.SerializedAdvancement advancement_serializedadvancement = (Advancement.SerializedAdvancement) entry.getValue();

                if (advancement_serializedadvancement.a((java.util.function.Function) function)) {
                    Advancement advancement = advancement_serializedadvancement.b(minecraftkey);

                    this.advancements.put(minecraftkey, advancement);
                    flag = true;
                    iterator.remove();
                    if (advancement.b() == null) {
                        this.c.add(advancement);
                        if (this.e != null) {
                            this.e.a(advancement);
                        }
                    } else {
                        this.d.add(advancement);
                        if (this.e != null) {
                            this.e.c(advancement);
                        }
                    }
                }
            }

            if (!flag) {
                iterator = map.entrySet().iterator();

                while (true) {
                    if (!iterator.hasNext()) {
                        break label42;
                    }

                    entry = (Entry) iterator.next();
                    Advancements.LOGGER.error("Couldn't load advancement {}: {}", entry.getKey(), entry.getValue());
                }
            }
        }

        Advancements.LOGGER.info("Loaded {} advancements", this.advancements.size());
    }

    public Iterable<Advancement> b() {
        return this.c;
    }

    public Collection<Advancement> c() {
        return this.advancements.values();
    }

    @Nullable
    public Advancement a(MinecraftKey minecraftkey) {
        return (Advancement) this.advancements.get(minecraftkey);
    }

    public interface a {

        void a(Advancement advancement);

        void c(Advancement advancement);
    }
}
