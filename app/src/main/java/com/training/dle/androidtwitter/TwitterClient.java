package com.training.dle.androidtwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.util.Log;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "6tC4mTmVQlSdqedCrJZZBAr11";       // Change this
	public static final String REST_CONSUMER_SECRET = "PhwDtXRSfmpzkxZuyytabe7aLAsJzCD4ScuWASjrMRBXWKA0t9"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://androidtwitter"; // Change this (here and in manifest)
	public static final long MAX_ID = 999998267484508160L;
	public static final int NUMBER_OF_ROWS = 25;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		getHomeTimeline(MAX_ID, handler);
	}

	public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("max_id", String.valueOf(maxId));
		params.put("count", NUMBER_OF_ROWS);
		client.get(apiUrl, params, handler);
	}

	public void tweet(String body, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");

		RequestParams params = new RequestParams();
		params.put("status", body);
		client.post(apiUrl, params, handler);
	}

	public void getUserTimeline(long maxId, String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("max_id", String.valueOf(maxId));
		params.put("screen_name", screenName);
		params.put("count", NUMBER_OF_ROWS);
		client.get(apiUrl, params, handler);
	}

	public void getMentionsTimeline(long maxId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("max_id", String.valueOf(maxId));
		params.put("count", NUMBER_OF_ROWS);
		client.get(apiUrl, params, handler);
	}

	public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = null;
		if (screenName == null){
			apiUrl = getApiUrl("account/verify_credentials.json");
		}else {
			apiUrl = getApiUrl("users/show.json");
		}
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		client.get(apiUrl, params, handler);
	}

}