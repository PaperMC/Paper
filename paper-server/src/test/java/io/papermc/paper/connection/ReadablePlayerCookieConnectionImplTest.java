package io.papermc.paper.connection;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
import net.minecraft.resources.Identifier;
import org.bukkit.NamespacedKey;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@Normal
public class ReadablePlayerCookieConnectionImplTest {

    private static final NamespacedKey KEY = new NamespacedKey("test", "cookie");
    private static final Identifier ID = Identifier.fromNamespaceAndPath("test", "cookie");

    private ReadablePlayerCookieConnectionImpl cookieConnection;

    @BeforeEach
    public void setup() {
        this.cookieConnection = Mockito.mock(
            ReadablePlayerCookieConnectionImpl.class,
            Mockito.withSettings()
                .useConstructor(Mockito.mock(Connection.class))
                .defaultAnswer(Mockito.CALLS_REAL_METHODS)
        );
    }

    private static ServerboundCookieResponsePacket response(final String payload) {
        return new ServerboundCookieResponsePacket(ID, payload.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testSingleRequest() {
        final CompletableFuture<byte[]> future = this.cookieConnection.retrieveCookie(KEY);
        Assertions.assertTrue(this.cookieConnection.isAwaitingCookies());

        Assertions.assertTrue(this.cookieConnection.handleCookieResponse(response("a")));
        Assertions.assertArrayEquals("a".getBytes(StandardCharsets.UTF_8), future.getNow(null));
        Assertions.assertFalse(this.cookieConnection.isAwaitingCookies());
    }

    @Test
    public void testSameKeyRequestedTwice() {
        final CompletableFuture<byte[]> first = this.cookieConnection.retrieveCookie(KEY);
        final CompletableFuture<byte[]> second = this.cookieConnection.retrieveCookie(KEY);

        Assertions.assertTrue(this.cookieConnection.handleCookieResponse(response("a")));
        Assertions.assertArrayEquals("a".getBytes(StandardCharsets.UTF_8), first.getNow(null));
        Assertions.assertFalse(second.isDone());
        Assertions.assertTrue(this.cookieConnection.isAwaitingCookies());

        Assertions.assertTrue(this.cookieConnection.handleCookieResponse(response("b")));
        Assertions.assertArrayEquals("b".getBytes(StandardCharsets.UTF_8), second.getNow(null));
        Assertions.assertFalse(this.cookieConnection.isAwaitingCookies());
    }

    @Test
    public void testUnexpectedResponse() {
        Assertions.assertFalse(this.cookieConnection.handleCookieResponse(response("a")));

        this.cookieConnection.retrieveCookie(KEY);
        Assertions.assertTrue(this.cookieConnection.handleCookieResponse(response("a")));
        Assertions.assertFalse(this.cookieConnection.handleCookieResponse(response("a")));
    }

    @Test
    public void testRequestFromCallback() {
        final CompletableFuture<byte[]>[] fromCallback = new CompletableFuture[1];
        this.cookieConnection.retrieveCookie(KEY).thenRun(() -> fromCallback[0] = this.cookieConnection.retrieveCookie(KEY));

        Assertions.assertTrue(this.cookieConnection.handleCookieResponse(response("a")));
        Assertions.assertNotNull(fromCallback[0]);
        Assertions.assertTrue(this.cookieConnection.isAwaitingCookies());

        Assertions.assertTrue(this.cookieConnection.handleCookieResponse(response("b")));
        Assertions.assertArrayEquals("b".getBytes(StandardCharsets.UTF_8), fromCallback[0].getNow(null));
    }
}
