package net.minecraft.server;

import java.util.Collection;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistentScoreboard extends PersistentBase {

    private static final Logger LOGGER = LogManager.getLogger();
    private Scoreboard b;
    private NBTTagCompound c;

    public PersistentScoreboard() {
        super("scoreboard");
    }

    public void a(Scoreboard scoreboard) {
        this.b = scoreboard;
        if (this.c != null) {
            this.a(this.c);
        }

    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        if (this.b == null) {
            this.c = nbttagcompound;
        } else {
            this.b(nbttagcompound.getList("Objectives", 10));
            this.b.a(nbttagcompound.getList("PlayerScores", 10));
            if (nbttagcompound.hasKeyOfType("DisplaySlots", 10)) {
                this.c(nbttagcompound.getCompound("DisplaySlots"));
            }

            if (nbttagcompound.hasKeyOfType("Teams", 9)) {
                this.a(nbttagcompound.getList("Teams", 10));
            }

        }
    }

    protected void a(NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
            String s = nbttagcompound.getString("Name");

            if (s.length() > 16) {
                s = s.substring(0, 16);
            }

            ScoreboardTeam scoreboardteam = this.b.createTeam(s);
            IChatMutableComponent ichatmutablecomponent = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("DisplayName"));

            if (ichatmutablecomponent != null) {
                scoreboardteam.setDisplayName(ichatmutablecomponent);
            }

            if (nbttagcompound.hasKeyOfType("TeamColor", 8)) {
                scoreboardteam.setColor(EnumChatFormat.b(nbttagcompound.getString("TeamColor")));
            }

            if (nbttagcompound.hasKeyOfType("AllowFriendlyFire", 99)) {
                scoreboardteam.setAllowFriendlyFire(nbttagcompound.getBoolean("AllowFriendlyFire"));
            }

            if (nbttagcompound.hasKeyOfType("SeeFriendlyInvisibles", 99)) {
                scoreboardteam.setCanSeeFriendlyInvisibles(nbttagcompound.getBoolean("SeeFriendlyInvisibles"));
            }

            IChatMutableComponent ichatmutablecomponent1;

            if (nbttagcompound.hasKeyOfType("MemberNamePrefix", 8)) {
                ichatmutablecomponent1 = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("MemberNamePrefix"));
                if (ichatmutablecomponent1 != null) {
                    scoreboardteam.setPrefix(ichatmutablecomponent1);
                }
            }

            if (nbttagcompound.hasKeyOfType("MemberNameSuffix", 8)) {
                ichatmutablecomponent1 = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("MemberNameSuffix"));
                if (ichatmutablecomponent1 != null) {
                    scoreboardteam.setSuffix(ichatmutablecomponent1);
                }
            }

            ScoreboardTeamBase.EnumNameTagVisibility scoreboardteambase_enumnametagvisibility;

            if (nbttagcompound.hasKeyOfType("NameTagVisibility", 8)) {
                scoreboardteambase_enumnametagvisibility = ScoreboardTeamBase.EnumNameTagVisibility.a(nbttagcompound.getString("NameTagVisibility"));
                if (scoreboardteambase_enumnametagvisibility != null) {
                    scoreboardteam.setNameTagVisibility(scoreboardteambase_enumnametagvisibility);
                }
            }

            if (nbttagcompound.hasKeyOfType("DeathMessageVisibility", 8)) {
                scoreboardteambase_enumnametagvisibility = ScoreboardTeamBase.EnumNameTagVisibility.a(nbttagcompound.getString("DeathMessageVisibility"));
                if (scoreboardteambase_enumnametagvisibility != null) {
                    scoreboardteam.setDeathMessageVisibility(scoreboardteambase_enumnametagvisibility);
                }
            }

            if (nbttagcompound.hasKeyOfType("CollisionRule", 8)) {
                ScoreboardTeamBase.EnumTeamPush scoreboardteambase_enumteampush = ScoreboardTeamBase.EnumTeamPush.a(nbttagcompound.getString("CollisionRule"));

                if (scoreboardteambase_enumteampush != null) {
                    scoreboardteam.setCollisionRule(scoreboardteambase_enumteampush);
                }
            }

            this.a(scoreboardteam, nbttagcompound.getList("Players", 8));
        }

    }

    protected void a(ScoreboardTeam scoreboardteam, NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.size(); ++i) {
            this.b.addPlayerToTeam(nbttaglist.getString(i), scoreboardteam);
        }

    }

    protected void c(NBTTagCompound nbttagcompound) {
        for (int i = 0; i < 19; ++i) {
            if (nbttagcompound.hasKeyOfType("slot_" + i, 8)) {
                String s = nbttagcompound.getString("slot_" + i);
                ScoreboardObjective scoreboardobjective = this.b.getObjective(s);

                this.b.setDisplaySlot(i, scoreboardobjective);
            }
        }

    }

    protected void b(NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);

            IScoreboardCriteria.a(nbttagcompound.getString("CriteriaName")).ifPresent((iscoreboardcriteria) -> {
                String s = nbttagcompound.getString("Name");

                if (s.length() > 16) {
                    s = s.substring(0, 16);
                }

                IChatMutableComponent ichatmutablecomponent = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("DisplayName"));
                IScoreboardCriteria.EnumScoreboardHealthDisplay iscoreboardcriteria_enumscoreboardhealthdisplay = IScoreboardCriteria.EnumScoreboardHealthDisplay.a(nbttagcompound.getString("RenderType"));

                this.b.registerObjective(s, iscoreboardcriteria, ichatmutablecomponent, iscoreboardcriteria_enumscoreboardhealthdisplay);
            });
        }

    }

    @Override
    public NBTTagCompound b(NBTTagCompound nbttagcompound) {
        if (this.b == null) {
            PersistentScoreboard.LOGGER.warn("Tried to save scoreboard without having a scoreboard...");
            return nbttagcompound;
        } else {
            nbttagcompound.set("Objectives", this.e());
            nbttagcompound.set("PlayerScores", this.b.i());
            nbttagcompound.set("Teams", this.a());
            this.d(nbttagcompound);
            return nbttagcompound;
        }
    }

    protected NBTTagList a() {
        NBTTagList nbttaglist = new NBTTagList();
        Collection<ScoreboardTeam> collection = this.b.getTeams();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreboardTeam scoreboardteam = (ScoreboardTeam) iterator.next();
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            nbttagcompound.setString("Name", scoreboardteam.getName());
            nbttagcompound.setString("DisplayName", IChatBaseComponent.ChatSerializer.a(scoreboardteam.getDisplayName()));
            if (scoreboardteam.getColor().b() >= 0) {
                nbttagcompound.setString("TeamColor", scoreboardteam.getColor().f());
            }

            nbttagcompound.setBoolean("AllowFriendlyFire", scoreboardteam.allowFriendlyFire());
            nbttagcompound.setBoolean("SeeFriendlyInvisibles", scoreboardteam.canSeeFriendlyInvisibles());
            nbttagcompound.setString("MemberNamePrefix", IChatBaseComponent.ChatSerializer.a(scoreboardteam.getPrefix()));
            nbttagcompound.setString("MemberNameSuffix", IChatBaseComponent.ChatSerializer.a(scoreboardteam.getSuffix()));
            nbttagcompound.setString("NameTagVisibility", scoreboardteam.getNameTagVisibility().e);
            nbttagcompound.setString("DeathMessageVisibility", scoreboardteam.getDeathMessageVisibility().e);
            nbttagcompound.setString("CollisionRule", scoreboardteam.getCollisionRule().e);
            NBTTagList nbttaglist1 = new NBTTagList();
            Iterator iterator1 = scoreboardteam.getPlayerNameSet().iterator();

            while (iterator1.hasNext()) {
                String s = (String) iterator1.next();

                nbttaglist1.add(NBTTagString.a(s));
            }

            nbttagcompound.set("Players", nbttaglist1);
            nbttaglist.add(nbttagcompound);
        }

        return nbttaglist;
    }

    protected void d(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        boolean flag = false;

        for (int i = 0; i < 19; ++i) {
            ScoreboardObjective scoreboardobjective = this.b.getObjectiveForSlot(i);

            if (scoreboardobjective != null) {
                nbttagcompound1.setString("slot_" + i, scoreboardobjective.getName());
                flag = true;
            }
        }

        if (flag) {
            nbttagcompound.set("DisplaySlots", nbttagcompound1);
        }

    }

    protected NBTTagList e() {
        NBTTagList nbttaglist = new NBTTagList();
        Collection<ScoreboardObjective> collection = this.b.getObjectives();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreboardObjective scoreboardobjective = (ScoreboardObjective) iterator.next();

            if (scoreboardobjective.getCriteria() != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.setString("Name", scoreboardobjective.getName());
                nbttagcompound.setString("CriteriaName", scoreboardobjective.getCriteria().getName());
                nbttagcompound.setString("DisplayName", IChatBaseComponent.ChatSerializer.a(scoreboardobjective.getDisplayName()));
                nbttagcompound.setString("RenderType", scoreboardobjective.getRenderType().a());
                nbttaglist.add(nbttagcompound);
            }
        }

        return nbttaglist;
    }
}
