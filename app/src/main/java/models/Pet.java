package models;


import enums.AnimalStatus;
import enums.AnimalType;

/**
 * Note:  This class must have a public default constructor and public getters for each property
 * in order to work with Firestore.
 */
public class Pet {

    private String name;            // the name of the animal, as provided by user
    private AnimalType animalType;  // type of animal (dog, cat, etc) enumerated type
    private String[] keywords;      // a string array of keywords, as provided by the user
    private AnimalStatus status;    // lost/found.etc, enumerated type
    private String ownerID;         // the unique id of the pet's owner (if applicable)
    private String pictureUID;      // the unique ID of the picture associated with this animal


    // default constructor (required)
    public Pet() {
    }

    public Pet(String name, AnimalType animalType, String[] keywords, AnimalStatus status, String ownerID, String pictureUID) {
        this.name = name;
        this.animalType = animalType;
        this.keywords = keywords;
        this.status = status;
        this.ownerID = ownerID;
        this.pictureUID = pictureUID;
    }


    // Getters
    public String getName() {
        return name;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public AnimalStatus getStatus() {
        return status;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getPictureUID() {
        return pictureUID;
    }


}
