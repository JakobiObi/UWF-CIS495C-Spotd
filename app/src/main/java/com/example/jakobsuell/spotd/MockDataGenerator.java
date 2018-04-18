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


public class MockDataGenerator {

    private final String TAG = "MockDataGenerator";
    public ArrayList<Pet> pets;
    public ArrayList<User> users;

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
        pets = new ArrayList<>();

        pets.add(createPet(
                "BXzedITf2VGN8eEjar5o",
                "Tweety",
                AnimalType.Bird,
                AnimalStatus.Home,
                "mrsdoe@fake.com",
                null,
                new String[]{"orange","yellow","parrot"}));

        pets.add(createPet(
                "wISynRaXI8nhXjZPR6JC",
                "Ginger",
                AnimalType.Bird,
                AnimalStatus.Lost,
                "mrsdoe@fake.com",
                null,
                new String[]{"orange","white","cockatoo"}));

        pets.add(createPet(
                "N7F7XxQVxGfVmSVHRj1e",
                "Tigger",
                AnimalType.Cat,
                AnimalStatus.Home,
                "mrdoe@fake.com",
                null,
                new String[]{"orange","white","tabby"}));

        pets.add(createPet(
                "pt97zaK1do7plnIooq4E",
                "Oinkers",
                AnimalType.Pig,
                AnimalStatus.Home,
                "mrdoe@fake.com",
                null,
                new String[]{"fat","young","pink", "potbelly"}));

        pets.add(createPet(
                "ifgE6TAKSb8XaqEKUUlF",
                "",
                AnimalType.Cat,
                AnimalStatus.Found,
                null,
                "whereRuScooby@mysterymachine.com",
                new String[]{"brown","black","white", "tabby", "kitten"}));

        pets.add(createPet(
                "7RglBQu9vbDwBPX6Km5Q",
                "Chairman Meow",
                AnimalType.Cat,
                AnimalStatus.Home,
                "jinkies@mysterymachine.com",
                null,
                new String[]{"brown","black","white feet", "tabby", "tricolor"}));

        pets.add(createPet(
                "x8gFZNvjm9VNGXkccw7r",
                "Scooby Doo",
                AnimalType.Dog,
                AnimalStatus.Lost,
                "whereRuScooby@mysterymachine.com",
                null,
                new String[]{"brown","retriever","hungry"}));

        pets.add(createPet(
                "cdxvjOxTzoUx1HoxobXY",
                "Mr. Barkley",
                AnimalType.Dog,
                AnimalStatus.Lost,
                "jinkies@mysterymachine.com",
                null,
                new String[]{"brown","tan","white", "boxer", "red collar"}));

        pets.add(createPet(
                "2XlUFHpt7wNKflnfA2Ur",
                "Wyatt Earp",
                AnimalType.Cat,
                AnimalStatus.Home,
                "whereRuScooby@mysterymachine.com",
                null,
                new String[]{"gray","white","longhair", "mustache", "tuxedo"}));

        pets.add(createPet(
                "Yeb9AtnyClAnhdTjGMKo",
                "",
                AnimalType.Dog,
                AnimalStatus.Found,
                null,
                "shaggy2dope@mysterymachine.com",
                new String[]{"reddish","curly"}));

        pets.add(createPet(
                "P2tOLF1X6r5QC9XaEMaB",
                "",
                AnimalType.Dog,
                AnimalStatus.Found,
                null,
                "mrdoe@fake.com",
                new String[]{"labrador","yellow"}));

        pets.add(createPet(
                "5pRSo5boJ3ASYugYYfg3",
                "Giggles",
                AnimalType.Other,
                AnimalStatus.Lost,
                "sweet_ascot@fake.com",
                null,
                new String[]{"white","brown","black","guinea pig"}));

        pets.add(createPet(
                "nAGFTcX3t7Pw1OYhcIbw",
                "Thumper",
                AnimalType.Rabbit,
                AnimalStatus.Lost,
                "sweet_ascot@fake.com",
                null,
                new String[]{"grey","white","netherland dwarf"}));

        pets.add(createPet(
                "LFbm894gKzXNOVMcEwWb",
                "Rabbit De Niro",
                AnimalType.Rabbit,
                AnimalStatus.Home,
                "sweet_ascot@fake.com",
                null,
                new String[]{"white","chocolate","brown","english lop"}));

        pets.add(createPet(
                "UJKNSTDgdPWg7WA5PieM",
                "",
                AnimalType.Rabbit,
                AnimalStatus.Home,
                null,
                "mrsdoe@fake.com",
                new String[]{"sand","white","rex"}));
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

    private Pet createPet(String petID, String name, AnimalType type, AnimalStatus status, String ownerID, String finderID, String[] keywords) {
        List<String> words = new ArrayList<>();
        for (String word : keywords) {
            words.add(word);
        }
        Pet newPet = new Pet(name, type, words, status, ownerID, finderID);
        if (petID != null) {
            newPet.setPetID(petID);
        }
        return newPet;
    }

}
