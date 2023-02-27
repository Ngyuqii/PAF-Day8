package paf.mongoaggregation.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import static paf.mongoaggregation.model.Constants.*;

import java.util.List;

@Repository
public class TvRepository {

	@Autowired
	private MongoTemplate template;

	/*
	db.tv.aggregate([
		{ $match: { language: { $regex: 'english', $options: 'i' }}},
		{ $project: { _id: 0, name: 1, genres: 1, url: 1}},
		{ $limit: 3}
	])
	*/
	public List<Document> find() {

		//Match - return all english shows
		Criteria c = Criteria.where(FIELD_LANGUAGE).regex("english", "i");
		MatchOperation matchLang = Aggregation.match(c);

		//Project - return data with attributes name, genres and url
		ProjectionOperation project = Aggregation.project()
				.andExclude(FIELD_PKID)
				.andInclude(FIELD_NAME, FIELD_GENRES, FIELD_URL);

		//Limit to 3 results
		LimitOperation limitResult = Aggregation.limit(3);

		Aggregation pipeline = Aggregation.newAggregation(matchLang, project, limitResult);
		AggregationResults<Document> results = template.aggregate(pipeline, COLLECTION, Document.class);

		return results.getMappedResults();
	}

	/*
	db.tv.aggregate([
		{ $group: { _id: "$runtime", shows: {$push: "$name"}, total: {$sum:1} }},
		{ $sort: {total: -1}}
	])
	*/
	public List<Document> groupTvShowsByRuntime() {

		//Group by runtime and return show names and count as attributes
		GroupOperation groupByRuntime = Aggregation.group(FIELD_RUNTIME)
				.push(FIELD_NAME).as(FIELD_SHOWS)
				.count().as(FIELD_TOTAL);

		//Sort in descending order
		SortOperation sortByTotal = Aggregation.sort(Sort.by(Direction.DESC, FIELD_TOTAL));
		
		Aggregation pipeline = Aggregation.newAggregation(groupByRuntime, sortByTotal);
		AggregationResults<Document> results = template.aggregate(pipeline, COLLECTION, Document.class);
		
		return results.getMappedResults();
	}

	/*
	db.tv.aggregate([
		{ $project: { _id: 0, title: {$concat: ["$name", " (", {$toString: "$runtime"}, ")"]}, rating: "$rating.average"}}
	])
	*/
	public List<Document> getTitleAndRating() {
		ProjectionOperation project = Aggregation.project()
			.and(AggregationExpression.from(MongoExpression.create(CONCAT_NAMEANDRUNTIME))).as(FIELD_TITLE)
			.and(FIELD_AVGRATING).as(FIELD_RATING)
			.andExclude(FIELD_PKID);

		Aggregation pipeline = Aggregation.newAggregation(project);
		AggregationResults<Document> results = template.aggregate(pipeline, COLLECTION, Document.class);
		
		return results.getMappedResults();
	}
}