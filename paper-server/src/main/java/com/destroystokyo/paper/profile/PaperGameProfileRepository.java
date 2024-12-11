package com.destroystokyo.paper.profile;

import com.destroystokyo.paper.event.profile.LookupProfileEvent;
import com.destroystokyo.paper.event.profile.PreLookupProfileEvent;
import com.google.common.collect.Sets;
import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.yggdrasil.YggdrasilGameProfileRepository;
import java.net.Proxy;
import java.util.Set;

public class PaperGameProfileRepository extends YggdrasilGameProfileRepository {
    public PaperGameProfileRepository(Proxy proxy, Environment environment) {
        super(proxy, environment);
    }

    @Override
    public void findProfilesByNames(String[] names, ProfileLookupCallback callback) {
        Set<String> unfoundNames = Sets.newHashSet();
        for (String name : names) {
            PreLookupProfileEvent event = new PreLookupProfileEvent(name);
            event.callEvent();
            if (event.getUUID() != null) {
                // Plugin provided UUID, we can skip network call.
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
            super.findProfilesByNames(namesArr, new PreProfileLookupCallback(callback));
        }
    }

    private record PreProfileLookupCallback(ProfileLookupCallback callback) implements ProfileLookupCallback {
        @Override
        public void onProfileLookupSucceeded(GameProfile gameProfile) {
            PlayerProfile from = CraftPlayerProfile.asBukkitMirror(gameProfile);
            new LookupProfileEvent(from).callEvent();
            this.callback.onProfileLookupSucceeded(gameProfile);
        }

        @Override
        public void onProfileLookupFailed(final String profileName, final Exception exception) {
            this.callback.onProfileLookupFailed(profileName, exception);
        }
    }
}
