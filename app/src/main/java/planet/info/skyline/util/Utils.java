package planet.info.skyline.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.old_activity.Click_Dialog;
import planet.info.skyline.tech.billable_timesheet.Clock_Submit_Type_Activity;

public class Utils {
    static Context context;
    static SharedPreferences sharedPreferences;
    static Intent inte;
    static String path;

    public static void openImageIntent(String fname, Uri outputFileUri, Activity activity, int requestCode) {

        // Determine Uri of camera image to save.
/*	final File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyDir" + File.separator);
	root.mkdirs();


		//String fname = Utils.createImageFile();

	final File sdImageMainDirectory = new File(root, fname);
	outputFileUri = Uri.fromFile(sdImageMainDirectory);*/

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = activity.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

        activity.startActivityForResult(chooserIntent, requestCode);
    }

    public static String createImageFile() {
        return "img_" + System.currentTimeMillis() + ".jpg";
    }


    ////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("deprecation")
    public static String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static Bitmap decodeFile(File f, int WIDTH, int HIGHT) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            if (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT) {
                scale *= 2;

                if (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT) {
                    scale *= 2;


                }

                if (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT) {
                    scale *= 2;


                }

            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public static void alert(final Context mContext, String message, String Title) {

				/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
				alertDialog.setTitle(Title);
		          alertDialog.setMessage(message);
		          alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog,int which) {
		            	  dialog.cancel();
		              }
		          });
		          // Showing Alert Message
		          alertDialog.show();*/


    }

    public static void alert_with_intent(final Activity mContext, String message, String Title, final Intent ii) {


        final Dialog showd = new Dialog(mContext);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.labourcode);
        showd.setCancelable(false);
        // showd.setTitle("");
        showd.show();
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        TextView textView1rr = (TextView) showd.findViewById(R.id.textView1rr);
        textView1rr.setText(Title);
        texrtdesc.setText(message);
        yesfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mContext.startActivity(ii);
                mContext.finish();
                showd.dismiss();

            }
        });

    }


    public static void alert_with_intent_withoutintent(final Activity mContext, String message, String Title, final String Client_name) {

        final Dialog showd = new Dialog(mContext);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.labourcode_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        showd.setCancelable(false);
        showd.show();
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        TextView textView1rr = (TextView) showd.findViewById(R.id.textView1rr);
        textView1rr.setText(Title);
        texrtdesc.setText(message);
        yesfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Client_name != "") {
                    Log.d("BHANU", "alert_with_intent_withoutintent");
                    Intent ii = new Intent(mContext, Clock_Submit_Type_Activity.class);
                    mContext.startActivity(ii);
                    mContext.finish();
                    showd.dismiss();
                } else {
                    Log.d("BHANU", "finish");
                    mContext.finish();
                    showd.dismiss();
                }

            }
        });

    }

    public static void alert_with_onlyshow(final Activity mContext, String message, String Title) {
        final Dialog showd = new Dialog(mContext);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.labourcode);
        showd.setCancelable(false);
        // showd.setTitle("");
        showd.show();
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        TextView textView1rr = (TextView) showd.findViewById(R.id.textView1rr);
        textView1rr.setText(Title);
        texrtdesc.setText(message);
        yesfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();

            }
        });

    }

    public static void alertwith_negative_click(final Context mContext, String message, String Title, final Click_Dialog listener) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(Title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                listener.positive_Click();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                listener.negative_Click();
            }
        });
        // Showing Alert Message
        alertDialog.show();

    }
			
		/*	public static void toast(String messege, Context context,Activity activity, int color){
				  Toast tempToast = new Toast(activity.getApplicationContext ( ));
			      tempToast.setDuration ( Toast.LENGTH_LONG );
			      tempToast.setGravity ( Gravity.BOTTOM | Gravity.CENTER
			                             , 0 , 0 );
			      LinearLayout layout = new LinearLayout ( activity ) ;
			      layout.setBackgroundColor (
			                   activity.getResources().getColor ( android.R.color.white ) );        
			      layout.setLayoutParams (
			              new LayoutParams ( LayoutParams.MATCH_PARENT ,
			                                 LayoutParams.MATCH_PARENT ) ) ;            
			      layout.setOrientation ( LinearLayout.HORIZONTAL ) ;            
			      TextView textView = new TextView(activity);
			      textView.setLayoutParams ( new LayoutParams (
			                LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT ) );
			      textView.setText (messege);
			      textView.setTextColor ( activity.getResources().getColor ( color ) );
			      textView.setTextSize(14);
			     // textView.setBackgroundColor (activity.getResources().getColor(R.color.yellow));
			      textView.setGravity ( Gravity.BOTTOM );
			      layout.addView(textView);
			      float[] values =  {8,8,8,8,8,8,8,8};
			      RectF rectf = new RectF();
			      rectf.set ( 2 , 2 , 2 , 2 );
			      ShapeDrawable drawable = new ShapeDrawable(
			                 new RoundRectShape (values,rectf,values) );
			      drawable.setPadding ( 10 , 10, 10 , 10 );
			      drawable.getPaint ( ).setColor (
			              activity.getApplicationContext ( ).getResources ( )
			              .getColor ( android.R.color.white ) );
			      drawable.getPaint ( ).setStyle ( Style.FILL_AND_STROKE );
			      layout.setBackgroundDrawable( drawable );
			      tempToast.setView ( layout ) ;
			      tempToast.show ( ) ;
				  }
		*/

    /**@author MIPC27
     * this one is the correct & tested one which was being used previously
     * but due to design change it is now changed to different i.e. next smaller custom layout toast.
     * @param messege : used for the message shown
     * @param activity : context
     */
			/*  public static void toast(final String messege,final Activity activity){
				  activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast tempToast = new Toast(activity);
					      tempToast.setDuration ( Toast.LENGTH_LONG );
					      tempToast.setGravity ( Gravity.BOTTOM 
					                             , 0 , 10 );
					      LinearLayout layout = new LinearLayout ( activity ) ;
					      layout.setBackgroundColor (
					                   activity.getResources().getColor ( android.R.color.black ) );        
					      layout.setLayoutParams (
					              new LayoutParams ( LayoutParams.MATCH_PARENT ,
					                                 LayoutParams.MATCH_PARENT ) ) ;            
					      layout.setOrientation ( LinearLayout.HORIZONTAL ) ;            
					      TextView textView = new TextView(activity);
					      textView.setLayoutParams ( new LayoutParams (
					                LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT ) );
					      textView.setText (messege);
					      textView.setTextColor ( activity.getResources().getColor (  android.R.color.white ) );
					      textView.setTextSize(14);
					     // textView.setBackgroundColor (activity.getResources().getColor(R.color.yellow));
					      textView.setGravity ( Gravity.BOTTOM );
					      layout.addView(textView);
					      float[] values =  {8,8,8,8,8,8,8,8};
					      RectF rectf = new RectF();
					      rectf.set ( 2 , 2 , 2 , 2 );
					      ShapeDrawable drawable = new ShapeDrawable(
					                 new RoundRectShape (values,rectf,values) );
					      drawable.setPadding ( 5 , 5, 5 , 5 );
					      drawable.getPaint ( ).setColor (
					              activity.getApplicationContext ( ).getResources ( )
					              .getColor ( android.R.color.black ) );
					      drawable.getPaint ( ).setStyle ( Style.FILL );
					      layout.setBackgroundDrawable( drawable );
					      tempToast.setView ( layout ) ;
					      tempToast.show ( ) ;
						
					}
				});
				  
				  }
				*/

    /**
     * this is used currently now
     *
     * @param messege
     * @param activity
     */
    public static void toast(final String messege, final Activity activity) {
        activity.runOnUiThread(new Runnable() {


            @Override
            public void run() {
                LayoutInflater inflater = activity.getLayoutInflater();

                View layout = inflater.inflate(R.layout.toast_custom,
                        (ViewGroup) activity.findViewById(R.id.custom_toast_layout_id));

                // set a message
                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(messege);

                // Toast...
                Toast toast = new Toast(activity);
                //   toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });
    }


    //////////////////////////////////////////////////////////////////////////

}
