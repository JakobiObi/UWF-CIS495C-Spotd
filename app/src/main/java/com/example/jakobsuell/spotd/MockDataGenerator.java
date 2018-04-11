package com.example.jakobsuell.spotd;

import java.util.ArrayList;
import java.util.List;

import enums.AnimalStatus;
import enums.AnimalType;
import models.Pet;
import models.User;

// This will setup and create a bunch of mock testing data.
public class MockDataGenerator {

    private static MockDataGenerator mockDataGenerator;

    private List<Pet> pets;
    private List<User> users;

    private MockDataGenerator() {
        makePetList();
    }

    public static MockDataGenerator make() {
        if (mockDataGenerator == null) {
            mockDataGenerator = new MockDataGenerator();
        }
        return mockDataGenerator;
    }
    private void makeUserList() {
        List<User> users = new ArrayList<>();
        users.add(new User("John Doe", "mrdoe@fake.com", 0,0));
        users.add(new User("Jane Doe", "mrsdoe@fake.com", 0,0));
        users.add(new User("Shaggy Rogers", "shaggy2dope@mysterymachine.com", 0,0));
        users.add(new User("Fred Jones", "sweet_ascot@mysterymachine.com", 0,0));
        users.add(new User("Daphne Blake", "whereRuScooby@mysterymachine.com", 0,0));
        users.add(new User("Velma Dinkley", "jinkies@mysterymachine.com", 0,0));

    }

    private void makePetList() {

        // note that normally you don't explicitly set the petID, but we already have photos made up
        List<String> keywords = new ArrayList<>();
        List<Pet> pets = new ArrayList<>();
        Pet pet;

        keywords.clear();
        keywords.add("orange");
        keywords.add("yellow");
        keywords.add("parrot");
        pet = new Pet("Tweety", AnimalType.Bird, keywords, AnimalStatus.Home, "mrsdoe@fake.com", null);
        pet.setPetID("BXzedITf2VGN8eEjar5o");
        pets.add(pet);

        keywords.clear();
        keywords.add("orange");
        keywords.add("white");
        keywords.add("cockatoo");
        pet = new Pet("Ginger", AnimalType.Bird, keywords, AnimalStatus.Lost, "mrsdoe@fake.com",null);
        pet.setPetID("wISynRaXI8nhXjZPR6JC");
        pets.add(pet);

        keywords.clear();
        keywords.add("orange");
        keywords.add("white");
        keywords.add("tabby");
        pet = new Pet("Tigger", AnimalType.Cat, keywords, AnimalStatus.Home, "mrdoe@fake.com",null);
        pet.setPetID("N7F7XxQVxGfVmSVHRj1e");
        pets.add(pet);

        keywords.clear();
        keywords.add("fat");
        keywords.add("young");
        keywords.add("pink");
        keywords.add("potbelly")
        pet = new Pet("Oinkers", AnimalType.Pig, keywords, AnimalStatus.Home, "mrdoe@fake.com",null);
        pet.setPetID("P2tOLF1X6r5QC9XaEMaB");
        pets.add(pet);

        keywords.clear();
        keywords.add("brown");
        keywords.add("black");
        keywords.add("white");
        keywords.add("tabby");
        keywords.add("kitten");
        pet = new Pet("", AnimalType.Cat, keywords, AnimalStatus.Found, null,"whereRuScooby@mysterymachine.com");
        pet.setPetID("ifgE6TAKSb8XaqEKUUlF");
        pets.add(pet);

        keywords.clear();
        keywords.add("brown");
        keywords.add("black");
        keywords.add("white feet");
        keywords.add("tabby");
        keywords.add("tricolor");
        pet = new Pet("Chairman Meow", AnimalType.Cat, keywords, AnimalStatus.Home, "jinkies@mysterymachine.com",null);
        pet.setPetID("7RglBQu9vbDwBPX6Km5Q");
        pets.add(pet);

        keywords.clear();
        keywords.add("brown");
        keywords.add("retriever");
        keywords.add("hungry");
        pet = new Pet("Scooby Doo", AnimalType.Dog, keywords, AnimalStatus.Lost, "whereRuScooby@mysterymachine.com",null);
        pet.setPetID("x8gFZNvjm9VNGXkccw7r");
        pets.add(pet);

        keywords.clear();
        keywords.add("brown");
        keywords.add("tan");
        keywords.add("white");
        keywords.add("boxer");
        keywords.add("red collar");
        pet = new Pet("Mr. Barkley", AnimalType.Dog, keywords, AnimalStatus.Lost, "jinkies@mysterymachine.com",null);
        pet.setPetID("cdxvjOxTzoUx1HoxobXY");
        pets.add(pet);

        keywords.clear();
        keywords.add("gray");
        keywords.add("white");
        keywords.add("longhair");
        keywords.add("mustache");
        keywords.add("tuxedo");
        pet = new Pet("Wyatt Earp", AnimalType.Cat, keywords, AnimalStatus.Home, "whereRuScooby@mysterymachine.com",null);
        pet.setPetID("2XlUFHpt7wNKflnfA2Ur");
        pets.add(pet);


    }


}
