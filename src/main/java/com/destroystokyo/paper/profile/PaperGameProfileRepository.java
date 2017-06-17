package com.destroystokyo.paper.profile;

import com.destroystokyo.paper.event.profile.LookupProfileEvent;
import com.destroystokyo.paper.event.profile.PreLookupProfileEvent;
import com.google.common.collect.Sets;
import com.mojang.authlib.Agent;
import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilGameProfileRepository;

import java.util.Set;
public class PaperGameProfileRepository extends YggdrasilGameProfileRepository {
    public PaperGameProfileRepository(YggdrasilAuthenticationService authenticationService, Environment environment) {
        super(authenticationService, environment);
    }

    @Override
    public void findProfilesByNames(String[] names, Agent agent, ProfileLookupCallback callback) {
        Set<String> unfoundNames = Sets.newHashSet();
        for (String name : names) {
            PreLookupProfileEvent event = new PreLookupProfileEvent(name);
            event.callEvent();
            if (event.getUUID() != null) {
                // Plugin provided UUI, we can skip network call.
                GameProfile gameprofile = new GameProfile(event.getUUID(), name);
                // We might even have properties!
                Set<ProfileProperty> profileProperties = event.getProfileProperties();
                if (!profileProperties.isEmpty()) {
                    for (ProfileProperty property : profileProperties) {
                        gameprofile.getProperties().put(property.getName(), CraftPlayerProfile.asAuthlib(property));
                    }
                }
                callback.onProfileLookupSucceeded(gameprofile);
            } else {
                unfoundNames.add(name);
            }
        }

        // Some things were not found.... Proceed to look up.
        if (!unfoundNames.isEmpty()) {
            String[] namesArr = unfoundNames.toArray(new String[unfoundNames.size()]);
            super.findProfilesByNames(namesArr, agent, new PreProfileLookupCallback(callback));
        }
    }

    private static class PreProfileLookupCallback implements ProfileLookupCallback {
        private final ProfileLookupCallback callback;

        PreProfileLookupCallback(ProfileLookupCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onProfileLookupSucceeded(GameProfile gameProfile) {
            PlayerProfile from = CraftPlayerProfile.asBukkitMirror(gameProfile);
            new LookupProfileEvent(from).callEvent();
            callback.onProfileLookupSucceeded(gameProfile);
        }

        @Override
        public void onProfileLookupFailed(GameProfile gameProfile, Exception e) {
            callback.onProfileLookupFailed(gameProfile, e);
        }
    }
}
