package com.booknest.booknest_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
@RestController
public class BooknestBackendApplication {

	private String latestJoke = "No joke yet";
	private String lastRun = "Not executed yet";

	public static void main(String[] args) {
		SpringApplication.run(BooknestBackendApplication.class, args);
	}

	@Scheduled(fixedRate = 300000)
	public void callApi() {

		try {

			URL url = new URL(
				"https://official-joke-api.appspot.com/random_joke"
			);

			HttpURLConnection connection =
					(HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");

			BufferedReader br = new BufferedReader(
					new InputStreamReader(connection.getInputStream())
			);

			StringBuilder response = new StringBuilder();

			String line;

			while ((line = br.readLine()) != null) {
				response.append(line);
			}

			latestJoke = response.toString();
			lastRun = LocalDateTime.now().toString();

			System.out.println("================================");
			System.out.println("Time : " + lastRun);
			System.out.println("Joke : " + latestJoke);
			System.out.println("================================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/scheduler-status")
	public Map<String, String> getStatus() {

		Map<String, String> response = new HashMap<>();

		response.put("lastRun", lastRun);
		response.put("joke", latestJoke);

		return response;
	}
}
