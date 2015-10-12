package com.smalt.smaltcheck.Entity;

/**
 * Created by yassinebouderbala on 10/10/2015.
 */
public class Alert {
    private Long id;
    private String url;
    private Integer errorCode;

    public Alert() {

    }

    public Alert(String url, Integer errorCode) {
        this.url = url;
        this.errorCode = errorCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
