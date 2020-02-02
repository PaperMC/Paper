package com.destroystokyo.paper.gui;

import net.minecraft.server.MinecraftServer;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class GuiStatsComponent extends JPanel {
    private final Timer timer;
    private final RAMGraph ramGraph;

    public GuiStatsComponent(MinecraftServer server) {
        super(new BorderLayout());

        setOpaque(false);

        ramGraph = new RAMGraph();
        RAMDetails ramDetails = new RAMDetails(server);

        add(ramGraph, "North");
        add(ramDetails, "Center");

        timer = new Timer(500, (event) -> {
            ramGraph.update();
            ramDetails.update();
        });
        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(350, 200);
    }

    public void close() {
        timer.stop();
        ramGraph.stop();
    }
}
