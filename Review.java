
public class Review {

    private Author author;
    private Document reviewedDocument;
    private String language;
    private Date releaseDate;
    private int rating;
    public static final int MAX_RATING = 10;
    public static final int MIN_RATING = 0;

    public Review (Author author, Document reviewedDocument, String language, Date releaseDate, int rating){
        this.setLanguage(language);
        this.setRating(rating);
        this.author = author;
        this.reviewedDocument = reviewedDocument;
        this.releaseDate = releaseDate;
    }

    public Author getAuthor() {return author; }
    public Document getReviewedDocument() {return reviewedDocument; }
    public String getLanguage() {return language; }
    public Date getReleaseDate() {return releaseDate; }
    public int getRating() {return rating; }
    public String toString() {
        return this.reviewedDocument.toString() + " is rated " + this.rating + "by" +
                this.author.toString();
    }
    public int getAge(Date today) {return this.releaseDate.getAgeInDaysAt(today); }

    public void setAuthor(Author author) {this.author = author; }

    public void setLanguage(String language) {
        if (language == null) {
            this.language = "";
        } else {
            this.language = language;
        }
    }
    // set rating of the reviews
    public void setRating(int rating) {
        if (rating < Review.MIN_RATING) {
            this.rating = Review.MAX_RATING;
        } else if (rating > Review.MAX_RATING) {
            this.rating = Review.MAX_RATING;
        } else {
            this.rating = rating;
        }
    }

    // checks if reviews are equal
    public boolean equals (Review review) {
        boolean result = true;
        if (review != null) {
            if (this.author.equals(review.author) && this.reviewedDocument.equals(review.reviewedDocument) &&
            this.language.equals(review.language) && this.releaseDate.equals(review.releaseDate) && this.rating == review.rating) {
                result = true;
            } else {
                System.out.println("review is empty");
                result = false;
            }
        }return result;
    }
    }
