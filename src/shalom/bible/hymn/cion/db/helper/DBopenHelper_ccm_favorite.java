package shalom.bible.hymn.cion.db.helper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBopenHelper_ccm_favorite extends SQLiteOpenHelper {
	public DBopenHelper_ccm_favorite(Context context) {
		super(context, "favorite_ccm.db", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
			String createtable = "create table favorite_list ( _id integer primary key autoincrement,"+
			"id text, title text, category text, thumbnail_hq text, duration text);";
			db.execSQL(createtable);

	}		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               		
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exits favorite_list"); 
		onCreate(db);
	}
}