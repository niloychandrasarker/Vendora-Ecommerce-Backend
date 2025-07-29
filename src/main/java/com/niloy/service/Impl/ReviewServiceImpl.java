package com.niloy.service.Impl;

import com.niloy.modal.Product;
import com.niloy.modal.Reviews;
import com.niloy.modal.User;
import com.niloy.repository.ReviewRepository;
import com.niloy.request.CreateReviewRequest;
import com.niloy.service.ReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Reviews createReview(CreateReviewRequest req, User user, Product product) {
        Reviews review = new Reviews();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);
        return reviewRepository.save(review);
    }

    @Override
    public List<Reviews> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Reviews updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
        Reviews review = findReviewById(reviewId);

        if(review.getUser().getId().equals(userId)){
            review.setReviewText(reviewText);
            review.setRating(rating);
            return reviewRepository.save(review);
        }
        throw new Exception("you can't update this review");
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        Reviews review = findReviewById(reviewId);
        if(review.getUser().getId().equals(userId)) {
            throw new Exception("you can't delete this review");
        }
        reviewRepository.delete(review);

    }

    @Override
    public Reviews findReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception("Review not found with id: " + reviewId));
    }
}
