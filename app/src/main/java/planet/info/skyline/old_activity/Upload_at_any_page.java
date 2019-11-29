package planet.info.skyline.old_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.network.Api;
import planet.info.skyline.tech.shared_preference.Shared_Preference;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;


public class Upload_at_any_page extends Activity {
	/*
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * setContentView(R.layout.activity_upload_at_any_page); } }
	 */

	List<HashMap<String, String>> listad = new ArrayList<HashMap<String, String>>();
	ListView listdatal;
	// EditText item1,item2,item3,item4;
	TextView gobacktosca;

	DisplayImageOptions options;
	SharedPreferences sp;
	Editor ed;
	Uri mImageCaptureUri;
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
	String wjobid, wjobidq, wimg, wuname, royaldesc, royaldescq, royalid;
	int resultmy, check_gotocatch;
	String itemtype = "";
	Boolean second = false;
	String urlofwebservice = "http://www.exhibitpower2.com/WebService/techlogin_service.asmx?op=send_po_and_job",
			receivedString, pmname = "", jobname = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_upload_at_any_page);
		opendilogforattachfileandimage();
	}

	public void opendilogforattachfileandimage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		file1 = new File(Environment.getExternalStorageDirectory(),
				String.valueOf(System.currentTimeMillis()) + "_FromCamera.jpg");
		Uri mImageCaptureUri = Uri.fromFile(file1);
		try {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent,
					AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
			// attachpicandfilesubtask.setImageResource(R.drawable.attachafter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * final Dialog dialog = new Dialog(Upload_at_any_page.this); //
		 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * dialog.setContentView(R.layout.openattachmentdilog);
		 * dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(Color.parseColor("#FFFFFF")));
		 * dialog.setTitle("Attach"); LinearLayout cameralayout = (LinearLayout)
		 * dialog .findViewById(R.id.cameralayout); LinearLayout gallarylayout =
		 * (LinearLayout) dialog .findViewById(R.id.gallarylayout); LinearLayout
		 * filelayout = (LinearLayout) dialog .findViewById(R.id.filelayout);
		 * 
		 * cameralayout.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { dialog.dismiss(); Intent
		 * intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); file1 = new
		 * File(Environment.getExternalStorageDirectory(),
		 * String.valueOf(System.currentTimeMillis()) + "_FromCamera.jpg"); Uri
		 * mImageCaptureUri = Uri.fromFile(file1); try {
		 * intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
		 * mImageCaptureUri); intent.putExtra("return-data", true);
		 * startActivityForResult(intent,
		 * AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE); //
		 * attachpicandfilesubtask.setImageResource(R.drawable.attachafter); }
		 * catch (Exception e) { e.printStackTrace(); }
		 * 
		 * } }); gallarylayout.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new Intent();
		 * intent.setType("image/*"); if (Build.VERSION.SDK_INT >= 19) {//
		 * Build.VERSION_CODES.KITKAT){
		 * 
		 * Intent aa = new Intent( Intent.ACTION_PICK,
		 * android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 * startActivityForResult( Intent.createChooser(aa,
		 * "Complete action using"),
		 * AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE); } else {
		 * intent.setAction(Intent.ACTION_GET_CONTENT);
		 * startActivityForResult(Intent.createChooser(intent,
		 * "Complete action using"),
		 * AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE); } dialog.dismiss();
		 * 
		 * } }); filelayout.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub dialog.dismiss(); Intent intent = new Intent();
		 * intent.setType("file/*");
		 * intent.setAction(Intent.ACTION_GET_CONTENT); startActivityForResult(
		 * Intent.createChooser(intent, "Complete action using"), 4);
		 * 
		 * } });
		 * 
		 * dialog.show(); dialog.setOnCancelListener(new
		 * DialogInterface.OnCancelListener() {
		 * 
		 * public void onCancel(DialogInterface dialog) { // finish(); } });
		 */
	}

	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		@SuppressWarnings("deprecation")
        Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
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
								Upload_at_any_page.this); // from Gallery
						// upload();
						// uploadFile(path);
						new asyntaskupload().execute();
					} catch (Exception e) {
						try {
							path = mImageCaptureUri.getPath();

							// upload();
							new asyntaskupload().execute();
							// uploadFile(path);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Log.i("check image  or not", e.toString());
					}
				} else {
					Uri mImageCaptureUri = Uri.fromFile(file1);
					try {
						path = getPath(mImageCaptureUri,
								Upload_at_any_page.this); // from Gallery
					} catch (Exception e) {
						path = mImageCaptureUri.getPath();
						Log.i("check image  or not", e.toString());
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
								Upload_at_any_page.this); // from Gallery
						// upload();
						// uploadFile(path);
						new asyntaskupload().execute();
					} catch (Exception e) {
						try {
							path = mImageCaptureUri.getPath();

							// upload();
							new asyntaskupload().execute();
							// uploadFile(path);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Log.i("check image  or not", e.toString());
					}
				} else {
					Uri mImageCaptureUri = Uri.fromFile(file1);
					try {
						path = getPath(mImageCaptureUri,
								Upload_at_any_page.this); // from Gallery
					} catch (Exception e) {
						path = mImageCaptureUri.getPath();
						Log.i("check image  or not", e.toString());
					}
					new asyntaskupload().execute();
					String arr[] = path.split("/");
					int i;
				}

			}
			//
		}

	}

	public void showprogressdialog() {
		pDialog.show();
	}

	public void hideprogressdialog() {
		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}
	}

	public class asyntaskupload extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			uploadFile(path);
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

				// HashMap<String, String> map=new HashMap<String, String>();
				// map.put("value", " ");
				// map.put("upload", "");
				// listad.add(map);
				// adapter.notifyDataSetChanged();
			}
			hideprogressdialog();
			// \
			try {
			//	String sfs = sp.getString("upload", "");
				String sfs =  Shared_Preference.getUPLOAD(Upload_at_any_page.this);
				String ds = Shared_Preference.getCrate_ID(Upload_at_any_page.this);
				String fr = ds;
				String[] arr = sfs.split("/");
				int le = arr.length;

				String dds = "";
				dds = arr[le - 1];
				dds =  URL_EP1+ Api.API_COLLATERAL_PATH + dds;

				wimg = dds;
			} catch (Exception e) {

			}
			super.onPostExecute(result);
		}

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
				URL url = new URL( URL_EP1+Api.API_UPLOAD);

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
							// Toast.makeText(Rubishcratedataupload.this,
							// fname+"File Upload Complete. ",
							// Toast.LENGTH_SHORT).show();

							//ed.putString("upload", fname).commit();
Shared_Preference.setUPLOAD(Upload_at_any_page.this,fname);
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
						Toast.makeText(Upload_at_any_page.this,
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
						Toast.makeText(Upload_at_any_page.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("  to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			// dialog.dismiss();
			return serverResponseCode;

		} // End else block

	}

}
