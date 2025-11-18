package com.travelapp.travel_api.repository;

import com.travelapp.travel_api.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Repository interface for Trip entity
public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query(
            value = """
                    SELECT DISTINCT t.*
                    FROM trips t
                    LEFT JOIN LATERAL UNNEST(
                        COALESCE(
                            (SELECT array_agg(tt.tag) FROM trip_tags tt WHERE tt.trip_id = t.id),
                            ARRAY[]::varchar[]
                        )
                    ) AS tag(tag_value) ON TRUE
                    WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                       OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                       OR LOWER(tag.tag_value) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    """,
            nativeQuery = true
    )
    List<Trip> searchByKeyword(@Param("keyword") String keyword);

    List<Trip> findByAuthorId(Long authorId);
}
