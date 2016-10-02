package com.bacon.secretsanta;

/**
 * Created by Jason on 2016-10-01.
 */

public class Person{

    private String name;
    private String contactMethod;
    private String contactInformation;
    private String giftee;

    /**
     * An empty constructor to let the database helper run smoothly (line 71-80)
     */
    public Person(){}

    /**
     * Creates a new person object
     * @param name The name of the person
     * @param method The method with which to contact the person
     * @param information The SMS number or email of the person
     */
    public Person(String name, String method, String information){
        this.name = name;
        this.contactMethod = method;
        this.contactInformation = information;
        this.giftee = "not calculated";
    }

    /**
     * Returns the persons name
     * @return The persons name
     */
    public String getName(){
        return name;
    }

    /**
     * Sets or replaces the name of the person
     * @param name The new name of the person
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Returns the preferred contact method of the person
     * @return 'EMAIL' or 'SMS'
     */
    public String getContactMethod(){
        return contactMethod;
    }

    /**
     * Sets or replaces the contact method of
     * @param method SMS or EMAIL depending on the persons preference
     */
    public void setContactMethod(String method){
        this.contactMethod = method;
    }

    /**
     * Returns the contact information of the person
     * @return An SMS number or email depending on contact method
     */
    public String getContactInformation(){
        return contactInformation;
    }

    /**
     * Sets or replaces the contact information of this person
     * @param information SMS number or EMAIL address depending on the persons preferred contact method
     */
    public void setContactInformation(String information){
        this.contactInformation = information;
    }

    /**
     * Returns the giftee of the person
     * @return The name of the giftee
     */
    public String getGiftee(){
        return giftee;
    }

    /**
     * Sets or replaces the giftee of the person
     * @param giftee The name of the giftee
     */
    public void setGiftee(String giftee){
        this.giftee = giftee;
    }

    /**
     * Creates a textual representation of the person
     * @return The name, contact method, contact information, and giftee of the person
     */
    public String toString(){
        return name + " has " + giftee + " as their giftee.";
    }
}
