package paf.mongoaggregation2.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import static paf.mongoaggregation2.model.Constants.*;

@Repository
public class AppsRepository {

	@Autowired
	private MongoTemplate template;

	/*
	db.apps.aggregate([
		{ $match: {Rating: {$ne: NaN} }},
  		{ $group: {_id: "$Category", Apps: { $push: "$App" }, AvgRating: { $avg: "$Rating" } }}
	])
	*/
	public List<Document> getApplicationsByCategory() {

		//Match - filter all records with NaN rating
		Criteria c = Criteria.where(FIELD_RATING).ne(Float.NaN);
		MatchOperation matchNotNaN = Aggregation.match(c);

		//Group apps by category and return average rating
		GroupOperation groupByCategory = Aggregation.group(FIELD_CATEGORY)
				.push(FIELD_APP).as(FIELD_APPS)
				.and(FIELD_RATING, AggregationExpression.from(MongoExpression.create(FIELD_CALAVG)));

		Aggregation pipeline = Aggregation.newAggregation(matchNotNaN, groupByCategory);
		AggregationResults<Document> results = template.aggregate(pipeline, COLLECTION, Document.class);

		return results.getMappedResults();
	}

}