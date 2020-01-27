package nl.fhict.s3.websocketclient.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import nl.fhict.s3.sharedmodel.GameInfo;
import nl.fhict.s3.sharedmodel.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;

@ClientEndpoint
public class GreeterClientEndpoint extends Observable {

    private static final Logger log = LoggerFactory.getLogger(GreeterClientEndpoint.class);
    private static GreeterClientEndpoint instance = null;
    private static final String URI = "ws://localhost:8095/greeter/";
    private Session session;
    private Gson gson;
    private boolean isRunning = false;
    private String message;

    private GreeterClientEndpoint() {
        gson = new Gson();
    }

    public static GreeterClientEndpoint getInstance() {
        if (instance == null) {
            instance = new GreeterClientEndpoint();
            log.info("GreeterClientEndpoint singleton instantiated");
        }
        return instance;
    }

    public void start() {
        if (!isRunning) {
            startClient();
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            stopClient();
            isRunning = false;
        }
    }

    @OnOpen
    public void onWebSocketConnect(Session session) {
        log.info("Client open session {}", session.getRequestURI());
        this.session = session;
    }

    @OnMessage
    public void onWebSocketText(String message, Session session) {
        this.message = message;
        log.info("Client message received {}", message);
        processMessage(message);
    }

    @OnError
    public void onWebSocketError(Session session, Throwable cause) {
        log.info("Client connection error {}", cause.toString());
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        log.info("Client close session {} for reason {} ", session.getRequestURI(), reason);
        session = null;
    }

    public void sendMessageToServer(User message) {
        String jsonMessage = gson.toJson(message);
        log.info("Sending message to server: {}", message);
        session.getAsyncRemote().sendText(jsonMessage);
    }

    public void sendMessageToServer(GameInfo gameInfo){
        String jsonMessage = gson.toJson(gameInfo);
        log.info("Sending game info message to server: {}", gameInfo);
        log.info("Messagetype is : {}", gameInfo.getMessageType());
        session.getAsyncRemote().sendText(jsonMessage);
    }

    private void startClient() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(URI));
            log.info("Connected to server at {}", URI);

        } catch (IOException | URISyntaxException | DeploymentException ex) {
            log.error("Error in startClient: {}", ex.getMessage());
        }
    }

    private void stopClient() {
        try {
            session.close();
            log.info("Session closed");

        } catch (IOException ex) {
            log.error("Error in stopClient {}", ex.getMessage());
        }
    }

    private void processMessage(String jsonMessage) {
        GameInfo gameInfo;
        log.info("Processing message: {}", jsonMessage);

        try {
            gameInfo = gson.fromJson(jsonMessage, GameInfo.class);
            log.info("Message processed: {}", gameInfo);

            switch (gameInfo.getMessageType()) {
                case "heyMessage":
                    break;
                case "rematch":
                    log.info("Opponent sends if he wants a rematch or not");
                    break;

                case "sendTurn":
                    log.info("Opponent send turn");
                    break;
                case "youAre":
                    if(gameInfo.isRed()){
                        log.info("Player is red");

                    }else {
                        log.info("Player is yellow");
                    }
                    break;
                default:
                    log.info("!Unhandled message type!");
            }



        } catch (JsonSyntaxException ex) {
            log.error("Can't process message: {}", ex.getMessage());
            return;
        }

        this.setChanged();
        this.notifyObservers(gameInfo);
    }
}