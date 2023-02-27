package paf.mongotext.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import static paf.mongotext.model.Constants.*;

@Repository
public class CommentRepository {

	@Autowired
	private MongoTemplate template;

	//Create a text index, then search the c_text attribute using text search
	//Adds a new attribute score to the search result that holds a value telling how well the document matches the search terms
	/*
	db.comment.createIndex({
		c_text: "text"
	})

	db.comment.find({ 
		$text: {
			$search: "?"
		}
	},
	{
		score: {
			$meta: "textScore"
		}
	})
	 */
	public List<Document> searchCommentText(String... texts) {

		//Create a text index
		TextIndexDefinition textIndex = new TextIndexDefinitionBuilder().onField(FIELD_COMMENT)
    										.build();

		//Print out the array of text input
		for (int i = 0; i < texts.length; i++) 
			System.out.printf(">>> texts[%d]: %s\n", i, texts[i]);

		//Set textcriteria to match any of the given word in texts array
		//Include score and sort by relevance
		TextCriteria c = TextCriteria.forDefaultLanguage().matchingAny(texts);
		TextQuery tQ = TextQuery.queryText(c)
							.includeScore(FIELD_TEXTSCORE)
							.sortByScore();

		//Limit search result to 5
		Query q = tQ.limit(5);

		return template.find(q, Document.class, COLLECTION);
	}
}