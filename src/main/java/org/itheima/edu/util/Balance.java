package org.itheima.edu.util;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Balance {
//	private final static long DURATION = 3 * 60 * 1000;
	private final static long DURATION = 5 * 1000;
//	private final static String BASE_HOST = "http://172.16.0.82";
	private final static String BASE_HOST = "http://127.0.0.1";
	private final static String URL = BASE_HOST + ":26100";

	private static OkHttpClient client;
	private static long lastTime;
	private static String lastUrl;

	static {
		client = new OkHttpClient.Builder().addInterceptor(new LogInterceptor()).connectTimeout(25, TimeUnit.SECONDS)
				.readTimeout(25, TimeUnit.SECONDS).writeTimeout(25, TimeUnit.SECONDS).build();
	}

	public static String getBaseUrl() {
		long current = System.currentTimeMillis();
		if (current - lastTime < DURATION) {
			return lastUrl;
		}

		lastTime = current;
		lastUrl = getBalance();
		if (lastUrl == null) {
			lastTime = 0;
		}
		return lastUrl;
	}

	private static String getBalance() {
		Builder builder = new Builder();
		Request request = builder.url(URL).get().build();

		Call call = client.newCall(request);
		try {
			Response response = call.execute();
			if (response.isSuccessful()) {
				ResponseBody body = response.body();

				String strPort = body.string();

				Integer port = Integer.valueOf(strPort);
				if (port == 0) {
					return null;
				}
				return BASE_HOST + ":".concat(strPort);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
