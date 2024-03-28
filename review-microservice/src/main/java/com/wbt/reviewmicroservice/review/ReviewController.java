package com.wbt.reviewmicroservice.review;

import com.wbt.reviewmicroservice.review.dto.ReviewRequest;
import com.wbt.reviewmicroservice.review.dto.ReviewResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"/api/v1/reviews"})
public record ReviewController(ReviewJpaService reviewJpaService) {

    @GetMapping(path = {"/all"})
    public ResponseEntity<List<ReviewResponse>> allReviews() {
        return new ResponseEntity<>(this.reviewJpaService.fetchAll(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> companyReviews(final @RequestParam(name = "company") Long companyId) {
        return new ResponseEntity<>(this.reviewJpaService.fetchCompanyReviews(companyId), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<?> getReview(final @PathVariable(name = "id") Long reviewId) {
        final var response = this.reviewJpaService.fetchById(reviewId);
        if (response == null)
            return new ResponseEntity<>("Resource review with Id %s not found".formatted(reviewId), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> create(final @RequestBody ReviewRequest request, final @RequestParam(name = "company") Long id) {
        final var isSaved = this.reviewJpaService.addReview(request, id);
        if (isSaved) return new ResponseEntity<>("Review resource successfully saved", HttpStatus.CREATED);
        return new ResponseEntity<>("Unable to save this resource", HttpStatus.BAD_REQUEST);
    }

    @PutMapping(path = {"/{id}"})
    public ResponseEntity<String> update(final @PathVariable(name = "id") Long reviewId, final @RequestBody ReviewRequest request, final @RequestParam(name = "company") Long companyId) {
        final var isUpdated = this.reviewJpaService.update(reviewId, request, companyId);
        if (isUpdated) return new ResponseEntity<>("Review resource successfully updated", HttpStatus.OK);
        return new ResponseEntity<>("Unable to update review resource with Id %s".formatted(reviewId), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<String> remove(final @PathVariable(name = "id") Long reviewId) {
        final var isRemoved = this.reviewJpaService.remove(reviewId);
        if (isRemoved) return new ResponseEntity<>("Review resource successfully removed", HttpStatus.OK);
        return new ResponseEntity<>("Unable to remove review resource with Id %s".formatted(reviewId), HttpStatus.BAD_REQUEST);
    }
}
