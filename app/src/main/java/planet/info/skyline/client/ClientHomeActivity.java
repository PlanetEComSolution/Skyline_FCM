package planet.info.skyline.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import planet.info.skyline.home.LoginActivity;
import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_GetClientNotification;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class ClientHomeActivity extends AppCompatActivity implements View.OnClickListener {
    AlertDialog alertDialog;

    LinearLayout ll_PowerBoard, ll_MyAccount,
            ll_ManageUser, ll_Notification,
            ll_Help, ll_logout_new, ll_ProjectPhotos, ll_Proof_Renders, ll_ProjectFiles, ll_ClientFiles;
    Context context;
    SharedPreferences sp;
    ImageView imgvw_client_logo;
    TextView tv_ver;
    boolean doubleBackToExitPressedOnce = false;
    // private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnmABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String Client_id_Pk, comp_ID;

    int eventCount = 0;
    Button btn_count;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);
        sp = getApplicationContext().getSharedPreferences("skyline", Context.MODE_PRIVATE);
        imgvw_client_logo = findViewById(R.id.client_logo);
        btn_count = findViewById(R.id.count);
        btn_count.setVisibility(View.INVISIBLE);
        //textView1rr
        String user = sp.getString(Utility.CLIENT_LOGIN_UserName, "");
        TextView tv_user = findViewById(R.id.textView1rr);
        tv_user.setText("Welcome\n" + user);

/**********************************************************************************/

        ll_PowerBoard = findViewById(R.id.ll_PowerBoard);
        ll_PowerBoard.setOnClickListener(this);
        ll_MyAccount = findViewById(R.id.ll_MyAccount);
        ll_MyAccount.setOnClickListener(this);
        ll_ManageUser = findViewById(R.id.ll_ManageUser);
        ll_ManageUser.setOnClickListener(this);
        ll_Notification = findViewById(R.id.ll_Notification);
        ll_Notification.setOnClickListener(this);
        ll_Help = findViewById(R.id.ll_Help);
        ll_Help.setOnClickListener(this);
        ll_logout_new = findViewById(R.id.ll_logout_new);
        ll_logout_new.setOnClickListener(this);

        ll_ProjectPhotos = findViewById(R.id.ll_ProjectPhotos);
        ll_ProjectPhotos.setOnClickListener(this);

        ll_Proof_Renders = findViewById(R.id.ll_Proof_Renders);
        ll_Proof_Renders.setOnClickListener(this);

        ll_ProjectFiles = findViewById(R.id.ll_ProjectFiles);
        ll_ProjectFiles.setOnClickListener(this);

        ll_ClientFiles = findViewById(R.id.ll_ClientFiles);
        ll_ClientFiles.setOnClickListener(this);

        tv_ver = findViewById(R.id.tv_ver);
/*****************************************************************************/
        try {
            PackageManager manager = ClientHomeActivity.this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ClientHomeActivity.this.getPackageName(), 0);
            String versionName = info.versionName;
            tv_ver.setText("v" + versionName);
        } catch (Exception e) {
            e.getMessage();
        }
/**********************************************************************************/
        context = ClientHomeActivity.this;

        Client_id_Pk = sp.getString(Utility.CLIENT_LOGIN_userID, "");
        comp_ID = sp.getString(Utility.CLIENT_LOGIN_CompID, "");


        setClientLogo();
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new ConnectionDetector(ClientHomeActivity.this).isConnectingToInternet()) {
                    new Async_EventCount().execute();
                } else {
                    Toast.makeText(ClientHomeActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (new ConnectionDetector(ClientHomeActivity.this).isConnectingToInternet()) {
            new Async_EventCount().execute();
        } else {
            Toast.makeText(ClientHomeActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }

    }


    private void setClientLogo() {
        // client_logo.set

        String imgeName = sp.getString(Utility.CLIENT_LOGIN_Imagepath, "");
        String imagePath = SOAP_API_Client.URL_EP2 + "/register/Client_logo/" + imgeName;
        Glide
                .with(context)
                .load(imagePath)
                .into(imgvw_client_logo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_PowerBoard:
                Click_ll_PowerBoard();
                break;
            case R.id.ll_MyAccount:
                Click_ll_MyAccount();
                break;
            case R.id.ll_ManageUser:
                Click_ll_ManageUser();
                break;
            case R.id.ll_Notification:
                Click_ll_Notification();
                break;
            case R.id.ll_Help:
                Click_ll_Help();
                break;
            case R.id.ll_logout_new:
                Click_ll_logout_new();
                break;
            case R.id.ll_ProjectPhotos:
                startActivity(new Intent(ClientHomeActivity.this, ProjectPhotosActivity.class));
                break;
            case R.id.ll_Proof_Renders:
                startActivity(new Intent(ClientHomeActivity.this, Proof_RendersActivity.class));
                break;

            case R.id.ll_ProjectFiles:
                // startActivity(new Intent(ClientHomeActivity.this, Proof_RendersActivity.class));
                startActivity(new Intent(ClientHomeActivity.this, ProjectFilesActivity.class));

                //Toast.makeText(context, "---- ----", Toast.LENGTH_SHORT).show();

                break;

            case R.id.ll_ClientFiles:
                startActivity(new Intent(ClientHomeActivity.this, ClientFilesActivity.class));
                break;
        }
    }

    public void Click_ll_PowerBoard() {
        //Toast.makeText(context,"Click_ll_PowerBoard",Toast.LENGTH_SHORT).show();

        startActivity(new Intent(ClientHomeActivity.this, DashboardActivity.class));
        //    startActivity(new Intent(ClientHomeActivity.this,Select_Company_job.class));

      /* Intent intent=   new Intent(ClientHomeActivity.this,Select_Company_job.class);
        startActivityForResult(intent,121);
     */


    }

    public void Click_ll_MyAccount() {
        //  Toast.makeText(context,"Click_ll_MyAccount",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ClientHomeActivity.this, MyAccountActivity.class));
    }

    public void Click_ll_ManageUser() {
        // Toast.makeText(context,"----ManageUser----",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ClientHomeActivity.this, UserListActivity.class));
    }

    public void Click_ll_Notification() {
        //   Toast.makeText(context, "-Coming soon-", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ClientHomeActivity.this, Client_Events.class));

    }

    public void Click_ll_Help() {
        // Toast.makeText(context, "----Help----", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(ClientHomeActivity.this, HelpActivity.class));


    }

    public void Click_ll_logout_new() {
        //  Toast.makeText(context,"Click_ll_logout_new",Toast.LENGTH_SHORT).show();
        try {


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientHomeActivity.this);
            LayoutInflater inflater = LayoutInflater.from(ClientHomeActivity.this);
            final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
            dialogView.setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialogBuilder.setView(dialogView);
            final TextView title = dialogView.findViewById(R.id.textView1rr);
            final TextView message = dialogView.findViewById(R.id.texrtdesc);

            final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
            final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
            ImageView close = (ImageView) dialogView.findViewById(R.id.close);
            close.setVisibility(View.INVISIBLE);
            // dialogBuilder.setTitle("Device Details");
            title.setText("Logout?");
            message.setText("Are you sure?");
            positiveBtn.setText("Yes");
            negativeBtn.setText("No");
            negativeBtn.setVisibility(View.VISIBLE);
            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    boolean result = sp.edit().clear().commit();
                    if (result) {

                        Utility.setLoginFalse(ClientHomeActivity.this);
                        finish();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(ClientHomeActivity.this, "Unable to clear app data!", Toast.LENGTH_SHORT).show();
                    }


                }
            });
            negativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();

                }
            });
            alertDialog = dialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();






          /*  new SweetAlertDialog(ClientHomeActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Logout?")
                    .setContentText("Are you sure?")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            boolean result = sp.edit().clear().commit();
                            if (result) {

                                Utility.setLoginFalse(ClientHomeActivity.this);
                                finish();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(ClientHomeActivity.this, "Unable to clear app data!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setCancelText("No")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();

                        }
                    })
                    .show();*/


        } catch (Exception e) {
            e.getCause();
        }

    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Utility.HideRunningClock(getApplicationContext());
            return;
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit!",
                Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);


    }

    public void getProjectPhotos2() {

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetClientNotification;
        final String METHOD_NAME =API_GetClientNotification;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("ClientUserId", comp_ID);//
        request.addProperty("Agency", "0");//
        request.addProperty("IsCount", "1");//


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();
            //  JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String _eventCount = jsonObject1.getString("NewEvent");
                eventCount = Integer.parseInt(_eventCount);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class Async_EventCount extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDoalog = new ProgressDialog(ClientHomeActivity.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getProjectPhotos2();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();
            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }


            btn_count.setText(String.valueOf(eventCount));
            if (eventCount == 0) {
                btn_count.setVisibility(View.INVISIBLE);
            } else {
                btn_count.setVisibility(View.VISIBLE);
            }

            //   eventCount


        }
    }

}
