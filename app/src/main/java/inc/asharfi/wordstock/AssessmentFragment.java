package inc.asharfi.wordstock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AssessmentFragment extends Fragment {
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private AssessmentPagerAdapter mWSPagerAdapter, mFlashcardPagerAdapter, mResultPagerAdapter;
	private AssessmentPagerAdapter mCompletionPagerAdapter, mEquivalencePagerAdapter, mAnnouncePagerAdapter;
	private boolean mWSInited = false, mFlashcardInited = false, mCompletionInited = false, mEquivalenceInited = false;
	private Test result;
	private View rootView;
	private long flashcardStartTime = 0;
	private long completionStartTime = 0;
	private long equivalenceStartTime = 0;
	//private long startTime = 0, extraTime = 0;
	private Runnable flashcardTimerThread = null, completionTimerThread = null, equivalenceTimerThread = null;
	private Handler customHandler = new Handler();
	private int bg;
	private TextView timerTextView= null;
	//private Thread mythread = null;
	public final String TAG = AssessmentFragment.class.getSimpleName();
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	public AssessmentFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWSPagerAdapter = new AssessmentPagerAdapter(this, 1, false);
		mFlashcardPagerAdapter = new AssessmentPagerAdapter(this, 2, true);
		mCompletionPagerAdapter = new AssessmentPagerAdapter(this, 3, true);
		mEquivalencePagerAdapter = new AssessmentPagerAdapter(this, 4, true);
		mResultPagerAdapter = new AssessmentPagerAdapter(this, -1, false);
		mAnnouncePagerAdapter = new AssessmentPagerAdapter(this, 7, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_assessment, container, false);
		timerTextView = (TextView) rootView.findViewById(R.id.timer);
		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);

		mWSPagerAdapter.initData(((MainActivity)this.getActivity()).getDataBase().getAllWords(1), 0, 0, 0, null, null);

		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setAdapter(mWSPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				AssessmentPagerAdapter adapter = (AssessmentPagerAdapter) mViewPager.getAdapter();
				updateActionBar(adapter.getSectionNumber());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		reInit();

		return rootView;
	}

	@Override
	public void onHiddenChanged (boolean hidden) {
		super.onHiddenChanged(hidden);

		if (hidden) {
			if (flashcardStartTime != 0) {
				addTime("FlashcardTestTime", (System.nanoTime() - flashcardStartTime));
				flashcardStartTime = 0;
				customHandler.removeCallbacks(flashcardTimerThread);
				//flashcardTimerThread = null;
			}

			if (completionStartTime != 0) {
				addTime("CompletionTestTime", (System.nanoTime() - completionStartTime));
				completionStartTime = 0;
				customHandler.removeCallbacks(completionTimerThread);
				//completionTimerThread = null;
			}

			if (equivalenceStartTime != 0) {
				addTime("EquivalenceTestTime", (System.nanoTime() - equivalenceStartTime));
				equivalenceStartTime = 0;
				customHandler.removeCallbacks(equivalenceTimerThread);
				//equivalenceTimerThread = null;
			}
			if (timerTextView != null)
				timerTextView.setText("");
		} else {
			reInit();
			if ((MainActivity)this.getActivity() != null)
				startTimer(((MainActivity)this.getActivity()).getSectionNumber());
		}
	}

	private void startTimer(int sectionNumber) {
		switch (sectionNumber) {
			case 2:
				if (flashcardStartTime != 0) {
					addTime("FlashcardTestTime", (System.nanoTime() - flashcardStartTime));
					flashcardStartTime = 0;
					customHandler.removeCallbacks(flashcardTimerThread);
				}

				flashcardStartTime = System.nanoTime();
				if (flashcardTimerThread == null) {
					flashcardTimerThread = new Runnable() {
						public void run() {
							long longTime = ((System.nanoTime() - flashcardStartTime) + getTime("FlashcardTestTime")) / 1000000000;
							if (longTime / 3600 > 0) {
								timerTextView.setText((longTime / 3600)
										+ ":" + ((longTime % 60) / 60)
										+ ":" + ((longTime % 60) % 60));
							} else if (longTime / 60 > 0) {
								timerTextView.setText((longTime / 60) + ":" + (longTime % 60));
							} else {
								timerTextView.setText("00:" + longTime);
							}
							customHandler.postDelayed(this, 1000);
						}
					};
					customHandler.postDelayed(flashcardTimerThread, 0);
				} else {
					customHandler.postDelayed(flashcardTimerThread, 0);
				}
				break;
			case 3:
				if (completionStartTime != 0) {
					addTime("CompletionTestTime", (System.nanoTime() - completionStartTime));
					completionStartTime = 0;
					customHandler.removeCallbacks(completionTimerThread);
				}

				completionStartTime = System.nanoTime();
				if (completionTimerThread == null) {
					completionTimerThread = new Runnable() {
						public void run() {
							long longTime = ((System.nanoTime() - completionStartTime) + getTime("CompletionTestTime")) / 1000000000;
							if (longTime / 3600 > 0) {
								timerTextView.setText((longTime / 3600)
										+ ":" + ((longTime % 60) / 60)
										+ ":" + ((longTime % 60) % 60));
							} else if (longTime / 60 > 0) {
								timerTextView.setText((longTime / 60) + ":" + (longTime % 60));
							} else {
								timerTextView.setText("00:" + longTime);
							}
							customHandler.postDelayed(this, 1000);
						}
					};
					customHandler.postDelayed(completionTimerThread, 0);
				} else {
					customHandler.postDelayed(completionTimerThread, 0);
				}
				break;
			case 4:
				if (equivalenceStartTime != 0) {
					addTime("EquivalenceTestTime", (System.nanoTime() - equivalenceStartTime));
					equivalenceStartTime = 0;
					customHandler.removeCallbacks(equivalenceTimerThread);
				}

				equivalenceStartTime = System.nanoTime();
				if (equivalenceTimerThread == null) {
					equivalenceTimerThread = new Runnable() {
						public void run() {
							long longTime = ((System.nanoTime() - equivalenceStartTime) + getTime("EquivalenceTestTime")) / 1000000000;
							if (longTime / 3600 > 0) {
								timerTextView.setText((longTime / 3600)
										+ ":" + ((longTime % 60) / 60)
										+ ":" + ((longTime % 60) % 60));
							} else if (longTime / 60 > 0) {
								timerTextView.setText((longTime / 60) + ":" + (longTime % 60));
							} else {
								timerTextView.setText("00:" + longTime);
							}
							customHandler.postDelayed(this, 1000);
						}
					};
					customHandler.postDelayed(equivalenceTimerThread, 0);
				} else {
					customHandler.postDelayed(equivalenceTimerThread, 0);
				}
				break;
		}
	}

	public void changeAdapter(int sectionNumber) {

		switch (sectionNumber) {
		case 1:
			mViewPager.setAdapter(mWSPagerAdapter);
			mViewPager.getAdapter().notifyDataSetChanged();
			if (mViewPager.getCurrentItem() != mWSPagerAdapter.getCurrentCount())
				mViewPager.setCurrentItem(mWSPagerAdapter.getCurrentCount());
			break;
		case 2:
			mViewPager.setAdapter(mFlashcardPagerAdapter);
			mViewPager.getAdapter().notifyDataSetChanged();
			if (mViewPager.getCurrentItem() != mFlashcardPagerAdapter.getCurrentCount())
				mViewPager.setCurrentItem(mFlashcardPagerAdapter.getCurrentCount());
			break;
		case 3:
			mViewPager.setAdapter(mCompletionPagerAdapter);
			mViewPager.getAdapter().notifyDataSetChanged();
			if (mViewPager.getCurrentItem() != mCompletionPagerAdapter.getCurrentCount())
				mViewPager.setCurrentItem(mCompletionPagerAdapter.getCurrentCount());
			break;
		case 4:
			mViewPager.setAdapter(mEquivalencePagerAdapter);
			mViewPager.getAdapter().notifyDataSetChanged();
			if (mViewPager.getCurrentItem() != mEquivalencePagerAdapter.getCurrentCount())
				mViewPager.setCurrentItem(mEquivalencePagerAdapter.getCurrentCount());
			break;
		case 7:
			mViewPager.setAdapter(mAnnouncePagerAdapter);
			mViewPager.getAdapter().notifyDataSetChanged();
			mViewPager.setCurrentItem(0);
			break;
		}
		startTimer(sectionNumber);
		//this.sectionNumber = sectionNumber;
	}

	public void initiateAdapter(int sectionNumber, final int position, List<String> wordsList) {
		if (this.getActivity() == null)
			return;

		List<String> data = null;
		
		data = wordsList;

		switch (sectionNumber) {
		case 1:
			mWSInited = true;
			mWSPagerAdapter.initData(data, 0, 0, 0, null, null);
			mViewPager.setAdapter(mWSPagerAdapter);
			mViewPager.getAdapter().notifyDataSetChanged();
			break;
		case 2:
			mFlashcardInited = true;
			mFlashcardPagerAdapter.initData(data, 0, 0, 0, null, null);
			mViewPager.setAdapter(mFlashcardPagerAdapter);
			mViewPager.getAdapter().notifyDataSetChanged();
			break;
		case 3:
			mCompletionInited = true;
			mCompletionPagerAdapter.initData(data, 0, 0, 0, null, null);
			mViewPager.setAdapter(mCompletionPagerAdapter);
			mViewPager.getAdapter().notifyDataSetChanged();
			break;
		case 4:
			mEquivalenceInited = true;
			mEquivalencePagerAdapter.initData(data, 0, 0, 0, null, null);
			mViewPager.setAdapter(mEquivalencePagerAdapter);
			mViewPager.getAdapter().notifyDataSetChanged();
			break;
		default:
			break;
		}

		startTimer(sectionNumber);
		mViewPager.setCurrentItem(position);
		updateActionBar(sectionNumber);
	}
	
	public void updateActionBar(int sectionNumber) {
		switch (sectionNumber) {
		case 1:
			mWSPagerAdapter.setCurrentCount(mViewPager.getCurrentItem());
			//((MainActivity) this.getActivity()).changeActionSelector(true, true);
			((MainActivity) this.getActivity()).changeActionButton((mViewPager.getCurrentItem() + 1) + " / "
																	+ mWSPagerAdapter.getCount(), true, true);

			((MainActivity) this.getActivity()).setInt("VisibleListPosition", mViewPager.getCurrentItem());
			break;
		case 2:
			mFlashcardPagerAdapter.setCurrentCount(mViewPager.getCurrentItem());
			((MainActivity) this.getActivity()).changeActionFinish(true, true);
			((MainActivity) this.getActivity()).changeActionButton(mFlashcardPagerAdapter.getCorrectCount() + " / "
																	+ (mFlashcardPagerAdapter.getCurrentCount() + 1), true, true);
			break;
		case 3:
			mCompletionPagerAdapter.setCurrentCount(mViewPager.getCurrentItem());
			((MainActivity) this.getActivity()).changeActionFinish(true, true);
			((MainActivity) this.getActivity()).changeActionButton(mCompletionPagerAdapter.getCorrectCount() + " / "
																	+ (mCompletionPagerAdapter.getCurrentCount() + 1), true, true);
			break;
		case 4:
			mEquivalencePagerAdapter.setCurrentCount(mViewPager.getCurrentItem());
			((MainActivity) this.getActivity()).changeActionFinish(true, true);
			((MainActivity) this.getActivity()).changeActionButton(mEquivalencePagerAdapter.getCorrectCount() + " / "
																	+ (mEquivalencePagerAdapter.getCurrentCount() + 1), true, true);
			break;
		case 7:
			((MainActivity) this.getActivity()).changeActionFinish(true, false);
			//((MainActivity) this.getActivity()).changeActionSelector(true, false);
			((MainActivity) this.getActivity()).changeActionButton(" / ", true, false);
			break;
		}
	}

	public void resetWS () {
		mWSInited = false;
		mWSPagerAdapter = null;
		mWSPagerAdapter = new AssessmentPagerAdapter(this, 1, false);
	}

	public boolean isWSAvailable () {
		return mWSInited;
	}

	public boolean isFlashcardAvailable () {
		return mFlashcardInited;
	}

	public boolean isCompletionAvailable () {
		return mCompletionInited;
	}

	public boolean isEquivalenceAvailable () {
		return mEquivalenceInited;
	}

	public void setResultData(int sectionNumber) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd, HH:mm", getResources().getConfiguration().locale);

		switch (sectionNumber) {
		case 2:
			if (flashcardStartTime != 0) {
				addTime("FlashcardTestTime", (System.nanoTime() - flashcardStartTime));
				flashcardStartTime = 0;
			}
			customHandler.removeCallbacks(flashcardTimerThread);

			result = new Test(	"" + sdf.format(new Date()),
								"" + (getTime("FlashcardTestTime") / 1000000000),
								"" + sectionNumber,
								mFlashcardPagerAdapter.getCurrentCount() + 1,
								mFlashcardPagerAdapter.getAttemptCount(),
								mFlashcardPagerAdapter.getCorrectCount(),
								mFlashcardPagerAdapter.getCorrectWords(),
								mFlashcardPagerAdapter.getIncorrectWords());
			break;
		case 3:
			if (completionStartTime != 0) {
				addTime("CompletionTestTime", (System.nanoTime() - completionStartTime));
				completionStartTime = 0;
			}
			customHandler.removeCallbacks(completionTimerThread);

			result = new Test(	"" + sdf.format(new Date()),
								"" + (getTime("CompletionTestTime") / 1000000000),
								"" + sectionNumber,
								mCompletionPagerAdapter.getCurrentCount() + 1,
								mCompletionPagerAdapter.getAttemptCount(),
								mCompletionPagerAdapter.getCorrectCount(),
								mCompletionPagerAdapter.getCorrectWords(),
								mCompletionPagerAdapter.getIncorrectWords());
			break;
		case 4:
			if (equivalenceStartTime != 0) {
				addTime("EquivalenceTestTime", (System.nanoTime() - equivalenceStartTime));
				equivalenceStartTime = 0;
			}
			customHandler.removeCallbacks(equivalenceTimerThread);

			result = new Test(	"" + sdf.format(new Date()),
								"" + (getTime("EquivalenceTestTime") / 1000000000),
								"" + sectionNumber,
								mEquivalencePagerAdapter.getCurrentCount() + 1,
								mEquivalencePagerAdapter.getAttemptCount(),
								mEquivalencePagerAdapter.getCorrectCount(),
								mEquivalencePagerAdapter.getCorrectWords(),
								mEquivalencePagerAdapter.getIncorrectWords());
			break;
		default:
			break;
		}
		if (timerTextView != null)
			timerTextView.setText("");
	}

	public void showResult(int sectionNumber) {
		MainActivity parentActivity = (MainActivity) this.getActivity();
		//parentActivity.changeActionSelector(true, true);
		parentActivity.changeActionButton("", true, false);
		mViewPager.setAdapter(mResultPagerAdapter);
		mViewPager.getAdapter().notifyDataSetChanged();
		SharedPreferences sp = parentActivity.getSharedPreferences(parentActivity.PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

			
		switch (sectionNumber) {
		case 2:
			mFlashcardInited = false;
			editor.remove("FlashcardCurrentCount");
			editor.remove("FlashcardAttemptCount");
			editor.remove("FlashcardCorrectCount");
			editor.remove("FlashcardCorrectWords");
			editor.remove("FlashcardWords");
			editor.remove("FlashcardTestTime");
			flashcardStartTime = 0;
			break;
		case 3:
			mCompletionInited = false;
			editor.remove("CompletionCurrentCount");
			editor.remove("CompletionAttemptCount");
			editor.remove("CompletionCorrectCount");
			editor.remove("CompletionCorrectWords");
			editor.remove("CompletionWords");
			editor.remove("CompletionTestTime");
			completionStartTime = 0;
			break;
		case 4:
			mEquivalenceInited = false;
			editor.remove("EquivalenceCurrentCount");
			editor.remove("EquivalenceAttemptCount");
			editor.remove("EquivalenceCorrectCount");
			editor.remove("EquivalenceCorrectWords");
			editor.remove("EquivalenceWords");
			editor.remove("EquivalenceTestTime");
			equivalenceStartTime = 0;
			break;
		default:
			break;
		}

		editor.apply();
	}

	public Test getResultData() {
		return result;
	}

	public void cleanUp() {
		if (rootView != null)
			rootView.setBackgroundResource(0);

		MainActivity parentActivity = (MainActivity) this.getActivity();
		if (parentActivity != null) {
			SharedPreferences sp = parentActivity.getSharedPreferences(parentActivity.PREFS_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
	
			if (mFlashcardInited) {
				editor.putInt("FlashcardCurrentCount", mFlashcardPagerAdapter.getCurrentCount());
				editor.putInt("FlashcardAttemptCount", mFlashcardPagerAdapter.getAttemptCount());
				editor.putInt("FlashcardCorrectCount", mFlashcardPagerAdapter.getCorrectCount());
				editor.putString("FlashcardCorrectWords", mFlashcardPagerAdapter.getCorrectWords());
				editor.putString("FlashcardWords", mFlashcardPagerAdapter.getAllWords());
				editor.putString("FlashcardQuesPositions", mFlashcardPagerAdapter.getAllQuesPositions());
			}
	
			if (mCompletionInited) {
				editor.putInt("CompletionCurrentCount", mCompletionPagerAdapter.getCurrentCount());
				editor.putInt("CompletionAttemptCount", mCompletionPagerAdapter.getAttemptCount());
				editor.putInt("CompletionCorrectCount", mCompletionPagerAdapter.getCorrectCount());
				editor.putString("CompletionCorrectWords", mCompletionPagerAdapter.getCorrectWords());
				editor.putString("CompletionWords", mCompletionPagerAdapter.getAllWords());
				editor.putString("CompletionQuesPositions", mCompletionPagerAdapter.getAllQuesPositions());
			}
	
			if (mEquivalenceInited) {
				editor.putInt("EquivalenceCurrentCount", mEquivalencePagerAdapter.getCurrentCount());
				editor.putInt("EquivalenceAttemptCount", mEquivalencePagerAdapter.getAttemptCount());
				editor.putInt("EquivalenceCorrectCount", mEquivalencePagerAdapter.getCorrectCount());
				editor.putString("EquivalenceCorrectWords", mEquivalencePagerAdapter.getCorrectWords());
				editor.putString("EquivalenceWords", mEquivalencePagerAdapter.getAllWords());
				editor.putString("EquivalenceQuesPositions", mEquivalencePagerAdapter.getAllQuesPositions());
			}
	
			editor.apply();

			if (flashcardStartTime != 0) {
				addTime("FlashcardTestTime", (System.nanoTime() - flashcardStartTime));
				flashcardStartTime = 0;
			}
	
			if (completionStartTime != 0) {
				addTime("CompletionTestTime", (System.nanoTime() - completionStartTime));
				completionStartTime = 0;
			}
	
			if (equivalenceStartTime != 0) {
				addTime("EquivalenceTestTime", (System.nanoTime() - equivalenceStartTime));
				equivalenceStartTime = 0;
			}
		}
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

		MainActivity parentActivity = (MainActivity) this.getActivity();
		SharedPreferences sp = parentActivity.getSharedPreferences(parentActivity.PREFS_NAME, Context.MODE_PRIVATE);

		if (sp.getString("FlashcardWords", null) != null) {
			mFlashcardInited = true;

			String[] quesTokens = sp.getString("FlashcardQuesPositions", null).split(", ");

			String[] wordTokens = sp.getString("FlashcardWords", null).split(", ");
			List<String> wordList = new ArrayList<String>();
			for (int i = 0; i < wordTokens.length ; i++)
				wordList.add(i, wordTokens[i]);

			String[] correctWordTokens = sp.getString("FlashcardCorrectWords", null).split(", ");
			List<String> correctWordList = new ArrayList<String>();
			for (int i = 0; i < correctWordTokens.length; i++)
				correctWordList.add(i, correctWordTokens[i]);

			while (correctWordList.contains("nil"))
				correctWordList.remove("nil");

			mFlashcardPagerAdapter.initData(wordList,
					sp.getInt("FlashcardCurrentCount", 0),
					sp.getInt("FlashcardCorrectCount", 0),
					sp.getInt("FlashcardAttemptCount", 0),
					correctWordList, quesTokens);
		}

		if (sp.getString("CompletionWords", null) != null) {
			mCompletionInited = true;

			String[] quesTokens = sp.getString("CompletionQuesPositions", null).split(", ");

			String[] wordTokens = sp.getString("CompletionWords", null).split(", ");
			List<String> wordList = new ArrayList<String>();
			for (int i = 0; i < wordTokens.length ; i++)
				wordList.add(i, wordTokens[i]);

			String[] correctWordTokens = sp.getString("CompletionCorrectWords", null).split(", ");
			List<String> correctWordList = new ArrayList<String>();
			for (int i = 0; i < correctWordTokens.length; i++)
				correctWordList.add(i, correctWordTokens[i]);

			while (correctWordList.contains("nil"))
				correctWordList.remove("nil");

			mCompletionPagerAdapter.initData(wordList,
					sp.getInt("CompletionCurrentCount", 0),
					sp.getInt("CompletionCorrectCount", 0),
					sp.getInt("CompletionAttemptCount", 0),
					correctWordList, quesTokens);
		}

		if (sp.getString("EquivalenceWords", null) != null) {
			mEquivalenceInited = true;

			String[] quesTokens = sp.getString("EquivalenceQuesPositions", null).split(", ");

			String[] wordTokens = sp.getString("EquivalenceWords", null).split(", ");
			List<String> wordList = new ArrayList<String>();
			for (int i = 0; i < wordTokens.length ; i++)
				wordList.add(i, wordTokens[i]);

			String[] correctWordTokens = sp.getString("EquivalenceCorrectWords", null).split(", ");
			List<String> correctWordList = new ArrayList<String>();
			for (int i = 0; i < correctWordTokens.length; i++)
				correctWordList.add(i, correctWordTokens[i]);

			while (correctWordList.contains("nil"))
				correctWordList.remove("nil");

			mEquivalencePagerAdapter.initData(wordList,
					sp.getInt("EquivalenceCurrentCount", 0),
					sp.getInt("EquivalenceCorrectCount", 0),
					sp.getInt("EquivalenceAttemptCount", 0),
					correctWordList, quesTokens);
		}

		if (mViewPager != null)
			mViewPager.getAdapter().notifyDataSetChanged();
	}

	private void addTime(String key, long newTime) {
		MainActivity parentActivity = (MainActivity) this.getActivity();
		SharedPreferences sp = parentActivity.getSharedPreferences(parentActivity.PREFS_NAME, Context.MODE_PRIVATE);
		long oldTime = sp.getLong(key, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong(key, (oldTime + newTime));
		editor.apply();
	}

	private long getTime(String key) {
		MainActivity parentActivity = (MainActivity) this.getActivity();
		if (parentActivity != null) {
			SharedPreferences sp = parentActivity.getSharedPreferences(parentActivity.PREFS_NAME, Context.MODE_PRIVATE);
			return sp.getLong(key, 0);
		} else {
			return 0;
		}
	}
}