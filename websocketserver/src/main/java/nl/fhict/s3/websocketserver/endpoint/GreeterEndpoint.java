package nl.fhict.s3.websocketserver.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import nl.fhict.s3.sharedmodel.GameInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint(value = "/greeter/")
public class GreeterEndpoint {

    private static final Logger log = LoggerFactory.getLogger(GreeterEndpoint.class);
    private static final List<Session> sessions = new ArrayList<>();
    private static boolean gameStart = false;
    private static Session player1;
    private static Session player2;


    @OnOpen
    public void onConnect(Session session) {
        log.info("Connected SessionID: {}", session.getId());

        sessions.add(session);
        log.info("Session added. Session count is {}", sessions.size());
    }

    @OnMessage
    public void onText(String message, Session session) {

        log.info("Session ID: {} Received: {}", session.getId(), message);
        handleMessageFromClient(message, session);
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        log.info("Session ID: {} Closed. Reason: {}", session.getId(), reason);

        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable cause, Session session) {
        log.error("Session ID: {} Error: {}", session.getId(), cause.getMessage());
    }

    private void handleMessageFromClient(String jsonMessage, Session session) {
        Gson gson = new Gson();
        log.info("Session ID: {} Handling message: {}", session.getId(), jsonMessage);

        try {
            GameInfo message = gson.fromJson(jsonMessage, GameInfo.class);
            log.info("Session ID: {} Message handled: {}", session.getId(), message);
            log.info("Session ID: {} Message type is: {}", session.getId(), message.getMessageType());

            switch (message.getMessageType()) {
                case "heyMessage":

                    if (player1 == null) {
                        player1 = session;
                    } else if (player2 == null) {
                        player2 = session;
                    }
                    break;
                case "rematch":

                case "sendTurn":

                    sendToOpponent(session, jsonMessage);

                    break;
                default:
                    log.info("!Unhandled message type!");
            }

            // als 2 clients dan beginnen met spel
            // stuur naar clients welke kleur/volgorde
            if (sessions.size() == 2) {
                if (gameStart == false) {

                    // dynamisch maken evt in player stoppen (eerst player classe maken)
                    GameInfo gameInfoPlayer1 = new GameInfo();
                    gameInfoPlayer1.setMessageType("youAre");
                    gameInfoPlayer1.setRed(true);

                    GameInfo gameInfoPlayer2 = new GameInfo();
                    gameInfoPlayer2.setMessageType("youAre");
                    gameInfoPlayer2.setRed(false);

                    String jsonMessageP1 = gson.toJson(gameInfoPlayer1);
                    String jsonMessageP2 = gson.toJson(gameInfoPlayer2);

                    try {
                        sessions.get(0).getBasicRemote().sendText(jsonMessageP1);
                        sessions.get(1).getBasicRemote().sendText(jsonMessageP2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    gameStart = true;
                }
            }

        } catch (JsonSyntaxException ex) {
            log.error("Can't process message: {0}", ex);
        }
    }

    private void sendToOpponent(Session session, String jsonMessage) {
        if (session.getId().equals(player1.getId())) {
            try {
                player2.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (session.getId().equals(player2.getId())) {
            try {
                player1.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendStartingColor() {

    }
}
