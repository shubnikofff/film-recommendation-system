package uk.co.imperatives.exercise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmRecommendationApplicationTests {

	private static final DockerImageName IMAGE_NAME = DockerImageName.parse("dvdrental:latest")
		.asCompatibleSubstituteFor("postgres:17");

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(IMAGE_NAME)
		.withDatabaseName("dvdrental")
		.withUsername("postgres")
		.withPassword("postgres");

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ResourceLoader resourceLoader;

	@Test
	void should_return_recommended_films_for_given_customer_id() throws IOException {
		final var responseEntity = restTemplate.getForEntity("/api/v1/film/recommendations?customerId=1", String.class);

		final var resource = resourceLoader.getResource("classpath:examples/customer-1-recommended-films.json");
		final var expectedJson = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.hasBody()).isTrue();
		assertThatJson(responseEntity.getBody()).isEqualTo(expectedJson);
	}

}
