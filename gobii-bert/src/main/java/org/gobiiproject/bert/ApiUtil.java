package org.gobiiproject.bert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ApiUtil {

	private HttpURLConnection connect(String method, String urlString, Map<String, String> params) throws IOException {

		URL url = new URL(urlString);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod(method);

		for (Map.Entry<String, String> param : params.entrySet()) {
			connection.setRequestProperty(param.getKey(), param.getValue());
		}

		return connection;
	}

	private static Map<String, Object> makeParamMap(Object ... params) {

		if (! (params.length % 2 == 0)) {
			throw new RuntimeException("Params must be an even number");
		}

		Map<String, Object> paramMap = new HashMap<>();

		for (int i = 0 ; i < params.length ; i+=2) {
			paramMap.put(params[i].toString(), params[i+1]);
		}

		return paramMap;
	}

	public static <T> ResponseEntity<T> get(String url, Class<T> as, Object ... params) {

		return new RestTemplate().getForEntity(url, as, makeParamMap(params));
	}

	public static <T> ResponseEntity<T> post(String url, Object object, Class<T> as, Object ... params) {

		return new RestTemplate().postForEntity(url, object, as, makeParamMap(params));
	}

}
