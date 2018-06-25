package shalom.bible.hymn.cion;


import android.app.Application;
import shalom.bible.hymn.cion.util.NetworkHelper;

public class BibleHymnApp extends Application{
	private static BibleHymnApp INSTANCE			= null;
	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		INSTANCE = this;
	}

	/**
	 * Application을 리턴한다.
	 * @return
	 */
	public static BibleHymnApp getApplication(){
		return INSTANCE;
	}


	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {

		NetworkHelper.getInstance().close();

		INSTANCE = null;
		super.onTerminate();
	}
	
	public static BibleHymnApp getInstance() {
		return INSTANCE;
  }
}
