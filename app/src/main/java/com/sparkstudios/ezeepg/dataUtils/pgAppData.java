package com.sparkstudios.ezeepg.dataUtils;


import android.os.Parcel;
import android.os.Parcelable;


public class pgAppData implements Parcelable {

    private int pgPrice;
    private float pgRating;
    private String pgName;
    private String pgRoomType;
    private String pgId;
    private String pgImage;
    private String pgType;
    private String pgColor;
    private String pgAddress;

    public pgAppData() {
    }
    @Override
    public boolean equals(Object obj)
    { if (obj == this) { return true; }
        if (obj == null || obj.getClass() != this.getClass()) { return false; }
        pgAppData guest = (pgAppData) obj;
    return pgPrice == guest.pgPrice && (pgName.equals(guest.pgName) || (pgName != null && pgName.equals(guest.getPgName()))) && pgRating == guest.pgRating && (pgRoomType.equals(guest.pgRoomType) || (pgRoomType != null && pgRoomType.equals(guest.getPgRoomType())))&& (pgId.equals(guest.pgId) || (pgId != null && pgId.equals(guest.getPgId()))) && (pgImage.equals(guest.pgImage) || (pgImage!= null && pgImage.equals(guest.getPgImage()))) && (pgType.equals(guest.pgType) ||(pgType!=null&&pgType.equals(guest.getPgType())))&& (pgColor.equals(guest.pgColor) || (pgColor!= null && pgColor.equals(guest.getPgColor())))&&(pgAddress.equals(guest.pgAddress) ||(pgAddress!=null && pgAddress.equals(guest.getPgAddress())));
    }

    public String getPgAddress() {
        return pgAddress;
    }

    public pgAppData(int pgPrice, String pgImage, float pgRating, String pgName, String pgRoomType, String pgId, String pgType, String pgColor,String pgAddress) {
        this.pgPrice = pgPrice;
        this.pgImage = pgImage;
        this.pgRating = pgRating;
        this.pgName = pgName;
        this.pgRoomType = pgRoomType;
        this.pgId = pgId;
        this.pgColor = pgColor;
        this.pgType = pgType;
        this.pgAddress = pgAddress;
    }

    public String getPgColor() {
        return pgColor;
    }

    public void setPgType(String pgType) {
        this.pgType = pgType;
    }

    public String getPgType() {
        return pgType;

    }

    public int getPgPrice() {
        return pgPrice;
    }

    public String getPgImage() {
        return pgImage;
    }

    public float getPgRating() {
        return pgRating;
    }

    public String getPgId() {
        return pgId;
    }

    public String getPgName() {
        return pgName;
    }

    public String getPgRoomType() {
        return pgRoomType;
    }


    protected pgAppData(Parcel in) {
        pgPrice = in.readInt();
        pgRating = in.readFloat();
        pgName = in.readString();
        pgRoomType = in.readString();
        pgId = in.readString();
        pgImage = in.readString();
        pgType = in.readString();
        pgColor = in.readString();
        pgAddress = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pgPrice);
        dest.writeFloat(pgRating);
        dest.writeString(pgName);
        dest.writeString(pgRoomType);
        dest.writeString(pgId);
        dest.writeString(pgImage);
        dest.writeString(pgType);
        dest.writeString(pgColor);
        dest.writeString(pgAddress);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<pgAppData> CREATOR = new Parcelable.Creator<pgAppData>() {
        @Override
        public pgAppData createFromParcel(Parcel in) {
            return new pgAppData(in);
        }

        @Override
        public pgAppData[] newArray(int size) {
            return new pgAppData[size];
        }
    };



}


