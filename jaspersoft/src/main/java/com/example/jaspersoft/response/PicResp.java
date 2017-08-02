package com.example.jaspersoft.response;

import java.io.Serializable;

/**
 * Created by martin on 2017/8/2.
 */
public class PicResp implements Serializable {

    private static final long serialVersionUID = -4878627448672112277L;

    private String title;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
