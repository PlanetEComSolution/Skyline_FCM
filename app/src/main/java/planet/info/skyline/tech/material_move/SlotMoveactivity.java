package planet.info.skyline.tech.material_move;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


import planet.info.skyline.network.Api;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;

public class SlotMoveactivity extends BaseActivity {
    String LocationId, id = "", Location_Name = "";
    String LocationType = "", locationname = "";
    String linkforconect = "";
   // SharedPreferences sp;
    HashMap<String, String> hasmap;
    String new_loc;
    //Editor ed;
    int Scann_value;
    int result_logic = 0;
    ProgressDialog pDialog;
    Map<String, HashMap<String, String>> lochasmap = new HashMap<String, HashMap<String, String>>();
    //Map<String,HashMap<String, String>> cratehasmap=new HashMap<String, HashMap<String,String>>();
    Map<String, HashMap<String, String>> cratehasmap = new HashMap<String, HashMap<String, String>>();
    AlertDialog alertDialog;
    String mainCrate_Name;
    String mainCrate_Id;
    String Crate_id, Crate_Name, Client_id;
    ArrayList<HashMap<String, String>> list_CratesPackInto = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_slot_moveactivity);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        showforfisttime();
    }

    public void showdialog() {


        try {
            pDialog.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void hidedialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void Show_Dialog_for_More_Crate_Scan(String message) {
        final Dialog showd = new Dialog(SlotMoveactivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
       // sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        //ed = sp.edit();
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        // texrtdesc.setText("Please scan the crates you want to move");
        texrtdesc.setText(message);
        nofo.setText("  No  ");
        yesfo.setText("Yes, scan another crate");
        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Please select");
        nofo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                if (cratehasmap.size() > 0) {
                    showscannewbin();
                } else {
                    Intent i = new Intent(SlotMoveactivity.this, MainActivity.class);
                    startActivity(i);
                    SlotMoveactivity.this.finish();
                }

            }
        });
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                Intent i = new Intent(SlotMoveactivity.this, MainActivity.class);
                startActivity(i);
                SlotMoveactivity.this.finish();
            }
        });
        yesfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();

                } catch (Exception e) {
                    e.getMessage();
                }
                //finish();
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    Scann_value = 1;
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                }
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void showscannewbin() {
        final Dialog showd = new Dialog(SlotMoveactivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        texrtdesc.setText("Move the Crate(s) and scan the new Bin/Freight Vendor/Area");
        nofo.setText("  Ok  ");
        yesfo.setText("");
        yesfo.setVisibility(View.GONE);
        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Please select");
        nofo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                try {

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

                    Scann_value = 4;
                    startActivityForResult(intent, 4);

                } catch (Exception e) {

                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);

                }
            }
        });
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                Intent i = new Intent(SlotMoveactivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });
        yesfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                finish();
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void showforfisttime() {
        final Dialog showd = new Dialog(SlotMoveactivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(true);
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        texrtdesc.setText("Please scan the Crate(s) you want to move");
        nofo.setText("  OK  ");
        yesfo.setText("");
        yesfo.setVisibility(View.GONE);
        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Please select");
        nofo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                try {

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

                    Scann_value = 1;
                    startActivityForResult(intent, Scann_value);

                } catch (Exception e) {

                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);

                }
            }
        });
        close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                try {
                    Intent i = new Intent(SlotMoveactivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    e.getMessage();
                }
                return true;
            }
        });

        //nks
        showd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                try {
                    Intent i = new Intent(SlotMoveactivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
        //nks

        yesfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();

                } catch (Exception e) {
                    e.getMessage();
                }

                finish();
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void Confirm_Dialog_to_MoveCrate() {
        final Dialog showd = new Dialog(SlotMoveactivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);

        String crate_names = "";
        String[] array;
        //sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        //ed = sp.edit();
        try {


            if (id.contains(",")) {
                array = id.split(",");

                for (int i = 0; i <= array.length; i++) {

                    crate_names = crate_names + cratehasmap.get(array[i]).get("name") + " ,";

                }
            } else {

                crate_names = cratehasmap.get(id).get("name");
            }
        } catch (Exception e) {

        }
        if (crate_names.endsWith(",")) {
            crate_names = crate_names.substring(0, crate_names.length() - 1);
        }

//        if (result_logic == 1) {
//            texrtdesc.setText("Please confirm that you have moved " + crate_names + " crate(s) to " + new_loc);
//
//        } else {


        //first char to upper case
        String loc = LocationType.substring(0, 1).toUpperCase() + LocationType.substring(1);

        texrtdesc.setText("Please confirm that you have moved " + crate_names +
                " crate(s) to " + loc + "  " + Location_Name);


        //   }
        nofo.setText("  Yes  ");
        yesfo.setText("  No  ");

        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Please select");


        nofo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


            //    String clientid = sp.getString("tname", "");
                String clientid =  Shared_Preference.getLOGIN_USERNAME(SlotMoveactivity.this);
                if (id.endsWith(",")) {
                    try {
                        id = id.substring(0, id.length() - 1);
                    } catch (Exception e) {

                    }
                }

                if (result_logic == 1) {//Freight vendor
                    linkforconect = URL_EP1 + Api.API_CRATE_INFO_UPDATE +"cratesid=" + id + "&type=" + LocationType + "&fname=" + Location_Name + "&tech_id=" + clientid;
                } else {//Bin
                    linkforconect = URL_EP1 + Api.API_CRATE_INFO_UPDATE +"cratesid=" + id + "&type=" + LocationType + "&aid=" + LocationId + "&tech_id=" + clientid + "&print=yes";
                }

                linkforconect = linkforconect.replace(" ", "%20");
                Log.d("CrateMoveApi", linkforconect);
                if (new ConnectionDetector(SlotMoveactivity.this).isConnectingToInternet()) {
                    Call_Api_MoveCrateToBin_Freight_Area();
                } else {
                    Toast.makeText(SlotMoveactivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }

            }
        });
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                Intent i = new Intent(SlotMoveactivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });
        yesfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //nks
                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SlotMoveactivity.this);
                LayoutInflater inflater = LayoutInflater.from(SlotMoveactivity.this);
                final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
                dialogView.setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogBuilder.setView(dialogView);
                final TextView title = dialogView.findViewById(R.id.textView1rr);
                final  TextView message = dialogView.findViewById(R.id.texrtdesc);

                final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
                final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
                ImageView close = (ImageView) dialogView.findViewById(R.id.close);
                close.setVisibility(View.INVISIBLE);
                // dialogBuilder.setTitle("Device Details");
                title.setText("Please select the action!");
                message.setText("Do you want to scan the\nBin/Freight Vendor again?");
                positiveBtn.setText("Yes");
                negativeBtn.setText("No");
                negativeBtn.setVisibility(View.VISIBLE);
                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                        intent.setPackage(getApplicationContext().getPackageName());
                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                        Scann_value = 4;
                        startActivityForResult(intent, Scann_value);

                    }
                });
                negativeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Intent iiu = new Intent(SlotMoveactivity.this, MainActivity.class);
                        iiu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        iiu.putExtra("urString", "exit");
                        startActivity(iiu);

                        finish();


                    }
                });
                alertDialog = dialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();









             /*   new SweetAlertDialog(SlotMoveactivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Please choose the option !")
                        .setContentText("Do you want to scan the Bin/Freight Vendor again? ")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                                intent.setPackage(getApplicationContext().getPackageName());
                                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                                Scann_value = 4;
                                startActivityForResult(intent, Scann_value);


                            }
                        })
                        .setCancelText("No")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();

                                Intent iiu = new Intent(SlotMoveactivity.this, MainActivity.class);
                                iiu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                iiu.putExtra("urString", "exit");
                                startActivity(iiu);

                                finish();


                            }
                        })
                        .show();*/


                //nks

            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {  //Crate
                Crate_Name = "";
                try {

                    Crate_id = "";
                    Client_id = "";

                    String contents = data.getStringExtra("SCAN_RESULT");
                    if (contents.contains("^")) {
                        contents = contents.replaceAll(Pattern.quote("^"), "#");//nks
                    }


                    if (contents.contains("crate_name")) {  // valid crate QR code

                        String hom = contents;//"exhibitpower.com/admin/inventory/qrcrates1.php?act=edit_id=671_client=224_loc=S14-3-0";
                        Crate_id = hom.substring(hom.indexOf("_id=") + 4, hom.indexOf("_client"));
                        Crate_Name = hom.substring(hom.indexOf("_crate_name=") + 12, hom.length());
                        Client_id = hom.substring(hom.indexOf("client="), hom.indexOf("_loc"));
                        Client_id = Client_id.substring(Client_id.indexOf("=") + 1);

                        if (Crate_Name == null || Crate_Name.equals("")) {
                            getCrateName();
                        } else {
                            Check_This_Crate_Packed_In_Other_Crate(Crate_id, Crate_Name);
                        }

                    } else {
                        ShowValidationDialog("Please scan valid QR Code of Crate!", 1);
                    }


                } catch (Exception e) {
                }

            } else if (requestCode == 4) {  //Bin/Freight vendor/Area
                hasmap = new HashMap<String, String>();
                String contents = data.getStringExtra("SCAN_RESULT");
                /*validation-can scan only Bin/Freight vendor/Area*/
                //nks
                if (contents.contains("^")) {
                    contents = contents.replaceAll(Pattern.quote("^"), "#");//nks
                }
                //nks
                Log.d("BHANU----second>", contents);
                String hom = contents;


/*
bin:-
https://beta.ep2.businesstowork.com/ep1//superadmin/inventory/
warehouse-qr-code-new.php?type=bin_id=992_aid=260_w=473_wn=TR1221

area:-
https://beta.ep2.businesstowork.com/ep1//superadmin/inventory/
show-qr-warehouse-one.php?type=area_id=39_loc=Client

freight:-
id :586Vname :Pankaj Saini:Cat :
Service Production CoordinatorScanValue : 1
*/

                if ((contents.contains(",") || (contents.contains("crate_name")))) {
                    ShowValidationDialog("Please scan valid QR Code of Bin/Freight Vendor/Area!", 4);

                } else if (hom.contains("Vname")) {//freight
                    result_logic = 1;

                    LocationType = "freight";
                    try {
                        String[] tokens = hom.split(":");

                        Location_Name = tokens[2].trim();

                        Confirm_Dialog_to_MoveCrate();


                    } catch (Exception e) {

                        Log.d("BHANU", String.valueOf(e));
                    }
                } else if (contents.contains("type=bin_id")) {    //bin
                    result_logic = 2;
                    try {
                        LocationType = "bin";
                        LocationId = hom.substring(hom.indexOf("bin_id=") + 7, hom.indexOf("_aid"));
                        GetBinName(LocationId);
                    } catch (Exception e) {
                        LocationId = "";

                    }
                } else if (contents.contains("type=area_id")) {  //area
                    result_logic = 2;
                    try {
                        LocationType = "area";
                        LocationId = hom.substring(hom.indexOf("area_id=") + 8, hom.indexOf("_loc"));

                        GetAreaName(LocationId);
                    } catch (Exception e) {
                        LocationId = "";

                    }

                } else if (contents.contains("id") && contents.contains("aid")) { // Bin, for OLD QR Codes
                    result_logic = 2;
                    try {
                        LocationType = "bin";
                        LocationId = hom.substring(hom.indexOf("id=") + 3, hom.indexOf("_aid"));
                        GetBinName(LocationId);
                    } catch (Exception e) {
                        LocationId = "";

                    }
                } else {
                    ShowValidationDialog("Please scan valid QR Code of Bin/Freight Vendor/Area!", 4);

                }


            } else if (requestCode == 2) {
                Confirm_Dialog_to_MoveCrate();
            } else if (requestCode == 10) {


                String cratename = "";
                try {
                    String result = "";
                    String contents = data.getStringExtra("SCAN_RESULT");
                    if (contents.contains("^")) {
                        contents = contents.replaceAll(Pattern.quote("^"), "#");//nks
                    }

                    if (contents.contains("loc=")) {
                        String ll = contents;
                        String sss = ll.substring(ll.indexOf("_id="));
                        String hom = contents;//"exhibitpower.com/admin/inventory/qrcrates1.php?act=edit_id=671_client=224_loc=S14-3-0";
                        result = hom.substring(hom.indexOf("_id=") + 4, hom.indexOf("_client"));
                        cratename = hom.substring(hom.indexOf("_crate_name=") + 12, hom.length());


                        if (result.equals(mainCrate_Id)) { // to check are we scanning same Main_Crate
                            if (cratehasmap.size() > 0) {
                                if (cratehasmap.containsKey(result)) { // to check if the Main crate is already added to move
                                    Show_Dialog_for_More_Crate_Scan("This crate is already added! \n Do you want to scan more Crates?");//this is for scanning more crates to move
                                } else {
                                    HashMap<String, String> hasmap = new HashMap<>();
                                    hasmap.put("id", result);
                                    hasmap.put("name", cratename);
                                    cratehasmap.put(result, hasmap);

                                    if (id.equalsIgnoreCase("")) {
                                        id = result;
                                    } else {
                                        id = id + "," + result;
                                    }

                                    Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crates?");//this is for scanning more crates to move
                                }
                            } else {
                                HashMap<String, String> hasmap = new HashMap<>();
                                hasmap.put("id", result);
                                hasmap.put("name", cratename);
                                cratehasmap.put(result, hasmap);
                                if (id.equalsIgnoreCase("")) {
                                    id = result;
                                } else {
                                    id = id + "," + result;
                                }

                                Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crates?");//this is for scanning more crates to move
                            }

                        } else {

                            ShowValidationDialog("Please scan " + mainCrate_Name + " QR code!", 10);


                        }


                    } else {

                        ShowValidationDialog("Please scan valid QR Code of Crate!", 10);


                    }


                } catch (Exception e) {
                }
            }


        } else {
            final Dialog diloge = new Dialog(SlotMoveactivity.this); // open a diloge if usre Scann wrong qr code
            diloge.requestWindowFeature(Window.FEATURE_NO_TITLE);
            diloge.setContentView(R.layout.validation2_new);
            diloge.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            diloge.setCancelable(true);
            Button scann = (Button) diloge.findViewById(R.id.scann_again);
            Button scann1 = (Button) diloge.findViewById(R.id.scann_again1);
            ImageView img_close = (ImageView) diloge.findViewById(R.id.close);

            img_close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        diloge.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    showforfisttime();
                }
            });

            //nks
            diloge.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                    try {
                        diloge.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    // showforfisttime();

                    try {
                        Intent i = new Intent(SlotMoveactivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            });

            //nks
            scann1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent iiu = new Intent(SlotMoveactivity.this, MainActivity.class);
                    iiu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    iiu.putExtra("urString", "exit");
                    startActivity(iiu);
                    finish();


                }
            });
            scann.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                        intent.setPackage(getApplicationContext().getPackageName());
                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

                        Scann_value = 4;
                        startActivityForResult(intent, Scann_value);

                    } catch (Exception e) {

                        Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                        startActivity(marketIntent);

                    }
                }
            });
            try {
                diloge.show();
            } catch (Exception e) {
                e.getMessage();
            }


        }


    }


    public void Call_Api_MoveCrateToBin_Freight_Area() {

        //nks
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(SlotMoveactivity.this);
        progressDoalog.setMessage("Please wait....");
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        try {
            progressDoalog.show();
        } catch (Exception e) {
            e.getMessage();
        }

        //nks

        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, linkforconect, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                if (progressDoalog.isShowing()) {

                    try {
                        progressDoalog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
                //nks

                Log.d("BHANU--RESULT", obj.toString());
                String status = "";
                try {
                    JSONObject s = obj.getJSONObject("cds");
                    status = s.getString("status");
                } catch (Exception e) {

                }

                //  {"cds":{"status":"1"}}


                // hidedialog();
                //finish();
                if (status.equalsIgnoreCase("1")) {
                    Toast.makeText(SlotMoveactivity.this, "Location updated successfully !", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(SlotMoveactivity.this, "Location updation failed ! ", Toast.LENGTH_LONG).show();
                }
                Intent iiu = new Intent(SlotMoveactivity.this, MainActivity.class);
                iiu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                iiu.putExtra("urString", "exit");
                startActivity(iiu);
                finish();
                //hideprogressdialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDoalog.isShowing()) {

                    try {
                        progressDoalog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
                //nks

                // hidedialog();
                //finish();
                Intent iiu = new Intent(SlotMoveactivity.this, MainActivity.class);
                iiu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                iiu.putExtra("urString", "exit");
                startActivity(iiu);
                finish();
                //	hideprogressdialog();
                Log.e(" mainactivity", arg0.toString());

            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }

    public void getCrateName() {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(SlotMoveactivity.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }

        //nks
        String url = SOAP_API_Client.URL_EP1 +Api.API_FETCH_CRATE_NAME + Crate_id;
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                //nks

                    try {
                        if (progressDailog.isShowing())
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                try {

                    JSONArray jsonArray = obj.getJSONArray("cds");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Crate_Name = jsonObject.getString("crate_number");
                    Check_This_Crate_Packed_In_Other_Crate(Crate_id, Crate_Name);

                } catch (Exception e) {
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks

                    try {
                        if (progressDailog.isShowing())
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                Toast.makeText(SlotMoveactivity.this, "Some api error occurred!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SlotMoveactivity.this, MainActivity.class));
                finish();
            }
        });
        AppController.getInstance().addToRequestQueue(bb);

    }

    public void Check_This_Crate_Packed_In_Other_Crate(final String CrateId, final String CrateName) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(SlotMoveactivity.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }

        //nks
        String url = SOAP_API_Client.URL_EP1 +Api.API_CHECK_CRATE + CrateId;
        Log.e("checkin_other",url);
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                //nks
                try {
                if (progressDailog.isShowing()) {
                        progressDailog.dismiss();
                    }
                }
                catch (Exception e) {
                    e.getMessage();
                }

                try {
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1"))//means crate is packed within another crate
                    {
                        mainCrate_Name = jsonObject.getString("main_crate");
                        mainCrate_Id = jsonObject.getString("crt_id");
                        if (cratehasmap.size() > 0) {
                            if (cratehasmap.containsKey(mainCrate_Id)) {

                                // String message = "This crate is packed inside crate: " + mainCrate_Name + "\n and crate " + mainCrate_Name + " is already added!\n" + "Do you want to scan more Crates?";
                                // Show_Dialog_for_More_Crate_Scan(message);//this is for scanning more crates to move

                                Show_Dialog_for_Main_Crate_Move(mainCrate_Name, mainCrate_Id);
                            } else {
                                Show_Dialog_for_Main_Crate_Move(mainCrate_Name, mainCrate_Id);
                            }
                        } else {
                            Show_Dialog_for_Main_Crate_Move(mainCrate_Name, mainCrate_Id);
                        }
                    } else {

                        /**/

                        Check_Crates_Packed_in_this_Crate(CrateId, CrateName);

                        /**/

                        /*******Add this crate to list

                         Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crates?");//this is for scanning more crates to move*/
                    }
                } catch (Exception e) {
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks

                    try {
                        if (progressDailog.isShowing())
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                Toast.makeText(SlotMoveactivity.this, "Some api error occurred!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SlotMoveactivity.this, MainActivity.class));
                finish();
            }
        });
        AppController.getInstance().addToRequestQueue(bb);

    }

    public void Show_Dialog_for_Main_Crate_Move(String mainCrate_Name, String mainCrate_Id) {

        final Dialog showd = new Dialog(SlotMoveactivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_main_crate_move);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);

        TextView text_Main_crateName = (TextView) showd.findViewById(R.id.text_Main_crateName);
        Button btn_MoveMainCrate = (Button) showd.findViewById(R.id.btn_MoveMainCrate);
        Button btn_RemoveFromMainCrate = (Button) showd.findViewById(R.id.btn_RemoveFromMainCrate);
        Button btn_PackInAnotherCrate = (Button) showd.findViewById(R.id.btn_PackInAnotherCrate);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        text_Main_crateName.setText(mainCrate_Name);
        TextView text_crateMsg = (TextView) showd.findViewById(R.id.textView1rr);

        String sourceString = "This crate is packed inside: " + "<b>" + mainCrate_Name + "</b> ";

        text_crateMsg.setText(Html.fromHtml(sourceString));

        btn_MoveMainCrate.setText("Please scan the " + mainCrate_Name + " QR Code to move " + mainCrate_Name + " and its contents.");
        btn_RemoveFromMainCrate.setText("Remove this crate from " + mainCrate_Name + " to Store/Ship alone.");
        btn_PackInAnotherCrate.setText("Pack this crate in a different crate.");

        btn_MoveMainCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 10);

                } catch (Exception e) {
                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    marketIntent.setPackage(getApplicationContext().getPackageName());
                    startActivity(marketIntent);

                }
            }
        });

        btn_RemoveFromMainCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                Remove_Crate_from_Main_Crate_to_Ship_Alone();
            }
        });


        btn_PackInAnotherCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                Get_Crates_to_Pack_in();
            }
        });


        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Intent i = new Intent(SlotMoveactivity.this, MainActivity.class);
                startActivity(i);
                SlotMoveactivity.this.finish();
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void ShowValidationDialog(String message, final int extra) {
        final Dialog diloge = new Dialog(SlotMoveactivity.this); // open a diloge if usre Scann wrong qr code
        diloge.requestWindowFeature(Window.FEATURE_NO_TITLE);
        diloge.setContentView(R.layout.validation_new);
        diloge.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        diloge.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        diloge.setCancelable(false);
        TextView scann = (TextView) diloge.findViewById(R.id.scann_again);
        TextView texrtdesc = (TextView) diloge.findViewById(R.id.texrtdesc);
        ImageView close = (ImageView) diloge.findViewById(R.id.close);

        texrtdesc.setText(message);

        scann.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    diloge.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, extra);

                } catch (Exception e) {
                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    marketIntent.setPackage(getApplicationContext().getPackageName());
                    startActivity(marketIntent);

                }
            }
        });

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    diloge.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                Intent iiu = new Intent(SlotMoveactivity.this, MainActivity.class);
                iiu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                iiu.putExtra("urString", "exit");
                startActivity(iiu);
                finish();
            }
        });


        try {
            diloge.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void Remove_Crate_from_Main_Crate_to_Ship_Alone() {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(SlotMoveactivity.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }
      //  sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
    //    String clientid = sp.getString("tname", "");
        String clientid =  Shared_Preference.getLOGIN_USERNAME(SlotMoveactivity.this);
        //nks
        String url = SOAP_API_Client.URL_EP1 + Api.API_STORE_ALONE_CRATE + Crate_id + "&tech_id=" + clientid;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success"))//means crate is shipped alone
                    {
                        String Message = "Crate " + Crate_Name + " has been shipped alone. \n Do you want to move this crate?";
                        Dialog_Crate_Shipped_Alone(Message, 0);
                    } else {

                        Toast.makeText(SlotMoveactivity.this, "Failed! Crate " + Crate_Name + " has not been shipped alone!", Toast.LENGTH_LONG).show();
                        Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crate(s)?");
                    }


                } catch (Exception e) {
                    e.getMessage();
                }

                if (progressDailog.isShowing()) {

                    try {
                        progressDailog.dismiss();

                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {

                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
                Toast.makeText(SlotMoveactivity.this, "Failed! Crate " + Crate_Name + " has not been shipped alone!", Toast.LENGTH_SHORT).show();
                Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crate(s)?");
            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void Dialog_Crate_Shipped_Alone(String message, final int extra) {
        final Dialog showd = new Dialog(SlotMoveactivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        Button Btn_No = (Button) showd.findViewById(R.id.Btn_No);
        Button Btn_Yes = (Button) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView txt_message = (TextView) showd.findViewById(R.id.texrtdesc);
        txt_message.setText(message);
        Btn_Yes.setText("Yes");
        Btn_No.setText("No");


        Btn_Yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    HashMap<String, String> hasmap = new HashMap<>();
                    hasmap.put("id", Crate_id);
                    hasmap.put("name", Crate_Name);
                    cratehasmap.put(Crate_id, hasmap);
                    if (id.equalsIgnoreCase("")) {
                        id = Crate_id;
                    } else {
                        id = id + "," + Crate_id;
                    }
                } catch (Exception e) {

                }

                Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crates?");//this is for scanning more crates to move
            }
        });

        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                Intent i = new Intent(SlotMoveactivity.this, MainActivity.class);
                startActivity(i);
                SlotMoveactivity.this.finish();
            }
        });

        Btn_No.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crates?");//this is for scanning more crates to move

            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void Get_Crates_to_Pack_in() {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(SlotMoveactivity.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        try {
            progressDailog.show();

        } catch (Exception e) {
            e.getMessage();
        }

    //    sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);

        list_CratesPackInto = new ArrayList<>();
        //nks
        String url = SOAP_API_Client.URL_EP1 + Api.API_CHECK_CRATE_PACKED_IN_OTHER_CRATE + Client_id + "&crate_id=" + Crate_id;
        url = url.replace(" ", "%20");
        Log.d("get_crates", url);
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                    JSONArray jsonArray = obj.getJSONArray("cds");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String Crate_Id = jsonObject.getString("id");
                        String Crate_Number = jsonObject.getString("crate_number");
                        String Crate_Desc = jsonObject.getString("description");

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Crate_Id", Crate_Id);
                        hashMap.put("Crate_Number", Crate_Number);
                        hashMap.put("Crate_Desc", Crate_Desc);
                        list_CratesPackInto.add(hashMap);

                    }
                    Show_Dialog_for_Crates();

                } catch (Exception e) {
                    e.getMessage();
                }

                if (progressDailog.isShowing()) {

                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {

                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
                Toast.makeText(SlotMoveactivity.this, "Failed! Error getting crate list!", Toast.LENGTH_SHORT).show();
                Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crate(s)?");
            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }


    public void Show_Dialog_for_Crates() {

        final Dialog showd = new Dialog(SlotMoveactivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.radiogroupfortimesheat);
        showd.setCancelable(true);//nks
        showd.setCanceledOnTouchOutside(false);
        TextView title = (TextView) showd.findViewById(R.id.textView1rr);

        title.setText("Select Crate");
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

        RadioGroup rg = (RadioGroup) showd.findViewById(R.id.radioGroup1);
        try {
            for (int i = 0; i < list_CratesPackInto.size(); i++) {
                RadioButton button = new RadioButton(this);
                button.setId(Integer.parseInt(list_CratesPackInto.get(i).get("Crate_Id")));
                String CrateNumber = list_CratesPackInto.get(i).get("Crate_Number");
                String CrateDesc = list_CratesPackInto.get(i).get("Crate_Desc");
                String text = CrateNumber + "\n" + CrateDesc;
                button.setText(text);
                button.setChecked(false);
                rg.addView(button);
            }
        } catch (Exception e) {
            e.getMessage();
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String CrateNum = (String) radioButton.getText();
                CrateNum = CrateNum.substring(0, CrateNum.indexOf("\n"));

                Dialog_Confirmation(checkedId + "", CrateNum);

                try {
                    showd.dismiss();

                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        //nks
        showd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onBackPressed();
            }
        });

    }

    public void Dialog_Confirmation(final String CrateId, final String CrateName) {
        final Dialog showd = new Dialog(SlotMoveactivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        Button Btn_No = (Button) showd.findViewById(R.id.Btn_No);
        Button Btn_Yes = (Button) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView txt_message = (TextView) showd.findViewById(R.id.texrtdesc);
        String message = "Please confirm that you have moved " + Crate_Name + " crate to crate " + CrateName;
        txt_message.setText(message);
        Btn_Yes.setText("Yes");
        Btn_No.setText("No");


        Btn_Yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    Move_CrateInto_Crate(Crate_id, CrateId);
                } catch (Exception e) {

                }


            }
        });

        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                Intent i = new Intent(SlotMoveactivity.this, MainActivity.class);
                startActivity(i);
                SlotMoveactivity.this.finish();
            }
        });

        Btn_No.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crates?");//this is for scanning more crates to move

            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void Move_CrateInto_Crate(String CrateToMove, String CrateIntoMove) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(SlotMoveactivity.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }
       // sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
      //  String techName = sp.getString("tname", "");
        String techName =  Shared_Preference.getLOGIN_USERNAME(SlotMoveactivity.this);
        //nks
        String url = SOAP_API_Client.URL_EP1 + Api.API_MOVE_CRATE_TO_OTHER_LOCATION + techName + "&crate_id=" + CrateToMove + "&new_crate_id=" + CrateIntoMove;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success"))//means crate is shipped alone
                    {
                        // String Message = "Crate " + Crate_Name + " has been shipped alone. \n Do you want to move this crate?";
                        Toast.makeText(SlotMoveactivity.this, "Crate moved successfully!", Toast.LENGTH_LONG).show();
                        Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crate(s)?");
                    } else {
                        Toast.makeText(SlotMoveactivity.this, "Failed!", Toast.LENGTH_LONG).show();
                        Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crate(s)?");
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

                if (progressDailog.isShowing()) {

                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {


                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                Toast.makeText(SlotMoveactivity.this, "Failed! Error moving crate into crate.", Toast.LENGTH_SHORT).show();
                Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crate(s)?");
            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void GetBinName(String BinId) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(SlotMoveactivity.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }
        //nks
        String url = SOAP_API_Client.URL_EP1 + Api.API_GET_BIN_NAME + BinId;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    Location_Name = jsonObject.getString("win_loc");
                    Log.e("Location_name", Location_Name);
                } catch (Exception e) {
                    e.getMessage();
                }
                if (progressDailog.isShowing()) {

                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
                hasmap.put("Location_Name", Location_Name);
                hasmap.put("loc_id", LocationId);
                Confirm_Dialog_to_MoveCrate();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {

                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void GetAreaName(String AreaId) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(SlotMoveactivity.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }
        //nks
        String url = SOAP_API_Client.URL_EP1 + Api.API_GET_AREA_LOCATION + AreaId;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {

                    JSONArray jsonArray = obj.getJSONArray("cds");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Location_Name = jsonObject.getString("area_name");
                    String areaCode = jsonObject.getString("area_code");
                    String areaId = jsonObject.getString("a_id");

                    Log.e("Location_name", Location_Name);
                } catch (Exception e) {
                    e.getMessage();
                }
                if (progressDailog.isShowing()) {

                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
                hasmap.put("Location_Name", Location_Name);
                hasmap.put("loc_id", LocationId);
                Confirm_Dialog_to_MoveCrate();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {

                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }

            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }


    public void Check_Crates_Packed_in_this_Crate(final String CrateId, final String CrateName) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(SlotMoveactivity.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //nks

        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }

        /*http://www.exhibitpower2.com/ep1/crateslist_insidecrate_webservice.php?id=3016*/
        String url = SOAP_API_Client.URL_EP1 + Api.API_GET_INSIDE_CRATE_LIST + CrateId;
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                if (progressDailog.isShowing()) {
                        progressDailog.dismiss();
                    }
                }
                catch (Exception e) {
                    e.getMessage();
                }
                try {
                    JSONArray jsonArray = obj.getJSONArray("data");

                    JSONObject jsonObject_Element1;
                    JSONObject jsonObject_Element2;
                    JSONArray jsonArray_Element1 = new JSONArray();
                    JSONArray jsonArray_Element2 = new JSONArray();
                    JSONObject jsonObject_main = jsonArray.getJSONObject(0);
                    try {

                        jsonArray_Element1 = jsonObject_main.getJSONArray("element1");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    try {

                        jsonArray_Element2 = jsonObject_main.getJSONArray("element2");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    int totalCrates = jsonArray_Element1.length() + jsonArray_Element2.length();
                    if (totalCrates > 0) {
                        ArrayList<HashMap<String, String>> List_InsideCrates = new ArrayList<>();

                        for (int i = 0; i < jsonArray_Element1.length(); i++) {

                            JSONObject jsonObject = jsonArray_Element1.getJSONObject(i);
                            String CrateId = jsonObject.getString("id");
                            String CrateName = jsonObject.getString("crate_number");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("CrateId", CrateId);
                            hashMap.put("CrateName", CrateName);
                            List_InsideCrates.add(hashMap);
                        }

                        for (int i = 0; i < jsonArray_Element2.length(); i++) {
                            JSONObject jsonObject = jsonArray_Element2.getJSONObject(i);
                            String CrateId = jsonObject.getString("id");
                            String CrateName = jsonObject.getString("crate_number");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("CrateId", CrateId);
                            hashMap.put("CrateName", CrateName);
                            List_InsideCrates.add(hashMap);
                        }

                        Show_Dialog_for_Inside_Crates(List_InsideCrates, CrateName, CrateId);
                    } else {

                        //Add this crate to list********
                        if (cratehasmap.size() > 0) {
                            if (cratehasmap.containsKey(CrateId)) {
                                Toast.makeText(getApplicationContext(), "This crate is already added!", Toast.LENGTH_SHORT).show();
                            } else {
                                HashMap<String, String> hasmap = new HashMap<>();
                                hasmap.put("id", CrateId);
                                hasmap.put("name", CrateName);
                                cratehasmap.put(CrateId, hasmap);
                                if (id.equalsIgnoreCase("")) {
                                    id = CrateId;
                                } else {
                                    id = id + "," + CrateId;
                                }
                            }
                        } else {
                            HashMap<String, String> hasmap = new HashMap<>();
                            hasmap.put("id", CrateId);
                            hasmap.put("name", CrateName);
                            cratehasmap.put(CrateId, hasmap);
                            if (id.equalsIgnoreCase("")) {
                                id = CrateId;
                            } else {
                                id = id + "," + CrateId;
                            }
                        }

                        //*******Add this crate to list

                        Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crates?");//this is for scanning more crates to move
                    }

                } catch (Exception e) {

                        try {
                            if (progressDailog.isShowing())
                            progressDailog.dismiss();
                        } catch (Exception e1) {
                            e1.getMessage();
                        }


                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {

                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            }
        });
        AppController.getInstance().addToRequestQueue(bb);
    }

    public void Show_Dialog_for_Inside_Crates(ArrayList<HashMap<String, String>> List_InsideCrates, final String MainCrateName, final String MainCrateId) {
        final Dialog showd = new Dialog(SlotMoveactivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        Button Btn_No = (Button) showd.findViewById(R.id.Btn_No);
        Button Btn_Yes = (Button) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView txt_message = (TextView) showd.findViewById(R.id.texrtdesc);


        String InsideCrates = "";
        for (int i = 0; i < List_InsideCrates.size(); i++) {
            HashMap<String, String> hashMap = List_InsideCrates.get(i);
            InsideCrates = InsideCrates + hashMap.get("CrateName") + ",";
        }

        InsideCrates = InsideCrates.substring(0, InsideCrates.lastIndexOf(","));


        String message = "The Crate " + MainCrateName + " contains Crate(s):" + InsideCrates + ".\n" + "Do you want to move this Crate?";
        txt_message.setText(message);
        Btn_Yes.setText("Yes");
        Btn_No.setText("No");


        Btn_Yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //Add this crate to list********
                    if (cratehasmap.size() > 0) {
                        if (cratehasmap.containsKey(MainCrateId)) {
                            Toast.makeText(getApplicationContext(), "This crate is already added!", Toast.LENGTH_SHORT).show();
                        } else {
                            HashMap<String, String> hasmap = new HashMap<>();
                            hasmap.put("id", MainCrateId);
                            hasmap.put("name", MainCrateName);
                            cratehasmap.put(MainCrateId, hasmap);
                            if (id.equalsIgnoreCase("")) {
                                id = MainCrateId;
                            } else {
                                id = id + "," + MainCrateId;
                            }
                        }
                    } else {
                        HashMap<String, String> hasmap = new HashMap<>();
                        hasmap.put("id", MainCrateId);
                        hasmap.put("name", MainCrateName);
                        cratehasmap.put(MainCrateId, hasmap);
                        if (id.equalsIgnoreCase("")) {
                            id = MainCrateId;
                        } else {
                            id = id + "," + MainCrateId;
                        }
                    }

                    //*******Add this crate to list


                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crates?");//this is for scanning more crates to move


                } catch (Exception e) {

                }
            }
        });

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crates?");//this is for scanning more crates to move

            }
        });

        Btn_No.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crates?");//this is for scanning more crates to move

            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }


}
