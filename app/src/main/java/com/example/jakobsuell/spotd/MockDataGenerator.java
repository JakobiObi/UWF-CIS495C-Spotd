package com.example.jakobsuell.spotd;

import java.util.List;
import java.util.Random;

import enums.AnimalType;
import models.Pet;
import models.User;

public class MockDataGenerator {

    private static MockDataGenerator mockDataGenerator;

    private List<String> petNames;
    private List<String> petPictures;
    private List<Pet> randomPets;
    private List<User> randomUsers;


    private MockDataGenerator() {


    }

    public static MockDataGenerator make() {
        if (mockDataGenerator == null) {
            mockDataGenerator = new MockDataGenerator();
        }
        return mockDataGenerator;
    }

    public Pet makeRandom() {

        Pet randomPet = new Pet();
        randomPet = giveRandomInfo(randomPet).belongsTo(aRandomUser());
        return randomPet;
    }

    private void initializeNameList() {
        petNames.add("Fluffy");
        petNames.add("Samson");
        petNames.add("Scruff");
        petNames.add("Shredder");
        petNames.add("Bella");
        petNames.add("Zoey");
        petNames.add("Spot");
        petNames.add("Tiger");
        petNames.add("Garfield");
        petNames.add("Odie");
    }

    private void initializePictureList() {
        // list of various pictures
    }

    private String getRandomName() {

        Random randName = new Random();
        return petNames.get(randName.nextInt(petNames.size()));
    }

    private User aRandomUser() {
        Random randomUser = new Random();
        return randomUsers.get(randomUser.nextInt(randomUsers.size()));
    }

    private Pet giveRandomInfo(Pet pet) {
        pet.setName(mockDataGenerator.getRandomName());
        pet.setAnimalType(AnimalType.getRandom());
        return pet;
    }
}
