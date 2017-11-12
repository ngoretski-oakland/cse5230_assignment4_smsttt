package edu.csi5230.ngoretski.homework4;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText userName;
    private Button callButton;

    private BroadcastReceiver br;

    private boolean waitingForResponse = false;

    private SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumber = (EditText) findViewById(R.id.editText);
        userName = (EditText) findViewById(R.id.editText2);
        callButton = (Button) findViewById(R.id.button);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},1);

        smsManager = SmsManager.getDefault();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneNumber.getText().toString();

                smsManager.sendTextMessage(number, null, MessageHandler.getInviteMessage(), null, null);

                waitingForResponse = true;
            }
        });

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

                    for (SmsMessage m: messages) {
                        final String number = m.getDisplayOriginatingAddress();
                        String text = m.getDisplayMessageBody();

                        System.err.println("text:"+text);

                        if (MessageHandler.isMessageInvite(text)) {
                            new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Game Invite")
                            .setMessage("Would you like to play a game with "+number +"?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    smsManager.sendTextMessage(number, null, MessageHandler.getAcceptMessage(), null, null);

                                    Intent i = new Intent(context, TttActivity.class);
                                    i.putExtra("NUMBER", number);
                                    i.putExtra("STARTING_TURN", true);
                                    i.putExtra("SYMBOL", "X");
                                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(i);
                                }})
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    smsManager.sendTextMessage(number, null, MessageHandler.getDenyMessage(), null, null);
                                }
                            }).show();
                        }
                        else if (MessageHandler.isMessageAccept(text)) {
                            Intent i = new Intent(context, TttActivity.class);
                            i.putExtra("NUMBER", phoneNumber.getText().toString());
                            i.putExtra("STARTING_TURN", false);
                            i.putExtra("SYMBOL", "O");
                            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(i);
                        }
                        else if (MessageHandler.isMessageDeny(text)) {
                            Toast.makeText(MainActivity.this, "They said No", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            }
        };
        registerReceiver(br, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

    }
}
