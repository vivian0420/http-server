package cs601.project3;

/**
 * AmazonQA class, for getting the text that "question" field and "answer" field corresponding to,
 * and setting up the output format.
 */
public class AmazonQA extends Amazon {

    // when convert json object to AmazonQA object, "tell" the compiler that AmazonQA object wants to include
    // two fields:question field and answer field from json object.
    private String question;
    private String answer;

    /**
     * QA text getter method
     *
     * @return a string that combines question field and answer field
     */
    @Override
    public String toString() {
        //Error handling: the situation that json object doesn't have question field and answer field.
        if (this.question == null && this.answer == null) {
            return "";
        }
        if (this.question == null) {
            return this.answer;
        }
        if (this.answer == null) {
            return this.question;
        }
        return this.question + " " + this.answer;
    }

    /**
     * set up the format for QA output.
     *
     * @return the format for output
     */

    public String text() {
        return "AmazonQA{" +
                "Asin='" + super.getAsin() + "'" +
                ", question='" + this.question + '\'' +
                ", answer='" + this.answer + '\'' +
                '}';
    }
}
