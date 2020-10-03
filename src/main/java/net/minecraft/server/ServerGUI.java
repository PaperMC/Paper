package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.util.QueueLogAppender;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerGUI extends JComponent {

    private static final Font a = new Font("Monospaced", 0, 12);
    private static final Logger LOGGER = LogManager.getLogger();
    private final DedicatedServer c;
    private Thread d;
    private final Collection<Runnable> e = Lists.newArrayList();
    private final AtomicBoolean f = new AtomicBoolean();

    public static ServerGUI a(final DedicatedServer dedicatedserver) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            ;
        }

        final JFrame jframe = new JFrame("Minecraft server");
        final ServerGUI servergui = new ServerGUI(dedicatedserver);

        jframe.setDefaultCloseOperation(2);
        jframe.add(servergui);
        jframe.pack();
        jframe.setLocationRelativeTo((Component) null);
        jframe.setVisible(true);
        jframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowevent) {
                if (!servergui.f.getAndSet(true)) {
                    jframe.setTitle("Minecraft server - shutting down!");
                    dedicatedserver.safeShutdown(true);
                    servergui.f();
                }

            }
        });
        servergui.a(jframe::dispose);
        servergui.a();
        return servergui;
    }

    private ServerGUI(DedicatedServer dedicatedserver) {
        this.c = dedicatedserver;
        this.setPreferredSize(new Dimension(854, 480));
        this.setLayout(new BorderLayout());

        try {
            this.add(this.e(), "Center");
            this.add(this.c(), "West");
        } catch (Exception exception) {
            ServerGUI.LOGGER.error("Couldn't build server GUI", exception);
        }

    }

    public void a(Runnable runnable) {
        this.e.add(runnable);
    }

    private JComponent c() {
        JPanel jpanel = new JPanel(new BorderLayout());
        GuiStatsComponent guistatscomponent = new GuiStatsComponent(this.c);

        this.e.add(guistatscomponent::a);
        jpanel.add(guistatscomponent, "North");
        jpanel.add(this.d(), "Center");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
        return jpanel;
    }

    private JComponent d() {
        JList<?> jlist = new PlayerListBox(this.c);
        JScrollPane jscrollpane = new JScrollPane(jlist, 22, 30);

        jscrollpane.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
        return jscrollpane;
    }

    private JComponent e() {
        JPanel jpanel = new JPanel(new BorderLayout());
        JTextArea jtextarea = new JTextArea();
        JScrollPane jscrollpane = new JScrollPane(jtextarea, 22, 30);

        jtextarea.setEditable(false);
        jtextarea.setFont(ServerGUI.a);
        JTextField jtextfield = new JTextField();

        jtextfield.addActionListener((actionevent) -> {
            String s = jtextfield.getText().trim();

            if (!s.isEmpty()) {
                this.c.issueCommand(s, this.c.getServerCommandListener());
            }

            jtextfield.setText("");
        });
        jtextarea.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent focusevent) {}
        });
        jpanel.add(jscrollpane, "Center");
        jpanel.add(jtextfield, "South");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
        this.d = new Thread(() -> {
            String s;

            while ((s = QueueLogAppender.getNextLogEvent("ServerGuiConsole")) != null) {
                this.a(jtextarea, jscrollpane, s);
            }

        });
        this.d.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(ServerGUI.LOGGER));
        this.d.setDaemon(true);
        return jpanel;
    }

    public void a() {
        this.d.start();
    }

    public void b() {
        if (!this.f.getAndSet(true)) {
            this.f();
        }

    }

    private void f() {
        this.e.forEach(Runnable::run);
    }

    public void a(JTextArea jtextarea, JScrollPane jscrollpane, String s) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> {
                this.a(jtextarea, jscrollpane, s);
            });
        } else {
            Document document = jtextarea.getDocument();
            JScrollBar jscrollbar = jscrollpane.getVerticalScrollBar();
            boolean flag = false;

            if (jscrollpane.getViewport().getView() == jtextarea) {
                flag = (double) jscrollbar.getValue() + jscrollbar.getSize().getHeight() + (double) (ServerGUI.a.getSize() * 4) > (double) jscrollbar.getMaximum();
            }

            try {
                document.insertString(document.getLength(), s, (AttributeSet) null);
            } catch (BadLocationException badlocationexception) {
                ;
            }

            if (flag) {
                jscrollbar.setValue(Integer.MAX_VALUE);
            }

        }
    }
}
