package com.bacon.secretsanta;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by Jason on 2016-10-23.
 */

public class ExternalContact extends AsyncTask<Person, Integer, String> {

    /* The address the email will be sent from */
    private static final String senderAddress = SensitiveInformation.senderEmail;
    /* The password used to access the sender email */
    private static final String senderPassword = SensitiveInformation.senderPassword;
    /* Database helper object */
    private static DatabaseHelper myDatabase;
    /* Activity calling the async task */
    private Context context;

    public ExternalContact(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        myDatabase = MainActivity.myDatabase;
    }

    @Override
    protected String doInBackground(Person... persons){
        for (Person person : persons) {
            myDatabase.updateRecord(person);
            switch(person.getContactMethod()){
                case "SMS":
                    sendSms(person.getContactInformation(), "Your secret santa giftee is " + person.getGiftee(), context);
                    break;
                case "EMAIL":
                    sendEmail("Secret Santa", "Your secret santa giftee is " + person.getGiftee(), person.getContactInformation(), context);
                    break;
            }
        }
        return "Passed to post execute";
    }

    @Override
    protected void onProgressUpdate(Integer... values){}

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast.makeText(context, "Messages successfully sent", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sends ExternalContact message to a desired phone number
     * @param phoneNumber The phone number to sent the message to
     * @param message The ExternalContact message to send
     */
    public static void sendSms(String phoneNumber, String message, Context context){

        SmsManager smsManager = SmsManager.getDefault();
        try{
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sends an email message to the desired email address
     * @param subject The subject line for the email
     * @param body The body message of the email
     * @param recipientAddress The email address the message will be sent to
     */
    public static void sendEmail(String subject, String body, String recipientAddress, Context context){

        try{
            MailSender sender = new MailSender(senderAddress, senderPassword);
            sender.sendMail(subject, body, senderAddress, recipientAddress);
        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
