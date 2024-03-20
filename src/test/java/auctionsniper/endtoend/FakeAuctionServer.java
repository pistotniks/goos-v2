package auctionsniper.endtoend;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.debugger.ConsoleDebugger;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FakeAuctionServer {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "192.168.0.136";
    public static final int XMPP_PORT = 5222;
    private static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private final AbstractXMPPConnection connection;
    private Chat currentChat;
    private final IncomingChatMessageListener messageListener = new SingleMessageListener();

    public FakeAuctionServer(String itemId) throws XMPPException, XmppStringprepException {
        this.itemId = itemId;
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(JidCreate.domainBareFrom(XMPP_HOSTNAME))
                .setDebuggerFactory(ConsoleDebugger.Factory.INSTANCE)
                .setHost(XMPP_HOSTNAME)
                .setPort(XMPP_PORT)
                .setUsernameAndPassword(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD)
                .setResource(AUCTION_RESOURCE)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setHostnameVerifier((hostname, session) -> {
                    return true; // Accept any hostname
                })
                .build();
        this.connection = new XMPPTCPConnection(config);
    }

    public String getItemId() {
        return itemId;
    }

    public void startSellingItem() throws Exception {
        connection.connect().login();

        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addIncomingListener(messageListener);
    }

    public void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
        receivesAMessageMatching(sniperId, equalTo(XMPPAuction.JOIN_COMMAND_FORMAT));
    }

    private void receivesAMessageMatching(String sniperId, Matcher<? super String> messageMatcher) throws InterruptedException {
        ((SingleMessageListener)messageListener).receivesAMessage(messageMatcher);
        //assertThat(currentChat.getXmppAddressOfChatPartner().toString(), equalTo(sniperId));
    }

    public void announceClosed() throws Exception {
        currentChat.send("SOLVersion: 1.1; Event: CLOSE;");
    }

    public void stop() {
        connection.disconnect();
    }

    public class SingleMessageListener implements IncomingChatMessageListener {
        private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1);

        public void processMessage(Message message) {
            messages.add(message);
        }

        public void receivesAMessage(Matcher<? super String> messageMatcher)
                throws InterruptedException {
            final Message message = messages.poll(5, TimeUnit.SECONDS);
            assertThat(message.getBody(), messageMatcher);
        }

        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            currentChat = chat;
            System.out.println("SingleMessageListener in fake auction server - New message from " + from + ": " + message.getBody());
            processMessage(message);
        }
    }
}
