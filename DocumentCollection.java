public class DocumentCollection {
    // begin of list
    private DocumentCollectionCell start;
    // last element in the collection 
    private DocumentCollectionCell end;
    private int size;
    // empty list
    public DocumentCollection(){
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
    // if document contained many times, the lowest index will be returned
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
    // calculate similarity between specified query and all documents in this
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
    private static DocumentCollectionCell[] mergeSortIt(DocumentCollectionCell[] a){
        // separate array in one elements arrays
        DocumentCollectionCell[][] parts = new DocumentCollectionCell[a.length][];
        for (int i = 0; i < a.length; i++){
            parts[i] = new DocumentCollectionCell[] {a[i]};
        }
        while (parts.length > 1) {
            DocumentCollectionCell[][] partsNew = new DocumentCollectionCell[parts.length + 1/2][];
            for (int i = 0; i < partsNew.length; i++) {
                // fill up new array with merged and sorted numbers
                if ( 2 * i + 1 < parts.length)
                    partsNew[i] = merge(parts[2*i], parts[2 * i + 1]);
                else
                    partsNew[i] = parts[2 * i];
            }
            parts = partsNew;
        }
        return parts[0];
    }
    private static DocumentCollectionCell[] merge(DocumentCollectionCell[] a, DocumentCollectionCell[] b){
        DocumentCollectionCell[] merged = new DocumentCollectionCell[a.length + b.length];
        int aIndex = 0;
        int bIndex = 0;
        // joins all elements in one array
        for (int i = 0; i < merged.length; i++){
            // fill out second half of array
            if (aIndex >= a.length)
                merged[i] = b[bIndex++];
            // fill out first half of array if
            else if (bIndex >= b.length)
                merged[i] = a[aIndex++];
            // fill out first half of array with docs which are more relevant to request
            else if (a[aIndex].getQuerySimilarity() > b[bIndex].getQuerySimilarity())
                merged[i] = a[aIndex++];
            // fill out second half of array with resting elements
            else
                merged[i] = b[bIndex];
        }
        return merged;
    }

    private void swap(DocumentCollectionCell cell1, DocumentCollectionCell cell2){
        // swap cells with doc and corresponding similarity
        Document tmpDoc = cell1.getDoc();
        double tmpSim = cell1.getQuerySimilarity();

        cell1.setDocument(cell2.getDoc());
        cell1.setQuerySimilarity(cell2.getQuerySimilarity());

        cell2.setDocument(tmpDoc);
        cell2.setQuerySimilarity(tmpSim);
    }
    private void sortBySimilarityDesc() {
        // copy rows in array
        DocumentCollectionCell[] cells = new DocumentCollectionCell[this.numDocs()];
        DocumentCollectionCell actCell = this.start;
        for(int i = 0; actCell != null; i++){
            cells[i] = actCell;
            actCell = actCell.getNext();
        }
        // sort
        cells = mergeSortIt(cells);

        // convert to list
        DocumentCollectionCell previous = null;
        DocumentCollectionCell next = null;
        for(int i = 0; i < cells.length; i++){
            if ( i > 0)
                previous = cells[i - 1];
            if (i == cells.length - 1)
                next = null;
            else
                next = cells[i+1];
            cells[i].setNext(next);
            cells[i].setPrevious(previous);
        }
        this.start = cells[0];
    }
    public double getQuerySimilarity(int index){
        if (index < 0 || index >= this.numDocs()) {
            return -1;
        }
        return this.getDocCollectionCell(index).getQuerySimilarity();
    }
    public String toString() {
        if (this.numDocs() == 0){
            return "[]";
        }
        if (this.numDocs() == 1){
            return "[" + this.get(0).getTitle() + "]";
        }
        String res = "[";
        for (int i = 0; i < this.numDocs() - 1; i++){
            res += this.get(i).getTitle() + ", ";
        }
        res += this.get(this.numDocs()-1).getTitle() + "]";
        return res;
    }


}
