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
    if (this == wca){
        return true;
    }
    if ((wca == null) || (this.size() != wca.size())) {
        return false;
    }
    for (int i = 0; i < this.size(); i++) {
        if (!this.getWord(i).equals(wca.getWord(i))) {
            return  false;
        }
    }
    return true;
}

public void sort() {this.doBubbleSort(); }

private void doBubbleSort() {
for (int pass = 1; pass < this.actualSize; pass++){
    for(int i = 0; i < this.actualSize - pass; i++){
        if (this.getWord(i).compareTo(this.getWord(i+1)) > 0) {
            WordCount tmp = this.wordCounts[i];
            this.wordCounts[i] = this.wordCounts[i+1];
            this.wordCounts[i+1] = tmp;
        }
    }
}}

private static int bucketAt(String word, int maxLength, int index) {
    int iPrime = maxLength - index - 1;
    if (iPrime > word.length())
        return 0;
    return word.charAt(iPrime) - 'a' + 1;
}

// sorts WordCount object with nucket sort, assumed that all words begin with lower case
private void doBucketSort() {
// one bucket for every character
    WordCountsArray[] buckets = new WordCountsArray[27];

    // initialize buckets
    for (int i = 0; i < buckets.length; i++){
        buckets[i] = new WordCountsArray(this.actualSize/buckets.length);

    int maxLength = 0;
    for (int i = 0; i < this.actualSize; i++){
        maxLength = Math.max(maxLength, this.getWord(i).length());
    }
    // sort words into buckets
    for (int i = 0; i < this.actualSize; i++){
        String word = this.getWord(i);
        int charIndex = bucketAt(word, maxLength, 0);
        if (charIndex >= 0){
            int count = this.getCount();
            buckets[charIndex].add(word, count);
        }
    }
    for (int pos = 1; pos < maxLength; pos++){
        WordCountsArray[] bucketsNew = new WordCountsArray[buckets.length];
        for (int i = 0; i < bucketsNew.length; i++){
            bucketsNew[i] = new WordCountsArray(this.actualSize/buckets.length);
        for(int i = 0; i < buckets.length;i++){
            for(int j = 0; j < buckets[i].size(); j++){
                String word = buckets[i].getWord(j);
                int charIndex = bucketAt(word, maxLength, pos);
                int count = bucket[i].getCount(j);
                bucketsNew[charIndex].add(word, count);
            }
        }
        buckets = bucketsNew;
        }
        // new word count which contain sorted WordCount objects
        WordCount[] newWordCounts =  new WordCount[this.actualSize];

        // concatenate sorted buckets into new array
    int j = 0;
    for (int bucket = 0; bucket < bucket.length; bucket++) {
        for (int i = 0; i < buckets[bucket].size(); i++) {
            newWordCounts[j] = buckets[bucket].get(i);
            j++;
        }
    }
    this.wordCounts = newWordCounts;
    this.maxSize = this.actualSize;
    }
    }
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
    }

private void calculateNormalizedWeights(DocumentCollection dc) {
    if (dc == null)
        return;

    this.calculateWeights(dc);
    // norm for weight normalization
    double norm = 0;
    for (int i = 0; i < this.size(); i++) {
        norm += Math.pow(this.wordCounts[i].getWeight(), 2);
    }
    if (norm > 0) {
        norm = Math.sqrt(norm);

        // if norm > 0, loop over words and calculate their normalized weight
        for (int i = 0; i < this.size(); i++) {
            this.wordCounts[i].setNormalizedWeight(this.wordCounts[i].getWeight() / norm);
        }
    } else {
    // if norm = 0, set normalized weight to 0
        for (int i = 0; i < this.size(); i++) {
            this.wordCounts[i].setNormalizedWeight(0);
    }
}
}}}
