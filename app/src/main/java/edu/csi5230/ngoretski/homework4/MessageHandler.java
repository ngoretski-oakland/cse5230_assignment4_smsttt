package edu.csi5230.ngoretski.homework4;

/**
 * Created by nathan on 11/11/17.
 */

public final class MessageHandler {

    private static final String UNIQUE_KEY = "$$ASSIGNMENT4TTT$$";
    private static final String DELIMITER = "JJJ";

    private static final String INVITE = "INVITE";
    private static final String ACCEPT = "ACCEPT";
    private static final String DENY = "DENY";

    private static final String MOVE = "MOVE";
    private static final String GAMEOVER = "GAMEOVER";

    public static String getInviteMessage() {
        return UNIQUE_KEY + DELIMITER + INVITE;
    }

    public static String getAcceptMessage() {
        return UNIQUE_KEY + DELIMITER + ACCEPT;
    }

    public static String getDenyMessage() {
        return UNIQUE_KEY + DELIMITER + DENY;
    }

    public static String getMessageMove(int pos, String symbol) {
        return UNIQUE_KEY + DELIMITER + MOVE + DELIMITER+ pos + DELIMITER + symbol;
    }

    public static boolean isMessageInvite(String text) {
        return getInviteMessage().equals(text);
    }

    public static boolean isMessageAccept(String text) {
        return getAcceptMessage().equals(text);
    }

    public static boolean isMessageDeny(String text) {
        return getDenyMessage().equals(text);
    }

    public static boolean isMessageMove(String text) {
        return text.startsWith(UNIQUE_KEY + DELIMITER + MOVE + DELIMITER);
    }

    public static boolean isGameOverMessage(String text) {
        return getGameOverMessage().equals(text);
    }

    public static String getSymbol(String text) {
        String[] pieces = text.split(DELIMITER);

        return pieces[3];

    }

    public static int getPosition(String text) {
        String[] pieces = text.split(DELIMITER);

        return Integer.valueOf(pieces[2]);
    }

    public static String getGameOverMessage() {
        return UNIQUE_KEY + DELIMITER + GAMEOVER;
    }
}
