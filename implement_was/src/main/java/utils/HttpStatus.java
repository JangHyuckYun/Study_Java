package utils;

public enum HttpStatus {
    OK(200),
    NULL(404);

    int status;

    private HttpStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
