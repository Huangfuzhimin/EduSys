package org.itheima.edu.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Connector {

	private static OkHttpClient client;

	static {
		client = new OkHttpClient.Builder().addInterceptor(new LogInterceptor()).connectTimeout(120, TimeUnit.SECONDS)
				.readTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).build();
	}

	private Connector() {
	}

	private static String BASE() {
		return Balance.getBaseUrl();
	}

	public static String load(String pkg, String root) {
		String url = BASE().concat("/pkg/load");
		Builder builder = new Builder();

		try {
			root = URLEncoder.encode(root, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		FormBody b = new FormBody.Builder().add("pkg", pkg).build();
		Request request = builder.url(url).addHeader("root", root).post(b).build();

		Call call = client.newCall(request);
		try {
			Response response = call.execute();
			if (response.isSuccessful()) {
				ResponseBody body = response.body();
				return body.string();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String run(String root, Map<String, String> map) {
		String datas = new Gson().toJson(map);

		String url = BASE().concat("/pkg/run");

		FormBody b = new FormBody.Builder().add("datas", datas).build();

		try {
			root = URLEncoder.encode(root, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Builder builder = new Builder();
		Request request = builder.url(url).addHeader("root", root).post(b).build();

		Call call = client.newCall(request);
		try {
			Response response = call.execute();
			if (response.isSuccessful()) {
				ResponseBody body = response.body();
				return body.string();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String submit(String root, Map<String, String> map) {
		String datas = new Gson().toJson(map);

		String url = BASE().concat("/pkg/submit");
		FormBody b = new FormBody.Builder().add("datas", datas).build();

		try {
			root = URLEncoder.encode(root, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Builder builder = new Builder();
		Request request = builder.url(url).addHeader("root", root).post(b).build();

		Call call = client.newCall(request);
		try {
			Response response = call.execute();
			if (response.isSuccessful()) {
				ResponseBody body = response.body();
				return body.string();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
