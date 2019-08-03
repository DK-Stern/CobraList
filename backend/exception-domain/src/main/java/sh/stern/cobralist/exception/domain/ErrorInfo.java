package sh.stern.cobralist.exception.domain;

public class ErrorInfo {
    private final String url;
    private final String error;

    public ErrorInfo(String url, Exception error) {
        this.url = url;
        this.error = error.getLocalizedMessage();
    }

    public String getUrl() {
        return url;
    }

    public String getError() {
        return error;
    }
}
