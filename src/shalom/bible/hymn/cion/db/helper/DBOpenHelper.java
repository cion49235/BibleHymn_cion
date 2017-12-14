package shalom.bible.hymn.cion.db.helper;

import java.util.ArrayList;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import shalom.bible.hymn.cion.BibleHymnApp;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.activity.PodcastActivity_Download_Sub;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.dao.Activity_Data_CCM_Favorite;
import shalom.bible.hymn.cion.dao.Activity_Data_Podcast_Favorite;
import shalom.bible.hymn.cion.dao.Fragment_Data_Podcast_Download;
import shalom.bible.hymn.cion.dao.Fragment_Data_Prayer;
import shalom.bible.hymn.cion.podcast.mediaplayer.CustomMediaPlayer_Podcast;
import shalom.bible.hymn.cion.util.AlertUtil;
import shalom.bible.hymn.cion.util.DownloadAsync;
import shalom.bible.hymn.cion.util.NetworkHelper;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.Utils;

public class DBOpenHelper {
	private final Application mApp;
	public SQLiteDatabase mdb, mdb2, mdb_note, mdb_prayer;
	private DBOpenHelper_kbb kbb_db;
	private DBOpenHelper_kjv kjv_db;
	private DBOpenHelper_kkk kkk_db;
	private DBOpenHelper_jpnnew jpnnew_db;
	private DBOpenHelper_ckb ckb_db;
	private DBOpenHelper_frenchdarby frenchdarby_db;
	private DBOpenHelper_germanluther germanluther_db;
	private DBOpenHelper_gst gst_db;
	private DBOpenHelper_indonesianbaru indonesianbaru_db;
	private DBOpenHelper_portugal portugal_db;
	private DBOpenHelper_russiansynodal russiansynodal_db;
	private DBOpenHelper_alb alb_db;
	private DBOpenHelper_asv asv_db;
	private DBOpenHelper_avs avs_db;
	private DBOpenHelper_barun barun_db;
	private DBOpenHelper_chb chb_db;
	private DBOpenHelper_chg chg_db;
	private DBOpenHelper_cjb cjb_db;
	private DBOpenHelper_ckc ckc_db;
	private DBOpenHelper_ckg ckg_db;
	private DBOpenHelper_cks cks_db;
	private DBOpenHelper_hebbhs hebbhs_db;
	private DBOpenHelper_hebmod hebmod_db;
	private DBOpenHelper_hebwlc hebwlc_db;
	private DBOpenHelper_indianhindi indianhindi_db;
	private DBOpenHelper_indiantamil indiantamil_db;
	private DBOpenHelper_jpnold jpnold_db;
	private DBOpenHelper_reina reina_db;
	private DBOpenHelper_tagalog tagalog_db;
	private DBOpenHelper_tkh tkh_db;
	private DBOpenHelper_web web_db;
	private DBOpenHelper_note note_db;
	private DBOpenHelper_voice_pause voice_pause_db;
	private DBOpenHelper_prayer prayer_db;
	private DBopenHelper_podcast_favorite podcast_main_favorite_db;
	private DBopenHelper_podcast_download podcast_download_db;
	private DBOpenHelper_podcast_pause podcast_pause_db;
	private DBopenHelper_podcast_continue podcast_continue_db;
	private DBopenHelper_ccm_favorite ccm_favorite_db;
	private DBopenHelper_bible_bookmark bible_bookmark_db;
	
	private static DBOpenHelper mINSTANCE = new DBOpenHelper();
	private final NetworkHelper mNetHelper = NetworkHelper.getInstance();
	public static DBOpenHelper getInstance(){
		return mINSTANCE;
	}

	private DBOpenHelper(){
		mApp = BibleHymnApp.getApplication();
		voice_pause_db = new DBOpenHelper_voice_pause(mApp);
		note_db = new DBOpenHelper_note(mApp);
		prayer_db = new DBOpenHelper_prayer(mApp);
		podcast_main_favorite_db = new DBopenHelper_podcast_favorite(mApp);
		podcast_download_db = new DBopenHelper_podcast_download(mApp);
		podcast_pause_db = new DBOpenHelper_podcast_pause(mApp);
		podcast_continue_db = new DBopenHelper_podcast_continue(mApp);
		ccm_favorite_db = new DBopenHelper_ccm_favorite(mApp);
		bible_bookmark_db = new DBopenHelper_bible_bookmark(mApp);
		
	}
	
	/*public ArrayList<Fragment_Data_Note> bind_note_db(ArrayList<Fragment_Data_Note> list){
		try{
			note_db = new DBOpenHelper_note(mApp);
			mdb_note = note_db.getReadableDatabase();
			Cursor cursor = mdb.rawQuery("select * from my_list order by _id desc", null);
			while(cursor.moveToNext()){
				int idx = cursor.getColumnIndex("_id");
				int id = cursor.getInt(idx);
				list.add(new Fragment_Data_Note(id, cursor.getString(cursor.getColumnIndex("kwon")),cursor.getString(cursor.getColumnIndex("jang")), cursor.getString(cursor.getColumnIndex("jul")), cursor.getString(cursor.getColumnIndex("content"))));
				Log.i("dsu", "노트추가내용 : " +"id : " + id + cursor.getString(cursor.getColumnIndex("content")));
			}	
		}catch (Exception e) {
		}finally{
			close_note_db();
		}
		return list;
	}*/
	public void podcast_db_pause_task(String title, int current_position, String pubDate){
//		Log.i("dsu", "podcast_db_pause_task======>title : " + title + "\npubDate : " + pubDate);
    	ContentValues cv = new ContentValues();
		try{
    		Cursor cursor = podcast_pause_db.getReadableDatabase().rawQuery("select * from video_pause WHERE video_title = '"+title+"' AND video_pubDate = '"+pubDate+"'", null);
            if(cursor != null && cursor.moveToFirst()) {
            	int podcast_currentPosition = cursor.getInt(cursor.getColumnIndex("video_currentPosition"));
            	cv.put("video_title", title);
    			cv.put("video_currentPosition", current_position);
    			cv.put("video_pubDate", pubDate);
    			podcast_pause_db.getWritableDatabase().update("video_pause", cv, "video_currentPosition" + "=" + podcast_currentPosition, null);
//    			Log.i("dsu", "DB_Update=======>" + current_position);
            }else{
            	cv.put("video_title", title);
            	cv.put("video_currentPosition", current_position);
            	cv.put("video_pubDate", pubDate);
            	podcast_pause_db.getWritableDatabase().insert("video_pause", null, cv);
//            	Log.i("dsu", "DB_Insert=======>" + current_position);
            }
    	}catch (Exception e) {
		}finally{
			close_podcast_pause_db();
		}
    }
	
	public void podcast_db_current_position(String title, String pubDate){
//		Log.i("dsu", "podcast_db_current_position======>title : " + title + "\npubDate : " + pubDate);
		try{
			Cursor cursor = podcast_pause_db.getReadableDatabase().rawQuery("select * from video_pause WHERE video_title = '"+title+"' AND video_pubDate = '"+pubDate+"'", null);
            if(cursor != null && cursor.moveToFirst()) {
            	Const.current_position_podcast = cursor.getInt(cursor.getColumnIndex("video_currentPosition"));
//            	Log.i("dsu", "이어서재생===>" + Const.current_position_podcast);
            }else{
            	Const.current_position_podcast = 0;
//            	Log.i("dsu", "처음재생====>" + Const.current_position_podcast);
            }
		}catch(Exception e) {
		}finally{
			close_podcast_pause_db();
		}
	}
	
	public void close_podcast_pause_db(){
		if(podcast_pause_db != null){
			podcast_pause_db.close();
		}
	}
	
	public void bind_podcast_download_main_db(ArrayList<Fragment_Data_Podcast_Download> list){
		try{
			Cursor cursor = podcast_download_db.getReadableDatabase().rawQuery("select * from download_list group by description_title order by _id desc", null);
			while(cursor.moveToNext()){
				list.add(new Fragment_Data_Podcast_Download(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
			}	
		}catch (Exception e) {
		}finally{
			close_podcast_download_db();
		}
	}
	
	public void bind_podcast_download_sub_db(ArrayList<Fragment_Data_Podcast_Download> list, String description_title){
		try{
			Cursor cursor = podcast_download_db.getReadableDatabase().rawQuery("select * from download_list where description_title = '"+description_title+"' order by _id desc", null);
			while(cursor.moveToNext()){
				list.add(new Fragment_Data_Podcast_Download(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
			}	
		}catch (Exception e) {
		}finally{
			close_podcast_download_db();
		}
	}
	
	String down_enclosure = "empty";   
	public void check_podcast_download_db(String sub_file_name, ImageButton bt_down){
		try{
			Cursor cursor = podcast_download_db.getReadableDatabase().rawQuery(
					"select * from download_list where enclosure = '"+sub_file_name+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				down_enclosure = cursor.getString(cursor.getColumnIndex("enclosure"));
			}else{
				down_enclosure = "empty";
			}
//			Log.i("dsu", "down_enclosure : " + down_enclosure);
			if(down_enclosure.equals("empty")){
				bt_down.setImageResource(R.drawable.ic_action_download);
			}else{
				bt_down.setImageResource(R.drawable.ic_action_play_over_video);
			}
		}catch (Exception e) {
		}finally{
			close_podcast_download_db();
		}
	}
	
	public void delete_podcast_download_db(int _id){
		try{
			podcast_download_db.getWritableDatabase().delete("download_list", "_id" + "='" +_id+"'", null);
		}catch (Exception e) {
		}finally{
			close_podcast_download_db();
		}
	}
	
	String continue_enclosure = "empty";   
	public void check_podcast_continue_db(String sub_file_name, ImageButton bt_continue){
		try{
			Cursor cursor = podcast_continue_db.getReadableDatabase().rawQuery(
					"select * from continue_list where enclosure = '"+sub_file_name+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				continue_enclosure = cursor.getString(cursor.getColumnIndex("enclosure"));
			}else{
				continue_enclosure = "empty";
			}
//			Log.i("dsu", "down_enclosure : " + down_enclosure);
			if(continue_enclosure.equals("empty")){
				bt_continue.setImageResource(R.drawable.bt_continue_normal);
			}else{
				bt_continue.setImageResource(R.drawable.bt_continue_pressed);
			}
		}catch (Exception e) {
		}finally{
			close_podcast_continue_db();
		}
	}
	
	public void close_podcast_continue_db(){
		if(podcast_continue_db != null){
			podcast_continue_db.close();
		}
	}
	
	public void insert_delete_podcast_continue_db(Context context, String sub_file_name, ImageButton bt_continue, String title, String enclosure, String pubDate, String image, String description_title){
		try{
			Cursor cursor = podcast_continue_db.getReadableDatabase().rawQuery(
					"select * from continue_list where enclosure = '"+enclosure+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				continue_enclosure = cursor.getString(cursor.getColumnIndex("enclosure"));
			}else{
				continue_enclosure = "empty";
			}
			if(continue_enclosure.equals("empty")){
				bt_continue.setImageResource(R.drawable.bt_continue_pressed);
				ContentValues cv = new ContentValues();
				cv.put("title", title);
				cv.put("enclosure", enclosure);
				cv.put("pubDate", pubDate);
				cv.put("image", image);
				cv.put("description_title", description_title);
				podcast_continue_db.getWritableDatabase().insert("continue_list", null, cv);
				Toast.makeText(context, context.getString(R.string.activity_podcast_16), Toast.LENGTH_SHORT).show();
			}else{
				bt_continue.setImageResource(R.drawable.bt_continue_normal);
				podcast_continue_db.getWritableDatabase().delete("continue_list", "enclosure" + "='" +continue_enclosure+"'", null);
				Toast.makeText(context, context.getString(R.string.activity_podcast_17), Toast.LENGTH_SHORT).show();
			}
		}finally{
			close_podcast_continue_db();
		}
	}
	
	
	public void check_podcast_download_task(Context context, String sub_file_name, String title, String enclosure, int position, String description_title,  String provider, String image, String pubDate, String old_title, int type, int _id){
		try{
			Cursor cursor = podcast_download_db.getReadableDatabase().rawQuery(
					"select * from download_list where enclosure = '"+sub_file_name+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				down_enclosure = cursor.getString(cursor.getColumnIndex("enclosure"));
			}else{
				down_enclosure = "empty";
			}
//			Log.i("dsu", "down_enclosure : " + down_enclosure);
			if(down_enclosure.equals("empty")){
				if(!mNetHelper.is3GConnected() && !mNetHelper.isWIFIConneced()){
					Toast.makeText(context, context.getString(R.string.download_data_connection_ment), Toast.LENGTH_LONG).show();
					return;
				}
//				Log.i("dsu", "is3GConnected : " + mNetHelper.is3GConnected() + "\nisWIFIConneced : " + mNetHelper.isWIFIConneced());
				if(mNetHelper.is3GConnected()){
					AlertUtil.AlertShow(context.getString(R.string.activity_podcast_11), context, title, enclosure, position, description_title, podcast_download_db, provider, image, pubDate, old_title);
				}else{
					DownloadAsync downloadAsync = new DownloadAsync(context, title, enclosure,position, description_title, podcast_download_db, provider, image, pubDate, old_title);
					downloadAsync.execute();
				}
			}else{
//				Log.i("dsu", "type : " + type);
				if(Utils.file_check(sub_file_name) == true){
					Intent intent = new Intent(context, CustomMediaPlayer_Podcast.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.putExtra("title", old_title);
					intent.putExtra("enclosure", down_enclosure);
					intent.putExtra("pubDate", pubDate);
					intent.putExtra("image", image);
					intent.putExtra("description_title", description_title);
					intent.putExtra("down_buffer", true);
					context.startActivity(intent);
				}else{
					if(type == 0){
						AlertUtil.AlertShow(context.getString(R.string.activity_podcast_13), context, title, enclosure, position, description_title, podcast_download_db, provider, image, pubDate, old_title);	
					}else{
						Toast.makeText(context, context.getString(R.string.activity_podcast_29), Toast.LENGTH_SHORT).show();
						delete_podcast_download_db(_id);
						if(shalom.bible.hymn.cion.util.Utils.file_check(sub_file_name) == true){
							shalom.bible.hymn.cion.util.Utils.file_delete(sub_file_name);	
						}
						((PodcastActivity_Download_Sub)PodcastActivity_Download_Sub.context).datasetchanged();
					}
				}
			}
		}catch (Exception e) {
		}finally{
			close_podcast_download_db();
		}
	}
	
	public void close_podcast_download_db(){
		if(podcast_download_db != null){
			podcast_download_db.close();
		}
	}
	
	public void bind_podcast_favorite_db(ArrayList<Activity_Data_Podcast_Favorite> list){
		try{
			Cursor cursor = podcast_main_favorite_db.getReadableDatabase().rawQuery("select * from favorite_list order by _id desc", null);
			while(cursor.moveToNext()){
				list.add(new Activity_Data_Podcast_Favorite(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
			}	
		}catch (Exception e) {
		}finally{
			close_podcast_main_favorite_db();
			}
		}
	
	public void delete_podcast_favorite_db(int _id){
		try{
			podcast_main_favorite_db.getWritableDatabase().delete("favorite_list", "_id" + "=" +_id, null);
		}catch (Exception e) {
		}finally{
			close_podcast_main_favorite_db();
		}
	}
	
	String num = "empty";
	public void check_podcast_main_favorite_db(String get_num, Button bt_favorite){
		Cursor cursor;
		try{
			cursor = podcast_main_favorite_db.getReadableDatabase().rawQuery(
					"select * from favorite_list where num = '"+get_num+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				num = cursor.getString(cursor.getColumnIndex("num"));
			}else{
				num = "empty";
			}
			if(num.equals("empty")){
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_normal);
			}else{
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_pressed);	
			}
		}catch (Exception e) {
		}finally{
			close_podcast_main_favorite_db();
		}
	}
	
	public void insert_delete_podcast_main_favorite_db(String id,String get_num, String title, String provider,String imageurl,String rssurl, String udate, Button bt_favorite){
		Cursor cursor;
		try{
			cursor = podcast_main_favorite_db.getReadableDatabase().rawQuery(
					"select * from favorite_list where num = '"+get_num+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				num = cursor.getString(cursor.getColumnIndex("num"));
			}else{
				num = "empty";
			}
			if(num.equals("empty")){
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_pressed);
				ContentValues cv = new ContentValues();
				cv.put("id",id);
				cv.put("num",get_num);
				cv.put("title",title);
				cv.put("provider",provider);
				cv.put("imageurl",imageurl);
				cv.put("rssurl",rssurl);
				cv.put("udate",udate);
				podcast_main_favorite_db.getWritableDatabase().insert("favorite_list", null, cv);
				Toast.makeText(mApp, mApp.getString(R.string.frg_podcast_06), Toast.LENGTH_SHORT).show();
			}else{
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_normal);
				podcast_main_favorite_db.getWritableDatabase().delete("favorite_list", "num" + "=" +num, null);
				Toast.makeText(mApp, mApp.getString(R.string.frg_podcast_07), Toast.LENGTH_SHORT).show();
			}
			
		}finally{
			close_podcast_main_favorite_db();
		}
	}
	
	public void close_podcast_main_favorite_db(){
		if(podcast_main_favorite_db != null){
			podcast_main_favorite_db.close();
		}
	}
	
	public void check_bible_bookmark_db(Context context, String kwon, String jang, Button Bottom_07){
		String empty_kwon = "empty";
		String empty_jang = "empty";
		try{
			Cursor cursor = bible_bookmark_db.getReadableDatabase().rawQuery(
					"select * from bookmark_list where kwon = '"+kwon+"' and jang = '"+jang+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				empty_kwon = cursor.getString(cursor.getColumnIndex("kwon"));
				empty_jang = cursor.getString(cursor.getColumnIndex("jang"));
			}else{
				empty_kwon = "empty";
			}
			if(empty_kwon.equals("empty") && empty_jang.equals("empty")){
				Bottom_07.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_new_label, 0, 0);
			}else{
				Bottom_07.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_new_label_on, 0, 0);
			}
		}catch (Exception e) {
		}finally{
			close_bible_bookmark_db();
		}
	}
	
	public void insert_delete_bible_bookmark_db(Context context, String kwon, String jang, Button Bottom_07){
		String empty_kwon = "empty";
		String empty_jang = "empty";
		try{
			Cursor cursor = bible_bookmark_db.getReadableDatabase().rawQuery(
					"select * from bookmark_list where kwon = '"+kwon+"' and jang = '"+jang+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				empty_kwon = cursor.getString(cursor.getColumnIndex("kwon"));
				empty_jang = cursor.getString(cursor.getColumnIndex("jang"));
			}else{
				empty_kwon = "empty";
			}
			if(empty_kwon.equals("empty") && empty_jang.equals("empty")){
				Bottom_07.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_new_label_on, 0, 0);
				ContentValues cv = new ContentValues();
				cv.put("kwon", kwon);
				cv.put("jang", jang);
				bible_bookmark_db.getWritableDatabase().insert("bookmark_list", null, cv);
				Toast.makeText(mApp, mApp.getString(R.string.toast_bookmark_on), Toast.LENGTH_SHORT).show();
			}else{
				Bottom_07.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_new_label, 0, 0);
				bible_bookmark_db.getWritableDatabase().delete("bookmark_list", "kwon" + "='"+kwon+"' AND " + "jang" + "='"+jang+"'",null);
				Toast.makeText(mApp, mApp.getString(R.string.toast_bookmark_off), Toast.LENGTH_SHORT).show();
			}
		}finally{
			close_bible_bookmark_db();
		}
	}
	
	public void close_bible_bookmark_db(){
		if(bible_bookmark_db != null){
			bible_bookmark_db.close();
		}
	}
	
	
	public void delete_ccm_favorite_db(int _id){
		try{
			ccm_favorite_db.getWritableDatabase().delete("favorite_list", "_id" + "=" +_id, null);
		}catch (Exception e) {
		}finally{
			close_ccm_favorite_db();
		}
	}
	
	public void bind_ccm_favorite_db(ArrayList<Activity_Data_CCM_Favorite> list){
		try{
			Cursor cursor = ccm_favorite_db.getReadableDatabase().rawQuery("select * from favorite_list order by _id desc", null);
			while(cursor.moveToNext()){
				list.add(new Activity_Data_CCM_Favorite(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
			}	
		}catch (Exception e) {
		}finally{
			close_ccm_favorite_db();
		}
	}
	
	String id = "empty";
	public void check_ccm_favorite_db(Context context, String get_id, Button bt_favorite){
		try{
			Cursor cursor = ccm_favorite_db.getReadableDatabase().rawQuery(
					"select * from favorite_list where id = '"+get_id+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				id = cursor.getString(cursor.getColumnIndex("id"));
				_id = cursor.getInt(cursor.getColumnIndex("_id"));
			}else{
				id = "empty";
				_id = -1;
			}
			if(id.equals("empty")){
				bt_favorite.setText(context.getString(R.string.frg_ccm_05));
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_normal);
			}else{
				bt_favorite.setText(context.getString(R.string.frg_ccm_06));	
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_pressed);
			}
		}catch (Exception e) {
		}finally{
			close_ccm_favorite_db();
		}
	}
	
	public int _id = -1;
	public void insert_delete_ccm_favorite_db(Context context, String get_id,String title, String category, String thumbnail_hq, String duration, Button bt_favorite){
		try{
			Cursor cursor = ccm_favorite_db.getReadableDatabase().rawQuery(
					"select * from favorite_list where id = '"+get_id+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				id = cursor.getString(cursor.getColumnIndex("id"));
				_id = cursor.getInt(cursor.getColumnIndex("_id"));
			}else{
				id = "empty";
				_id = -1;
			}
			if(id.equals("empty")){
				bt_favorite.setText(context.getString(R.string.frg_ccm_06));
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_pressed);
				ContentValues cv = new ContentValues();
				cv.put("id", get_id);
				cv.put("title", title);
				cv.put("category", category);
				cv.put("thumbnail_hq", thumbnail_hq);
				cv.put("duration", duration);
				ccm_favorite_db.getWritableDatabase().insert("favorite_list", null, cv);
				Toast.makeText(context, context.getString(R.string.frg_ccm_03), Toast.LENGTH_SHORT).show();
			}else{
				bt_favorite.setText(context.getString(R.string.frg_ccm_05));
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_normal);
				ccm_favorite_db.getWritableDatabase().delete("favorite_list", "_id" + "=" +_id, null);
				Toast.makeText(context, context.getString(R.string.frg_ccm_04), Toast.LENGTH_SHORT).show();
			}
			
		}finally{
			close_ccm_favorite_db();
		}
	}
	
	public void close_ccm_favorite_db(){
		if(ccm_favorite_db != null){
			ccm_favorite_db.close();
		}
	}
	
	
	
	public void bind_prayer_db(ArrayList<Fragment_Data_Prayer> list){
	try{
		Cursor cursor;
		mdb_prayer = prayer_db.getReadableDatabase();
		if(PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which) == 7){
			if(PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_NEW_KYODOK_PRAYER_WHICH, Const.new_kyodok_prayer_which) == 0){
				cursor = mdb_prayer.rawQuery("select * from gidomun_list  WHERE gidomun_main_description = '"+Const.main_description_prayer[PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]+"'", null);
			}else{
				cursor = mdb_prayer.rawQuery("select * from gidomun_list  WHERE gidomun_main_description = '"+Const.main_description_prayer[PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]+"' and gidomun_sub_description = '"+Const.new_kyodok_description_prayer[PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_NEW_KYODOK_PRAYER_WHICH, Const.new_kyodok_prayer_which)]+"'", null);
			}
		}else if(PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which) == 8){
			if(PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_SIMBANG_PRAYER_WHICH, Const.simbang_prayer_which) == 0){
				cursor = mdb_prayer.rawQuery("select * from gidomun_list  WHERE gidomun_main_description = '"+Const.main_description_prayer[PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]+"'", null);
			}else{
				cursor = mdb_prayer.rawQuery("select * from gidomun_list  WHERE gidomun_main_description = '"+Const.main_description_prayer[PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]+"' and gidomun_sub_description = '"+Const.simbang_description_prayer[PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_SIMBANG_PRAYER_WHICH, Const.simbang_prayer_which)]+"'", null);
			}
		}else if(PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which) == 9){
			if(PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_OLD_KYODOK_PRAYER_WHICH, Const.old_kyodok_prayer_which) == 0){
				cursor = mdb_prayer.rawQuery("select * from gidomun_list  WHERE gidomun_main_description = '"+Const.main_description_prayer[PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]+"'", null);
			}else{
				cursor = mdb_prayer.rawQuery("select * from gidomun_list  WHERE gidomun_main_description = '"+Const.main_description_prayer[PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]+"' and gidomun_sub_description = '"+Const.old_kyodok_description_prayer[PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_OLD_KYODOK_PRAYER_WHICH, Const.old_kyodok_prayer_which)]+"'", null);
			}
		}else{
			if(Utils.language(mApp).equals("ko_KR")){
				cursor = mdb_prayer.rawQuery("select * from gidomun_list  WHERE gidomun_main_description = '"+Const.main_description_prayer[PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]+"'", null);	
			}else{
				cursor = mdb_prayer.rawQuery("select * from gidomun_list  WHERE gidomun_main_description = '"+Const.main_description_prayer_en[PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]+"'", null);
			}
		}
		while(cursor.moveToNext()){
			int idx = cursor.getColumnIndex("_id");
			int id = cursor.getInt(idx);
			list.add(new Fragment_Data_Prayer(id, cursor.getString(cursor.getColumnIndex("gidomun_main_description")),cursor.getString(cursor.getColumnIndex("gidomun_sub_description")), cursor.getString(cursor.getColumnIndex("gidomun_content"))));
		}	
	}catch (Exception e) {
	}finally{
		close_prayer_db();
		}
	}
	
	public void close_prayer_db(){
		if(prayer_db != null){
			prayer_db.close();
		}
	}
	
	public boolean check_note_db(StringBuffer strBuf){
		mdb_note = note_db.getReadableDatabase();
		Cursor cursor = mdb_note.rawQuery("select * from my_list", null);
		while(cursor.moveToNext()){
			if((cursor.getString(cursor.getColumnIndex("content")).equals(strBuf.toString() + ""))){
				return true;
			}
		}
		return false;
	}
	
	public void insert_note_db(ContentValues cv){
		note_db.getWritableDatabase().insert("my_list", null, cv);
	}
	
	public void update_note_db(ContentValues cv, int _id){
		note_db.getWritableDatabase().update("my_list", cv, "_id" + "=" + _id, null);
	}
	
	public void delete_note_db(int id){
		try{
			note_db = new DBOpenHelper_note(mApp);
			note_db.getWritableDatabase().delete("my_list", "_id" + "=" + id, null);
		}catch (Exception e) {
			close_note_db();
		}
	}
	
	public void close_note_db(){
		if(note_db != null){
			note_db.close();
		}
	}
	
	public void bible_voice_db_pause_task(int current_position, String kwon, String jang){
    	ContentValues cv = new ContentValues();
		try{
			Cursor cursor = voice_pause_db.getReadableDatabase().rawQuery("select * from voice_pause WHERE kwon = '"+kwon+"' AND jang = '"+jang+"'", null);
            if(cursor != null && cursor.moveToFirst()) {
            	int voice_currentPosition = cursor.getInt(cursor.getColumnIndex("voice_currentPosition"));
            	cv.put("kwon", kwon);
    			cv.put("voice_currentPosition", current_position);
    			cv.put("jang", jang);
    			voice_pause_db.getWritableDatabase().update("voice_pause", cv, "voice_currentPosition" + "=" + voice_currentPosition, null);
//    			Log.i("dsu", "DB_Update=======>" + kwon + " "+jang + "\ncurrent_position : " + current_position);
            }else{
            	cv.put("kwon", kwon);
            	cv.put("voice_currentPosition", current_position);
            	cv.put("jang", jang);
            	voice_pause_db.getWritableDatabase().insert("voice_pause", null, cv);
//            	Log.i("dsu", "DB_Insert=======>" + kwon + " "+jang + "\ncurrent_position : " + current_position);
            }
    	}catch (Exception e) {
		}finally{
			close_voice_pause_db();
		}
	}
	
	public void bible_voice_db_current_position(String kwon, String jang){
		try{
			Cursor cursor = voice_pause_db.getReadableDatabase().rawQuery("select * from voice_pause WHERE kwon = '"+kwon+"' AND jang = '"+jang+"'", null);
			if(cursor != null && cursor.moveToFirst()) {
				Const.current_position_bible =  cursor.getInt(cursor.getColumnIndex("voice_currentPosition"));
//				Log.i("dsu", "이어서재생==========> : " +  Const.current_position);
	       }else{
	    	   Const.current_position_bible =  0;
//	    	   Log.i("dsu", "처음부터재생=======> : " +  Const.current_position);
	       }
		}catch (Exception e) {
		}finally{
			close_voice_pause_db();
		}
	}
	
	public void close_voice_pause_db(){
		if(voice_pause_db != null){
			voice_pause_db.close();
		}
	}
	int bible_type_1;
	public void bible_db_init(){
    	if(Utils.language(mApp).equals("ko_KR")){
    		bible_type_1 = PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_BIBLE_TYPE_1, 0);	
    	}else{
    		bible_type_1 = PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_BIBLE_TYPE_1, 2);
    	}
//		Log.i("dsu", "bible_type_1 : " + bible_type_1);
		kkk_db = new DBOpenHelper_kkk(mApp);
		kbb_db = new DBOpenHelper_kbb(mApp);
		kjv_db = new DBOpenHelper_kjv(mApp);
		jpnnew_db = new DBOpenHelper_jpnnew(mApp);
		ckb_db = new DBOpenHelper_ckb(mApp);
		frenchdarby_db = new DBOpenHelper_frenchdarby(mApp);
		germanluther_db = new DBOpenHelper_germanluther(mApp);
		gst_db = new DBOpenHelper_gst(mApp);
		indonesianbaru_db = new DBOpenHelper_indonesianbaru(mApp);
		portugal_db = new DBOpenHelper_portugal(mApp);
		russiansynodal_db = new DBOpenHelper_russiansynodal(mApp);
		alb_db = new DBOpenHelper_alb(mApp);
		asv_db = new DBOpenHelper_asv(mApp);
		avs_db = new DBOpenHelper_avs(mApp);
		barun_db = new DBOpenHelper_barun(mApp);
		chb_db = new DBOpenHelper_chb(mApp);
		chg_db = new DBOpenHelper_chg(mApp);
		cjb_db = new DBOpenHelper_cjb(mApp);
		ckc_db = new DBOpenHelper_ckc(mApp);
		ckg_db = new DBOpenHelper_ckg(mApp);
		cks_db = new DBOpenHelper_cks(mApp);
		hebbhs_db = new DBOpenHelper_hebbhs(mApp);
		hebmod_db = new DBOpenHelper_hebmod(mApp);
		hebwlc_db = new DBOpenHelper_hebwlc(mApp);
		indianhindi_db = new DBOpenHelper_indianhindi(mApp);
		indiantamil_db = new DBOpenHelper_indiantamil(mApp);
		jpnold_db = new DBOpenHelper_jpnold(mApp);
		reina_db = new DBOpenHelper_reina(mApp);
		tagalog_db = new DBOpenHelper_tagalog(mApp);
		tkh_db = new DBOpenHelper_tkh(mApp);
		web_db = new DBOpenHelper_web(mApp);
		if(bible_type_1 == 0){
			mdb = kkk_db.getReadableDatabase();
		}else if(bible_type_1 == 1){
			mdb = kbb_db.getReadableDatabase();
		}else if(bible_type_1 == 2){
			mdb = kjv_db.getReadableDatabase(); 
		}else if(bible_type_1 == 3){
			mdb = jpnnew_db.getReadableDatabase(); 
		}else if(bible_type_1 == 4){
			mdb = ckb_db.getReadableDatabase(); 
		}else if(bible_type_1 == 5){
			mdb = frenchdarby_db.getReadableDatabase(); 
		}else if(bible_type_1 == 6){
			mdb = germanluther_db.getReadableDatabase(); 
		}else if(bible_type_1 == 7){
			mdb = gst_db.getReadableDatabase(); 
		}else if(bible_type_1 == 8){
			mdb = indonesianbaru_db.getReadableDatabase(); 
		}else if(bible_type_1 == 9){
			mdb = portugal_db.getReadableDatabase(); 
		}else if(bible_type_1 == 10){
			mdb = russiansynodal_db.getReadableDatabase(); 
		}else if(bible_type_1 == 11){
			mdb = alb_db.getReadableDatabase(); 
		}else if(bible_type_1 == 12){
			mdb = asv_db.getReadableDatabase(); 
		}else if(bible_type_1 == 13){
			mdb = avs_db.getReadableDatabase(); 
		}else if(bible_type_1 == 14){
			mdb = barun_db.getReadableDatabase(); 
		}else if(bible_type_1 == 15){
			mdb = chb_db.getReadableDatabase(); 
		}else if(bible_type_1 == 16){
			mdb = chg_db.getReadableDatabase(); 
		}else if(bible_type_1 == 17){
			mdb = cjb_db.getReadableDatabase(); 
		}else if(bible_type_1 == 18){
			mdb = ckc_db.getReadableDatabase(); 
		}else if(bible_type_1 == 19){
			mdb = ckg_db.getReadableDatabase(); 
		}else if(bible_type_1 == 20){
			mdb = cks_db.getReadableDatabase(); 
		}else if(bible_type_1 == 21){
			mdb = hebbhs_db.getReadableDatabase(); 
		}else if(bible_type_1 == 22){
			mdb = hebmod_db.getReadableDatabase(); 
		}else if(bible_type_1 == 23){
			mdb = hebwlc_db.getReadableDatabase(); 
		}else if(bible_type_1 == 24){
			mdb = indianhindi_db.getReadableDatabase(); 
		}else if(bible_type_1 == 25){
			mdb = indiantamil_db.getReadableDatabase(); 
		}else if(bible_type_1 == 26){
			mdb = jpnold_db.getReadableDatabase(); 
		}else if(bible_type_1 == 27){
			mdb = reina_db.getReadableDatabase(); 
		}else if(bible_type_1 == 28){
			mdb = tagalog_db.getReadableDatabase(); 
		}else if(bible_type_1 == 29){
			mdb = tkh_db.getReadableDatabase(); 
		}else if(bible_type_1 == 30){
			mdb = web_db.getReadableDatabase(); 
		}
		
		int bible_type_2 = PreferenceUtil.getIntSharedData(mApp, PreferenceUtil.PREF_BIBLE_TYPE_2, 0);
		if(bible_type_2 == 1){
			mdb2 = kjv_db.getReadableDatabase(); 
		}else if(bible_type_2 == 2){
			mdb2 = kkk_db.getReadableDatabase(); 
		}else if(bible_type_2 == 3){
			mdb2 = kbb_db.getReadableDatabase(); 
		}else if(bible_type_2 == 4){
			mdb2 = jpnnew_db.getReadableDatabase(); 
		}else if(bible_type_2 == 5){
			mdb2 = ckb_db.getReadableDatabase(); 
		}else if(bible_type_2 == 6){
			mdb2 = frenchdarby_db.getReadableDatabase(); 
		}else if(bible_type_2 == 7){
			mdb2 = germanluther_db.getReadableDatabase(); 
		}else if(bible_type_2 == 8){
			mdb2 = gst_db.getReadableDatabase(); 
		}else if(bible_type_2 == 9){
			mdb2 = indonesianbaru_db.getReadableDatabase(); 
		}else if(bible_type_2 == 10){
			mdb2 = portugal_db.getReadableDatabase(); 
		}else if(bible_type_2 == 11){
			mdb2 = russiansynodal_db.getReadableDatabase(); 
		}else if(bible_type_2 == 12){
			mdb2 = alb_db.getReadableDatabase(); 
		}else if(bible_type_2 == 13){
			mdb2 = asv_db.getReadableDatabase(); 
		}else if(bible_type_2 == 14){
			mdb2 = avs_db.getReadableDatabase(); 
		}else if(bible_type_2 == 15){
			mdb2 = barun_db.getReadableDatabase(); 
		}else if(bible_type_2 == 16){
			mdb2 = chb_db.getReadableDatabase(); 
		}else if(bible_type_2 == 17){
			mdb2 = chg_db.getReadableDatabase(); 
		}else if(bible_type_2 == 18){
			mdb2 = cjb_db.getReadableDatabase(); 
		}else if(bible_type_2 == 19){
			mdb2 = ckc_db.getReadableDatabase(); 
		}else if(bible_type_2 == 20){
			mdb2 = ckg_db.getReadableDatabase(); 
		}else if(bible_type_2 == 21){
			mdb2 = cks_db.getReadableDatabase(); 
		}else if(bible_type_2 == 22){
			mdb2 = hebbhs_db.getReadableDatabase(); 
		}else if(bible_type_2 == 23){
			mdb2 = hebmod_db.getReadableDatabase(); 
		}else if(bible_type_2 == 24){
			mdb2 = hebwlc_db.getReadableDatabase(); 
		}else if(bible_type_2 == 25){
			mdb2 = indianhindi_db.getReadableDatabase(); 
		}else if(bible_type_2 == 26){
			mdb2 = indiantamil_db.getReadableDatabase(); 
		}else if(bible_type_2 == 27){
			mdb2 = jpnold_db.getReadableDatabase(); 
		}else if(bible_type_2 == 28){
			mdb2 = reina_db.getReadableDatabase(); 
		}else if(bible_type_2 == 29){
			mdb2 = tagalog_db.getReadableDatabase(); 
		}else if(bible_type_2 == 30){
			mdb2 = tkh_db.getReadableDatabase(); 
		}else if(bible_type_2 == 31){
			mdb2 = web_db.getReadableDatabase(); 
		}
	}

	public void close_bible_db(){
		if(mdb != null){
			mdb.close();
		}
		if(kkk_db != null){
    		kkk_db.close();
    	}
    	if(kbb_db != null){
    		kbb_db.close();
    	}
    	if(kjv_db != null){
    		kjv_db.close();
    	}
    	if(jpnnew_db != null){
    		jpnnew_db.close();
    	}
		if(ckb_db != null){
			ckb_db.close();
		}
		if(frenchdarby_db != null){
			frenchdarby_db.close();
		}
		if(germanluther_db != null){
			germanluther_db.close();
		}
		if(gst_db != null){
			gst_db.close();
		}
		if(indonesianbaru_db != null){
			indonesianbaru_db.close();
		}
		if(portugal_db != null){
			portugal_db.close();
		}
		if(russiansynodal_db != null){
			russiansynodal_db.close();
		}
		if(alb_db != null){
			alb_db.close();
		}
		if(asv_db != null){
			asv_db.close();
		}
		if(avs_db != null){
			avs_db.close();
		}
		if(barun_db != null){
			barun_db.close();
		}
		if(chb_db != null){
			chb_db.close();
		}
		if(chg_db != null){
			chg_db.close();
		}
		if(cjb_db != null){
			cjb_db.close();
		}
		if(ckc_db != null){
			ckc_db.close();
		}
		if(ckg_db != null){
			ckg_db.close();
		}
		if(cks_db != null){
			cks_db.close();
		}
		if(hebbhs_db != null){
			hebbhs_db.close();
		}
		if(hebmod_db != null){
			hebmod_db.close();
		}
		if(hebwlc_db != null){
			hebwlc_db.close();
		}
		if(indianhindi_db != null){
			indianhindi_db.close();
		}
		if(indiantamil_db != null){
			indiantamil_db.close();
		}
		if(jpnold_db != null){
			jpnold_db.close();
		}
		if(reina_db != null){
			reina_db.close();
		}
		if(tagalog_db != null){
			tagalog_db.close();
		}
		if(tkh_db != null){
			tkh_db.close();
		}
		if(web_db != null){
			web_db.close();
		}
	}

	/**
	 * 네트워크 상태를 로드한다.
	 */
	public void loadNetStatus(){
	}
}
