package inc.asharfi.wordstock;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TestFragment extends Fragment {

	private MainActivity parentActivity;
	private boolean isListViewChanged, isLastAnswerCorrect;
	private int sectionNumber;
	private View rootView;
	private LayoutInflater rootInflater;
	private String[] test_strings;
	private ListView test_list;
	private ArrayAdapter<String> testAdapter;
	private int answerCount = 0;
	private List<Integer> answerPositions;
	private Word question;
	private AssessmentPagerAdapter mAssessmentPagerAdapter;
	private int textColor = Color.BLACK;
	private int textShadow = Color.DKGRAY;
	private int width;

	
	public void setData (List<Word> data, boolean hasInactiveUI,
						AssessmentPagerAdapter apa, int sectionNumber,
						boolean shouldReverse) {
		test_strings = new String[5];
		answerPositions = new ArrayList<Integer>();

		for (int i = 0; i < data.size(); i++) {

			if (data.get(i).getFlag().equals("synonym")) {
				answerPositions.add(i);
			} else if (!(data.get(i).getFlag().equals("X"))) {
				answerPositions.add(i);
				question = data.get(i);
			}

			if (shouldReverse) {
				if (data.get(i).getShortDefinitions().equals("nil"))
					test_strings[i] = getStaticMeaning(data.get(i).getMeanings(), data.get(i).getFlag());
    			else
    				test_strings[i] = getStaticMeaning(data.get(i).getShortDefinitions(), data.get(i).getFlag());
			} else {
				test_strings[i] = data.get(i).getWord();
			}
		}

		if (data.size() == 4)
			test_strings[4] = "I don't know";

		isListViewChanged = hasInactiveUI;
		mAssessmentPagerAdapter = apa;
		this.sectionNumber = sectionNumber;
	}
	
	@SuppressLint("DefaultLocale")
	@Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {

		String text = "";
		parentActivity = (MainActivity) this.getActivity();
		rootInflater = inflater;

	    rootView = rootInflater.inflate(R.layout.fragment_test, container, false);
	    rootView.setBackgroundColor(Color.TRANSPARENT);

	    WindowManager wm = (WindowManager) parentActivity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;

	    testAdapter = new QuesAdapter(parentActivity, test_strings);
		test_list = (ListView)rootView.findViewById(R.id.ques_list);
		test_list.setAdapter(testAdapter);
		
		Typeface cf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf");
		View wordView = (View) rootView.findViewById(R.id.word_bg_view);
		TextView wordText = (TextView) rootView.findViewById(R.id.word_view);
		TextView qText = (TextView) rootView.findViewById(R.id.q_view);

		if (parentActivity.getFlag("night_mode")) {
			textColor = Color.WHITE;
			textShadow = Color.BLACK;
			wordView.setBackgroundResource(R.drawable.first_item_bg_night);
		}

		wordText.setTypeface(cf);
		wordText.setTextColor(textColor);
		wordText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		wordText.setShadowLayer(((3 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);
		qText.setTypeface(cf);
		qText.setTextColor(textColor);
		qText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		qText.setShadowLayer(((3 * width) / 1080), ((1 * width) / 1080), ((2 * width) / 1080), textShadow);

		switch (sectionNumber) {
		case 2:
			if (parentActivity.getFlag("flashcard_reverse")) {
				text = question.getWord();
			} else {
				if (question.getShortDefinitions().equals("nil"))
					text = getStaticMeaning(question.getMeanings(), question.getFlag());
    			else
    				text = getStaticMeaning(question.getShortDefinitions(), question.getFlag());
			}
			break;
		case 3:
			text = getStaticSentence(question.getWord(), question.getSentences(), question.getFlag());
			break;
		case 4:
			text = getStaticSentence(question.getWord(), question.getSentences(), question.getFlag());
			break;
		}

		if (sectionNumber == 2 && parentActivity.getFlag("flashcard_reverse")) {
			wordText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
			wordText.setGravity(Gravity.CENTER);
			qText.setVisibility(View.GONE);
		}

		wordText.setText(Html.fromHtml(text));

		// ListView Item Click Listener
		test_list.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				if (!isListViewChanged) {
					answerCount++;

					//view.setBackground(new ColorDrawable(Color.argb(0x80, 255, 255, 255)));
					if (sectionNumber != 4) {
						isListViewChanged = true;
						if (answerPositions.contains(position)) {
							mAssessmentPagerAdapter.answerIsCorrect(question.getWord());
						} else {
							view.findViewById(R.id.wrong_image).setVisibility(View.VISIBLE);
							mAssessmentPagerAdapter.answerIsIncorrect(question.getWord());
						}

						getViewByPosition(answerPositions.get(0), test_list).findViewById(R.id.right_image).setVisibility(View.VISIBLE);
					} else {
						if (answerCount == 2) {
							isListViewChanged = true;
							getViewByPosition(answerPositions.get(0), test_list).findViewById(R.id.right_image).setVisibility(View.VISIBLE);
							getViewByPosition(answerPositions.get(1), test_list).findViewById(R.id.right_image).setVisibility(View.VISIBLE);
							if (isLastAnswerCorrect && answerPositions.contains(position)) {
								mAssessmentPagerAdapter.answerIsCorrect(question.getWord());
							} else {
								if (!answerPositions.contains(position))
									view.findViewById(R.id.wrong_image).setVisibility(View.VISIBLE);
								mAssessmentPagerAdapter.answerIsIncorrect(question.getWord());
							}
						} else {
							if (answerPositions.contains(position)) {
								view.findViewById(R.id.right_image).setVisibility(View.VISIBLE);
								isLastAnswerCorrect = true;
							} else {
								view.findViewById(R.id.wrong_image).setVisibility(View.VISIBLE);
								isLastAnswerCorrect = false;
							}
						}
					}
				}
			}

		});

        return rootView;
    }

	public String getStaticMeaning (String text, String position) {
		String[] tokens = text.split("~~~");

		if (position.contains("X") || position.contains("synonym"))
			position = "0";

		if (Integer.parseInt(position) < tokens.length)
			text = tokens[Integer.parseInt(position)];
		else
			text = tokens[0];

		text = text.replaceAll("^(.+)<br>(.+)$", "$1");
		text = text.replaceAll("^(.+):(.+)$", "$2");
		text = text.replace("<b>", "");
		text = text.replace("</b>", "");

		return text;
	}

	@SuppressLint("DefaultLocale")
	public String getStaticSentence (String wordText, String text, String position) {
		String[] tokens = text.split("~~~");

		if (Integer.parseInt(position) < tokens.length)
			text = tokens[Integer.parseInt(position)];
		else
			text = tokens[0];

		text = text.replace("\\r\\n", "<br><br>");

		if (!text.contains("______")) {
			String[] units = text.split("\\s");
			text = "";
			for (int i = 0; i < units.length; i++) {
				if ((wordText.length() > 3 && units[i].regionMatches(true, 0, wordText, 0, wordText.length() - 2))
					|| (units[i].toLowerCase().contains(wordText.toLowerCase())))
					units[i] = "______";

				text += units[i] + " ";
			}
		}

		return text;
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
	

	private class QuesAdapter extends ArrayAdapter<String> {

        public QuesAdapter(Context context, String[] strings) {
            super(context, -1, -1, strings);
        }

        @Override
        public boolean isEnabled(int position) {
        		return true;
        }

		@SuppressLint({ "ViewHolder", "NewApi" })
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
			String text = super.getItem(position);
			if (text == null)
				text = "Don't know !";

			if (convertView == null)
				convertView = rootInflater.inflate(R.layout.test_row, parent, false);

			Typeface ctf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf");
			TextView rowText = (TextView)convertView.findViewById(R.id.row_index_view);
			rowText.setTextColor(textColor);
			rowText.setTypeface(ctf);
			rowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			rowText.setText(Html.fromHtml("<b>" + (position+1) + ".  </b>"));

            rowText = (TextView)convertView.findViewById(R.id.row_text_view);
            rowText.setTextColor(textColor);
        	rowText.setTypeface(ctf);
        	rowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
       		rowText.setText(Html.fromHtml(text));

            if (isListViewChanged && answerPositions.contains(position)) {
            	convertView.findViewById(R.id.right_image).setVisibility(View.VISIBLE);
            } else {
            	convertView.findViewById(R.id.right_image).setVisibility(View.INVISIBLE);
            }

            convertView.findViewById(R.id.wrong_image).setVisibility(View.INVISIBLE);

            if ((getCount() - 1) == position) {

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
    }
}