package com.destroystokyo.paper.profile;

import com.destroystokyo.paper.event.profile.LookupProfileEvent;
import com.destroystokyo.paper.event.profile.PreLookupProfileEvent;
import com.google.common.collect.Sets;
import com.mojang.authlib.Environment;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.yggdrasil.YggdrasilGameProfileRepository;
import com.mojang.authlib.yggdrasil.response.NameAndId;
import java.net.Proxy;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
                callback.onProfileLookupSucceeded(name, event.getUUID());
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

    @Override
    public Optional<NameAndId> findProfileByName(final String name) {
        PreLookupProfileEvent event = new PreLookupProfileEvent(name);
        event.callEvent();
        if (event.getUUID() != null) {
            // Plugin provided UUID, we can skip network call.
            return Optional.of(new NameAndId(event.getUUID(), name));
        }
        return super.findProfileByName(name);
    }

    private record PreProfileLookupCallback(ProfileLookupCallback callback) implements ProfileLookupCallback {
        @Override
        public void onProfileLookupSucceeded(final String  profileName, final UUID profileId) {
            PlayerProfile from = new CraftPlayerProfile(profileId, profileName);
            new LookupProfileEvent(from).callEvent();
            this.callback.onProfileLookupSucceeded(profileName, profileId);
        }

        @Override
        public void onProfileLookupFailed(final String profileName, final Exception exception) {
            this.callback.onProfileLookupFailed(profileName, exception);
        }
    }
}
