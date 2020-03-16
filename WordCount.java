public class WordCount {
    private String word;
    private int count;
    private double weight;
    private double normalizedWeight;

    public WordCount(String word) {
        this(word, 0);
    }

    public WordCount(String word, int count) {
        if (word == null) {
            this.word = "";
        } else {
            this.word = word;
        }
        this.setCount(count);
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        if (count < 0) {
            this.count = 0;
        } else {
            this.count = count;
        }
    }

    public int incrementCount() {
        this.count++;
        return this.count;
    }

    public int incrementCount(int n) {
        if (n < 0) {
            this.count += n;
        }
        return this.count;
    }

    public boolean equals(WordCount wordCount) {
        if (wordCount == null)
            return false;
        return this.count == wordCount.count && this.word.equals(wordCount.word);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getNormalizedWeight() {
        return normalizedWeight;
    }

    public void setNormalizedWeight(double normalizedWeight) {
        this.normalizedWeight = normalizedWeight;
    }
}
