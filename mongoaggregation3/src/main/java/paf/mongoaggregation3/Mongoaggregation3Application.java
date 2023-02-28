package paf.mongoaggregation3;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.mongoaggregation3.repository.TvRepository;

@SpringBootApplication
public class Mongoaggregation3Application implements CommandLineRunner {

	@Autowired
	private TvRepository tvRepo;

	public static void main(String[] args) {
		SpringApplication.run(Mongoaggregation3Application.class, args);
	}

	@Override
	public void run(String... args) {

		List<Document> results = tvRepo.countGenres();
		//List<Document> results = tvRepo.ratingsChart();

		for (Document d: results) 
			System.out.printf(">>>> %s\n\n", d.toJson());
	}

}