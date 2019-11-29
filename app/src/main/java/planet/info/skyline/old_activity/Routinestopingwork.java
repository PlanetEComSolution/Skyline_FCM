package planet.info.skyline.old_activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.tech.billable_timesheet.Clock_Submit_Type_Activity;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_stop_tech_with_resion;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class Routinestopingwork extends BaseActivity {
	TextView timerValue, clientnam;
	ImageView merchantname, missing, homeacti;
	SharedPreferences sp;
	Editor ed;
	DisplayImageOptions options;
	EditText reason;
	ProgressDialog pDialog;
	String urlofwebservice = URL_EP2+"/WebService/techlogin_service.asmx?op=stop_tech_with_resion";
	int resultmy, check_gotocatch;
	String receivedString;
	String jobid, techid, reasonsstring;
	TextView gomissi, gobacktosca;
	String webhit = "";
	TextView textView1rr,routinetext,plslisttext;
	String stopaurpause="1";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_routinestopingwork);

		timerValue = (TextView) findViewById(R.id.timer);
			plslisttext= (TextView) findViewById(R.id.plslisttext);
			routinetext= (TextView) findViewById(R.id.routinetext);
			textView1rr= (TextView) findViewById(R.id.textView1rr);
		merchantname = (ImageView) findViewById(R.id.merchantname);
		reason = (EditText) findViewById(R.id.editText1);
		gomissi = (TextView) findViewById(R.id.gomissi);
		gobacktosca = (TextView) findViewById(R.id.gobacktosca);
		merchantname.setImageResource(R.drawable.exhibitlogoa);
		pDialog = new ProgressDialog(Routinestopingwork.this);
		pDialog.setMessage("Kindly wait");
		pDialog.setCancelable(false);
		clientnam = (TextView) findViewById(R.id.textView1);

		sp = getApplicationContext().getSharedPreferences("skyline",
				MODE_PRIVATE);
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mBroadcastReceiver, new IntentFilter("subtask"));
		ed = sp.edit();

		


Bundle ii=getIntent().getExtras();
		if(null==ii){
			
		}else{
		if(ii.containsKey("btn_PauseWork")){
			gomissi.setText("Pause work due to these Issues");

textView1rr.setText("Pause  Work");
routinetext.setText("Routine Pause of Work");
plslisttext.setText("Please list your reason for Pause work :");

		}
		else{
			
		}
		}
		missing = (ImageView) findViewById(R.id.missing);
		missing.setVisibility(View.VISIBLE);
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.skylinelogopng)

				.showImageForEmptyUri(R.drawable.skylinelogopng)
				.showImageOnFail(R.drawable.skylinelogopng).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		//String imageloc = sp.getString("imglo", "");
		String imageloc =	Shared_Preference.getCLIENT_IMAGE_LOGO_URL(this);

		//jobid = sp.getString("jobid", "");
		 jobid =   Shared_Preference.getSWO_ID(Routinestopingwork.this);
		//techid = sp.getString("clientid", "");
		techid= Shared_Preference.getLOGIN_USER_ID(Routinestopingwork.this);
		homeacti = (ImageView) findViewById(R.id.homeacti);

		homeacti.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent ii = new Intent(Routinestopingwork.this,
						Clock_Submit_Type_Activity.class);

				startActivity(ii);
				// finish();

			}
		});

		gomissi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				reasonsstring = reason.getText().toString();
				if (reasonsstring.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"please enter reaseons for stop work",
							Toast.LENGTH_SHORT).show();
				} else {



					if (new ConnectionDetector(Routinestopingwork.this).isConnectingToInternet()) {
						new asyntask().execute();//
					} else {
						Toast.makeText(Routinestopingwork.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
					}

				}
			}
		});
		gobacktosca.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		if (imageloc.equals("") || imageloc.equalsIgnoreCase("")) {
			//ed.putString("imglo", "").commit();
			Shared_Preference.setCLIENT_IMAGE_LOGO_URL(this,"");

			missing.setVisibility(View.GONE);
		} else {
			imageLoadery.displayImage(imageloc, missing, options);
		}
	//	String nam = sp.getString("name", "");
		String nam = Shared_Preference.getCLIENT_NAME(this);


	//	String names=sp.getString("client_name","");
		String names =  Shared_Preference.getCLIENT(this);
		Log.d("BHANU", names);
		if (names!="") {
			clientnam.setText(names);
			missing.setVisibility(View.GONE);
		} else {
			clientnam.setText(nam);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.routinestopingwork, menu);
		return true;
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

	public void showprogressdialog() {
		pDialog.show();
	}

	public void hideprogressdialog() {
		pDialog.dismiss();
	}

	public class asyntask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			showprogressdialog();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			hideprogressdialog();
			if (resultmy == 0) {
				Toast.makeText(getApplicationContext(),
						"please check your connection", Toast.LENGTH_LONG)
						.show();

			}
			else if (resultmy == 1)
			{

			} else if (resultmy == 3)
			{

			} else
			{

			}
			Intent iiu = new Intent(Routinestopingwork.this, MainActivity.class);
			iiu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			iiu.putExtra("urString","exit");
			startActivity(iiu);
			finish();
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub


if(gomissi.getText().toString().contains("Pause"))
{
	stopaurpause="0";
			}
else
			{
				stopaurpause="1";
			}


			stopwork();//
		//	String dd = sp.getString("jobid", "");
			String	dd =   Shared_Preference.getSWO_ID(Routinestopingwork.this);
			//String clientidme = sp.getString("clientid", "");
			String clientidme= Shared_Preference.getLOGIN_USER_ID(Routinestopingwork.this);
			// webhit="http://exhibitpower2.com/Register/auto_generate_event2.aspx?swo_id="+dd+"&status=stop";
			if(gomissi.getText().toString().contains("Pause")){
				webhit = URL_EP2+"/Register/auto_generate_event2.aspx?done_by="
						+ clientidme + "&swo_id=" + dd + "&status=btn_PauseWork";
			}
			else{
				webhit = URL_EP2+"/Register/auto_generate_event2.aspx?done_by="
						+ clientidme + "&swo_id=" + dd + "&status=stop";
			}

			getjsonobject1();
			//
			// add
			// startActivity(new Intent(Routinestopingwork.this,
			// SubmitTimesheet.class));
		/*	Intent ii = new Intent(Routinestopingwork.this, Timerclass.class);

			stopService(ii);*/

			return null;
		}

	}

	public void getjsonobject1() {
		JsonObjectRequest bb = new JsonObjectRequest(Method.GET, webhit, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject obj) {

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});

		AppController.getInstance().addToRequestQueue(bb);
	}

	public void stopwork() {

		check_gotocatch = 0;
		final String NAMESPACE =  KEY_NAMESPACE+"";
		final String URL = urlofwebservice;
		final String SOAP_ACTION =  KEY_NAMESPACE+API_stop_tech_with_resion;
		final String METHOD_NAME =API_stop_tech_with_resion;
		// Create SOAP request

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("swo_id", jobid);
		request.addProperty("doneby", techid);
		request.addProperty("region", reasonsstring);
		request.addProperty("status", stopaurpause);
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
						// name=json_obj.getString("name");
						// mob=json_obj.getString("mobile");
						// id=json_obj.getString("id");
						// ed.putString("tname", name).commit();
						// ed.putString("mob", mob).commit();
						// ed.putString("clientid", id).commit();
					} else if (resuSt.equals("1")
							|| resuSt.equalsIgnoreCase("1") || resuSt == "1") {
						resultmy = 1;
						// name=json_obj.getString("name");
						// mob="";//json_obj.getString("mobile");
						// id=json_obj.getString("id");
						// // ed.putString("name", name).commit();
						// // ed.putString("mob", name).commit();
						// ed.putString("clientid", name).commit();
					} else {
						// resultmy=2;
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

			}

			catch (Exception e) {
				check_gotocatch++;
				// Toast.makeText(getApplicationContext(),
				// "Error on loding data"+e.toString(),
				// Toast.LENGTH_LONG).show();
				resultmy = 3;
				e.printStackTrace();
			}

		}

	}

	public void showtoast() {
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
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 120);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
}
