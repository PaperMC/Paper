/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spigotmc;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.bukkit.plugin.AuthorNagException;
import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import co.aikar.timings.TimingsManager;

import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * This is here for legacy purposes incase any plugin used it.
 *
 * If you use this, migrate ASAP as this will be removed in the future!
 *
 * @deprecated
 * @see co.aikar.timings.Timings#of
 */
@Deprecated(forRemoval = true)
public final class CustomTimingsHandler {
    private final Timing handler;

    public CustomTimingsHandler(@NotNull String name) {
        Timing timing;

        new AuthorNagException("Deprecated use of CustomTimingsHandler. Timings has been removed.").printStackTrace();
        try {
            final Method ofSafe = TimingsManager.class.getDeclaredMethod("getHandler", String.class, String.class, Timing.class);
            ofSafe.setAccessible(true);
            timing = (Timing) ofSafe.invoke(null,"Minecraft", "(Deprecated API) " + name, null);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "This handler could not be registered");
            timing = Timings.NULL_HANDLER;
        }
        handler = timing;
    }

    public void startTiming() { handler.startTiming(); }
    public void stopTiming() { handler.stopTiming(); }

}
