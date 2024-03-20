package auctionsniper;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.debugger.ConsoleDebugger;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.impl.JidCreate;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;
    public static final int XMPP_PORT = 5222;
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT =
            ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private static Main main;
    private static MainWindow ui;
    private static Chat notToBeGCd;
    private static AbstractXMPPConnection connection;

    public Main() throws Exception {
        startUserInterface();
    }
    public static void main(String... args) throws Exception {
        main = new Main();

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(JidCreate.domainBareFrom(args[ARG_HOSTNAME]))
                .setDebuggerFactory(ConsoleDebugger.Factory.INSTANCE)
                .setHost(args[ARG_HOSTNAME])
                .setPort(XMPP_PORT)
                .setUsernameAndPassword(args[ARG_USERNAME], args[ARG_PASSWORD])
                //.setResource(AUCTION_RESOURCE)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setHostnameVerifier((hostname, session) -> {
                    return true; // Accept any hostname
                })
                .build();

        connection = new XMPPTCPConnection(config);
        connection.connect();
        connection.login();
        ChatManager chatManager = ChatManager.getInstanceFor(connection);

        chatManager.addIncomingListener((from, message, chat) -> {
            System.out.println("Main - Incoming new message from " + from + ": " + message.getBody());
            SwingUtilities.invokeLater(() -> ui.showStatus(MainWindow.STATUS_LOST));
        });

        EntityFullJid user = connection.getUser(); // Returned sniper@192.168.0.136/12ecc64b-650a-448a-8882-9ad168929b5e
        System.out.println("User EntityFullJid: " + user);
        String jidString = user.asEntityBareJidString(); // Returned sniper@192.168.0.136
        System.out.println("jidString asEntityBareJidString: " + jidString);
        String jid = auctionId(args[ARG_ITEM_ID], "192.168.0.136"); // auction-item-54321@sniper@192.168.0.136/Auction
        System.out.println("jid " + jid);
        EntityBareJid entityBareJid = JidCreate.entityBareFrom(jid);

        Chat chat = chatManager.chatWith(entityBareJid);
        notToBeGCd = chat;

        // Sending a message. Note: You might want to customize this message instead of sending an empty one.
        Message message = new Message(entityBareJid, Message.Type.chat);
        message.setBody("SOLVersion: 1.1; Command: JOIN;");
        chat.send(message);

        main.disconnectWhenUICloses();
    }

    private static String auctionId(String itemId, String connectionUser) {
        return String.format(AUCTION_ID_FORMAT, itemId, connectionUser);
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow());
    }

    public void dispose() {
        ui.dispose();
    }

    public static Main getMain() {
        return main;
    }

    private void disconnectWhenUICloses() {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
            // this now happens since i call ui.dispose
            connection.disconnect();
            }


//            @Override
//            public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
//                xmppAuctionHouse.disconnect();
//            }

        });
    }
}