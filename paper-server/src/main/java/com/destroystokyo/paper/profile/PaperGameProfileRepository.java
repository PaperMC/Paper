package com.destroystokyo.paper.profile;

import com.mojang.authlib.Environment;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.yggdrasil.YggdrasilGameProfileRepository;
import java.net.Proxy;

public class PaperGameProfileRepository extends YggdrasilGameProfileRepository {
    public PaperGameProfileRepository(Proxy proxy, Environment environment) {
        super(proxy, environment);
    }

    @Override
    public void findProfilesByNames(String[] names, ProfileLookupCallback callback) {
        super.findProfilesByNames(names, callback);
    }
}
