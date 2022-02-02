package org.bukkit.craftbukkit.profile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.junit.Assert;
import org.junit.Test;

public class PlayerProfileTest {

    /*
        {
          "timestamp" : 1636282649111,
          "profileId" : "29a4042b05ab4c7294607aa3b567e8da",
          "profileName" : "DerFrZocker",
          "signatureRequired" : true,
          "textures" : {
            "SKIN" : {
              "url" : "http://textures.minecraft.net/texture/284dbf60700b9882c0c2ad1943b515cc111f0b4e562a9a36682495636d846754",
              "metadata" : {
                "model" : "slim"
              }
            },
            "CAPE" : {
              "url" : "http://textures.minecraft.net/texture/2340c0e03dd24a11b15a8b33c2a7e9e32abb2051b2481d0ba7defd635ca7a933"
            }
          }
        }
     */
    private static final UUID UNIQUE_ID = UUID.fromString("29a4042b-05ab-4c72-9460-7aa3b567e8da");
    private static final String NAME = "DerFrZocker";
    private static final URL SKIN;
    private static final URL CAPE;
    static {
        try {
            SKIN = new URL("http://textures.minecraft.net/texture/284dbf60700b9882c0c2ad1943b515cc111f0b4e562a9a36682495636d846754");
            CAPE = new URL("http://textures.minecraft.net/texture/2340c0e03dd24a11b15a8b33c2a7e9e32abb2051b2481d0ba7defd635ca7a933");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    private static final long TIMESTAMP = 1636282649111L;
    private static final String VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTYzNjI4MjY0OTExMSwKICAicHJvZmlsZUlkIiA6ICIyOWE0MDQyYjA1YWI0YzcyOTQ2MDdhYTNiNTY3ZThkYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJEZXJGclpvY2tlciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yODRkYmY2MDcwMGI5ODgyYzBjMmFkMTk0M2I1MTVjYzExMWYwYjRlNTYyYTlhMzY2ODI0OTU2MzZkODQ2NzU0IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM0MGMwZTAzZGQyNGExMWIxNWE4YjMzYzJhN2U5ZTMyYWJiMjA1MWIyNDgxZDBiYTdkZWZkNjM1Y2E3YTkzMyIKICAgIH0KICB9Cn0=";
    private static final String SIGNATURE = "lci91bn2RkJyQ6gGlfTaTJW3afJopeB5Sud2cgVlQJgEvL3j7kIvnCls+2otzVnI6tzbzoXSHbSnfxs7QW+Rv8bGcoH3lAC8UAkwu6ZOLjSxf3e0l4VJJ5lQncI8PG75tGQuQTldnriAhvtV6Q5c0J7aef0bpin+N31NChh/JdZcjpz9zKXkbNph/sZybGY9OlzBcn0Wd8ZVJOKzTLRKtjC8Z7Eu1pd6ZY6WgAoM+nwzag4EAwk+5HhZxSw/r8tentoGK/6/r8oleIDMJVxDPOglnJoFQJMKjC5nrsNBYx59O7I89JDN02jNIdPSdfPwnbgSiaPzIb+o9AA775iDBsF1bPIZ99dc2cXggVA10eQhSaSWRwfDQ0kkiv9YmdKuPpNhewbmTF4bGz0H3v71pOMHT6bvV5qq7IT3XgqK3YwDrIxH2kpE2K6jsbldjDF2uKs0DPDkjPZArT0L/TxwEf02QzLVxU3ctCk6J7VvGQHTqF9vQHnJLWQNjoXG2W4NfPtH2IaYqiecX0PMc6eL+5RtlCs6viRawx8gOjSEKs3MtvV3BqWB3EDFUc1quuLEiDS3R2NSVScOS7CWhiQWCeh2fjm4lnPHA9OmhoMZcnuy0sdPMDu2Omjd8vVZDv/mqlf6Z7O8+mQSockpOFHmaYhTIGO3qRjdMmQdB3YGLVE=";
    // {"textures":{"SKIN":{"url":"http://textures.minecraft.net/texture/b72144309873464f239d9ae0ec49d2e7f9670552cda8a7a85a76282dd09e14dd"}}}
    private static final String COMPACT_VALUE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjg0ZGJmNjA3MDBiOTg4MmMwYzJhZDE5NDNiNTE1Y2MxMTFmMGI0ZTU2MmE5YTM2NjgyNDk1NjM2ZDg0Njc1NCJ9fX0=";

    private static CraftPlayerProfile buildPlayerProfile() {
        GameProfile gameProfile = new GameProfile(UNIQUE_ID, NAME);
        gameProfile.getProperties().put(CraftPlayerTextures.PROPERTY_NAME, new Property(CraftPlayerTextures.PROPERTY_NAME, VALUE, SIGNATURE));
        return new CraftPlayerProfile(gameProfile);
    }

    @Test
    public void testProvidedValues() {
        Property property = new Property(CraftPlayerTextures.PROPERTY_NAME, VALUE, SIGNATURE);
        Assert.assertTrue("Invalid test property signature, has the public key changed?", CraftProfileProperty.hasValidSignature(property));
    }

    @Test
    public void testProfileCreation() {
        // Invalid profiles:
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new CraftPlayerProfile(null, null);
        });
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new CraftPlayerProfile(null, "");
        });
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new CraftPlayerProfile(null, " ");
        });

        // Valid profiles:
        new CraftPlayerProfile(UNIQUE_ID, null);
        new CraftPlayerProfile(null, NAME);
        new CraftPlayerProfile(UNIQUE_ID, NAME);
    }

    @Test
    public void testGameProfileWrapping() {
        // Invalid profiles:
        Assert.assertThrows(NullPointerException.class, () -> {
            new CraftPlayerProfile(null);
        });

        // Valid profiles:
        CraftPlayerProfile profile1 = new CraftPlayerProfile(new GameProfile(UNIQUE_ID, NAME));
        Assert.assertEquals("Unique id is not the same", UNIQUE_ID, profile1.getUniqueId());
        Assert.assertEquals("Name is not the same", NAME, profile1.getName());

        CraftPlayerProfile profile2 = new CraftPlayerProfile(new GameProfile(UNIQUE_ID, null));
        Assert.assertEquals("Unique id is not the same", UNIQUE_ID, profile2.getUniqueId());
        Assert.assertEquals("Name is not null", null, profile2.getName());

        CraftPlayerProfile profile3 = new CraftPlayerProfile(new GameProfile(null, NAME));
        Assert.assertEquals("Unique id is not null", null, profile3.getUniqueId());
        Assert.assertEquals("Name is not the same", NAME, profile3.getName());
    }

    @Test
    public void testTexturesLoading() {
        CraftPlayerProfile profile = buildPlayerProfile();
        Assert.assertEquals("Unique id is not the same", UNIQUE_ID, profile.getUniqueId());
        Assert.assertEquals("Name is not the same", NAME, profile.getName());
        Assert.assertEquals("Skin url is not the same", SKIN, profile.getTextures().getSkin());
        Assert.assertEquals("Skin model is not the same", PlayerTextures.SkinModel.SLIM, profile.getTextures().getSkinModel());
        Assert.assertEquals("Cape url is not the same", CAPE, profile.getTextures().getCape());
        Assert.assertEquals("Timestamp is not the same", TIMESTAMP, profile.getTextures().getTimestamp());
    }

    @Test
    public void testBuildGameProfile() {
        CraftPlayerProfile profile = buildPlayerProfile();
        GameProfile gameProfile = profile.buildGameProfile();
        Assert.assertNotNull("GameProfile is null", gameProfile);

        Property property = CraftPlayerProfile.getProperty(gameProfile, CraftPlayerTextures.PROPERTY_NAME);
        Assert.assertNotNull("Textures property is null", property);
        Assert.assertEquals("Property values are not the same", VALUE, property.getValue());
        Assert.assertEquals("Names are not the same", NAME, gameProfile.getName());
        Assert.assertEquals("Unique ids are not the same", UNIQUE_ID, gameProfile.getId());
        Assert.assertTrue("Signature is missing", property.hasSignature());
        Assert.assertTrue("Signature is not valid", CraftProfileProperty.hasValidSignature(property));
    }

    @Test
    public void testBuildGameProfileReturnsNewInstance() {
        CraftPlayerProfile profile = buildPlayerProfile();
        GameProfile gameProfile1 = profile.buildGameProfile();
        GameProfile gameProfile2 = profile.buildGameProfile();
        Assert.assertTrue("CraftPlayerProfile#buildGameProfile() does not produce a new instance", gameProfile1 != gameProfile2);
    }

    @Test
    public void testSignatureValidation() {
        CraftPlayerProfile profile = buildPlayerProfile();
        Assert.assertTrue("Signature is not valid", profile.getTextures().isSigned());
    }

    @Test
    public void testSignatureInvalidation() {
        CraftPlayerProfile profile = buildPlayerProfile();
        profile.getTextures().setSkin(null);
        Assert.assertTrue("Textures has a timestamp", profile.getTextures().getTimestamp() == 0L);
        Assert.assertTrue("Textures signature is valid", !profile.getTextures().isSigned());

        // Ensure that the invalidation is preserved when the property is rebuilt:
        profile.rebuildDirtyProperties();
        Assert.assertTrue("Rebuilt textures has a timestamp", profile.getTextures().getTimestamp() == 0L);
        Assert.assertTrue("Rebuilt textures signature is valid", !profile.getTextures().isSigned());
    }

    @Test
    public void testSetSkinResetsSkinModel() {
        CraftPlayerProfile profile = buildPlayerProfile();
        Assert.assertEquals("Skin model is not the same", PlayerTextures.SkinModel.SLIM, profile.getTextures().getSkinModel());
        profile.getTextures().setSkin(SKIN);
        Assert.assertEquals("Skin model was not reset by skin change", PlayerTextures.SkinModel.CLASSIC, profile.getTextures().getSkinModel());
    }

    @Test
    public void testSetTextures() {
        CraftPlayerProfile profile = buildPlayerProfile();
        CraftPlayerProfile profile2 = new CraftPlayerProfile(new GameProfile(UNIQUE_ID, NAME));

        Assert.assertTrue("profile has no textures", !profile.getTextures().isEmpty());
        Assert.assertTrue("profile2 has textures", profile2.getTextures().isEmpty());

        profile2.setTextures(profile.getTextures());
        Assert.assertTrue("profile2 has no textures", !profile2.getTextures().isEmpty());
        Assert.assertEquals("copied profile textures are not the same", profile.getTextures(), profile2.getTextures());

        profile2.setTextures(null);
        Assert.assertTrue("cleared profile2 has textures", profile2.getTextures().isEmpty());
        Assert.assertTrue("cleared profile2 has textures timestamp", profile2.getTextures().getTimestamp() == 0L);
        Assert.assertTrue("cleared profile2 has signed textures", !profile2.getTextures().isSigned());
    }

    @Test
    public void testClearTextures() {
        CraftPlayerProfile profile = buildPlayerProfile();
        Assert.assertTrue("profile has no textures", !profile.getTextures().isEmpty());

        profile.getTextures().clear();
        Assert.assertTrue("cleared profile has textures", profile.getTextures().isEmpty());
        Assert.assertTrue("cleared profile has textures timestamp", profile.getTextures().getTimestamp() == 0L);
        Assert.assertTrue("cleared profile has signed textures", !profile.getTextures().isSigned());
    }

    @Test
    public void testCustomSkin() {
        CraftPlayerProfile profile = new CraftPlayerProfile(UNIQUE_ID, NAME);
        profile.getTextures().setSkin(SKIN);
        Assert.assertEquals("profile with custom skin does not match expected value", COMPACT_VALUE, profile.getTextures().getProperty().getValue());
    }

    @Test
    public void testEquals() {
        CraftPlayerProfile profile1 = buildPlayerProfile();
        CraftPlayerProfile profile2 = buildPlayerProfile();
        CraftPlayerProfile profile3 = new CraftPlayerProfile(new GameProfile(UNIQUE_ID, NAME));
        CraftPlayerProfile profile4 = new CraftPlayerProfile(new GameProfile(UNIQUE_ID, NAME));
        CraftPlayerProfile profile5 = new CraftPlayerProfile(new GameProfile(UNIQUE_ID, null));
        CraftPlayerProfile profile6 = new CraftPlayerProfile(new GameProfile(null, NAME));

        Assert.assertEquals("profile1 and profile2 are not equal", profile1, profile2);
        Assert.assertEquals("profile3 and profile4 are not equal", profile3, profile4);
        Assert.assertNotEquals("profile1 and profile3 are equal", profile1, profile3);
        Assert.assertNotEquals("profile4 and profile5 are equal", profile4, profile5);
        Assert.assertNotEquals("profile4 and profile6 are equal", profile4, profile6);
    }

    @Test
    public void testTexturesEquals() {
        CraftPlayerProfile profile1 = buildPlayerProfile();
        CraftPlayerProfile profile2 = buildPlayerProfile();
        Assert.assertEquals("Profile textures are not equal", profile1.getTextures(), profile2.getTextures());

        profile1.getTextures().setCape(null);
        Assert.assertNotEquals("Modified profile textures are still equal", profile1.getTextures(), profile2.getTextures());

        profile2.getTextures().setCape(null);
        Assert.assertEquals("Modified profile textures are not equal", profile1.getTextures(), profile2.getTextures());
    }

    @Test
    public void testClone() {
        PlayerProfile profile = buildPlayerProfile();
        PlayerProfile copy = profile.clone();
        Assert.assertEquals("profile and copy are not equal", profile, copy);

        // New copies are independent (don't affect the original profile):
        copy.getTextures().setSkin(null);
        Assert.assertEquals("copy is not independent", SKIN, profile.getTextures().getSkin());
    }

    @Test
    public void testSerializationFullProfile() throws InvalidConfigurationException {
        ConfigurationSerialization.registerClass(CraftPlayerProfile.class);
        PlayerProfile playerProfile = buildPlayerProfile();
        YamlConfiguration configuration = new YamlConfiguration();

        configuration.set("test", playerProfile);

        String saved = configuration.saveToString();

        configuration = new YamlConfiguration();
        configuration.loadFromString(saved);

        Assert.assertTrue(configuration.contains("test"));
        Assert.assertEquals("Profiles are not equal", playerProfile, configuration.get("test"));
    }
}
