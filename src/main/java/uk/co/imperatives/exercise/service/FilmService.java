package uk.co.imperatives.exercise.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.co.imperatives.exercise.model.Film;
import uk.co.imperatives.exercise.repository.FilmRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

	private final FilmRepository filmRepository;

	public List<Film> getRecommendations(int customerId) {
		if(customerId <= 0) {
			throw new IllegalArgumentException("Customer id must be positive");
		}

		log.info("Looking for film recommendations. Customer id {}.", customerId);

		return filmRepository.findRecommendedFilmsByCustomerId(customerId);
	}

}
