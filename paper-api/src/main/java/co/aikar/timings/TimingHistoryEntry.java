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

import com.google.common.base.Function;

import java.util.List;
import org.jetbrains.annotations.NotNull;

import static co.aikar.util.JSONUtil.toArrayMapper;

class TimingHistoryEntry {
    final TimingData data;
    private final TimingData[] children;

    TimingHistoryEntry(@NotNull TimingHandler handler) {
        this.data = handler.record.clone();
        children = handler.cloneChildren();
    }

    @NotNull
    List<Object> export() {
        List<Object> result = data.export();
        if (children.length > 0) {
            result.add(
                toArrayMapper(children, new Function<TimingData, Object>() {
                    @NotNull
                    @Override
                    public Object apply(TimingData child) {
                        return child.export();
                    }
                })
            );
        }
        return result;
    }
}
