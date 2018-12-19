package it.unical.asde.pr78.form;

public class ApiResponseForm {
    public final static String STATUS_OK = "ok";
    public final static String STATUS_ERROR = "error";

    private String status;
    private String message;
    private Object data;

    public ApiResponseForm() {
    }

    public ApiResponseForm(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
