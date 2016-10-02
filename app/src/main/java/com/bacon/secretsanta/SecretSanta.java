package com.bacon.secretsanta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Jason on 2016-10-02.
 */

public class SecretSanta {

    private static Random rng = new Random();
    private static ArrayList<Person> santaGroup;
    private static ArrayList<Person> clone;
    private static int selection;

    /**
     * Creates a new SecretSanata object
     * @param person The array of people participating
     */
    private SecretSanta(Person[] person){
        santaGroup = new ArrayList<>(Arrays.asList(person));
        clone = new ArrayList<>(Arrays.asList(person));
    }

    /**
     * Assigns a giftee to a person
     * @param person The person being assigned their giftee
     * @param remainingPersons The array of people that haven't been assigned gifters
     * @return The name of the person giftee
     */
    private static String shuffle(Person person, ArrayList<Person> remainingPersons){
        selection = rng.nextInt(remainingPersons.size());
        while(remainingPersons.get(selection) == person){
            selection = rng.nextInt(remainingPersons.size());
        }
        String name = remainingPersons.get(selection).getName();
        santaGroup.remove(selection);
        return name;
    }

    /**
     *  Traverses the players and assigns them giftees
     * @param person The list of players
     * @return The list of players with giftees assigned
     */
    public static Person[] assignPlayers(Person[] person){
        SecretSanta organize = new SecretSanta(person);
        for(int i = 0; i < clone.size(); i++){
            clone.get(i).setGiftee(shuffle(clone.get(i), santaGroup));
        }
        return clone.toArray(new Person[clone.size()]);
    }
}
