package inc.asharfi.wordstock;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SettingsFragment extends Fragment {

	private MainActivity parentActivity;
	private LayoutInflater rootInflater;
	private View rootView;
	private int bg;
	private int width;
	private int textShadow = Color.DKGRAY;
	private int textColor = Color.BLACK;
	public final String TAG = AssessmentFragment.class.getSimpleName();
	
	public SettingsFragment() {
	}

	@Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {
		parentActivity = (MainActivity) this.getActivity();
		rootInflater = inflater;
		//bg = ((MainActivity)parentActivity).getBG();
		WindowManager wm = (WindowManager) parentActivity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;

		rootView = inflater.inflate(R.layout.fragment_settings, container, false);

		final Button wallButton = (Button) rootView.findViewById(R.id.wallpaper_select);
		wallButton.setTextColor(Color.WHITE);
		if (parentActivity.getFlag("night_mode")) {
			wallButton.setVisibility(View.INVISIBLE);
    	} else {
    		wallButton.setVisibility(View.VISIBLE);
    		// if button is clicked, close the custom dialog
    		wallButton.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				showWallpaperList();
    			}
    		});
    	}

		//rootView.setBackgroundResource(bg);

		// =================================== flashcard_reverse_switch =============================
		((Switch) rootView.findViewById(R.id.flashcard_reverse_switch))
			.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					parentActivity.handleSwitch(buttonView);
				}
			});

		if (parentActivity.getFlag("flashcard_reverse"))
			((Switch) rootView.findViewById(R.id.flashcard_reverse_switch)).setChecked(true);

		// =================================== ws_random_switch =============================
		((Switch) rootView.findViewById(R.id.ws_random_switch))
			.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					parentActivity.handleSwitch(buttonView);
				}
			});

		if (parentActivity.getFlag("ws_random"))
			((Switch) rootView.findViewById(R.id.ws_random_switch)).setChecked(true);

		// =================================== ws_expand_definition_switch =============================
		((Switch) rootView.findViewById(R.id.ws_expand_definition_switch))
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				parentActivity.handleSwitch(buttonView);
			}
		});

		if (parentActivity.getFlag("ws_expand_definition"))
			((Switch) rootView.findViewById(R.id.ws_expand_definition_switch)).setChecked(true);

		// =================================== ws_expand_examples_switch =============================
		((Switch) rootView.findViewById(R.id.ws_expand_examples_switch))
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				parentActivity.handleSwitch(buttonView);
			}
		});

		if (parentActivity.getFlag("ws_expand_examples"))
			((Switch) rootView.findViewById(R.id.ws_expand_examples_switch)).setChecked(true);

		// =================================== ws_expand_synonyms_switch =============================
		((Switch) rootView.findViewById(R.id.ws_expand_synonyms_switch))
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				parentActivity.handleSwitch(buttonView);
			}
		});

		if (parentActivity.getFlag("ws_expand_synonyms"))
			((Switch) rootView.findViewById(R.id.ws_expand_synonyms_switch)).setChecked(true);

		// =================================== ws_expand_antonyms_switch =============================
		((Switch) rootView.findViewById(R.id.ws_expand_antonyms_switch))
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				parentActivity.handleSwitch(buttonView);
			}
		});

		if (parentActivity.getFlag("ws_expand_antonyms"))
			((Switch) rootView.findViewById(R.id.ws_expand_antonyms_switch)).setChecked(true);

		// =================================== ws_expand_origin_switch =============================
		((Switch) rootView.findViewById(R.id.ws_expand_origin_switch))
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				parentActivity.handleSwitch(buttonView);
			}
		});

		if (parentActivity.getFlag("ws_expand_origin"))
			((Switch) rootView.findViewById(R.id.ws_expand_origin_switch)).setChecked(true);

		// =================================== ws_speak_definition_switch =============================
		((Switch) rootView.findViewById(R.id.ws_speak_definition_switch))
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				parentActivity.handleSwitch(buttonView);
			}
		});

		if (parentActivity.getFlag("ws_speak_definition"))
			((Switch) rootView.findViewById(R.id.ws_speak_definition_switch)).setChecked(true);
		
		// =================================== ws_speak_example_switch =============================
		((Switch) rootView.findViewById(R.id.ws_speak_example_switch))
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				parentActivity.handleSwitch(buttonView);
			}
		});

		if (parentActivity.getFlag("ws_speak_example"))
			((Switch) rootView.findViewById(R.id.ws_speak_example_switch)).setChecked(true);

		// =================================== ws_speak_synonym_switch =============================
				((Switch) rootView.findViewById(R.id.ws_speak_synonym_switch))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						parentActivity.handleSwitch(buttonView);
					}
				});

				if (parentActivity.getFlag("ws_speak_synonym"))
					((Switch) rootView.findViewById(R.id.ws_speak_synonym_switch)).setChecked(true);

		// =================================== ws_speak_antonym_switch =============================
		((Switch) rootView.findViewById(R.id.ws_speak_antonym_switch))
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				parentActivity.handleSwitch(buttonView);
			}
		});

		if (parentActivity.getFlag("ws_speak_antonym"))
			((Switch) rootView.findViewById(R.id.ws_speak_antonym_switch)).setChecked(true);

		// =================================== ws_speak_origin_switch =============================
				((Switch) rootView.findViewById(R.id.ws_speak_origin_switch))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						parentActivity.handleSwitch(buttonView);
					}
				});

				if (parentActivity.getFlag("ws_speak_origin"))
					((Switch) rootView.findViewById(R.id.ws_speak_origin_switch)).setChecked(true);

		// =================================== night_mode_switch =============================
		((Switch) rootView.findViewById(R.id.night_mode_switch))
			.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked)
						wallButton.setVisibility(View.GONE);
					else
						wallButton.setVisibility(View.VISIBLE);
					parentActivity.handleSwitch(buttonView);
				}
			});

		if (parentActivity.getFlag("night_mode")) { 
			((Switch) rootView.findViewById(R.id.night_mode_switch)).setChecked(true);
		}

		Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf");
		Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Bold.ttf");

		((TextView) rootView.findViewById(R.id.ws_header)).setTypeface(tf_bold);
		((TextView) rootView.findViewById(R.id.ws_header)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_header)).setText(Html.fromHtml("<b>Word Stock</b>"));
		((TextView) rootView.findViewById(R.id.ws_random)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.ws_random)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_random)).setText(Html.fromHtml("<b>Random words</b>"));
		((TextView) rootView.findViewById(R.id.ws_expand_definition)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.ws_expand_definition)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_expand_definition)).setText(Html.fromHtml("<b>Expand definition</b>"));
		((TextView) rootView.findViewById(R.id.ws_expand_examples)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.ws_expand_examples)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_expand_examples)).setText(Html.fromHtml("<b>Expand examples</b>"));
		((TextView) rootView.findViewById(R.id.ws_expand_synonyms)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.ws_expand_synonyms)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_expand_synonyms)).setText(Html.fromHtml("<b>Expand synonyms</b>"));
		((TextView) rootView.findViewById(R.id.ws_expand_antonyms)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.ws_expand_antonyms)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_expand_antonyms)).setText(Html.fromHtml("<b>Expand antonyms</b>"));
		((TextView) rootView.findViewById(R.id.ws_expand_origin)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.ws_expand_origin)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_expand_origin)).setText(Html.fromHtml("<b>Expand origin</b>"));
		((TextView) rootView.findViewById(R.id.ws_speak_definition)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.ws_speak_definition)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_speak_definition)).setText(Html.fromHtml("<b>Speak definition</b>"));
		((TextView) rootView.findViewById(R.id.ws_speak_example)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.ws_speak_example)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_speak_example)).setText(Html.fromHtml("<b>Speak examples</b>"));
		((TextView) rootView.findViewById(R.id.ws_speak_antonym)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.ws_speak_antonym)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_speak_antonym)).setText(Html.fromHtml("<b>Speak antonyms</b>"));
		((TextView) rootView.findViewById(R.id.ws_speak_synonym)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.ws_speak_synonym)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_speak_synonym)).setText(Html.fromHtml("<b>Speak synonyms</b>"));
		((TextView) rootView.findViewById(R.id.ws_speak_origin)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.ws_speak_origin)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.ws_speak_origin)).setText(Html.fromHtml("<b>Speak origin</b>"));
		((TextView) rootView.findViewById(R.id.flashcard_header)).setTypeface(tf_bold);
		((TextView) rootView.findViewById(R.id.flashcard_header)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.flashcard_header)).setText(Html.fromHtml("<b>Flashcard Test</b>"));
		((TextView) rootView.findViewById(R.id.flashcard_reverse)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.flashcard_reverse)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.flashcard_reverse)).setText(Html.fromHtml("<b>Word as Question</b>"));
		((TextView) rootView.findViewById(R.id.misc_header)).setTypeface(tf_bold);
		((TextView) rootView.findViewById(R.id.misc_header)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.misc_header)).setText(Html.fromHtml("<b>Miscellaneous</b>"));
		((TextView) rootView.findViewById(R.id.night_mode)).setTypeface(tf);
		((TextView) rootView.findViewById(R.id.night_mode)).setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		((TextView) rootView.findViewById(R.id.night_mode)).setText(Html.fromHtml("<b>Night mode</b>"));

		reInit();
        return rootView;
    }

	@Override
	public void onHiddenChanged (boolean hidden) {
		super.onHiddenChanged(hidden);

		if (!hidden) {
	        //((MainActivity) this.getActivity()).changeActionSelector(false, false);
	        ((MainActivity) this.getActivity()).changeActionFinish(false, false);
	    	((MainActivity) this.getActivity()).changeActionButton("", false, false);

	    	reInit();
		}
	}

	public void cleanUp() {
		if (rootView != null)
			rootView.setBackgroundResource(0);
	}

	public void reInit() {
		if (rootView != null) {
			bg = ((MainActivity)this.getActivity()).getBG();
			rootView.setBackgroundResource(0);
			if (((MainActivity)this.getActivity()).getFlag("night_mode")) {
				rootView.setBackgroundColor(bg);
			} else {
				rootView.setBackgroundResource(bg);
			}
		}
	}

	public View getViewByPosition(int pos, ListView listView) {
	    final int firstListItemPosition = listView.getFirstVisiblePosition();
	    final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

	    if (pos < firstListItemPosition || pos > lastListItemPosition ) {
	        return listView.getAdapter().getView(pos, null, listView);
	    } else {
	        final int childIndex = pos - firstListItemPosition;
	        return listView.getChildAt(childIndex);
	    }
	}
	
	@SuppressLint("InflateParams")
	public void showWallpaperList() {
		Dialog builder = new Dialog(parentActivity);
	    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    builder.getWindow().setGravity(Gravity.CENTER);
		if (((MainActivity)this.getActivity()).getFlag("night_mode"))
			builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 51, 51, 51)));
		else
			builder.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
	        @Override
	        public void onDismiss(DialogInterface dialogInterface) {
	            //nothing;
	        }
	    });

	    View PopupLayout = rootInflater.inflate(R.layout.popup_list, null);

	    // =============================== Find ads ================================
	    /*final AdView mAdView = (AdView) PopupLayout.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
        	public void onAdClosed() {
        		mAdView.setVisibility(View.GONE);
        	}

        	public void onAdFailedToLoad(int errorCode) {
        			mAdView.setVisibility(View.GONE);
        	}

        	public void onAdLeftApplication() {
        		
        	}

        	public void onAdLoaded() {
        		mAdView.setVisibility(View.VISIBLE);
        	}

        	public void onAdOpened() {
        	}
		});*/

	    final ListView wallLV = (ListView) PopupLayout.findViewById(R.id.correct_list);
	    wallLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            	View prevView = getViewByPosition(((MainActivity)parentActivity).getInt("wall_selected_index"), wallLV);
            	prevView.findViewById(R.id.right_image).setVisibility(View.GONE);
            	view.findViewById(R.id.right_image).setVisibility(View.VISIBLE);
            	((MainActivity)parentActivity).setInt("wall_selected_index", position);
            	rootView.setBackgroundResource(((MainActivity)parentActivity).getBG());
            }
       });

	    ArrayAdapter<Tile> correctAdapter = new ArrayAdapter<Tile>(parentActivity.getApplicationContext(),
				R.layout.nav_drawer_item, R.id.item_text, parentActivity.getWallpapers()) {

				   public View getView(int position, View convertView, ViewGroup container) {

					   if (convertView == null)
						   convertView = rootInflater.inflate(R.layout.wall_row, container, false);

					    Tile tile = getItem(position);

				        TextView wordText = (TextView)convertView.findViewById(R.id.wall_text);
				        wordText.setTextColor(textColor);
				    	Typeface cf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf");
				    	wordText.setTypeface(cf);
				    	wordText.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
				    	wordText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			    		wordText.setText(Html.fromHtml(tile.getName()));

			    		//convertView.findViewById(R.id.meaning).setVisibility(View.GONE);
			    		//convertView.findViewById(R.id.type).setVisibility(View.GONE);
			    		if (((MainActivity)parentActivity).getInt("wall_selected_index") != position)
			    			convertView.findViewById(R.id.right_image).setVisibility(View.GONE);
			    		ImageView iv = (ImageView)convertView.findViewById(R.id.wall_image);
			    		iv.setVisibility(View.VISIBLE);
			    		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			    		     iv.setImageDrawable(getResources().getDrawable(tile.getDrawable(), parentActivity.getApplicationContext().getTheme()));
			    		   } else {
			    		     iv.setImageDrawable(getResources().getDrawable(tile.getDrawable()));
			    		}
			    		
			    		if (position == (getCount() -1))
			    			convertView.findViewById(R.id.secondary).setVisibility(View.GONE);
			    		else
			    			convertView.findViewById(R.id.secondary).setVisibility(View.VISIBLE);

						return convertView;
				    }
		 };

		wallLV.setAdapter(correctAdapter);
		
		builder.addContentView(PopupLayout, new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		builder.show();
	}
}