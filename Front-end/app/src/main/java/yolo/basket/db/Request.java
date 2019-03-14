
package yolo.basket.db;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.*;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Request {
    private URL url;

    {
        try {
            // Here you can switch from localhost to server
            url = new URL("http://138.68.155.75:8080/");
            // url = new URL("http://localhost:8080/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private String paramString;
    private HttpURLConnection con;

    private static String userName;
    private static String password;
    private static String protocol = "GET";

    public static void setUserName(String userName) {
        Request.userName = userName;
    }
    public static void setPassword(String password) {
        Request.password = password;
    }
    public static String getUserName() {return userName; }

    public static void setProtocol(String protocol) {
        Request.protocol = protocol;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private HttpURLConnection makeRequest() throws IllegalStateException, JSONException, IOException {
        HttpURLConnection con;
        if(paramString != null) {
            url = new URL(url.toString() + paramString);
        }
        con = (HttpURLConnection) url.openConnection();
        if (userName != null && password != null) {
            String userCredentials = userName + ":" + password;
            String basicAuth = null;
            basicAuth = "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes());
            con.setRequestProperty("Authorization", basicAuth);
        } else if (userName != null || password != null)
            throw new IllegalStateException("Username or password not found");

        // HERE WE DEBUG!!
        Log.d("160492", url.toString());
        con.setRequestMethod(protocol);
        return con;
    }

    public void addParameters(List<Param> parameters) throws JSONException {
        paramString = "?";
        for(Param param : parameters) {
            paramString += param.getKey();
            paramString += "=";
            paramString += URLEncoder.encode(param.getValue());
            paramString += "&";
        }
        //cut off last character
        paramString = paramString.substring(0, paramString.length() - 1);
    }

    private String extractJSONstringFromRequest() throws JSONException, IOException {
        BufferedReader in;
        boolean isError = false;
        try {
            in = new BufferedReader( new InputStreamReader(con.getInputStream()) );
        } catch (IOException e) {
            e.printStackTrace();
            // Print error stream
            return "Error";
        }
        String inputLine;
        StringBuffer responsetext = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            responsetext.append(inputLine);
        }
        in.close();
        if (isError) Log.d("160492 @ RequestError", "::" + responsetext.toString());
        return responsetext.toString();
    }

    private JSONArray getParsedJSONbyStringRequest() throws JSONException, IOException {
        String json = extractJSONstringFromRequest();
        JSONArray obj;
        try {
            obj = new JSONArray(json);
        } catch (JSONException e) {
            return new JSONArray("[" + json + "]");
        }
        return obj;
    }

    private void initRequest(String databaseMethod) throws JSONException, MalformedURLException {
        //paramString as null is allowed here
        url = new URL(url, databaseMethod);
    }

    public Request(String databaseMethod) throws JSONException, MalformedURLException, IOException {
        initRequest(databaseMethod);
        con = makeRequest();
    }

    public static List<Param> itemAsList(Param p) {
        List<Param> paramList = new ArrayList<>();
        paramList.add(p);
        return paramList;
    }

    public Request(String databaseMethod, Param param) throws JSONException, IOException {
        this(databaseMethod,  itemAsList(param));
    }

    public Request(String databaseMethod, List<Param> params) throws JSONException, IOException {
        initRequest(databaseMethod);
        addParameters(params);
        con = makeRequest();
    }

    public JSONArray resolve() throws JSONException, IOException {
        return getParsedJSONbyStringRequest();
    }
}
