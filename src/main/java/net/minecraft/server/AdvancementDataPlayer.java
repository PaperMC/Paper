package net.minecraft.server;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdvancementDataPlayer {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson b = (new GsonBuilder()).registerTypeAdapter(AdvancementProgress.class, new AdvancementProgress.a()).registerTypeAdapter(MinecraftKey.class, new MinecraftKey.a()).setPrettyPrinting().create();
    private static final TypeToken<Map<MinecraftKey, AdvancementProgress>> c = new TypeToken<Map<MinecraftKey, AdvancementProgress>>() {
    };
    private final DataFixer d;
    private final PlayerList e;
    private final File f;
    public final Map<Advancement, AdvancementProgress> data = Maps.newLinkedHashMap();
    private final Set<Advancement> h = Sets.newLinkedHashSet();
    private final Set<Advancement> i = Sets.newLinkedHashSet();
    private final Set<Advancement> j = Sets.newLinkedHashSet();
    private EntityPlayer player;
    @Nullable
    private Advancement l;
    private boolean m = true;

    public AdvancementDataPlayer(DataFixer datafixer, PlayerList playerlist, AdvancementDataWorld advancementdataworld, File file, EntityPlayer entityplayer) {
        this.d = datafixer;
        this.e = playerlist;
        this.f = file;
        this.player = entityplayer;
        this.d(advancementdataworld);
    }

    public void a(EntityPlayer entityplayer) {
        this.player = entityplayer;
    }

    public void a() {
        Iterator iterator = CriterionTriggers.a().iterator();

        while (iterator.hasNext()) {
            CriterionTrigger<?> criteriontrigger = (CriterionTrigger) iterator.next();

            criteriontrigger.a(this);
        }

    }

    public void a(AdvancementDataWorld advancementdataworld) {
        this.a();
        this.data.clear();
        this.h.clear();
        this.i.clear();
        this.j.clear();
        this.m = true;
        this.l = null;
        this.d(advancementdataworld);
    }

    private void b(AdvancementDataWorld advancementdataworld) {
        Iterator iterator = advancementdataworld.getAdvancements().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            this.c(advancement);
        }

    }

    private void c() {
        List<Advancement> list = Lists.newArrayList();
        Iterator iterator = this.data.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Advancement, AdvancementProgress> entry = (Entry) iterator.next();

            if (((AdvancementProgress) entry.getValue()).isDone()) {
                list.add(entry.getKey());
                this.j.add(entry.getKey());
            }
        }

        iterator = list.iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            this.e(advancement);
        }

    }

    private void c(AdvancementDataWorld advancementdataworld) {
        Iterator iterator = advancementdataworld.getAdvancements().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            if (advancement.getCriteria().isEmpty()) {
                this.grantCriteria(advancement, "");
                advancement.d().a(this.player);
            }
        }

    }

    private void d(AdvancementDataWorld advancementdataworld) {
        if (this.f.isFile()) {
            try {
                JsonReader jsonreader = new JsonReader(new StringReader(Files.toString(this.f, StandardCharsets.UTF_8)));
                Throwable throwable = null;

                try {
                    jsonreader.setLenient(false);
                    Dynamic<JsonElement> dynamic = new Dynamic(JsonOps.INSTANCE, Streams.parse(jsonreader));

                    if (!dynamic.get("DataVersion").asNumber().result().isPresent()) {
                        dynamic = dynamic.set("DataVersion", dynamic.createInt(1343));
                    }

                    dynamic = this.d.update(DataFixTypes.ADVANCEMENTS.a(), dynamic, dynamic.get("DataVersion").asInt(0), SharedConstants.getGameVersion().getWorldVersion());
                    dynamic = dynamic.remove("DataVersion");
                    Map<MinecraftKey, AdvancementProgress> map = (Map) AdvancementDataPlayer.b.getAdapter(AdvancementDataPlayer.c).fromJsonTree((JsonElement) dynamic.getValue());

                    if (map == null) {
                        throw new JsonParseException("Found null for advancements");
                    }

                    Stream<Entry<MinecraftKey, AdvancementProgress>> stream = map.entrySet().stream().sorted(Comparator.comparing(Entry::getValue));
                    Iterator iterator = ((List) stream.collect(Collectors.toList())).iterator();

                    while (iterator.hasNext()) {
                        Entry<MinecraftKey, AdvancementProgress> entry = (Entry) iterator.next();
                        Advancement advancement = advancementdataworld.a((MinecraftKey) entry.getKey());

                        if (advancement == null) {
                            AdvancementDataPlayer.LOGGER.warn("Ignored advancement '{}' in progress file {} - it doesn't exist anymore?", entry.getKey(), this.f);
                        } else {
                            this.a(advancement, (AdvancementProgress) entry.getValue());
                        }
                    }
                } catch (Throwable throwable1) {
                    throwable = throwable1;
                    throw throwable1;
                } finally {
                    if (jsonreader != null) {
                        if (throwable != null) {
                            try {
                                jsonreader.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        } else {
                            jsonreader.close();
                        }
                    }

                }
            } catch (JsonParseException jsonparseexception) {
                AdvancementDataPlayer.LOGGER.error("Couldn't parse player advancements in {}", this.f, jsonparseexception);
            } catch (IOException ioexception) {
                AdvancementDataPlayer.LOGGER.error("Couldn't access player advancements in {}", this.f, ioexception);
            }
        }

        this.c(advancementdataworld);
        this.c();
        this.b(advancementdataworld);
    }

    public void b() {
        Map<MinecraftKey, AdvancementProgress> map = Maps.newHashMap();
        Iterator iterator = this.data.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Advancement, AdvancementProgress> entry = (Entry) iterator.next();
            AdvancementProgress advancementprogress = (AdvancementProgress) entry.getValue();

            if (advancementprogress.b()) {
                map.put(((Advancement) entry.getKey()).getName(), advancementprogress);
            }
        }

        if (this.f.getParentFile() != null) {
            this.f.getParentFile().mkdirs();
        }

        JsonElement jsonelement = AdvancementDataPlayer.b.toJsonTree(map);

        jsonelement.getAsJsonObject().addProperty("DataVersion", SharedConstants.getGameVersion().getWorldVersion());

        try {
            FileOutputStream fileoutputstream = new FileOutputStream(this.f);
            Throwable throwable = null;

            try {
                OutputStreamWriter outputstreamwriter = new OutputStreamWriter(fileoutputstream, Charsets.UTF_8.newEncoder());
                Throwable throwable1 = null;

                try {
                    AdvancementDataPlayer.b.toJson(jsonelement, outputstreamwriter);
                } catch (Throwable throwable2) {
                    throwable1 = throwable2;
                    throw throwable2;
                } finally {
                    if (outputstreamwriter != null) {
                        if (throwable1 != null) {
                            try {
                                outputstreamwriter.close();
                            } catch (Throwable throwable3) {
                                throwable1.addSuppressed(throwable3);
                            }
                        } else {
                            outputstreamwriter.close();
                        }
                    }

                }
            } catch (Throwable throwable4) {
                throwable = throwable4;
                throw throwable4;
            } finally {
                if (fileoutputstream != null) {
                    if (throwable != null) {
                        try {
                            fileoutputstream.close();
                        } catch (Throwable throwable5) {
                            throwable.addSuppressed(throwable5);
                        }
                    } else {
                        fileoutputstream.close();
                    }
                }

            }
        } catch (IOException ioexception) {
            AdvancementDataPlayer.LOGGER.error("Couldn't save player advancements to {}", this.f, ioexception);
        }

    }

    public boolean grantCriteria(Advancement advancement, String s) {
        boolean flag = false;
        AdvancementProgress advancementprogress = this.getProgress(advancement);
        boolean flag1 = advancementprogress.isDone();

        if (advancementprogress.a(s)) {
            this.d(advancement);
            this.j.add(advancement);
            flag = true;
            if (!flag1 && advancementprogress.isDone()) {
                advancement.d().a(this.player);
                if (advancement.c() != null && advancement.c().i() && this.player.world.getGameRules().getBoolean(GameRules.ANNOUNCE_ADVANCEMENTS)) {
                    this.e.sendMessage(new ChatMessage("chat.type.advancement." + advancement.c().e().a(), new Object[]{this.player.getScoreboardDisplayName(), advancement.j()}), ChatMessageType.SYSTEM, SystemUtils.b);
                }
            }
        }

        if (advancementprogress.isDone()) {
            this.e(advancement);
        }

        return flag;
    }

    public boolean revokeCritera(Advancement advancement, String s) {
        boolean flag = false;
        AdvancementProgress advancementprogress = this.getProgress(advancement);

        if (advancementprogress.b(s)) {
            this.c(advancement);
            this.j.add(advancement);
            flag = true;
        }

        if (!advancementprogress.b()) {
            this.e(advancement);
        }

        return flag;
    }

    private void c(Advancement advancement) {
        AdvancementProgress advancementprogress = this.getProgress(advancement);

        if (!advancementprogress.isDone()) {
            Iterator iterator = advancement.getCriteria().entrySet().iterator();

            while (iterator.hasNext()) {
                Entry<String, Criterion> entry = (Entry) iterator.next();
                CriterionProgress criterionprogress = advancementprogress.getCriterionProgress((String) entry.getKey());

                if (criterionprogress != null && !criterionprogress.a()) {
                    CriterionInstance criterioninstance = ((Criterion) entry.getValue()).a();

                    if (criterioninstance != null) {
                        CriterionTrigger<CriterionInstance> criteriontrigger = CriterionTriggers.a(criterioninstance.a());

                        if (criteriontrigger != null) {
                            criteriontrigger.a(this, new CriterionTrigger.a<>(criterioninstance, advancement, (String) entry.getKey()));
                        }
                    }
                }
            }

        }
    }

    private void d(Advancement advancement) {
        AdvancementProgress advancementprogress = this.getProgress(advancement);
        Iterator iterator = advancement.getCriteria().entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<String, Criterion> entry = (Entry) iterator.next();
            CriterionProgress criterionprogress = advancementprogress.getCriterionProgress((String) entry.getKey());

            if (criterionprogress != null && (criterionprogress.a() || advancementprogress.isDone())) {
                CriterionInstance criterioninstance = ((Criterion) entry.getValue()).a();

                if (criterioninstance != null) {
                    CriterionTrigger<CriterionInstance> criteriontrigger = CriterionTriggers.a(criterioninstance.a());

                    if (criteriontrigger != null) {
                        criteriontrigger.b(this, new CriterionTrigger.a<>(criterioninstance, advancement, (String) entry.getKey()));
                    }
                }
            }
        }

    }

    public void b(EntityPlayer entityplayer) {
        if (this.m || !this.i.isEmpty() || !this.j.isEmpty()) {
            Map<MinecraftKey, AdvancementProgress> map = Maps.newHashMap();
            Set<Advancement> set = Sets.newLinkedHashSet();
            Set<MinecraftKey> set1 = Sets.newLinkedHashSet();
            Iterator iterator = this.j.iterator();

            Advancement advancement;

            while (iterator.hasNext()) {
                advancement = (Advancement) iterator.next();
                if (this.h.contains(advancement)) {
                    map.put(advancement.getName(), this.data.get(advancement));
                }
            }

            iterator = this.i.iterator();

            while (iterator.hasNext()) {
                advancement = (Advancement) iterator.next();
                if (this.h.contains(advancement)) {
                    set.add(advancement);
                } else {
                    set1.add(advancement.getName());
                }
            }

            if (this.m || !map.isEmpty() || !set.isEmpty() || !set1.isEmpty()) {
                entityplayer.playerConnection.sendPacket(new PacketPlayOutAdvancements(this.m, set, set1, map));
                this.i.clear();
                this.j.clear();
            }
        }

        this.m = false;
    }

    public void a(@Nullable Advancement advancement) {
        Advancement advancement1 = this.l;

        if (advancement != null && advancement.b() == null && advancement.c() != null) {
            this.l = advancement;
        } else {
            this.l = null;
        }

        if (advancement1 != this.l) {
            this.player.playerConnection.sendPacket(new PacketPlayOutSelectAdvancementTab(this.l == null ? null : this.l.getName()));
        }

    }

    public AdvancementProgress getProgress(Advancement advancement) {
        AdvancementProgress advancementprogress = (AdvancementProgress) this.data.get(advancement);

        if (advancementprogress == null) {
            advancementprogress = new AdvancementProgress();
            this.a(advancement, advancementprogress);
        }

        return advancementprogress;
    }

    private void a(Advancement advancement, AdvancementProgress advancementprogress) {
        advancementprogress.a(advancement.getCriteria(), advancement.i());
        this.data.put(advancement, advancementprogress);
    }

    private void e(Advancement advancement) {
        boolean flag = this.f(advancement);
        boolean flag1 = this.h.contains(advancement);

        if (flag && !flag1) {
            this.h.add(advancement);
            this.i.add(advancement);
            if (this.data.containsKey(advancement)) {
                this.j.add(advancement);
            }
        } else if (!flag && flag1) {
            this.h.remove(advancement);
            this.i.add(advancement);
        }

        if (flag != flag1 && advancement.b() != null) {
            this.e(advancement.b());
        }

        Iterator iterator = advancement.e().iterator();

        while (iterator.hasNext()) {
            Advancement advancement1 = (Advancement) iterator.next();

            this.e(advancement1);
        }

    }

    private boolean f(Advancement advancement) {
        for (int i = 0; advancement != null && i <= 2; ++i) {
            if (i == 0 && this.g(advancement)) {
                return true;
            }

            if (advancement.c() == null) {
                return false;
            }

            AdvancementProgress advancementprogress = this.getProgress(advancement);

            if (advancementprogress.isDone()) {
                return true;
            }

            if (advancement.c().j()) {
                return false;
            }

            advancement = advancement.b();
        }

        return false;
    }

    private boolean g(Advancement advancement) {
        AdvancementProgress advancementprogress = this.getProgress(advancement);

        if (advancementprogress.isDone()) {
            return true;
        } else {
            Iterator iterator = advancement.e().iterator();

            Advancement advancement1;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                advancement1 = (Advancement) iterator.next();
            } while (!this.g(advancement1));

            return true;
        }
    }
}
