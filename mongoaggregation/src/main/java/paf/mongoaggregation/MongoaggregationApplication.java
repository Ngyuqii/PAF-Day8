package paf.mongoaggregation;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.mongoaggregation.repository.TvRepository;

@SpringBootApplication
public class MongoaggregationApplication implements CommandLineRunner {

	@Autowired
	private TvRepository tvRepo;

	public static void main(String[] args) {
		SpringApplication.run(MongoaggregationApplication.class, args);
	}

	@Override
	public void run(String... args) {

		List<Document> results = tvRepo.find();
		//List<Document> results = tvRepo.groupTvShowsByRuntime();
		//List<Document> results = tvRepo.getTitleAndRating();

		for (Document d: results) 
			System.out.printf(">>>> %s\n\n", d.toJson());
	}

}