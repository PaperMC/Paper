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
package co.aikar.timings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides an ability to time sections of code within the Minecraft Server
 *
 * @deprecated Timings will be removed in the future
 */
@Deprecated(forRemoval = true)
public interface Timing extends AutoCloseable {
    /**
     * Starts timing the execution until {@link #stopTiming()} is called.
     *
     * @return Timing
     */
    @NotNull
    Timing startTiming();

    /**
     * <p>Stops timing and records the data. Propagates the data up to group handlers.</p>
     *
     * Will automatically be called when this Timing is used with try-with-resources
     */
    void stopTiming();

    /**
     * Starts timing the execution until {@link #stopTiming()} is called.
     *
     * But only if we are on the primary thread.
     *
     * @return Timing
     */
    @NotNull
    Timing startTimingIfSync();

    /**
     * <p>Stops timing and records the data. Propagates the data up to group handlers.</p>
     *
     * <p>Will automatically be called when this Timing is used with try-with-resources</p>
     *
     * But only if we are on the primary thread.
     */
    void stopTimingIfSync();

    /**
     * @deprecated Doesn't do anything - Removed
     */
    @Deprecated
    void abort();

    /**
     * Used internally to get the actual backing Handler in the case of delegated Handlers
     *
     * @return TimingHandler
     */
    @Nullable
    TimingHandler getTimingHandler();

    @Override
    void close();
}
