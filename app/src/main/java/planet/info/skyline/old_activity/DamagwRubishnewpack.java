package planet.info.skyline.old_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONObject;

import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.tech.billable_timesheet.SubmitTimesheet;
import planet.info.skyline.tech.shared_preference.Shared_Preference;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class DamagwRubishnewpack extends BaseActivity {


DisplayImageOptions options;
SharedPreferences sp;
Editor ed;
ImageView merchantname;
TextView gobacktosca;
String webhit="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_damagw_rubishnewpack);
		gobacktosca=(TextView)findViewById(R.id.gobacktosca);

		sp=getApplicationContext().getSharedPreferences("skyline",MODE_PRIVATE);
			ed=sp.edit();
			merchantname=(ImageView)findViewById(R.id.merchantname);
	options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.skylinelogopng)
					
					.showImageForEmptyUri(R.drawable.skylinelogopng)
					.showImageOnFail(R.drawable.skylinelogopng)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();

	//String imageloc=sp.getString("imglo","");
		String imageloc =	Shared_Preference.getCLIENT_IMAGE_LOGO_URL(this);
	 imageLoadery.displayImage(imageloc, merchantname, options);
	 try{
		gobacktosca.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try{
			//	stopService(new Intent(DamagwRubishnewpack.this, Timerclass.class));
			//	String dd = sp.getString("jobid", "");
					String dd= Shared_Preference.getLOGIN_USER_ID(DamagwRubishnewpack.this);
		//	String clientidme = sp.getString("clientid", "");
					String clientidme =	Shared_Preference.getLOGIN_USER_ID(DamagwRubishnewpack.this);
				// webhit="http://exhibitpower2.com/Register/auto_generate_event2.aspx?swo_id="+dd+"&status=stop";
				webhit =  URL_EP2+"/Register/auto_generate_event2.aspx?done_by="
						+ clientidme + "&swo_id=" + dd + "&status=stop";
				getjsonobject1();
				// deviceofftime
			//	ed.putLong("deviceofftime", 0).commit();
				// Boolean bb = sp.getBoolean("billable", false);
				// if (bb) {
				// // callifworkwithnonbillable();
				// // dialogforshowingbillornonbill();
				// callifworkwithnonbillable();
				// } else {
				// finish();
				// }
				// finish();
				/////alertforsurework();
				startActivity(new Intent(DamagwRubishnewpack.this, SubmitTimesheet.class));
				finish();
			 }catch(Exception e){
				 
			 }
				
			}
		});
	 }catch(Exception e){
		 
	 }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.damagw_rubishnewpack, menu);
		return true;
	}

	public void getjsonobject1(){
		JsonObjectRequest bb=new JsonObjectRequest(Method.GET, webhit,null,new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject obj) {
				// TODO Auto-generated method stub
				
			
				//hideprogressdialog();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			//	hideprogressdialog();
				Log.e("api_error on mainactivity", arg0.toString());
				
			}
		});
	/*JsonObjectRequest jsonobj =new JsonObjectRequest(urlskyline, null, new Response.Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject arg0) {
			// TODO Auto-generated method stub
			
		}
	}, new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			
		}
	});	*/
		AppController.getInstance().addToRequestQueue(bb);
	}
}
