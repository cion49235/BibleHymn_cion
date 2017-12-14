package shalom.bible.hymn.cion.activity;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import shalom.bible.hymn.cion.MainFragmentActivity;
import shalom.bible.hymn.cion.R;



public class IntroActivity extends Activity{
	public Handler handler;
	public Context context;
	public KBB_Async kbb_Async = null;
	public KJV_Async kjv_Async = null;
	public KKK_Async kkk_Async = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        context = this;
        String kkk_path = context.getString(R.string.txt_kkk_path);
        String kbb_path = context.getString(R.string.txt_kbb_path);
    	String kjv_path = context.getString(R.string.txt_kjv_path);
    	
    	kkk_Async = new KKK_Async(kkk_path);
    	kkk_Async.execute();
    	kbb_Async = new KBB_Async(kbb_path);
        kbb_Async.execute();
        kjv_Async = new KJV_Async(kjv_path);
        kjv_Async.execute();
        
        handler = new Handler();
        handler.postDelayed(runnable, 2000);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if(handler != null){
    		handler.removeCallbacks(runnable);
    	}
    	if(kbb_Async != null){
    		kbb_Async.cancel(true);
    	}
    	if(kjv_Async != null){
    		kjv_Async.cancel(true);
    	}
    	if(kkk_Async != null){
    		kkk_Async.cancel(true);
    	}
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    }
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    }
    
    public class KBB_Async extends AsyncTask<Boolean, Void, Boolean> {
    	String path;
    	File file;
		public KBB_Async(String path) {
			this.path = path;
		}
		@Override
		protected void onPreExecute() {
		}
		@Override
		protected Boolean doInBackground(Boolean... arg0) {
			try {
				file = new File(path);
			} catch (IllegalArgumentException e){
			
			} catch (Resources.NotFoundException e){
			
			} catch (StringIndexOutOfBoundsException e){
			
			} catch (RuntimeException e){
				
			} 
			return file.exists();
		}
 
		@Override
		protected void onPostExecute(Boolean Response) {
			MAKE_KBB_DB();
		}
	}
    
    public class KJV_Async extends AsyncTask<Boolean, Void, Boolean> {
    	String path;
    	File file;
		public KJV_Async(String path) {
			this.path = path;
		}
		@Override
		protected void onPreExecute() {
		}
		@Override
		protected Boolean doInBackground(Boolean... arg0) {
			try {
				file = new File(path);
			} catch (IllegalArgumentException e){
			
			} catch (Resources.NotFoundException e){
			
			} catch (StringIndexOutOfBoundsException e){
			
			} catch (RuntimeException e){
				
			} 
			return file.exists();
		}
 
		@Override
		protected void onPostExecute(Boolean Response) {
			MAKE_KJV_DB();
		}
	}
    
    public class KKK_Async extends AsyncTask<Boolean, Void, Boolean> {
    	String path;
    	File file;
		public KKK_Async(String path) {
			this.path = path;
		}
		@Override
		protected void onPreExecute() {
		}
		@Override
		protected Boolean doInBackground(Boolean... arg0) {
			try {
				file = new File(path);
			} catch (IllegalArgumentException e){
			
			} catch (Resources.NotFoundException e){
			
			} catch (StringIndexOutOfBoundsException e){
			
			} catch (RuntimeException e){
				
			} 
			return file.exists();
		}
 
		@Override
		protected void onPostExecute(Boolean Response) {
			MAKE_KKK_DB();
		}
	}
    
    public void MAKE_KBB_DB(){
    	AssetManager manager = context.getAssets();
        String folderPath = context.getString(R.string.txt_folder_path);
        String filePath = context.getString(R.string.txt_kbb_path);
        File folder = new File(folderPath);
        File file = new File(filePath);
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
                InputStream is = manager.open(context.getString(R.string.txt_input_kbb));
                BufferedInputStream bis = new BufferedInputStream(is);
                if (folder.exists()) {
                }else{
                        folder.mkdirs();
                }
                if (file.exists()) {
                        file.delete();
                        file.createNewFile();
                }
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                int read = -1;
                byte[] buffer = new byte[1024];
                while ((read = bis.read(buffer, 0, 1024)) != -1) {
                        bos.write(buffer, 0, read);
                }
                bos.flush();
                bos.close();
                fos.close();
                bis.close();
                is.close();
        } catch (IOException e) {
        } 
    }
    
    public void MAKE_KJV_DB(){
    	AssetManager manager = context.getAssets();
        String folderPath = context.getString(R.string.txt_folder_path);
        String filePath = context.getString(R.string.txt_kjv_path);
        File folder = new File(folderPath);
        File file = new File(filePath);
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
                InputStream is = manager.open(context.getString(R.string.txt_input_kjv));
                BufferedInputStream bis = new BufferedInputStream(is);
                if (folder.exists()) {
                }else{
                        folder.mkdirs();
                }
                if (file.exists()) {
                        file.delete();
                        file.createNewFile();
                }
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                int read = -1;
                byte[] buffer = new byte[1024];
                while ((read = bis.read(buffer, 0, 1024)) != -1) {
                        bos.write(buffer, 0, read);
                }
                bos.flush();
                bos.close();
                fos.close();
                bis.close();
                is.close();
        } catch (IOException e) {
        } 
    }
    
    public void MAKE_KKK_DB(){
    	AssetManager manager = context.getAssets();
        String folderPath = context.getString(R.string.txt_folder_path);
        String filePath = context.getString(R.string.txt_kkk_path);
        File folder = new File(folderPath);
        File file = new File(filePath);
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
                InputStream is = manager.open(context.getString(R.string.txt_input_kkk));
                BufferedInputStream bis = new BufferedInputStream(is);
                if (folder.exists()) {
                }else{
                        folder.mkdirs();
                }
                if (file.exists()) {
                        file.delete();
                        file.createNewFile();
                }
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                int read = -1;
                byte[] buffer = new byte[1024];
                while ((read = bis.read(buffer, 0, 1024)) != -1) {
                        bos.write(buffer, 0, read);
                }
                bos.flush();
                bos.close();
                fos.close();
                bis.close();
                is.close();
        } catch (IOException e) {
        } 
    }
    
    Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Intent intent = new Intent(context, MainFragmentActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			//fade_animation
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
	};
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(handler != null) handler.removeCallbacks(runnable);
		finish();
	}
}
