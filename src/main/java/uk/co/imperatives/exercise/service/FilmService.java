package uk.co.imperatives.exercise.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.co.imperatives.exercise.model.Film;
import uk.co.imperatives.exercise.repository.FilmRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

	private final FilmRepository filmRepository;

	public List<Film> getRecommendations(int customerId) {
		if(customerId <= 0) {
			throw new IllegalArgumentException("Customer id must be positive");
		}

		return filmRepository.findRecommendedFilmsByCustomerId(customerId);
	}

}
