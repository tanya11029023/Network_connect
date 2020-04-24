import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkedDocument extends Document {
    // Variables for ID and links
    String ID;
    String[] links;
    LinkedDocumentCollection incomingLinks;
    LinkedDocumentCollection outgoingLinks;

    public LinkedDocument(String ID, String title, String language, String description, String content, Date releaseDate, Author author){
        // call constructor of superclass
        super(title, language, description, releaseDate, author, content);
        this.ID = ID;
        this.links = findOutgoingIDs(content);
        this.incomingLinks = null;
        this.outgoingLinks = null;
        this.setLinkCountZero();
    }

    public String getID(){
        return ID;
    }

    // override of equals method in Document class
    @Override
    public boolean equals(Document document){
        return document instanceof LinkedDocument && ((LinkedDocument)document).ID.equals(this.ID) && super.equals(document);
    }

    //find IDs in document
    private String[] findOutgoingIDs(String text) {
        // use regex to find patter in text
        String pattern = "\\blink:(\\S+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        // how many words in result array
        int count = 0;
        // index of word in result array
        int index = 0;
        while (m.find()) {
            count++;
        }
        // result array
        String[] res = new String[count];
        // if match find pattern
        if(m.find(0)){
            do{
                res[index] = m.group(1);
                index++;
            } while(m.find());
        }
    return res;
    }

    // set count of links to 0
    private void setLinkCountZero(){
        // create WordCountsArray in superclass and get its counts
        WordCountsArray wca = super.getWordCounts();
        // go through all links
        for (int i = 0; i < this.links.length; i++){
            // String with all links
            String link = this.links[i];
            // find index of the word
            int indexOfLink = wca.getIndexOfWord(link);
            // if index -1 than add new link and set its frequency to 0
            if (indexOfLink == -1){
                wca.add(link, 0);
            } else {
                wca.setCount(indexOfLink, 0);
            }
        }
    }
}
