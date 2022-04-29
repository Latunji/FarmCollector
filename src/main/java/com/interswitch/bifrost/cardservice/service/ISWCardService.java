package com.interswitch.bifrost.cardservice.service;

import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.concurrent.TimeUnit;

import com.interswitch.bifrost.cardservice.util.CustomPropertyNamingStrategy;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




public class ISWCardService {
    private static final Logger LOGGER = Logger.getLogger(ISWCardService.class.getName());
    
    public String htppPost(String requestUrl, String requestBody) throws IOException {
        LOGGER.log(Level.INFO, "NEW SERVICE CALL URL :{0}", requestUrl);
        String username = "mobile";
        String password = "M()b1L3Us3r";

        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes("UTF-8"));
        String authHeader = "Basic " + new String(encodedAuth);

        RequestBody body;
        ObjectMapper mapper = new ObjectMapper()
                .setPropertyNamingStrategy(new CustomPropertyNamingStrategy())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        LOGGER.log(Level.INFO, "Passing request to bank service");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        body = RequestBody.create(mediaType, requestBody);
        LOGGER.log(Level.INFO, "Request String: " + requestBody);

        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("Authorization", authHeader)
                .build();

        Response res;
        String value;
        try {
            res = client.newCall(request).execute();
            value = res.body().string();

            LOGGER.log(Level.INFO, "BANK RESPONSE: {0}",value);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "UNABLE TO REACH BANK SERVICE", e);
            throw new IOException("Your provipay request cannot be processed at this time. Please retry later", e);
        }

        return value;

    }
}
