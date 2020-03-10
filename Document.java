import javax.print.Doc;

public class Document{
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
}
