
import javax.swing.event.DocumentEvent;

public class DocumentCollection {
    // all elements of DocumentCollection
    private DocumentCollectionCell elements;
    // begin of list
    private DocumentCollectionCell start;
    // empty list
    public DocumentCollection() {elements = null; }

// add element in the beginning of the list
    public void prependDocument (Document doc){
        DocumentCollectionCell newEl = new DocumentCollectionCell(doc);
        /*
        create new list with current elements
        */
        DocumentCollectionCell curList = elements;

        // if doc is empty, no change
        if (doc == null) {
            return;
        } else { // if elements is still empty, put newEl in list
            if (elements == null) {
                elements = newEl;
            } else {
                // if there are some elements, move next elements to the next places
                while (elements.getNext() != null){
                    elements.setNext(elements.getNext());
                } // after moving all elements to next place, set newEl in the beginning
                elements = newEl;
            }
        }
    }
    // add element in the end of list
    public void appendDocument (Document doc){
        // create new cell for doc
        DocumentCollectionCell newEl = new DocumentCollectionCell(doc);
        // if doc is empty do nothing
        if (doc == null) {
            return;
        } else { // if doc is not empty, check if list is empty
            if (elements == null){
                elements = newEl; }
            else {
                while (elements.getNext() == null) {
                    // set new element on the end of list
                    elements.setNext(newEl);
                }
            }
        }
    }

    public boolean isEmpty(){
        return elements == null ? true : false;
    }

    public int numDocs() {
        int result = 1;
        while (elements.getNext()!= null){
            elements = elements.getNext();
            result++;
        }
        return result;
    }
    public Document getFirstDocument(){
        Document result = null;
        // cell for found first element
        DocumentCollectionCell found;
        if (isEmpty()){
            result = null;
        } else { // if list has elements --> find first one
            found = elements;
            result = found.getDoc();
        }
        return result;
    }

    public Document getLastDocument(){
        Document res = null;
        DocumentCollectionCell found;
        if (isEmpty()) {
            res = null;
        } else {
            while(elements.getNext() != null){
                found = elements.getNext();
                res = found.getDoc();
            }
        }
        return res;
    }
    public void removeFirstDocument() {
        if (isEmpty()){
            return;
        } else {
            while (elements.getNext() != null){
                elements = elements.getNext();
            }
        }
    }
    public void removeLastDocument() {
        if (isEmpty()){
            return;
        } else { // last element found
            if (elements.getNext() == null){
                elements.setNext(null);
            }
        }
    }

    public boolean remove(int index) {
        // if index less than number of documents, return
        if (index < numDocs()){
            return false;
        } else if (index == 0){
            removeFirstDocument();
            return true; }
        else if (index == numDocs()){
            removeLastDocument();
            return true;
        } else {
            int j = 0;
            // search document in list
            while (j < index -1 ){
                elements = elements.getNext();
                j++;
            } // element on index found --> set next on this place
            elements.setNext(elements.getNext());
        }
        return true;
    }
}
