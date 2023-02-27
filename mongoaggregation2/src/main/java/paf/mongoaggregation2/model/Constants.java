package paf.mongoaggregation2.model;

public class Constants {

	//Set database and collection names
    public static final String DATABASE = "playstore";
    public static final String COLLECTION = "apps";

	//All use fields
	public static final String FIELD_APP = "App";
	public static final String FIELD_CATEGORY = "Category";
	public static final String FIELD_RATING = "Rating";
	public static final String FIELD_APPS = "Apps";
	public static final String FIELD_AVGRATING = "AverageRating";
	
	public static final String FIELD_CALAVG = """ 
		$avg: "$Rating" 
	""";

}