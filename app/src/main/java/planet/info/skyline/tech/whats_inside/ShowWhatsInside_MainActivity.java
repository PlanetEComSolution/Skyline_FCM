package planet.info.skyline.tech.whats_inside;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Pattern;

import planet.info.skyline.network.Api;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;


public class ShowWhatsInside_MainActivity extends BaseActivity {
    int REQUEST_CODE = 1;
    String urlCrateId;
    Button btn_ScanQR, btn_Show;
    LinearLayout ll_view;
    TextView tv_Caret_id, tv_msg_validation;
    String crateId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ll_view = (LinearLayout) findViewById(R.id.ll_view_QRInfo);
        ll_view.setVisibility(View.GONE);
        btn_Show = (Button) findViewById(R.id.button_View);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Utility.getTitle("What's Inside"));
        tv_Caret_id = (TextView) findViewById(R.id.tv_Caret_id);
        tv_msg_validation = (TextView) findViewById(R.id.tv_msg_validation);
        tv_msg_validation.setText("Scan Crate QR Code ");

        btn_ScanQR = (Button) findViewById(R.id.button_scan);
        btn_ScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_view.setVisibility(View.GONE);

                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.setPackage(getApplicationContext().getPackageName());
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, REQUEST_CODE);


            }
        });

        btn_Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new ConnectionDetector(ShowWhatsInside_MainActivity.this).isConnectingToInternet()) {

                    Intent intent = new Intent(getApplicationContext(), ShowWhatsInside_Activity.class);
                    intent.putExtra(Utility.KEY_CRATE_ID, crateId);
                    startActivityForResult(intent, REQUEST_CODE);

                } else {
                    Toast.makeText(ShowWhatsInside_MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String contents = data.getStringExtra("SCAN_RESULT");
            if (requestCode == 1) {

                if (contents.contains("edit_id=")) {
                    int index = contents.indexOf("edit_id=");
                    crateId = contents.substring(index + 8, contents.indexOf("_client="));


                    /*Call api for crateName*/
                    getCrateName(crateId);

                    /**/

                  /*  String CrateName = contents.substring(contents.lastIndexOf("=") + 1);
                    //nks
                    if (CrateName.contains("^")) {
                        CrateName = CrateName.replaceAll(Pattern.quote("^"), "#");//nks
                    }
                    //nks*/

                    urlCrateId = URL_EP1 + Api.API_SHOW_CRATE + crateId;

                 //   tv_msg_validation.setText("Scan Crate QR Code");
                  //  ll_view.setVisibility(View.VISIBLE);
                    // tv_Caret_id.setText("Crate Number : " + CrateName);

                } else {
                    ll_view.setVisibility(View.GONE);
                   // tv_msg_validation.setText("Invalid QR code, Please scan another QR code !");
                    tv_msg_validation.setText("Please scan valid QR Code of Crate!");
                }


            }
        } else {
            ll_view.setVisibility(View.GONE);
            tv_msg_validation.setText("Scan Crate QR Code ");
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getCrateName(String CrateId) {
        final ProgressDialog ringProgressDialog = new ProgressDialog(ShowWhatsInside_MainActivity.this);
        ringProgressDialog.setMessage("Kindly wait...");
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        String urlCrateId = URL_EP1 +Api.API_GET_CRATE_NAME_BY_CARTE_ID + CrateId;

        JsonObjectRequest bb = new JsonObjectRequest(Request.Method.GET, urlCrateId,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub

                //    tv_msg.setVisibility(View.GONE);
                try {
                    JSONArray jsonArryy = obj.getJSONArray("cds");
                    JSONObject obj1 = jsonArryy.getJSONObject(0);
                    String CrateName = obj1.getString("crate_number");


                    //nks
                    if (CrateName.contains("^")) {
                        CrateName = CrateName.replaceAll(Pattern.quote("^"), "#");//nks
                    }
                    //nks
                    if (CrateName == null || CrateName.trim().equalsIgnoreCase("null") || CrateName.trim().equals("")) {
                        tv_Caret_id.setText("Crate Number: Not Available");
                    } else {
                        tv_Caret_id.setText("Crate Number: " + CrateName);
                    }
                    tv_msg_validation.setText("Scan Crate QR Code");
                    ll_view.setVisibility(View.VISIBLE);

                    if (ringProgressDialog.isShowing()) {
                        ringProgressDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.getMessage();
                    tv_Caret_id.setText("Crate Number: Not Available");
                    if (ringProgressDialog.isShowing()) {
                        ringProgressDialog.dismiss();
                    }

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                if (ringProgressDialog.isShowing()) {
                    ringProgressDialog.dismiss();
                }
                //  tv_msg.setVisibility(View.VISIBLE);


            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }


}
