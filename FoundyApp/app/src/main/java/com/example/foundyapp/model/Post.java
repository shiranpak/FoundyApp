package com.example.foundyapp.model;
import android.location.Address;
import android.location.Geocoder;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.foundyapp.MyApplication;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Entity(tableName = "posts")
public class Post implements Parcelable {
    final public static String COLLECTION_NAME = "posts";
    public final static String LAST_UPDATED = "lastUpdated";
    final static String POSTS_LAST_UPDATE = "POSTS_LAST_UPDATE";

    @PrimaryKey
    @NonNull
    private String postId;
    private String title;
    private String category;
    private LatLng location = null;
    private Long date = new Long(0);
    private String description;
    private boolean type; //true = found, false = lost
    private String userId;

    private boolean isDeleted = false;
    private String imageUrl;
    private Long lastUpdated = new Long(0);
//    @Ignore
    private String address = "";

    public Post(){}


    public Post(@NonNull String postid, String title, String category, LatLng location, Long date, String description, boolean type, String userId, boolean isDeleted , Long lastUpdated) {
        this.postId = postid;
        this.category = category;
        this.title = title;
        this.location = location;
        this.date = date;
        this.description = description;
        this.type = type;
        this.userId = userId;
        this.isDeleted = isDeleted;
        this.lastUpdated = lastUpdated;
    }

    protected Post(Parcel in) {
        postId = in.readString();
        title = in.readString();
        category = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
        if (in.readByte() == 0) {
            date = null;
        } else {
            date = in.readLong();
        }
        description = in.readString();
        type = in.readByte() != 0;
        userId = in.readString();
        isDeleted = in.readByte() != 0;
        imageUrl = in.readString();
        if (in.readByte() == 0) {
            lastUpdated = null;
        } else {
            lastUpdated = in.readLong();
        }
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @NonNull
    public String getPostId() {
        return postId;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    private void setAddressFromLocation(LatLng location) {
        Geocoder gcd = new Geocoder(MyApplication.getContext(), Locale.getDefault());
        try {
            List<Address> addressList = gcd.getFromLocation(
                    location.latitude, location.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);

                StringBuilder sb = new StringBuilder();
                sb.append(address.getThoroughfare()).append(", ");
                sb.append(address.getLocality()).append(", ");
                sb.append(address.getCountryName());
                this.address = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setPostId(@NonNull String postId) {
        this.postId = postId;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }


    public String getAddress() {
        return address;
    }

    public String getFormattedDate() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
        return df.format(this.getDate());
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("category",category);
        json.put("title",title);
        json.put("description",description);
        json.put("location",location);
        json.put("date",date);
        json.put("type",type);
        json.put("user",userId);
        json.put("isDeleted",isDeleted);
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        json.put("imageUrl",imageUrl);
        return json;
    }

    public static Post create(String postId, Map<String, Object> json) {
        String id = postId;
        String category = (String) json.get("category");
        String title = (String) json.get("title");
        String description = (String) json.get("description");
        String user = (String) json.get("user");
        LatLng location = new LatLng(0,0);
        HashMap<String, Double> data = (HashMap<String, Double>) json.get("location");
        if(data != null) {
            double latitude = data.get("latitude");
            double longitude = data.get("longitude");
            location = new LatLng(latitude, longitude);
        }
        Boolean isDeleted = (Boolean) json.get("isDeleted");
        Boolean type = (Boolean) json.get("type");
        Long date = (long)json.get("date");
        String imageUrl = (String)json.get("imageUrl");
        Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
        Long lastUpdated = new Long(ts.getSeconds());

        Post post = new Post(id, title,category,location,date,description,type,user,isDeleted,lastUpdated);
        post.setImageUrl(imageUrl);
        post.setAddressFromLocation(location);
        post.setIsDeleted(isDeleted);
        return post;
    }

    static Long getLocalLastUpdated(){
        Long localLastUpdate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong(POSTS_LAST_UPDATE,0);
        return localLastUpdate;
    }

    static void setLocalLastUpdated(Long date){
        SharedPreferences.Editor editor = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        editor.putLong(POSTS_LAST_UPDATE,date);
        editor.commit();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postId);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeParcelable(location, flags);
        if (date == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(date);
        }
        dest.writeString(description);
        dest.writeByte((byte) (type ? 1 : 0));
        dest.writeString(userId);
        dest.writeByte((byte) (isDeleted ? 1 : 0));
        dest.writeString(imageUrl);
        if (lastUpdated == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(lastUpdated);
        }
    }
}

