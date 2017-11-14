package edu.csi5230.ngoretski.homework4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class TttActivity extends AppCompatActivity {

    List<Button> buttons = new ArrayList<Button>(9);
    TttTextView label = null;

    String enemyNumber;
    boolean myTurn = false;
    String symbol;

    private BroadcastReceiver br;
    private SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttt);

        enemyNumber = getIntent().getExtras().get("NUMBER").toString();
        myTurn = getIntent().getBooleanExtra("STARTING_TURN", false);
        symbol = getIntent().getStringExtra("SYMBOL");

        smsManager = SmsManager.getDefault();

        label = (TttTextView) findViewById(R.id.textView1);

        setTurnLabel();

        buttons.add((Button) findViewById(R.id.button0));
        buttons.add((Button) findViewById(R.id.button1));
        buttons.add((Button) findViewById(R.id.button2));
        buttons.add((Button) findViewById(R.id.button3));
        buttons.add((Button) findViewById(R.id.button4));
        buttons.add((Button) findViewById(R.id.button5));
        buttons.add((Button) findViewById(R.id.button6));
        buttons.add((Button) findViewById(R.id.button7));
        buttons.add((Button) findViewById(R.id.button8));

        for (int i = 0; i < 9 ; i++) {
            final Button button = buttons.get(i);

            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println("myTurn:"+myTurn + " button.getText():" + String.valueOf(button.getText()));

                    if (!myTurn || (button.getText() != null && !"".equals(button.getText().toString()))) {
                        System.err.println("ignorning click");
                        System.out.println("ignoring click myTurn:"+myTurn + " button.getText():" + String.valueOf(button.getText()));

                        return;
                    }

                    System.err.println("Sending message");
                    System.out.println("ignoring click myTurn:"+myTurn + " button.getText():" + String.valueOf(button.getText()));

                    smsManager.sendTextMessage(enemyNumber, null, MessageHandler.getMessageMove(buttons.indexOf(button), symbol), null, null);
                    button.setText(symbol);

                    boolean win = checkForWin(symbol);

                    if (win) {
                        label.setText("YOU WON!");
                        myTurn = false;
                        return;
                    }

                    myTurn = false;
                    setTurnLabel();
                }
            });

        }

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

                    for (SmsMessage m: messages) {
                        final String number = m.getDisplayOriginatingAddress();
                        String text = m.getDisplayMessageBody();

                        System.err.println("text:"+text);

                        if (!myTurn && MessageHandler.isMessageMove(text)) {
                            String tmpSymbol = MessageHandler.getSymbol(text);
                            int pos = MessageHandler.getPosition(text);

                            if (buttons.get(pos).getText() == null || "".equals(buttons.get(pos).getText().toString())) {
                                buttons.get(pos).setText(tmpSymbol);

                                boolean win = checkForWin(tmpSymbol);

                                if (win) {
                                    label.setText(enemyNumber + " WON!");
                                    return;
                                }

                                myTurn = true;
                                setTurnLabel();
                            }
                        }
                    }
                }
            }
        };
        registerReceiver(br, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

    }

    private boolean checkForWin(String symbol) {
        if ((symbol.equals(buttons.get(0).getText().toString()) && symbol.equals(buttons.get(1).getText().toString()) && symbol.equals(buttons.get(2).getText().toString()))
                ||
                (symbol.equals(buttons.get(3).getText().toString()) && symbol.equals(buttons.get(4).getText().toString()) && symbol.equals(buttons.get(5).getText().toString()))
                ||
                (symbol.equals(buttons.get(6).getText().toString()) && symbol.equals(buttons.get(7).getText().toString()) && symbol.equals(buttons.get(8).getText().toString()))
                ||
                (symbol.equals(buttons.get(0).getText().toString()) && symbol.equals(buttons.get(3).getText().toString()) && symbol.equals(buttons.get(6).getText().toString()))
                ||
                (symbol.equals(buttons.get(1).getText().toString()) && symbol.equals(buttons.get(4).getText().toString()) && symbol.equals(buttons.get(7).getText().toString()))
                ||
                (symbol.equals(buttons.get(2).getText().toString()) && symbol.equals(buttons.get(5).getText().toString()) && symbol.equals(buttons.get(8).getText().toString()))
                ||
                (symbol.equals(buttons.get(0).getText().toString()) && symbol.equals(buttons.get(4).getText().toString()) && symbol.equals(buttons.get(8).getText().toString()))
                ||
                (symbol.equals(buttons.get(2).getText().toString()) && symbol.equals(buttons.get(4).getText().toString()) && symbol.equals(buttons.get(6).getText().toString()))){

            if (myTurn) {
                label.setText("YOU WIN!");
            }
            else {
                label.setText(enemyNumber + " WINS!");
            }

            smsManager.sendTextMessage(enemyNumber, null, MessageHandler.getGameOverMessage(), null, null);

            return true;
        }

        return false;
    }

    private void setTurnLabel() {
        if (myTurn) {
            label.setText("Your turn");
        }
        else {
            label.setText(enemyNumber + "'s turn");
        }
    }

}
