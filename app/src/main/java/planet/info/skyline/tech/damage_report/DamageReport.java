package planet.info.skyline.tech.damage_report;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import planet.info.skyline.network.REST_API_Client;
import planet.info.skyline.old_activity.AppConstants;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.old_activity.CustomMultiPartEntity;
import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.ProgressRequestBody;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.CameraUtils;
import planet.info.skyline.util.Myspinner;
import planet.info.skyline.util.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static planet.info.skyline.network.Api.API_add_item_descwithFile;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class DamageReport extends BaseActivity implements ProgressRequestBody.UploadCallbacks {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static String imageStoragePath;
    List<HashMap<String, String>> listad = new ArrayList<HashMap<String, String>>();
    ListView listdatal;
    // EditText item1,item2,item3,item4;
    TextView gobacktosca;
    AlertDialog alertDialog;
    DisplayImageOptions options;
    SharedPreferences sp;
    Editor ed;
    Uri mImageCaptureUri;
    int serverResponseCode;
    TextView clientname;
    ImageView merchantname, missing, homeacti;
    ProgressDialog pDialog;
    String path, fname = "";
    File file1;
    TextView jobid, projmgt, date, salesrap, tech, configsiz, gomissi,
            uploadpict1;// size
    //  Button itencount;
    //   ImageView uploadpict;
    String item, desc, esimate, labourhou, linkforreport, withimgh, withoutimg;
    EditText item1, item2, item3, item4;
    EditText item5, item6, item7, item8;
    String wjobid, wjobidq, wimg = "", wuname, royaldesc, royaldescq, royalid;
    int resultmy, check_gotocatch;
    // String itemtype = "";
    Boolean second = false;
    String urlofwebservice = URL_EP2 + "/WebService/techlogin_service.asmx?op=send_po_and_job",
            receivedString, pmname = "", jobname = "";
    Spinner spnr_Item_Type, spnr_Item_Type1;
    View footerLayout;
    // ArrayList<String> list_TotalPhotoforItem = new ArrayList<>();
    //  ArrayList<String> list_TotalPhotoforItem2 = new ArrayList<>();
    ArrayList<HashMap<String, String>> list_itemDesc = new ArrayList<>();
    ArrayList<String> list_path = new ArrayList<>();
    ArrayList<String> list_UploadImageName = new ArrayList<>();
    long totalSize = 0;
    LinearLayout ll_container;
    ScrollView scrolll;
    int itemNo = -1;
    HashMap<Integer, String> hmap = new HashMap<>();
    ProgressDialog uploadProgressDialog;
    int Count_Image_Uploaded = 0;
   // API_Interface APIInterface;
    private TextView timerValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rubishcratedataupload_new_1);
        gobacktosca = (TextView) findViewById(R.id.gobacktosca);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        scrolll = (ScrollView) findViewById(R.id.scrolll);
        gomissi = (TextView) findViewById(R.id.gomissi);
        clientname = (TextView) findViewById(R.id.textView1);
        merchantname = (ImageView) findViewById(R.id.merchantname);
        merchantname.setImageResource(R.drawable.exhibitlogoa);
        missing = (ImageView) findViewById(R.id.missing);
        sp = getApplicationContext().getSharedPreferences("skyline",
                MODE_PRIVATE);
        ed = sp.edit();
      //  String nam = sp.getString("name", "");
        String nam  = Shared_Preference.getCLIENT_NAME(this);
        //String imageloc = sp.getString("imglo", "");
        String imageloc =	Shared_Preference.getCLIENT_IMAGE_LOGO_URL(this);
        if (imageloc.equals("") || imageloc.equalsIgnoreCase("")) {
         //   ed.putString("imglo", "").commit();
            Shared_Preference.setCLIENT_IMAGE_LOGO_URL(this,"");
            missing.setVisibility(View.GONE);
            clientname.setText(nam);
        } else {
            // imageLoadery.displayImage(imageloc, missing, options);

            Glide
                    .with(DamageReport.this)
                    .load(imageloc)     // Uri of the picture
                    .into(missing);


        }
        homeacti = (ImageView) findViewById(R.id.homeacti);

        addItem();
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
        gobacktosca.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int valid = validation();
                if (valid == 1) {
                    addItem();
                }
            }
        });
        gomissi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int valid = validation();
                list_itemDesc.clear();
                if (valid == 1) {
                    int cnt = ll_container.getChildCount();
                    for (int i = 0; i < cnt; i++) {
                        View vi = ll_container.getChildAt(i);

                        EditText item1 = (EditText) vi.findViewById(R.id.itemdetails);
                        EditText item2 = (EditText) vi.findViewById(R.id.itemdetails1);
                        Spinner spnr_Item_Type = (Spinner) vi.findViewById(R.id.spnr_Item_Type);
                        Myspinner myspinner = (Myspinner) spnr_Item_Type.getSelectedItem();
                        String Item_type_val = myspinner.getspinnerVal();
                        String Item_type_text = myspinner.getSpinnerText();
                        int ItemNo = (int) vi.getTag();


                        String item = item1.getText().toString().trim();
                        String desc = item2.getText().toString().trim();
                        String descs = "Damage Report to " + Item_type_text + ": " + item
                                + ": " + desc;

                        HashMap<String, String> hashMap = new HashMap<String, String>();

                        if (hmap.containsKey(i)) {
                            String path = hmap.get(i);
                            hashMap.put(Utility.IMAGE_PATH, path);
                        } else {
                            hashMap.put(Utility.IMAGE_PATH, "");
                        }


                        hashMap.put(Utility.ITEM_DESC, descs);
                        list_itemDesc.add(hashMap);//nks

                    }

                    if (new ConnectionDetector(DamageReport.this).isConnectingToInternet()) {
                        new asyntask().execute();
                    } else {
                        Toast.makeText(DamageReport.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

    }

    public void addItem() {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.child_row_1, null);

        Button itencount = view.findViewById(R.id.itencount);
        ImageView uploadpict = view.findViewById(R.id.uploadpict);
        EditText item1 = (EditText) view.findViewById(R.id.itemdetails);
        EditText item2 = (EditText) view.findViewById(R.id.itemdetails1);
        ImageView remove_item = view.findViewById(R.id.remove_item);
        Spinner spnr_Item_Type = (Spinner) view.findViewById(R.id.spnr_Item_Type);
        ImageView uploadPic = view.findViewById(R.id.uploadpict);
////set Spinner
        ArrayList<Myspinner> al_itemType = new ArrayList<>();
        al_itemType.add(new Myspinner("--Choose your Item Type--", "0"));
        al_itemType.add(new Myspinner("Customer Owned", "1"));
        al_itemType.add(new Myspinner("IDC Rental", "2"));
        al_itemType.add(new Myspinner("Local Rental", "3"));
        al_itemType.add(new Myspinner("New Product", "4"));
        ArrayAdapter<Myspinner> adapter = new ArrayAdapter<Myspinner>(DamageReport.this, android.R.layout.simple_spinner_item, al_itemType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_Item_Type.setAdapter(adapter);
//////
        remove_item.setVisibility(View.GONE);
        ll_container.addView(view);
        int childCnt = ll_container.getChildCount();
        itencount.setText(String.valueOf(childCnt));
        if (childCnt > 1) {
            remove_item.setVisibility(View.VISIBLE);
        }
        view.setTag(childCnt);

        //  scrolll.scrollTo(0, btn_FinishWork.getBottom());
        remove_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DamageReport.this);
                LayoutInflater inflater = LayoutInflater.from(DamageReport.this);
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
                title.setText("Remove this item?");
                message.setText("Are you sure?");
                positiveBtn.setText("Yes");
                negativeBtn.setText("No");
                negativeBtn.setVisibility(View.VISIBLE);
                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                        /**/
                        ll_container.removeView(view);
                        try {
                            int index = ((int) view.getTag()) - 1;
                            hmap.remove(index);
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        int cnt = ll_container.getChildCount();
                        for (int i = 0; i < cnt; i++) {
                            View vi = ll_container.getChildAt(i);
                            vi.setTag(i + 1);
                            Button itemCount = vi.findViewById(R.id.itencount);
                            itemCount.setText(String.valueOf(i + 1));
                            ImageView remove_item = vi.findViewById(R.id.remove_item);
                            if (i == 0) {
                                remove_item.setVisibility(View.GONE);
                            } else {
                                remove_item.setVisibility(View.VISIBLE);
                            }
                        }
                        /**/

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


            }
        });
        uploadPic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new ConnectionDetector(DamageReport.this).isConnectingToInternet()) {
                    itemNo = (int) view.getTag();
                    opendilogforattachfileandimage();
                } else {
                    Toast.makeText(DamageReport.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void opendilogforattachfileandimage() {
        final Dialog dialog = new Dialog(DamageReport.this);
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
                dialog.dismiss();



                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }


            }
        });
        gallarylayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DamageReport.this, AlbumSelectActivity.class);
                intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 1);
                startActivityForResult(intent, AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);


                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                // finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {

                if (data != null) {
                    //The array list has the image paths of the selected images
                    ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
                    for (int i = 0; i < images.size(); i++) {
                        list_path.add(images.get(i).path);
                    }
                    UploadImage();
                } else {
                    Uri mImageCaptureUri = FileProvider.getUriForFile(DamageReport.this, DamageReport.this.getApplicationContext().getPackageName() + ".provider", file1);
                    try {
                        path = getPath(mImageCaptureUri,
                                DamageReport.this); // from Gallery
                        list_path.add(path);
                        UploadImage();
                        //nks
                    } catch (Exception e) {
                        path = mImageCaptureUri.getPath();
                        list_path.add(path);
                        UploadImage();
                    }

                }




            }
            //
            else if (requestCode == AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                path = imageStoragePath;
                ///change orientation of captured photo
                int orientation = Utility.getExifOrientation(path);
                try {
                    if (orientation == 90) {
                        //   list_TempImagePath.add(path);
                        Bitmap bmp = Utility.getRotatedBitmap(path, 90);
                        path = Utility.saveImage(bmp);
                    }
                } catch (Exception e) {
                    e.getCause();
                }
                ///change orientation
                list_path.add(path);
                UploadImage();

            }

        } else {
            itemNo = -1;
        }

    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void UploadImage() {


        //    new asyntaskupload().execute();
        multipartImageUpload();

    }

    public int validation() {

        int val = 0;
        int cnt = ll_container.getChildCount();
        for (int i = 0; i < cnt; i++) {
            View vi = ll_container.getChildAt(i);

            EditText item1 = (EditText) vi.findViewById(R.id.itemdetails);
            EditText item2 = (EditText) vi.findViewById(R.id.itemdetails1);
            Spinner spnr_Item_Type = (Spinner) vi.findViewById(R.id.spnr_Item_Type);
            Myspinner myspinner = (Myspinner) spnr_Item_Type.getSelectedItem();
            String Item_type_val = myspinner.getspinnerVal();
            String Item_type_text = myspinner.getSpinnerText();
            int ItemNo = (int) vi.getTag();
            if (item1.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(DamageReport.this,
                        "Please enter item description of item " + ItemNo, Toast.LENGTH_LONG).show();
                val = 0;
                break;
            } else if (item2.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(DamageReport.this,
                        "Please enter damage description of item " + ItemNo, Toast.LENGTH_LONG)
                        .show();
                val = 0;
                break;
            } else if (Item_type_val.equalsIgnoreCase("0")) {
                Toast.makeText(DamageReport.this,
                        "Please choose Item Type of item " + ItemNo, Toast.LENGTH_LONG)
                        .show();
                val = 0;
                break;
            } else {
                val = 1;

            }

        }
        return val;
    }

    public void Deletecategory(String Desc, String imageName) {

        if (imageName == null) {
            imageName = "";
        }
       // wjobid = sp.getString("jobid", "s");
        wjobid = Shared_Preference.getSWO_ID(this);
     //   wuname = sp.getString("tname", "");
        wuname =    Shared_Preference.getLOGIN_USERNAME(this);

        //String emp_id = sp.getString("clientid", "");
        String emp_id =	Shared_Preference.getLOGIN_USER_ID(DamageReport.this);

        check_gotocatch = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx?op=add_item_descwithFile";
        final String SOAP_ACTION = KEY_NAMESPACE + API_add_item_descwithFile;
        final String METHOD_NAME = API_add_item_descwithFile;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("swo_id", wjobid);
        request.addProperty("emp_id", emp_id);
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
            receivedString = ks.toString();

            if (receivedString.contains("{add_item_descwithFileResult")) {
                String json = receivedString.substring(receivedString.indexOf("=") + 1, receivedString.indexOf(";"));

                JSONObject jsonObject = new JSONObject(json);
                String res = jsonObject.getString("cds");
                if (res.contains("1")) {
                    resultmy = 1;
                } else {
                    resultmy = 0;
                }
            } else {
                resultmy = 0;
            }


        } catch (Exception e) {
            resultmy = 0;
            e.printStackTrace();
        }


    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void requestCameraPermission(final int type) {
        try {
            Dexter.withActivity(this)
                    .withPermissions(android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    // Manifest.permission.RECORD_AUDIO)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {

                                if (type == MEDIA_TYPE_IMAGE) {
                                    // capture picture
                                    captureImage();
                                } else {
                                    // captureVideo();
                                }

                            } else if (report.isAnyPermissionPermanentlyDenied()) {
                                showPermissionsAlert();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(DamageReport.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void dialog_photo_upload_failed() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DamageReport.this);
        LayoutInflater inflater = LayoutInflater.from(DamageReport.this);
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
        title.setText("Failed!");
        message.setText("Image(s) not uploaded!");
        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.GONE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
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
    }

    @Override
    public void onProgressUpdate(int percentage) {
        // textView.setText(percentage + "%");
        uploadProgressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {
        //  textView.setText("Uploaded Failed!");
    }

    @Override
    public void onFinish() {
        if (Count_Image_Uploaded < list_path.size())
            Count_Image_Uploaded++;
        uploadProgressDialog.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_path.size() + ", Please wait..");

    }

    @Override
    public void uploadStart() {
        //  textView.setText("0%");
    }

    private void multipartImageUpload() {
        if (Count_Image_Uploaded < list_path.size())
            Count_Image_Uploaded++;
        uploadProgressDialog = new ProgressDialog(DamageReport.this);
        // uploadProgressDialog.setMessage("Uploading , Please wait..");
        uploadProgressDialog.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_path.size() + ", Please wait..");
        uploadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        uploadProgressDialog.setIndeterminate(false);
        uploadProgressDialog.setProgress(0);
        uploadProgressDialog.setMax(100);
        uploadProgressDialog.setCancelable(false);
        uploadProgressDialog.show();

    //    initRetrofitClient();

        /*damage report photo upload*/
        list_UploadImageName.clear();
        //   int statusCode = 0;
        totalSize = 0;

        try {
            /**/
            MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[list_path.size()];
            for (int index = 0; index < list_path.size(); index++) {

                String path = list_path.get(index);
                File file = new File(path);
                ProgressRequestBody surveyBody = new ProgressRequestBody(file, this);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("images[]", file.getName(), surveyBody);
                long Size = file.length();
                totalSize = totalSize + Size;

            }
         //   String jid = sp.getString(Utility.KEY_JOB_ID_FOR_JOBFILES, "");
            String jid =  Shared_Preference.getJOB_ID_FOR_JOBFILES(this);
            String url = URL_EP2 + "/UploadFileHandler.ashx?jid=" + jid;
            /**/

            API_Interface APIInterface = REST_API_Client.getClient().create(API_Interface.class);

            Call<ResponseBody> req = APIInterface.uploadMedia(surveyImagesParts, url);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    //     String result = "";
                    Count_Image_Uploaded = 0;
                    if (String.valueOf(response.code()).equals("200")) {

                        try {
                            uploadProgressDialog.setProgress(100);
                            String responseStr = response.body().string();
                            if (!responseStr.contains("api_error")) {
                                String s[] = responseStr.split(",");
                                List<String> stringList = new ArrayList<String>(Arrays.asList(s)); //new Ar
                                list_UploadImageName = new ArrayList<String>(stringList);
                                hmap.put(itemNo - 1, responseStr);
                                itemNo = -1;

                                //      result = "1";
                            } else {
                                //     result = "0";
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            //  result = "0";
                        }

                    }


                    for (int i = 0; i < list_path.size(); i++) {
                        String TempImagePath = list_path.get(i);
                        if (TempImagePath.contains("TempImage-")) {
                            Utility.delete(TempImagePath);
                        }
                    }

                    list_path.clear();

                    try {
                        uploadProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    if (String.valueOf(response.code()).equals("200")) {
                        Toast.makeText(getApplicationContext(), "Photo uploaded successfully!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), " Upload Failed!", Toast.LENGTH_SHORT).show();
                        dialog_photo_upload_failed();

                    }


                    }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        if (uploadProgressDialog.isShowing())
                            uploadProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    Count_Image_Uploaded = 0;
                    list_path.clear();
                    Toast.makeText(getApplicationContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    dialog_photo_upload_failed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .build();
        try {
            APIInterface = new Retrofit.Builder().baseUrl(URL_EP2).client(client).build().create(API_Interface.class);
        } catch (Exception e) {
            e.getMessage();
        }
    }*/

    public class asyntask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDoalog = new ProgressDialog(DamageReport.this);
            progressDoalog.setMessage(getString(R.string.Loading_text));
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();

        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            list_itemDesc.clear();
            hmap.clear();
            if (resultmy == 1) {
                Toast.makeText(DamageReport.this, "Report submitted successfully !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DamageReport.this, "Report submitting failed!", Toast.LENGTH_SHORT).show();
            }
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

    public class asyntaskupload extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            list_UploadImageName.clear();
            int statusCode = 0;
            totalSize = 0;
            try {
              //  String jid = sp.getString(Utility.KEY_JOB_ID_FOR_JOBFILES, "");
                String jid =  Shared_Preference.getJOB_ID_FOR_JOBFILES(DamageReport.this);
                String FILE_UPLOAD_URL = URL_EP2 + "/UploadFileHandler.ashx?jid=" + jid;
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

                for (int i = 0; i < list_path.size(); i++) {
                    try {
                        String path = list_path.get(i);
                        File sourceFile_1 = new File(path);
                        entity.addPart("image" + i, new FileBody(sourceFile_1));
                        long Size = sourceFile_1.length();
                        totalSize = totalSize + Size;
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
                    String s[] = responseStr.split(",");
                    List<String> stringList = new ArrayList<String>(Arrays.asList(s)); //new Ar
                    list_UploadImageName = new ArrayList<String>(stringList);
                    hmap.put(itemNo - 1, responseStr);
                    itemNo = -1;
                } else {
                    String resultdata = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (Exception e) {
                e.getMessage();
                statusCode = 0;
            }

            return statusCode + "";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            try {
                progressDialog.setProgress(progress[0]);

            } catch (Exception e) {
                e.getMessage();
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(DamageReport.this);
            progressDialog.setMessage("Uploading, please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // TODO Auto-generated method stub
            try {
                progressDialog.dismiss();

                for (int i = 0; i < list_path.size(); i++) {
                    String TempImagePath = list_path.get(i);
                    if (TempImagePath.contains("TempImage-")) {
                        Utility.delete(TempImagePath);
                    }
                }

                list_path.clear();

                if (result.equalsIgnoreCase("200")) {
                    Toast.makeText(getApplicationContext(), "Photo uploaded successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    dialog_photo_upload_failed();

                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

    }
}
