package com.tcc.smsdecrypt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;


import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;


public class SmsSend extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_MESSAGES = 0;
    ImageView sendImgV;
    Button sendBtn;
    EditText txtMessage;
    EditText txtContato;
    String message;
    String contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_send);

        sendImgV = (ImageView) findViewById(R.id.imageSend);
        txtMessage = (EditText) findViewById(R.id.messageText);
        txtContato = (EditText) findViewById(R.id.messageTo);
//        sendBtn = (Button) findViewById(R.id.btnSend);

        txtContato.setText("+5531994429981");

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_MESSAGES);
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.SEND_SMS},
//                        MY_PERMISSIONS_REQUEST_SEND_MESSAGES);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        sendImgV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicado", Toast.LENGTH_SHORT);
                System.out.println("CLICADO");
                sendSMSMessage();
            }
        });

    }

    protected void sendSMSMessage() {
        SmsManager smsManager = SmsManager.getDefault();
        String mensagemCriptografada = "";
        try {
            message = txtMessage.getText().toString();
            contato = txtContato.getText().toString();
            mensagemCriptografada = new EncriptaDecriptaRSA().criptografaPub(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
/*
        String a = mensagemCriptografada.substring(0, 150);*/

        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(mensagemCriptografada);
        sms.sendMultipartTextMessage(contato, null, parts, null, null);

        Toast.makeText(getApplicationContext(), "SMS enviada.",
                Toast.LENGTH_LONG).show();


        /*System.out.println(mensagemCriptografada);
        smsManager.sendTextMessage("+5531994429981", null, a, null, null);*/

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_MESSAGES);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_MESSAGES: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
