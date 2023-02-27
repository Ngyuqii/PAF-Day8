package paf.mongoaggregation2;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.mongoaggregation2.repository.AppsRepository;

@SpringBootApplication
public class Mongoaggregation2Application implements CommandLineRunner {

	@Autowired
	private AppsRepository appsRepo;

	public static void main(String[] args) {
		SpringApplication.run(Mongoaggregation2Application.class, args);
	}

	@Override
	public void run(String... args) {

		List<Document> results = appsRepo.getApplicationsByCategory();

		for (Document d: results) 
			System.out.printf(">>>> %s\n\n", d.toJson());
	}

}