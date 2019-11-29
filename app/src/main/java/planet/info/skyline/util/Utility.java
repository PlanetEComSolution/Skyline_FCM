package planet.info.skyline.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import planet.info.skyline.R;
import planet.info.skyline.floating_view.ChatHeadService;
import planet.info.skyline.model.LaborCode;
import planet.info.skyline.model.OverlapTimesheet;
import planet.info.skyline.model.SavedTask;
import planet.info.skyline.network.Api;
import planet.info.skyline.tech.billable_timesheet.Clock_Submit_Type_Activity;
import planet.info.skyline.tech.non_billable_timesheet.NonBillable_jobs;
import planet.info.skyline.tech.shared_preference.Shared_Preference;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Admin on 2/20/2017.
 */

public class Utility {

    // public static final String TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE = "TimerStartedFromAdminClockModule";
    public static final String CLIENT_ID_FOR_NON_BILLABLE = "CLIENT_ID_FOR_NON_BILLABLE";
    public static final String IS_NON_BILLABLE_IN_FRONT = "IS_NONBILLABLE_IN_FRONT";
    public static final int OVERLAY_PERMISSION_REQUEST_CODE = 113;

    public static final String SHOW_JOB_FILE_NEW = "SHOW_JOB_FILE_NEW";
    public static final String KEY_CRATE_ID = "KEY_CRATE_ID";
    public static final String KEY_BUNDLE_ELEMENT_1 = "KEY_BUNDLE_ELEMENT_1";
    public static final String KEY_BUNDLE_ELEMENT_2 = "KEY_BUNDLE_ELEMENT_2";
    public static final String KEY_BUNDLE_GRAPHICS_1 = "KEY_BUNDLE_GRAPHICS_1";
    public static final String KEY_BUNDLE_GRAPHICS_2 = "KEY_BUNDLE_GRAPHICS_2";
    public static final String KEY_BUNDLE_CRATES_1 = "KEY_BUNDLE_CRATES_1";
    public static final String KEY_BUNDLE_CRATES_2 = "KEY_BUNDLE_CRATES_2";
    public static final String KEY_WHATSINSIDE_TAB_ID = "KEY_WHATSINSIDE_TAB_ID";

    //  public static final String KEY_PHOTO_UPLOAD_FROM_SAME_MODULE = "MODULE_PHOTO_UPLOAD";
    public static final String KEY_JOB_FILES_FROM_SAME_MODULE = "KEY_JOB_FILES_FROM_SAME_MODULE";
    public static final String KEY_Jobid_or_swoid = "Jobid_or_swoid";
    public static final String KEY_dataa = "dataa";
    public static final String KEY_image_size = "image_size";
    public static final String KEY_name = "name";
    public static final String KEY_clid = "clid";
    public static final String KEY_job_or_swo = "job_or_swo";
    public static final String KEY_imagename = "imagename";
    public static final String KEY_imagePath = "imagePath";
    public static final String IS_DANAGE_REFURBISH_IN_FRONT = "IS_DANAGE_REFURBISH_IN_FRONT";
    // public static final String TIMER_STARTED_FROM_BILLABLE_MODULE = "TimerStartedFromBillableModule";
    //public static final String PICTURE_FOR_ITEM = "PICTURE_FOR_ITEM";
    public static final String IMAGE_PATH = "IMAGE_PATH";
    public static final String ITEM_DESC = "ITEM_DESC";
    public static final String IS_CHECKLIST_DONE = "IS_CHECKLIST_DONE";
    public static final Integer REQUEST_CODE_CHECKLIST = 2;
    public static final Integer REQUEST_CODE_COMPLETED_DATE = 3;

    public static final String PAUSED_TIME = "PAUSED_TIME";

    //public static final String KEY_JOB_ID_FOR_JOBFILES = "KEY_JOB_ID_FOR_JOBFILES";
    // public static final String CLOCK_START_TIME = "CLOCK_START_TIME";

    public static final String KEY_IS_EMPTY = "IS_EMPTY";
    // public static final String TIMEGAP_JOB_START_TIME = "TIMEGAP_JOB_START_TIME";
    // public static final String TIMEGAP_JOB_END_TIME = "TIMEGAP_JOB_END_TIME";
    // public static final String TIMEGAP_PREV_JOB_START_TIME = "TIMEGAP_PREV_JOB_START_TIME";
    // public static final String STARTING_BILLABLE_JOB = "STARTING_BILLABLE_JOB";
    //  public static final String TIME_SHEET_ID = "TIME_SHEET_ID";
    public static final String NO_INTERNET = "Lost Internet!";
    public static final String MSG_OFFLINE_DATA_SAVE = "Do you want to save data offline? This will be submitted when Internet is available.";
    public static final String SAVED = "Saved successfully!";
    //  public static final String KEY_INCOMPLETE_ASYNC_ARRAY = "INCOMPLETE_ASYNC_ARRAY";
    public static final String KEY_OVERLAP_TIMESHEET_ARRAY = "OVERLAP_TIMESHEET_ARRAY";
    //public static final String KEY_APP_LAUNCHED_FIRST_TIME = "APP_LAUNCHED_FIRST_TIME";
    public static final String KEY_IS_LOGIN = "IS_LOGIN";
    public static final String KEY_APP_UPDATE_CHECKED = "APP_UPDATE_CHECKED";
    public static final String KEY_APP_LATEST_VERSION = "APP_LATEST_VERSION";
    public static final String KEY_APP_OLD_VERSION = "APP_OLD_VERSION";
    public static final String NON_BILLABLE = "Non-Billable";
    public static final String BILLABLE = "1";
    public static final String CHANGE_TIME_CODE = "2";
    public static final String KEY_SELECTED_SWO_STATUS = "SELECTED_SWO_STATUS";
    public static final String USER_ROLE_ARTIST = "6";
    public static final String USER_ROLE_APC = "5";
    public static final String USER_ROLE_TECH = "9";
    public static final int SWO_LIST_REQUEST_CODE = 24459;

    public static final String COMP_ID = "COMP_ID";
    public static final String JOB_ID = "JOB_ID";


    /*********************************CLIENT*******************************************/

    public static final String CLIENT_LOGIN_userID = "CLIENT_LOGIN_userID";
    public static final String CLIENT_LOGIN_txt_Mail = "CLIENT_LOGIN_txt_Mail";
    public static final String CLIENT_LOGIN_CompID = "CLIENT_LOGIN_CompID";
    public static final String CLIENT_LOGIN_CompName = "CLIENT_LOGIN_CompName";
    public static final String CLIENT_LOGIN_UserName = "CLIENT_LOGIN_UserName";
    public static final String CLIENT_LOGIN_UserCategory = "CLIENT_LOGIN_UserCategory";
    public static final String CLIENT_LOGIN_CaType = "CLIENT_LOGIN_CaType";
    public static final String CLIENT_LOGIN_DealerID = "CLIENT_LOGIN_DealerID";
    public static final String CLIENT_LOGIN_dtype = "CLIENT_LOGIN_dtype";
    public static final String CLIENT_LOGIN_Login_Email = "CLIENT_LOGIN_Login_Email";
    public static final String CLIENT_LOGIN_dealer_name = "CLIENT_LOGIN_dealer_name";
    public static final String CLIENT_LOGIN_status = "CLIENT_LOGIN_status";
    public static final String CLIENT_LOGIN_Imagepath = "CLIENT_LOGIN_Imagepath";
    public static final String CLIENT_LOGIN_Masterstatus = "CLIENT_LOGIN_Masterstatus";
    public static final String LOGIN_TYPE_CLIENT = "LOGIN_TYPE_CLIENT";
    public static final String LOGIN_TYPE_NORMAL = "LOGIN_TYPE_NORMAL";
    public static final String LOGIN_TYPE = "LOGIN_TYPE";


    public static final String OVERLAP_TIME_ENTRY = "OVERLAP_TIME_ENTRY";
    public static final String PAUSED_JOB = "PAUSED_JOB";
    public static final int CODE_SELECT_COMPANY = 124;
    public static final String KEY_PAUSED_TIMESHEET_ID = "PAUSED_TIMESHEET_ID";
    public static final String IS_JOB_MANDATORY = "IS_JOB_MANDATORY";
    public static final String Show_DIALOG_SHOW_INFO = "DIALOG_SHOW_INFO";
    public static final String IS_ADD = "IS_ADD";
    public static final String USAGE_CHARGE_ID = "USAGE_CHARGE_ID";
    public static final String Method_BillableTimeSheet = Api.API_BILLABLE_TIMESHEET;// Api.API_BILLABLE_TIMESHEET_Tech_Artist;//
    public static final String CLOCK_IN = "Clock In";
    public static final String CLOCK_OUT = "Clock Out";
    public static final String isFrom_PROJECT_FILE = "isFrom_PROJECT_FILE";
    public static final String Client_Mail = "Client_Mail";
    public static final int PROJECT_FILE_GET_CLIENT_MAILS = 222;

    public static final String FROM_CLOCK = "FROM_CLOCK";

    public static final String MISByDailyTime = "41";
    public static final String EditTime = "42";
    public static final String EnterTime = "65";
    public static final String ChangeOrder = "60";
    /**************************************************************************************/

    //Method

    // public static final String API_BILLABLE_TIMESHEET = "timesheet13_Dec";


    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;
    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";
    /**/


    private static final int NOTIFICATION_ID = 9083150;
    private static final String STR_DATE = "dd-MM-yyyy";
    private static final String STR_TIME = "HH:mm:ss";
    //public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static final String DATE_FORMAT = STR_DATE + " " + STR_TIME;
    public static String imgExt[] = {".JPG", ".jpg", ".jpeg", ".JPEG", ".png", ".PNG", ".bmp",
            ".BMP", ".gif", ".GIF", ".webp", ".WEBP"};
    public static String docExt[] = {".doc", ".DOC", ".psd", ".PSD", ".docx", ".PSD",
            ".docx", ".DOCX", ".pdf", ".PDF", ".xlsx", ".XLSX", ".pptx"};
    public static String mediaExt[] = {".aac", ".AAC", ".m4a", ".M4A",
            ".mp4", ".MP4", ".3gp", ".3GP", ".m4b", ".M4B", ".mp3", ".MP3",
            ".wave", ".WAVE"};
    public static String wordExt[] = {".doc", ".DOC", ".psd", ".PSD", ".docx", ".PSD",
            ".docx", ".DOCX"};
    public static String pdfExt[] = {".pdf", ".PDF"};
    public static String txtExt[] = {".txt", ".TXT"};
    public static String excelExt[] = {".xlsx", ".XLSX", ".xls", ".XLS"};

    /**/

    public static ArrayList<HashMap<String, String>> JSONEncoding(JSONArray result, ArrayList<String> listval) {
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<HashMap<String, String>> al1 = new ArrayList<>();
        try {
            JSONArray array = result;
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                HashMap<String, String> h = new HashMap<>();
                for (int icount = 0; icount < listval.size(); icount++) {
                    String servicename = row.getString(listval.get(icount));
                    h.put(listval.get(icount), row.getString(listval.get(icount)));
                }
                al1.add(h);

            }
        } catch (Exception e) {
            String msg = e.getMessage();
        }
        return al1;
    }

    public static String getUniqueId() {
        return System.currentTimeMillis() + "";
    }

    public static String addTime(String startTime, String endTime) {
        String date3 = "";
        try {
            String time1 = startTime;
            String time2 = endTime;

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date1 = timeFormat.parse(time1);
            Date date2 = timeFormat.parse(time2);

            long sum = date1.getTime() + date2.getTime();
            date3 = timeFormat.format(new Date(sum));
        } catch (Exception e) {
            e.getMessage();
        }
        return date3;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());

    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognise a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }

        return degree;
    }

    public static void StopRunningClock(Context context) {


        SharedPreferences sp;
        sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);

        try {
            Intent i2 = new Intent(context, ChatHeadService.class);
            context.stopService(i2);
        } catch (Exception e) {
            e.getMessage();
        }

        try {
            if (sp.contains(Shared_Preference.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE)) {
                // ed.putBoolean(Utility.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, false).apply();//nks
                Shared_Preference.setTIMER_STARTED_FROM_ADMIN_CLOCK_MODULE(context, false);
            }
            if (sp.contains(Shared_Preference.TIMER_STARTED_FROM_BILLABLE_MODULE)) {
                // ed.putBoolean(Shared_Preference.TIMER_STARTED_FROM_BILLABLE_MODULE, false).apply();//nks
                Shared_Preference.setTIMER_STARTED_FROM_BILLABLE_MODULE(context, false);


            }
        } catch (Exception e) {
            e.getMessage();
        }

        try {
            NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(Utility.NOTIFICATION_ID);
        } catch (Exception e) {
            e.getMessage();
        }


    }

    /* public static String getDealerID(Context context) {
         SharedPreferences.Editor ed;
         SharedPreferences sp;
         sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
         return sp.getString(Utility.DEALER_ID, "");
     }*/
    /*public static String getUserID(Context context) {
        SharedPreferences.Editor ed;
        SharedPreferences sp;
        sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        return sp.getString("clientid", "");
    }*/
    public static void HideRunningClock(Context context) {

        if (isMyServiceRunning(ChatHeadService.class, context)) {
            try {
                Intent i2 = new Intent(context, ChatHeadService.class);
                context.stopService(i2);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public static void startNotification(Context context) {
        // TODO Auto-generated method stub
        NotificationCompat.Builder notification;
        PendingIntent pIntent;
        NotificationManager manager;
        Intent resultIntent;
        TaskStackBuilder stackBuilder;


        boolean isClockRunningForBillableTime = checkClockRunningForBillable(context);
        Class className;
        String contentText = "Clock Running...";

        if (isClockRunningForBillableTime) {
            className = Clock_Submit_Type_Activity.class;
            contentText = "Clock Running for Billable Job";
        } else {
            className = NonBillable_jobs.class;
            contentText = "Clock Running for Admin Functions";
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            final String channelId = context.getResources().getString(R.string.default_notification_channel_id);
            final String channelName = "channel name";
            final NotificationChannel defaultChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MIN);
            manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(defaultChannel);
            }


            stackBuilder = TaskStackBuilder.create(context);
            /*  stackBuilder.addParentStack(Result.class);*/
            //Intent which is opened when notification is clicked
            resultIntent = new Intent(context, className);
            stackBuilder.addNextIntent(resultIntent);
            pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            Notification notification2 = new Notification.Builder(context)
                    .setContentTitle(context.getString(R.string.chathead_content_title))
                    .setContentText(contentText)
                    .setSmallIcon(R.drawable.exhibit_power_logo_white)
                    .setChannelId(channelId)
                    .setContentIntent(pIntent)
                    .setOngoing(true)
                    .build();

         /*   //Creating Notification Builder
            notification = new NotificationCompat.Builder(context);
            //Title for Notification
            notification.setContentTitle(context.getString(R.string.chathead_content_title));
            //Message in the Notification
            notification.setContentText(contentText);
            //Alert shown when Notification is received
            notification.setTicker(contentText);
            //Icon to be set on Notification
            notification.setSmallIcon(R.drawable.exhibit_power_logo_white);

            *//*nks*//*
             *//*  notification.setCategory(NotificationCompat.CATEGORY_SERVICE);
        notification.setPriority(NotificationCompat.PRIORITY_MIN);*//*
            notification.setOngoing(true);
            *//*nks*//*
            //Creating new Stack Builder
            stackBuilder = TaskStackBuilder.create(context);
            *//*  stackBuilder.addParentStack(Result.class);*//*
            //Intent which is opened when notification is clicked
            resultIntent = new Intent(context, className);
            stackBuilder.addNextIntent(resultIntent);
            pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pIntent);*/


            manager.notify(Utility.NOTIFICATION_ID, notification2);

        } else {
            //Creating Notification Builder
            notification = new NotificationCompat.Builder(context);
            //Title for Notification
            notification.setContentTitle(context.getString(R.string.chathead_content_title));
            //Message in the Notification
            notification.setContentText(contentText);
            //Alert shown when Notification is received
            notification.setTicker(contentText);
            //Icon to be set on Notification
            notification.setSmallIcon(R.drawable.exhibit_power_logo_white);

            /*nks*/
      /*  notification.setCategory(NotificationCompat.CATEGORY_SERVICE);
        notification.setPriority(NotificationCompat.PRIORITY_MIN);*/
            notification.setOngoing(true);
            /*nks*/
            //Creating new Stack Builder
            stackBuilder = TaskStackBuilder.create(context);
            /*  stackBuilder.addParentStack(Result.class);*/
            //Intent which is opened when notification is clicked
            resultIntent = new Intent(context, className);
            stackBuilder.addNextIntent(resultIntent);
            pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pIntent);
            manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(Utility.NOTIFICATION_ID, notification.build());

        }


    }

    public static boolean checkClockRunningForBillable(Context context) {

        boolean clockRunningForBillableTime = false;

        try {
            SharedPreferences sp;
            sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
            //boolean isTimerRunningFromAdminClockModule = sp.getBoolean(Utility.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, false);
            boolean isTimerRunningFromAdminClockModule = Shared_Preference.getTIMER_STARTED_FROM_ADMIN_CLOCK_MODULE(context);

            if (isTimerRunningFromAdminClockModule) {

                clockRunningForBillableTime = false;
            } else {

                clockRunningForBillableTime = true;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return clockRunningForBillableTime;
    }

    public static Bitmap getRotatedBitmap(String IMAGE_PATH, float orientation) {
        //get Bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_PATH, options);
        //rotate bitmap
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
        //create new rotated bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    public static String saveImage(Bitmap bmp) {
       /* FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/

        //////////

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, "Exhibit Power");

        if (!folder.exists()) {
            folder.mkdir();
        }
        File folder1 = new File(folder, "Camera");
        if (!folder1.exists()) {
            folder1.mkdir();
        }

        String unique_id = Utility.getUniqueId();

        File file = new File(folder1, unique_id + "_CameraImage.jpg");


        /////////////






       /* String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "TempImage-" + n + ".jpg";

        File file = new File(myDir, fname);*/
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 96, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    public static boolean delete(String path) {
        boolean deleted = false;
        File fdelete = new File(path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                deleted = true;
            } else {
                deleted = false;
            }
        }
        return deleted;
    }

    public static Date getCurrentTime() {
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat(DATE_FORMAT);
        String strDate = mdformat.format(calendar.getTime());

        try {
            date = mdformat.parse(strDate);
            //   System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;

    }

    public static String getCurrentTimeString() {
        String strDate = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat(DATE_FORMAT);
        strDate = mdformat.format(calendar.getTime());

        SimpleDateFormat mdformatTest = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        String strDateTest = mdformatTest.format(calendar.getTime());


        return strDate;

    }

    public static String getDateTimeDifference(Date startDate, Date endDate) {
        //milliseconds
        String time = "";
        try {
            long different = endDate.getTime() - startDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;


            String hrs, second, minute;

            if (elapsedSeconds < 10) {
                second = "0" + elapsedSeconds;
            } else {
                second = "" + elapsedSeconds;
            }
            if (elapsedMinutes < 10) {
                minute = "0" + elapsedMinutes;
            } else {
                minute = "" + elapsedMinutes;
            }
            if (elapsedHours < 10) {
                hrs = "0" + elapsedHours;
            } else {
                hrs = "" + elapsedHours;
            }


            time = hrs + ":" + minute + ":" + second;
        } catch (Exception e) {
            e.getMessage();
        }

        return time;
    }

    public static long getDateTimeDifference_seconds(Date startDate, Date endDate) {
        //milliseconds
        long elapsedSeconds = 0;
        try {
            long different = endDate.getTime() - startDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

          /*  long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;*/

            elapsedSeconds = different / secondsInMilli;


        } catch (Exception e) {
            e.getMessage();
        }

        return elapsedSeconds;
    }

    public static String get_TotalClockTime(Context context) {
        Date CurrentTime = Utility.getCurrentTime();
        SharedPreferences sp;
        sp = context.getSharedPreferences("skyline", MODE_PRIVATE);
        // String sTime = sp.getString(Utility.CLOCK_START_TIME, "");

        String sTime = Shared_Preference.getCLOCK_START_TIME(context);

        SimpleDateFormat mdformat = new SimpleDateFormat(Utility.DATE_FORMAT);
        Date startTime = null;
        try {
            startTime = mdformat.parse(sTime);
        } catch (Exception e) {
            e.getMessage();
        }
        String Total_time = Utility.getDateTimeDifference(startTime, CurrentTime);
        return Total_time;
    }

    public static void ResetClock(Context context) {
        String sDate = Utility.getCurrentTimeString();
        SharedPreferences sp;
        sp = context.getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        //  ed.putString(Utility.CLOCK_START_TIME, sDate).commit();
        Shared_Preference.setCLOCK_START_TIME(context, sDate);
    }


    public static String minus_seconds_fromCurrentTime(String DateTime, int seconds) {
        String strDate;

        //   String currentTime= DateTime;//"2017-10-19 22:00:00";
        SimpleDateFormat mdformat = new SimpleDateFormat(Utility.DATE_FORMAT);
        Date startTime = null;
        try {
            startTime = mdformat.parse(DateTime);
        } catch (Exception e) {
            e.getMessage();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        cal.add(Calendar.SECOND, -seconds);
        strDate = mdformat.format(cal.getTime());
        return strDate;
    }

    public static String add_seconds_toCurrentTime(String DateTime, int seconds) {
        String strDate;

        //   String currentTime= DateTime;//"2017-10-19 22:00:00";
        SimpleDateFormat mdformat = new SimpleDateFormat(Utility.DATE_FORMAT);
        Date startTime = null;
        try {
            startTime = mdformat.parse(DateTime);
        } catch (Exception e) {
            e.getMessage();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        cal.add(Calendar.SECOND, seconds);
        strDate = mdformat.format(cal.getTime());
        return strDate;
    }

    public static boolean IsTimeGapIssue(Context context) {

        boolean val = false;

        SharedPreferences sp = context.getSharedPreferences("skyline", MODE_PRIVATE);
        //  String Str_PrevJobEndTime = sp.getString(Utility.TIMEGAP_JOB_END_TIME, "");
        String Str_PrevJobEndTime = Shared_Preference.getTIMEGAP_JOB_END_TIME(context);


        //String Str_NewJobStartTime = sp.getString(Utility.TIMEGAP_JOB_START_TIME, "");
        String Str_NewJobStartTime = Shared_Preference.getTIMEGAP_JOB_START_TIME(context);//dd-MM-yyyy HH:mm:ss

        SimpleDateFormat mdformat = new SimpleDateFormat(Utility.DATE_FORMAT);
        Date PrevJobEndTime = null;
        Date NewJobStartTime = null;
        try {
            PrevJobEndTime = mdformat.parse(Str_PrevJobEndTime);
            NewJobStartTime = mdformat.parse(Str_NewJobStartTime);
        } catch (Exception e) {
            e.getMessage();
        }

        if (PrevJobEndTime == null || NewJobStartTime == null) {
            val = false;
        } else {
            long Total_Elapsed_Seconds = Utility.getDateTimeDifference_seconds(PrevJobEndTime, NewJobStartTime);
            if (Total_Elapsed_Seconds <= (7 * 60) + 30) {  //7 min =(7*60) sec
                val = true;
            } else {
                val = false;
            }
        }
        return val;
    }

    public static long GetTimeGap(Context context) {
        long Total_Elapsed_Seconds = 0;

        SharedPreferences sp = context.getSharedPreferences("skyline", MODE_PRIVATE);
        // String Str_PrevJobEndTime = sp.getString(Utility.TIMEGAP_JOB_END_TIME, "");
        String Str_PrevJobEndTime = Shared_Preference.getTIMEGAP_JOB_END_TIME(context);


        //String Str_NewJobStartTime = sp.getString(Utility.TIMEGAP_JOB_START_TIME, "");
        String Str_NewJobStartTime = Shared_Preference.getTIMEGAP_JOB_START_TIME(context);//dd-MM-yyyy HH:mm:ss
        SimpleDateFormat mdformat = new SimpleDateFormat(Utility.DATE_FORMAT);
        Date PrevJobEndTime = null;
        Date NewJobStartTime = null;
        try {
            PrevJobEndTime = mdformat.parse(Str_PrevJobEndTime);
            NewJobStartTime = mdformat.parse(Str_NewJobStartTime);
        } catch (Exception e) {
            e.getMessage();
        }

        if (PrevJobEndTime == null || NewJobStartTime == null) {
            Total_Elapsed_Seconds = 0;
        } else {
            Total_Elapsed_Seconds = Utility.getDateTimeDifference_seconds(PrevJobEndTime, NewJobStartTime);

        }
        return Total_Elapsed_Seconds;


    }

    public static String TotalTimeDifference(String time1, String time2) {
        String time = "";

        //  String time1 = "16:06";       //HH:mm
        //  String time2 = "00:07";      //HH:mm
        try {

            String ar[] = time1.split(":");
            int minutes1 = (Integer.parseInt(ar[0])) * 60 + (Integer.parseInt(ar[1]));

            String ar1[] = time2.split(":");
            int minutes2 = (Integer.parseInt(ar1[0])) * 60 + (Integer.parseInt(ar1[1]));
            int Total_minutes = minutes1 - minutes2;

            int hrs = Total_minutes / 60;
            int minuts = Total_minutes % 60;

            if (minuts < 10) {
                time = String.valueOf(hrs) + ":" + "0" + String.valueOf(minuts);
            } else {
                time = String.valueOf(hrs) + ":" + String.valueOf(minuts);
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return time;
    }

    public static String TotalTimeAdd(String time1, String time2) {

        String time = "";

        //  String time1 = "16:06";       //HH:mm
        //   String time2 = "00:07";      //HH:mm
        try {

            String ar[] = time1.split(":");
            int minutes1 = (Integer.parseInt(ar[0])) * 60 + (Integer.parseInt(ar[1]));

            String ar1[] = time2.split(":");
            int minutes2 = (Integer.parseInt(ar1[0])) * 60 + (Integer.parseInt(ar1[1]));
            int Total_minutes = minutes1 + minutes2;

            int hrs = Total_minutes / 60;
            int minuts = Total_minutes % 60;

            if (minuts < 10) {
                time = String.valueOf(hrs) + ":" + "0" + String.valueOf(minuts);
            } else {
                time = String.valueOf(hrs) + ":" + String.valueOf(minuts);
            }


        } catch (Exception e) {
            e.getMessage();
        }
        return time;
    }

    public static boolean IsSameDay(String firstDateTime, String SecondDateTime) {
        boolean val = false;

        String ar1[] = firstDateTime.split(" ");
        String date1 = ar1[0];

        String ar2[] = SecondDateTime.split(" ");
        String date2 = ar2[0];

        if (date1.equals(date2)) {
            val = true;
        }

        return val;


    }

    @SuppressLint("NewApi")
    public static void showChatHead(Context context) {
        // API22以下かチェック
        SharedPreferences sp;
        sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);


        try {
            boolean is_TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE = false;
            boolean is_TIMER_STARTED_FROM_BILLABLE_MODULE = false;
            if (sp.contains(Shared_Preference.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE)) {
                // is_TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE = sp.getBoolean(Shared_Preference.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, false);

                is_TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE = Shared_Preference.getTIMER_STARTED_FROM_ADMIN_CLOCK_MODULE(context);

            }
            if (sp.contains(Shared_Preference.TIMER_STARTED_FROM_BILLABLE_MODULE)) {
                //  is_TIMER_STARTED_FROM_BILLABLE_MODULE = sp.getBoolean(Utility.TIMER_STARTED_FROM_BILLABLE_MODULE, false);
                is_TIMER_STARTED_FROM_BILLABLE_MODULE = Shared_Preference.getTIMER_STARTED_FROM_BILLABLE_MODULE(context);
            }

            if (is_TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE || is_TIMER_STARTED_FROM_BILLABLE_MODULE) {
                try {

                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (!isMyServiceRunning(ChatHeadService.class, context)) {
                            context.startService(new Intent(context, ChatHeadService.class));
                        }
                        return;
                    }
                    // 他のアプリの上に表示できるかチェック
                    if (Settings.canDrawOverlays(context)) {
                        if (!isMyServiceRunning(ChatHeadService.class, context)) {
                            Intent intent = new Intent(context, ChatHeadService.class);
                            context.startService(intent);
                        }
                        // context.startService(new Intent(context, ChatHeadService.class));
                        return;
                    }

                    // オーバレイパーミッションの表示
                } catch (Exception e) {
                    e.getMessage();
                }


            }


        } catch (Exception e) {
            e.getMessage();
        }


    }

    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                //                Log.e("app",appPackageName);
                return true;
            }
        }
        return false;
    }

    public static boolean isWiFiConnected(Context context) {
        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = connManager.getActiveNetworkInfo();
        boolean isWifi = current != null && current.getType() == ConnectivityManager.TYPE_WIFI;
        return isWifi;
    }

    public static void saveOfflineIncompleteAsynctask(Context context, String api_input, String unique_apiId, String JobType) {
        JSONArray jsonArray = new JSONArray();
        JSONObject final_jsonObject = new JSONObject();


        //   timesheetNonBillable{tech_id=28; jobId=605; start_time=11:04; end_time=11:05; description=yyyy; code=21; region=yyyy  (00:01:19); status=3; }

        String api_name = api_input.substring(0, api_input.indexOf("{"));
        String temp = api_input.substring(api_input.indexOf("{") + 1, api_input.indexOf("}"));
        String input_arr[] = temp.split(";");
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < input_arr.length; i++) {
            String st = input_arr[i];
            if (st.contains("=")) {
                String req_ar[] = st.split("=");
                try {
                    jsonObject.put(req_ar[0].trim(), req_ar[1].trim());
                } catch (Exception e) {
                    e.getCause();
                }
            }
        }
        try {
            final_jsonObject.put("apiId", unique_apiId);
            final_jsonObject.put("api_name", api_name);
            final_jsonObject.put("job_type", JobType);
            final_jsonObject.put("apiInput", jsonObject);
        } catch (Exception e) {
            e.getCause();
        }

        if (Shared_Preference.containsKey(context, Shared_Preference.KEY_INCOMPLETE_ASYNC_ARRAY)) {
            String IncompleteAsyncArray = Shared_Preference.getINCOMPLETE_ASYNC_ARRAY(context);
            try {
                jsonArray = new JSONArray(IncompleteAsyncArray);
                jsonArray.put(final_jsonObject);
            } catch (Exception e) {
                e.getCause();
            }
            Shared_Preference.setINCOMPLETE_ASYNC_ARRAY(context, jsonArray.toString());

        } else {
            jsonArray.put(final_jsonObject);
            Shared_Preference.setINCOMPLETE_ASYNC_ARRAY(context, jsonArray.toString());
        }

        Log.e("AsyncArrAfterAdd", jsonArray.toString());

    }

    public static void removeIncompleteAsynctask(Context context, String unique_apiId) {
        JSONArray jsonArray = new JSONArray();

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();


        if (sp.contains(Shared_Preference.KEY_INCOMPLETE_ASYNC_ARRAY)) {
            //  String IncompleteAsyncArray = sp.getString(KEY_INCOMPLETE_ASYNC_ARRAY, "");
            String IncompleteAsyncArray = Shared_Preference.getINCOMPLETE_ASYNC_ARRAY(context);
            try {
                jsonArray = new JSONArray(IncompleteAsyncArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String str_apiId = jsonObject1.getString("apiId");
                    if (str_apiId.equals(unique_apiId)) {
                        jsonArray.remove(i);
                        break;
                    }

                }

            } catch (Exception e) {
                e.getCause();
            }
            // ed.putString(KEY_INCOMPLETE_ASYNC_ARRAY, jsonArray.toString()).apply();
            Shared_Preference.setINCOMPLETE_ASYNC_ARRAY(context, jsonArray.toString());
        }
        Log.e("AsyncArrAfterRemove", jsonArray.toString());
    }


    public static ArrayList<SavedTask> getOfflineTaskList(Context context) {

        ArrayList<SavedTask> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        if (Shared_Preference.containsKey(context, Shared_Preference.KEY_INCOMPLETE_ASYNC_ARRAY)) {
            // String IncompleteAsyncArray = sp.getString(Utility.KEY_INCOMPLETE_ASYNC_ARRAY, "");
            String IncompleteAsyncArray = Shared_Preference.getINCOMPLETE_ASYNC_ARRAY(context);
            try {
                jsonArray = new JSONArray(IncompleteAsyncArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String str_apiId = jsonObject1.getString("apiId");
                    String api_name = jsonObject1.getString("api_name");
                    String job_type = jsonObject1.getString("job_type");
                    JSONObject jsonObject = jsonObject1.getJSONObject("apiInput");
                    String start_time = jsonObject.getString("start_time");
                    String end_time = jsonObject.getString("end_time");
                    String description = jsonObject.getString("description");

                    list.add(new SavedTask(job_type, start_time + " - " + end_time, description));

                }

            } catch (Exception e) {
                e.getCause();
            }
            Log.e("AsyncArrAfterAdd", list.toString());

        }
        return list;
    }


    public static void saveOverlapTimeSheetData(Context context, String jobId, String start_time, String end_time, String description, String code, String region, String status, String dayInfo, String jobType, String jobDesc, String Swo_Status, String JOB_ID_BILLABLE, String PausedTimeSheetId) {
        JSONArray jsonArray = new JSONArray();
        //  JSONObject final_jsonObject = new JSONObject();

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("uid", Utility.getUniqueId());
            jsonObject.put("jobId", jobId);
            jsonObject.put("start_time", start_time);
            jsonObject.put("end_time", end_time);
            jsonObject.put("description", description);
            jsonObject.put("code", code);
            jsonObject.put("region", region);
            jsonObject.put("status", status);
            jsonObject.put("dayInfo", dayInfo);
            jsonObject.put("jobType", jobType);
            jsonObject.put("jobDesc", jobDesc);
            jsonObject.put("Swo_Status", Swo_Status);
            jsonObject.put("jobIdBillable", JOB_ID_BILLABLE);
            jsonObject.put("pausedTimeSheetId", PausedTimeSheetId);
        } catch (Exception e) {
            e.getCause();
        }
        try {
            //   final_jsonObject.put("overlap", jsonObject);
        } catch (Exception e1) {
            e1.getCause();
        }

        if (sp.contains(KEY_OVERLAP_TIMESHEET_ARRAY)) {
            String IncompleteAsyncArray = sp.getString(KEY_OVERLAP_TIMESHEET_ARRAY, "");
            try {
                jsonArray = new JSONArray(IncompleteAsyncArray);
                jsonArray.put(jsonObject);
            } catch (Exception e1) {
                e1.getCause();
            }
            ed.putString(KEY_OVERLAP_TIMESHEET_ARRAY, jsonArray.toString()).commit();
        } else {
            jsonArray.put(jsonObject);
            ed.putString(KEY_OVERLAP_TIMESHEET_ARRAY, jsonArray.toString()).commit();
        }

        Log.e("OverlapArrAfterAdd", jsonArray.toString());
    }

    public static ArrayList<OverlapTimesheet> getOverlapTimesheetDataList(Context context) {

        ArrayList<OverlapTimesheet> list = new ArrayList<>();


        JSONArray jsonArray = new JSONArray();
        SharedPreferences sp = context.getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        if (sp.contains(Utility.KEY_OVERLAP_TIMESHEET_ARRAY)) {
            String IncompleteAsyncArray = sp.getString(Utility.KEY_OVERLAP_TIMESHEET_ARRAY, "");
            try {
                jsonArray = new JSONArray(IncompleteAsyncArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String uid = jsonObject1.getString("uid");
                    String jobId = jsonObject1.getString("jobId");
                    String start_time = jsonObject1.getString("start_time");
                    String end_time = jsonObject1.getString("end_time");
                    String description = jsonObject1.getString("description");
                    String code = jsonObject1.getString("code");
                    String region = jsonObject1.getString("region");
                    String status = jsonObject1.getString("status");
                    String dayInfo = jsonObject1.getString("dayInfo");
                    String jobType = jsonObject1.getString("jobType");
                    String jobDesc = jsonObject1.getString("jobDesc");
                    String Swo_Status = jsonObject1.getString("Swo_Status");
                    String jobIdBillable = jsonObject1.getString("jobIdBillable");
                    String pausedTimeSheetId = jsonObject1.getString("pausedTimeSheetId");

                    list.add(new OverlapTimesheet(uid, jobId, start_time, end_time, description,
                            code, region, status, dayInfo, jobType, jobDesc, Swo_Status, jobIdBillable, pausedTimeSheetId));


                }

            } catch (Exception e) {
                e.getCause();
            }
            Log.e("OVERLAP_TIMESHEET_ARRAY", list.toString());

        }
        return list;
    }

    public static void removeOverlapTimesheetData(Context context, String unique_apiId) {
        JSONArray jsonArray = new JSONArray();

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        if (sp.contains(KEY_OVERLAP_TIMESHEET_ARRAY)) {
            String IncompleteAsyncArray = sp.getString(KEY_OVERLAP_TIMESHEET_ARRAY, "");
            try {
                jsonArray = new JSONArray(IncompleteAsyncArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String str_apiId = jsonObject1.getString("uid");
                    if (str_apiId.equals(unique_apiId)) {
                        jsonArray.remove(i);
                        break;
                    }

                }

            } catch (Exception e) {
                e.getCause();
            }
            ed.putString(KEY_OVERLAP_TIMESHEET_ARRAY, jsonArray.toString()).apply();
        }
        Log.e("overlapArrAfterRemove", jsonArray.toString());
    }

    public static void removeAllOverlapTimesheetData(Context context) {
        JSONArray jsonArray = new JSONArray();
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.remove(KEY_OVERLAP_TIMESHEET_ARRAY).apply();
        Log.e("overlapArrAfterRemove", jsonArray.toString());
    }

    public static boolean isOverlapTimesheetExist(Context context) {

        boolean isOverlapEntryExist = false;


        JSONArray jsonArray = new JSONArray();

        SharedPreferences sp = context.getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        if (Shared_Preference.containsKey(context, Utility.KEY_OVERLAP_TIMESHEET_ARRAY)) {
            String IncompleteAsyncArray = sp.getString(Utility.KEY_OVERLAP_TIMESHEET_ARRAY, "");
            try {
                jsonArray = new JSONArray(IncompleteAsyncArray);
                if (jsonArray.length() > 0) {
                    isOverlapEntryExist = true;
                }

            } catch (Exception e) {
                e.getCause();
            }

        }
        return isOverlapEntryExist;
    }


    public static void setAppLaunchedFirstTime(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        //  ed.putBoolean(Utility.KEY_APP_LAUNCHED_FIRST_TIME, true).commit();//nks
        Shared_Preference.setAPP_LAUNCHED_FIRST_TIME(context, true);

    }

    public static void setAppNotLaunchedFirstTime(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        //    ed.putBoolean(Utility.KEY_APP_LAUNCHED_FIRST_TIME, false).commit();//nks
        Shared_Preference.setAPP_LAUNCHED_FIRST_TIME(context, false);

    }

    public static void setAppUpdateChecked(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(Utility.KEY_APP_UPDATE_CHECKED, true).apply();//nks
    }

    public static void setAppUpdateNotChecked(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(Utility.KEY_APP_UPDATE_CHECKED, false).apply();//nks
    }

    public static void setLatestVersion(Context context, String ver) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(Utility.KEY_APP_LATEST_VERSION, ver).apply();//nks
    }


    public static void setOldVersion(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(Utility.KEY_APP_OLD_VERSION, getAppVersion(context)).apply();//nks
    }

    public static String getOldVersion(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        if (sp.contains(Utility.KEY_APP_OLD_VERSION)) {
            return sp.getString(Utility.KEY_APP_OLD_VERSION, "0");
        } else return "0";
    }


    public static String getLatestVersion(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        if (sp.contains(Utility.KEY_APP_LATEST_VERSION)) {
            return sp.getString(Utility.KEY_APP_LATEST_VERSION, "0");
        } else return "0";
    }


    public static String getAppVersion(Context context) {
        String version = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            e.getMessage();
        }
        return version;
    }


    public static boolean isAppUpdated(Context context) {

        boolean appUpdated = false;

        if (Utility.isAppUpdateChecked(context)) {
            Utility.setAppUpdateNotChecked(context);
            String version = Utility.getAppVersion(context);
            String LatestVersion = Utility.getLatestVersion(context);
            // if (true) {
            if (LatestVersion.equals(version)) {
                appUpdated = true;
            }
        }

        return appUpdated;

    }

    public static boolean isAppUpdateChecked(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        boolean checkedForUpdate = false;
        if (sp.contains(Utility.KEY_APP_UPDATE_CHECKED)) {
            checkedForUpdate = sp.getBoolean(Utility.KEY_APP_UPDATE_CHECKED, false);
        } else {
            checkedForUpdate = false;
        }


        return checkedForUpdate;

    }

    public static boolean isAppLaunchedFirstTime(Context context) {
        // SharedPreferences sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        //  sp.getBoolean(Utility.KEY_APP_LAUNCHED_FIRST_TIME, false);
        return Shared_Preference.getAPP_LAUNCHED_FIRST_TIME(context);

    }


    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.getCause();
        }


    }

    public static void showKeyboard(Activity activity, EditText editText) {

        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(editText.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);


    }

    public static void isKeyboardVisible(final View view) {

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if ((view.getRootView().getHeight() - view.getHeight()) >
                        view.getRootView().getHeight() / 3) {

                    // keyboard is open

                } else {

                    // keyboard is closed

                }
            }
        });
    }

    public static boolean isLogin(Context context) {
        SharedPreferences sp;
        sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        boolean isUserLogin = sp.getBoolean(KEY_IS_LOGIN, false);
        return isUserLogin;
    }

    public static void setLoginTrue(Context context, String Login_Type) {
        SharedPreferences.Editor ed;
        SharedPreferences sp;
        sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        ed = sp.edit();
        ed.putBoolean(KEY_IS_LOGIN, true);
        ed.putString(LOGIN_TYPE, Login_Type).apply();
    }

    public static void setLoginFalse(Context context) {
        SharedPreferences.Editor ed;
        SharedPreferences sp;
        sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        ed = sp.edit();
        ed.putBoolean(KEY_IS_LOGIN, false).apply();
    }

    public static String getLoginType(Context context) {
        SharedPreferences sp;
        sp = context.getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        String LoginType = sp.getString(LOGIN_TYPE, "");
        return LoginType;
    }

    public static Spanned getTitle(String title) {
/*

        SpannableString ss1 = new SpannableString(title);
        ss1.setSpan(new AbsoluteSizeSpan(40), 0, title.length(), SPAN_INCLUSIVE_INCLUSIVE);
*/

        return Html.fromHtml("<small>" + title + "</small>");

    }

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    //    public static String getPath(Context context, Uri uri) throws URISyntaxException {
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = { "_data" };
//            Cursor cursor = null;
//
//            try {
//                cursor = context.getContentResolver().query(uri, projection, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow("_data");
//                if (cursor.moveToFirst()) {
//                    return cursor.getString(column_index);
//                }
//            } catch (Exception e) {
//                // Eat it
//            }
//        }
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }
    public static String getPath(final Context context, final Uri uri) {
        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    String id = "";
                    try {
                        id = DocumentsContract.getDocumentId(uri);
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        return getDataColumn(context, contentUri, null, null);
                    } catch (Exception e) {
                        e.getCause();
                        if (id.contains(":")) {
                            return id.substring(id.indexOf(":") + 1);
                        }
                    }
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
            e.getCause();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String parseDateToMMddyyyy(String time) {
        String inputPattern = "dd-MM-yyyy HH:mm:ss";
        String outputPattern = "MM-dd-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String ChangeDateFormat(String inputPattern, String outputPattern, String time) {

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Boolean IsTimeAfter(String str_time1, String str_time2) {
        boolean isTimeAfter = false;

        try {

            String string1 = str_time1;
            Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);

            String string2 = str_time2;
            Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);

            if (calendar1.getTime().after(calendar2.getTime())) {
                //checkes whether the str_time1 time is after str_time2.
                System.out.println(true);
                isTimeAfter = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isTimeAfter;
    }

    public static Boolean IsTimeBefore(String str_time1, String str_time2) {
        boolean isTimeBefore = false;

        try {
            String string1 = str_time1;
            Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);

            String string2 = str_time2;
            Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);

            if (calendar1.getTime().before(calendar2.getTime())) {
                //checkes whether the str_time1 time is after str_time2.
                System.out.println(true);
                isTimeBefore = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isTimeBefore;
    }

    public static Boolean IsTimeEqual(String str_time1, String str_time2) {
        boolean isTimeEqual = false;
        boolean isTimeafter = IsTimeAfter(str_time1, str_time2);
        boolean isTimeBefore = IsTimeBefore(str_time1, str_time2);

        if (!isTimeafter && !isTimeBefore) {
            isTimeEqual = true;
        }

        return isTimeEqual;
    }

    public static void SaveClientLoginData(Context context,
                                           String userID,
                                           String txt_Mail,
                                           String CompID,
                                           String CompName,
                                           String UserName,
                                           String UserCategory,
                                           String CaType,
                                           String DealerID,
                                           String dtype,
                                           String Login_Email,
                                           String dealer_name,
                                           String status,
                                           String Imagepath,
                                           String Masterstatus) {
        SharedPreferences sp = context.getSharedPreferences("skyline", MODE_PRIVATE);
        SharedPreferences.Editor ed;
        ed = sp.edit();

        ed.putString(Utility.CLIENT_LOGIN_userID, userID);
        ed.putString(Utility.CLIENT_LOGIN_txt_Mail, txt_Mail);
        ed.putString(Utility.CLIENT_LOGIN_CompID, CompID);
        ed.putString(Utility.CLIENT_LOGIN_CompName, CompName);
        ed.putString(Utility.CLIENT_LOGIN_UserName, UserName);
        ed.putString(Utility.CLIENT_LOGIN_UserCategory, UserCategory);
        ed.putString(Utility.CLIENT_LOGIN_CaType, CaType);
        ed.putString(Utility.CLIENT_LOGIN_DealerID, DealerID);
        ed.putString(Utility.CLIENT_LOGIN_dtype, dtype);
        ed.putString(Utility.CLIENT_LOGIN_Login_Email, Login_Email);
        ed.putString(Utility.CLIENT_LOGIN_dealer_name, dealer_name);
        ed.putString(Utility.CLIENT_LOGIN_status, status);
        ed.putString(Utility.CLIENT_LOGIN_Imagepath, Imagepath);
        ed.putString(Utility.CLIENT_LOGIN_Masterstatus, Masterstatus);

        ed.apply();
    }

    public static boolean isEntryOverLaps(String new_startTime, String new_endTime, String old_startTime, String old_endTime) {
        boolean isOverLappingEntry = false;

        try {
      /*  ///old_endTime > new_startTime
        if (IsTimeAfter(old_endTime, new_startTime)) {
            isOverLappingEntry = true;
        }*/
            //new_endTime>old_startTime
       /* else if (IsTimeAfter(new_endTime, old_startTime)) {
            isOverLappingEntry = true;
        }*/

            //old_startTime == new_startTime
            if (IsTimeEqual(new_startTime, old_startTime)) {
                isOverLappingEntry = true;
            }

            //new_startTime<old_startTime && new_endTime><old_endTime
            else if (IsTimeBefore(new_startTime, old_startTime) && !IsTimeEqual(new_endTime, old_endTime)) {
                isOverLappingEntry = true;
            }
        } catch (Exception e) {
            e.getCause();
        }

        return isOverLappingEntry;
    }

    public static String getCorrectTime(String hrs_mnts) {

        if (hrs_mnts == null) {
            return "00:00";
        } else if (!hrs_mnts.contains(":")) {

            String hrs = hrs_mnts.substring(0, 2);
            String mnts = hrs_mnts.substring(2, 4);

            return hrs + ":" + mnts;


        }
        return "00:00";
    }

    public static int random_number_notificationId() {
        Random r = new Random();
        int i1 = r.nextInt(500000 - 10) + 10;
        return i1;
    }

    public static String getCurrentdate(String DateFormat) {
        String date = new
                SimpleDateFormat(DateFormat,
                Locale.getDefault()).format(new Date());
        return date;
    }

    public static boolean isToDatebeforeFromDate(String fromDate, String toDate, String format) {

        boolean isDate = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date _fromDate = sdf.parse(fromDate);
            Date _toDate = sdf.parse(toDate);


            if (_toDate.before(_fromDate)) {
                isDate = true;
            }


        } catch (Exception e) {
            e.getMessage();
        }
        return isDate;
    }

    public static boolean isToDateafterFromDate(String fromDate, String toDate, String format) {

        boolean isDate = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date _fromDate = sdf.parse(fromDate);
            Date _toDate = sdf.parse(toDate);


            if (_toDate.after(_fromDate)) {
                isDate = true;
            }


        } catch (Exception e) {
            e.getMessage();
        }
        return isDate;
    }

    public static String getDayFromDate(String date, String format) {
        Date dt1 = null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            dt1 = format1.parse(date);
        } catch (Exception e) {
            e.getMessage();
        }
        return (String) DateFormat.format("EEEE", dt1);
    }


    public static String TrimString(String str, int str_length) {
        String text = str;
        if (text.length() > str_length) {
            text = text.substring(0, str_length) + "...";
        }
        return text;
    }

    public static void animateTextview(final TextView textView, Context context) {

       /* int colorFrom = context.getResources().getColor(R.color.textvw_value);
        int colorTo = context.getResources().getColor(R.color.textvw_label);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(500); // milliseconds
        colorAnimation.setRepeatCount(150);
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                textView.setTextColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();*/


        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        textView.startAnimation(anim);


    }

    public static void StopanimateTextview(final TextView textView, Context context) {

       /* int colorFrom = context.getResources().getColor(R.color.textvw_value);
        int colorTo = context.getResources().getColor(R.color.textvw_label);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(0); // milliseconds
        colorAnimation.setRepeatCount(1);
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                textView.setTextColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.cancel();*/

        textView.clearAnimation();
    }

    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }


    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public static ArrayList<LaborCode> getNonBilableCodes(Context context) {
        String nonBiilableCodes = Shared_Preference.getNON_BILLABLE_CODES(context);
        ArrayList<LaborCode> listCodes = new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(nonBiilableCodes);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int k = 0; k < (jsonArray.length()); k++) {
                    JSONObject json_obj = jsonArray.getJSONObject(k);
                    String laborId = json_obj.getString("Lalor_ID_PK");
                    String laborName = json_obj.getString("Labor_name");
                    listCodes.add(new LaborCode(laborId, laborName));
                }

            }


        } catch (Exception e) {
            e.getMessage();
        }
        return listCodes;
    }

    public static JSONObject SoapObject_To_JsonObject(SoapObject soapObject){
        String str=soapObject.toString();
        str=  str.substring(str.indexOf("{")+1,str.indexOf("}"));
        String ar[]=str.split(";");
        JSONObject jsonObject = new JSONObject();
        for(int i=0;i<ar.length;i++){
          if(  ar[i]!=null && !ar[i].equals("")){
              try {
                  String rs[] = ar[i].split("=");
                  jsonObject.put(rs[0].trim(), rs[1].trim());
              }catch (Exception e){
                  e.getMessage();
              }
              }
        }
        return jsonObject;
    }

}
