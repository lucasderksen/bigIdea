package nl.fhict.s3.websocketclient;

import nl.fhict.s3.sharedmodel.GameInfo;
import nl.fhict.s3.sharedmodel.User;
import nl.fhict.s3.websocketclient.singleplayer.Disc;
import nl.fhict.s3.websocketclient.endpoint.GreeterClientEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

public class WebsocketClient implements Observer {

    private static final Logger log = LoggerFactory.getLogger(WebsocketClient.class);
    public GreeterClientEndpoint greeterClientEndpoint;
    public GameInfo gameInfo;

    public void start() {
        try {
            greeterClientEndpoint = GreeterClientEndpoint.getInstance();
            greeterClientEndpoint.addObserver(this);
            greeterClientEndpoint.start();
            log.info("Websocket client started");
            lookingForGame();
        } catch (Exception ex) {
            log.error("Client couldn't start.");
        }
    }

    public void example() {
        greeterClientEndpoint.sendMessageToServer(new User("Whoohoo", "test"));
    }

    public void lookingForGame() {
        GameInfo heyMessage = new GameInfo();
        heyMessage.setMessageType("heyMessage");
        greeterClientEndpoint.sendMessageToServer(heyMessage);
    }

    public void sendTurn(Disc disc, int column, int row) {
        GameInfo gameInfoTurn = new GameInfo(disc.isRed(), column, row);
        gameInfoTurn.setMessageType("sendTurn");
        greeterClientEndpoint.sendMessageToServer(gameInfoTurn);
    }

    public void rematch(boolean rematch) {
        GameInfo gameInfoRematch = new GameInfo();
        gameInfoRematch.setRed(rematch);
        gameInfoRematch.setMessageType("rematch");
        greeterClientEndpoint.sendMessageToServer(gameInfoRematch);
    }

    public void stop() {
        greeterClientEndpoint.stop();
        log.info("Websocket client stopped");
    }

    @Override
    public void update(Observable o, Object arg) {
        log.info("Update called. {}", arg);
        log.info("Update called, object gameinfo. {}", ((GameInfo) arg).isRed());

        gameInfo = ((GameInfo) arg);
    }
}
