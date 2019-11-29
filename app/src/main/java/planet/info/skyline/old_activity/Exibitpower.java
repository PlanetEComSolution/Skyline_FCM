package planet.info.skyline.old_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import planet.info.skyline.R;
import planet.info.skyline.tech.locate_crates.LocateCrates;

public class Exibitpower extends Activity {
TextView t1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exibitpower);
		t1=(TextView)findViewById(R.id.textView1);
		t1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Exibitpower.this, LocateCrates.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exibitpower, menu);
		return true;
	}

}
