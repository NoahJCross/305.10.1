package com.example.a101;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryInterceptor implements Interceptor {

    // Maximum number of retries
    private int maxRetries;

    // Current retry count
    private int retryCount = 0;

    // Constructor to initialize maximum retries
    public RetryInterceptor(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Get the request from the chain
        Request request = chain.request();

        // Perform the initial request
        Response response = doRequest(chain, request);

        // Retry if the response is null and the maximum retry count has not been reached
        while (response == null && retryCount < maxRetries) {
            retryCount++;
            response = doRequest(chain, request);
        }

        // Throw an IOException if the response is still null after maximum retries
        if (response == null) {
            throw new IOException("Max retries reached: " + retryCount);
        }

        // Return the response
        return response;
    }

    // Method to perform the request
    private Response doRequest(Chain chain, Request request) {
        try {
            // Proceed with the request and return the response
            return chain.proceed(request);
        } catch (Exception e) {
            // Return null if an exception occurs
            return null;
        }
    }
}
