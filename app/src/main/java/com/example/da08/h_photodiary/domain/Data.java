package com.example.da08.h_photodiary.domain;

import android.net.Uri;

/**
 * Created by Da08 on 2017. 7. 6..
 */

public class Data {

    public String title;
    public String content;
    public long date;
    public String fileUriString;
    public Uri imgUri;

    public Data() {

    }

    public Data(String title,String content, long date){
        this.title = title;
        this.content = content;
        this.date = date;
    }
}
