package com.destroystokyo.paper.gui;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class RAMGraph extends JComponent {
    public static final LinkedList<GraphData> DATA = new LinkedList<GraphData>() {
        @Override
        public boolean add(GraphData data) {
            if (size() >= 348) {
                remove();
            }
            return super.add(data);
        }
    };

    static {
        GraphData empty = new GraphData(0, 0, 0);
        for (int i = 0; i < 350; i++) {
            DATA.add(empty);
        }
    }

    private final Timer timer;
    private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private int currentTick;

    public RAMGraph() {
        ToolTipManager.sharedInstance().setInitialDelay(0);

        addMouseListener(new MouseAdapter() {
            final int defaultDismissTimeout = ToolTipManager.sharedInstance().getDismissDelay();
            final int dismissDelayMinutes = (int) TimeUnit.MINUTES.toMillis(10);

            @Override
            public void mouseEntered(MouseEvent me) {
                ToolTipManager.sharedInstance().setDismissDelay(dismissDelayMinutes);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                ToolTipManager.sharedInstance().setDismissDelay(defaultDismissTimeout);
            }
        });

        timer = new Timer(50, (event) -> repaint());
        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(350, 110);
    }

    public void update() {
        Runtime jvm = Runtime.getRuntime();
        DATA.add(new GraphData(jvm.totalMemory(), jvm.freeMemory(), jvm.maxMemory()));

        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        if (pointerInfo != null) {
            Point point = pointerInfo.getLocation();
            if (point != null) {
                Point loc = new Point(point);
                SwingUtilities.convertPointFromScreen(loc, this);
                if (this.contains(loc)) {
                    ToolTipManager.sharedInstance().mouseMoved(
                        new MouseEvent(this, -1, System.currentTimeMillis(), 0, loc.x, loc.y,
                            point.x, point.y, 0, false, 0));
                }
            }
        }

        currentTick++;
    }

    @Override
    public void paint(Graphics graphics) {
        graphics.setColor(new Color(0xFFFFFFFF));
        graphics.fillRect(0, 0, 350, 100);

        graphics.setColor(new Color(0x888888));
        graphics.drawLine(1, 25, 348, 25);
        graphics.drawLine(1, 50, 348, 50);
        graphics.drawLine(1, 75, 348, 75);

        int i = 0;
        for (GraphData data : DATA) {
            i++;
            if ((i + currentTick) % 120 == 0) {
                graphics.setColor(new Color(0x888888));
                graphics.drawLine(i, 1, i, 99);
            }
            int used = data.getUsedPercent();
            if (used > 0) {
                Color color = data.getLineColor();
                graphics.setColor(data.getFillColor());
                graphics.fillRect(i, 100 - used, 1, used);
                graphics.setColor(color);
                graphics.fillRect(i, 100 - used, 1, 1);
            }
        }

        graphics.setColor(new Color(0xFF000000));
        graphics.drawRect(0, 0, 348, 100);

        Point m = getMousePosition();
        if (m != null && m.x > 0 && m.x < 348 && m.y > 0 && m.y < 100) {
            GraphData data = DATA.get(m.x);
            int used = data.getUsedPercent();
            graphics.setColor(new Color(0x000000));
            graphics.drawLine(m.x, 1, m.x, 99);
            graphics.drawOval(m.x - 2, 100 - used - 2, 5, 5);
            graphics.setColor(data.getLineColor());
            graphics.fillOval(m.x - 2, 100 - used - 2, 5, 5);
            setToolTipText(String.format("<html><body>Used: %s mb (%s%%)<br/>%s</body></html>",
                Math.round(data.getUsedMem() / 1024F / 1024F),
                used, getTime(m.x)));
        }
    }

    public String getTime(int halfSeconds) {
        int millis = (348 - halfSeconds) / 2 * 1000;
        return TIME_FORMAT.format(new Date((System.currentTimeMillis() - millis)));
    }

    public void stop() {
        timer.stop();
    }
}
