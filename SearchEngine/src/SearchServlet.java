

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String DIRECTORY_PATH = "C:\\Users\\sharat\\Dropbox\\Project\\";

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query=request.getParameter("q");
		
		String docPath = DIRECTORY_PATH + "CrawlerOutput";
		Map<String,String> resultMap=Lucene.answerQuery (query);
		System.out.println(resultMap);
		
		writeToResponse(resultMap, response);
		
		
	}
	private void writeToResponse(Object object, HttpServletResponse response) {
		String json = "";

		if (object != null) {
			try {
				json = new Gson().toJson(object);
			} catch (IllegalArgumentException e) {
				System.out.println("Object Conversion to JSON failed.");
			}

			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
