package com.wbt.reviewmicroservice.review.dto;

public record ReviewRequest(String title, String content, Double rating) {
}
