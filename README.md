# SecretSanta
Android application that randomly assigns players giftees

The user name and password for the email that notification messages will be sent from is not included in this repository for security reasons.
To include your own login combination create a file titled "SensitiveInformation.java" in the format:

> package com.bacon.secretsanta;
>
> public class SensitiveInformation {
>
>     protected static String senderEmail = "******@gmail.com";
>     protected static String senderPassword = "********";
> }
