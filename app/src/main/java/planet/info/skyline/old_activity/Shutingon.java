package planet.info.skyline.old_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Shutingon extends BroadcastReceiver {

	SharedPreferences sp;
	Editor ed;
	long taskruntime;
	String deviceofftiem, currenttime;
	long dviceontimelong, currenttimelong, difference, deviceofftime;

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onReceive(Context context, Intent intent) {
//		Toast.makeText(context, "onadcsadfsadfsn", 50000).show();
//		try {
//			sp = context.getSharedPreferences("skyline",
//					context.MODE_WORLD_WRITEABLE);
//			ed = sp.edit();
//			taskruntime = sp.getLong("times", 0);
//			if ((taskruntime == 0)) {
//				return;
//			} else {
//				// for onn
//				// intent.
//				deviceofftime = sp.getLong("deviceofftime", 0);
//				currenttimelong = System.currentTimeMillis();
//				difference = currenttimelong - deviceofftime;
//				long tot = difference + taskruntime;
//
//				// ed.putLong("times", tot).commit(); only uncomment this line
//				// ed.putLong("deviceofftime", tot).commit();
//				// for off
//				// long tim= System.currentTimeMillis();
//				// ed.putLong("deviceofftime", tim).commit();
//			}
//
//		} catch (Exception e) {
//			Log.e("errorerrorerrorerrorerrorerrorerrorerrorerror", e.toString());
//		}
	}

}
