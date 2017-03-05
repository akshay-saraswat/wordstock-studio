package inc.asharfi.wordstock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    final String PREFS_NAME = "MyPrefsFile";
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isTaskRoot()) {
        	finish();
        	return;
       	}

        setContentView(R.layout.activity_splash);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;

        TextView tv = (TextView) findViewById(R.id.desc_text);
        Typeface cf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Bold.ttf");
        tv.setTypeface(cf);
        tv.setTextColor(Color.WHITE);
        tv.setShadowLayer(((5 * width) / 1080), 2, 3, Color.DKGRAY);

        if (!getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        		.getString("PreviousAppVersion", getString(R.string.app_version))
        		.equals(getString(R.string.app_version))) {
			SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
			editor.putString("PreviousAppVersion", getString(R.string.app_version));
			editor.commit();
        }

        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}