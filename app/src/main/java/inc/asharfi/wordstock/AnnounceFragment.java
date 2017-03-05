package inc.asharfi.wordstock;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AnnounceFragment extends Fragment {

	private MainActivity parentActivity;
	private View rootView;
	private LayoutInflater rootInflater;


	@Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {
	    parentActivity = (MainActivity) this.getActivity();
		rootInflater = inflater;
		WindowManager wm = (WindowManager) parentActivity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

	    rootView = rootInflater.inflate(R.layout.fragment_announce, container, false);
	    
	    TextView tv = (TextView) rootView.findViewById(R.id.title);
	    tv.setText("Do you have American dream?");
	    tv.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Bold.ttf"));
	    
	    Button btn = (Button) rootView.findViewById(R.id.register);
	    btn.setText("Register & save 200");
	    btn.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Bold.ttf"));
	    btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.craduate.com");
			    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			    startActivity(intent);
			}
		});
	    
	    btn = (Button) rootView.findViewById(R.id.survey);
	    btn.setText("Let us know your requirements");
	    btn.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Bold.ttf"));
	    btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.craduate.com/student-survey");
			    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			    startActivity(intent);
			}
		});

	    ImageButton ib = (ImageButton) rootView.findViewById(R.id.img_btn);
	    
	    ib.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.craduate.com");
			    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			    startActivity(intent);
			}
		});

		System.gc();

        return rootView;
    }
}