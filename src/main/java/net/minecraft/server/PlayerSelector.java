package net.minecraft.server;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerSelector {

    private static final Pattern a = Pattern.compile("^@([parf])(?:\\[([\\w=,!-]*)\\])?$");
    private static final Pattern b = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
    private static final Pattern c = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");

    public static EntityPlayer getPlayer(ICommandListener icommandlistener, String s) {
        EntityPlayer[] aentityplayer = getPlayers(icommandlistener, s);

        return aentityplayer != null && aentityplayer.length == 1 ? aentityplayer[0] : null;
    }

    public static IChatBaseComponent getPlayerNames(ICommandListener icommandlistener, String s) {
        EntityPlayer[] aentityplayer = getPlayers(icommandlistener, s);

        if (aentityplayer != null && aentityplayer.length != 0) {
            IChatBaseComponent[] aichatbasecomponent = new IChatBaseComponent[aentityplayer.length];

            for (int i = 0; i < aichatbasecomponent.length; ++i) {
                aichatbasecomponent[i] = aentityplayer[i].getScoreboardDisplayName();
            }

            return CommandAbstract.a(aichatbasecomponent);
        } else {
            return null;
        }
    }

    public static EntityPlayer[] getPlayers(ICommandListener icommandlistener, String s) {
        // CraftBukkit start
        if (!(icommandlistener instanceof CommandBlockListenerAbstract)) {
            return null;
        }
        // CraftBukkit end

        Matcher matcher = a.matcher(s);

        if (!matcher.matches()) {
            return null;
        } else {
            Map map = h(matcher.group(2));
            String s1 = matcher.group(1);
            int i = c(s1);
            int j = d(s1);
            int k = f(s1);
            int l = e(s1);
            int i1 = g(s1);
            int j1 = EnumGamemode.NONE.a();
            ChunkCoordinates chunkcoordinates = icommandlistener.getChunkCoordinates();
            Map map1 = a(map);
            String s2 = null;
            String s3 = null;
            boolean flag = false;

            if (map.containsKey("rm")) {
                i = MathHelper.a((String) map.get("rm"), i);
                flag = true;
            }

            if (map.containsKey("r")) {
                j = MathHelper.a((String) map.get("r"), j);
                flag = true;
            }

            if (map.containsKey("lm")) {
                k = MathHelper.a((String) map.get("lm"), k);
            }

            if (map.containsKey("l")) {
                l = MathHelper.a((String) map.get("l"), l);
            }

            if (map.containsKey("x")) {
                chunkcoordinates.x = MathHelper.a((String) map.get("x"), chunkcoordinates.x);
                flag = true;
            }

            if (map.containsKey("y")) {
                chunkcoordinates.y = MathHelper.a((String) map.get("y"), chunkcoordinates.y);
                flag = true;
            }

            if (map.containsKey("z")) {
                chunkcoordinates.z = MathHelper.a((String) map.get("z"), chunkcoordinates.z);
                flag = true;
            }

            if (map.containsKey("m")) {
                j1 = MathHelper.a((String) map.get("m"), j1);
            }

            if (map.containsKey("c")) {
                i1 = MathHelper.a((String) map.get("c"), i1);
            }

            if (map.containsKey("team")) {
                s3 = (String) map.get("team");
            }

            if (map.containsKey("name")) {
                s2 = (String) map.get("name");
            }

            World world = flag ? icommandlistener.getWorld() : null;
            List list;

            if (!s1.equals("p") && !s1.equals("a")) {
                if (!s1.equals("r")) {
                    return null;
                } else {
                    list = MinecraftServer.getServer().getPlayerList().a(chunkcoordinates, i, j, 0, j1, k, l, map1, s2, s3, world);
                    Collections.shuffle(list);
                    list = list.subList(0, Math.min(i1, list.size()));
                    return list != null && !list.isEmpty() ? (EntityPlayer[]) list.toArray(new EntityPlayer[0]) : new EntityPlayer[0];
                }
            } else {
                list = MinecraftServer.getServer().getPlayerList().a(chunkcoordinates, i, j, i1, j1, k, l, map1, s2, s3, world);
                return list != null && !list.isEmpty() ? (EntityPlayer[]) list.toArray(new EntityPlayer[0]) : new EntityPlayer[0];
            }
        }
    }

    public static Map a(Map map) {
        HashMap hashmap = new HashMap();
        Iterator iterator = map.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            if (s.startsWith("score_") && s.length() > "score_".length()) {
                String s1 = s.substring("score_".length());

                hashmap.put(s1, Integer.valueOf(MathHelper.a((String) map.get(s), 1)));
            }
        }

        return hashmap;
    }

    public static boolean isList(String s) {
        Matcher matcher = a.matcher(s);

        if (matcher.matches()) {
            Map map = h(matcher.group(2));
            String s1 = matcher.group(1);
            int i = g(s1);

            if (map.containsKey("c")) {
                i = MathHelper.a((String) map.get("c"), i);
            }

            return i != 1;
        } else {
            return false;
        }
    }

    public static boolean isPattern(String s, String s1) {
        Matcher matcher = a.matcher(s);

        if (matcher.matches()) {
            String s2 = matcher.group(1);

            return s1 == null || s1.equals(s2);
        } else {
            return false;
        }
    }

    public static boolean isPattern(String s) {
        return isPattern(s, (String) null);
    }

    private static final int c(String s) {
        return 0;
    }

    private static final int d(String s) {
        return 0;
    }

    private static final int e(String s) {
        return Integer.MAX_VALUE;
    }

    private static final int f(String s) {
        return 0;
    }

    private static final int g(String s) {
        return s.equals("a") ? 0 : 1;
    }

    private static Map h(String s) {
        HashMap hashmap = new HashMap();

        if (s == null) {
            return hashmap;
        } else {
            Matcher matcher = b.matcher(s);
            int i = 0;

            int j;

            for (j = -1; matcher.find(); j = matcher.end()) {
                String s1 = null;

                switch (i++) {
                case 0:
                    s1 = "x";
                    break;

                case 1:
                    s1 = "y";
                    break;

                case 2:
                    s1 = "z";
                    break;

                case 3:
                    s1 = "r";
                }

                if (s1 != null && matcher.group(1).length() > 0) {
                    hashmap.put(s1, matcher.group(1));
                }
            }

            if (j < s.length()) {
                matcher = c.matcher(j == -1 ? s : s.substring(j));

                while (matcher.find()) {
                    hashmap.put(matcher.group(1), matcher.group(2));
                }
            }

            return hashmap;
        }
    }
}
