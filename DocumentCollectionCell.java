public class DocumentCollectionCell {
    // pointer to the next /previos cell 
private DocumentCollectionCell next; 
private DocumentCollectionCell previous; 
    // document in this cell
private Document doc;
    //similarity of the document in this cell 
private double querySimilarity; 
    
public DocumentCollectionCell (Document doc, DocumentCollectionCell previous, DocumentCollectionCell next){
    this.document = document; 
    this.next = next;
    this.previous = previous;
    this.querySimilarity = 0;
}
    
public void setNext (DocumentCollectionCell next){
    this.next = next;
}
    
public void setPrevious(DocumentCollectionCell previous) {
    this.previous = previous; 
}
    
public Document setDocument(Document document) {
    Document oldDoc = this.document;
    this.document = document;
    return oldDoc; 
}
public void setQuerySimilarity(double querySimilarity) {
    this.querySimilarity = querySimilarity; 
}
public DocumentCollectionCell getPrevious() { 
    return previous;
}
public DocumentCollectionCell getNext (){
    return next;
}
public Document getDoc () {
    return doc;
}
public double getQuerySimilarity(){
    return querySimilarity; 
}

}
