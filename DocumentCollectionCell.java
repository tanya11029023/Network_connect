public class DocumentCollectionCell {

private DocumentCollectionCell next;
private Document doc;

public void setNext (DocumentCollectionCell next){
    this.next = next;
}
public void setDoc (Document doc){
    this.doc = doc;
}
public DocumentCollectionCell getNext (){
    return next;
}
public Document getDoc () {
    return doc;
}
public DocumentCollectionCell (Document doc){
this.next = null;
this.doc = doc;
}

}
