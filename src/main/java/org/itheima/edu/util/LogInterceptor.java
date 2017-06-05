package org.itheima.edu.util;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogInterceptor implements Interceptor {
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();

		long t1 = System.nanoTime();

		System.out.println("======= Request =======");
		System.out.println(String.format("%s %s%n%s", request.url(),
				chain.connection(), request.headers()));

		Response response = chain.proceed(request);

		long t2 = System.nanoTime();
		System.out.println("======= Response =======");
		System.out.println(String.format("%s in %.1fms%n%s", response.request()
				.url(), (t2 - t1) / 1e6d, response.headers()));

		return response;
	}
}