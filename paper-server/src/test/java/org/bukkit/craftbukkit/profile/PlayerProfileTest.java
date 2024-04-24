package org.bukkit.craftbukkit.profile;

import static org.junit.jupiter.api.Assertions.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import net.minecraft.SystemUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.support.condition.EnableIfMojangServerAvailable;
import org.junit.jupiter.api.Test;

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
    @EnableIfMojangServerAvailable
    public void testProvidedValues() {
        Property property = new Property(CraftPlayerTextures.PROPERTY_NAME, VALUE, SIGNATURE);
        assertTrue(CraftProfileProperty.hasValidSignature(property), "Invalid test property signature, has the public key changed?");
    }

    @Test
    public void testProfileCreation() {
        // Invalid profiles:
        assertThrows(IllegalArgumentException.class, () -> {
            new CraftPlayerProfile(null, null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new CraftPlayerProfile(null, "");
        });
        assertThrows(IllegalArgumentException.class, () -> {
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
        assertThrows(NullPointerException.class, () -> {
            new CraftPlayerProfile(null);
        });

        // Valid profiles:
        CraftPlayerProfile profile1 = new CraftPlayerProfile(new GameProfile(UNIQUE_ID, NAME));
        assertEquals(UNIQUE_ID, profile1.getUniqueId(), "Unique id is not the same");
        assertEquals(NAME, profile1.getName(), "Name is not the same");

        CraftPlayerProfile profile2 = new CraftPlayerProfile(new GameProfile(UNIQUE_ID, ""));
        assertEquals(UNIQUE_ID, profile2.getUniqueId(), "Unique id is not the same");
        assertEquals(null, profile2.getName(), "Name is not null");

        CraftPlayerProfile profile3 = new CraftPlayerProfile(new GameProfile(SystemUtils.NIL_UUID, NAME));
        assertEquals(null, profile3.getUniqueId(), "Unique id is not null");
        assertEquals(NAME, profile3.getName(), "Name is not the same");
    }

    @Test
    public void testTexturesLoading() {
        CraftPlayerProfile profile = buildPlayerProfile();
        assertEquals(UNIQUE_ID, profile.getUniqueId(), "Unique id is not the same");
        assertEquals(NAME, profile.getName(), "Name is not the same");
        assertEquals(SKIN, profile.getTextures().getSkin(), "Skin url is not the same");
        assertEquals(PlayerTextures.SkinModel.SLIM, profile.getTextures().getSkinModel(), "Skin model is not the same");
        assertEquals(CAPE, profile.getTextures().getCape(), "Cape url is not the same");
        assertEquals(TIMESTAMP, profile.getTextures().getTimestamp(), "Timestamp is not the same");
    }

    @Test
    @EnableIfMojangServerAvailable
    public void testBuildGameProfile() {
        CraftPlayerProfile profile = buildPlayerProfile();
        GameProfile gameProfile = profile.buildGameProfile();
        assertNotNull(gameProfile, "GameProfile is null");

        Property property = CraftPlayerProfile.getProperty(gameProfile, CraftPlayerTextures.PROPERTY_NAME);
        assertNotNull(property, "Textures property is null");
        assertEquals(VALUE, property.value(), "Property values are not the same");
        assertEquals(NAME, gameProfile.getName(), "Names are not the same");
        assertEquals(UNIQUE_ID, gameProfile.getId(), "Unique ids are not the same");
        assertTrue(property.hasSignature(), "Signature is missing");
        assertTrue(CraftProfileProperty.hasValidSignature(property), "Signature is not valid");
    }

    @Test
    public void testBuildGameProfileReturnsNewInstance() {
        CraftPlayerProfile profile = buildPlayerProfile();
        GameProfile gameProfile1 = profile.buildGameProfile();
        GameProfile gameProfile2 = profile.buildGameProfile();
        assertNotSame(gameProfile1, gameProfile2, "CraftPlayerProfile#buildGameProfile() does not produce a new instance");
    }

    @Test
    @EnableIfMojangServerAvailable
    public void testSignatureValidation() {
        CraftPlayerProfile profile = buildPlayerProfile();
        assertTrue(profile.getTextures().isSigned(), "Signature is not valid");
    }

    @Test
    public void testSignatureInvalidation() {
        CraftPlayerProfile profile = buildPlayerProfile();
        profile.getTextures().setSkin(null);
        assertEquals(0L, profile.getTextures().getTimestamp(), "Textures has a timestamp");
        assertFalse(profile.getTextures().isSigned(), "Textures signature is valid");

        // Ensure that the invalidation is preserved when the property is rebuilt:
        profile.rebuildDirtyProperties();
        assertEquals(0L, profile.getTextures().getTimestamp(), "Rebuilt textures has a timestamp");
        assertFalse(profile.getTextures().isSigned(), "Rebuilt textures signature is valid");
    }

    @Test
    public void testSetSkinResetsSkinModel() {
        CraftPlayerProfile profile = buildPlayerProfile();
        assertEquals(PlayerTextures.SkinModel.SLIM, profile.getTextures().getSkinModel(), "Skin model is not the same");
        profile.getTextures().setSkin(SKIN);
        assertEquals(PlayerTextures.SkinModel.CLASSIC, profile.getTextures().getSkinModel(), "Skin model was not reset by skin change");
    }

    @Test
    public void testSetTextures() {
        CraftPlayerProfile profile = buildPlayerProfile();
        CraftPlayerProfile profile2 = new CraftPlayerProfile(new GameProfile(UNIQUE_ID, NAME));

        assertFalse(profile.getTextures().isEmpty(), "profile has no textures");
        assertTrue(profile2.getTextures().isEmpty(), "profile2 has textures");

        profile2.setTextures(profile.getTextures());
        assertFalse(profile2.getTextures().isEmpty(), "profile2 has no textures");
        assertEquals(profile.getTextures(), profile2.getTextures(), "copied profile textures are not the same");

        profile2.setTextures(null);
        assertTrue(profile2.getTextures().isEmpty(), "cleared profile2 has textures");
        assertEquals(0L, profile2.getTextures().getTimestamp(), "cleared profile2 has textures timestamp");
        assertFalse(profile2.getTextures().isSigned(), "cleared profile2 has signed textures");
    }

    @Test
    public void testClearTextures() {
        CraftPlayerProfile profile = buildPlayerProfile();
        assertFalse(profile.getTextures().isEmpty(), "profile has no textures");

        profile.getTextures().clear();
        assertTrue(profile.getTextures().isEmpty(), "cleared profile has textures");
        assertEquals(0L, profile.getTextures().getTimestamp(), "cleared profile has textures timestamp");
        assertFalse(profile.getTextures().isSigned(), "cleared profile has signed textures");
    }

    @Test
    public void testCustomSkin() {
        CraftPlayerProfile profile = new CraftPlayerProfile(UNIQUE_ID, NAME);
        profile.getTextures().setSkin(SKIN);
        assertEquals(COMPACT_VALUE, profile.getTextures().getProperty().value(), "profile with custom skin does not match expected value");
    }

    @Test
    public void testEquals() {
        CraftPlayerProfile profile1 = buildPlayerProfile();
        CraftPlayerProfile profile2 = buildPlayerProfile();
        CraftPlayerProfile profile3 = new CraftPlayerProfile(UNIQUE_ID, NAME);
        CraftPlayerProfile profile4 = new CraftPlayerProfile(UNIQUE_ID, NAME);
        CraftPlayerProfile profile5 = new CraftPlayerProfile(UNIQUE_ID, null);
        CraftPlayerProfile profile6 = new CraftPlayerProfile(null, NAME);

        assertEquals(profile1, profile2, "profile1 and profile2 are not equal");
        assertEquals(profile3, profile4, "profile3 and profile4 are not equal");
        assertNotEquals(profile1, profile3, "profile1 and profile3 are equal");
        assertNotEquals(profile4, profile5, "profile4 and profile5 are equal");
        assertNotEquals(profile4, profile6, "profile4 and profile6 are equal");
    }

    @Test
    public void testTexturesEquals() {
        CraftPlayerProfile profile1 = buildPlayerProfile();
        CraftPlayerProfile profile2 = buildPlayerProfile();
        assertEquals(profile1.getTextures(), profile2.getTextures(), "Profile textures are not equal");

        profile1.getTextures().setCape(null);
        assertNotEquals(profile1.getTextures(), profile2.getTextures(), "Modified profile textures are still equal");

        profile2.getTextures().setCape(null);
        assertEquals(profile1.getTextures(), profile2.getTextures(), "Modified profile textures are not equal");
    }

    @Test
    public void testClone() {
        PlayerProfile profile = buildPlayerProfile();
        PlayerProfile copy = profile.clone();
        assertEquals(profile, copy, "profile and copy are not equal");

        // New copies are independent (don't affect the original profile):
        copy.getTextures().setSkin(null);
        assertEquals(SKIN, profile.getTextures().getSkin(), "copy is not independent");
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

        assertTrue(configuration.contains("test"));
        assertEquals(playerProfile, configuration.get("test"), "Profiles are not equal");
    }
}
