package com.example.bkakran.nytimessearch.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by bkakran on 5/28/16.
 */
public class Article implements Parcelable {
    String webUrl;
    String headLine;
    String thumbNail;

    public Article(JSONObject jsonObject) throws JSONException {
        this.webUrl = jsonObject.getString("web_url");
        this.headLine = jsonObject.getJSONObject("headline").getString("main");
        JSONArray multiMedia = jsonObject.getJSONArray("multimedia");
        if (multiMedia.length() > 0) {
            JSONObject multiMediaJson = multiMedia.getJSONObject(0);
            this.thumbNail = "http://www.nytimes.com/" + multiMediaJson.getString("url");
        } else {
            this.thumbNail = "";
        }
    }

    public static ArrayList<Article> fromJSonArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Article> results = new ArrayList<>();
        for (int x = 0; x < jsonArray.length(); x++) {
            results.add(new Article(jsonArray.getJSONObject(x)));
        }
        return results;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeString(this.headLine);
        dest.writeString(this.thumbNail);
    }

    protected Article(Parcel in) {
        this.webUrl = in.readString();
        this.headLine = in.readString();
        this.thumbNail = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
