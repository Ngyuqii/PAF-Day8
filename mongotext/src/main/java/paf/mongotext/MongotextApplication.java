package paf.mongotext;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.mongotext.repository.CommentRepository;

@SpringBootApplication
public class MongotextApplication implements CommandLineRunner {

	@Autowired
	private CommentRepository commentRepo;

	public static void main(String[] args) {
		SpringApplication.run(MongotextApplication.class, args);
	}

	@Override
	public void run(String... args) {

		List<Document> results = commentRepo.searchCommentText("enjoyed", "good", "fun");

		for (Document d: results) 
			System.out.printf(">>>> %s\n\n", d.toJson());
	}

}
