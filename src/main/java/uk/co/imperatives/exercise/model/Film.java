package uk.co.imperatives.exercise.model;

public record Film(
	long id,
	String title,
	String description,
	String category,
	String rating,
	int length
) {

}
