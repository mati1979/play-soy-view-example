package controllers;

import play.api.libs.ws.WSClient;
import play.api.libs.ws.WSRequestHolder;

/**
 * Created by mati on 26/05/2014.
 */
public class HysterixWSClient implements WSClient {

    private WSClient wsClient;

    public HysterixWSClient(final WSClient wsClient) {
        this.wsClient = wsClient;
    }

    @Override
    public <T> T underlying() {
        return wsClient.underlying();
    }

    @Override
    public WSRequestHolder url(String url) {
        return null;
    }

}
