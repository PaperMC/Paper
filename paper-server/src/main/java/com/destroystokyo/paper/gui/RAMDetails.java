package com.destroystokyo.paper.gui;

import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.TimeUtil;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Vector;

public class RAMDetails extends JList<String> {
    public static final DecimalFormat DECIMAL_FORMAT = Util.make(new DecimalFormat("########0.000"), (format)
        -> format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT)));

    private final MinecraftServer server;

    public RAMDetails(MinecraftServer server) {
        this.server = server;

        setBorder(new EmptyBorder(0, 10, 0, 0));
        setFixedCellHeight(20);
        setOpaque(false);

        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setOpaque(false);
        setCellRenderer(renderer);

        setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setAnchorSelectionIndex(final int anchorIndex) {
            }

            @Override
            public void setLeadAnchorNotificationEnabled(final boolean flag) {
            }

            @Override
            public void setLeadSelectionIndex(final int leadIndex) {
            }

            @Override
            public void setSelectionInterval(final int index0, final int index1) {
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(350, 100);
    }

    public void update() {
        GraphData data = RAMGraph.DATA.peekLast();
        Vector<String> vector = new Vector<>();

        // Follows CraftServer#getTPS
        double[] tps = server.getTPS();
        String[] tpsAvg = new String[tps.length];

        for ( int g = 0; g < tps.length; g++) {
            tpsAvg[g] = format( tps[g] );
        }
        vector.add("Memory use: " + (data.getUsedMem() / 1024L / 1024L) + " mb (" + (data.getFree() * 100L / data.getMax()) + "% free)");
        vector.add("Heap: " + (data.getTotal() / 1024L / 1024L) + " / " + (data.getMax() / 1024L / 1024L) + " mb");
        vector.add("Avg tick: " + DECIMAL_FORMAT.format((double)this.server.getAverageTickTimeNanos() / (double) TimeUtil.NANOSECONDS_PER_MILLISECOND) + " ms");
        vector.add("TPS from last 1m, 5m, 15m: " + String.join(", ", tpsAvg));
        setListData(vector);
    }

    private static String format(double tps) {
        return ( ( tps > 21.0 ) ? "*" : "" ) + Math.min( Math.round( tps * 100.0 ) / 100.0, 20.0 );
    }
}
