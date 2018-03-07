//This is source code of favorite. Copyright?? Tarks. All Rights Reserved.
package shalom.bible.hymn.cion.favorite.page;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;
import com.admixer.InterstitialAd;
import com.admixer.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.RecordingService;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.favorite.GalleryView;
import shalom.bible.hymn.cion.favorite.connect.AsyncHttpTask;
import shalom.bible.hymn.cion.favorite.global.Global;
import shalom.bible.hymn.cion.favorite.global.Globalvariable;
import shalom.bible.hymn.cion.util.PreferenceUtil;

public class document_write extends SherlockActivity implements AdViewListener, InterstitialAdListener, OnClickListener {

	Button bt;
	String title;
	String content;
	String page_srl;
	String page_name;
	String status = "0";
	String doc_title;
	String doc_contents;
	
	EditText content_edittext, edittext_title;
	
	int REQ_CODE_PICK_PICTURE = 2;
	int IMAGE_EDIT = 3;
	int CAMERA_PIC_REQUEST = 4;
	int FILE_CODE = 5;
	boolean attach_exist =false;
	int file_kind = 0;
	Uri file_path;
	// 1 image 2 file
	// IMG
	Uri mImageUri;
	String externel_path;
	ArrayList<String> files = new ArrayList<String>();
	public static RelativeLayout ad_layout;
	boolean show_alert = false;
	private NativeExpressAdView admobNative;
	public static com.admixer.InterstitialAd interstialAd;
	private LinearLayout layout_photo, layout_file;
	ImageView mImageView;
	TextView txt_file_name;
	Context context;
	private MediaPlayer mMediaPlayer = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.favorite_document_write);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		context = this;
		layout_photo = (LinearLayout)findViewById(R.id.layout_photo);
		layout_file = (LinearLayout)findViewById(R.id.layout_file);
		
		mImageView = (ImageView) findViewById(R.id.iv_photo);
		txt_file_name = (TextView)findViewById(R.id.txt_file_name);
		
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "vuh3q0an");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/7757784963");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1711251362");
		show_alert = true;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSupportProgressBarIndeterminateVisibility(false);
		if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
			addBannerView();		
		}
//    	init_admob_naive();
		// Get Intent
		Intent intent = getIntent();
		page_srl = intent.getStringExtra("page_srl");
		page_name = intent.getStringExtra("page_name");
		doc_contents = intent.getStringExtra("doc_contents");
		edittext_title = (EditText)findViewById(R.id.edittext_title);
		content_edittext = (EditText) findViewById(R.id.editText1);
		mImageUri = intent.getParcelableExtra("image_uri");
		externel_path= getExternalCacheDir().getAbsolutePath() + "/";
		if(page_name != null) getSupportActionBar().setSubtitle(page_name);
		if(doc_contents != null) content_edittext.setText(doc_contents);
		if(mImageUri != null) confirmImage();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		admobNative.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		admobNative.resume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		admobNative.destroy();
		show_alert = false;
		if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
			addInterstitialView();
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	@Override
	public void onClick(View view) {
		
	}
	
	public void addInterstitialView() {
    	if(interstialAd == null) {
        	AdInfo adInfo = new AdInfo("0a5esrry");
//        	adInfo.setTestMode(false);
        	interstialAd = new com.admixer.InterstitialAd(this);
        	interstialAd.setAdInfo(adInfo, this);
        	interstialAd.setInterstitialAdListener(this);
        	interstialAd.startInterstitial();
    	}
    }
	
	public void addBannerView() {
    	AdInfo adInfo = new AdInfo("vuh3q0an");
    	adInfo.setTestMode(false);
        AdView adView = new AdView(this);
        adView.setAdInfo(adInfo, this);
        adView.setAdViewListener(this);
        ad_layout = (RelativeLayout)findViewById(R.id.ad_layout);
        if(ad_layout != null){
        	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ad_layout.addView(adView, params);	
        }
    }
	
	private void init_admob_naive(){
		RelativeLayout nativeContainer = (RelativeLayout) findViewById(R.id.admob_native);
		AdRequest adRequest = new AdRequest.Builder().build();	    
		admobNative = new NativeExpressAdView(this);
		admobNative.setAdSize(new AdSize(360, 100));
		admobNative.setAdUnitId("ca-app-pub-4637651494513698/6141450967");
		nativeContainer.addView(admobNative);
		admobNative.loadAd(adRequest);
	}
	
	public void PostAct() {
		// IF Sucessfull no timeout
		setSupportProgressBarIndeterminateVisibility(true);
		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("page_srl");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("title");
		Paramname.add("content");
		Paramname.add("permission");
		Paramname.add("status");
		Paramname.add("privacy");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("1");
		Paramvalue.add(page_srl);
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(Global.setValue(title));
		Paramvalue.add(Global.setValue(content));
		Paramvalue.add("3");
		Paramvalue.add(status);
		Paramvalue.add("0");
		
	
//Check attach exist
	if(attach_exist){
		String path = null;
		if(file_kind == 1){
		 path = getCacheDir().toString() + "/attach_image.jpg";
		//check image already exist
		}else if(file_kind == 3){
			path = PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_NAME_RECORDING, "");
			Log.i("dsu", "path : " + path);
		} else {
			path = Global.getPath(this, file_path);
		}
			files.add(path);
		
	}else{
		files = null;
	}

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/documents_app_write.php", mHandler, Paramname,
				Paramvalue, files, 1, 0);
	}

	public void FinishAct() {
		Intent intent = new Intent();
		this.setResult(RESULT_OK, intent);
		finish();
	}
	
	public void CancelWritingAlert(){
		// Alert
		AlertDialog.Builder builder = new AlertDialog.Builder(document_write.this);
		builder.setMessage(getString(R.string.cancel_writing_des)).setTitle(
				getString(R.string.warning));
		builder.setPositiveButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						finish();
						
					}
				});
		builder.setNegativeButton(getString(R.string.no),null);
		builder.show();
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// IF Sucessfull no timeout
			setSupportProgressBarIndeterminateVisibility(false);
			if (msg.what == -1) {
				if(show_alert) Global.ConnectionError(document_write.this);
			}

			if (msg.what == 1) {
				String result = msg.obj.toString();
				if (result.matches("document_write_succeed")) {
					FinishAct();
				} else {
					if(show_alert) Global.ConnectionError(document_write.this);
				}
				Log.i("Result", msg.obj.toString());
			}
		}
	};
	
	public void confirmImage(){
		Log.i("dsu", "CAMERA_PIC_REQUEST : " + mImageUri);
		Intent intent = new Intent(this, GalleryView.class);
		intent.putExtra("uri", mImageUri);
		intent.putExtra("edit", true);
		startActivityForResult(intent, IMAGE_EDIT);
		layout_photo.setVisibility(View.VISIBLE);
		layout_file.setVisibility(View.GONE);
		txt_file_name.setText("");
		if(mImageUri != null) mImageView.setImageURI(mImageUri);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			// setListAdapter();
			String result_status = data.getStringExtra("status");
			Log.i("dsu", "result_status : " + result_status);
			status = result_status;
		}
		
		if (requestCode == REQ_CODE_PICK_PICTURE && resultCode == Activity.RESULT_OK) {
			Log.i("dsu", "REQ_CODE_PICK_PICTURE : " + data.getData());
				Intent intent = new Intent(document_write.this, GalleryView.class);
				intent.putExtra("uri", data.getData());
				intent.putExtra("edit", true);
				startActivityForResult(intent, IMAGE_EDIT);
				layout_photo.setVisibility(View.VISIBLE);
				layout_file.setVisibility(View.GONE);
				txt_file_name.setText("");
				if(data.getData() != null) mImageView.setImageURI(data.getData());
		}

		if (requestCode == IMAGE_EDIT && resultCode == Activity.RESULT_OK) {
			// Log.i("Imageresult", "itsok");
			
				byte[] b = Globalvariable.image;
//				profile_bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//				// Log.i("datasetdata", data.getData().toString() + "ssdsd");
//				profile.setImageBitmap(profile_bitmap); 
//				// Profile changed
file_kind = 1;
				attach_exist = true;
				invalidateOptionsMenu();
//				// Set global image null
//				Globalvariable.image = null;
		}

		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
			// ImageView imageView;
			// ... some code to inflate/create/find appropriate ImageView to
			// place grabbed image
			// profile_bitmap = Global.grabImage(mImageUri);
			confirmImage();
		}
		
		if (requestCode == FILE_CODE && resultCode == RESULT_OK) {
			// ImageView imageView;
			// ... some code to inflate/create/find appropriate ImageView to
			// place grabbed image
			// profile_bitmap = Global.grabImage(mImageUri)
file_kind = 2;
			file_path = data.getData();
//			String file_name = file_path.toString().substring(file_path.toString().lastIndexOf("%2")+1);
			txt_file_name.setText(context.getString(R.string.txt_file_title) + data.getData().getLastPathSegment());
			Log.i("dsu", "file_name : " + data.getData().getLastPathSegment());
		//	Log.i("test", data.getDataString());
		//	Log.i("file", file_path);
			attach_exist = true;
			invalidateOptionsMenu();
			layout_file.setVisibility(View.VISIBLE);
			layout_photo.setVisibility(View.GONE);
		}
	}
	
	public String getFileName(Uri uri) {
		  String result = null;
		  if (uri.getScheme().equals("content")) {
		    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		    try {
		      if (cursor != null && cursor.moveToFirst()) {
		        result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
		      }
		    } finally {
		      cursor.close();
		    }
		  }
		  if (result == null) {
		    result = uri.getPath();
		    int cut = result.lastIndexOf('/');
		    if (cut != -1) {
		      result = result.substring(cut + 1);
		    }
		  }
		  return result;
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item;
	    SubMenu subMenu1 = menu.addSubMenu(getString(R.string.attach));
        subMenu1.add(0, 1001, 0, getString(R.string.camera));
        subMenu1.add(0, 1002, 0, getString(R.string.choose_picture));
        subMenu1.add(0, 1003, 0, getString(R.string.attach_file));
        subMenu1.add(0, 1004, 0, getString(R.string.attach_recording));
       // subMenu1.add(0, 1004, 0, "목록");
     if(attach_exist) subMenu1.add(0, 1005, 0, getString(R.string.delete));

        MenuItem subMenu1Item = subMenu1.getItem();
        
        menu.add(0, 3, 0, getString(R.string.attach_recording))
	    .setIcon(R.drawable.ic_action_mic)
	    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
		menu.add(0, 2, 0, getString(R.string.privacy_content))
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		subMenu1Item.setIcon(R.drawable.ic_action_new_picture);
	    subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
	    menu.add(0, 1, 0, getString(R.string.write))
	    .setIcon(R.drawable.accept)
	    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			if (Globalvariable.okbutton == true) {
				// Set ok button disable
				Globalvariable.okbutton = false;
				Global.ButtonEnable(1);
				setSupportProgressBarIndeterminateVisibility(true);
				
				title = edittext_title.getText().toString();
				content = content_edittext.getText().toString();

				if (!title.matches("")&&!content.matches("")) {
					PostAct();
				} else {
					setSupportProgressBarIndeterminateVisibility(false);
					Global.Infoalert(this, getString(R.string.warning),
							getString(R.string.no_content),
							getString(R.string.yes));
				}
			}

			return true;

		case 2:
			Intent intent1 = new Intent(document_write.this, privacy_category.class);
			intent1.putExtra("status", status);
			startActivityForResult(intent1, 1);

			return true;
		
		case 3:
			alert_recording();
			return true;			
		
		case 1001:
			int w, h;
			// Intent cameraIntent = new
			// Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// startActivityForResult(cameraIntent , CAMERA_PIC_REQUEST);

			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			File photo;
			try {
				// place where to store camera taken picture
				photo = Global.createTemporaryFile("picture", ".jpg");
				photo.delete();
			} catch (Exception e) {
				Global.toast(getString(R.string.no_storage_error));
				return false;
			}
			mImageUri = Uri.fromFile(photo);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
			// start camera intent
			this.startActivityForResult(intent, CAMERA_PIC_REQUEST);
			return true;
			
		case 1002:
			Intent i = new Intent(Intent.ACTION_PICK);
			i.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//			i.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // images
			startActivityForResult(i, REQ_CODE_PICK_PICTURE);
			return true;
			
		case 1003:
			Intent i1 = new Intent(Intent.ACTION_GET_CONTENT);
			i1.setType("*/*");
			startActivityForResult(i1, FILE_CODE);
			return true;
			
		case 1004:
			Log.i("dsu", "1004");
			alert_recording();
			return true;
			
		case 1005:
		layout_photo.setVisibility(View.GONE);
		txt_file_name.setText("");
		layout_file.setVisibility(View.GONE);
		attach_exist = false;
		invalidateOptionsMenu();
			return true;

		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	// Play start/stop
    private void onPlay(boolean isPlaying, String file_path){
        if (!isPlaying) {
            //currently MediaPlayer is not playing audio
            if(mMediaPlayer == null) {
            	Toast.makeText(context, context.getString(R.string.toast_media_volume), Toast.LENGTH_SHORT).show();
                startPlaying(file_path); //start from beginning
            } else {
                resumePlaying(); //resume the currently paused MediaPlayer
            }

        } else {
            //pause the MediaPlayer
            pausePlaying();
        }
    }
    
    private void startPlaying(String file_path) {
    	img_recording.setImageResource(R.drawable.ic_action_pause);
        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(file_path);
            mMediaPlayer.prepare();
//            mSeekBar.setMax(mMediaPlayer.getDuration());

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });

        updateSeekBar();
        //keep screen on while playing audio
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    private boolean isPlaying = false;
    private void stopPlaying() {
    	img_recording.setImageResource(R.drawable.ic_action_play);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
//        mSeekBar.setProgress(mSeekBar.getMax());
        isPlaying = !isPlaying;
        chronometer.setText("00:00");
//        mSeekBar.setProgress(mSeekBar.getMax());
        //allow the screen to turn off again once audio is finished playing
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    private void resumePlaying() {
    	img_recording.setImageResource(R.drawable.ic_action_pause);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.start();
        updateSeekBar();
    }
    
    private void pausePlaying() {
    	img_recording.setImageResource(R.drawable.ic_action_play);
        mHandler.removeCallbacks(mRunnable);
        if(mMediaPlayer != null) mMediaPlayer.pause();
    }
    
  //updating mSeekBar
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mMediaPlayer != null){

                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
//                mSeekBar.setProgress(mCurrentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);
                chronometer.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };
    
    private void updateSeekBar() {
        mHandler.postDelayed(mRunnable, 1000);
    }
	
	// Recording Start/Stop
    //TODO: recording pause
    private void onRecord(boolean start){
        Intent intent = new Intent(context, RecordingService.class);
        if (start) {
            // start recording
        	img_recording.setImageResource(R.drawable.ic_action_recording_stop);
            //mPauseButton.setVisibility(View.VISIBLE);
            Toast.makeText(context,R.string.toast_recording_start,Toast.LENGTH_SHORT).show();
            File folder = new File(Environment.getExternalStorageDirectory() + "/Shalom_Bible");
            if (!folder.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                folder.mkdir();
            }

            //start Chronometer
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    if (mRecordPromptCount == 0) {
                    	txt_recording.setText(getString(R.string.record_in_progress) + ".");
                    } else if (mRecordPromptCount == 1) {
                    	txt_recording.setText(getString(R.string.record_in_progress) + "..");
                    } else if (mRecordPromptCount == 2) {
                    	txt_recording.setText(getString(R.string.record_in_progress) + "...");
                        mRecordPromptCount = -1;
                    }
                    mRecordPromptCount++;
                }
            });

            //start RecordingService
            startService(intent);
            //keep screen on while recording
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            txt_recording.setText(getString(R.string.record_in_progress) + ".");
            mRecordPromptCount++;

        } else {
        	alert_button.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        	alert_button.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(true);
            //stop recording
        	img_recording.setImageResource(R.drawable.ic_action_play);
            //mPauseButton.setVisibility(View.GONE);
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            timeWhenPaused = 0;
            txt_recording.setText(getString(R.string.play_prompt));
            stopService(intent);
            //allow the screen to turn off again once recording is finished
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            chronometer.setText("00:00");
        }
    }
    long timeWhenPaused = 0; //stores time when user clicks pause button
    private int mRecordPromptCount = 0;
    Chronometer chronometer;
    TextView txt_recording;
    ImageView img_recording;
    private boolean mStartRecording = true;
    private int SDK_INT = android.os.Build.VERSION.SDK_INT;
	private void alert_recording(){
		Log.i("dsu", "alert_recording");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.favorite_alert_recording, null);
		alert.setCancelable(false);
		alert.setView(view);
		chronometer = (Chronometer)view.findViewById(R.id.chronometer);
		txt_recording = (TextView)view.findViewById(R.id.txt_recording);
		img_recording = (ImageView)view.findViewById(R.id.img_recording);
		img_recording.setFocusable(false);
		img_recording.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i("dsu", "onClick");
				if(PreferenceUtil.getBooleanSharedData(context, PreferenceUtil.PREF_DO_RECORDING, true) == false){
					File file = new File(PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_NAME_RECORDING, ""));
					if(file.exists() == true) {
						 onPlay(isPlaying, PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_NAME_RECORDING, ""));
						 isPlaying = !isPlaying;
					}
		    		return;
		    	}else{
		    		if (SDK_INT >= Build.VERSION_CODES.M){ 
		    			checkPermission();	
		    		}else{
		    			onRecord(mStartRecording);
		                mStartRecording = !mStartRecording;
		    		}
		    		
		    	}
			}
		});
		
		alert.setNeutralButton(context.getString(R.string.alert_record_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int sumthin) {
            	File file = new File(PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_NAME_RECORDING, ""));
				if(file.exists() == true) {
					file.delete();
				}
				layout_photo.setVisibility(View.GONE);
				txt_file_name.setText("");
				layout_file.setVisibility(View.GONE);
				attach_exist = false;
				invalidateOptionsMenu();
            	dialog.dismiss();
            	PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_DO_RECORDING, true);
            }
        });
		
		alert.setPositiveButton(context.getString(R.string.alert_record_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int sumthin) {
            	file_kind = 3;
            	attach_exist = true;
            	invalidateOptionsMenu();
    			layout_file.setVisibility(View.VISIBLE);
    			layout_photo.setVisibility(View.GONE);
            	PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_DO_RECORDING, true);
            	txt_file_name.setText(context.getString(R.string.txt_file_title) + PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_NAME_RECORDING, ""));
            	dialog.dismiss();
            }
        });
		alert_button = alert.show();
		alert_button.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
		alert_button.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(false);
	}
	AlertDialog alert_button;
	
	private int MY_PERMISSION_REQUEST_STORAGE = 0;
	private void checkPermission() {
	    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
	            != PackageManager.PERMISSION_GRANTED
	            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
	            != PackageManager.PERMISSION_GRANTED
	            || checkSelfPermission(Manifest.permission.RECORD_AUDIO)
	            != PackageManager.PERMISSION_GRANTED) {
	        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
	        	Toast.makeText(context, context.getString(R.string.permission_ment), Toast.LENGTH_LONG).show();
	        }
	        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
	                MY_PERMISSION_REQUEST_STORAGE);
	    } else {
	    	onRecord(mStartRecording);
            mStartRecording = !mStartRecording;
	        // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
	    }
	}
	
	@Override
	public void onBackPressed() {
		if(content_edittext.getText().toString().matches("")){
			finish();
		}else{
			CancelWritingAlert();
		}
	}
	
	@Override
	public void onClickedAd(String arg0, AdView arg1) {
	}
	@Override
	public void onFailedToReceiveAd(int arg0, String arg1, AdView arg2) {
		
	}
	@Override
	public void onReceivedAd(String arg0, AdView arg1) {
		Log.i("dsu", "배너광고 : arg0 : " + arg0+"\n" + arg1) ;
	}		
	
	@Override
	public void onInterstitialAdFailedToReceive(int arg0, String arg1,
			InterstitialAd arg2) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdReceived(String arg0, InterstitialAd arg1) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdShown(String arg0, InterstitialAd arg1) {
	}

	@Override
	public void onLeftClicked(String arg0, InterstitialAd arg1) {
	}

	@Override
	public void onRightClicked(String arg0, InterstitialAd arg1) {
	}

	@Override
	public void onInterstitialAdClosed(InterstitialAd arg0) {
	}
}
