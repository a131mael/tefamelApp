package aaf.com.br.favodemelapp.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class JSONPPost {

	public static void sendJson(JSONObject json, String url) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(json.toString());

			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			httpClient.execute(request);
			// handle response here...
		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	public static String sendJson(String json, String url) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(json);

			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			httpClient.execute(request);
//			HttpResponse response = httpClient.execute(request);
//			int status = response.getStatusLine().getStatusCode();
//			if (status >= 200 && status < 300) {
//				HttpEntity entity = response.getEntity();
//				return EntityUtils.toString(entity);
//			} else {
//				throw new ClientProtocolException("Unexpected response status: " + status);
//			}

			// handle response here...
		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return url;
	}
	
	public static JSONObject postJson(String json, String url) {
		HttpClient httpClient = new DefaultHttpClient();
		JSONObject json2= null;
		try {
			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(json);

			request.addHeader("content-type", "application/json");
			request.setEntity(params);
		//	httpClient.execute(request);
			HttpResponse response = httpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				  json2 = new JSONObject(EntityUtils.toString(entity));

			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}

			// handle response here...
		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return json2;
	}

}