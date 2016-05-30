package com.example.bkakran.nytimessearch.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bkakran on 5/29/16.
 */
public class Request implements Parcelable {
    String beginDate;
    String sortOrder;
    List<Boolean> newsDesk;

    enum SortOrder {
        OLDEST, NEWEST;
    }

    enum NewsDesk {
        ARTS, FASHION, SPORTS
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<Boolean> getNewsDesk() {
        return newsDesk;
    }

    public void setNewsDesk(List<Boolean> newsDesk) {
        this.newsDesk = newsDesk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.beginDate);
        dest.writeString(this.sortOrder);
        dest.writeList(this.newsDesk);
    }

    public Request() {
    }

    protected Request(Parcel in) {
        this.beginDate = in.readString();
        this.sortOrder = in.readString();
        this.newsDesk = new ArrayList<Boolean>();
        in.readList(this.newsDesk, Boolean.class.getClassLoader());
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel source) {
            return new Request(source);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (beginDate != null ? !beginDate.equals(request.beginDate) : request.beginDate != null)
            return false;
        if (sortOrder != null ? !sortOrder.equals(request.sortOrder) : request.sortOrder != null)
            return false;
        return newsDesk != null ? newsDesk.equals(request.newsDesk) : request.newsDesk == null;

    }

    @Override
    public int hashCode() {
        int result = beginDate != null ? beginDate.hashCode() : 0;
        result = 31 * result + (sortOrder != null ? sortOrder.hashCode() : 0);
        result = 31 * result + (newsDesk != null ? newsDesk.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Request{" +
                "beginDate='" + beginDate + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", newsDesk=" + newsDesk +
                '}';
    }
}


