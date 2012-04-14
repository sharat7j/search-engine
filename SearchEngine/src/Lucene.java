
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.LockObtainFailedException;
public class Lucene {
	public static String FILES_TO_INDEX_DIRECTORY;
	public static final String DIRECTORY_PATH = SearchServlet.DIRECTORY_PATH;
	public static final String INDEX_DIRECTORY = DIRECTORY_PATH + "indexDirectory";
	public static final String DOCPATH = DIRECTORY_PATH + "CrawlerOutput";
	public static final String FIELD_PATH = "path";
	public static final String FIELD_CONTENTS = "contents";

	static Map<Integer,String> map = null;
	static WebGraph graph ;
	static HITSALG hitsAlgo ;	 

	static{
		try
		{
			// Enable when index needs to be created from scratch again			
			// CreateIndex();

			// Read the hashmap with DOCID as the key and URL as the value
			FileInputStream fis = null;
			ObjectInputStream in = null;	
			try
			{
				// Hashmap directory
				fis = new FileInputStream(DIRECTORY_PATH + "HashMap.ser"); // String filename=Path+"HashMap.ser";
				in = new ObjectInputStream(fis);
				map = (Map<Integer,String>)in.readObject();			   
				in.close();
				// Construct web graph using the link information
				File linkFile = new File(DIRECTORY_PATH + "links.txt");
				graph = new WebGraph(linkFile);
				hitsAlgo = new HITSALG(graph);
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			catch (ClassNotFoundException e){
				e.printStackTrace();
			}
		} catch (Exception e){

		}	
	}

	// Creates the Index from the crawler output present in 
	private void CreateIndex() throws CorruptIndexException, LockObtainFailedException, IOException {
		Analyzer analyzer = new StandardAnalyzer();
		boolean recreateIndexIfExists = true;
		IndexWriter indexWriter = new IndexWriter(INDEX_DIRECTORY, analyzer, recreateIndexIfExists);
		File dir = new File(DOCPATH);
		List<File> files = FileListing.getFileListing(dir);
		for (File file : files) {
			Document document = new Document();

			String path = file.getName();
			document.add(new Field(FIELD_PATH, path, Field.Store.YES, Field.Index.UN_TOKENIZED));

			Reader reader = new FileReader(file);
			document.add(new Field(FIELD_CONTENTS, reader,Field.TermVector.YES));

			indexWriter.addDocument(document);
		}
		indexWriter.optimize();
		indexWriter.close();
	}	

	// Function which takes query string as input and return set of links
	static public Map<String, String> answerQuery (String QueryString)
	{		
		IndexReader r;
		LinkedHashMap<String, String> top10results = new LinkedHashMap<String, String>();
		TreeMap<Double, String> orderedqueryresults = new TreeMap<Double, String>(); 

		try {
			r = IndexReader.open(INDEX_DIRECTORY);			
			IndexSearcher indexsearcher = new IndexSearcher(INDEX_DIRECTORY, true);

			QueryParser queryparser = new QueryParser(FIELD_CONTENTS, new StandardAnalyzer());
			Query query;
			try {				
				query = queryparser.parse(QueryString);
				System.out.println("Query is " + query.toString());
				Hits hits = indexsearcher.search(query);
				//System.out.println ("HITS size is " + hits.length());
				for(int i = 0; i < hits.length(); i++) {
					QueryResult qres = new QueryResult();
					Document doc = hits.doc(i);
					qres.score = (double) hits.score(i);
					// Has the Document IDs
					qres.URL = doc.get(FIELD_PATH);					
					// Get the actual URL					
					qres.URL = map.get(Integer.parseInt(qres.URL));			
					qres.score += hitsAlgo.hubScore(qres.URL);
					qres.score += hitsAlgo.authorityScore(qres.URL);
					//System.out.println ("URL : " + qres.URL +", Score: " + qres.score);					
					//System.out.println ("hubscore : " + hitsAlgo.hubScore(qres.URL) +", authscore: " + hitsAlgo.authorityScore(qres.URL));
					orderedqueryresults.put(qres.score, qres.URL);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Put the 10 results in the arraylist
		System.out.println("Top 10 results for this query are:");
		for (int i = 0; i < 10; i ++){			
			if (orderedqueryresults.lastEntry()!=null){
				top10results.put(orderedqueryresults.lastEntry().getValue(), "");
				System.out.println(orderedqueryresults.lastEntry().getKey());
				orderedqueryresults.remove(orderedqueryresults.lastKey());
			}
		}		

		return top10results;
	}	

	public static void main (String[] args){

		Lucene l = new Lucene();
		l.answerQuery ("Golf");
	}
}
