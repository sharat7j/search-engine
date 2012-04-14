import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	public static final Integer NUMBER_OF_FILES=1500;
	static Integer count=0;
	static Map<Integer,String> docIdMap=new HashMap<Integer,String>();

	Pattern filters = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
			+ "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
			+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	/*
	 * You should implement this function to specify
	 * whether the given URL should be visited or not.
	 */



	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		if (filters.matcher(href).matches()) {
			return false;
		}
		if (href.startsWith("http://www.kostina-golf.com")) {
			return false;
		}

		if (href.startsWith("http://search.ask.")) {
			return false;
		}
		if (href.startsWith("http://www.google.")) {
			return false;
		}
		if (href.startsWith("http://gigablast.com/")) {
			return false;
		}
		if (href.startsWith("http://www.bing.")) {
			return false;
		}
		if (href.startsWith("http://search.yippy.com/")) {
			return false;
		}
		if (href.startsWith("http://search.yahoo.com")) {
			return false;
		}
		if (href.startsWith("http://search.aol.com/")) {
			return false;
		}
		if (href.startsWith("http://search.lycos.")) {
			return false;
		}
		return true;
	}

	/*
	 * This function is called when a page is fetched
	 * and ready to be processed by your program
	 */
	public void visit(Page page) {
//		int docid = page.getWebURL().getDocid();
//		String url = page.getWebURL().getURL();         
//		String text = page.getText();
//		List<WebURL> links = page.getURLs();   
		//System.out.println(url);
		//System.out.println("Index"+docid);
		//System.out.println("links"+links);
		//System.out.println(text);

		checkDocId(page);


	}

	private static synchronized boolean checkDocId(Page page) {
		String url = page.getWebURL().getURL();

		if(count>=NUMBER_OF_FILES){
			System.out.println("Limit Reached. Going to interrupt");
			System.out.println(docIdMap);
			File filename=new File("CrawlerOutput","HashMap.ser");
			FileOutputStream fos = null;
			ObjectOutputStream out = null;
			try
			{
				fos = new FileOutputStream(filename);
				out = new ObjectOutputStream(fos);
				out.writeObject(docIdMap);
				out.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}

			System.exit(0);
		}else if(url.startsWith("http://www.dmoz.org")){
			return false;
		}else{
			Integer docId=page.getWebURL().getDocid();
			File file=new File("CrawlerOutput",docId+"");

			PrintWriter writer=null;
			try {
				writer=new PrintWriter(file);
				writer.write(page.getText());
				docIdMap.put(docId, url);	
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(writer!=null){
					writer.close();
				}
			}


			File linkTextFile = new File("CrawlerOutput","Links.txt");

			writer=null;
			try {
				writer=new PrintWriter(new FileWriter(linkTextFile,true));
				//System.out.println("Page URLS:"+page.getURLs());

				for(WebURL webURL:page.getURLs()){
					String line=url+" -> "+webURL.getURL()+"\n";
					writer.write(line);
					//System.out.println(line);
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(writer!=null){
					writer.close();
				}
			}

			System.out.println("Completed Writing to file="+count+"-->"+url);		        


			count++;

		}
		return true;
	}
}
