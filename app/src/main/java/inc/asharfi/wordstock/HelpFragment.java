package inc.asharfi.wordstock;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

public class HelpFragment extends Fragment {

	private MainActivity parentActivity;
	private View rootView;
	private LayoutInflater rootInflater;
	private int count;
	
	/*public HelpFragment(int count) {
		this.count = count;
	}*/

	@Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {
	    parentActivity = (MainActivity) this.getActivity();
		rootInflater = inflater;
		WindowManager wm = (WindowManager) parentActivity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

	    rootView = rootInflater.inflate(R.layout.fragment_help, container, false);
	    rootView.setBackgroundColor(Color.TRANSPARENT);

	    ImageView iv = (ImageView) rootView.findViewById(R.id.imageView);
	    switch (count) {
	    case 0:
	    	iv.setImageResource(R.drawable.help_1);
	    	break;
	    case 1:
	    	iv.setImageResource(R.drawable.help_2);
	    	break;
	    case 2:
	    	iv.setImageResource(R.drawable.help_3);
	    	break;
	    case 3:
	    	iv.setImageResource(R.drawable.help_4);
	    	break;
	    case 4:
	    	iv.setImageResource(R.drawable.help_5);
	    	break;
	    case 5:
	    	iv.setImageResource(R.drawable.help_6);
	    	break;
	    }

		System.gc();

        return rootView;
    }
}