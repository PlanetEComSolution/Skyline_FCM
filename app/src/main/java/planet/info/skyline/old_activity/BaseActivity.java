package planet.info.skyline.old_activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;

import planet.info.skyline.home.MainActivity;
import planet.info.skyline.util.Utility;


/**
 * @author Harsha
 * 
 *         BaseActivity class extends Activity
 */
public abstract class BaseActivity extends AppCompatActivity {

	protected static final String TAG = BaseActivity.class.getName();

	/**/
	protected ImageLoader imageLoadery = ImageLoader.getInstance();
	/**/
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		/**/

	}

	public static boolean isAppWentToBg = false;

	public static boolean isWindowFocused = false;

	public static boolean isMenuOpened = false;

	public static boolean isBackPressed = false;

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart isAppWentToBg " + isAppWentToBg);

		applicationWillEnterForeground();

		super.onStart();
	}

	private void applicationWillEnterForeground() {
		if (isAppWentToBg) {
			isAppWentToBg = false;
			/*Toast.makeText(getApplicationContext(), "App is in foreground",
					Toast.LENGTH_SHORT).show();
*/

			Utility.showChatHead(getApplicationContext());

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();

		Log.d(TAG, "onStop ");
		applicationdidenterbackground();
	}

	public void applicationdidenterbackground() {
		if (!isWindowFocused) {
			isAppWentToBg = true;
			/*Toast.makeText(getApplicationContext(),
					"App is Going to Background", Toast.LENGTH_SHORT).show();*/

			if( !(Utility.isAppOnForeground(getApplicationContext()))){
				Utility.HideRunningClock(getApplicationContext());
			}

		}


	}


	@Override
	public void onBackPressed() {

		if (this instanceof MainActivity) {

		} else {
			isBackPressed = true;
		}

		Log.d(TAG,
				"onBackPressed " + isBackPressed + ""
						+ this.getLocalClassName());
		super.onBackPressed();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		isWindowFocused = hasFocus;

		if (isBackPressed && !hasFocus) {
			isBackPressed = false;
			isWindowFocused = true;
		}

		super.onWindowFocusChanged(hasFocus);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		/*case R.id.action_settings:
			Intent i = new Intent(this, SettingActivity.class);
			startActivity(i);
			break;*/
		}
		return true;
	}

	/*@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		*//*case R.id.action_settings:
			Intent i = new Intent(this, SettingActivity.class);
			startActivity(i);
			break;*//*
		}
		return true;
	}*/

}
