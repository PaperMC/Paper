package net.minecraft.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;
import java.util.UUID;

public class ServerPing {

    private IChatBaseComponent a;
    private ServerPing.ServerPingPlayerSample b;
    private ServerPing.ServerData c;
    private String d;

    public ServerPing() {}

    public IChatBaseComponent a() {
        return this.a;
    }

    public void setMOTD(IChatBaseComponent ichatbasecomponent) {
        this.a = ichatbasecomponent;
    }

    public ServerPing.ServerPingPlayerSample b() {
        return this.b;
    }

    public void setPlayerSample(ServerPing.ServerPingPlayerSample serverping_serverpingplayersample) {
        this.b = serverping_serverpingplayersample;
    }

    public ServerPing.ServerData getServerData() {
        return this.c;
    }

    public void setServerInfo(ServerPing.ServerData serverping_serverdata) {
        this.c = serverping_serverdata;
    }

    public void setFavicon(String s) {
        this.d = s;
    }

    public String d() {
        return this.d;
    }

    public static class Serializer implements JsonDeserializer<ServerPing>, JsonSerializer<ServerPing> {

        public Serializer() {}

        public ServerPing deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = ChatDeserializer.m(jsonelement, "status");
            ServerPing serverping = new ServerPing();

            if (jsonobject.has("description")) {
                serverping.setMOTD((IChatBaseComponent) jsondeserializationcontext.deserialize(jsonobject.get("description"), IChatBaseComponent.class));
            }

            if (jsonobject.has("players")) {
                serverping.setPlayerSample((ServerPing.ServerPingPlayerSample) jsondeserializationcontext.deserialize(jsonobject.get("players"), ServerPing.ServerPingPlayerSample.class));
            }

            if (jsonobject.has("version")) {
                serverping.setServerInfo((ServerPing.ServerData) jsondeserializationcontext.deserialize(jsonobject.get("version"), ServerPing.ServerData.class));
            }

            if (jsonobject.has("favicon")) {
                serverping.setFavicon(ChatDeserializer.h(jsonobject, "favicon"));
            }

            return serverping;
        }

        public JsonElement serialize(ServerPing serverping, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            if (serverping.a() != null) {
                jsonobject.add("description", jsonserializationcontext.serialize(serverping.a()));
            }

            if (serverping.b() != null) {
                jsonobject.add("players", jsonserializationcontext.serialize(serverping.b()));
            }

            if (serverping.getServerData() != null) {
                jsonobject.add("version", jsonserializationcontext.serialize(serverping.getServerData()));
            }

            if (serverping.d() != null) {
                jsonobject.addProperty("favicon", serverping.d());
            }

            return jsonobject;
        }
    }

    public static class ServerData {

        private final String a;
        private final int b;

        public ServerData(String s, int i) {
            this.a = s;
            this.b = i;
        }

        public String a() {
            return this.a;
        }

        public int getProtocolVersion() {
            return this.b;
        }

        public static class Serializer implements JsonDeserializer<ServerPing.ServerData>, JsonSerializer<ServerPing.ServerData> {

            public Serializer() {}

            public ServerPing.ServerData deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
                JsonObject jsonobject = ChatDeserializer.m(jsonelement, "version");

                return new ServerPing.ServerData(ChatDeserializer.h(jsonobject, "name"), ChatDeserializer.n(jsonobject, "protocol"));
            }

            public JsonElement serialize(ServerPing.ServerData serverping_serverdata, Type type, JsonSerializationContext jsonserializationcontext) {
                JsonObject jsonobject = new JsonObject();

                jsonobject.addProperty("name", serverping_serverdata.a());
                jsonobject.addProperty("protocol", serverping_serverdata.getProtocolVersion());
                return jsonobject;
            }
        }
    }

    public static class ServerPingPlayerSample {

        private final int a;
        private final int b;
        private GameProfile[] c;

        public ServerPingPlayerSample(int i, int j) {
            this.a = i;
            this.b = j;
        }

        public int a() {
            return this.a;
        }

        public int b() {
            return this.b;
        }

        public GameProfile[] c() {
            return this.c;
        }

        public void a(GameProfile[] agameprofile) {
            this.c = agameprofile;
        }

        public static class Serializer implements JsonDeserializer<ServerPing.ServerPingPlayerSample>, JsonSerializer<ServerPing.ServerPingPlayerSample> {

            public Serializer() {}

            public ServerPing.ServerPingPlayerSample deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
                JsonObject jsonobject = ChatDeserializer.m(jsonelement, "players");
                ServerPing.ServerPingPlayerSample serverping_serverpingplayersample = new ServerPing.ServerPingPlayerSample(ChatDeserializer.n(jsonobject, "max"), ChatDeserializer.n(jsonobject, "online"));

                if (ChatDeserializer.d(jsonobject, "sample")) {
                    JsonArray jsonarray = ChatDeserializer.u(jsonobject, "sample");

                    if (jsonarray.size() > 0) {
                        GameProfile[] agameprofile = new GameProfile[jsonarray.size()];

                        for (int i = 0; i < agameprofile.length; ++i) {
                            JsonObject jsonobject1 = ChatDeserializer.m(jsonarray.get(i), "player[" + i + "]");
                            String s = ChatDeserializer.h(jsonobject1, "id");

                            agameprofile[i] = new GameProfile(UUID.fromString(s), ChatDeserializer.h(jsonobject1, "name"));
                        }

                        serverping_serverpingplayersample.a(agameprofile);
                    }
                }

                return serverping_serverpingplayersample;
            }

            public JsonElement serialize(ServerPing.ServerPingPlayerSample serverping_serverpingplayersample, Type type, JsonSerializationContext jsonserializationcontext) {
                JsonObject jsonobject = new JsonObject();

                jsonobject.addProperty("max", serverping_serverpingplayersample.a());
                jsonobject.addProperty("online", serverping_serverpingplayersample.b());
                if (serverping_serverpingplayersample.c() != null && serverping_serverpingplayersample.c().length > 0) {
                    JsonArray jsonarray = new JsonArray();

                    for (int i = 0; i < serverping_serverpingplayersample.c().length; ++i) {
                        JsonObject jsonobject1 = new JsonObject();
                        UUID uuid = serverping_serverpingplayersample.c()[i].getId();

                        jsonobject1.addProperty("id", uuid == null ? "" : uuid.toString());
                        jsonobject1.addProperty("name", serverping_serverpingplayersample.c()[i].getName());
                        jsonarray.add(jsonobject1);
                    }

                    jsonobject.add("sample", jsonarray);
                }

                return jsonobject;
            }
        }
    }
}
