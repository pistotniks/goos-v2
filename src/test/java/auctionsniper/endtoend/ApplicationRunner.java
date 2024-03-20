package auctionsniper.endtoend;

import auctionsniper.Main;

public class ApplicationRunner {
    public static final String XMPP_HOSTNAME = "192.168.0.136";
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@192.168.0.136/Auction";
    private static final String STATUS_JOINING = "Joining";
    private static final String STATUS_LOST = "Lost";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction) {
        Thread thread = new Thread("Test Application") {
            @Override public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.showsSniperStatus(STATUS_JOINING);
    }

    public void showsSniperHasLostAuction(FakeAuctionServer auction) {
        driver.showsSniperStatus(auction.getItemId(), STATUS_LOST);
    }

    public void stop() {
        if (driver != null) {
            Main main = Main.getMain();
            main.dispose(); // This triggers closing and event of cleaning on closing
            // todo driver.dispose();
        }
    }
}
