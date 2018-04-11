package com.example.jakobsuell.spotd;

import java.util.List;

import models.Pet;
import models.User;

// This will setup and create a bunch of mock testing data.
public class MockDataGenerator {

    private static MockDataGenerator mockDataGenerator;

    private List<Pet> pets;
    private List<User> users;

    private MockDataGenerator() {
        //makePetList();
    }

    public static MockDataGenerator make() {
        if (mockDataGenerator == null) {
            mockDataGenerator = new MockDataGenerator();
        }
        return mockDataGenerator;
    }


/*    private void makePetList() {

        List<Pet> pets = new ArrayList<>();
        List<String> keywords = new ArrayList<>();
        keywords.add("orange");
        keywords.add("yellow");
        keywords.add("parrot");
        pets.add(new Pet("Tweety", AnimalType.Bird, keywords, AnimalStatus.Home, )

    }*/


}
