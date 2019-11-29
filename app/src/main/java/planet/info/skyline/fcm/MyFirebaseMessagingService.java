package planet.info.skyline.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import planet.info.skyline.home.MainActivity;
import planet.info.skyline.R;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;


import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String urlofwebservice = URL_EP2 + "/WebService/techlogin_service.asmx";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        try {
            sendMyNotification(message.getNotification().getBody());
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
      /*  SharedPreferences sp = this.getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(Utility.FCM_TOKEN, s).apply();*/

        Shared_Preference.setFCM_TOKEN(this,s);

       // String empId = sp.getString("clientid", "");

        String empId =    Shared_Preference.getLOGIN_USER_ID(this);

        try {
            if (!empId.equals("")) {
                new async_updateToken().execute();
            }
        } catch (Exception e) {
            e.getCause();
        }

    }

    private void sendMyNotification(String message) {
        //On click of notification it redirect to this Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Exhibit Power")
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.exhibit_power_logo_white)
                .setSound(soundUri)
                .setChannelId("my_channel_id_0001") // set channel id
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Utility.random_number_notificationId(), notificationBuilder.build());
    }

    public void UpdateTokenOnServer() {
       // SharedPreferences sp = this.getSharedPreferences("skyline", MODE_PRIVATE);
        String empId =    Shared_Preference.getLOGIN_USER_ID(this);
     //   String fcm_token = sp.getString(Utility.FCM_TOKEN, "");

        String fcm_token =   Shared_Preference.getFCM_TOKEN(this);

        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = urlofwebservice;
        final String SOAP_ACTION = KEY_NAMESPACE + "";
        final String METHOD_NAME = "";
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("emp", empId);
        request.addProperty("token", fcm_token);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        httpTransport.debug = true;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            Log.d("HTTP REQUEST ", httpTransport.requestDump);
            Log.d("HTTP RESPONSE", httpTransport.responseDump);
            Object results = (Object) envelope.getResponse();
            String resultstring = results.toString();
            JSONObject jsonObject = new JSONObject(resultstring);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class async_updateToken extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
           // UpdateTokenOnServer();
            return null;
        }

    }
}
