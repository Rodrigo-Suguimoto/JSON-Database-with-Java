package shared;

import com.google.gson.Gson;

public class Request {
    private String requestType;
    private String key;
    private String value;

    public Request(String requestType, String key, String value) {
        this.requestType = requestType;
        this.key = key;
        this.value = value;
    }

    public Request(String requestType, String key) {
        this(requestType, key, "");
    }

    public Request(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static String serializeToGson(Request request) {
        return new Gson().toJson(request);
    }
}