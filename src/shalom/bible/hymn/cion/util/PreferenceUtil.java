package shalom.bible.hymn.cion.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.webkit.WebView;



public class PreferenceUtil {
	
	private static final String SHARED_FILE_TITLE = "pref_biblehymn";
	public final static String PREF_BIBLE_TYPE_1 = "old_bible_type_1";
	public final static String PREF_BIBLE_TYPE_2 = "old_bible_type_2";
	public final static String PREF_BIBLE_TEXT_SIZE = "pref_bible_text_size";
	public final static String PREF_KWON_OLD = "pref_kwon_old";
	public final static String PREF_KWON_NEW = "pref_kwon_new";
	public final static String PREF_JANG_OLD = "pref_jang_old";
	public final static String PREF_JANG_NEW = "pref_jang_new";
	public final static String PREF_KWON_OLD_WHICH = "pref_kwon_old_which";
	public final static String PREF_KWON_NEW_WHICH = "pref_kwon_new_which";
	public final static String PREF_JANG_OLD_WHICH = "pref_jang_old_which";
	public final static String PREF_JANG_NEW_WHICH = "pref_jang_new_which";
	
	/*public final static String PREF_KWON_NEW = "pref_kwon_new";
	public final static String PREF_JANG_NEW = "pref_jang_new";
	public final static String PREF_KWON_NEW_WHICH = "pref_kwon_new_which";
	public final static String PREF_JANG_NEW_WHICH = "pref_jang_new_which";*/
	
	public final static String PREF_AUDIO_SPEED = "pref_audio_speed";
	public final static String PREF_SWIPE_MODE = "pref_swipe_mode";
	public final static String PREF_SCROLL_SPEED = "pref_scroll_speed";
	public final static String PREF_BIBLE_TEXT_COLOR = "pref_bible_text_color";
	public final static String PREF_BIBLE_TEXT_COLOR2 = "pref_bible_text_color2";
	public final static String PREF_BIBLE_BG_COLOR = "pref_bible_bg_color";
	
	public final static String PREF_MAIN_PRAYER_WHICH = "pref_main_prayer_which";
	public final static String PREF_NEW_KYODOK_PRAYER_WHICH = "pref_new_kyodok_prayer_which";
	public final static String PREF_OLD_KYODOK_PRAYER_WHICH = "pref_old_kyodok_prayer_which";
	public final static String PREF_SIMBANG_PRAYER_WHICH = "pref_simbang_prayer_which";
	public final static String PREF_VOICE_CONTINUE = "pref_voice_continue";
	public final static String PREF_HYMN_CONTINUE = "pref_hymn_continue";
	public final static String PREF_PODCAST_TITLE= "pref_podcast_title";
	public final static String PREF_PODCAST_PUBDATE= "pref_podcast_pubdate";
	
	public final static String PREF_DO_RECORDING = "pref_do_recording";
	public final static String PREF_NAME_RECORDING = "pref_name_recording";
	
	public final static String PREF_AD_VIEW = "ad_view";
	public final static String PREF_AD_TIME = "ad_time";
	
	//==============================================================================================//
	//================================ 프리퍼런스 저장하고 불러오기 ================================//
	public static boolean getBooleanSharedData(Context context, String key, boolean defaultData) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultData);
    }
    public static void setBooleanSharedData(Context context, String key, boolean flag) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean(key, flag);
        e.commit();
    }
    public static int getIntSharedData(Context context, String key, int defaultData) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getInt(key, defaultData);
    }
    public static void setIntSharedData(Context context, String _key, int _data) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putInt(_key, _data);
        e.commit();
    }
    public static long getLongSharedData(Context context, String key, long defaultData) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getLong(key, defaultData);
    }
    public static void setLongSharedData(Context context, String _key, long _data) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putLong(_key, _data);
        e.commit();
    }
    public static String getStringSharedData(Context context, String key, String defaultData) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getString(key, defaultData);
    }
    public static void setStringSharedData(Context context, String key, String data) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putString(key, data);
        e.commit();
    }
    
    /**
     * 테블릿인지
     */
    private static final String PREFERENCE_NAME         = "ATTEND_PREFERENCE";
    private static final String KEY_IS_TABLE			= "KEY_IS_TABLE";
    
    public static boolean getIsTable(Context cxt) {
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(null == sp) {
            return false;
        }

        return sp.getBoolean(KEY_IS_TABLE, false);
    }

    public static void setIsTable(Context cxt, boolean isTable){
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(null == sp) {
            return;
        }
        Editor et = sp.edit();
        et.putBoolean(KEY_IS_TABLE, isTable);
        et.commit();
    }
    
    public static boolean checkTabletDeviceWithUserAgent(Context context) {
        WebView webView = new WebView(context);
        String ua=webView.getSettings().getUserAgentString();
        webView = null;
        if(ua.contains("Mobile Safari")){
            return false;
        }else{
           return true;
        }
    }
}
