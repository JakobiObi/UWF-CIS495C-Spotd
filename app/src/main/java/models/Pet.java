package models;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import enums.AnimalStatus;
import enums.AnimalType;

/**
 * Note:  This class must have a public default constructor and public getters for each property
 * in order to work with Firestore.
 */
public class Pet {

    private String name;            // the name of the animal, as provided by user
    private AnimalType animalType;  // type of animal (dog, cat, etc) enumerated type
    private List<String> keywords;      // a string array of keywords, as provided by the user
    private AnimalStatus status;    // lost/found.etc, enumerated type
    private String petID;           // the unique ID of this pet. Assigned when saved to db.
    private String ownerID;         // the unique ID of the pet's owner (if applicable)
    private String pictureUID;      // the unique ID of the picture associated with this animal


    // default constructor (required)
    public Pet() {
    }

    public Pet(String name, AnimalType animalType, List<String> keywords, AnimalStatus status, String petID, String ownerID, String pictureUID) {
        this.name = name;
        this.animalType = animalType;
        this.keywords = new ArrayList<>(keywords);
        this.petID = petID;
        this.status = status;
        this.ownerID = ownerID;
        this.pictureUID = pictureUID;
    }


    // Getters
    public String getName() {
        return name;
    }

    // Setters
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

    public String getPictureUID() {
        return pictureUID;
    }

    public void setPictureUID(String pictureUID) {
        this.pictureUID = pictureUID;
    }

    public String getPetID() {
        return petID;
    }

    public void setPetID(String petID) {
        this.petID = petID;
    }

    // Other stuff

    /**
     * Writes the information for this pet object to the debug log.
     */
    public void show() {

        StringBuilder logMsg = new StringBuilder("name: " + this.name
                + " " + "type: " + this.animalType.description()
                + " " + "status: " + this.status.description()
                + " " + "petID: " + this.petID
                + " " + "ownerID: " + this.ownerID
                + " " + "pictureID: " + this.pictureUID
                + " " + "keywords:");

        for (String keyword : keywords) {
            logMsg.append(" ").append(keyword);
        }

        Log.d("Pet", logMsg.toString());

    }
}
