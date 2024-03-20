package auctionsniper;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainWindow extends JFrame {
    private static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";
    public static final String MAIN_WINDOW_NAME = "Auction Sniper";

    public static final String SNIPER_STATUS_NAME = "sniper status";
    private final JLabel sniperStatus = createLabel(STATUS_JOINING);
    public MainWindow() {
        super(MAIN_WINDOW_NAME);
        setName(MAIN_WINDOW_NAME);
        add(sniperStatus);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }

    public void showStatus(String status) {
        System.out.println("Show status called and set to: " + status);
        sniperStatus.setText(status);
    }
}
