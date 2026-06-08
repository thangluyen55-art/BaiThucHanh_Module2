package Network;

import java.util.Map;

public class Request {
    public String action;
    public String keyword;
    public Map<String, String> params;

    public Request(String action, String keyword, Map<String, String> params) {
        this.action = action;
        this.keyword = keyword;
        this.params = params;
    }
}