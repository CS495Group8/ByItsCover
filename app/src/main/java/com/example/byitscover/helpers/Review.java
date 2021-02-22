package com.example.byitscover.helpers;

/**
 * This class is represents a review.
 * If a field is null, the corresponding property of the review is unknown.
 *
 * @author Jack
 * @version 1.0
 */

public class Review {
    // TODO: Discuss group preferences on immutability
    private final String reviewer;
    private final String comment;
    private final double rating;

    public Review(String reviewer, String comment, double rating) {
        this.reviewer = reviewer;
        this.comment = comment;
        this.rating = rating;
    }

    /**
     * Getter for reviewer
     * @return reviewer
     */
    public String getReviewer() {
        return reviewer;
    }

    /**
     * Getter for comment
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Getter for rating
     * @return rating
     */
    public double getRating() {
        return rating;
    }
}
