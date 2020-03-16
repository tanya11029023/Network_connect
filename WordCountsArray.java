public class WordCountsArray {
    private WordCount[] wordCounts;
    private int actualSize;
    private int maxSize;
// creates a new instance of this class
// created instance is able to administer at most maxSize of words

    public WordCountsArray(int maxSize) {
        if (maxSize < 0) {
            this.maxSize = 0;
        } else {
            this.maxSize = maxSize;
        }

        this.actualSize = 0;
        this.wordCounts = new WordCount[this.maxSize];
    }
// adds specified word with count to this instance
public void add(String word, int count) {
        if (word == null || word.equals(""))
            return;

        if (count < 0) {
            return;
        }
// get the index, if the word is already administered
    int index = getIndexOfWord(word.toLowerCase());

// word found, insert on this index
    if (index != -1) {
        this.wordCounts[index].incrementCount(count);
    } else { // haven't found --> insert in the end of this list
        if (actualSize == maxSize) {
            this.doubleSize();
        }
        this.wordCounts[actualSize] = new WordCount(word.toLowerCase(), count);
        this.actualSize++;
    }}
 // determines, whether the words administered by instance and word in
 // specified WordCountsArray are equal and in the same order
    
private boolean wordsEqual(WordCountsArray wca) {
        // the same words
        if (this == wca) {
            return true;
        }
        // words can't be the same
        if ((wca == null) || (this.size() != wca.size())){
            return false;
        }

        // compare every single word at every position
        for (int i = 0; i < this.size(); i++) {
        // words are not the same, return false
            if (!this.getWord(i).equals(wca.getWord(i))) {
                return false;
            }
        }
        return true;
    }

// calculate weight of single words in WordCountsArray and save them in appropriate
// Word-Count object.
private void calculateWeights(DocumentCollection dc) {
        if (dc == null)
            return;
        for (int i = 0; i < this.size(); i++){
            int noOfDocsContainingWord = dc.noOfDocsContainingWord(this.getWord(i));

            if (noOfDocsContainingWord != 0) {
            // inverted frequency = log(amount of docs in dc + 1 / amount of docs which contain w)
                double invDocFreq = Math.log(dc.numDocs() + 1 / (double) dc.noOfDocsContainingWord(this.getWord(i)));
            // weight = frequency of word w in a * inverted frequency
                wordCounts[i].setWeight(this.getCount(i) * invDocFreq);
            } else {
                wordCounts[i].setWeight(0);
            }
        }
    }}
