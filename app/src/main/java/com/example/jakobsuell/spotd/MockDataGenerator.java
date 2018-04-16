package com.example.jakobsuell.spotd;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import controllers.FirestoreController;
import enums.AnimalStatus;
import enums.AnimalType;
import models.Pet;
import models.User;

// This will setup and create a bunch of mock testing data.
public class MockDataGenerator {

    private final String TAG = "MockDataGenerator";
    public List<Pet> pets;
    public List<User> users;

    public static MockDataGenerator make() {
        return new MockDataGenerator();
    }

    private MockDataGenerator() {
        makeUserList();
        makePetList();
    }

    private void makeUserList() {
        users = new ArrayList<>();
        users.add(new User("John Doe", "mrdoe@fake.com", 0,0));
        users.add(new User("Jane Doe", "mrsdoe@fake.com", 0,0));
        users.add(new User("Shaggy Rogers", "shaggy2dope@mysterymachine.com", 0,0));
        users.add(new User("Fred Jones", "sweet_ascot@mysterymachine.com", 0,0));
        users.add(new User("Daphne Blake", "whereRuScooby@mysterymachine.com", 0,0));
        users.add(new User("Velma Dinkley", "jinkies@mysterymachine.com", 0,0));
    }

    private void makePetList() {

        List<String> keywords = new ArrayList<>();
        pets = new ArrayList<>();
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
        keywords.add("potbelly");
        pet = new Pet("Oinkers", AnimalType.Pig, keywords, AnimalStatus.Home, "mrdoe@fake.com",null);
        pet.setPetID("pt97zaK1do7plnIooq4E");
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

        keywords.clear();
        keywords.add("reddish");
        keywords.add("curly");
        pet = new Pet("", AnimalType.Dog, keywords, AnimalStatus.Found, null,"shaggy2dope@mysterymachine.com");
        pet.setPetID("Yeb9AtnyClAnhdTjGMKo");
        pets.add(pet);

        keywords.clear();
        keywords.add("labrador");
        keywords.add("yellow");
        pet = new Pet("", AnimalType.Dog, keywords, AnimalStatus.Found, null,"mrdoe@fake.com");
        pet.setPetID("P2tOLF1X6r5QC9XaEMaB");
        pets.add(pet);

        keywords.clear();
        keywords.add("white");
        keywords.add("brown");
        keywords.add("black");
        keywords.add("guinea pig");
        pet = new Pet("Giggles", AnimalType.Other, keywords, AnimalStatus.Lost, "sweet_ascot@fake.com",null);
        pet.setPetID("5pRSo5boJ3ASYugYYfg3");
        pets.add(pet);

        keywords.clear();
        keywords.add("grey");
        keywords.add("white");
        keywords.add("netherland dwarf");
        pet = new Pet("Thumper", AnimalType.Rabbit, keywords, AnimalStatus.Lost, "sweet_ascot@fake.com",null);
        pet.setPetID("nAGFTcX3t7Pw1OYhcIbw");
        pets.add(pet);

        keywords.clear();
        keywords.add("white");
        keywords.add("chocolate");
        keywords.add("brown");
        keywords.add("english lop");
        pet = new Pet("Rabbit De Niro", AnimalType.Rabbit, keywords, AnimalStatus.Home, "sweet_ascot@fake.com",null);
        pet.setPetID("LFbm894gKzXNOVMcEwWb");
        pets.add(pet);

        keywords.clear();
        keywords.add("sand");
        keywords.add("white");
        keywords.add("rex");
        pet = new Pet("", AnimalType.Rabbit, keywords, AnimalStatus.Found, null,"mrsdoe@fake.com");
        pet.setPetID("UJKNSTDgdPWg7WA5PieM");
        pets.add(pet);
    }

    public void saveData() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        saveUsers(firestore);
        savePets(firestore);
    }

    private void saveUsers(FirebaseFirestore firestore) {
        FirestoreController.saveUser(firestore, users).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "mock users written to Firestore");
            }
        });
    }

    private void savePets(FirebaseFirestore firestore) {
        FirestoreController.savePet(firestore, pets).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "mock pets written to Firestore");
            }
        });
    }

}
