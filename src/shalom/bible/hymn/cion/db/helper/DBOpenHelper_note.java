package shalom.bible.hymn.cion.db.helper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper_note extends SQLiteOpenHelper {
	public DBOpenHelper_note(Context context) {
		super(context, "bible_favorite_list.db", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
			String createtable = "create table my_list ( _id integer primary key autoincrement,"+
			"kwon text, jang text, jul text, content text);";
			db.execSQL(createtable);
		}		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               		
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exits my_list"); 
		onCreate(db);
	}
}