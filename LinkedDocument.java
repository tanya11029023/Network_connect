import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkedDocument extends Document {
    // Variables for ID and links
    String ID;
    String[] links;
    LinkedDocumentCollection incomingLinks;
    LinkedDocumentCollection outgoingLinks;

    public LinkedDocument(String ID, String title, String language, String description, Date releaseDate, Author author, String content){
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
    public void addIncomingLink(LinkedDocument incomingLink) {
        if(incomingLink.ID.equals(this.ID)){
            return;
        } // if link is empty then create new LDCollection
        if (this.incomingLinks == null){
            this.incomingLinks = new LinkedDocumentCollection();
        } // if else, add document to LDCollection
        this.incomingLinks.appendDocument(incomingLink);
    }

    // create LinkedDocument from Data in parameter
    public static LinkedDocument createLinkedDocumentFromFile(String fileName) {
        // resulting string array contains rows of file, valid data has exactly 2 rows:
        // title and ID of linkedDocument
        String[] filetoLink = Terminal.readFile(fileName);
        if (filetoLink.length < 2)
            return null;
        String title = filetoLink[0];
        String content = filetoLink[1];
        return new LinkedDocument(fileName, title, "","", null, null, content);
    }

    // found IDs are name of data, create LinkedDocument
    // put LD to LDCollection, which represent outgoing links
    private void createOutgoingDocumentCollection(){
        // create LDCollection for outgoing links
        this.outgoingLinks = new LinkedDocumentCollection();
        // go through all links
        for (String link : this.links){
            if (link.equals(this.ID)){
                continue;
            }
            LinkedDocument newDoc = LinkedDocument.createLinkedDocumentFromFile(link);
            // add this LD to outgoing links
            this.outgoingLinks.appendDocument(newDoc);
        }
    }
    public LinkedDocumentCollection getOutgoingLinks(){
        if (outgoingLinks == null)
            createOutgoingDocumentCollection();
        return outgoingLinks;
    }
    public LinkedDocumentCollection getIncomingLinks(){
        return incomingLinks;
    }
    public LinkedDocumentCollection getOutgoingLinks(LinkedDocumentCollection alreadyLoadedCol){
        // create LDCollection for result
        LinkedDocumentCollection res = new LinkedDocumentCollection();
        // go through all links, continue if they are equal
        for (String link : this.links){
            if(link.equals(this.ID)){
                continue;
            }
            boolean alreadyLoaded = false;
            // go through LDCollection
            for (int i = 0; i < alreadyLoadedCol.numDocs(); i++) {
                // create doc from collection
                LinkedDocument doc = ((LinkedDocument)alreadyLoadedCol.get(i));
                if (doc.getID().equals(link)) {
                    alreadyLoaded = true;
                }
            }
            if(!alreadyLoaded) {
                LinkedDocument newDoc = LinkedDocument.createLinkedDocumentFromFile(link);
                res.appendDocument(newDoc);
            }
        }
        return res;
    }
}
