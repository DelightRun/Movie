package com.movie.movie.entity.common;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "movie_similarity")
@EntityListeners(AuditingEntityListener.class)
public class MovieBasedRecommend extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "recommended_movie_id", nullable = false)
    private Movie recommendedMovie;

    @Column(name = "score", nullable = false)
    private Float score;
}
