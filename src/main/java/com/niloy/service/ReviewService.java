package com.niloy.service;

import com.niloy.modal.Product;
import com.niloy.modal.Reviews;
import com.niloy.modal.User;
import com.niloy.request.CreateReviewRequest;


import java.util.List;

public interface ReviewService {
    Reviews createReview(CreateReviewRequest req,
                         User user,
                         Product product);

    List<Reviews> getReviewByProductId(Long productId);
    Reviews updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception;

    void deleteReview(Long reviewId, Long userId) throws Exception;
    Reviews findReviewById(Long reviewId) throws Exception;
}
