package auctionsniper.endtoend;

import auctionsniper.MainWindow;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.timing.Pause;

public class AuctionSniperDriver {
    private FrameFixture window;

    public AuctionSniperDriver(int timeoutMillis) {
        Robot robot = BasicRobot.robotWithCurrentAwtHierarchy();
        robot.settings().delayBetweenEvents(timeoutMillis);
        window = WindowFinder.findFrame(MainWindow.MAIN_WINDOW_NAME).using(robot);
        window.show();
    }

    public void showsSniperStatus(String statusText) {
        window.label(MainWindow.SNIPER_STATUS_NAME).requireText(statusText);
    }

    public void showsSniperStatus(String itemId, String statusText) {
        Pause.pause(2000);
        JLabelFixture label = window.label(MainWindow.SNIPER_STATUS_NAME);
        System.out.println("shows sniper status label fixture text " + label.text());
        label.requireText(statusText);
    }

}
