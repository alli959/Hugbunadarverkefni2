
package search.generator;

import javafx.util.Pair;
import org.json.*;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class Request {


	private URL url = new URL("http://localhost:8080/database/");
	private String paramString;
	private HttpURLConnection con;

	private HttpURLConnection makeRequest() throws IOException {
		HttpURLConnection con;
		if(paramString != null) url = new URL(url.toString() + paramString);
		con = (HttpURLConnection) url.openConnection();
		// HERE WE DEBUG!!
		// System.out.println(url.toString());
		con.setRequestMethod("GET");
		return con;
	}

	public void convertToAsciiURL() {

	}

	public void addParameters(List<Pair<String, String>> parameters) throws IOException {
	    paramString = "?";
	    for(Pair<String, String> keyAndValue : parameters) {
	    	paramString += keyAndValue.getKey();
	    	paramString += "=";
	    	paramString += URLEncoder.encode(keyAndValue.getValue());
	    	paramString += "&";
		}
		//cut off last character
		paramString = paramString.substring(0, paramString.length() - 1);

	}

	private String extractJSONstringFromRequest() throws IOException {
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream())
		);
		String inputLine;
		StringBuffer responsetext = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			responsetext.append(inputLine);
		}
		in.close();
		return responsetext.toString();
	}

	private JSONArray getParsedJSONbyStringRequest() throws IOException {
		String json = extractJSONstringFromRequest();
		JSONArray obj;
		try {
			obj = new JSONArray(json);
		} catch (JSONException e) {
			return new JSONArray("[" + json + "]");
		}
		return obj;
	}

	private void initRequest(String databaseMethod) throws IOException {
		//paramString as null is allowed here
		url = new URL(url, databaseMethod);
	}

	public Request(String databaseMethod) throws IOException {
		initRequest(databaseMethod);
		con = makeRequest();
	}

	public static List<Pair<String, String>> itemAsList(Pair<String, String> p) {
		List<Pair<String, String>> pairList = new ArrayList<>();
		pairList.add(p);
		return pairList;
	}

	public Request(String databaseMethod, Pair<String, String> param) throws IOException {
		this(databaseMethod,  itemAsList(param));
	}

	public Request(String databaseMethod, List<Pair<String, String>> params) throws IOException {
		initRequest(databaseMethod);
		addParameters(params);
		con = makeRequest();
	}

	public JSONArray resolve() throws IOException {
		return getParsedJSONbyStringRequest();
	}
}
