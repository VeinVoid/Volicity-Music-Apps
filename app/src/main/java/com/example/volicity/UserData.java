package com.example.volicity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserData implements Parcelable {

    String Username;
    String Email;
    Uri ImageProfile;

    protected UserData(Parcel in) {
        Username = in.readString();
        Email = in.readString();
        ImageProfile = in.readParcelable(Uri.class.getClassLoader());
    }

    public UserData() {

    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Uri getImageProfile() {
        return ImageProfile;
    }

    public void setImageProfile(Uri imageProfile) {
        ImageProfile = imageProfile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(Username);
        dest.writeString(Email);
        dest.writeParcelable(ImageProfile, flags);
    }
}
