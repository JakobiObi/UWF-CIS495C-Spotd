package models;
//hello
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import enums.AnimalStatus;
import enums.AnimalType;

/**
 * Note:  This class must have a public default constructor and public getters for each property
 * in order to work with Firestore.
 */
public class Pet implements Parcelable {

    private String name;
    private AnimalType animalType;
    private List<String> keywords;
    private AnimalStatus status;
    private String petID;
    private String ownerID;
    private String finderID;

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };
    private final String charset = "abcdefghijklmnopqrstuvwxyz123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final int idSize = 20;

    // default constructor is required
    public Pet() {
        this.petID = getUniqueID(idSize, charset);
    }


    // getters & setters

    public Pet(String name, AnimalType animalType, List<String> keywords, AnimalStatus status, String ownerID, String finderID) {
        this.name = name;
        this.animalType = animalType;
        this.keywords = new ArrayList<>(keywords);
        this.status = status;
        this.ownerID = ownerID;
        this.finderID = finderID;
        this.petID = getUniqueID(idSize, charset);
    }

    protected Pet(Parcel in) {
        name = in.readString();
        keywords = in.createStringArrayList();
        petID = in.readString();
        ownerID = in.readString();
        finderID = in.readString();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }
    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public List<String> getKeywords() {
        return keywords;
    }
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public AnimalStatus getStatus() {
        return status;
    }
    public void setStatus(AnimalStatus status) {
        this.status = status;
    }

    public String getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getPetID() {
        return petID;
    }
    public void setPetID(String petID) {
        this.petID = petID;
    }

    public String getFinderID() {
        return finderID;
    }

    public void setFinderID(String finderID) {
        this.finderID = finderID;
    }

    // Other stuff

    public Pet belongsTo(User user) {
        this.setOwnerID(user.getUserID());
        return this;
    }

    public void show() {
        StringBuilder logMsg = new StringBuilder("name: " + this.name
                + " " + "type: " + this.animalType.description()
                + " " + "status: " + this.status.description()
                + " " + "petID: " + this.petID
                + " " + "ownerID: " + this.ownerID
                + " " + "keywords:");

        if (keywords != null) {
            for (String keyword : keywords) {
                logMsg.append(" ").append(keyword);
            }
        }
        Log.d("Pet", logMsg.toString());
    }

    public static String getUniqueID(int size, String charset) {

        StringBuilder uniqueID = new StringBuilder();
        Random random = new Random();
        char randomChar;
        while (uniqueID.length() < size){
            randomChar = charset.charAt(random.nextInt(charset.length()));
            uniqueID.append(randomChar);
        }
        return uniqueID.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeStringList(keywords);
        parcel.writeString(petID);
        parcel.writeString(ownerID);
        parcel.writeString(finderID);
    }
}
