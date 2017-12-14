package shalom.bible.hymn.cion.db.helper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper_reina extends SQLiteOpenHelper {
	public DBOpenHelper_reina(Context context) {
		super(context, "reina.db", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		}		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               		
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exits bible"); 
		onCreate(db);
	}
}