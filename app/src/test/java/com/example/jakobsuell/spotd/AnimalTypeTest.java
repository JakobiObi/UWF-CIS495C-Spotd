package com.example.jakobsuell.spotd;

import org.junit.Test;

import enums.AnimalType;

import static org.junit.Assert.assertEquals;

public class AnimalTypeTest {

    @Test
    public void validAnimalType_ReturnsTrue() {

        // arrange
        String bird1 = "BIRD";
        String bird2 = "bird";
        String bird3 = "Bird";
        String bird4 = "bIRD";

        boolean isBird1 = false;
        boolean isBird2 = false;
        boolean isBird3 = false;
        boolean isBird4 = false;

        // act
        isBird1 = AnimalType.isValidAnimalType(bird1);
        isBird2 = AnimalType.isValidAnimalType(bird2);
        isBird3 = AnimalType.isValidAnimalType(bird3);
        isBird4 = AnimalType.isValidAnimalType(bird4);

        // assert
        assertEquals(true, isBird1);
        assertEquals(true, isBird2);
        assertEquals(true, isBird3);
        assertEquals(true, isBird4);

    }

    @Test
    public void invalidAnimalType_ReturnsFalse() {

        // arrange
        String donkey = "donkey";
        boolean isValid = false;

        // act
        isValid = AnimalType.isValidAnimalType(donkey);

        // assert
        assertEquals(false, isValid);

    }


}