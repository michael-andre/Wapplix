package com.wapplix.data;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Michaël on 10/07/13.
 */
public class OAuth10Signer {

    private static final String OAUTH_PARAM_CONSUMER_KEY = "oauth_consumer_key";
    private static final String OAUTH_PARAM_NONCE = "oauth_none";
    private static final String OAUTH_PARAM_SIGNATURE = "oauth_signature";
    private static final String OAUTH_PARAM_SIGNATURE_METHOD = "oauth_signature_method";
    private static final String OAUTH_PARAM_TIMESTAMP = "oauth_timestamp";
    private static final String OAUTH_PARAM_TOKEN = "oauth_token";
    private static final String OAUTH_PARAM_VERSION = "oauth_version";

    private String mConsumerKey;
    private String mConsumerSecret;
    private String mToken;
    private String mTokenSecret;
    private Map<String, String> mOAuthParameters = new HashMap<String, String>();

    public OAuth10Signer(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        mConsumerKey = consumerKey;
        mConsumerSecret = consumerSecret;
        mToken = token;
        mTokenSecret = tokenSecret;
    }

    public void addOAuthParameter(String key, String value) {
        mOAuthParameters.put("oauth_" + key, value);
    }

    public void signRequest(Endpoint endpoint, HttpURLConnection connection) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

        // Prepare OAuth parameters
        Map<String, String> oAuthParams = new HashMap<String, String>(mOAuthParameters);
        oAuthParams.put(OAUTH_PARAM_CONSUMER_KEY, mConsumerKey);
        oAuthParams.put(OAUTH_PARAM_TOKEN, mToken);
        oAuthParams.put(OAUTH_PARAM_SIGNATURE_METHOD, "HMAC-SHA1");
        oAuthParams.put(OAUTH_PARAM_VERSION, "1.0");
        oAuthParams.put(OAUTH_PARAM_NONCE, UUID.randomUUID().toString().replaceAll("-", ""));
        oAuthParams.put(OAUTH_PARAM_TIMESTAMP, String.valueOf(new Date().getTime() / 1000));

        // Add signature
        String signatureBase = getSignatureBaseString(endpoint.getMethod(), endpoint.getUrlBase(), oAuthParams, endpoint.getQueryParameters(), endpoint.getPostValues());
        String signingKey = getSigningKey(mConsumerSecret, mTokenSecret);
        oAuthParams.put(OAUTH_PARAM_SIGNATURE, getSignature(signatureBase, signingKey));

        connection.setRequestProperty("Authorization", getAuthorizationHeader(oAuthParams));

    }

    private static String getSignatureBaseString(String httpMethod, String baseUrl, Map<String, String>... parametersMaps) throws UnsupportedEncodingException {

        // Percent-encode parameters and add to sorted map
        String paramsString = "";
        TreeMap<String, String> sortedParams = new TreeMap<String, String>();
        for(Map<String, String> params : parametersMaps) {
            for (Map.Entry<String, String> entry: params.entrySet()) {
                sortedParams.put(
                        URLEncoder.encode(entry.getKey(), "UTF-8"),
                        URLEncoder.encode(entry.getValue(), "UTF-8")
                );
            }
        }

        // Build parameters string from sorted map
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            paramsString += "&" + entry.getKey() + "=" + entry.getValue();
        }
        paramsString = paramsString.substring(1);

        // Build complete signature string
        return httpMethod.toUpperCase() + "&" + URLEncoder.encode(baseUrl, "UTF-8") + "&" + paramsString;
    }

    private static String getSigningKey(String consumerSecret, String tokenSecret) throws UnsupportedEncodingException {
        String key = URLEncoder.encode(consumerSecret, "UTF-8") + "&";
        // Token secret is optional in case of application-only authorization
        if (tokenSecret != null) key += URLEncoder.encode(tokenSecret, "UTF-8");
        return key;
    }

    private static String getSignature(String signatureBaseString, String signingKey) throws NoSuchAlgorithmException, InvalidKeyException {
        // Use SHA-1 encoder with the signing key
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(signingKey.getBytes(), mac.getAlgorithm());
        mac.init(secret);
        // Return Base64 encoded SHA-1 encoded base string
        byte[] digest = mac.doFinal(signatureBaseString.getBytes());
        return Base64.encodeToString(digest, Base64.DEFAULT);
    }

    private static String getAuthorizationHeader(Map<String, String> oAuthParameters) throws UnsupportedEncodingException {
        String header = "";
        for (Map.Entry<String, String> entry : oAuthParameters.entrySet()) {
            header += ", " + URLEncoder.encode(entry.getKey(), "UTF-8") + "=\"" + URLEncoder.encode(entry.getValue(), "UTF-8");
        }
        // remove leading ,
        header = header.substring(2);
        return "OAuth " + header;
    }

}
