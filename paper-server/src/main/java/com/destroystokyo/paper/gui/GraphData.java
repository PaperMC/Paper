package com.destroystokyo.paper.gui;

import java.awt.Color;

public class GraphData {
    private long total;
    private long free;
    private long max;
    private long usedMem;
    private int usedPercent;

    public GraphData(long total, long free, long max) {
        this.total = total;
        this.free = free;
        this.max = max;
        this.usedMem = total - free;
        this.usedPercent = usedMem == 0 ? 0 : (int) (usedMem * 100L / max);
    }

    public long getTotal() {
        return total;
    }

    public long getFree() {
        return free;
    }

    public long getMax() {
        return max;
    }

    public long getUsedMem() {
        return usedMem;
    }

    public int getUsedPercent() {
        return usedPercent;
    }

    public Color getFillColor() {
        return GraphColor.getFillColor(usedPercent);
    }

    public Color getLineColor() {
        return GraphColor.getLineColor(usedPercent);
    }
}
