package planet.info.skyline.RequestControler;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Iterator;

import planet.info.skyline.R;
import planet.info.skyline.network.SOAP_API_Client;

import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;

// The types specified here are the input data type, the progress type, and the result type
public class MyAsyncTask extends AsyncTask<Void, Void, String> {

    Context context;
    private ResponseInterface responseInterface;
    private String API_NAME;
    private JSONObject INPUT;
    private ProgressDialog pDialog;

    // String Response="";
    public MyAsyncTask(Context context, ResponseInterface responseInterface, String API_NAME, JSONObject INPUT) {
        this.context = context;
        this.responseInterface = responseInterface;
        this.API_NAME = API_NAME;
        this.INPUT = INPUT;
    }

    protected void onPreExecute() {
        try {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(context.getString(R.string.Loading_text));
            pDialog.setCancelable(false);
            if (!pDialog.isShowing()) {
                pDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String doInBackground(Void... strings) {
        return Execute();
    }

 /*   protected void onProgressUpdate(Progress... values) {
        // Executes whenever publishProgress is called from doInBackground
        // Used to update the progress indicator
        progressBar.setProgress(values[0]);
    }*/

    protected void onPostExecute(String response) {
        try {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        responseInterface.handleResponse(response, API_NAME);
    }

    public String Execute() {

        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = SOAP_API_Client.BASE_URL;
        final String METHOD_NAME = API_NAME;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;

        // Create SOAP request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        Iterator iterator = INPUT.keys();
        try {
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String val = INPUT.getString(key);
                request.addProperty(key, val);
            }
        } catch (Exception e) {
            e.getMessage();
        }


        //nks
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        String receivedString = "";
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return receivedString;
    }


}
