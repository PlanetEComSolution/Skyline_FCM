package planet.info.skyline.floating_view;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager;
import planet.info.skyline.tech.billable_timesheet.Clock_Submit_Type_Activity;
import planet.info.skyline.tech.non_billable_timesheet.NonBillable_jobs;
import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;


/**
 * ChatHead Service
 */
public class ChatHeadService extends Service implements FloatingViewListener {

    /**
     * デバッグログ用のタグ
     */
    private static final String TAG = "ChatHeadService";

    /**
     * 通知ID
     */
    //  private static final int NOTIFICATION_ID = 9083150;
    /**
     * {@inheritDoc}
     */

    TextView tv_timer;
    // TextView tv_job;
    SharedPreferences sp;
   // ImageView close;
    LinearLayout ll_clock;
    View iconView;
    Handler handler;
    // private Handler customHandler = new Handler();
    Runnable myRunnable;
    /**
     * FloatingViewManager
     */
    private FloatingViewManager mFloatingViewManager;
    private ScreenStateReceiver mReceiver;

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            String Total_time = Utility.get_TotalClockTime(getApplicationContext());
            tv_timer.setText(Total_time);
            //  customHandler.postDelayed(this, 0);
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenStateReceiver();
        registerReceiver(mReceiver, intentFilter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 既にManagerが存在していたら何もしない
        if (mFloatingViewManager != null) {
            return START_STICKY;
        }

        ////


        SharedPreferences sp1 = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp1.edit();

        //////

        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);


///////////////////
     /*   int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams      params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

*/

       ////////////////////////////

        final LayoutInflater inflater = LayoutInflater.from(this);
        iconView = (View) inflater.inflate(R.layout.widget_chathead_with_cross, null);
//        iconView = (View) inflater.inflate(R.layout.clock_new, null);

        tv_timer = (TextView) iconView.findViewById(R.id.tv_timer);

//        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/digital_7.ttf");
//        tv_timer.setTypeface(typeface);

       // Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.myfont);
      //  tv_timer.setTypeface(typeface);

        // tv_job = (TextView) iconView.findViewById(R.id.tv_job);
      //  close = (ImageView) iconView.findViewById(R.id.close);
       // close.setVisibility(View.GONE);
        ll_clock = (LinearLayout) iconView.findViewById(R.id.ll_clock);

        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClockClick();
            }
        });
        tv_timer.setText("");

        startTimerThread();


        //nks
        mFloatingViewManager = new FloatingViewManager(this, this);
        mFloatingViewManager.setFixedTrashIconImage(R.drawable.ic_trash_fixed);
        mFloatingViewManager.setActionTrashIconImage(R.drawable.ic_trash_action);
        mFloatingViewManager.setTrashViewEnabled(false);//to disable 'drag to bottom to dismiss'
        mFloatingViewManager.setDisplayMode(FloatingViewManager.DISPLAY_MODE_SHOW_ALWAYS);

        final FloatingViewManager.Options options = new FloatingViewManager.Options();
        options.overMargin = (int) (0 * metrics.density);//nks-set here margin
        mFloatingViewManager.addViewToWindow(iconView, options);

        //  startForeground(Utility.NOTIFICATION_ID, createNotification());
        /*nks*/
        Utility.startNotification(ChatHeadService.this);
        /*nks*/
        return START_REDELIVER_INTENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        //nks
        stopTimerThread();
        unRegisterReceiver();
        //nks
        destroy();
        super.onDestroy();


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFinishFloatingView() {
        stopSelf();
        Log.d(TAG, getString(R.string.finish_deleted));


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTouchFinished(boolean isFinishing, int x, int y) {
        if (isFinishing) {
            Log.d(TAG, getString(R.string.deleted_soon));
        } else {
            Log.d(TAG, getString(R.string.touch_finished_position, x, y));
        }
    }

    /**
     * Viewを破棄します。
     */
    private void destroy() {
        if (mFloatingViewManager != null) {
            mFloatingViewManager.removeAllViewToWindow();
            mFloatingViewManager = null;
        }
    }



    private void onClockClick() {
        if (new ConnectionDetector((getApplicationContext())).isConnectingToInternet()) {
            try {
                boolean isTimerRunningFromAdminClockModule = Shared_Preference.getTIMER_STARTED_FROM_ADMIN_CLOCK_MODULE(this);
                if (isTimerRunningFromAdminClockModule) {
                    try {
                        Intent intent = new Intent(getApplicationContext(), NonBillable_jobs.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     //   intent.putExtra(Utility.FROM_CLOCK,true);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.getMessage();
                    }

                } else {
                    try {
                        Intent in = new Intent(getApplicationContext(), Clock_Submit_Type_Activity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getApplicationContext().startActivity(in);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            } catch (Exception e) {
                e.getMessage();
            }


        } else {
            Toast.makeText(getApplicationContext(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }


    }





    public void startTimerThread() {
        handler = new Handler();
        myRunnable = new Runnable() {
            public void run() {
                // do something
                String Total_time = Utility.get_TotalClockTime(getApplicationContext());
                tv_timer.setText(Total_time);
                Log.e(TAG, "Thread running: " + Total_time);
                handler.postDelayed(this, 1000);//1000 milli second=1 second
            }
        };
        handler.postDelayed(myRunnable, 0);
    }


    public void stopTimerThread() {
    try {
        handler.removeCallbacks(myRunnable);
        Log.e(TAG, "Thread Stopped: ");
    }catch (Exception e){
        e.getMessage();
        Log.e(TAG, "Error in Thread Stopping: ");
    }
    }

    public void unRegisterReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    public class ScreenStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                //code
                Log.d("Screen", "On");
                // startTimerThread();
                Utility.showChatHead(getApplicationContext());
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                //code
                Log.d("Screen", "Off");
                //  stopTimerThread();


                Utility.HideRunningClock(getApplicationContext());
            }
        }
    }
}
