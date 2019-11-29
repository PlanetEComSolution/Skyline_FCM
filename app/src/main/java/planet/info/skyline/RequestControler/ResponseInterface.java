package planet.info.skyline.RequestControler;

import org.json.JSONException;

/**
 * Created by admin on 04-Oct-17.
 */

public interface ResponseInterface {
        void handleResponse(String responseStr, String api);// throws JSONException;
       // void handleErrorMessage(String response, String api);
}
