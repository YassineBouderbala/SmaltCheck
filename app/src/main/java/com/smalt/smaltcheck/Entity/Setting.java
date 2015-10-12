package com.smalt.smaltcheck.Entity;

/**
 * Created by yassinebouderbala on 11/10/2015.
 */
public class Setting {
    private Long id;
    private String alert;
    private int timerefresh;

    public Setting(String alert, int timerefresh) {
        this.alert = alert;
        this.timerefresh = timerefresh;
    }

    public Setting() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public int getTimerefresh() {
        return timerefresh;
    }

    public void setTimerefresh(int timerefresh) {
        this.timerefresh = timerefresh;
    }
}
