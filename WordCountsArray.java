public class WordCountsArray {

    // controlled WordCount object
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
    for (int i = 0; i < buckets.length; i++) {
        buckets[i] = new WordCountsArray(this.actualSize / buckets.length);
    }
    int maxLength = 0;
    for (int i = 0; i < this.actualSize; i++){
        maxLength = Math.max(maxLength, this.getWord(i).length());
    }
    // sort words into buckets
    for (int i = 0; i < this.actualSize; i++){
        String word = this.getWord(i);
        int charIndex = bucketAt(word, maxLength, 0);
        if (charIndex >= 0){
            int count = this.getCount(i);
            buckets[charIndex].add(word, count);
        }
    }
    for (int pos = 1; pos < maxLength; pos++){
        WordCountsArray[] bucketsNew = new WordCountsArray[26];
        for (int i = 0; i < bucketsNew.length; i++)
            bucketsNew[i] = new WordCountsArray(this.actualSize/26);
        for(int i = 0; i < buckets.length;i++){
            for(int j = 0; j < buckets[i].size(); j++){
                String word = buckets[i].getWord(j);
                int charIndex = bucketAt(word, maxLength, pos) - 'a';
                int count = buckets[i].getCount(j);
                bucketsNew[charIndex].add(word, count);
            }
        }
        buckets = bucketsNew;
        }
        // new word count which contain sorted WordCount objects
        WordCount[] newWordCounts =  new WordCount[this.actualSize];

        // concatenate sorted buckets into new array
    int j = 0;
    for (int bucket = 0; bucket < buckets.length; bucket++) {
        for (int i = 0; i < buckets[bucket].size(); i++, j++) {
            newWordCounts[j] = buckets[bucket].get(i);
        }
    }
    this.wordCounts = newWordCounts;
    this.maxSize = this.actualSize;
    }

private WordCount get(int index) {
    if (index < 0 || index >= this.actualSize) {
        return null;
    }
    return this.wordCounts[index];
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
}

public int size() {return this.actualSize; }

public String getWord(int index) {
    if (index < 0 || index >= this.actualSize){
        return null;
    }
    return this.wordCounts[index].getWord();
}

public int getCount(int index) {
    if (index < 0 || index >= this.actualSize)
        return 0;
    return this.wordCounts[index].getCount();
}

public int getIndexOfWord(String word) {
    if (word == null || word.equals("")){
        return -1;
    }

    // search word in array of WordCounts
    for(int i = 0; i < this.actualSize; i++) {
        if (this.wordCounts[i].getWord().equals(word)) {
            return i;
        }
    }
    return -1;
}

public void setCount(int index, int count) {
    if (index < 0 || index > this.actualSize) {
        return;
    }

    if (count < 0 ) {
        this.wordCounts[index].setCount(0);
    } else {
        this.wordCounts[index].setCount(index);
    }
}

private void doubleSize() {
    this.maxSize = this.maxSize * 2;

    if (this.maxSize <= 0) {
        this.maxSize = 1;
    }

    WordCount[] newWordCount = new WordCount[this.maxSize];

    for (int i = 0; i < wordCounts.length; i++) {
        newWordCount[i] = this.wordCounts[i];
    }
    this.wordCounts = newWordCount;
}
// if instance and specified WordCountsArray are equal
public boolean equals(WordCountsArray wca) {
    if (this == wca)
        return true;

    if (wca == null || this.size() != wca.size())
        return false;

    // compare word and their counts at every position
    for (int i = 0; i < this.size(); i++) {
        if (!this.getWord(i).equals(wca.getWord(i)) || this.getCount(i) != wca.getCount(i))
            return false;
    } return true;
}

// calculate similarity of instance and specified WordCountsArray from 0.00 to 1.00
public double computeSimilarity(WordCountsArray wca){
    if (wca == null)
        return 0;

    double scalarProThis = this.scalarProduct(this);
    double scalarProWca = this.scalarProduct(wca);

    double scalarProduct = 0;

    if (scalarProThis != 0 && scalarProWca != 0)
        scalarProduct = this.scalarProduct(wca) / (Math.sqrt(scalarProThis * scalarProWca));
    return scalarProduct;
}

public double scalarProduct(WordCountsArray wca) {
    if (wca == null || this.size() != wca.size() || (this != wca) && !this.wordsEqual(wca))
        return 0;
    double scalarProduct = 0;

    for (int i = 0; i < this.size(); i++)
        scalarProduct += this.getCount(i) * wca.getCount(i);
    return scalarProduct;
}

}
