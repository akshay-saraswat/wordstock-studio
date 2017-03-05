package inc.asharfi.wordstock;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FlashcardFragment extends Fragment {
    private View rootView;
    private MainActivity parentActivity;
    private LayoutInflater rootInflater;
	private String wordText;
	private List<String> toonsList = null;
	private float width;
	private ExpandableListView meaningList;
	private ExpandableListAdapter meaningAdapter;
	private List<String> meaningListDataHeader;
	private HashMap<String, List<String>> meaningListChildData;
	private String mnemonics;
	private int textColor = Color.BLACK;
	private int textShadow = Color.DKGRAY;
	//private final String TOONS_FOLDER = "/data/app/inc.asharfi.wordstock/toons/";

	public void setData (Word data) {
		Bundle args = new Bundle();

		if (data != null) {
			wordText = data.getWord();
			mnemonics = data.getMnemonics();
			meaningListDataHeader = new ArrayList<String>();
			meaningListChildData = new HashMap<String, List<String>>();

			List<String> meanings = new ArrayList<String>();
			String[] meaningsArray;
			if (data.getShortDefinitions().equals("nil"))
				meaningsArray = data.getMeanings().split("~~~");
			else
				meaningsArray = data.getShortDefinitions().split("~~~");
			for (int i = 0; i < meaningsArray.length; i++) {
				if (!meaningsArray[i].equals("nil"))
					meanings.add(meaningsArray[i]);
			}

			List<String> examples = new ArrayList<String>();
			String[] examplesArray = data.getExamples().split("~~~");
			for (int i = 0; i < examplesArray.length; i++) {
				if (!examplesArray[i].equals("nil"))
					examples.add(examplesArray[i]);
			}

			List<String> synonyms = new ArrayList<String>();
			if (!data.getSynonyms().equals("nil"))
				synonyms.add(data.getSynonyms());

			List<String> antonyms = new ArrayList<String>();
			if (!data.getAntonyms().equals("nil"))
				antonyms.add(data.getAntonyms());

			List<String> etymology = new ArrayList<String>();
			String[] etymologyArray = data.getEtymology().split("~~~");
			for (int i = 0; i < etymologyArray.length; i++) {
				if (!etymologyArray[i].equals("nil"))
					etymology.add(etymologyArray[i]);
			}

			toonsList = new ArrayList<String>();

			if (!meanings.isEmpty()) {
				meaningListDataHeader.add("Definition");
				meaningListChildData.put("Definition", meanings);
			}

			if (!examples.isEmpty()) {
				meaningListDataHeader.add("Examples");
				meaningListChildData.put("Examples", examples);
			}

			if (!synonyms.isEmpty()) {
				meaningListDataHeader.add("Synonyms");
				meaningListChildData.put("Synonyms", synonyms);
			}

			if (!antonyms.isEmpty()) {
				meaningListDataHeader.add("Antonyms");
				meaningListChildData.put("Antonyms", antonyms);
			}

			if (!etymology.isEmpty()) {
				meaningListDataHeader.add("Origin");
				meaningListChildData.put("Origin", etymology);
			}
		}

		this.setArguments(args);
	}

	@SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {

		parentActivity = (MainActivity) this.getActivity();
		rootInflater = inflater;
		rootView = rootInflater.inflate(R.layout.fragment_flashcard, container, false);
	    rootView.setBackgroundColor(Color.TRANSPARENT);

	    fillToonsList();
	    WindowManager wm = (WindowManager) parentActivity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
	    
		final TextView wordTextView = (TextView) rootView.findViewById(R.id.word_view);
		Typeface cfRegular = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf");

		if (parentActivity.getFlag("night_mode")) {
			textColor = Color.WHITE;
			textShadow = Color.BLACK;
			wordTextView.setBackgroundResource(R.drawable.first_item_bg_night);
		}

		wordTextView.setTypeface(cfRegular);
		wordTextView.setTextColor(textColor);
		wordTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
		wordTextView.setShadowLayer(((5 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		wordTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		Spanned sp = Html.fromHtml(wordText);
		wordTextView.setText(sp);

		Button speak_button = (Button) rootView.findViewById(R.id.sound_button);
		speak_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				parentActivity.speakOut(wordText);
				if (parentActivity.getFlag("ws_speak_definition") && meaningListChildData.containsKey("Definition")) {
					parentActivity.speakOut("   ");
					parentActivity.speakOut("Definition");
					parentActivity.speakOut("   ");
					for (int i = 0; i < meaningListChildData.get("Definition").size(); i++) {
						if (meaningListChildData.get("Definition").size() > 1)
							parentActivity.speakOut(String.valueOf(i + 1));
						parentActivity.speakOut(meaningListChildData.get("Definition").get(i));
					}
				}
				if (parentActivity.getFlag("ws_speak_example") && meaningListChildData.containsKey("Examples")) {
					parentActivity.speakOut("   ");
					parentActivity.speakOut("Examples");
					parentActivity.speakOut("   ");
					for (int i = 0; i < meaningListChildData.get("Examples").size(); i++) {
						if (meaningListChildData.get("Examples").size() > 1)
							parentActivity.speakOut(String.valueOf(i + 1));
						parentActivity.speakOut(meaningListChildData.get("Examples").get(i));
					}
				}
				if (parentActivity.getFlag("ws_speak_synonym") && meaningListChildData.containsKey("Synonyms")) {
					parentActivity.speakOut("   ");
					parentActivity.speakOut("Synonyms");
					parentActivity.speakOut("   ");
					for (int i = 0; i < meaningListChildData.get("Synonyms").size(); i++) {
						parentActivity.speakOut(meaningListChildData.get("Synonyms").get(i));
					}
				}
				if (parentActivity.getFlag("ws_speak_antonym") && meaningListChildData.containsKey("Antonyms")) {
					parentActivity.speakOut("   ");
					parentActivity.speakOut("Antonyms");
					parentActivity.speakOut("   ");
					for (int i = 0; i < meaningListChildData.get("Antonyms").size(); i++) {
						parentActivity.speakOut(meaningListChildData.get("Antonyms").get(i));
					}
				}
				if (parentActivity.getFlag("ws_speak_origin") && meaningListChildData.containsKey("Origin")) {
					parentActivity.speakOut("   ");
					parentActivity.speakOut("Origin");
					parentActivity.speakOut("   ");
					for (int i = 0; i < meaningListChildData.get("Origin").size(); i++) {
						parentActivity.speakOut(meaningListChildData.get("Origin").get(i));
					}
				}
			}
		});

		meaningAdapter = new FlashcardAdapter(meaningListDataHeader, meaningListChildData);
		meaningList = (ExpandableListView)rootView.findViewById(R.id.meaning_list);
		meaningList.setAdapter(meaningAdapter);
		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			meaningList.setIndicatorBounds((int) width - (int)((160 * width) / 1080), (int) width - (int)((100 * width) / 1080));
		} else {
		    meaningList.setIndicatorBoundsRelative((int) width - (int)((160 * width) / 1080), (int) width - (int)((100 * width) / 1080));
		}

		for (int position = 0; position < meaningAdapter.getGroupCount(); position++) {
			if (((String) meaningAdapter.getGroup(position)).equals("Definition")
					&& parentActivity.getFlag("ws_expand_definition"))
				meaningList.expandGroup(position);
			else if (((String) meaningAdapter.getGroup(position)).equals("Examples")
					&& parentActivity.getFlag("ws_expand_examples"))
				meaningList.expandGroup(position);
			else if (((String) meaningAdapter.getGroup(position)).equals("Synonyms")
					&& parentActivity.getFlag("ws_expand_synonyms"))
				meaningList.expandGroup(position);
			else if (((String) meaningAdapter.getGroup(position)).equals("Antonyms")
					&& parentActivity.getFlag("ws_expand_antonyms"))
				meaningList.expandGroup(position);
			else if (((String) meaningAdapter.getGroup(position)).equals("Origin")
					&& parentActivity.getFlag("ws_expand_origin"))
				meaningList.expandGroup(position);
		}

		return rootView;
	}

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

	public void fillToonsList() {
		int count = 0;
		String fileName = null;
		//String path = parentActivity.getApplicationContext().getFilesDir().getAbsolutePath() + "/toons/";
		if (wordText.contains(getString(R.string.latin_e)))
			fileName = "toons/" + wordText.replace(getString(R.string.latin_e), "e").toLowerCase(Locale.getDefault()) + "_";
		else
			//fileName = wordText.toLowerCase(Locale.getDefault()) + "_";
			fileName = "toons/" + wordText.toLowerCase(Locale.getDefault()) + "_";
		do {
			count++;
			try {
				InputStream istr = parentActivity.getApplicationContext().getAssets().open(fileName + count + ".png");

				//InputStream istr = new FileInputStream(new File(path + fileName + count + ".png"));
				istr.close();
				toonsList.add(fileName + count + ".png");

			} catch (IOException e1) {
				try {
					InputStream istr = parentActivity.getApplicationContext().getAssets().open(fileName + count + ".jpg");
					//InputStream istr = new FileInputStream(new File(path + fileName + count + ".jpg"));
					istr.close();
					toonsList.add(fileName + count + ".jpg");
				} catch (IOException e2) {
			        break;
			    }
		    }
		} while (true);
	}

	private class FlashcardAdapter extends BaseExpandableListAdapter {
		private List<String> _listDataHeader;
		private HashMap<String, List<String>> _listChildData;

		public FlashcardAdapter(List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        		this._listChildData = listChildData;
        		this._listDataHeader = listDataHeader;
        }

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return this._listChildData.get(this._listDataHeader.get(groupPosition)).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@SuppressLint({ "NewApi", "DefaultLocale" })
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

            String text = (String) getChild(groupPosition, childPosition);
            if (text.startsWith(": "))
            	text = text.substring(2);
            else if (text.startsWith(":"))
            	text = text.substring(1);

            if (convertView == null)
            	convertView = rootInflater.inflate(R.layout.flashcard_row, parent, false);

            convertView.findViewById(R.id.secondary).setVisibility(View.GONE);

            Typeface cf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf");

            TextView rowText = (TextView)convertView.findViewById(R.id.row_text_view);
            rowText.setTextColor(textColor);
        	rowText.setTypeface(cf);
        	rowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        	if (getGroup(groupPosition).equals("Examples")) {
        		if (childPosition == 0)
        			text = "<br>" + text;
        		if (isLastChild)
        			text += "<br>";

        		String startTag = "<u>";
        		String endTag = "</u>";
  
    			String[] tokens = text.split("\\s");
    			text = "";
    			for (int i = 0; i < tokens.length; i++) {
    				if ((wordText.length() > 3 && tokens[i].regionMatches(true, 0, wordText, 0, wordText.length() - 2))
    					|| (tokens[i].toLowerCase().contains(wordText.toLowerCase())))
    					tokens[i] = startTag + tokens[i] + endTag;

    				text += tokens[i] + " ";
    			}

            	Spanned sp = Html.fromHtml( text );
        		rowText.setText(sp);
        	} else if (getGroup(groupPosition).equals("Definition")) {
        		if (childPosition == 0)
        			text = "<br>" + text;
        		if (isLastChild)
        			text += "<br>";

            	Spanned sp = Html.fromHtml( text );
        		rowText.setText(sp);
        	} else {
        		if (childPosition == 0)
        			text = System.getProperty("line.separator") + text;
        		if (isLastChild)
        			text += System.getProperty("line.separator");
        		rowText.setText(text);
        	}

        	if ((getGroup(groupPosition).equals("Definition")
        		 || getGroup(groupPosition).equals("Examples"))
        		&& !(isLastChild && childPosition == 0)) {
		            TextView indexText = (TextView)convertView.findViewById(R.id.index_text_view);
		            cf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Bold.ttf");
		            indexText.setTypeface(cf);
		            indexText.setTextColor(textColor);
		            indexText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

		            if (childPosition == 0)
						indexText.setText(System.getProperty("line.separator") + "*");
						//indexText.setText(System.getProperty("line.separator") + (childPosition + 1) + ". ");
		            else if (isLastChild)
		            	indexText.setText("*" + System.getProperty("line.separator"));
						//indexText.setText((childPosition + 1) + ". " + System.getProperty("line.separator"));
		        	else
		        		indexText.setText("*");
						//indexText.setText((childPosition + 1) + ". ");
        	} else {
        		TextView indexText = (TextView)convertView.findViewById(R.id.index_text_view);
        		indexText.setText("");
        	}
            
            if (isLastChild && groupPosition == (getGroupCount() -1)) {
            	if (parentActivity.getFlag("night_mode"))
            		convertView.setBackgroundResource(R.drawable.last_item_bg_night);
            	else
            		convertView.setBackgroundResource(R.drawable.last_item_bg);
            } else {
            	if (parentActivity.getFlag("night_mode"))
            		convertView.setBackgroundResource(R.drawable.mid_item_bg_night);
            	else
            		convertView.setBackgroundResource(R.drawable.mid_item_bg);
            }

            return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return this._listChildData.get(this._listDataHeader.get(groupPosition)).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return this._listDataHeader.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return this._listDataHeader.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@SuppressLint("NewApi")
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String text = (String) getGroup(groupPosition);

			if (convertView == null)
				convertView = rootInflater.inflate(R.layout.flashcard_header, parent, false);

			convertView.findViewById(R.id.secondary).setVisibility(View.VISIBLE);

			Typeface cf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf");

            ((TextView)convertView.findViewById(R.id.index_text_view)).setText("     ");

            TextView rowText = (TextView)convertView.findViewById(R.id.row_text_view);
            rowText.setTextColor(textColor);
        	//rowText.setShadowLayer(((3 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
        	rowText.setTypeface(cf);
        	rowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        	rowText.setText(text);

        	if (mnemonics.equals("nil") || groupPosition > 0) {
        		convertView.findViewById(R.id.mnem_button).setVisibility(View.GONE);
        	} else {
    			Button mnem_button = (Button) convertView.findViewById(R.id.mnem_button);
    			mnem_button.setVisibility(View.VISIBLE);
    			mnem_button.setOnClickListener(new OnClickListener() {
    		
    				@Override
    				public void onClick(View v) {
    					showMnemonics();
    				}
    			});
        	}

        	if (toonsList.size() == 0 || groupPosition > 0) {
        		convertView.findViewById(R.id.gallery_button).setVisibility(View.GONE);
    		} else {
    			Button gallery_button = (Button) convertView.findViewById(R.id.gallery_button);
    			gallery_button.setVisibility(View.VISIBLE);
    			gallery_button.setOnClickListener(new OnClickListener() {
    		
    				@Override
    				public void onClick(View v) {
    					showImage();
    				}
    			});
    		}

        	if (!isExpanded && groupPosition == (getGroupCount() -1)) {

        		if (parentActivity.getFlag("night_mode"))
            		convertView.setBackgroundResource(R.drawable.last_item_bg_night);
            	else
            		convertView.setBackgroundResource(R.drawable.last_item_bg);

        		convertView.findViewById(R.id.secondary).setVisibility(View.INVISIBLE);

        	} else {

        		if (parentActivity.getFlag("night_mode"))
            		convertView.setBackgroundResource(R.drawable.mid_item_bg_night);
            	else
            		convertView.setBackgroundResource(R.drawable.mid_item_bg);

        		convertView.findViewById(R.id.secondary).setVisibility(View.VISIBLE);
        	}
        	
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
    }

	@SuppressLint("InflateParams") public void showMnemonics() {
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
	    //PopupLayout.findViewById(R.id.adView).setVisibility(View.GONE);
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

	    ListView mnemLV = (ListView) PopupLayout.findViewById(R.id.correct_list);

	    if (parentActivity.getFlag("night_mode")) {
	    	mnemLV.setBackgroundResource(R.drawable.list_bg_night);
    	}

	    if (mnemonics.equals("nil"))
	    	return;

	    String[] mnemList = mnemonics.split("~~~");
	    ArrayList<String> finalList = new ArrayList<String>();
	    for (int i = 0; i < mnemList.length; i++) {
	    	String[] tempList = mnemList[i].split("- by");
	    	if (tempList.length < 2 || tempList[0].length() < 3 || tempList[0].isEmpty())
	    		continue;
	    	finalList.add(mnemList[i]);
	    }
	    for (int i = 0; i < finalList.size(); i++)
	    	mnemList[i] = finalList.get(i);

	    ArrayAdapter<String> correctAdapter = new ArrayAdapter<String>(parentActivity.getApplicationContext(),
				R.layout.nav_drawer_item, R.id.item_text, mnemList) {

				   public View getView(int position, View convertView, ViewGroup container) {

					   if (convertView == null)
						   convertView = rootInflater.inflate(R.layout.input_row, container, false);

				        TextView wordText = (TextView)convertView.findViewById(R.id.word);
				        wordText.setTextColor(textColor);
				    	Typeface cf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf");
				    	wordText.setTypeface(cf);
				    	//wordText.setShadowLayer(((3 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
				    	wordText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			    		wordText.setText(Html.fromHtml(getItem(position)));

			    		convertView.findViewById(R.id.meaning).setVisibility(View.GONE);
			    		convertView.findViewById(R.id.type).setVisibility(View.GONE);
			    		convertView.findViewById(R.id.star_btn).setVisibility(View.GONE);
			    		convertView.findViewById(R.id.indicator).setVisibility(View.GONE);

			    		if (position == (getCount() -1))
			    			convertView.findViewById(R.id.secondary).setVisibility(View.GONE);
			    		else
			    			convertView.findViewById(R.id.secondary).setVisibility(View.VISIBLE);

						return convertView;
				    }

				   public boolean isEnabled(int position) {
					   return false;
				   }
		 };

		mnemLV.setAdapter(correctAdapter);
		
		builder.addContentView(PopupLayout, new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		builder.show();
	}
	
	public ArrayList<Bitmap> getData () {
		// Prepare some dummy data for gridview
        final ArrayList<Bitmap> imageItems = new ArrayList<Bitmap>();
        for (int i = 0; i < toonsList.size(); i++) {
	        try {
	        	InputStream istr = parentActivity.getApplicationContext().getAssets().open(toonsList.get(i));
				Drawable d = Drawable.createFromStream(istr, null);
	        	Bitmap bMap = ((BitmapDrawable)d).getBitmap();
	        	istr.close();
		        imageItems.add(bMap);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
        }
        return imageItems;
    }

	@SuppressLint("InflateParams")
	public void showImage() {
		/*ImageView imageView = new ImageView(parentActivity);*/
		final Dialog builder = new Dialog(parentActivity);
	    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    builder.getWindow().setGravity(Gravity.CENTER);
		if (((MainActivity)this.getActivity()).getFlag("night_mode"))
			builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 51, 51, 51)));
		else
	    	builder.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				//nothing
			}
		});

	    View mView = rootInflater.inflate(R.layout.popup_grid, null);

	    // ======================= Generate Image gallery ===========================
	    GridView gridView = (GridView) mView.findViewById(R.id.image_grid);
	    
	    // ============================= new code start ===================================
	    GridViewAdapter gridAdapter = new GridViewAdapter(parentActivity.getBaseContext(), rootInflater, R.layout.grid_item_layout, width, getData());
        gridView.setAdapter(gridAdapter);
        builder.setContentView(mView);

	    LayoutParams lp = builder.getWindow().getAttributes();
	    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
	    lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
	    builder.getWindow().setAttributes(lp);
	    builder.show();

	    //mView.findViewById(R.id.adView).setVisibility(View.GONE);
		// =============================== Find ads ================================
		/*if (parentActivity.isNetworkAvailable()) {
			final AdView mAdView = (AdView) mView.findViewById(R.id.adView);
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
			});
		}*/
	}
}
