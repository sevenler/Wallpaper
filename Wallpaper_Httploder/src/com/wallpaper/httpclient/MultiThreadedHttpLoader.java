package com.wallpaper.httpclient;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class MultiThreadedHttpLoader {

	HttpClient httpclient;

	public MultiThreadedHttpLoader() {
		super();
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setMaxTotal(3);
		this.httpclient = new DefaultHttpClient(cm, null);
	}

	public InputStream execute(String url) throws InterruptedException {
		HttpGet httpget = new HttpGet(url);
		InputStream result = null;
		GetThread thread = new GetThread(httpclient, httpget, result);
		thread.start();
		thread.join();
		return result;
	}

	public InputStream executeBlocking(String url) throws InterruptedException {
		HttpGet httpget = new HttpGet(url);
		CountDownLatch latch = new CountDownLatch(1);
		InputStream result = null;
		GetThread thread = new GetThread(httpclient, httpget, result, latch);
		thread.start();
		thread.join();
		latch.await();
		return result;
	}

	public void shutdown() {
		httpclient.getConnectionManager().shutdown();
	}

	static class GetThread extends Thread {

		private final HttpClient httpClient;
		private final HttpContext context;
		private final HttpGet httpget;
		private InputStream output;

		public InputStream getOutput() {
			return output;
		}

		private final CountDownLatch latch;

		public GetThread(HttpClient httpClient, HttpGet httpget, InputStream output, CountDownLatch latch) {
			this.httpClient = httpClient;
			this.context = new BasicHttpContext();
			this.httpget = httpget;
			this.output = output;
			this.latch = latch;
		}

		public GetThread(HttpClient httpClient, HttpGet httpget, InputStream output) {
			this.httpClient = httpClient;
			this.context = new BasicHttpContext();
			this.httpget = httpget;
			this.output = output;
			this.latch = null;
		}

		@Override
		public void run() {
			System.out.println(" - about to get something from " + httpget.getURI());
			try {
				HttpResponse response = httpClient.execute(httpget, context);
				System.out.println(" - get executed");
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
				output = bufHttpEntity.getContent();
				if (latch != null)
					latch.countDown();
			} catch (Exception e) {
				httpget.abort();
				System.out.println(" - error: " + e);
			}
		}
	}

}
