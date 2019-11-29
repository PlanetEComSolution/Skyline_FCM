package planet.info.skyline.old_activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.network.Api;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;


public class Foundscaneract extends Activity {
String updateloc="";
TextView et1;
TextView et2;
TextView et3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_foundscaneract);
		et1=(TextView)findViewById(R.id.editText1);
		et2=(TextView)findViewById(R.id.editText2);
		et3=(TextView)findViewById(R.id.editText3);
		Intent ii=getIntent();
		String ss=ii.getStringExtra("ss");
		 try {

			    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

			    startActivityForResult(intent, 1);

			} catch (Exception e) {

			    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.cliendroint.ad");
			    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
			    startActivity(marketIntent);

			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.foundscaneract, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			if(requestCode==1){
				String[] gg = null;
				String contents = data.getStringExtra("SCAN_RESULT");
				//205.204.76.234/update_location_web.php?id=524&LocationId=arvind
				if(contents.contains("loc=")){
				gg=contents.split("loc=");
				}
				String createid=gg[0];
				String locatSt=gg[1];
				
				updateloc= URL_EP1+ Api.API_CRATE_LOCATION_UPDATE+"524&LocationId=arvind";
				updatelocation(updateloc);
			}
		}
	}
	public void updatelocation(String links){
		JsonObjectRequest updatess=new JsonObjectRequest(Method.GET, updateloc,null,new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsono) {
				// TODO Auto-generated method stub
				updatelayout(jsono);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		AppController.getInstance().addToRequestQueue(updatess);
	}
	public void updatelayout(JSONObject jjo){
		JSONArray jj = null;
		try {
			jj = jjo.getJSONArray("cds");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<=jj.length();i++){
			try{
			JSONObject jobj=jj.getJSONObject(i);
			String cati=jobj.getString("id");
			String oldloc=jobj.getString("old_location");
			String newloc=jobj.getString("cur_loacation");
			et1.setText(newloc);
			et2.setText(cati);
			et3.setText(oldloc);
			}catch(Exception e){
				
			}
			
		}
		
		
	}

}
