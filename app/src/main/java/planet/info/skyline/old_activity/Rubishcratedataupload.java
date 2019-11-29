package planet.info.skyline.old_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.network.Api;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Myspinner;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_add_item_desc;
import static planet.info.skyline.network.Api.API_add_item_descwithFile;
import static planet.info.skyline.network.Api.API_send_po_and_job;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class Rubishcratedataupload extends BaseActivity {
    List<HashMap<String, String>> listad = new ArrayList<HashMap<String, String>>();
    ListView listdatal;
    // EditText item1,item2,item3,item4;
    TextView gobacktosca;

    DisplayImageOptions options;
  //  SharedPreferences sp;
 //   Editor ed;
    Uri mImageCaptureUri;
    adapterforblank adapter;
    int serverResponseCode;
    TextView clientname;
    ImageView merchantname, missing, homeacti;
    ProgressDialog pDialog;
    String path, fname = "";
    private TextView timerValue;
    File file1;
    TextView jobid, projmgt, date, salesrap, tech, configsiz, gomissi,
            itencount, uploadpict, uploadpict1;// size
    String item, desc, esimate, labourhou, linkforreport, withimgh, withoutimg;
    EditText item1, item2, item3, item4;
    EditText item5, item6, item7, item8;
    String wjobid, wjobidq, wimg = "", wuname, royaldesc, royaldescq, royalid;
    int resultmy, check_gotocatch;
    // String itemtype = "";
    Boolean second = false;
    String urlofwebservice = URL_EP2+"/WebService/techlogin_service.asmx?op=send_po_and_job",
            receivedString, pmname = "", jobname = "";
    Spinner spnr_Item_Type, spnr_Item_Type1;


    View footerLayout;
    String itemNo = "";
    ArrayList<String> list_TotalPhotoforItem1 = new ArrayList<>();
    ArrayList<String> list_TotalPhotoforItem2 = new ArrayList<>();
    ArrayList<HashMap<String, String>> list_itemDesc = new ArrayList<>();
    ArrayList<String> list_path = new ArrayList<>();
    ArrayList<String> list_UploadImageName = new ArrayList<>();
    long totalSize = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rubishcratedataupload_new);
        listdatal = (ListView) findViewById(R.id.listdatal);
        gobacktosca = (TextView) findViewById(R.id.gobacktosca);
        clientname = (TextView) findViewById(R.id.textView1);
        timerValue = (TextView) findViewById(R.id.timer);
        missing = (ImageView) findViewById(R.id.missing);
        //
        footerLayout = findViewById(R.id.upperheader2);
        item1 = (EditText) findViewById(R.id.itemdetails);
        item2 = (EditText) findViewById(R.id.itemdetails1);
        item3 = (EditText) findViewById(R.id.itemdetails2);
        item4 = (EditText) findViewById(R.id.itemdetails3);
        itencount = (TextView) findViewById(R.id.itencount);
        uploadpict = (TextView) findViewById(R.id.uploadpict);
        //nks
        spnr_Item_Type = (Spinner) findViewById(R.id.spnr_Item_Type);
        setSpinner();
        //nks
        //
        missing.setVisibility(View.VISIBLE);
        jobid = (TextView) findViewById(R.id.jobid);
        projmgt = (TextView) findViewById(R.id.projmgt);
        date = (TextView) findViewById(R.id.date);
        salesrap = (TextView) findViewById(R.id.salesrap);
        tech = (TextView) findViewById(R.id.tech);
        configsiz = (TextView) findViewById(R.id.dated);
        gomissi = (TextView) findViewById(R.id.gomissi);
        pDialog = new ProgressDialog(Rubishcratedataupload.this);
        pDialog.setMessage("Kindly wait");
        pDialog.setCancelable(false);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mBroadcastReceiver, new IntentFilter("subtask"));
      //  sp = getApplicationContext().getSharedPreferences("skyline",
                //MODE_PRIVATE);
     //   ed = sp.edit();
        insertdata();
        merchantname = (ImageView) findViewById(R.id.merchantname);
        merchantname.setImageResource(R.drawable.exhibitlogoa);
        Intent kk = getIntent();
        //   itemtype = kk.getStringExtra("type");
        //  String dshd = itemtype;
        //  String s = dshd;
      //  ed.putString("upload", "").commit();
        Shared_Preference.setUPLOAD(Rubishcratedataupload.this,"");
        Shared_Preference.setUPLOAD2(Rubishcratedataupload.this,"");
      //  ed.putString("upload2", "").commit();
        homeacti = (ImageView) findViewById(R.id.homeacti);

        homeacti.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               /* Intent ii = new Intent(Rubishcratedataupload.this,
                        Clock_Submit_Type_Activity.class);
                startActivity(ii);*/
                // finish();
                onBackPressed();
            }
        });
       // ed.putString("upload", "").commit();
        Shared_Preference.setUPLOAD(Rubishcratedataupload.this,"");
        //
        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,
        // 40);
        // // params.weight =2.0f;
        // params.gravity = Gravity.LEFT;
        //
        // merchantname.setLayoutParams(params);
        // merchantname.set
        uploadpict.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // int pos=listdatal.getPositionForView(v);
                // String aupload=list.get(pos).get("upload");
                // if(aupload.equals("")){
                // uploadpict.setText("Upload Picture");
                // //showdialod();
                // opendilogforattachfileandimage();
                // }else{
                // uploadpict.setText("View Picture");
                // }
                // // showdialod();
                //
                itemNo = "1";

                opendilogforattachfileandimage();
            }
        });


        if (new ConnectionDetector(Rubishcratedataupload.this).isConnectingToInternet()) {
            new asyntaskgetdatofpm().execute();
        } else {
            Toast.makeText(Rubishcratedataupload.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }


        itencount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                   // String sfs = sp.getString("upload", "");
                    String sfs =  Shared_Preference.getUPLOAD(Rubishcratedataupload.this);

                    String[] arr = sfs.split("/");
                    int le = arr.length;

                    String dds = "";
                    dds = arr[le - 1];
                    dds = URL_EP1+ Api.API_COLLATERAL_PATH
                            + dds;
                    if (dds == "" || dds.equals("") || dds.equalsIgnoreCase("")) {

                    } else {
                        showdialodimage(dds);
                    }
                } catch (Exception e) {

                }
            }
        });
        //
        gomissi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Toast.makeText(Rubishcratedataupload.this,item5.getText().toString(),
                // Toast.LENGTH_LONG).show();
                // Toast.makeText(Rubishcratedataupload.this,item6.getText().toString(),
                // Toast.LENGTH_LONG).show();
                // Toast.makeText(Rubishcratedataupload.this,item7.getText().toString(),
                // Toast.LENGTH_LONG).show();
                // Toast.makeText(Rubishcratedataupload.this,item8.getText().toString(),
                // Toast.LENGTH_LONG).show();

                list_itemDesc.clear();

                String item = item1.getText().toString();
                String desc = item2.getText().toString();
                String esimate = item3.getText().toString();
                String labourhour = item4.getText().toString();
                Myspinner myspinner = (Myspinner) spnr_Item_Type.getSelectedItem();
                String Item_type_val = myspinner.getspinnerVal();
                String Item_type_text = myspinner.getSpinnerText();

                if (item.length() == 0) {
                    Toast.makeText(Rubishcratedataupload.this,
                            "Please enter item description", Toast.LENGTH_LONG).show();
                    return;
                } else if (desc.length() == 0) {
                    Toast.makeText(Rubishcratedataupload.this,
                            "Please enter damage description", Toast.LENGTH_LONG)
                            .show();
                    return;
                } else if (Item_type_val.equalsIgnoreCase("0")) {
                    Toast.makeText(Rubishcratedataupload.this,
                            "Please choose item type", Toast.LENGTH_LONG)
                            .show();
                    return;
                }


                /*else if (esimate.length() == 0) {
                    Toast.makeText(Rubishcratedataupload.this,
							"please enter cost", Toast.LENGTH_LONG).show();
					return;
				} else if (labourhour.length() == 0) {
					Toast.makeText(Rubishcratedataupload.this,
							"please enter hours", Toast.LENGTH_LONG).show();
					return;
				}*/


                String descs = "Damage Report to " + Item_type_text + " " + item
                        + " : " + desc;

                HashMap<String, String> hashMap = new HashMap<String, String>();
                if (list_TotalPhotoforItem1.size() > 0) {
                    for (int i = 0; i < list_TotalPhotoforItem1.size(); i++) {
                        hashMap.put(Utility.IMAGE_PATH, list_TotalPhotoforItem1.get(i));
                        hashMap.put(Utility.ITEM_DESC, descs);
                        list_itemDesc.add(hashMap);//nks
                    }
                } else {
                    hashMap.put(Utility.IMAGE_PATH, "");
                    hashMap.put(Utility.ITEM_DESC, descs);
                    list_itemDesc.add(hashMap);//nks
                }


                desc = descs;
                String ss = "";// =sp.getString("", "");
                String fr = "";
                try {
                //    String sfs = sp.getString("upload", "");
                    String sfs =  Shared_Preference.getUPLOAD(Rubishcratedataupload.this);
                    String ds = Shared_Preference.getCrate_ID(Rubishcratedataupload.this);

                    fr = ds;
                    String sfsq = sfs;
                    if (sfsq.startsWith(",")) {
                        sfsq = sfsq.substring(1, sfsq.length());
                    } else {

                    }
                    String[] arr = null;//=sfsq.split(",");
                    if (sfsq.contains(",")) {
                        arr = sfsq.split(",");
                    } else {
                    }
                    int lens = 0;//arr.length;
                    try {
                        lens = arr.length;
                        if (sfsq.contains(",")) {

                        } else {

                        }
                        if (lens > 0) {
                            String[] tot = sfsq.split(",");
                            for (int i = 0; i < lens; i++) {
                                String imageurl = tot[i];
//							String[] arrq = sfsq.split("/");
                                String[] arrq = imageurl.split("/");
                                int leq = arrq.length;

                                String ddsq = "";
                                ddsq = arrq[leq - 1];
                                ddsq = URL_EP1+Api.API_COLLATERAL_PATH
                                        + ddsq;

                                wimg = wimg + "," + ddsq;
                                if (ddsq == "" || ddsq.equals("")
                                        || ddsq.equalsIgnoreCase("")) {
                                    ss = ddsq;
                                } else {
                                    ss = ddsq;
                                }
                            }
                        } else {
                            String[] arrq = sfsq.split("/");
                            int leq = arrq.length;

                            String ddsq = "";
                            ddsq = arrq[leq - 1];
                            ddsq = URL_EP1+Api.API_COLLATERAL_PATH
                                    + ddsq;

                            wimg = ddsq;
                            if (ddsq == "" || ddsq.equals("")
                                    || ddsq.equalsIgnoreCase("")) {
                                ss = ddsq;
                            } else {
                                ss = ddsq;
                            }
                        }
                    } catch (Exception e) {

                    }
                /*	String[] arr = sfs.split("/");
                    int le = arr.length;

					String dds = "";
					dds = arr[le - 1];
					dds = "http://exhibitpower.com/admin/uploads/collateral/"
							+ dds;

					wimg = dds;
					if (dds == "" || dds.equals("") || dds.equalsIgnoreCase("")) {
						ss = dds;
					} else {
						ss = dds;
					}*/
                    if (wimg.startsWith(",")) {
                        wimg = wimg.substring(1, wimg.length());
                    }
                } catch (Exception e) {
                    ss = "real";
                }
                showprogressdialog();
                linkforreport = URL_EP1+Api.API_DEFECT_CRATE
                        + fr
                        + "&item="
                        + item
                        + "&mcost="
                        + esimate
                        + "&descr="
                        + desc
                        + "&labour="
                        + labourhour
                        + "&img="
                        + wimg;
                String dsd = linkforreport;
                String sdms = dsd;
              //  wjobid = sp.getString("jobid", "s");
                wjobid =   Shared_Preference.getSWO_ID(Rubishcratedataupload.this);
                //wuname = sp.getString("tname", "");
                wuname =   Shared_Preference.getLOGIN_USERNAME(Rubishcratedataupload.this);
             //   String clientidme = sp.getString("clientid", "");
                String clientidme= Shared_Preference.getLOGIN_USER_ID(Rubishcratedataupload.this);
                withoutimg = URL_EP2+"/Register/change_order.aspx?swo_id=2100&emp_id=28&desc=ddddhdhdhdg";
                withoutimg = withoutimg.replace(" ", "%20");
                String dsk = withoutimg;
                String kd = dsk;

                //		getjsonobjectwithoutimage();
                royalid = clientidme;
                royaldesc = desc;
                if (ss == "" || ss.equals("") || ss.equalsIgnoreCase("")
                        || ss.equalsIgnoreCase("real")) {

                } else {
                    withimgh = URL_EP2+"/Register/s_Work_Orderch.aspx?id="
                            + wjobid
                            + "&name="
                            + wuname
                            + "&desc="
                            + desc
                            + "&url=" + wimg;
                    withimgh = withimgh.replace(" ", "%20");
                    withimgh = URL_EP2+"/Register/s_Work_Orderch.aspx?id="
                            + wjobid
                            + "&url="
                            + wimg
                            + "&name="
                            + wuname
                            + "&desc=" + desc;
                    withimgh = withimgh.replace(" ", "%20");
                    String sdj = withimgh;
                    String sgs = sdj;

                    getjsonobjectwithmage();
                }
                // new async_login().execute();
                if (second) {
                    //

                    // Toast.makeText(Rubishcratedataupload.this,item5.getText().toString(),
                    // Toast.LENGTH_LONG).show();
                    // Toast.makeText(Rubishcratedataupload.this,item6.getText().toString(),
                    // Toast.LENGTH_LONG).show();
                    // Toast.makeText(Rubishcratedataupload.this,item7.getText().toString(),
                    // Toast.LENGTH_LONG).show();
                    // Toast.makeText(Rubishcratedataupload.this,item8.getText().toString(),
                    // Toast.LENGTH_LONG).show();

                    String itemq = item5.getText().toString();
                    String descq = item6.getText().toString();
                    String esimateq = item7.getText().toString();
                    String labourhourq = item8.getText().toString();


                    Myspinner myspinner1 = (Myspinner) spnr_Item_Type1.getSelectedItem();
                    String Item_type_val1 = myspinner1.getspinnerVal();
                    String Item_type_text1 = myspinner1.getSpinnerText();


                    if (itemq.length() == 0) {
                        Toast.makeText(Rubishcratedataupload.this,
                                "Please enter item description", Toast.LENGTH_LONG).show();
                        return;
                    } else if (descq.length() == 0) {
                        Toast.makeText(Rubishcratedataupload.this,
                                "Please enter damage description", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else if (Item_type_val1.equalsIgnoreCase("0")) {
                        Toast.makeText(Rubishcratedataupload.this,
                                "Please choose item type", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }


                    /* else if (esimateq.length() == 0) {
                        Toast.makeText(Rubishcratedataupload.this,
								"please enter cost", Toast.LENGTH_LONG).show();
						return;
					} else if (labourhourq.length() == 0) {
						Toast.makeText(Rubishcratedataupload.this,
								"please enter hours", Toast.LENGTH_LONG).show();
						return;
					}*/
                    String descsq = "Damage Report to " + Item_type_text1 + " "
                            + itemq + " : " + descq;


                    HashMap<String, String> hashMap1 = new HashMap<String, String>();
                    if (list_TotalPhotoforItem2.size() > 0) {
                        for (int i = 0; i < list_TotalPhotoforItem2.size(); i++) {
                            hashMap1.put(Utility.IMAGE_PATH, list_TotalPhotoforItem2.get(i));
                            hashMap1.put(Utility.ITEM_DESC, descsq);
                            list_itemDesc.add(hashMap1);//nks
                        }
                    } else {
                        hashMap1.put(Utility.IMAGE_PATH, "");
                        hashMap1.put(Utility.ITEM_DESC, descsq);
                        list_itemDesc.add(hashMap1);//nks
                    }


                    descq = descsq;
                    String ssq = "";// =sp.getString("", "");
                    String frq = "";
                    try {
                      //  String sfsq = sp.getString("upload2", "");
                        String sfsq =   Shared_Preference.getUPLOAD2(Rubishcratedataupload.this);
                        String dsq =Shared_Preference.getCrate_ID(Rubishcratedataupload.this);
                        frq = dsq;
                        //
                        if (sfsq.startsWith(",")) {
                            sfsq = sfsq.substring(1, sfsq.length());
                        } else {

                        }
                        String[] arr = null;//=sfsq.split(",");
                        if (sfsq.contains(",")) {
                            arr = sfsq.split(",");
                        } else {
                        }
                        int lens = 0;//arr.length;
                        try {
                            lens = arr.length;
                            if (lens > 0) {
                                for (int i = 0; i < lens; i++) {
                                    String[] arrq = sfsq.split("/");
                                    int leq = arrq.length;

                                    String ddsq = "";
                                    ddsq = arrq[leq - 1];
                                    ddsq = URL_EP1+Api.API_COLLATERAL_PATH
                                            + ddsq;

                                    wimg = wimg + "," + ddsq;
                                    if (ddsq == "" || ddsq.equals("")
                                            || ddsq.equalsIgnoreCase("")) {
                                        ssq = ddsq;
                                    } else {
                                        ssq = ddsq;
                                    }
                                }
                            } else {
                                String[] arrq = sfsq.split("/");
                                int leq = arrq.length;

                                String ddsq = "";
                                ddsq = arrq[leq - 1];
                                ddsq = URL_EP1+Api.API_COLLATERAL_PATH
                                        + ddsq;

                                wimg = ddsq;
                                if (ddsq == "" || ddsq.equals("")
                                        || ddsq.equalsIgnoreCase("")) {
                                    ssq = ddsq;
                                } else {
                                    ssq = ddsq;
                                }
                            }
                        } catch (Exception e) {

                        }
                        if (wimg.startsWith(",")) {
                            wimg = wimg.substring(1, wimg.length());
                        }
                        /*String[] arrq = sfsq.split("/");
                        int leq = arrq.length;

						String ddsq = "";
						ddsq = arrq[leq - 1];
						ddsq = "http://exhibitpower.com/admin/uploads/collateral/"
								+ ddsq;

						wimg = ddsq;
						if (ddsq == "" || ddsq.equals("")
								|| ddsq.equalsIgnoreCase("")) {
							ssq = ddsq;
						} else {
							ssq = ddsq;
						}*/
                    } catch (Exception e) {
                        ssq = "real";
                    }
                    showprogressdialog();
    /*				linkforreport = "http://exhibitpower.com/defect_crate_webservice.php?id="
							+ frq
							+ "&item="
							+ itemq
							+ "&mcost="
							+ esimateq
							+ "&descr="
							+ descq
							+ "&labour="
							+ labourhourq
							+ "&img=" + ssq;*/
                    linkforreport = URL_EP1+Api.API_DEFECT_CRATE
                            + frq
                            + "&item="
                            + itemq
                            + "&mcost="
                            + esimateq
                            + "&descr="
                            + descq
                            + "&labour="
                            + labourhourq
                            + "&img=" + wimg;
                    String dsdq = linkforreport;
                    String sdmsq = dsdq;
                  //  wjobidq = sp.getString("jobid", "s");
                    wjobid =   Shared_Preference.getSWO_ID(Rubishcratedataupload.this);
                   // wuname = sp.getString("tname", "");

                    wuname =  Shared_Preference.getLOGIN_USERNAME(Rubishcratedataupload.this);
                  //  String clientidmeq = sp.getString("clientid", "");
                    withoutimg = URL_EP2+"/Register/change_order.aspx?swo_id=2100&emp_id=28&desc=ddddhdhdhdg";
                    withoutimg = withoutimg.replace(" ", "%20");
                    String dskq = withoutimg;
                    String kdq = dskq;

                    // getjsonobjectwithoutimage();
                    royalid = clientidme;
                    royaldescq = descq;
                    if (ss == "" || ss.equals("") || ss.equalsIgnoreCase("")
                            || ss.equalsIgnoreCase("real")) {

                    } else {
                        withimgh = URL_EP2+"/Register/s_Work_Orderch.aspx?id="
                                + wjobid
                                + "&name="
                                + wuname
                                + "&desc="
                                + desc
                                + "&url=" + wimg;
                        withimgh = withimgh.replace(" ", "%20");
                        withimgh = URL_EP2+"/Register/s_Work_Orderch.aspx?id="
                                + wjobid
                                + "&url="
                                + wimg
                                + "&name="
                                + wuname
                                + "&desc=" + descq;
                        withimgh = withimgh.replace(" ", "%20");
                        String sdj = withimgh;
                        String sgs = sdj;

                        // getjsonobjectwithmage();
                    }
                    //  new asyntask1().execute();

                    //
                }

                new asyntask().execute();


            }
        });
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.skylinelogopng)

                .showImageForEmptyUri(R.drawable.skylinelogopng)
                .showImageOnFail(R.drawable.skylinelogopng).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

     //   String nam = sp.getString("name", "");
        String nam = Shared_Preference.getCLIENT_NAME(this);
      //  String imageloc = sp.getString("imglo", "");
        String imageloc =	Shared_Preference.getCLIENT_IMAGE_LOGO_URL(this);
        if (imageloc.equals("") || imageloc.equalsIgnoreCase("")) {
           // ed.putString("imglo", "").commit();
            Shared_Preference.setCLIENT_IMAGE_LOGO_URL(this,"");
            missing.setVisibility(View.GONE);
            clientname.setText(nam);
        } else {
            // imageLoadery.displayImage(imageloc, merchantname, options);
            imageLoadery.displayImage(imageloc, missing, options);
        }


        // getlist();
        getlist();
        String[] from = {"value", "value"};
        int[] to = {R.id.itemdetails, R.id.itemdetails};

		/*
		 * adapter=new
		 * adapterforblank(Rubishcratedataupload.this,listad,R.layout
		 * .customlistforuploadrubishdata,from,to); //
		 * listdatal.setAdapter(adapter); listdatal.setAdapter(adapter);
		 * adapter.notifyDataSetChanged(); working
		 */

        gobacktosca.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (footerLayout.getVisibility() == View.VISIBLE) {
                    // footerLayout.setVisibility(View.VISIBLE);
                } else {

                    Myspinner myspinner = (Myspinner) spnr_Item_Type.getSelectedItem();
                    String ItemType_val = myspinner.getspinnerVal();
                    //  myspinner.getSpinnerText();

                    if (item1.getText().toString().length() == 0
                            || item2.getText().toString().length() == 0
                            || ItemType_val.equalsIgnoreCase("0")
                            ) {

                        Toast.makeText(Rubishcratedataupload.this, "Please enter item detail first !", Toast.LENGTH_SHORT).show();

                    } else {
                        second = true;
                        footerLayout.setVisibility(View.VISIBLE);
                        item5 = (EditText) footerLayout
                                .findViewById(R.id.itemdetails); // footerLayout.findViewById(R.id.footer_text);
                        item6 = (EditText) footerLayout
                                .findViewById(R.id.itemdetails1);
                        item7 = (EditText) footerLayout
                                .findViewById(R.id.itemdetails2);
                        item8 = (EditText) footerLayout
                                .findViewById(R.id.itemdetails3);
                        uploadpict1 = (TextView) footerLayout
                                .findViewById(R.id.uploadpict);
                        //
                        spnr_Item_Type1 = (Spinner) footerLayout
                                .findViewById(R.id.spnr_Item_Type);
                        setSpinner();

                        //
                        TextView item = (TextView) footerLayout
                                .findViewById(R.id.itencount);
                        item.setText("Item 2");
                        uploadpict1.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // int pos=listdatal.getPositionForView(v);
                                // String aupload=list.get(pos).get("upload");
                                // if(aupload.equals("")){
                                // uploadpict.setText("Upload Picture");
                                // //showdialod();
                                // opendilogforattachfileandimage();
                                // }else{
                                // uploadpict.setText("View Picture");
                                // }
                                // // showdialod();
                                //
                                itemNo = "2";
                                opendilogforattachfileandimage();
                            }
                        });
                    }
                }
                // TODO Auto-generated method stub
				/*
				 * getlist(); String[] from = {"value","value"}; int[] to =
				 * {R.id.itemdetails,R.id.itemdetails};
				 * 
				 * adapterforblank adapter=new
				 * adapterforblank(Rubishcratedataupload
				 * .this,listad,R.layout.customlistforuploadrubishdata,from,to);
				 * // listdatal.setAdapter(adapter);
				 * listdatal.setAdapter(adapter);
				 * adapter.notifyDataSetChanged();
				 * jjjjkkk.getListViewsize(listdatal
				 * ,Rubishcratedataupload.this);
				 */
                // adapter.no
            }
        });
        // listdatal.setAdapter(adapter)
    }

    public void getlist() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("value", " ");
        map.put("upload", "");
        listad.add(map);
    }

    public class adapterforblank extends SimpleAdapter {
        Activity act;
        private LayoutInflater layoutinflater = null;
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        public adapterforblank(Activity context,
                               List<HashMap<String, String>> data, int resource,
                               String[] from, int[] to) {

            super(context, data, resource, from, to);
            act = context;
            this.list = data;
            layoutinflater = (LayoutInflater) act
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View vis = convertView;
            if (convertView == null)
                vis = layoutinflater.inflate(
                        R.layout.customlistforuploadrubishdata, null);
            // EditText item1,item2,item3,item4;
            TextView itencount;
            final TextView uploadpict;
            item1 = (EditText) vis.findViewById(R.id.itemdetails);
            item2 = (EditText) vis.findViewById(R.id.itemdetails1);
            item3 = (EditText) vis.findViewById(R.id.itemdetails2);
            item4 = (EditText) vis.findViewById(R.id.itemdetails3);
            itencount = (TextView) vis.findViewById(R.id.itencount);
            uploadpict = (TextView) vis.findViewById(R.id.uploadpict);
            item1.setText(list.get(position).get("value"));
            item2.setText(list.get(position).get("value"));
            item3.setText(list.get(position).get("value"));
            item4.setText(list.get(position).get("value"));
            String upload = "";
            upload = list.get(position).get("upload");
            if (upload.equals("")) {
                uploadpict.setText("Upload Picture");
            } else {
                uploadpict.setText("View Picture");
            }
            item = item1.getText().toString();
            desc = item2.getText().toString();
            esimate = item3.getText().toString();
            labourhou = item4.getText().toString();
            // btn_FinishWork.setOnClickListener(new OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            // Toast.makeText(getApplicationContext(), "bffdghgfhgh",
            // 5000).show();
            // showprogressdialog();
            //
            // // TODO Auto-generated method stub
            // // String item=item1.getText().toString();
            // // String desc=item2.getText().toString();
            // // String esimate=item3.getText().toString();
            // // String labourhour=item4.getText().toString();
            // String ss="";//=sp.getString("", "");
            // String fr="";
            // try{
            // String sfs=sp.getString("upload", "");
            // String ds=sp.getString("catidnew", "");
            // fr=ds;
            // String[] arr=sfs.split("/");
            // int le=arr.length;
            //
            // String dds="";
            // dds=arr[le-1];
            // // dds="http://exhibitpower.com/admin/uploads/collateral/"+dds;
            // if(dds==""||dds.equals("")||dds.equalsIgnoreCase("")){
            // ss=dds;
            // }
            // else{
            // // showdialodimage(dds);
            // }
            // }catch(Exception e){
            //
            // }
            // //205.204.76.234/defect_crate_webservice.php?id=12&item=fg&mcost=fgfgf&descr=ffsf&labour=aa&img=aaa
            // linkforreport="http://exhibitpower.com/defect_crate_webservice.php?id="+fr+"&item="+item+"&mcost="+esimate+"&descr="+desc+"&labour="+labourhou+"&img="+ss;
            // String dsd= linkforreport;
            // //
            // linkforreport="http://exhibitpower.com/defect_crate_webservice.php?id=341&item=hello&mcost=hello&descr=hello&labour=hello&img=hello";
            // String sdms=dsd;
            // getoldloc(" ");
            // }
            // });

            // btn_FinishWork.setOnClickListener(new OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            // showprogressdialog();
            // // TODO Auto-generated method stub
            // // String item=item1.getText().toString();
            // // String desc=item2.getText().toString();
            // // String esimate=item3.getText().toString();
            // // String labourhour=item4.getText().toString();
            // String ss="";//=sp.getString("", "");
            // String fr="";
            // try{
            // String sfs=sp.getString("upload", "");
            // String ds=sp.getString("catidnew", "");
            // fr=ds;
            // String[] arr=sfs.split("/");
            // int le=arr.length;
            //
            // String dds="";
            // dds=arr[le-1];
            // // dds="http://exhibitpower.com/admin/uploads/collateral/"+dds;
            // if(dds==""||dds.equals("")||dds.equalsIgnoreCase("")){
            // ss=dds;
            // }
            // else{
            // // showdialodimage(dds);
            // }
            // }catch(Exception e){
            //
            // }
            // //205.204.76.234/defect_crate_webservice.php?id=12&item=fg&mcost=fgfgf&descr=ffsf&labour=aa&img=aaa
            // linkforreport="205.204.76.234/defect_crate_webservice.php?id="+fr+"&item="+item+"&mcost="+esimate+"&descr="+desc+"&labour="+labourhou+"&img="+ss;
            // getoldloc(" ");
            // }
            // });
            itencount.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                       // String sfs = sp.getString("upload", "");
                        String sfs =  Shared_Preference.getUPLOAD(Rubishcratedataupload.this);
                        String[] arr = sfs.split("/");
                        int le = arr.length;

                        String dds = "";
                        dds = arr[le - 1];
                        dds = URL_EP1+Api.API_COLLATERAL_PATH
                                + dds;
                        if (dds == "" || dds.equals("")
                                || dds.equalsIgnoreCase("")) {

                        } else {
                            showdialodimage(dds);
                        }
                    } catch (Exception e) {

                    }
                }
            });
            uploadpict.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    itemNo = "1";
                    int pos = listdatal.getPositionForView(v);
                    String aupload = list.get(pos).get("upload");
                    if (aupload.equals("")) {
                        uploadpict.setText("Upload Picture");
                        // showdialod();
                        opendilogforattachfileandimage();
                    } else {
                        uploadpict.setText("View Picture");
                    }
                    // showdialod();
                }
            });
            return super.getView(position, convertView, parent);
        }

    }

    public void opendilogforattachfileandimage() {
        final Dialog dialog = new Dialog(Rubishcratedataupload.this);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.openattachmentdilog_new);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //	dialog.setTitle("Attach");
        LinearLayout cameralayout = (LinearLayout) dialog
                .findViewById(R.id.cameralayout);
        LinearLayout gallarylayout = (LinearLayout) dialog
                .findViewById(R.id.gallarylayout);
        LinearLayout filelayout = (LinearLayout) dialog
                .findViewById(R.id.filelayout);
        ImageView crosse = (ImageView) dialog
                .findViewById(R.id.close);

        crosse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });
        cameralayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*
				 * dialog.dismiss(); Intent intent = new
				 * Intent(MediaStore.ACTION_IMAGE_CAPTURE); file1 = new
				 * File(Environment.getExternalStorageDirectory(),
				 * String.valueOf(System.currentTimeMillis()) +
				 * "_FromCamera.jpg"); Uri mImageCaptureUri =
				 * Uri.fromFile(file1); try {
				 * intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				 * mImageCaptureUri); intent.putExtra("return-data", true);
				 * 
				 * startActivityForResult(intent, 1);
				 * 
				 * } catch (Exception e) { e.printStackTrace(); }
				 */

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file1 = new File(Environment.getExternalStorageDirectory(),"FromCamera.jpg");
                Uri mImageCaptureUri = Uri.fromFile(file1);
                try {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            mImageCaptureUri);
                    intent.putExtra("return-data", true);

                    intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10545424L);
                    startActivityForResult(intent,
                            AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    dialog.dismiss();
                    // attachpicandfilesubtask.setImageResource(R.drawable.attachafter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        gallarylayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //
                // Intent photoIntent = new
                // Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // // Intent photoIntent = new Intent(Intent.ACTION_SEND);
                // // photoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,
                // 10545424L);
                // // photoIntent.setAction(Intent.ACTION_PICK);
                // ///
                // photoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // //photoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // startActivityForResult(photoIntent,
                // AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
                // dialog.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= 19) {// Build.VERSION_CODES.KITKAT){

                    // intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    // intent.addCategory(Intent.CATEGORY_OPENABLE);
                    Intent aa = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(
                            Intent.createChooser(aa, "Complete action using"),
                            AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
                } else {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    // intent.setAction(Intent.ACTION_OPEN_DOCUMENT);

                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"),
                            AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
                }
                dialog.dismiss();
                // startActivityForResult(Intent.createChooser(intent,
                // "Complete action using"), Constant.PICK_FROM_FILE);

            }
        });
        filelayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setType("file/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Complete action using"),
                        4);

            }
        });

        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                // finish();
            }
        });
    }

    public void showdialod() {
        final CharSequence[] intem = {"Take Photo", "Gallery"};
        AlertDialog.Builder buil = new AlertDialog.Builder(
                Rubishcratedataupload.this);
        buil.setTitle("add photo");
        buil.setItems(intem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int ii) {
                // TODO Auto-generated method stub
                if (intem[ii].equals("Take Photo")) {
                    dialog.dismiss();
					/*
					 * Intent intent=new
					 * Intent(MediaStore.ACTION_IMAGE_CAPTURE); File ff=new
					 * File(
					 * Environment.getExternalStorageDirectory(),String.valueOf
					 * (System.currentTimeMillis()) + "_FromCamera.jpg"); //
					 * intent.putExtra(MediaStore.EXTRA_OUTPUT,
					 * Uri.fromFile(ff)); Uri mImageCaptureUri =
					 * Uri.fromFile(ff); try {
					 * intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					 * mImageCaptureUri); intent.putExtra("return-data", true);
					 * startActivityForResult(intent, 1);
					 * 
					 * } catch (Exception e) { e.printStackTrace(); }
					 */
                    Intent intent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    file1 = new File(Environment.getExternalStorageDirectory(),"FromCamera.jpg");
                    // if (!file1.exists())
                    // {
                    // file1.mkdir();
                    // }
                    //

                    //
                    ////intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10545424L);
                    Uri mImageCaptureUri = Uri.fromFile(file1);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    intent.putExtra("return-data", true);
                    try {
                        // intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        // mImageCaptureUri);
                        // intent.putExtra("return-data", true);
                        // Bundle kdjs=new Bundle();
                        // if (mImageCaptureUri != null) {
                        // kdjs.putParcelable(MediaStore.EXTRA_OUTPUT,
                        // mImageCaptureUri);
                        // } else {
                        // kdjs.putBoolean("return-data", true);
                        // }
                        // intent.addCategory()
                        startActivityForResult(intent, 1);
                        // attachpicandfilesubtask.setImageResource(R.drawable.attachafter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
					/*
					 * Intent intent = new
					 * Intent(MediaStore.ACTION_IMAGE_CAPTURE); file1 = new
					 * File(Environment.getExternalStorageDirectory(),
					 * String.valueOf(System.currentTimeMillis()) +
					 * "_FromCamera.jpg"); Uri mImageCaptureUri =
					 * Uri.fromFile(file1); try {
					 * intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					 * mImageCaptureUri); intent.putExtra("return-data", true);
					 * startActivityForResult(intent,
					 * Constant.PICK_FROM_CAMERA);
					 * attachpicandfilesubtask.setImageResource
					 * (R.drawable.attachafter);
					 */
                    // startActivityForResult(intent, 1);
                } else {
                    Intent photoIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
//					photoIntent
//							.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10545424L);


                    file1 = new File(Environment.getExternalStorageDirectory(),"FromCamera.jpg");

                    //
                    Uri mImageCaptureUri = Uri.fromFile(file1);
                    photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    photoIntent.putExtra("return-data", true);


                    //
                    startActivityForResult(photoIntent,
                            AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
					/*
					 * Intent intent = new Intent(); intent.setType("image/*");
					 * if
					 * (Build.VERSION.SDK_INT>=19){//Build.VERSION_CODES.KITKAT
					 * ){
					 * 
					 * // intent.setAction(Intent.ACTION_OPEN_DOCUMENT); //
					 * intent.addCategory(Intent.CATEGORY_OPENABLE); Intent
					 * aa=new Intent(Intent.ACTION_PICK,
					 * android.provider.MediaStore
					 * .Images.Media.EXTERNAL_CONTENT_URI);
					 * startActivityForResult(Intent.createChooser(aa,
					 * "Complete action using"),2); }else{
					 * intent.setAction(Intent.ACTION_GET_CONTENT);
					 * //intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
					 * startActivityForResult(Intent.createChooser(intent,
					 * "Complete action using"), 2); }
					 * //startActivityForResult(Intent.createChooser(intent,
					 * "Complete action using"), Constant.PICK_FROM_FILE);
					 * //attachpicandfilesubtask
					 * .setImageResource(R.drawable.attachafter);
					 */
                }

            }
        });
        buil.show();
    }

    public void showdialodimage(String dd) {
        final Dialog dialog = new Dialog(Rubishcratedataupload.this);
        dialog.setContentView(R.layout.alerttoshowpreview);
        // dialog.getWindow().setBackgroundDrawable(new
        // ColorDrawable(Color.parseColor("#1c3959")));

        ImageView preview = (ImageView) dialog.findViewById(R.id.showimage);
        preview.setImageResource(R.drawable.exhibitlogoa);
        imageLoadery.displayImage(dd, preview, options);

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // onResume();


            if (requestCode == AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    mImageCaptureUri = data.getData();
                    try {
                        path = getPath(mImageCaptureUri,
                                Rubishcratedataupload.this); // from Gallery
                        list_path.add(path);
                        // upload();
                        // uploadFile(path);
                        // new asyntaskupload().execute();
                        workornot();
                    } catch (Exception e) {
                        try {
                            path = mImageCaptureUri.getPath();
                            list_path.add(path);
                            // upload();
                            //   new asyntaskupload().execute();
                            workornot();
                            // uploadFile(path);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                     //   Log.i("check image attach or not", e.toString());
                    }
                } else {
                    Uri mImageCaptureUri = Uri.fromFile(file1);
                    try {
                        path = getPath(mImageCaptureUri,
                                Rubishcratedataupload.this); // from Gallery
                        list_path.add(path);
                    } catch (Exception e) {
                        path = mImageCaptureUri.getPath();
                        list_path.add(path);
                    //    Log.i("check image attach or not", e.toString());
                    }
                    String arr[] = path.split("/");
                    int i;
                }
            }
            //

            if (requestCode == AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    mImageCaptureUri = data.getData();
                    try {
                        path = getPath(mImageCaptureUri,
                                Rubishcratedataupload.this); // from Gallery
                        list_path.add(path);
                        // upload();
                        // uploadFile(path);
                        // new asyntaskupload().execute();

                        workornot();

                    } catch (Exception e) {
                        try {
                            path = mImageCaptureUri.getPath();
                            list_path.add(path);
                            // upload();
                            // new asyntaskupload().execute();

                            workornot();
                            // uploadFile(path);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        //Log.i("check image attach or not", e.toString());
                    }
                } else {
                    Uri mImageCaptureUri = Uri.fromFile(file1);
                    try {
                        path = getPath(mImageCaptureUri,
                                Rubishcratedataupload.this); // from Gallery
                        list_path.add(path);
                    } catch (Exception e) {
                        path = mImageCaptureUri.getPath();
                        list_path.add(path);
                      //  Log.i("check image attach or not", e.toString());
                    }
                    //     new asyntaskupload().execute();

                    workornot();

                    String arr[] = path.split("/");
                    int i;
                }
                // Intent responseIntent=data;
                // BitmapFactory.Options options = new BitmapFactory.Options();
                // // downsizing image as it throws OutOfMemory Exception for
                // larger
                // // images
                // options.inSampleSize = 8;
                // Uri vid = data.getData();
                // Toast.makeText(getApplicationContext(),
                // vid.toString(),4000).show();
                // String imagePath = getPath(vid,Rubishcratedataupload.this);
                //
                // Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);

                // imgPrev.setImageBitmap(bitmap);
                // new UploadAsynk().execute(data);
                // mMessageList.setVisibility(View.GONE);
                // img_prev_rl.setVisibility(View.VISIBLE);

            }
            //
        } else {
            itemNo = "";
        }

    }

    public void showprogressdialog() {
        try {
            if (!pDialog.isShowing()) {
                pDialog.show();
            }
        } catch (Exception e) {
        }
    }

    public void hideprogressdialog() {
        try {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    public class asyntaskupload extends AsyncTask<Void, Integer, Void> {
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //    uploadFile(path);

            list_TotalPhotoforItem1.clear();
            list_TotalPhotoforItem2.clear();
            list_UploadImageName.clear();
            ////////////////////////////////////////////////////////////

            int statusCode = 0;
            totalSize = 0;
            try {
                //String FILE_UPLOAD_URL = "http://mobileappupload.businesstowork.com/api/fileupload";
             //   String jid = sp.getString(Utility.JOB_ID_BILLABLE, "");
                String jid = Shared_Preference.getJOB_NAME_BILLABLE(Rubishcratedataupload.this);
                String FILE_UPLOAD_URL = URL_EP2+"/UploadFileHandler.ashx?jid=" + jid;
                FILE_UPLOAD_URL = FILE_UPLOAD_URL.replaceAll(Pattern.quote(" "), "%20");//nks
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(FILE_UPLOAD_URL);

                CustomMultiPartEntity entity = new CustomMultiPartEntity(new CustomMultiPartEntity.ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        int progress = (int) ((num / (float) totalSize) * 100);
                        publishProgress(progress);


                    }
                });

                //  MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                for (int i = 0; i < list_path.size(); i++) {
                    try {
                        String path = list_path.get(i);

                        File sourceFile_1 = new File(path);
                        entity.addPart("image" + i, new FileBody(sourceFile_1));
                        // entity.addPart("jid", new StringBody(jid));
                        long Size = entity.getContentLength();
                        totalSize = totalSize + Size;
                        // list_imageSize.add(String.valueOf(Size / 1000));

                    } catch (Exception e) {
                        e.getMessage();
                    }

                }


                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {

                    String resultdata = statusCode + "";
                    String responseStr = EntityUtils.toString(response.getEntity());
                    //  String responseStrx= EntityUtils.toString(response.getEntity());

                    String s[] = responseStr.split(",");
                    List<String> stringList = new ArrayList<String>(Arrays.asList(s)); //new Ar
                    list_UploadImageName = new ArrayList<String>(stringList);

                    ///
                    if (itemNo.equalsIgnoreCase("1")) {
                        list_TotalPhotoforItem1.addAll(list_UploadImageName);
                    } else if (itemNo.equalsIgnoreCase("2")) {
                        list_TotalPhotoforItem2.addAll(list_UploadImageName);
                    }
                    itemNo = "";
                    ///

                } else {
                    String resultdata = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (Exception e) {
                e.getMessage();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            //   super.onProgressUpdate(progress[0]);

            progressDialog.setProgress(progress[0]);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressDialog = new ProgressDialog(Rubishcratedataupload.this);
            progressDialog.setMessage("Uploading , please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            progressDialog.dismiss();

            list_path.clear();
            if (serverResponseCode == 200) {

                // HashMap<String, String> map=new HashMap<String, String>();
                // map.put("value", " ");
                // map.put("upload", "");
                // listad.add(map);
                // adapter.notifyDataSetChanged();
            }
            // hideprogressdialog();
            super.onPostExecute(result);
        }

    }

    public class asyntaskgetdatofpm extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            findpmdata();
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            showprogressdialog();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            if (serverResponseCode == 200) {
                projmgt.setText("Project Mgmt:" + "\n" + pmname);
                jobid.setText("Job #:" + "\n" + jobname);

                // HashMap<String, String> map=new HashMap<String, String>();
                // map.put("value", " ");
                // map.put("upload", "");
                // listad.add(map);
                // adapter.notifyDataSetChanged();
            }
            hideprogressdialog();
            super.onPostExecute(result);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rubishcratedataupload, menu);
        return true;
    }

    public int uploadFile(String sourceFileUri) {

        String fileName = sourceFileUri;
        fname = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            // dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :");

            runOnUiThread(new Runnable() {
                public void run() {
                    // messageText.setText("Source File not exist :"+
                    // imagepath);
                }
            });

            return 0;

        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(
                        sourceFile);
                URL url = new URL(URL_EP1+"/UploadToServer.php");
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    + " F:/wamp/wamp/www/uploads";
                            // messageText.setText(msg);
                            Toast.makeText(Rubishcratedataupload.this,
                                    "File Uploaded Successfully",
                                    Toast.LENGTH_SHORT).show();
                            if (second) {
                               // String upload2 = sp.getString("upload2", "");
                                String upload2 =   Shared_Preference.getUPLOAD2(Rubishcratedataupload.this);
                                String name = upload2 + "," + fname;
                             //   ed.putString("upload2", name).commit();
                                Shared_Preference.setUPLOAD2(Rubishcratedataupload.this,name);
                                //ed.putString("upload2", fname).commit();
                            } else {
                              //  String upload2 = sp.getString("upload", "");
                                String upload2 =  Shared_Preference.getUPLOAD(Rubishcratedataupload.this);
                                String name = upload2 + "," + fname;
                               // ed.putString("upload", name).commit();
                                Shared_Preference.setUPLOAD(Rubishcratedataupload.this,name);
                                //ed.putString("upload", fname).commit();
                            }
                            // ed.putString("upload", fname).commit();
                        }
                    });
                }

                // close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                // dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(Rubishcratedataupload.this,
                                "MalformedURLException", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                Log.e("Upload file to server", "api_error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                // dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(Rubishcratedataupload.this,
                                "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
               /* Log.e("Upload file to server Exception",
                        "Exception : " + e.getMessage(), e);*/
            }
            // dialog.dismiss();
            return serverResponseCode;

        } // End else block

    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Toast.makeText(Subtaskpage.this, "sub task added", 5000).show();
            // adapter1.notifyDataSetChanged();
            String taskida = intent.getStringExtra("value");
            String type = intent.getStringExtra("type");// /type
            timerValue.setText(taskida);
            // if(type.equalsIgnoreCase("subtask")){
            // if(taskida.equals(Taskid))
            // getlistsubtaskdata ();//
            // }
            // else if(type.equalsIgnoreCase("note")){
            // if(taskida.equals(Taskid))
            // getnotelistdata();
            // }
            // else{
            //
            // }

        }
    };

    public void insertdata() {
        jobid = (TextView) findViewById(R.id.jobid);
        projmgt = (TextView) findViewById(R.id.projmgt);
        date = (TextView) findViewById(R.id.date);
        salesrap = (TextView) findViewById(R.id.salesrap);
        tech = (TextView) findViewById(R.id.tech);
        configsiz = (TextView) findViewById(R.id.dated);

     //   String jo = sp.getString("jobid", "");
        // jobid.setText("Job #:"+"\n"+jo);
        jobid.setText("Job #:" + "\n");

        //String nam = sp.getString("tname", "");
        String nam =   Shared_Preference.getLOGIN_USERNAME(Rubishcratedataupload.this);
        tech.setText("Tech:" + "\n" + nam);

        String pro = "proj";// sp.getString("pro", "");
        // projmgt.setText("Project Mgmt:"+"\n"+pro);
        projmgt.setText("Project Mgmt:" + "\n");

        String siz = "siz";// sp.getString("size", "");
        configsiz.setText("Config/Size:" + "\n");

        String sal = "sal";// sp.getString("jobid", "");
        salesrap.setText("Sales Rep:" + "\n");

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        // impleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        date.setText("Date : " + "\n" + formattedDate);
        // ,projmgt,date,salesrap,tech,configsiz;// size
    }

    public void getjsonobjectwithoutimage() {
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, withoutimg,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub

                hideprogressdialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                hideprogressdialog();
               // Log.e("api_error on mainactivity", arg0.toString());

            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }

    public void getjsonobjectwithmage() {
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, withimgh,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub

                // hideprogressdialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                // hideprogressdialog();
               // Log.e("api_error on mainactivity", arg0.toString());

            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }

    public void getoldloc(String links) {
        JsonObjectRequest updatess = new JsonObjectRequest(Method.GET,
                linkforreport, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsono) {
                // TODO Auto-generated method stub
                // updatelayout(jsono);
                hideprogressdialog();
                showtoast();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                hideprogressdialog();
                showtoast();

            }
        });
        AppController.getInstance().addToRequestQueue(updatess);
    }

    public void showtoast() {
       /* LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // set a dummy image
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.helloip);

        // set a message
        // TextView text = (TextView) layout.findViewById(R.id.text);
        // text.setText("Button is clicked!");

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        // toast.setGravity(Gravity.CENTER_VERTICAL, 0, 120);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 10, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();*/

        Toast.makeText(Rubishcratedataupload.this, "Report submitted successfully !", Toast.LENGTH_SHORT).show();
    }

    public void findpmdata() {

        check_gotocatch = 0;
        final String NAMESPACE = KEY_NAMESPACE+"";
        final String URL = urlofwebservice;
        final String SOAP_ACTION = KEY_NAMESPACE+API_send_po_and_job;
        final String METHOD_NAME = API_send_po_and_job;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
      //  String jo = sp.getString("jobid", "");
        String jo =Shared_Preference.getSWO_ID(Rubishcratedataupload.this);
        request.addProperty("swo_id", jo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j); // if complex type is present then you can
                // cast this to SoapObject and if primitive
                // type is returned you can use toString()
                // to get actuall value.
            }
            receivedString = ks.toString();
            // Toast.makeText(getBaseContext(), receivedString,
            // Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            resultmy = 3;
            // Toast.makeText(getBaseContext(), e.toString(),
            // Toast.LENGTH_LONG).show();
            check_gotocatch++;
            e.printStackTrace();
        }
        if (check_gotocatch == 0) {
            try {
                String Jsonstring = receivedString;
                // Toast.makeText(getApplicationContext(),
                // Jsonstring, Toast.LENGTH_LONG).show();
                String news = Jsonstring.substring(Jsonstring.indexOf("["));
                String n1 = news;
                JSONArray jArray = new JSONArray(n1);
                int len = jArray.length();
                for (int k = 0; k < (jArray.length()); k++) {
                    String name;
                    String mob;
                    String id;
                    JSONObject json_obj = jArray.getJSONObject(k);

                    serverResponseCode = 200;
                    resultmy = 0;
                    name = json_obj.getString("job_name");
                    mob = json_obj.getString("PM_name");

                    pmname = mob;
                    jobname = name;
                  //  ed.putString("jobname", name).commit();
                  //  ed.putString("pmname", mob).commit();

                }

            } catch (Exception e) {
                check_gotocatch++;
                // Toast.makeText(getApplicationContext(),
                // "Error on loding data"+e.toString(),
                // Toast.LENGTH_LONG).show();
                resultmy = 3;
                e.printStackTrace();
            }

        }

    }

    public void showtoastg() {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // set a dummy image
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.apply);

        // set a message
        // TextView text = (TextView) layout.findViewById(R.id.text);
        // text.setText("Button is clicked!");

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        // toast.setGravity(Gravity.CENTER_VERTICAL, 0, 120);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void Deletecategory1() {

        check_gotocatch = 0;
        final String NAMESPACE = KEY_NAMESPACE+"";
        final String URL = URL_EP2+"/WebService/techlogin_service.asmx?op=add_item_desc";
        final String SOAP_ACTION = KEY_NAMESPACE+API_add_item_desc;
        final String METHOD_NAME =API_add_item_desc;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("swo_id", wjobid);
        request.addProperty("emp_id", royalid);
        request.addProperty("desc", royaldescq);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j); // if complex type is present then you can
                // cast this to SoapObject and if primitive
                // type is returned you can use toString()
                // to get actuall value.
            }
            receivedString = ks.toString();
            // Toast.makeText(getBaseContext(), receivedString,
            // Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            resultmy = 3;
            // Toast.makeText(getBaseContext(), e.toString(),
            // Toast.LENGTH_LONG).show();
            check_gotocatch++;
            e.printStackTrace();
        }
        if (check_gotocatch == 0) {
            try {
                String Jsonstring = receivedString;
                // Toast.makeText(getApplicationContext(),
                // Jsonstring, Toast.LENGTH_LONG).show();
                String news = Jsonstring.substring(Jsonstring.indexOf("["));
                String n1 = news;
                JSONArray jArray = new JSONArray(n1);
                int len = jArray.length();
                for (int k = 0; k < (jArray.length()); k++) {
                    String name;
                    String mob;
                    String id;
                    JSONObject json_obj = jArray.getJSONObject(k);
                    String resuSt = json_obj.getString("result");
                    if (resuSt.equals("0") || resuSt.equalsIgnoreCase("0")
                            || resuSt == "0") {
                        resultmy = 0;
                       name = json_obj.getString("name");

                        mob = json_obj.getString("mobile");
                        id = json_obj.getString("id");
                       // ed.putString("tname", name).commit();
                        Shared_Preference.setLOGIN_USERNAME(this,name);
                        Shared_Preference.setUSER_MOBILE(this,mob);
                        Shared_Preference.setLOGIN_USER_ID(this,id);
                       // ed.putString("mob", mob).commit();
                     //   ed.putString("clientid", id).commit();

                    } else if (resuSt.equals("1")
                            || resuSt.equalsIgnoreCase("1") || resuSt == "1") {
                        resultmy = 1;
                        name = json_obj.getString("name");
                        mob = "";// json_obj.getString("mobile");
                        id = json_obj.getString("id");
                    } else {
                        resultmy = 2;
                    }

                }

            } catch (Exception e) {
                check_gotocatch++;
                // Toast.makeText(getApplicationContext(),
                // "Error on loding data"+e.toString(),
                // Toast.LENGTH_LONG).show();
                resultmy = 3;
                e.printStackTrace();
            }

        }

    }

    public void Deletecategory(String Desc, String imageName) {
        String Imgname = "";
       /* if (imagePath != null && imagePath.contains("/")) {
            Imgname = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            Imgname = Utility.getUniqueId() + "_" + Imgname;
        }*/

        if (imageName == null) {
            imageName = "";
        }

        check_gotocatch = 0;
        final String NAMESPACE = KEY_NAMESPACE+"";
        final String URL = URL_EP2+"/WebService/techlogin_service.asmx?op=add_item_descwithFile";
        final String SOAP_ACTION = KEY_NAMESPACE+API_add_item_descwithFile;
        final String METHOD_NAME =API_add_item_descwithFile;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("swo_id", wjobid);
        request.addProperty("emp_id", royalid);
        request.addProperty("desc", Desc);
        request.addProperty("fname", imageName);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j); // if complex type is present then you can
                // cast this to SoapObject and if primitive
                // type is returned you can use toString()
                // to get actuall value.
            }
            receivedString = ks.toString();
            // Toast.makeText(getBaseContext(), receivedString,
            // Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            resultmy = 3;
            // Toast.makeText(getBaseContext(), e.toString(),
            // Toast.LENGTH_LONG).show();
            check_gotocatch++;
            e.printStackTrace();
        }
        if (check_gotocatch == 0) {
            try {
                String Jsonstring = receivedString;
                // Toast.makeText(getApplicationContext(),
                // Jsonstring, Toast.LENGTH_LONG).show();
                String news = Jsonstring.substring(Jsonstring.indexOf("["));
                String n1 = news;
                JSONArray jArray = new JSONArray(n1);
                int len = jArray.length();
                for (int k = 0; k < (jArray.length()); k++) {
                    String name;
                    String mob;
                    String id;
                    JSONObject json_obj = jArray.getJSONObject(k);
                    String resuSt = json_obj.getString("result");
                    if (resuSt.equals("0") || resuSt.equalsIgnoreCase("0")
                            || resuSt == "0") {
                        resultmy = 0;
                      //  name = json_obj.getString("name");
                        name =   Shared_Preference.getLOGIN_USERNAME(Rubishcratedataupload.this);
                        mob = json_obj.getString("mobile");
                        id = json_obj.getString("id");
                       // ed.putString("tname", name).commit();
                        Shared_Preference.setLOGIN_USERNAME(this,name);
                        Shared_Preference.setUSER_MOBILE(this,mob);
                        Shared_Preference.setLOGIN_USER_ID(this,id);
                        // ed.putString("mob", mob).commit();
                       // ed.putString("clientid", id).commit();
                    } else if (resuSt.equals("1")
                            || resuSt.equalsIgnoreCase("1") || resuSt == "1") {
                        resultmy = 1;
                        name = json_obj.getString("name");
                        mob = "";// json_obj.getString("mobile");
                        id = json_obj.getString("id");
                        // ed.putString("name", name).commit();
                        // ed.putString("mob", name).commit();
                        // ed.putString("clientid", name).commit();
                    } else {
                        resultmy = 2;
                        // name=json_obj.getString("name");
                        // mob=json_obj.getString("mobile");
                        // id=json_obj.getString("id");
                    }
                    // String name=json_obj.getString("name");
                    // String mob=json_obj.getString("mobile");
                    // String id=json_obj.getString("id");
                    // {"cds":[{"id":28,"name":"tech tech","mobile":"","result":1}]}
                    //

                }

            } catch (Exception e) {
                check_gotocatch++;
                // Toast.makeText(getApplicationContext(),
                // "Error on loding data"+e.toString(),
                // Toast.LENGTH_LONG).show();
                resultmy = 3;
                e.printStackTrace();
            }

        }

    }

    public class asyntask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);
            showtoast();
            finish();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            for (int i = 0; i < list_itemDesc.size(); i++) {
                Deletecategory(list_itemDesc.get(i).get(Utility.ITEM_DESC), list_itemDesc.get(i).get(Utility.IMAGE_PATH));
            }
            return null;
        }

    }

    public class asyntask1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);
            showtoast();
            finish();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Deletecategory1();
            return null;
        }

    }

    public void setSpinner() {

        ArrayList<Myspinner> al_itemType = new ArrayList<>();

        al_itemType.add(new Myspinner("--Choose your item type--", "0"));
        al_itemType.add(new Myspinner("Customer Owned", "1"));
        al_itemType.add(new Myspinner("IDC Rental", "2"));
        al_itemType.add(new Myspinner("Local Rental", "3"));
        al_itemType.add(new Myspinner("New Product", "4"));
        ArrayAdapter<Myspinner> adapter = new ArrayAdapter<Myspinner>(Rubishcratedataupload.this, android.R.layout.simple_spinner_item, al_itemType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (!second) {
            spnr_Item_Type.setAdapter(adapter);
        } else {
            spnr_Item_Type1.setAdapter(adapter);
        }

    }

    public void workornot() {


        new asyntaskupload().execute();

/*

        final Dialog showd = new Dialog(Rubishcratedataupload.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        showd.setCancelable(false);
        TextView nofo = (TextView) showd.findViewById(R.id.noforlocation);
        TextView yesfo = (TextView) showd.findViewById(R.id.yesforloc);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        texrtdesc.setText("Do you want to upload more photos?");
        nofo.setText("  Yes  ");
        yesfo.setText("  No  ");

        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Please select");
        nofo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                opendilogforattachfileandimage();
                showd.dismiss();

                // finish();

            }
        });
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();
                //	finish();


            }
        });
        yesfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showd.dismiss();

                //call here multipart and after call webapi for desc upload

                if (new ConnectionDetector(Rubishcratedataupload.this).isConnectingToInternet()) {

                    new asyntaskupload().execute();

                }


                //finish();
                // finishs();
            }
        });

        showd.show();
*/


    }

}
