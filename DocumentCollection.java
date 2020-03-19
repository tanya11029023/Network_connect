public class DocumentCollection {
    // begin of list
    private DocumentCollectionCell start;
    // last element in the collection 
    private DocumentCollectionCell end;
    private int size; 
    
    // empty list
    public DocumentCollection() {
    this.start = null; 
    this.end = null; 
    this.size = 0; 
    }

// add element in the beginning of the list
    public void prependDocument(Document doc){
        
        // if doc is empty, no change
        if (doc == null) {
            return;
        }
        
        if (this.isEmpty()) { // if list is empty, add as only element
            this.start = new DocumentCollectionCell(doc, null, null);
            // move former first object to the last
            this.end = start;     
            } else {  
                // if there are some elements, move next elements to the right
                // set next as former first object with , delete previous 
                start.setPrevious(new DocumentCollectionCell(doc, null, start));
                // first element is previous one (moving right)
                this.start = start.getPrevious();
        }
        size++; 
    }
                                  
    // add element in the end of list
    public void appendDocument (Document doc){
  
        // if doc is empty do nothing
        if (doc == null) {
            return;
        } 
        
        if (this.isEmpty()){
            //list is empty add as only element
                this.start = new DocumentCollectionCell(doc, null, null);
                this.end = start; }
            else { // there are some elements, search empty and add in the end 
                end.setNext(new DocumentCollectionCell(doc, end, null));
                end = end.getNext(); 
            }
        size++; 
    }
    // search index of document in this collection
    // if document contained many time, the lowest index will be returned
    public int indexOf(Document doc) {
        if(doc == null || this.isEmpty())
            return -1; 
        
        // loop over list and find document 
        DocumentCollectionCell tmp = this.start;
        int index = 0; 
        
        while (tmp != null) {
         if(tmp.getDoc().equals(doc)) {
         return index;
         }
         tmp = tmp.getNext();
         index++; 
        }
    return -1; 
    }
    // if document has index, its in this collection 
    public boolean contains(Document doc) {
    return (this.indexOf(doc) != -1); 
    }

    public boolean isEmpty(){
        return this.size == 0;
    }

    public int numDocs() {
        return this.size; 
    }
            
    public void clear() {
        this.start = null;
        this.end = null;
        this.size = 0; 
    } 
    public Document getFirstDocument(){
        if(this.isEmpty()){
        return null; 
        }
        return this.start.getDoc();
    }

    public Document getLastDocument(){
        if (this.isEmpty()){
        return null; 
        }
        return this.end.getDoc();
    }
            
    public void removeFirstDocument() {
        if (this.isEmpty()){
            return;
        } 
        if (this.numDocs() == 1){
            this.clear();
            return;
            }
        this.start = this.start.getNext(); 
        this.start.setPrevious(null);
        size--; 
    }
    public void removeLastDocument() {
        if (this.isEmpty()){
            return;
        } 
        if (this.numDocs() == 1){
            this.clear();
            return; 
            }
        this.end = this.end.getPrevious();
        this.end.setNext(null);
        size--; 
        }

    public boolean remove(int index) {
        if (index < 0 || index >= this.numDocs()){
        return false; 
        }
        
        if(this.isEmpty()) {
            return false; 
        }
        // remove first 
        if (index == 0) {
            this.removeFirstDocument();
            return true; 
            // remove last 
        } 
        if (index == this.numDocs() - 1){
            this.removeLastDocument();
            return true;
        } 
        
        // if index >=1 and size >=2 
        // loop to index, keep track of previous 
        DocumentCollectionCell actual = this.start.getNext();
        DocumentCollectionCell prev = this.start;
        
        for (int i = 1; i < index; i++) {
         prev = actual;
         actual = actual.getNext(); 
        }
        // delete actual 
        prev.setNext(actual.getNext()); 
        prev.getNext().setPrevious(prev); 
        size--; 
        return true; 
}
    private DocumentCollectionCell getDocCollectionCell(int index) {
        if (index < 0 || index >= this.numDocs())
            return null;

        DocumentCollectionCell tmp = this.start;
        for (int i = 0; i < index; i++) {
            tmp = tmp.getNext();
        }
        return tmp;
}
            
    public Document get(int index){
        if (index < 0 || index >= this.size) {
            return null;
        }
        return getDocCollectionCell(index).getDoc();
    }
    //loop over all documents to create a WordCountsArray containing *all* words of
    // all documents
    private WordCountsArray allWords() {
        DocumentCollectionCell tmp = this.start;
        WordCountsArray allWords = new WordCountsArray(0);

        while (tmp != null) {
        Document doc = tmp.getDoc();
        WordCountsArray wca = doc.getWordCounts();

           for (int i =  0; i < wca.size();i++) {
               allWords.add(wca.getWord(i), 0);
           }
               tmp = tmp.getNext();
        }
        return allWords;
    }
    // number of docs in which word has frequency >= 1
    public int noOfDocsContainingWord(String word) {
int res = 0;

for (int i = 0; i < numDocs(); i++) {
    if (get(i).getWordCounts().getIndexOfWord(word) != -1) {
        res++;
    }
}
return res;
}
    // calculate similirity between specified query and all documents in this
    // DocumentCollection. Then its sorts documents in this collection according to the similarity
    public void match(String searchQuery) {
        if (this.isEmpty())
            return;
        if (searchQuery == null || searchQuery.equals(""))
            return;
        // add query to collection
        Document searchQ = new Document(null, null, null, null, null, searchQuery);
        this.prependDocument(searchQ);
        // add word to document with count 0
        this.addZeroWordsToDocs();

        // sort all WordCountsArrays of all docs
        DocumentCollectionCell tmp = this.start;
        while (tmp != null ) {
            tmp.getDoc().getWordCounts().sort();
            tmp = tmp.getNext();
        }

        // calcualte similiraties with query document
        tmp = this.start.getNext();
        while (tmp != null) {
            tmp.setQuerySimilarity(tmp.getDoc().getWordCounts().computeSimilarity(searchQ.getWordCounts()));
            tmp = tmp.getNext();
        }
        this.removeFirstDocument();
    }
    // set of all words in all documents of collection and add every word to every document
    // in this colllection with count 0. After execution, all documents will contain the same words as before
    private void addZeroWordsToDocs() {
        WordCountsArray allWords = this.allWords();
        DocumentCollectionCell tmp = this.start;

        while(tmp != null){
            for (int i = 0; i < allWords.size(); i++){
                String word = allWords.getWord(i);
                tmp.getDoc().getWordCounts().add(word, 0);
            }
            tmp = tmp.getNext();
        }
    }

}
