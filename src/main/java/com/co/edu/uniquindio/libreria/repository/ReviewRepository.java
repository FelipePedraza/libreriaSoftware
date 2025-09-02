
package com.co.edu.uniquindio.libreria.repository;

import com.co.edu.uniquindio.libreria.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
