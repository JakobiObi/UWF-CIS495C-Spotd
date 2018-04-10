package enums;

import java.util.ArrayList;
import java.util.Random;

/**
 * Different animal type categories.
 * <p>
 * This enum uses a trick to allow for referencing the string representation of the associated
 * item, simply using the syntax item.description(), for example:
 * AnimalType.Cat.description() would return the string "Cat"
 */

public enum AnimalType {

    Cat("Cat"),
    Dog("Dog"),
    Bird("Bird"),
    Rabbit("Rabbit"),
    Pig("Pig"),
    Snake("Snake"),
    Reptile("Reptile"),
    Other("Other");

    private final String description;

    AnimalType(String description) {
        this.description = description;
    }

    /**
     * Checks if a given string is a valid animal type, by checking if it matches
     * the string description of any type. Case of the input is ignored.
     *
     * @param type A string to compare.
     * @return A boolean, true if the given type is a valid AnimalType, otherwise false.
     */
    public static boolean isValidAnimalType(String type) {

        if (type == null || type.equals("")) {
            return false;
        }

        ArrayList<String> list = getDescriptionList();
        // adjust letter casing to be sentence cased
        type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();

        return list.contains(type);

    }

    /**
     * Gives an array of all the string descriptions of the animal types.  This is useful for
     * populating a drop-down selection box.
     *
     * @return A String array of all animal descriptions.
     */
    public static String[] getDescriptions() {
        return getDescriptionList().toArray(new String[0]);
    }

    private static ArrayList<String> getDescriptionList() {
        ArrayList<String> list = new ArrayList<>();
        for (AnimalType animal : AnimalType.values()) {
            list.add(animal.description());
        }
        return list;
    }

    public String description() {
        return this.description;
    }


    public AnimalType getRandom() {
        Random randItem = new Random();
        int numValues = AnimalType.values().length;
        return AnimalType.values()[randItem.nextInt(numValues)];

    }
}
