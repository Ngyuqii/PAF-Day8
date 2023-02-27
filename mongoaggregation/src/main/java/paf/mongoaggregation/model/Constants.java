package paf.mongoaggregation.model;

public class Constants {

	//Set database and collection names
    public static final String DATABASE = "shows";
    public static final String COLLECTION = "tv";

	 //All use fields
	public static final String FIELD_PKID = "_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_URL = "url";
	public static final String FIELD_LANGUAGE = "language";
	public static final String FIELD_GENRES = "genres";
	public static final String FIELD_RUNTIME = "runtime";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_TOTAL = "total";
	public static final String FIELD_SHOWS = "shows";
	public static final String FIELD_AVGRATING = "rating.average";
	public static final String FIELD_RATING = "rating";

	public static final String CONCAT_NAMEANDRUNTIME = """
        $concat: ["$name", " (", {$toString: "$runtime"}, ")"]        
	""";

}