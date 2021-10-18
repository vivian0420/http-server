package cs601.project3;

/**
 * Since both review json object and qa json object contain a common property, asin. And also, they have common
 * methods text(). Creating a super class is easier to maintain the project. For example, we can use same method
 * buildIndex to build the reviewIndex and qaIndex.Also, when we convert the json object to Amazon object, we don't
 * need to use 2 methods to create AmazonReview object and AmazonQA object separately.
 */
public abstract class Amazon {

    private String asin;

    /**
     * get asin method
     *
     * @return asin
     */
    public String getAsin() {
        return this.asin;
    }
}
