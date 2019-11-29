package planet.info.skyline.tech.upload_photo;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.ProgressRequestBody;
import planet.info.skyline.network.REST_API_Client;
import planet.info.skyline.old_activity.AppConstants;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivity;
import planet.info.skyline.tech.job_files_new.SharePhotosToClientActivity;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.CameraUtils;
import planet.info.skyline.util.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static planet.info.skyline.network.Api.API_UploadPhotoTech;
import static planet.info.skyline.network.Api.API_UploadPhotoTech_new;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class Upload_image_and_cooment_New extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static String imageStoragePath;

    ProgressDialog pDialog;
    String path;
    File file1;


    String urlofwebservice2 = URL_EP2 + "/WebService/techlogin_service.asmx?op=UploadPhotoTech";
    int job_or_swo = 1;
    AlertDialog alertDialog;
    String selectedJobId;//nks

    ArrayList<String> list_path = new ArrayList<>();
    ArrayList<String> list_TempImagePath = new ArrayList<>();
    ArrayList<HashMap<String, String>> list_ImageDescData = new ArrayList<>();
    ArrayList<String> list_imageSize = new ArrayList<>();
    ArrayList<String> list_UploadImageName = new ArrayList<>();
    ArrayList<HashMap<String, String>> list_UploadedImageID = new ArrayList<>();
    long totalSize = 0;


    ExpandableHeightListView listvw_images;
    LvAdapter adpter;
    String userRole = "";
    ProgressDialog uploadProgressDialog;
    int Count_Image_Uploaded = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload_image_and_cooment);
        pDialog = new ProgressDialog(Upload_image_and_cooment_New.this);
        pDialog.setMessage(getString(R.string.Loading_text));
        pDialog.setCancelable(false);

        userRole = Shared_Preference.getUSER_ROLE(this);
        final boolean TIMER_STARTED_FROM_BILLABLE_MODULE = Shared_Preference.getTIMER_STARTED_FROM_BILLABLE_MODULE(this);
        if (TIMER_STARTED_FROM_BILLABLE_MODULE) {
            selectedJobId = Shared_Preference.getJOB_ID_FOR_JOBFILES(this);
            job_or_swo = 3;
            opendilogforattachfileandimage_custom();

            String compID = Shared_Preference.getCOMPANY_ID_BILLABLE(this);
            String company_Name = Shared_Preference.getCLIENT_NAME(this);
            String job_Name = Shared_Preference.getJOB_NAME_BILLABLE(this);

        } else {
            Intent i = new Intent(this, SelectCompanyActivity.class);
            i.putExtra(Utility.IS_JOB_MANDATORY, "1");
            i.putExtra(Utility.Show_DIALOG_SHOW_INFO, true);
            startActivityForResult(i, Utility.CODE_SELECT_COMPANY);

        }


    }

    public void opendilogforattachfileandimage_custom() {
        final Dialog dialog = new Dialog(Upload_image_and_cooment_New.this);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.openattachmentdilog_new);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        //  dialog.setTitle("Attach");
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


                exitMethod();


            }
        });

        cameralayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
                dialog.dismiss();
                Intent intent = new Intent(Upload_image_and_cooment_New.this, AlbumSelectActivity.class);
                intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 50);
                startActivityForResult(intent, AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);


            }
        });
        filelayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

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
            }
        });
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //finish();
        finishs();
    }

    public void showprogressdialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    public void hideprogressdialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }




    public void dialog_sharePhotos() {
        final Dialog showd = new Dialog(Upload_image_and_cooment_New.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_yes_no);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        showd.setCancelable(false);
        TextView Btn_No = (TextView) showd.findViewById(R.id.Btn_No);
        TextView Btn_Yes = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        texrtdesc.setText("Do you want to share these photos?");


        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);

        textviewheader.setText("Uploaded Successfully!");

        Btn_No.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showd.dismiss();
                list_UploadedImageID.clear();
                finishs();

            }
        });
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();
                list_UploadedImageID.clear();
                finishs();

            }
        });
        Btn_Yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();


                Intent intent = new Intent(Upload_image_and_cooment_New.this, SharePhotosToClientActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) list_UploadedImageID);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
                finish();

            }
        });

        showd.show();


    }

    public String enterdata(String a, String b, String c, String d, String e,
                            String f, String g, String h) {


        String UploadedPhotoID = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice2;
        final String SOAP_ACTION = KEY_NAMESPACE + API_UploadPhotoTech;
        final String METHOD_NAME = API_UploadPhotoTech;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("job_swo_id", a);
        request.addProperty("description", b);
        request.addProperty("filename", c);
        request.addProperty("comment", d);
        request.addProperty("filesize", e);
        request.addProperty("tech_name", f);
        request.addProperty("tech_id", g);
        request.addProperty("type", h);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            Log.e("UploadPhotoTech--", "Called");
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();

            JSONObject jsonObject = new JSONObject(result);
            UploadedPhotoID = jsonObject.getString("cds");

            Log.e("UploadedPhotoID--", UploadedPhotoID);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("int_job_file_id", UploadedPhotoID);

            list_UploadedImageID.add(hashMap);

            Log.e("UploadPhotoTech--", "End");
        } catch (Exception ed) {

            ed.printStackTrace();
        }


        return String.valueOf(UploadedPhotoID);
    }

    public String generate_event(String job_swo_id, String tech_id, String type, String count) {
        String result = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        //  final String URL = urlofwebservice2;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_UploadPhotoTech_new;
        final String METHOD_NAME = API_UploadPhotoTech_new;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("job_swo_id", job_swo_id);
        request.addProperty("tech_id", tech_id);
        request.addProperty("type", type);
        request.addProperty("count", count);

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

            }
            String response = ks.toString();
            if (response.contains("st=1")) {
                result = "1";
            } else {
                result = "0";
            }
        } catch (Exception ed) {
            ed.printStackTrace();
        }
        return result;
    }


    public void finishs() {
        Intent iiu = new Intent(Upload_image_and_cooment_New.this, MainActivity.class);
        iiu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iiu);
        finish();
    }


    public void exitMethod() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Upload_image_and_cooment_New.this);
        LayoutInflater inflater = LayoutInflater.from(Upload_image_and_cooment_New.this);
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
        title.setText("Do you want to exit?");
        message.setText("Are you sure?");
        positiveBtn.setText("Yes");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent in = new Intent(Upload_image_and_cooment_New.this, MainActivity.class);
                startActivity(in);
                finishs();
            }
        });
        negativeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (list_ImageDescData.size() > 0) {

                    dialog_Multiple_image_Desciption();
                } else {
                    opendilogforattachfileandimage_custom();
                }
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();


    }

    private void dialog_Multiple_image_Desciption() {

        final Dialog dialog_image = new Dialog(Upload_image_and_cooment_New.this);
        dialog_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_image.setContentView(R.layout.dialog_photo_comment_list_1);
        try {
            dialog_image.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } catch (Exception e) {
            e.getMessage();
        }
        dialog_image.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        dialog_image.setCancelable(false);
        dialog_image.setCanceledOnTouchOutside(false);

        listvw_images = (ExpandableHeightListView) dialog_image.findViewById(R.id.list_multiple_photo_Comment);
        listvw_images.setItemsCanFocus(true);

        Button Btn_Submit = (Button) dialog_image.findViewById(R.id.Btn_Submit);
        Button Btn_AddMorePhotos = (Button) dialog_image.findViewById(R.id.Btn_AddMorePhotos);
        Button Btn_Back = (Button) dialog_image.findViewById(R.id.Btn_Back);
        String clid = Shared_Preference.getLOGIN_USER_ID(Upload_image_and_cooment_New.this);
        String name = Shared_Preference.getLOGIN_USERNAME(this);
        job_or_swo = 3;//means when Jobid_or_swoid not exists then type=2

//nks


        for (int i = 0; i < list_path.size(); i++) {

            String PathImage = list_path.get(i);
            String pathName[] = PathImage.split("/");
            String image_size = "24 kb";
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put(Utility.KEY_Jobid_or_swoid, selectedJobId);
            hmap.put(Utility.KEY_imagename, pathName[pathName.length - 1]);
            hmap.put(Utility.KEY_image_size, image_size);
            hmap.put(Utility.KEY_name, name);
            hmap.put(Utility.KEY_clid, clid);
            hmap.put(Utility.KEY_job_or_swo, job_or_swo + "");

            hmap.put(Utility.KEY_imagePath, PathImage);
            list_ImageDescData.add(hmap);
        }
        list_path.clear();


        ///////////////////////////////////////////////

        adpter = new LvAdapter(Upload_image_and_cooment_New.this);
        listvw_images.setAdapter(adpter);
        listvw_images.setExpanded(true);
        dialog_image.show();
//To scroll list to 1st item after adding item to list
        //  scroll_list_toTop();

        Btn_Submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                int valid = validation();
                if (valid == 1) {
                    dialog_image.dismiss();
                    ////////////////////////////////
                    Utility.hideKeyboard(Upload_image_and_cooment_New.this);
                    /////////////////////
                    if (new ConnectionDetector(Upload_image_and_cooment_New.this).isConnectingToInternet()) {
                        multipartImageUpload();
                    } else {
                        Toast.makeText(Upload_image_and_cooment_New.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Btn_AddMorePhotos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_image.dismiss();


                opendilogforattachfileandimage_custom();


            }
        });


        Btn_Back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Upload_image_and_cooment_New.this);
                LayoutInflater inflater = LayoutInflater.from(Upload_image_and_cooment_New.this);
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
                title.setText("Do you want to exit?");
                message.setText("All photo(s) will be lost!");
                positiveBtn.setText("Yes");
                negativeBtn.setText("No");
                negativeBtn.setVisibility(View.VISIBLE);
                positiveBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Intent in = new Intent(Upload_image_and_cooment_New.this, MainActivity.class);
                        startActivity(in);
                        finishs();
                    }
                });
                negativeBtn.setOnClickListener(new OnClickListener() {
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


    }

    public int validation() {

        int val = 0;
        int cnt = list_ImageDescData.size();
        for (int i = 0; i < cnt; i++) {

            String textDesc = list_ImageDescData.get(i).get(Utility.KEY_dataa);
            if (textDesc == null) {
                textDesc = "";
            }

            int ItemNo = i + 1;
            if (textDesc.equalsIgnoreCase("")) {
                Toast.makeText(Upload_image_and_cooment_New.this,
                        "Please enter description of photo " + ItemNo, Toast.LENGTH_LONG).show();
                val = 0;
                break;
            } else {
                val = 1;

            }

        }
        return val;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // onResume();
            if (requestCode == AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    //The array list has the image paths of the selected images
                    ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
                    for (int i = 0; i < images.size(); i++) {
                        list_path.add(images.get(i).path);
                    }

                    dialog_Multiple_image_Desciption();
                } else {
                    Uri mImageCaptureUri = FileProvider.getUriForFile(Upload_image_and_cooment_New.this, Upload_image_and_cooment_New.this.getApplicationContext().getPackageName() + ".provider", file1);
                    try {
                        path = getPath(mImageCaptureUri,
                                Upload_image_and_cooment_New.this); // from Gallery
                        list_path.add(path);
                        dialog_Multiple_image_Desciption();
                        //nks
                    } catch (Exception e) {
                        path = mImageCaptureUri.getPath();
                        list_path.add(path);
                        dialog_Multiple_image_Desciption();
                    }

                }
            }
            //

            if (requestCode == AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                path = imageStoragePath;
                int orientation = Utility.getExifOrientation(path);
                try {
                    if (orientation == 90) {
                        list_TempImagePath.add(path);
                        Bitmap bmp = Utility.getRotatedBitmap(path, 90);
                        path = Utility.saveImage(bmp);
                    }
                } catch (Exception e) {
                    e.getCause();
                }
                ///change orientation
                list_path.add(path);
                dialog_Multiple_image_Desciption();


            }
            if ( requestCode == Utility.CODE_SELECT_COMPANY) {

                //   if (resultCode == Activity.RESULT_OK) {
                try {
                    String compID = data.getStringExtra("CompID");
                    String jobID = data.getStringExtra("JobID");
                    String company_Name = data.getStringExtra("CompName");
                    String job_Name = data.getStringExtra("JobName");

                    selectedJobId = jobID;

                    opendilogforattachfileandimage_custom();

                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getApplicationContext(), "Exception caught!", Toast.LENGTH_SHORT).show();
                }

            }

        }
       if(resultCode == RESULT_CANCELED){
           finishs();
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
                        CameraUtils.openSettings(Upload_image_and_cooment_New.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void multipartImageUpload() {
        if (Count_Image_Uploaded < list_ImageDescData.size())
            Count_Image_Uploaded++;
        uploadProgressDialog = new ProgressDialog(Upload_image_and_cooment_New.this);
        // uploadProgressDialog.setMessage("Uploading , Please wait..");
        uploadProgressDialog.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_ImageDescData.size() + ", Please wait..");
        uploadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        uploadProgressDialog.setIndeterminate(false);
        uploadProgressDialog.setProgress(0);
        uploadProgressDialog.setMax(100);
        uploadProgressDialog.setCancelable(false);
        uploadProgressDialog.show();

        //  initRetrofitClient();

        try {
            /**/
            MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[list_ImageDescData.size()];
            for (int index = 0; index < list_ImageDescData.size(); index++) {
                File file = new File(list_ImageDescData.get(index).get(Utility.KEY_imagePath));
                ProgressRequestBody surveyBody = new ProgressRequestBody(file, this);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("images[]", file.getName(), surveyBody);

                long Size = file.length();
                totalSize = totalSize + Size;
                list_imageSize.add(String.valueOf(Size / 1000));//kb
            }
            String jid = list_ImageDescData.get(0).get(Utility.KEY_Jobid_or_swoid);
            String url = URL_EP2 + "/UploadFileHandler.ashx?jid=" + jid;
            /**/


            API_Interface APIInterface = REST_API_Client.getClient().create(API_Interface.class);

            Call<ResponseBody> req = APIInterface.uploadMedia(surveyImagesParts, url);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    String result = "";
                    Count_Image_Uploaded = 0;
                    if (String.valueOf(response.code()).equals("200")) {
                        try {
                            uploadProgressDialog.setProgress(100);
                            String responseStr = response.body().string();
                            if (!responseStr.contains("api_error")) {
                                String s[] = responseStr.split(",");
                                List<String> stringList = new ArrayList<String>(Arrays.asList(s)); //new Ar
                                list_UploadImageName = new ArrayList<String>(stringList);

                                result = "1";
                            } else {
                                result = "0";
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            result = "0";
                        }
                        if (result.equalsIgnoreCase("1")) {
                            new async_LinkUploadedFiles().execute();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Upload Failed!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    dialog_photo_upload_failed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (Count_Image_Uploaded < list_ImageDescData.size())
            Count_Image_Uploaded++;
        uploadProgressDialog.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_ImageDescData.size() + ", Please wait..");

    }

    @Override
    public void uploadStart() {
        //  textView.setText("0%");
    }

    private void dialog_photo_upload_failed() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Upload_image_and_cooment_New.this);
        LayoutInflater inflater = LayoutInflater.from(Upload_image_and_cooment_New.this);
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
        message.setText("Image(s) not uploaded!\nPlease try after some time!");
        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.GONE);
        positiveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finishs();
            }
        });
        negativeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public class LvAdapter extends BaseAdapter {

        LayoutInflater mInflater;
        Context context;

        public LvAdapter(Context context) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list_ImageDescData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            final ViewHolder holder;
            convertView = null;
            final String index = String.valueOf(position + 1);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.dialog_comment_box_new, null); // this is your cell

                holder.et_Desc = (EditText) convertView.findViewById(R.id.et_desc);
                holder.index_no = (Button) convertView.findViewById(R.id.serial_no);
                holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
                holder.img_remove_item = (ImageView) convertView.findViewById(R.id.img_remove_item);
                holder.imageName = (TextView) convertView.findViewById(R.id.imageName);

                holder.et_Desc.setTag(position);
                holder.et_Desc.setText(list_ImageDescData.get(position).get(Utility.KEY_dataa));
                holder.imageName.setText(list_ImageDescData.get(position).get(Utility.KEY_imagename));
                holder.index_no.setText(index);
                holder.img_remove_item.setTag(position);

                final String Image_Link = list_ImageDescData.get(position).get(Utility.KEY_imagePath);
                Glide
                        .with(context)
                        .load(new File(Image_Link))     // Uri of the picture
                        .into(holder.thumbnail);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            int tag_position = (Integer) holder.et_Desc.getTag();
            holder.et_Desc.setId(tag_position);

            int tag_position1 = (Integer) holder.img_remove_item.getTag();
            holder.img_remove_item.setId(tag_position1);

            holder.et_Desc.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    final int position2 = holder.et_Desc.getId();
                    final EditText Caption = (EditText) holder.et_Desc;

                    HashMap hashMap = (HashMap) list_ImageDescData.get(position2);
                    hashMap.put(Utility.KEY_dataa, Caption.getText().toString().trim());
                    list_ImageDescData.set(position2, hashMap);


                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });

            holder.img_remove_item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Upload_image_and_cooment_New.this);
                    LayoutInflater inflater = LayoutInflater.from(Upload_image_and_cooment_New.this);
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
                    positiveBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            final int index = holder.img_remove_item.getId();
                            list_ImageDescData.remove(index);
                            notifyDataSetChanged();
                            if (list_ImageDescData.size() < 1) {
                                Intent in = new Intent(Upload_image_and_cooment_New.this, MainActivity.class);
                                startActivity(in);
                                finishs();
                            }
                        }
                    });
                    negativeBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();

                        }
                    });
                    alertDialog = dialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();







/*                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Upload_image_and_cooment.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitleText("Remove this item?");
                    sweetAlertDialog.setContentText("Are you sure?");
                    sweetAlertDialog.setConfirmText("Yes");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();


                            final int index = holder.img_remove_item.getId();
                            list_ImageDescData.remove(index);
                            notifyDataSetChanged();
                            //To scroll list to 1st item after adding item to list
                            //         scroll_list_toTop();

                            if (list_ImageDescData.size() < 1) {
                                Intent in = new Intent(Upload_image_and_cooment.this, MainActivity.class);
                                startActivity(in);
                                finishs();
                            }

                        }
                    });
                    sweetAlertDialog.setCancelText("No");
                    sweetAlertDialog.showCancelButton(true);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();

                        }
                    })
                            .show();*/

                }
            });


            return convertView;
        }

    }

    public class ViewHolder {
        EditText et_Desc;
        Button index_no;
        ImageView thumbnail, img_remove_item;
        TextView imageName;
    }

    public class async_LinkUploadedFiles extends AsyncTask<String, Integer, String> {
        // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*   progressDialog = new ProgressDialog(Upload_image_and_cooment.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.show();*/

        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            for (int i = 0; i < list_ImageDescData.size(); i++) {

                String Jobid_or_swoid = list_ImageDescData.get(i).get(Utility.KEY_Jobid_or_swoid);
                String dataa = list_ImageDescData.get(i).get(Utility.KEY_dataa);
                String imagename = list_UploadImageName.get(i);//list_ImageDescData.get(i).get(Utility.KEY_imagename);
                String image_size = list_imageSize.get(i);
                String name = list_ImageDescData.get(i).get(Utility.KEY_name);
                String clid = list_ImageDescData.get(i).get(Utility.KEY_clid);
                String job_or_swo = list_ImageDescData.get(i).get(Utility.KEY_job_or_swo);

                String UploadedPhotoID = enterdata(Jobid_or_swoid, dataa, imagename, dataa, image_size,
                        name, clid, job_or_swo);
            }
            result = generate_event(list_ImageDescData.get(0).get(Utility.KEY_Jobid_or_swoid), list_ImageDescData.get(0).get(Utility.KEY_clid),
                    list_ImageDescData.get(0).get(Utility.KEY_job_or_swo), String.valueOf(list_ImageDescData.size()));


            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //  progressDialog.dismiss();

            for (int i = 0; i < list_TempImagePath.size(); i++) {
                String TempImagePath = list_TempImagePath.get(i);
                Utility.delete(TempImagePath);
            }

            list_TempImagePath.clear();
            list_path.clear();
            list_ImageDescData.clear();
            list_imageSize.clear();
            list_UploadImageName.clear();
            try {
                if (uploadProgressDialog.isShowing())
                    uploadProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }
            try {
                if (result.equalsIgnoreCase("1")) {
                    //  Toast.makeText(Upload_image_and_cooment.this, "Image(s) uploaded successfully!", Toast.LENGTH_SHORT).show();
                    dialog_sharePhotos();
                } else {
                    dialog_photo_upload_failed();
                }
            } catch (Exception e) {
                e.getMessage();
            }

        }

    }


}