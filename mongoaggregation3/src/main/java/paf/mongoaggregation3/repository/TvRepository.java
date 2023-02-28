package paf.mongoaggregation3.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.BucketOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.stereotype.Repository;

import static paf.mongoaggregation3.model.Constants.*;

import java.util.List;

@Repository
public class TvRepository {

	@Autowired
	private MongoTemplate template;

	/*
	db.tv.aggregate([
		{ $unwind: "$genres" },
		{ $group: { _id: "$genres", total: {$sum: 1} }},
		{ $sort: { _id: 1} }
	])
	*/
	public List<Document> countGenres() {

		//Unwind genres array and create indivdual documents for each genre
		UnwindOperation unwindGenres = Aggregation.unwind(FIELD_GENRES);

		//Group by genres and return count
		GroupOperation groupByGenres = Aggregation.group(FIELD_GENRES)
										.count().as(FIELD_TOTAL);

		//Sort by _id in ascending order
		SortOperation sortByGenres = Aggregation.sort(Sort.by(Direction.ASC, FIELD_PKID));

		Aggregation pipeline = Aggregation.newAggregation(unwindGenres, groupByGenres, sortByGenres);
		AggregationResults<Document> results = template.aggregate(pipeline, COLLECTION, Document.class);
		
		return results.getMappedResults();
	}

	/*
	db.tv.aggregate([
	{ $bucket: { groupBy: "$rating.average", boundaries: [ 2.5, 5, 7.5 ], default: ">7.5",
    	output: { total: { $sum: 1 }, titles: { $push: "$name" } }}}
	])
	*/
	public List<Document> ratingsChart() {

		//Bucket - create a histogram of tv shows by ratings
		BucketOperation ratingsBucket = Aggregation.bucket(FIELD_AVGRATING)
											.withBoundaries(2.5, 5, 7.5)
											.withDefaultBucket(">7.5")
											.andOutputCount().as(FIELD_TOTAL)
											.andOutput(FIELD_NAME).push().as(FIELD_TITLES);

		Aggregation pipeline = Aggregation.newAggregation(ratingsBucket);
		AggregationResults<Document> results = template.aggregate(pipeline, COLLECTION, Document.class);
		
		return results.getMappedResults();
	}

}