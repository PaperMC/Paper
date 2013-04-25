package org.bukkit.util.io;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.google.common.collect.ImmutableList;

@RunWith(Parameterized.class)
public class BukkitObjectStreamTest {

    @Parameters(name= "{index}: {0}")
    public static List<Object[]> data() {
        return ImmutableList.<Object[]>of(
            new Object[] {
                Color.class.getName(),
                "rO0ABXNyADZjb20uZ29vZ2xlLmNvbW1vbi5jb2xsZWN0LkltbXV0YWJsZUxpc3QkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAVsACGVsZW1lbnRzdAATW0xqYXZhL2xhbmcvT2JqZWN0O3hwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAABXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXNxAH4AAVsABnZhbHVlc3EAfgABeHB1cQB+AAMAAAAEdAACPT10AANSRUR0AARCTFVFdAAFR1JFRU51cQB+AAMAAAAEdAAFQ29sb3JzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAA/3NxAH4AEQAAAP9zcQB+ABEAAAD/c3EAfgAFc3EAfgAIdXEAfgADAAAABHEAfgALcQB+AAxxAH4ADXEAfgAOdXEAfgADAAAABHEAfgAQc3EAfgARAAAAAHNxAH4AEQAAAIBzcQB+ABEAAACAc3EAfgAFc3EAfgAIdXEAfgADAAAABHEAfgALcQB+AAxxAH4ADXEAfgAOdXEAfgADAAAABHEAfgAQc3EAfgARAAAAgHNxAH4AEQAAAIBxAH4AGnNxAH4ABXNxAH4ACHVxAH4AAwAAAARxAH4AC3EAfgAMcQB+AA1xAH4ADnVxAH4AAwAAAARxAH4AEHNxAH4AEQAAAP9xAH4AGnEAfgAac3EAfgAFc3EAfgAIdXEAfgADAAAABHEAfgALcQB+AAxxAH4ADXEAfgAOdXEAfgADAAAABHEAfgAQc3EAfgARAAAA/3EAfgAac3EAfgARAAAApQ==",
                ImmutableList.of(
                    Color.WHITE,
                    Color.TEAL,
                    Color.PURPLE,
                    Color.RED,
                    Color.ORANGE
                    )
            },
            new Object[] {
                FireworkEffect.class.getName(),
                "rO0ABXNyADZjb20uZ29vZ2xlLmNvbW1vbi5jb2xsZWN0LkltbXV0YWJsZUxpc3QkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAVsACGVsZW1lbnRzdAATW0xqYXZhL2xhbmcvT2JqZWN0O3hwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAAA3NyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXNxAH4AAVsABnZhbHVlc3EAfgABeHB1cQB+AAMAAAAGdAACPT10AAdmbGlja2VydAAFdHJhaWx0AAZjb2xvcnN0AAtmYWRlLWNvbG9yc3QABHR5cGV1cQB+AAMAAAAGdAAIRmlyZXdvcmtzcgARamF2YS5sYW5nLkJvb2xlYW7NIHKA1Zz67gIAAVoABXZhbHVleHABc3EAfgATAHNxAH4AAHVxAH4AAwAAAAJzcQB+AAVzcQB+AAh1cQB+AAMAAAAEcQB+AAt0AANSRUR0AARCTFVFdAAFR1JFRU51cQB+AAMAAAAEdAAFQ29sb3JzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAAHEAfgAicQB+ACJzcQB+AAVzcQB+AAh1cQB+AAMAAAAEcQB+AAtxAH4AG3EAfgAccQB+AB11cQB+AAMAAAAEcQB+AB9zcQB+ACAAAADAc3EAfgAgAAAAwHNxAH4AIAAAAMBzcQB+AAB1cQB+AAMAAAABc3EAfgAFc3EAfgAIdXEAfgADAAAABHEAfgALcQB+ABtxAH4AHHEAfgAddXEAfgADAAAABHEAfgAfc3EAfgAgAAAA/3NxAH4AIAAAAP9zcQB+ACAAAAD/dAAKQkFMTF9MQVJHRXNxAH4ABXNxAH4ACHVxAH4AAwAAAAZxAH4AC3EAfgAMcQB+AA1xAH4ADnEAfgAPcQB+ABB1cQB+AAMAAAAGcQB+ABJxAH4AFXEAfgAVc3EAfgAAdXEAfgADAAAAAXNxAH4ABXNxAH4ACHVxAH4AAwAAAARxAH4AC3EAfgAbcQB+ABxxAH4AHXVxAH4AAwAAAARxAH4AH3EAfgAic3EAfgAgAAAAgHEAfgAic3EAfgAAdXEAfgADAAAAAHQABEJBTExzcQB+AAVzcQB+AAh1cQB+AAMAAAAGcQB+AAtxAH4ADHEAfgANcQB+AA5xAH4AD3EAfgAQdXEAfgADAAAABnEAfgAScQB+ABRxAH4AFHNxAH4AAHVxAH4AAwAAAAFzcQB+AAVzcQB+AAh1cQB+AAMAAAAEcQB+AAtxAH4AG3EAfgAccQB+AB11cQB+AAMAAAAEcQB+AB9zcQB+ACAAAACAcQB+ACJxAH4AInEAfgA/dAAHQ1JFRVBFUg==",
                ImmutableList.of(
                    FireworkEffect.builder()
                        .withColor(Color.BLACK, Color.SILVER)
                        .with(Type.BALL_LARGE)
                        .withFade(Color.WHITE)
                        .withFlicker()
                        .build(),
                    FireworkEffect.builder()
                        .withColor(Color.NAVY)
                        .build(),
                    FireworkEffect.builder()
                        .withColor(Color.MAROON)
                        .withTrail()
                        .withFlicker()
                        .with(Type.CREEPER)
                        .build()
                    ),
            },
            new Object[] {
                Vector.class.getName(),
                "rO0ABXNyADZjb20uZ29vZ2xlLmNvbW1vbi5jb2xsZWN0LkltbXV0YWJsZUxpc3QkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAVsACGVsZW1lbnRzdAATW0xqYXZhL2xhbmcvT2JqZWN0O3hwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAABHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXNxAH4AAVsABnZhbHVlc3EAfgABeHB1cQB+AAMAAAAEdAACPT10AAF4dAABeXQAAXp1cQB+AAMAAAAEdAAGVmVjdG9yc3IAEGphdmEubGFuZy5Eb3VibGWAs8JKKWv7BAIAAUQABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAAAAAAABzcQB+ABEAAAAAAAAAAHNxAH4AEQAAAAAAAAAAc3EAfgAFc3EAfgAIdXEAfgADAAAABHEAfgALcQB+AAxxAH4ADXEAfgAOdXEAfgADAAAABHEAfgAQc3EAfgARQIOFwo9cKPZzcQB+ABFAtCKcKPXCj3NxAH4AEUBzrpeNT987c3EAfgAFc3EAfgAIdXEAfgADAAAABHEAfgALcQB+AAxxAH4ADXEAfgAOdXEAfgADAAAABHEAfgAQc3EAfgARwEQTMzMzMzNzcQB+ABFASYAAAAAAAHNxAH4AEcCjqG3UQTVUc3EAfgAFc3EAfgAIdXEAfgADAAAABHEAfgALcQB+AAxxAH4ADXEAfgAOdXEAfgADAAAABHEAfgAQc3EAfgARQd/////AAABzcQB+ABHB4AAAAAAAAHNxAH4AEQAAAAAAAAAA",
                ImmutableList.of(
                    new Vector(0, 0, 0),
                    new Vector(624.72, 5154.61, 314.912),
                    new Vector(-40.15, 51, -2516.21451),
                    new Vector(Integer.MAX_VALUE, Integer.MIN_VALUE, 0)
                    )
            });
    }

    @Parameter(0)
    public String className;

    @Parameter(1)
    public String preEncoded;

    @Parameter(2)
    public List<ConfigurationSerializable> object;

    @Test
    public void checkSerlialization() throws Throwable {
        // If this test fails, you may start your trek to debug by commenting the '@Ignore' on the next method
        // (and of course, you would read those comments too)
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new BukkitObjectOutputStream(out);
            oos.writeObject(object);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                }
            }
        }

        final byte[] preEncodedArray = Base64Coder.decode(preEncoded);

        final Object readBack;
        final Object preEncoded;

        ObjectInputStream ois = null;
        ObjectInputStream preois = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            ByteArrayInputStream preIn = new ByteArrayInputStream(preEncodedArray);
            ois = new BukkitObjectInputStream(in);
            preois = new BukkitObjectInputStream(preIn);

            readBack = ois.readObject();
            preEncoded = preois.readObject();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ex) {
                }
            }
            if (preois != null) {
                try {
                    preois.close();
                } catch (IOException ex) {
                }
            }
        }

        assertThat(object, is(readBack));
        assertThat(object, is(preEncoded));
    }

    @Ignore
    @Test
    public void preEncoded() throws Throwable {
        // This test is placed in the case that a necessary change is made to change the encoding format
        // Just remove the ignore (or run manually) and it'll give you the new pre-encoded values

        // It really does not matter if the encoded array is different per system (hence why this test is set to not run),
        // as long as all systems can deserialize it.

        // The entire reason the pre-encoded string was added is to make a build (test) fail if someone accidentally makes it not backward-compatible

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new BukkitObjectOutputStream(out);
            oos.writeObject(object);
            oos.flush();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                }
            }
        }

        final String string = new String(Base64Coder.encode(out.toByteArray()));
        try {
            assertThat(preEncoded, is(string));
        } catch (Throwable t) {
            System.out.println(className + ": \"" + string + "\"");
            throw t;
        }
    }
}
