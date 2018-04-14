package enums;

import java.util.ArrayList;
import java.util.Random;


/**
 * Different animal statuses.
 * <p>
 * This enum uses a trick to allow for referencing the string representation of the associated
 * item, simply using the syntax item.description(), for example:
 * AnimalType.Cat.description() would return the string "Cat"
 */
public enum AnimalStatus {

    Lost("Lost"),
    Found("Found"),
    Home("Home");

    private final String description;

    AnimalStatus(String description) {
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
        for (AnimalStatus status : AnimalStatus.values()) {
            list.add(status.description());
        }
        return list;
    }

    public String description() {
        return this.description;
    }


    public static AnimalStatus getRandom() {
        Random randItem = new Random();
        int numValues = AnimalStatus.values().length;
        return AnimalStatus.values()[randItem.nextInt(numValues)];
    }

}
