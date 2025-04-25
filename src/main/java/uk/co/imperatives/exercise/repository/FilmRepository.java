package uk.co.imperatives.exercise.repository;


import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uk.co.imperatives.exercise.model.Film;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class FilmRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public List<Film> findRecommendedFilmsByCustomerId(int customerId) {
		final var sql = """
			WITH customer_rented_categories AS (
			    SELECT fc.category_id,
			           COUNT(*) AS rental_count
			    FROM rental r
			        JOIN inventory i ON r.inventory_id = i.inventory_id
			        JOIN film f ON i.film_id = f.film_id
			        JOIN film_category fc ON f.film_id = fc.film_id
			    WHERE r.customer_id = :customer_id
			    GROUP BY fc.category_id
			),
			film_rental_count AS (
			    SELECT f.film_id,
			           COUNT(*) AS total_rentals
			    FROM film f
			        JOIN inventory i ON f.film_id = i.film_id
			        JOIN rental r ON i.inventory_id = r.inventory_id
			    GROUP BY f.film_id
			)
			SELECT f.film_id AS id,
			       f.title,
			       f.description,
			       c.name AS category,
			       f.rating,
			       f.length
			FROM film f
			         JOIN film_category fc ON f.film_id = fc.film_id
			         JOIN category c ON fc.category_id = c.category_id
			         LEFT JOIN film_rental_count frc ON f.film_id = frc.film_id
			         LEFT JOIN customer_rented_categories crc ON fc.category_id = crc.category_id
			WHERE NOT EXISTS (
			    SELECT 1
			    FROM rental r
			    JOIN inventory i ON r.inventory_id = i.inventory_id
			    WHERE i.film_id = f.film_id AND r.customer_id = :customer_id
			)
			ORDER BY COALESCE(crc.rental_count, 0) DESC,
			         COALESCE(frc.total_rentals, 0) DESC,
			         f.title
			LIMIT 10;
			""";

		return jdbcTemplate.queryForStream(
				sql,
				Map.of("customer_id", customerId),
				(rs, rowNum) -> new Film(
					rs.getLong("id"),
					rs.getString("title"),
					rs.getString("description"),
					rs.getString("category"),
					rs.getString("rating"),
					rs.getInt("length")
				))
			.toList();
	}

}
