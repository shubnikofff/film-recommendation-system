package uk.co.imperatives.exercise.controller;


import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.imperatives.exercise.model.Film;
import uk.co.imperatives.exercise.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("api/v1/film")
@RequiredArgsConstructor
public class FilmRecommendationController {

	private final FilmService filmService;

	@GetMapping("recommendations")
	public List<Film> getRecommendations(@RequestParam("customerId") @Positive Integer customerId) {
		return filmService.getRecommendations(customerId);
	}

}
