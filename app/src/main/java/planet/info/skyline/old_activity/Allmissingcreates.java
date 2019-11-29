package planet.info.skyline.old_activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.adapter.Allmissingadapt;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.model.Allmissing;
import planet.info.skyline.network.Api;
import planet.info.skyline.tech.billable_timesheet.Clock_Submit_Type_Activity;
import planet.info.skyline.tech.shared_preference.Shared_Preference;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;


public class Allmissingcreates extends BaseActivity {
	private ProgressDialog pDialog;
	ListView listmissing;
	// String bimm="Confirm Crates in Bin ";
	String bimm = "Please confirm which Crates are in Bin ";
	private Allmissingadapt adapter;
	TextView textforbin;
	String selectcre = "", missicre = "", locationcreate = "",
			fromact = "real";

	private List<Allmissing> lisrt = new ArrayList<Allmissing>();
	String linkforallcreate =URL_EP1+ Api.API_CRATES_LIST +"1368";

	private TextView timerValue;

	private long startTime = 0L;


	private Handler customHandler = new Handler();

	long timeInMilliseconds = 0L;

	long timeSwapBuff = 0L;
	DisplayImageOptions options;
	long updatedTime = 0L;
	SharedPreferences sp;
	ImageView merchantname, missing;
	Editor ed;
	TextView clientnam, stopp;
	ImageView homeacti;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_allmissingcreates_new);
		listmissing = (ListView) findViewById(R.id.listView1);
		textforbin = (TextView) findViewById(R.id.textView1rr);
		Intent ii = getIntent();
		clientnam = (TextView) findViewById(R.id.textView1);
		timerValue = (TextView) findViewById(R.id.timer);


		sp = getApplicationContext().getSharedPreferences("skyline",
				MODE_PRIVATE);


		LocalBroadcastManager.getInstance(this).registerReceiver(
				mBroadcastReceiver, new IntentFilter("subtask"));
		ed = sp.edit();
		merchantname = (ImageView) findViewById(R.id.merchantname);
		merchantname.setImageResource(R.drawable.exhibitlogoa);
		missing = (ImageView) findViewById(R.id.missing);
		missing.setVisibility(View.VISIBLE);
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.skylinelogopng)

				.showImageForEmptyUri(R.drawable.skylinelogopng)
				.showImageOnFail(R.drawable.skylinelogopng).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		//
		stopp = (TextView) findViewById(R.id.createnotfound);

		//
	//	String imageloc = sp.getString("imglo", "");
		String imageloc =	Shared_Preference.getCLIENT_IMAGE_LOGO_URL(this);
		if (imageloc.equals("") || imageloc.equalsIgnoreCase("")) {
			//ed.putString("imglo", "").commit();
			Shared_Preference.setCLIENT_IMAGE_LOGO_URL(this,"");
			missing.setVisibility(View.GONE);
		} else {
			imageLoadery.displayImage(imageloc, missing, options);
		}
		// long hh= sp.getLong("times", 0);
		// startTime = SystemClock.uptimeMillis();
		// startTime=startTime-hh;
		// customHandler.postDelayed(updateTimerThread, 1000);
		//String nam = sp.getString("name", "");
		String nam  = Shared_Preference.getCLIENT_NAME(this);
		clientnam.setText(nam);
		String sggs = "";
		/*
		 * selectcre=ii.getStringExtra("selectcreate");
		 * missicre=ii.getStringExtra("missingcreate");
		 * locationcreate=ii.getStringExtra("alllocation");
		 * sggs=ii.getStringExtra("realid");
		 */
		try {
			fromact = ii.getStringExtra("active");
			if(fromact==null) fromact="";
			if (fromact.equalsIgnoreCase("home")) {
				selectcre = ii.getStringExtra("selectcreate");
				missicre = ii.getStringExtra("missingcreate");
				locationcreate = ii.getStringExtra("alllocation");
				sggs = ii.getStringExtra("realid");

			} else {

				locationcreate = ii.getStringExtra("alllocation");
				sggs = ii.getStringExtra("realid");

			}

		}catch (Exception e){
			e.getMessage();
		}

		//
		// String loccc=ii.getStringExtra("");
		//
		//

		homeacti = (ImageView) findViewById(R.id.homeacti);

		homeacti.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent ii = new Intent(Allmissingcreates.this,
						Clock_Submit_Type_Activity.class);

				startActivity(ii);
				// finish();

			}
		});
		try {
			stopp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Intent ii=new Intent(activity,Foundscaneract.class);
					Intent ii = new Intent(Allmissingcreates.this,
							Updatecreatelocation.class);
					// ii.putExtra("oldlocation", locationcreate);
					// ii.putExtra("oldlocation",locationcreate);
					String sba = locationcreate;
					// ed.putString("oldlocation", sba).commit();
					ii.putExtra("oldlocation", sba);
					ii.putExtra("case", "1");
					ii.putExtra("idi", "real");
					startActivity(ii);
				}
			});
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
		}
		//
		// linkforallcreate="http://exhibitpower.com/location_web_service.php?location="+locationcreate;
		if (fromact.equalsIgnoreCase("home")) {
			linkforallcreate =URL_EP1+Api.API_CRATE_LOCATION
					+ sggs;
		} else {
			linkforallcreate =URL_EP1+Api.API_FETCH_CRATE_LOCATION
					+ locationcreate;
		}
		// bimm=bimm+"# E01010";
		bimm = bimm + "#" + locationcreate;
		textforbin.setText(bimm);
		// linkforallcreate=ii.getStringExtra("missing");

		try {
			adapter = new Allmissingadapt(this, lisrt);
			listmissing.setAdapter(adapter);
		} catch (Exception e) {

		}
		// adapter=new Allmissingadapt(this,lisrt);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading");
		pDialog.setCancelable(false);
		pDialog.show();
		getjsonobject();
	}

	public void getjsonobject() {
		JsonObjectRequest bb = new JsonObjectRequest(Method.GET,
				linkforallcreate, null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject obj) {
						// TODO Auto-generated method stub
						parseJsonFeed(obj);
						hideprogressdialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						hideprogressdialog();

					}
				});

		AppController.getInstance().addToRequestQueue(bb);
	}

	public void showprogressdialog() {
		pDialog.show();
	}

	public void hideprogressdialog() {
		pDialog.dismiss();
	}

	private void parseJsonFeed(JSONObject response) {
		try {
			JSONArray feedArray = response.getJSONArray("cds");

			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);

				Allmissing item = new Allmissing();

				item.setCretateid(feedObj.getString("id"));
				item.setDescription(feedObj.getString("description"));
				item.setLocationcrate(feedObj.getString("cur_loacation"));
				item.setUnique_crate_id(feedObj.getString("uni_crates"));

				item.setName(feedObj.getString("name"));
				item.setSelectim(0);
				item.setMissim(0);
				item.setPos(i);

				lisrt.add(item);
			}

			// notify data changes to list adapater
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.allmissingcreates, menu);
		return true;
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String taskida = intent.getStringExtra("value");
			String type = intent.getStringExtra("type");// /type
			timerValue.setText(taskida);

		}
	};

}
