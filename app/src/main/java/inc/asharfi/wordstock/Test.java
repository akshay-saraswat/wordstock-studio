package inc.asharfi.wordstock;

public class Test {
    
    //private variables
    private String _date;
    private String _span;
    private String _type;
    private int _total_count;
    private int _attempt_count;
    private int _correct_count;
    private String _correct_words;
    private String _incorrect_words;

    public Test () {
    	
    }

    // constructor
    public Test(String date, String span, String type,
    			int total_count, int attempt_count, int correct_count,
    			String correct_words, String incorrect_words) {
        this._date = date;
        this._span = span;
        this._type = type;
        this._total_count = total_count;
        this._attempt_count = attempt_count;
        this._correct_count = correct_count;
        this._correct_words = correct_words;
        this._incorrect_words = incorrect_words;
    }

    // getting Date
    public String getDate(){
        return this._date;
    }
     
    // getting Span
    public String getSpan(){
        return this._span;
    }

    // getting Test Type
    public String getType(){
        return this._type;
    }

    // getting Total Count
    public int getTotalCount(){
        return this._total_count;
    }

    // getting Attempt Count
    public int getAttemptCount(){
        return this._attempt_count;
    }

    // getting Correct Count
    public int getCorrectCount(){
        return this._correct_count;
    }

    // getting Correct Words
    public String getCorrectWords(){
        return this._correct_words;
    }

    // getting Incorrect Words
    public String getIncorrectWords(){
        return this._incorrect_words;
    }
}
