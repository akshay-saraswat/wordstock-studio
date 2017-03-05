package inc.asharfi.wordstock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private LayoutInflater rootInflater;
    public final String TAG = PlaceholderFragment.class.getSimpleName();
    private ListView attributeList = null;
    private View rootView;
    private List<String> alphaList, miscList, extraList;
    private int favCount;
    private int holdingSectionNumber = 1;
	private int bg;
	private MainActivity parentActivity;
	private PlaceholderFragment thisFragment;
	private AssessmentFragment mAssessmentFragment;
	private int width, attributeListScrollTo;
	private boolean isAlphaList1 = false, isAlphaList2 = false;
	private int textColor = Color.BLACK;
	private int textShadow = Color.DKGRAY;
	private Dialog mBuilder;

	public void setData(int sectionNumber, AssessmentFragment af) {
		mAssessmentFragment = af;

		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		this.setArguments(args);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

		parentActivity = (MainActivity) this.getActivity();


        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rootInflater = inflater;
        attributeList = (ListView)rootView.findViewById(R.id.attribute_list);
        thisFragment = this;

        fillArray();
        changeAdapter(1);

        WindowManager wm = (WindowManager) parentActivity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;

        /* ListView Item Click Listener */
        attributeList.setOnItemClickListener(new OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	  moveToNextList(position);
              }
         });

        reInit();

        return rootView;
    }
    
    public void moveToNextList (final int position) {
    	if (holdingSectionNumber == 5) {

  			Test mTest = getTestData(position);
  			showWordsList(mTest.getCorrectWords(), mTest.getIncorrectWords());

  		  } else {
  			//showSpinner();

			  new Timer().schedule(new TimerTask() {
				  @Override
				  public void run() {
					  if (isAlphaList1 || isAlphaList2) {
						  extraList = parentActivity.getDataBase().getAlphaWords((char) ('A' + position), holdingSectionNumber);
						  if (parentActivity.getFlag("ws_random"))
							Collections.shuffle(extraList);
						  goToCard(0);
					  } else {
						goToCard(position);
					  }
				  }
			  }, 100);
  		  }
    }

	public void goToCard (final int position) {
		if (holdingSectionNumber == 0) {
			View v = mAssessmentFragment.getView();
			if (v != null)
				v.post(new Runnable() {
					@Override
					public void run() {
						mAssessmentFragment.initiateAdapter(isAlphaList2 ? 2 : 1, position, extraList);
					}
				});

			FragmentManager fragmentManager =  parentActivity.getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.hide(thisFragment);
			transaction.show(mAssessmentFragment);
			transaction.commit();
		} else {
			FragmentManager fragmentManager =  parentActivity.getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();

			if (miscList.get(position).equals("GMAT List")) {
				if (holdingSectionNumber == 1 && !parentActivity.getFlag("ws_random"))
					parentActivity.setInt("VisibleList", 1);
				extraList = parentActivity.getDataBase().getListsWords("GMAT", holdingSectionNumber);
			} else if (miscList.get(position).equals("GRE List")) {
				if (holdingSectionNumber == 1 && !parentActivity.getFlag("ws_random"))
					parentActivity.setInt("VisibleList", 2);
				extraList = parentActivity.getDataBase().getListsWords("GRE", holdingSectionNumber);
			} else if (miscList.get(position).equals("SAT List")) {
				if (holdingSectionNumber == 1 && !parentActivity.getFlag("ws_random"))
					parentActivity.setInt("VisibleList", 3);
				extraList = parentActivity.getDataBase().getListsWords("SAT", holdingSectionNumber);
			} else if (miscList.get(position).equals("TOEFL List")) {
				if (holdingSectionNumber == 1 && !parentActivity.getFlag("ws_random"))
					parentActivity.setInt("VisibleList", 4);
				extraList = parentActivity.getDataBase().getListsWords("TOEFL", holdingSectionNumber);
			} else if (miscList.get(position).equals("Favorite Words")) {
				if (holdingSectionNumber == 1 && !parentActivity.getFlag("ws_random"))
					parentActivity.setInt("VisibleList", 5);
				extraList = parentActivity.getDataBase().getFavoriteWords(holdingSectionNumber);
			} else {
				if (holdingSectionNumber == 1 && !parentActivity.getFlag("ws_random"))
					parentActivity.setInt("VisibleList", 6);
				if (isAlphaList1 || isAlphaList2) {
					parentActivity.setInt("VisibleListPosition", 0);
					parentActivity.setInt("VisibleList", 0);
					attributeListScrollTo = 0;
				}
				extraList = parentActivity.getDataBase().getAllWords(holdingSectionNumber);
			}

			if (parentActivity.getFlag("ws_random"))
				Collections.shuffle(extraList);
			else
				Collections.sort(extraList);

			switch (holdingSectionNumber) {
				case 1:
						View v1 = thisFragment.getView();
						if (v1 != null)
						v1.post(new Runnable() {
							@Override
							public void run() {
								if (miscList.get(position).equals("Alphabet Lists")) {
									isAlphaList1 = true;
								}
								changeAdapter(0);
							}
						});
						break;
				case 2:
					if (miscList.get(position).equals("Alphabet Lists")) {
						View v2 = thisFragment.getView();
						if (v2 != null)
							v2.post(new Runnable() {
								@Override
								public void run() {
									isAlphaList2 = true;
									changeAdapter(0);
								}
							});
						break;
					}
				case 3:
				case 4:
					View v3 = mAssessmentFragment.getView();
					if (v3 != null)
						v3.post(new Runnable() {
							@Override
							public void run() {
								mAssessmentFragment.initiateAdapter(holdingSectionNumber,
										position < miscList.size() ? 0 : (position - miscList.size()),
										extraList);
							}
						});
					transaction.hide(thisFragment);
					transaction.show(mAssessmentFragment);
					transaction.commit();
					break;
				default:
					break;
			}
		}

		/*new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				dismissSpinner();
			}
		}, 100);*/
		
		attributeList.post(new Runnable() {
            @Override
            public void run() {
            	if (attributeListScrollTo > 0) {
            		attributeList.setSelection(attributeListScrollTo);
	        		attributeListScrollTo = 0;
	        		attributeList.clearFocus();
            	}
            }    
        });
	}

	public int fetchPosition (String query) {
		query = Character.toUpperCase(query.charAt(0)) + query.substring(1);
		if (extraList == null) {
			extraList = parentActivity.getDataBase().getAllWords(1);
		}
		if (extraList.contains(query))
			return extraList.lastIndexOf(query);
		else
			return -1;
	}

	public List<String> fetchSearchList (String query) {
		if (query.isEmpty())
			return null;

		int count = 0;
		/* Create list for strings matching search_item */
		List<String> allMatches = new ArrayList<>();

		if (isAlphaList1 && (!mAssessmentFragment.isWSAvailable() || extraList == null)) {
			allMatches = parentActivity.getDataBase().getAlphaWords(query, holdingSectionNumber, true);
			extraList = parentActivity.getDataBase().getAlphaWords(query.toUpperCase().charAt(0), holdingSectionNumber);
			if (parentActivity.getFlag("ws_random"))
				Collections.shuffle(extraList);

			return allMatches;
		}

		/* When section number is 1 or main word list is empty */
		if (holdingSectionNumber == 1 || extraList == null) {
			extraList = parentActivity.getDataBase().getAllWords(1);
			if (parentActivity.getFlag("ws_random"))
				Collections.shuffle(extraList);
		}

		for (String w : extraList) {
			/* Now check if String has the prefix */
		    if (w.toLowerCase().startsWith(query.toString())) {
		    	allMatches.add(w);
		    	if (++count > 5)
		    		break;
		    }
		}

		return allMatches;
	}

	public int getSectionNumber () {
		return holdingSectionNumber;
	}

	public void fetchCard(int position) {
		holdingSectionNumber = 0;
		goToCard(position);
	}

	private void showSpinner() {
		mBuilder = new Dialog(getActivity());
		mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mBuilder.getWindow().setGravity(Gravity.CENTER);
		mBuilder.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mBuilder.setCanceledOnTouchOutside(false);
		mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				/* nothing; */
			}
		});

		ProgressBar pb = new ProgressBar(getActivity());
		pb.setIndeterminateDrawable(getResources().getDrawable(R.drawable.my_spinner));
		pb.setIndeterminate(true);
		mBuilder.addContentView(pb, new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		mBuilder.show();
	}

	private void dismissSpinner() {
		if (mBuilder != null) {
			mBuilder.dismiss();
			mBuilder = null;
		}
		System.gc();
	}

    public void fillArray() {
    	alphaList = new ArrayList<>();
    	miscList = new ArrayList<>();
    	favCount = parentActivity.getDataBase().getFavoriteWordsCount(holdingSectionNumber);

    	for (char i = 'Z'; i >= 'A'; i--)
    		alphaList.add(0, "" + i);

		miscList.add(0, "Alphabet Lists");
		miscList.add(0, "All Words");
    	miscList.add(0, "TOEFL List");
    	miscList.add(0, "SAT List");
    	miscList.add(0, "GRE List");
    	miscList.add(0, "GMAT List");
    	recycleArray();
    }

    public void recycleArray() {
			
		if (!miscList.contains("Favorite Words")
				&& ((favCount > 0 && holdingSectionNumber == 1)
					|| (favCount > 5 && holdingSectionNumber > 1)))
			miscList.add(miscList.size(), "Favorite Words");
		else if (miscList.contains("Favorite Words")
				&& !((favCount > 0 && holdingSectionNumber == 1)
						|| (favCount > 5 && holdingSectionNumber > 1)))
			miscList.remove(miscList.lastIndexOf("Favorite Words"));

		if (holdingSectionNumber > 2 && miscList.contains("Alphabet Lists"))
			miscList.remove(miscList.lastIndexOf("Alphabet Lists"));
		else if (holdingSectionNumber < 2 && !miscList.contains("Alphabet Lists"))
			miscList.add(miscList.size(), "Alphabet Lists");
    }

    public Test getTestData (int index) {
		return parentActivity.getTestDataBase().getSingleByIndex(index);
    }

    public Word getData(String data) {
    	return parentActivity.getDataBase().getSingleByString(data);
    }

	public void changeAdapter(int sectionNumber) {

    	holdingSectionNumber = sectionNumber;

    	if (holdingSectionNumber != 0) {
    		isAlphaList1 = isAlphaList2 = false;
	    	if (parentActivity.getFlag("night_mode")) {
	    		textColor = Color.WHITE;
	    		textShadow = Color.BLACK;
	    	} else {
	    		textColor = Color.BLACK;
	    		textShadow = Color.DKGRAY;
	    	}
	
	    	reInit();
	    	recycleArray();
    	}

        if (holdingSectionNumber == 0 && !parentActivity.getFlag("ws_random")) {
        	attributeList.setFastScrollEnabled(true);
        } else {
        	attributeList.setFastScrollEnabled(false);
        }

    	attributeList.setVisibility(View.VISIBLE);

        if (attributeList != null) {
			AttributeAdapter a = new AttributeAdapter(this.getActivity());
			attributeList.setAdapter(a);
			a.notifyDataSetChanged();
		}

        ((MainActivity) this.getActivity()).changeActionFinish(false, false);
    	((MainActivity) this.getActivity()).changeActionButton("", false, false);

	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
	
	public void update_favorite(Word data) {
		parentActivity.getDataBase().updateWord(data);
	}

	public void cleanUp() {
		if (rootView != null)
			rootView.setBackgroundResource(0);
	}

	public void reInit(int sectionNumber) {
		holdingSectionNumber = sectionNumber;
		reInit();
	}

	public void reInit() {
		if (parentActivity == null)
			return;

		if (rootView != null) {
			bg = ((MainActivity)this.getActivity()).getBG();
			rootView.setBackgroundResource(0);
			if (((MainActivity)this.getActivity()).getFlag("night_mode")) {
				rootView.setBackgroundColor(bg);
			} else {
				rootView.setBackgroundResource(bg);
			}
		}

		if (holdingSectionNumber == 1 && attributeListScrollTo == 0) {
			attributeListScrollTo = parentActivity.getInt("VisibleListPosition");
	        switch (parentActivity.getInt("VisibleList")) {
	        case 1:
	        	moveToNextList(miscList.indexOf("GMAT List"));
	        	break;
	        case 2:
	        	moveToNextList(miscList.indexOf("GRE List"));
	        	break;
	        case 3:
	        	moveToNextList(miscList.indexOf("SAT List"));
	        	break;
	        case 4:
	        	moveToNextList(miscList.indexOf("TOEFL List"));
	        	break;
	        case 5:
	        	moveToNextList(miscList.indexOf("Favorite Words"));
	        	break;
	        case 6:
	        	moveToNextList(miscList.indexOf("All Words"));
	        	break;
	        default:
	        	attributeListScrollTo = 0;
	        	break;
	        }
		}
		
		//parentActivity.dismissSpinner();
        System.gc();
	}

    @SuppressWarnings("rawtypes")
	private class AttributeAdapter extends ArrayAdapter implements SectionIndexer {

    	private HashMap<String, Integer> azIndexer;
        private String[] sections;
        ArrayAdapter mArrayAdapter = null;

        public AttributeAdapter(Context context) {
            super(context, -1, -1);

			mArrayAdapter = this;

			if (!parentActivity.getFlag("ws_random") && extraList != null && holdingSectionNumber == 0) {
	            azIndexer = new HashMap<>();
	
	            for (int i = getCount() - 1; i >= 0; i--) {
	                String element = extraList.get(i);
	                /* We store the first letter of the word, and its index. */
	                azIndexer.put(element.substring(0, 1), i); 
	            }
	
	            /* set of letters */
	            Set<String> keys = azIndexer.keySet(); 

	            Iterator<String> it = keys.iterator();
	            ArrayList<String> keyList = new ArrayList<String>(); 
	
	            while (it.hasNext()) {
	                String key = it.next();
	                keyList.add(key);
	            }

	            /* sort the KeyList */
	            Collections.sort(keyList);

	            /* simple conversion to array */
	            sections = new String[keyList.size()];

	            keyList.toArray(sections);
			}
        }

        public int getCount() {
        	switch (holdingSectionNumber) {
        	case 0:
        		if (isAlphaList1 || isAlphaList2)
        			return alphaList.size();
        		else
        			return extraList.size();
        	case 1:
        	case 2:
        	case 3:
        	case 4:
        		return miscList.size();
        	case 5:
        		if (parentActivity.getTestDataBase().getAllTestsCount() > 0) {
        			int count = parentActivity.getTestDataBase().getAllTestsCount();
        			return count;
        		} else {
        			return 1;
        		}
        	default:
        		return 0;
        	}
        }

        @Override
        public boolean isEnabled(int position) {
        	if (getCount() == 1 && holdingSectionNumber == 5 && getTestData(0) == null)
        		return false;
        	else
        		return true;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

			if (holdingSectionNumber < 5) {

				String data = null;
				Word word = null;
				if (holdingSectionNumber == 0 && (isAlphaList1 || isAlphaList2)) {
					data = alphaList.get(position);
				} else if (holdingSectionNumber == 0) {
					parentActivity.setInt("VisibleListPosition", attributeList.getFirstVisiblePosition());
					data = extraList.get(position);
				} else if (position < miscList.size()) {
					data = miscList.get(position);
				}

				if (!(isAlphaList1 || isAlphaList2))
					word = getData(data);

                if (convertView == null)
                	convertView = rootInflater.inflate(R.layout.input_row, parent, false);

                Typeface cf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf");

                TextView wordText = (TextView)convertView.findViewById(R.id.word);
                wordText.setTextColor(textColor);
            	wordText.setTypeface(cf);
            	wordText.setShadowLayer(((3 * width) / 1080), (width / 1080), ((2 * width) / 1080), textShadow);
            	wordText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            	if (word == null || isAlphaList1 || isAlphaList2) {
            		wordText.setText(Html.fromHtml(data));
            		convertView.findViewById(R.id.meaning).setVisibility(View.GONE);
            		convertView.findViewById(R.id.type).setVisibility(View.GONE);
            		convertView.findViewById(R.id.star_btn).setVisibility(View.GONE);
            	} else {

            		wordText.setText(
							Html.fromHtml("<b>" + word.getWord() + "</b>"));

	                TextView meaningText = (TextView)convertView.findViewById(R.id.meaning);
	                meaningText.setVisibility(View.VISIBLE);
	                meaningText.setTextColor(textColor);
	            	meaningText.setTypeface(cf);
	            	meaningText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	            	String[] tokens;
	            	if (word.getShortDefinitions().equals("nil"))
	            		tokens = word.getMeanings().split("~~~");
	    			else
	    				tokens = word.getShortDefinitions().split("~~~");
	            	if (tokens[0].startsWith(": "))
	            		tokens[0] = tokens[0].substring(2);
	                else if (tokens[0].startsWith(":"))
	                	tokens[0] = tokens[0].substring(1);
	            	meaningText.setText(Html.fromHtml(tokens[0]));

	            	TextView typeText = (TextView)convertView.findViewById(R.id.type);
	            	typeText.setVisibility(View.VISIBLE);
	            	typeText.setTextColor(textColor);
	            	typeText.setTypeface(cf);
	            	typeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	            	String type = word.getTypes().replace("~~~", "  ")
	            								 .replace("adjective", "adj.")
	            								 .replace("verb", "v.")
	            								 .replace("adverb", "adv.")
	            								 .replace("noun", "n.")
	            								 .replace("name", "");
	            	if (type.equals("nil"))
	            		type = "";

	            	typeText.setText(Html.fromHtml("<i>" + type + "</i>"));

	               	final Button btn = ((Button) convertView.findViewById(R.id.star_btn));
	               	btn.setVisibility(View.VISIBLE);
	               	if (word.getFavorite() != null && word.getFavorite().equals("true"))
	               		btn.setBackgroundResource(R.drawable.star_on);
	               	else
	               		btn.setBackgroundResource(R.drawable.star_off);
	               	btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String data = extraList.get(position);
							Word word = getData(data);
							if (word.getFavorite().equals("true")) {
								btn.setBackgroundResource(R.drawable.star_off);
								word.setFavorite("false");
								update_favorite(word);
								favCount--;
								if (favCount == 4) {
									recycleArray();
									mArrayAdapter.notifyDataSetInvalidated();
									mArrayAdapter.notifyDataSetChanged();
								}
							} else {
								btn.setBackgroundResource(R.drawable.star_on);
								word.setFavorite("true");
								update_favorite(word);
								favCount++;
								if (favCount == 5) {
									recycleArray();
									mArrayAdapter.notifyDataSetInvalidated();
									mArrayAdapter.notifyDataSetChanged();
								}
							}
							
						}
					});
            	}
			} else {

				Typeface cf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf");
				Test mTest = getTestData(position);

				if (convertView == null)
                	convertView = rootInflater.inflate(R.layout.result_row, parent, false);

				if (mTest != null) {
					float correctness = (float) mTest.getCorrectCount() / (float) mTest.getTotalCount();
					float skipped =  (float) ((mTest.getTotalCount() - mTest.getAttemptCount()) + mTest.getCorrectCount()) / (float) mTest.getTotalCount();
					float barSize = ((200 * width) / 1080);

					convertView.findViewById(R.id.no_history_text).setVisibility(View.GONE);

					TextView tv = (TextView) convertView.findViewById(R.id.date_text);
					tv.setTypeface(cf);
					tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
					tv.setTextColor(textColor);
					tv.setShadowLayer(((3 * width) / 1080), (width / 1080), ((2 * width) / 1080), textShadow);
					tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					tv.setText(Html.fromHtml("<b>" + mTest.getDate() + "</b>"));

					tv = (TextView) convertView.findViewById(R.id.score_text);
					tv.setTypeface(cf);
					tv.setTextColor(textColor);
					tv.setShadowLayer(((3 * width) / 1080), (width / 1080), ((2 * width) / 1080), textShadow);
					tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					tv.setText(	Html.fromHtml("<b>Score:</b> "
								+ mTest.getCorrectCount()
								+ " / "
								+ mTest.getTotalCount()
								+ " ("
								+ (mTest.getCorrectCount() * 100) / mTest.getTotalCount()
								+ " %)" ));

					long span = Long.parseLong(mTest.getSpan());
					tv = (TextView) convertView.findViewById(R.id.speed_text);
					tv.setTypeface(cf);
					tv.setTextColor(textColor);
					tv.setShadowLayer(((3 * width) / 1080), (width / 1080), ((2 * width) / 1080), textShadow);
					tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					tv.setText(	Html.fromHtml("<b>Speed:</b> "
								+ (mTest.getTotalCount() * 60) / (span > 0 ? span : 1)
								+ " words/min"));

					tv = (TextView) convertView.findViewById(R.id.type_text);
					tv.setTypeface(cf);
					tv.setTextColor(textColor);
					tv.setShadowLayer(((3 * width) / 1080), (width / 1080), ((2 * width) / 1080), textShadow);
					tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

					switch(Integer.parseInt(mTest.getType())) {
					case 2:
						tv.setText(Html.fromHtml("<b>Type:</b> Flashcard Test"));
						break;
					case 3:
						tv.setText(Html.fromHtml("<b>Type:</b> S. Completion"));
						break;
					case 4:
						tv.setText(Html.fromHtml("<b>Type:</b> S. Equivalence"));
						break;
					}

					Bitmap bitmap = Bitmap.createBitmap((int) barSize, (int) barSize, Bitmap.Config.ARGB_8888);
					final Canvas canvas = new Canvas(bitmap);

					RectF rect = new RectF(20, 20, ((180 * width) / 1080), ((180 * width) / 1080));
				    ImageView drawingImageView = (ImageView) convertView.findViewById(R.id.result_bar);
					drawingImageView.setImageBitmap(bitmap);
					drawingImageView.setVisibility(View.VISIBLE);

			        /* Draw negative feedback */
					Paint negativeBar = new Paint(Paint.ANTI_ALIAS_FLAG);
					negativeBar.setColor(Color.rgb(238, 0, 0));
					drawingImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, negativeBar);
					if (!(correctness == 100 || skipped == 100 || (correctness + skipped) == 100)) {
						negativeBar.setShadowLayer(((5 * width) / 1080), (width / 1080), ((2 * width) / 1080), textShadow);
						canvas.drawArc(rect, -90, (360*1), true, negativeBar);
					}

					/* Draw don't care feedback */
			        Paint dontCareBar = new Paint(Paint.ANTI_ALIAS_FLAG);
			        dontCareBar.setColor(Color.rgb(238, 118, 0));
					drawingImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, dontCareBar);
					if ((barSize * skipped) > 0 && correctness != 100) {
						dontCareBar.setShadowLayer(((5 * width) / 1080), (width / 1080), ((2 * width) / 1080), textShadow);
						canvas.drawArc(rect, -90, (360 * skipped), true, dontCareBar);
					}

					/* Draw positive feedback */
			        Paint positiveBar = new Paint(Paint.ANTI_ALIAS_FLAG);
					positiveBar.setColor(Color.rgb(102, 205, 0));
					drawingImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, positiveBar);
					if ((barSize * correctness) > 0) {
						positiveBar.setShadowLayer(((5 * width) / 1080), (width / 1080), ((2 * width) / 1080), textShadow);
						canvas.drawArc(rect, -90, (360 * correctness), true, positiveBar);
					}

				} else {
					convertView.findViewById(R.id.result_bar).setVisibility(View.GONE);
					convertView.findViewById(R.id.date_text).setVisibility(View.GONE);
					convertView.findViewById(R.id.speed_text).setVisibility(View.GONE);
					convertView.findViewById(R.id.score_text).setVisibility(View.GONE);
					convertView.findViewById(R.id.type_text).setVisibility(View.GONE);
				}
			}

			if (parentActivity.getFlag("night_mode"))
				setNightViewBackground(convertView, position, getCount());
			else
				setViewBackground(convertView, position, getCount());

			System.gc();

            return convertView;
        }

		private void setViewBackground (View mView, int position, int count) {
			if (count == 1) {
            	mView.setBackgroundResource(R.drawable.list_bg);
				mView.findViewById(R.id.secondary).setVisibility(View.INVISIBLE);
			} else if (position == 0) {
            	mView.setBackgroundResource(R.drawable.first_item_bg);
				mView.findViewById(R.id.secondary).setVisibility(View.VISIBLE);
			} else if (position == (count - 1)) {
            	mView.setBackgroundResource(R.drawable.last_item_bg);
				mView.findViewById(R.id.secondary).setVisibility(View.INVISIBLE);
            } else {
            	mView.setBackgroundResource(R.drawable.mid_item_bg);
            	mView.findViewById(R.id.secondary).setVisibility(View.VISIBLE);
            }
		}

		private void setNightViewBackground (View mView, int position, int count) {
			if (count == 1) {
            	mView.setBackgroundResource(R.drawable.list_bg_night);
				mView.findViewById(R.id.secondary).setVisibility(View.INVISIBLE);
			} else if (position == 0) {
            	mView.setBackgroundResource(R.drawable.first_item_bg_night);
				mView.findViewById(R.id.secondary).setVisibility(View.VISIBLE);
			} else if (position == (count - 1)) {
            	mView.setBackgroundResource(R.drawable.last_item_bg_night);
				mView.findViewById(R.id.secondary).setVisibility(View.INVISIBLE);
            } else {
            	mView.setBackgroundResource(R.drawable.mid_item_bg_night);
            	mView.findViewById(R.id.secondary).setVisibility(View.VISIBLE);
            }
		}

		@Override
		public Object[] getSections() {
			return sections;
		}

		@Override
		public int getPositionForSection(int sectionIndex) {
			if (sections != null && sections.length > sectionIndex) {
				String letter = sections[sectionIndex];
				return azIndexer.get(letter);
			} else {
				return 0;
			}
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}
    }

	public void showWordsList (String correctWords, String incorrectWords) {

		Dialog builder = new Dialog(parentActivity);
	    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    builder.getWindow().setGravity(Gravity.CENTER);
	    builder.getWindow().setBackgroundDrawable(
	        new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
	        @Override
	        public void onDismiss(DialogInterface dialogInterface) {
	            /* nothing; */
	        }
	    });

	    View PopupLayout = rootInflater.inflate(R.layout.popup_list, null);
	    
	    // ======================================= Ad View ==================================
	    /*final AdView mAdView = (AdView) PopupLayout.findViewById(R.id.adView);
	    mAdView.setVisibility(View.GONE);*/

	    ListView correctLV = (ListView) PopupLayout.findViewById(R.id.correct_list);
	    ListView incorrectLV = (ListView) PopupLayout.findViewById(R.id.incorrect_list);

	    if (parentActivity.getFlag("night_mode")) {
	    	correctLV.setBackgroundResource(R.drawable.list_bg_night);
	    	incorrectLV.setBackgroundResource(R.drawable.list_bg_night);
    	}

	    if (!correctWords.equals("nil") && !incorrectWords.equals("nil")) {
		    RelativeLayout.LayoutParams incorrectLP = new RelativeLayout.LayoutParams(incorrectLV.getLayoutParams());
			incorrectLP.width = (width / 2) - 30 ;
			incorrectLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			incorrectLV.setLayoutParams(incorrectLP);
	
			RelativeLayout.LayoutParams correctLP = new RelativeLayout.LayoutParams(correctLV.getLayoutParams());
			correctLP.width = (width / 2) - 30;
			correctLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			correctLV.setLayoutParams(correctLP);
	    } else {
	    	if (correctWords.equals("nil"))
	    		correctLV.setVisibility(View.GONE);
	    	if (incorrectWords.equals("nil"))
	    		incorrectLV.setVisibility(View.GONE);
	    }

	    String[] correctList = correctWords.split(", ");
	    String[] incorrectList = incorrectWords.split(", ");

	    ArrayAdapter<String> correctAdapter = new ArrayAdapter<String>(parentActivity.getApplicationContext(),
				R.layout.nav_drawer_item, R.id.item_text, correctList) {

				   public View getView(int position, View convertView, ViewGroup container) {

					   if (convertView == null)
						   convertView = rootInflater.inflate(R.layout.input_row, container, false);

				        TextView wordText = (TextView)convertView.findViewById(R.id.word);
				        wordText.setTextColor(Color.rgb(34, 139, 34));
				    	Typeface cf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Bold.ttf");
				    	wordText.setTypeface(cf);
				    	wordText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			    		wordText.setText(getItem(position));

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

	 	ArrayAdapter<String> incorrectAdapter = new ArrayAdapter<String>(parentActivity.getApplicationContext(),
				R.layout.nav_drawer_item, R.id.item_text, incorrectList) {

				   public View getView(int position, View convertView, ViewGroup container) {

					   if (convertView == null)
						   convertView = rootInflater.inflate(R.layout.input_row, container, false);

				        TextView wordText = (TextView)convertView.findViewById(R.id.word);
				        wordText.setTextColor(Color.rgb(238, 0, 0));
				    	Typeface cf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Bold.ttf");
				    	wordText.setTypeface(cf);
				    	wordText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			    		wordText.setText(getItem(position));

			    		convertView.findViewById(R.id.meaning).setVisibility(View.GONE);
			    		convertView.findViewById(R.id.type).setVisibility(View.GONE);
			    		convertView.findViewById(R.id.star_btn).setVisibility(View.GONE);
			    		convertView.findViewById(R.id.indicator).setVisibility(View.GONE);

			    		if (position == (getCount() - 1))
			    			convertView.findViewById(R.id.secondary).setVisibility(View.GONE);
			    		else
			    			convertView.findViewById(R.id.secondary).setVisibility(View.VISIBLE);

						return convertView;
				    }

				   public boolean isEnabled(int position) {
					   return false;
				   }
	 	};

		correctLV.setAdapter(correctAdapter);
		incorrectLV.setAdapter(incorrectAdapter);
		
		builder.addContentView(PopupLayout, new RelativeLayout.LayoutParams(
		        ViewGroup.LayoutParams.MATCH_PARENT, 
		        ViewGroup.LayoutParams.MATCH_PARENT));
		builder.show();
	}
}
