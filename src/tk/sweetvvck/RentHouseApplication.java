package tk.sweetvvck;

import tk.sweetvvck.utils.FileOptService;
import tk.sweetvvck.utils.MySharedPreferences;
import android.app.Application;

public class RentHouseApplication extends Application {

	private boolean loginFlag;
	
	@Override
	public void onCreate() {
		super.onCreate();
		MySharedPreferences.init_SP_Instance(this, "ShortRentHouse");
		loginFlag = MySharedPreferences.get_Boolean("loginFlag", false);
		FileOptService.init(this);
	}

	public boolean isLoginFlag() {
		return loginFlag;
	}
}
