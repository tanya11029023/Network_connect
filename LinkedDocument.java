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
        String pattern = "";
    }

}
