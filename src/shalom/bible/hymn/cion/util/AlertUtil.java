package shalom.bible.hymn.cion.util;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.db.helper.DBopenHelper_podcast_download;

public class AlertUtil {
	public static void AlertShow(String msg, final Context context, final String title, final String enclosure, final int position, final String description_title, final DBopenHelper_podcast_download down_mydb, final String provider, final String image, final String pubDate, final String old_title) {
        AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(context);
        alert_internet_status.setCancelable(false);
        alert_internet_status.setMessage(msg);
        alert_internet_status.setPositiveButton(context.getString(R.string.activity_podcast_09),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	DownloadAsync downloadAsync = new DownloadAsync(context, title, enclosure,position, description_title, down_mydb, provider, image, pubDate, old_title);
                    	downloadAsync.execute();
                    	dialog.dismiss();
                    }
                });
        alert_internet_status.setNegativeButton(context.getString(R.string.activity_podcast_10), 
       		 new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        alert_internet_status.show();
	}
}
