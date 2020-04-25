public class LinkedDocumentCollection extends DocumentCollection {
    LinkedDocument doc;
    public LinkedDocumentCollection(){
        super();
    }
    @Override
    public void prependDocument(Document doc) {
        // if its LinkedDocument and Collection doesn't contain this doc
        if (doc instanceof LinkedDocument && !contains(doc)){
            super.prependDocument(doc);
        }
    }
    @Override
    public void appendDocument(Document doc) {
        if (doc instanceof LinkedDocument && !contains(doc))
            super.appendDocument(doc);
    }

    // calculate incoming links of every LinkedDoc on basis of outcome link
    public void calculateIncomingLinks() {
        // go through all docs
        for (int i = 0; i < this.numDocs(); i++) {
            // cast result to LinkedDoc and save it in doc
            LinkedDocument doc = ((LinkedDocument)this.get(i));
            // get collection of outgoing docs
            LinkedDocumentCollection outgoingDocs = doc.getOutgoingLinks();
            // go through all outgoing docs
            for (int j = 0; j < outgoingDocs.numDocs(); j++){
                // create new outgoing doc from collection
                LinkedDocument outgoingDoc = ((LinkedDocument)outgoingDocs.get(i));
                // add incoming link to new outgoing doc
                outgoingDoc.addIncomingLink(doc);
            }
        }
    }

    // public crawl calls private call and save it in resColl
    public LinkedDocumentCollection crawl() {
        LinkedDocumentCollection resColl = new LinkedDocumentCollection();
        crawl(resColl);
        return resColl;
    }
    // add all LinkedDocuments of LinkedDocumentCollection to resultCollection
    // proceed recursively with LDC, which correspond to outcomeLinks of LinkedDocuments
    private void crawl(LinkedDocumentCollection resultCollection){
        boolean added = false;
        // go through all docs in collection
        for (int i = 0; i < this.numDocs(); i++){
            // create new doc from collection
            Document doc = this.get(i);
            // if resultCollection doesn't contain this doc
            if (!resultCollection.contains(doc)) {
                added = true;
                resultCollection.appendDocument(this.get(i));
            }
        } // leave crawl if no new link was added (stop for crawl)
        if (!added) {
            return;
        } // if doc was added, go through all docs
        for (int i = 0; i < this.numDocs(); i++){
            LinkedDocument doc = ((LinkedDocument)this.get(i));
            // get ourgoingLink of doc
            LinkedDocumentCollection col = doc.getOutgoingLinks(resultCollection);
            // start crawl to search new links
            col.crawl(resultCollection);
        }
    }

}
