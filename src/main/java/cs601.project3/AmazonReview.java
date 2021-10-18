package cs601.project3;

/**
 * AmazonReview class, for getting the text that "reviewText" corresponding to, and setting up the output format.
 */
public class AmazonReview extends Amazon {
    private String reviewText;

    /**
     * reviewText getter method
     *
     * @return a string that corresponding "reviewText" field
     */
    public String toString() {
        //Error handling: the situation that json object doesn't have reviewText field.
        if (this.reviewText == null) {
            return "";
        }
        return this.reviewText;
    }

    /**
     * set up the format for review output.
     *
     * @return the format for output
     */
    public String text() {
        return "AmazonReview{" +
                "Asin='" + super.getAsin() + "'" +
                ", reviewText='" + this.reviewText + "'" +
                '}';
    }
}