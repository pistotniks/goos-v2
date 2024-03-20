package auctionsniper.endtoend;

import org.jivesoftware.smack.XMPPException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.jxmpp.stringprep.XmppStringprepException;

class AuctionSniperEndToEndTest {

    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");

    private final ApplicationRunner application = new ApplicationRunner();

    public AuctionSniperEndToEndTest() throws XmppStringprepException, XMPPException {
    }

    @Test
    void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem();

        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

        auction.announceClosed();
        application.showsSniperHasLostAuction(auction);
    }

    @AfterEach
    public void stopAuction() {
        auction.stop();
        application.stop();
    }
}
