package com.wbt.reviewmicroservice.review;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private Double rating;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Long companyId;

    public Review(String title, String content, Double rating, Long companyId) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.companyId = companyId;
    }

    public Review(Long id, String title, String content, Double rating, Long companyId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.companyId = companyId;
    }

    public Review(Long id, String title, String content, Double rating, LocalDateTime createdAt, Long companyId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.companyId = companyId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Review review = (Review) o;
        return getId() != null && Objects.equals(getId(), review.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
