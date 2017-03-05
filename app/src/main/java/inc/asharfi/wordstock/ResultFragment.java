package inc.asharfi.wordstock;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultFragment extends Fragment {

	private MainActivity parentActivity;
	private View rootView;
	private LayoutInflater rootInflater;
	private int width;
	private Test mTest;
	private int textColor = Color.WHITE;
	//private int textShadow = Color.argb(180, 0, 0, 0);
	private int textShadow = Color.DKGRAY;
	private InterstitialAd mInterstitialAd;
	final int fontSize = 18;

	
	public void setData(Test test) {
		mTest = test;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {
	    String time;
	    float correctness = (float) mTest.getCorrectCount() / (float) mTest.getTotalCount();
		float skipped =  (float) ((mTest.getTotalCount() - mTest.getAttemptCount()) + mTest.getCorrectCount()) / (float) mTest.getTotalCount();
		parentActivity = (MainActivity) this.getActivity();
		rootInflater = inflater;
		WindowManager wm = (WindowManager) parentActivity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		float barSize = width - 60;

		if (mTest.getTotalCount() > 5)
			parentActivity.getTestDataBase().addTest(mTest);

	    rootView = rootInflater.inflate(R.layout.fragment_result, container, false);
	    rootView.setBackgroundColor(Color.TRANSPARENT);

	    mInterstitialAd = new InterstitialAd(parentActivity);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        //requestNewInterstitial();

	    if (parentActivity.getFlag("night_mode")) {
	    	textColor = Color.WHITE;
	    	textShadow = Color.BLACK;
	    }

	    if (Long.parseLong(mTest.getSpan()) / 3600 > 0) {
	    	time = (Long.parseLong(mTest.getSpan())/3600) + ":" + ((Long.parseLong(mTest.getSpan())%60)/60) + ":" + ((Long.parseLong(mTest.getSpan())%60)%60) + " hrs";
	    } else if (Long.parseLong(mTest.getSpan())/60 > 0) {
	    	time = ((Long.parseLong(mTest.getSpan())/60)) + ":" + (Long.parseLong(mTest.getSpan())%60) + " mins";
	    } else {
	    	time = Long.parseLong(mTest.getSpan()) + " secs";
	    }

	    Typeface cfRegular = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf");
	    Typeface cfBold = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Bold.ttf");

	    TextView heading = (TextView) rootView.findViewById(R.id.heading_text);
		heading.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    heading.setTextColor(textColor);
	    heading.setTypeface(cfBold);
	    heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

	    if (mTest.getTotalCount() < 6)
	    	heading.setText("Try more ..");
	    else if ((correctness * 100) < 30)
	    	heading.setText("Buckle up !");
	    else if ((correctness * 100) < 70)
	    	heading.setText("Keep it up !");
	    else if ((correctness * 100) < 100)
	    	heading.setText("Way to go !");
	    else if ((correctness * 100) == 100)
	    	heading.setText("Excellent .. Congratulations !!");

	    // ============================================ Titles =========================================
	    TextView tv = ((TextView) rootView.findViewById(R.id.time_title));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfBold);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

	    tv = ((TextView) rootView.findViewById(R.id.span_title));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfBold);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

	    tv = ((TextView) rootView.findViewById(R.id.correct_title));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfBold);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

	    tv = ((TextView) rootView.findViewById(R.id.attempt_title));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfBold);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

	    tv = ((TextView) rootView.findViewById(R.id.total_title));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfBold);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	    tv.setText(Html.fromHtml("<u>" + tv.getText() + "</u>"));

	    if (mTest.getIncorrectWords().equals("nil")) {
	    	rootView.findViewById(R.id.to_learn_title).setVisibility(View.GONE);
	    } else {
		    tv = ((TextView) rootView.findViewById(R.id.to_learn_title));
			tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		    tv.setVisibility(View.VISIBLE);
		    tv.setTypeface(cfBold);
		    tv.setTextColor(textColor);
		    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
		    tv.setText("\t\r\n" + tv.getText());
	    }

	    // ============================================ Colons =========================================
	    tv = ((TextView) rootView.findViewById(R.id.time_colon));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfBold);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

	    tv = ((TextView) rootView.findViewById(R.id.span_colon));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfBold);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

	    tv = ((TextView) rootView.findViewById(R.id.correct_colon));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfBold);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

	    tv = ((TextView) rootView.findViewById(R.id.attempt_colon));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfBold);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

	    tv = ((TextView) rootView.findViewById(R.id.total_colon));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfBold);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

	    if (mTest.getIncorrectWords().equals("nil")) {
	    	rootView.findViewById(R.id.to_learn_colon).setVisibility(View.GONE);
	    } else {
		    tv = ((TextView) rootView.findViewById(R.id.to_learn_colon));
			tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		    tv.setVisibility(View.VISIBLE);
		    tv.setTypeface(cfBold);
		    tv.setTextColor(textColor);
		    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
		    tv.setText("\t\r\n" + tv.getText());
	    }

	    // ============================================ Texts =========================================
	    tv = ((TextView) rootView.findViewById(R.id.time_text));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfRegular);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
	    tv.setText("\t" + mTest.getDate());

	    tv = ((TextView) rootView.findViewById(R.id.span_text));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfRegular);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
	    tv.setText("\t" + time);

	    tv = ((TextView) rootView.findViewById(R.id.correct_text));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfRegular);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
	    tv.setText("\t" + mTest.getCorrectCount());

	    tv = ((TextView) rootView.findViewById(R.id.attempt_text));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfRegular);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
	    tv.setText("\t" + mTest.getAttemptCount());

	    tv = ((TextView) rootView.findViewById(R.id.total_text));
		tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
	    tv.setTypeface(cfRegular);
	    tv.setTextColor(textColor);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
	    tv.setText("\t" + mTest.getTotalCount());

	    if (mTest.getIncorrectWords().equals("nil")) {
	    	rootView.findViewById(R.id.to_learn_text).setVisibility(View.GONE);
	    } else {
		    tv = ((TextView) rootView.findViewById(R.id.to_learn_text));
			tv.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		    tv.setVisibility(View.VISIBLE);
		    tv.setTypeface(cfRegular);
		    tv.setTextColor(textColor);
		    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
		    tv.setText("\t\r\n\t" + mTest.getIncorrectWords());
	    }

	    Bitmap bitmap = Bitmap.createBitmap((width - 40), ((140  * width) / 1080), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);

	    ImageView drawingImageView = (ImageView) rootView.findViewById(R.id.result_bar);
		drawingImageView.setImageBitmap(bitmap);

        // Draw negative feedback
		Paint negativeBar = new Paint(Paint.ANTI_ALIAS_FLAG);
		negativeBar.setColor(Color.rgb(238, 0, 0));
		negativeBar.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), Color.argb(255, 0, 0, 0));
		drawingImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, negativeBar);
		if (!(correctness == 100 || skipped == 100 || (correctness + skipped) == 100))
			canvas.drawRect(20, 20, barSize, ((120 * width) / 1080), negativeBar);

		// Draw dont care feedback
        Paint dontCareBar = new Paint(Paint.ANTI_ALIAS_FLAG);
        dontCareBar.setColor(Color.rgb(238, 118, 0));
        dontCareBar.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), Color.argb(100, 0, 0, 0));
		drawingImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, dontCareBar);
		if ((barSize * skipped) > 0)
			canvas.drawRect(20, 20, (barSize * skipped), ((120 * width) / 1080), dontCareBar);

		// Draw positive feedback
        Paint positiveBar = new Paint(Paint.ANTI_ALIAS_FLAG);
		positiveBar.setColor(Color.rgb(102, 205, 0));
		positiveBar.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), Color.argb(50, 0, 0, 0));
		drawingImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, positiveBar);
		if ((barSize * correctness) > 0)
			canvas.drawRect(20, 20, (barSize * correctness), ((120 * width) / 1080), positiveBar);

		System.gc();

        return rootView;
    }

	private void requestNewInterstitial() {
		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded () {
				mInterstitialAd.show();
			}
            @Override
            public void onAdClosed() {
                //requestNewInterstitial();
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(adRequest);
    }
}