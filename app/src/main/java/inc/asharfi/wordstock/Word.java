package inc.asharfi.wordstock;

import android.content.Context;

public class Word {
    
    //private variables
    private String _word;
    private String _meanings;
    private String _examples;
    private String _etymology;
    private String _synonyms;
    private String _antonyms;
    private String _flag;
    private String _sentences;
    private String _favorite;
    private String _types;
    private String _lists;
    private String _short_definitions;
    private String _mnemonics;
    private String _status;

    public Word () {
    	
    }
    
    public Word(String word) {
    	this._word = word;
    }

    // constructor
    public Word(String word, String meanings, String antonyms, String synonyms,
	    		String examples, String etymology, String sentences, String types,
	    		String lists, String short_definitions, String mnemonics,
	    		String status, String favorite, Context context) {
        this._word = word.replace(context.getString(R.string.glyph), context.getString(R.string.latin_e));
        this._meanings = meanings;
        this._examples = examples;
        this._synonyms = synonyms;
        this._antonyms = antonyms;
        this._etymology = etymology;
        this._sentences = sentences;
        this._favorite = favorite;
        this._types = types;
        this._lists = lists;
        this._status = status;
        this._short_definitions = short_definitions;
        this._mnemonics = mnemonics;
    }

    // getting Word
    public String getWord(){
        return this._word;
    }
     
    // getting Meanings
    public String getMeanings(){
        return this._meanings;
    }

    // getting Examples
    public String getExamples(){
        return this._examples;
    }

    // getting Antonyms
    public String getAntonyms(){
        return this._antonyms;
    }

    // getting Synonyms
    public String getSynonyms(){
        return this._synonyms;
    }

    // getting Sentences
    public String getSentences(){
        return this._sentences;
    }

    // getting Etymology
    public String getEtymology(){
        return this._etymology;
    }

    // getting Types
    public String getTypes(){
        return this._types;
    }

    // getting Lists
    public String getLists(){
        return this._lists;
    }

    // getting Flag
    public String getFlag () {
    	return this._flag;
    }

    // getting Favorite
    public String getFavorite () {
    	return this._favorite;
    }

    // getting Status
    public String getStatus () {
    	return this._status;
    }

    // getting Mnemonics
    public String getMnemonics () {
    	return this._mnemonics;
    }

    // getting Short Definitions
    public String getShortDefinitions () {
    	return this._short_definitions;
    }

    // setting Flag
    public void setFlag(String flag) {
    	this._flag = flag;
    }

    // setting Status
    public void setStatus(String status) {
    	this._status = status;
    }

    // setting Favorite
    public void setFavorite(String favorite) {
    	this._favorite = favorite;
    }
}
