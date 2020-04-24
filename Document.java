public class Document {
private String title;
private String description;
private String language;
private Date releaseDate;
private Author author;
public static final String[] SUFFICES = { "ab", "al", "ant", "artig", "bar", "chen", "ei", "eln", "en", "end", "ent",
        "er", "fach", "fikation", "fizieren", "fähig", "gemäß", "gerecht", "haft", "haltig", "heit", "ieren", "ig", "in",
        "ion", "iren", "isch", "isieren", "isierung", "ismus", "ist", "ität", "iv", "keit", "kunde", "legen", "lein",
        "lich", "ling", "logie", "los", "mal", "meter", "mut", "nis", "or", "sam", "schaft", "tum", "ung", "voll", "wert",
        "würdig", "ie" };
// words of this documents and their counts
private WordCountsArray wordCounts;

public Document(String title, String description, String language, Date releaseDate, Author author, String content){

    this.setTitle(title);
    this.setDescription(description);
    this.setLanguage(language);
    this.releaseDate = releaseDate;
    this.author = author;
    this.addContent(content);
}

public String getTitle() { return title; }
public String getDescription() {return description; }
public String getLanguage() {return language; }
public Date getReleaseDate() {return releaseDate; }
public Author getAuthor() {return author; }
public WordCountsArray getWordCounts() { return this.wordCounts; }

public String toString() {
        return this.title + " by " + this.author.toString();
    }

public int getAgeAt (Date today) {return this.releaseDate.getAgeInDaysAt(today); }

public void setTitle(String title) {
    if (title == null) {
        this.title = "";
    } else {
        this.title = title;
    }
}
public void setLanguage(String language) {
    if (language == null) {
        this.language = "";
    } else {
        this.language = language;
    }
}

public void setDescription(String description) {
    if (description == null) {
        this.description = "";
    } else {
        this.description = description;
    }
}

public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }

public void setAuthor(Author author) {this.author = author; }

/* split specified text into its single words
search spaces, splits words at the spaces and returns array of separated words.
Assumed that all text written in lower case letters.
 */
private static String[] tokenize(String content) {
    int wordCount = 0;

    // calculate spaces in text
    for (int i = 0; i < content.length(); i++){
        if (content.charAt(i) == ' '){
            wordCount++;
        }
    }
    // + one word without space in its end
    wordCount++;
    // resulting array
    String[] words = new String[wordCount];

    String word = "";
    int wordIndex = 0;

    for (int i = 0; i <= content.length(); i++) {
        // reached end of content OR end of word
        if (i == content.length() || content.charAt(i) == ' '){
            if (word.length() > 0) {
                // add word in array, increment to the index of the next word
                words[wordIndex] = word;
                wordIndex++;

                // empty word for next loop
                word = "";
            }
        } else {
            // not end of word: append character
            word = word + content.charAt(i);
        }
    }
    return words;
    }

    private void addContent(String content) {
    String[] words = Document.tokenize(content);

    this.wordCounts = new WordCountsArray(0);

    for (int i = 0; i < words.length; i++){
        String word = words[i];
        // find suffix and cut it
        String suffix = Document.findSuffix(word);
        word = Document.cutSuffix(word, suffix);

        this.wordCounts.add(word, 1);
    }}
    // Determines if last characters of word1 and word2 are equal
    // n : how many characters to be checked
    private static boolean sufficesEqual(String word1, String word2, int n){
    if (n > word1.length() || n > word2.length()) {
        return false;
    }

    boolean isEqual = true;
    int i = 0;

    while(isEqual && i < n){
        // begin comparison at last char
        if (word1.charAt(word1.length()-1-i) != word2.charAt(word2.length()-1-i)){
            isEqual = false;
        }
        i++;
    }
    return isEqual;
    }
    /*
    Method uses suffices to find out, whether specified words ends with one of these suffices
     */
    private static String findSuffix(String word) {
        if (word == null || word.equals("")){
            return null;
        }

        String suffix = "";
        String suffixHit = "";
        int i = 0;

        while (i < Document.SUFFICES.length){
            suffix = Document.SUFFICES[i];

        // checks, if this suffix is a suffix of word
        if (sufficesEqual(word, suffix, suffix.length())){
            if(suffixHit.length() < suffix.length()) {
                suffixHit = suffix;
            }
        }
        i++;
        }
        return suffixHit;
    }

    /*
    if suffix is a suffix of word, then it'll be cutted off from word and
    remaining word stem is returned
     */
    private static String cutSuffix(String word, String suffix) {
        if (suffix == null || suffix.equals("")){
            return word;
        }
        if (word == null) {
            return null;
        }

        // not a suffix
        if (!sufficesEqual(word, suffix, suffix.length())){
            return word;
        }

        String wordWithoutSuffix = "";

        for(int i = 0; i < word.length() - suffix.length(); i++){
            wordWithoutSuffix = wordWithoutSuffix + word.charAt(i);
        }
        return wordWithoutSuffix;
    }

    public boolean equals (Document document){
        boolean result = true;
        if (document != null){
            if(this.title.equals(document.title) && this.language.equals(document.language) && this.description.equals(document.description)
            && this.releaseDate.equals(document.releaseDate) && this.author.equals(document.author)){
                result = true;
        } else {
            result = false;
            }} else {
            System.out.println("Document is empty");
            result = false;
        }
        return result;
    }

    public static void main (String [] args) {
        Date d1 = new Date(1, 1, 1990);
        Date d2 = new Date(1, 1, 1990);
        Author a1 = new Author(null, null, null, null, null);
        Author a2 = new Author("Tsoy", "Tatiana", d1, "Munich", "tanya11@com.com");
        Document doc1 = new Document("name", "en", "text", d1, a1, "llll");
        Document doc2 = new Document("name", "en", "text", d2, a2, "llll");
        System.out.println(doc1.equals(doc2));
    }


}
