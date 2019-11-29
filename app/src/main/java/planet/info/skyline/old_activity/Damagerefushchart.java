package planet.info.skyline.old_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import planet.info.skyline.R;
import planet.info.skyline.tech.shared_preference.Shared_Preference;

public class Damagerefushchart extends BaseActivity {


DisplayImageOptions options;
//SharedPreferences sp;
//Editor ed;
ImageView merchantname,missing;
TextView clientname,timerValue;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_damagerefushchart);
		timerValue=(TextView)findViewById(R.id.timer);
	//	sp=getApplicationContext().getSharedPreferences("skyline",MODE_PRIVATE);
		//ed=sp.edit();
		 LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter("subtask"));
		 clientname=(TextView)findViewById(R.id.textView1);
			      //clientname.setText("");
		   
		// String nam=sp.getString("name","");
		String nam  = Shared_Preference.getCLIENT_NAME(this);

		 clientname.setText(nam);
		 missing=(ImageView)findViewById(R.id.missing);
		 missing.setVisibility(View.VISIBLE);

		merchantname=(ImageView)findViewById(R.id.merchantname);
		merchantname.setImageResource(R.drawable.exhibitlogoa);
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
 imageLoadery.displayImage(imageloc, missing, options);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.damagerefushchart, menu);
		return true;
	}
	 private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				//Toast.makeText(Subtaskpage.this, "sub task added", 5000).show();
				//adapter1.notifyDataSetChanged();
				String taskida=intent.getStringExtra("value");
				String type=intent.getStringExtra("type");///type
				 timerValue.setText(taskida);
//				if(type.equalsIgnoreCase("subtask")){
//				if(taskida.equals(Taskid))
//			      getlistsubtaskdata ();//
//				}
//				else if(type.equalsIgnoreCase("note")){
//					if(taskida.equals(Taskid))
//						getnotelistdata();
//				}
//				else{
//					
//				}
				
			}
		};
}
