package planet.info.skyline.tech.shared_preference;

import android.content.Context;
import android.content.SharedPreferences;


public class Shared_Preference {
    public static final String TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE = "TimerStartedFromAdminClockModule";
    public static final String TIMER_STARTED_FROM_BILLABLE_MODULE = "TimerStartedFromBillableModule";
    public static final String TIME_SHEET_ID = "TIME_SHEET_ID";
    public static final String TIMEGAP_JOB_END_TIME = "TIMEGAP_JOB_END_TIME";
    public static final String TIMEGAP_PREV_JOB_START_TIME = "TIMEGAP_PREV_JOB_START_TIME";
    public static final String TIMEGAP_JOB_START_TIME = "TIMEGAP_JOB_START_TIME";
    public static final String KEY_APP_LAUNCHED_FIRST_TIME = "APP_LAUNCHED_FIRST_TIME";
    public static final String KEY_PAUSED_TIMESHEET_ID = "PAUSED_TIMESHEET_ID";
    public static final String KEY_INCOMPLETE_ASYNC_ARRAY = "INCOMPLETE_ASYNC_ARRAY";
    private static final String PrefName = "skyline";
    /**************************Note: Do not change the value of variables declared here************************************/
    //login
    private static final String LOGIN_USER_ROLE = "LOGIN_USER_ROLE";
    private static final String DEALER_ID = "DEALER_ID";
    private static final String USER_MOBILE = "mob";
    private static final String USERNAME = "tname";
    private static final String LOGIN_USER_ID = "clientid";
    private static final String LOGIN_USERNAME = "LOGIN_USERNAME";
    private static final String FCM_TOKEN = "FCM_TOKEN";
    private static final String USER_TIMESHEET_RIGHT = "USER_TIMESHEET_RIGHT";
    // in app pref
    private static final String SWO_ID = "jobid";
    private static final String KEY_JOB_ID_FOR_JOBFILES = "KEY_JOB_ID_FOR_JOBFILES";
    private static final String JOB_ID_BILLABLE = "JOB_ID_BILLABLE";
    private static final String COMPANY_ID_BILLABLE = "COMPANY_ID_BILLABLE";
    private static final String COMPANY_IMAGE_LOGO_URL = "imglo";
    private static final String COMPANY_NAME = "name";
    private static final String LINK = "link";
    private static final String UPLOAD = "upload";
    private static final String UPLOAD2 = "upload2";
    private static final String CLOCK_START_TIME = "CLOCK_START_TIME";
    private static final String JOB_TYPE_BILLABLE = "billable";
    private static final String STARTING_BILLABLE_JOB = "STARTING_BILLABLE_JOB";
    private static final String WORK_LOCATION = "worklocation";
    private static final String CLIENT_NAME = "client_name";

    private static final String CRATE_ID = "catidnew";

    private static final String NON_BILLABLE_CODES = "NON_BILLABLE_CODES";


    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
    }

    public static void setUSER_ROLE(Context context, String Role) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(LOGIN_USER_ROLE, Role);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUSER_ROLE(Context context) {
        String val = "";
        try {


            val = getSharedPreferences(context).getString(LOGIN_USER_ROLE, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setDEALER_ID(Context context, String DealerID) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(DEALER_ID, DealerID);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDEALER_ID(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(DEALER_ID, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setLOGIN_USERNAME(Context context, String Username) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(USERNAME, Username);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLOGIN_USERNAME(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(USERNAME, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setLOGIN_ID(Context context, String ID) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(LOGIN_USERNAME, ID);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLOGIN_ID(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(LOGIN_USERNAME, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setUSER_MOBILE(Context context, String Mobile) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(USER_MOBILE, Mobile);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUSER_MOBILE(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(USER_MOBILE, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setLOGIN_USER_ID(Context context, String Id) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(LOGIN_USER_ID, Id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLOGIN_USER_ID(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(LOGIN_USER_ID, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setFCM_TOKEN(Context context, String token) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(FCM_TOKEN, token);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFCM_TOKEN(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(FCM_TOKEN, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setTIMESHEET_RIGHT(Context context, String timesheet_right) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(USER_TIMESHEET_RIGHT, timesheet_right);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTIMESHEET_RIGHT(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(USER_TIMESHEET_RIGHT, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean delete_SharedPreference(Context context) {
        boolean result = false;
        try {

            result = getSharedPreferences(context).edit().clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void setSWO_ID(Context context, String swo_id) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(SWO_ID, swo_id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSWO_ID(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(SWO_ID, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setJOB_ID_FOR_JOBFILES(Context context, String job_id) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(KEY_JOB_ID_FOR_JOBFILES, job_id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJOB_ID_FOR_JOBFILES(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(KEY_JOB_ID_FOR_JOBFILES, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setJOB_NAME_BILLABLE(Context context, String job_id) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(JOB_ID_BILLABLE, job_id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJOB_NAME_BILLABLE(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(JOB_ID_BILLABLE, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setCOMPANY_ID_BILLABLE(Context context, String comp_id) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(COMPANY_ID_BILLABLE, comp_id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCOMPANY_ID_BILLABLE(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(COMPANY_ID_BILLABLE, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setCLIENT_IMAGE_LOGO_URL(Context context, String url) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(COMPANY_IMAGE_LOGO_URL, url);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCLIENT_IMAGE_LOGO_URL(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(COMPANY_IMAGE_LOGO_URL, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setCLIENT_NAME(Context context, String company_name) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(COMPANY_NAME, company_name);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCLIENT_NAME(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(COMPANY_NAME, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setLINK(Context context, String link) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(LINK, link);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLINK(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(LINK, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setUPLOAD(Context context, String str) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(UPLOAD, str);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUPLOAD(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(UPLOAD, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setUPLOAD2(Context context, String str) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(UPLOAD2, str);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUPLOAD2(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(UPLOAD2, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setCLOCK_START_TIME(Context context, String str) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(CLOCK_START_TIME, str);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCLOCK_START_TIME(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(CLOCK_START_TIME, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setJOB_TYPE_IS_BILLABLE(Context context, Boolean isBillable) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putBoolean(JOB_TYPE_BILLABLE, isBillable);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getJOB_TYPE_IS_BILLABLE(Context context) {
        boolean val = false;
        try {

            val = getSharedPreferences(context).getBoolean(JOB_TYPE_BILLABLE, false);
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }


    public static void setTIMER_STARTED_FROM_ADMIN_CLOCK_MODULE(Context context, Boolean is_TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putBoolean(TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, is_TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getTIMER_STARTED_FROM_ADMIN_CLOCK_MODULE(Context context) {
        boolean val = false;
        try {

            val = getSharedPreferences(context).getBoolean(TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, false);
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void setTIMER_STARTED_FROM_BILLABLE_MODULE(Context context, Boolean is_TIMER_STARTED_FROM_BILLABLE_MODULE) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putBoolean(TIMER_STARTED_FROM_BILLABLE_MODULE, is_TIMER_STARTED_FROM_BILLABLE_MODULE);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getTIMER_STARTED_FROM_BILLABLE_MODULE(Context context) {
        boolean val = false;
        try {

            val = getSharedPreferences(context).getBoolean(TIMER_STARTED_FROM_BILLABLE_MODULE, false);
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void setIS_STARTING_BILLABLE_JOB(Context context, Boolean IS_STARTING_BILLABLE_JOB) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putBoolean(STARTING_BILLABLE_JOB, IS_STARTING_BILLABLE_JOB);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getIS_STARTING_BILLABLE_JOB(Context context) {
        boolean val = false;
        try {

            val = getSharedPreferences(context).getBoolean(STARTING_BILLABLE_JOB, false);
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void setWORK_LOCATION(Context context, String Work_Location) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(WORK_LOCATION, Work_Location);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getWORK_LOCATION(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(WORK_LOCATION, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void setCLIENT(Context context, String Client_name) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(CLIENT_NAME, Client_name);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCLIENT(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(CLIENT_NAME, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void setTIME_SHEET_ID(Context context, String timeSheetId) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(TIME_SHEET_ID, timeSheetId);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTIME_SHEET_ID(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(TIME_SHEET_ID, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }


    public static void setTIMEGAP_JOB_END_TIME(Context context, String timegap_job_end_time) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(TIMEGAP_JOB_END_TIME, timegap_job_end_time);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTIMEGAP_JOB_END_TIME(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(TIMEGAP_JOB_END_TIME, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }


    public static void setTIMEGAP_JOB_START_TIME(Context context, String timegap_job_start_time) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(TIMEGAP_JOB_START_TIME, timegap_job_start_time);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTIMEGAP_JOB_START_TIME(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(TIMEGAP_JOB_START_TIME, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void setTIMEGAP_PREV_JOB_START_TIME(Context context, String timegap_prev_job_start_time) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(TIMEGAP_PREV_JOB_START_TIME, timegap_prev_job_start_time);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTIMEGAP_PREV_JOB_START_TIME(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(TIMEGAP_PREV_JOB_START_TIME, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }
    public static void setAPP_LAUNCHED_FIRST_TIME(Context context, boolean app_launched_first_time) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putBoolean(KEY_APP_LAUNCHED_FIRST_TIME, app_launched_first_time);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean getAPP_LAUNCHED_FIRST_TIME(Context context) {
        boolean val = false;
        try {

            val = getSharedPreferences(context).getBoolean(KEY_APP_LAUNCHED_FIRST_TIME, false);
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }
    public static void setPAUSED_TIMESHEET_ID(Context context, String paused_timesheet_id) {
        try {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(KEY_PAUSED_TIMESHEET_ID, paused_timesheet_id);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getPAUSED_TIMESHEET_ID(Context context) {
        String val = "";
        try {
            val = getSharedPreferences(context).getString(KEY_PAUSED_TIMESHEET_ID, "");
            return val;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }
    public static void setCrate_ID(Context context, String CrateId) {
        try {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(CRATE_ID, CrateId);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getCrate_ID(Context context) {
        String val = "";
        try {
            val = getSharedPreferences(context).getString(CRATE_ID, "");
            return val;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }
    public static void setINCOMPLETE_ASYNC_ARRAY(Context context, String incomplete_task_async_array) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(KEY_INCOMPLETE_ASYNC_ARRAY, incomplete_task_async_array);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getINCOMPLETE_ASYNC_ARRAY(Context context) {
        String val = "";
        try {

            val = getSharedPreferences(context).getString(KEY_INCOMPLETE_ASYNC_ARRAY, "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }
    public static boolean clearAllPreferences(Context context) {
        return getSharedPreferences(context).edit().clear().commit();
    }
    public static boolean containsKey(Context context, String Key){
      return   getSharedPreferences(context).contains(Key);
    }
    public static void setNON_BILLABLE_CODES(Context context, String json_array) {
        try {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(NON_BILLABLE_CODES, json_array);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getNON_BILLABLE_CODES(Context context) {
        String val = "";
        try {
            val = getSharedPreferences(context).getString(NON_BILLABLE_CODES, "");
            return val;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }




}
/**/