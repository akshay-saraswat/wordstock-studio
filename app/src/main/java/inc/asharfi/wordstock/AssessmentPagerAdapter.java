package inc.asharfi.wordstock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * A FragmentPagerAdapter that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class AssessmentPagerAdapter extends FragmentStatePagerAdapter {
	private int sectionNumber;
	private int[] quesPosition;
	private boolean isRandom;
	private int rowCount = 0, currentCount = 0, correctCount = 0, attemptCount = 0;
	private AssessmentFragment mAF;
	private List<String> allWords = null;
	private boolean[] correctWords = null;

	public AssessmentPagerAdapter(AssessmentFragment af, int sectionNumber, boolean isRandom) {
		super(af.getChildFragmentManager());
		this.sectionNumber = sectionNumber;
		this.isRandom = isRandom;
		mAF = af;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;

		switch (sectionNumber) {
		case -1:
			fragment = new ResultFragment();
			((ResultFragment)fragment).setData(mAF.getResultData());
			break;
		case 1:
			Word word = getData(allWords.get(position));
			fragment = new FlashcardFragment();
			((FlashcardFragment)fragment).setData(word);
			break;
		case 2:
			MainActivity parentActivity = (MainActivity) mAF.getActivity();
			if (position < currentCount) {
				fragment = new TestFragment();
				((TestFragment)fragment).setData(getTestData(position), true, this, sectionNumber, parentActivity.getFlag("flashcard_reverse"));
			} else {
				fragment = new TestFragment();
				((TestFragment)fragment).setData(getTestData(position), false, this, sectionNumber, parentActivity.getFlag("flashcard_reverse"));
			}
			break;
		case 3:
			if (position < currentCount) {
				fragment = new TestFragment();
				((TestFragment)fragment).setData(getTestData(position), true, this, sectionNumber, false);
			} else {
				fragment = new TestFragment();
				((TestFragment)fragment).setData(getTestData(position), false, this, sectionNumber, false);
			}
			break;
		case 4:
			if (position < currentCount) {
				fragment = new TestFragment();
				((TestFragment)fragment).setData(getTestData(position), true, this, sectionNumber, false);
			} else {
				fragment = new TestFragment();
				((TestFragment)fragment).setData(getTestData(position), false, this, sectionNumber, false);
			}
			break;
		case 7:
			fragment = new AnnounceFragment();
			break;
		default:
			break;
		}

		updateActionBar();
		return fragment;
	}

	@Override
	public int getCount() {
		if (sectionNumber < 0 || sectionNumber == 7)
			return 1;
		else
			return rowCount;
	}

	public void setCurrentCount(int count) {
		if (currentCount < count)
			currentCount = count;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public int getCorrectCount() {
		return correctCount;
	}

	public int getAttemptCount() {
		return attemptCount;
	}

	public int getSectionNumber() {
		return sectionNumber;
	}
	
	public void answerIsCorrect(String data) {
		correctWords[allWords.indexOf(data)] = true;
		correctCount++;
		attemptCount++;
		Word word = getData(data);
		//word.setStatus("done");
		updateData(word);
		updateActionBar();
	}
	
	public void answerIsIncorrect(String data) {
		attemptCount++;
		Word word = getData(data);
		//word.setStatus("todo");
		updateData(word);
	}

	public boolean getRandomAttribute() {
		return isRandom;
	}
	
	public void updateActionBar() {
		mAF.updateActionBar(sectionNumber);
	}

	@SuppressLint("UseValueOf")
	public void initData(List<String> data, int mCurrentCount, int mCorrectCount,
							int mAttemptCount, List<String> mCorrectWords, String[] ques) {
		if (isRandom && mCurrentCount == 0) {
			allWords = new ArrayList<String>();
			allWords.addAll(data);
			Collections.shuffle(allWords);
		} else {
			allWords = data;
		}

		quesPosition = new int[data.size()];
		if (ques == null) {
			for (int i = 0; i < data.size(); i++)
				quesPosition[i] = -1;
		} else {
			for (int i = 0; i < data.size(); i++)
				quesPosition[i] = Integer.parseInt(ques[i]);
		}

		correctWords = new boolean[data.size()];
		if (mCurrentCount != 0) {
			for (int i = 0; i < mCurrentCount; i++)
					correctWords[i] = mCorrectWords.contains(allWords.get(i));
		}

		rowCount = allWords.size();
		currentCount = mCurrentCount;
		correctCount = mCorrectCount;
		attemptCount = mAttemptCount;
  	}

	public Word getData (String word) {
		return ((MainActivity)mAF.getActivity()).getDataBase().getSingleByString(word);
	}
	
	public void updateData (Word word) {
		((MainActivity)mAF.getActivity()).getDataBase().updateWord(word);
	}

	public boolean testTypesMatch (String sTypes1, String sTypes2) {
		String[] aTypes1 = sTypes1.split("~~~");
		String[] aTypes2 = sTypes1.split("~~~");
		for (String i : aTypes1) {
			for (String j : aTypes2) {
				if (i.equals(j))
					return true;
			}
		}
		return false;
	}

	@SuppressLint("UseValueOf")
	public List<Word> getTestData(int position) {
		int count;
		int randomInt = 0;
		String[] tokens = null;
		String synonym = null;
		List<Integer> randomPositions = new ArrayList<Integer>();
  		List<Word> data = new ArrayList<Word>();
  		String types;
  		Random randomGenerator = new Random();
		Word word = getData(allWords.get(position));

  		// =================================== Word 1 =====================================
		if (quesPosition[position] == -1) {
			switch (sectionNumber) {
			case 2:
				tokens = word.getShortDefinitions().split("~~~");
				break;
			case 3:
				tokens = word.getSentences().split("~~~");
				break;
			case 4:
				tokens = word.getSentences().split("~~~");
				break;
			default:
				break;
			}

			randomInt = randomGenerator.nextInt(tokens.length);
			word.setFlag("" + randomInt);
			quesPosition[position] = randomInt;
		} else {
			word.setFlag("" + quesPosition[position]);
		}

		data.add(word);
		randomPositions.add(position);
		types = word.getTypes();

		if (sectionNumber == 4) {
			tokens = word.getSynonyms().split(", ");
	
			for (int i = 0; i < tokens.length; i++) {
				if (tokens[i].contains("\\s") || tokens[i].contains(" "))
					continue;
				StringBuilder synonymSb = new StringBuilder();
				synonymSb = synonymSb.append(tokens[i]);
				synonymSb.setCharAt(0, Character.toUpperCase(synonymSb.charAt(0))); 
				synonym = synonymSb.toString();
			}
		}

		// =================================== Words 2, 3, 4 =====================================
		for (int i = 0; i < 3; i++) {
			count = 50;
	  		while(true)
	  		{
	  			randomInt = randomGenerator.nextInt(rowCount);
	  			word = getData(allWords.get(randomInt));
	  			if (testTypesMatch(types, word.getTypes()) && !randomPositions.contains(randomInt))
	  				break;
	  			else if (count < 0 && !randomPositions.contains(randomInt))
	  				break;
	  			count--;
	  		}
	
	  		word.setFlag("X");
			data.add(word);
			randomPositions.add(randomInt);
		}

		// =================================== Word 5 =====================================
		count = 50;
		if (sectionNumber == 4) {
			word = new Word(synonym);
			word.setFlag("synonym");
		} else {
			while(true)
	  		{
	  			randomInt = randomGenerator.nextInt(rowCount);
	  			word = getData(allWords.get(randomInt));
	  			if (testTypesMatch(types, word.getTypes()) && !randomPositions.contains(randomInt))
	  				break;
	  			else if (count < 0 && !randomPositions.contains(randomInt))
	  				break;
	  			count--;
	  		}
	  		word.setFlag("X");
		}

		data.add(word);
		Collections.shuffle(data);

  		return data;
  	}

	public String getCorrectWords() {
		String words = "";
		for (int i = 0; i <= currentCount; i++) {
			if (correctWords[i])
				words += (allWords.get(i) + ", ");
		}

		if (words.length() > 0)
			return words.substring(0, words.length() - 2);
		else
			return "nil";
	}

	public String getIncorrectWords() {
		String words = "";
		for (int i = 0; i <= currentCount; i++) {
			if (!correctWords[i])
				words += (allWords.get(i) + ", ");
		}

		if (words.length() > 0)
			return words.substring(0, words.length() - 2);
		else
			return "nil";
	}

	public String getAllWords() {
		String words = "";
		for (int i = 0; i < allWords.size(); i++)
				words += (allWords.get(i) + ", ");

		if (words.length() > 0)
			words = words.substring(0, words.length() - 2);

		return words;
	}

	public String getAllQuesPositions() {
		String positions = "";
		for (int i = 0; i < quesPosition.length; i++)
			positions += (quesPosition[i] + ", ");

		if (positions.length() > 0)
			positions = positions.substring(0, positions.length() - 2);

		return positions;
	}
}