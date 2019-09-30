package com.psyberia.cardviewer.banner;

/**
 * Created by combo on 11/13/2017.
 *
 */

public class Banner {
    private String url =
            "https://www.google.com";
    private String message = "Тестовое сообщение";
    private String thumbnail = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public static boolean isEmpty(Banner data) {
        return data == null || data.getUrl().isEmpty();
    }
}
