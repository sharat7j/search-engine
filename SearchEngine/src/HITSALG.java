


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Kleinberg's hypertext-induced topic selection 
 * <a href="http://citeseer.ist.psu.edu/kleinberg99authoritative.html">(HITS)</a> algorithm 
 * is a very popular and effective algorithm to rank documents based on
 * the link information among a set of documents. The algorithm presumes
 * that a good hub is a document that points to many others, and a good 
 * authority is a document that many documents point to. 
 * 
 * Hubs and authorities exhibit a mutually reinforcing relationship: a better hub
 * points to many good authorities, and a better authority is pointed to by many good hubs.
 * 
 * Because the HITS algorithm ranks documents only depending on the 
 * in-degree and out-degree of links, it will cause problems in some cases. For 
 * example, <a href="http://citeseer.ist.psu.edu/bharat98improved.html">
 * Improved Algorithms for Topic Distillation in a Hyperlinked Environment</a> presents
 * two problems: mutually reinforcing relationships between hosts and topic drift. Both 
 * can be solved or alleviated by adding weights to documents. The first problem can 
 * be solved by giving the documents from the same host much less weight, and
 * the second problem can be alleviated by adding weights to edges based on text 
 * in the documents or their anchors.
 *
 * @author Bruno Martins
 *
 */
public class HITSALG {

	/** The data structure containing the Web linkage graph */
	private WebGraph graph;

	/** A <code>Map</code> containing the Hub score for each page */
	private Map hubScores;
	
	/** A <code>Map</code> containing the Authority score for each page */
	private Map authorityScores;
	
	/** 
	 * Constructor for HITS
	 * 
	 * @param graph The data structure containing the Web linkage graph
	 */
	public HITSALG ( WebGraph graph ) {
		this.graph = graph;
		this.hubScores = new HashMap();
		this.authorityScores = new HashMap();
		int numLinks = graph.numNodes();
		for(int i=0; i<=numLinks; i++) { 
			hubScores.put(new Integer(i),new Double(1));
			authorityScores.put(new Integer(i),new Double(1));
		}
		computeHITS();
	}

	/**
	 * Computes the Hub and Authority scores for all the nodes in the Web Graph.
	 * In this method, the maximum number of iterations of the algorithm is set to 25
	 */
	public void computeHITS() {
		computeHITS(25);
	}
	
	/**
	 * Computes the Hub and Authority scores for all the nodes in the Web Graph.
	 * 
	 * Given a Web graph, an iterative calculation is performed on the value of 
	 * authority and value of hub. For each page p, the authority value of page p is 
	 * the sum of hub scores of all the pages that points to p, the hub value of 
	 * page p is the sum of authority scores of all the pages that p points to.
	 * 
	 * Iteration proceeded on the neighborhood graph until the values converged. 
	 * 
     * @param iter The maximum number of iterations for the algorithm
     */
	public void computeHITS(int numIterations) {
		boolean change = true;
		while(numIterations-->0 && change) {
			change = false;
			double  normHub =0, normAuth=0;
			for (int i = 0; i < graph.numNodes(); i++) {
				Map inlinks    = graph.inLinks(new Integer(i));
				Map outlinks  = graph.outLinks(new Integer(i));
				Iterator inIter = inlinks.keySet().iterator();
				Iterator outIter = outlinks.keySet().iterator();
				double authorityScore = 0;
				double hubScore = 0;
				while (inIter.hasNext()) {
					authorityScore += ((Double)(hubScores.get((Integer)(inIter.next())))).doubleValue();
				}
				normAuth += (authorityScore*authorityScore);
				while (outIter.hasNext()) {
					hubScore += ((Double)(authorityScores.get((Integer)(outIter.next())))).doubleValue();
				}
				normHub += (hubScore*hubScore);
				Double authorityScore2 = (Double)(authorityScores.get(new Integer(i)));
				Double hubScore2 = (Double)(hubScores.get(new Integer(i)));
				if(authorityScore2.doubleValue()!=authorityScore) {
					change = true;
					authorityScores.put(new Integer(i),new Double(authorityScore));
				}
				if(hubScore2.doubleValue()!=hubScore) {
					change = true;
					hubScores.put(new Integer(i),new Double(hubScore));
				}
			}
			//Normalize the hub and authority scores
			normAuth = Math.sqrt(normAuth);
			normHub = Math.sqrt(normHub);
			if(change)
			for(int j=0;j<graph.numNodes();j++){
				Double auth = (Double) authorityScores.get((Integer)(j));
				Double hub = (Double) hubScores.get((Integer)(j));
				//Put updated scores
				if(normAuth!=0)
				authorityScores.put(new Integer(j),new Double(auth/normAuth));
				if(normHub!=0)
				hubScores.put(new Integer(j),new Double(hub/normHub));
				
			}
			
		}
	}
	
	/**
	 * Returns the Hub score associated with a given link
	 * 
	 * @param link The url for the link
	 * @return The Hub score associated with the given link
	 */
	public Double hubScore(String link) {
		return hubScore(graph.URLToIdentifyer(link));	
	}
	
	/**
	 * Returns the Hub score value associated with a given link identifyer.
	 * Identifyers are Integer numberes, used in <code>WebGraph</code> to
	 * represent the Web graph for efficiency reasons.
	 * 
	 * @param link An identifyer for the link
	 * @return The Hub score associated with the given link
	 * @see WebGraph.IdentifyerToURL()
	 */
	private Double hubScore(Integer id) {
		double d=0;
				
		try{
			d = (Double)(hubScores.get(id));
		}catch(NullPointerException e){
			
		}
		return d;
	}	

	/**
	 * Initializes the Hub score associated with a given link.
	 * 
	 * @param link The url for the link
	 * @param value The Hub score to assign
	 */
	public void initializeHubScore(String link, double value) {
		Integer id = graph.URLToIdentifyer(link);
		if(id!=null) hubScores.put(id,new Double(value));	
	}
	
	/**
	 * Initializes Hub score associated with a given link identifyer.
	 * Identifyers are Integer numberes, used in <code>WebGraph</code> to
	 * represent the Web graph for efficiency reasons.
	 * 
	 * @param link An identifyer for the link
	 * @param value The Hub score to assign 
	 * @see WebGraph.IdentifyerToURL()
	 */
	public void initializeHubScore(Integer id, double value) {
		if(id!=null) hubScores.put(id,new Double(value));	
	}

	/**
	 * Returns the Authority score associated with a given link
	 * 
	 * @param link The url for the link
	 * @return The Authority score associated with the given link
	 */
	public Double authorityScore(String link) {
		double d=0;
		try{
			d = authorityScore(graph.URLToIdentifyer(link));
			
		}catch (NullPointerException e) {
			
		}
		
		return d; 	
	}
	
	/**
	 * Returns the Authority score value associated with a given link identifyer.
	 * Identifyers are Integer numberes, used in <code>WebGraph</code> to
	 * represent the Web graph for efficiency reasons.
	 * 
	 * @param link An identifyer for the link
	 * @return The Authority score associated with the given link
	 * @see WebGraph.IdentifyerToURL()
	 */
	private Double authorityScore(Integer id) {
		return (Double)(authorityScores.get(id));
	}

	/**
	 * Initializes the Authority score associated with a given link.
	 * 
	 * @param link The url for the link
	 * @param value The Authority score to assign
	 */
	public void initializeAuthorityScore(String link, double value) {
		Integer id = graph.URLToIdentifyer(link);
		if(id!=null) authorityScores.put(id,new Double(value));	
	}
	
	/**
	 * Initializes Authority score associated with a given link identifyer.
	 * Identifyers are Integer numberes, used in <code>WebGraph</code> to
	 * represent the Web graph for efficiency reasons.
	 * 
	 * @param link An identifyer for the link
	 * @param value The Authority score to assign 
	 * @see WebGraph.IdentifyerToURL()
	 */
	public void initializeAuthorityScore(Integer id, double value) {
		if(id!=null) authorityScores.put(id,new Double(value));	
	}

}
