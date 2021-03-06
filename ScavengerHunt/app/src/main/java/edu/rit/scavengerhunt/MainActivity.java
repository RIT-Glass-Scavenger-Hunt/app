package edu.rit.scavengerhunt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    static final int QR_SCAN_RESULT = 0;
    static final int NEXT_TARGET_RESULT = 1;

    public String[] location_clues;
    public String[] location_QR;
    public int target_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create location clues
        location_clues = new String[3];
        location_clues[0] = "Where is Location #1?";
        location_clues[1] = "Where is Location #2?";
        location_clues[2] = "Where is Location #3?";

        location_QR = new String[3];
        location_QR[0] = "Location #1";
        location_QR[1] = "Location #2";
        location_QR[2] = "Location #3";

        //need to start up the location counter
        target_id = 0;

        //then add number to the target header
        TextView target_label = (TextView)findViewById(R.id.current_target);
        target_label.setText("Target 1/10:");

        //then show clue
        TextView clue = (TextView)findViewById(R.id.clue_text);
        showFirstClue(clue);
    }

    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, QR_SCAN_RESULT);
        } catch (ActivityNotFoundException anfe) {
            showDialog(MainActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    public void showFirstClue(View v) {
        TextView clue = (TextView)v;
        clue.setText(location_clues[target_id]);
    }

    private void showNextClue() {
        TextView target_label = (TextView) findViewById(R.id.current_target);
        TextView clue = (TextView) findViewById(R.id.clue_text);

        if (target_id < 2) {
            target_id++;
            clue.setText(location_clues[target_id]);

            String target_string = String.valueOf(target_id + 1);
            target_label.setText("Target " + target_string + "/10:");
        }
        else {
            target_label.setText("Done!");
            clue.setText("No more clues!");
        }

    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == QR_SCAN_RESULT) {
            if (resultCode == RESULT_OK) {

                String contents = intent.getStringExtra("SCAN_RESULT");
                int answer = 0;

                /*String feedback_text = "Sorry, incorrect! Try a different location!";
                String button_text = "Back to Clue";*/

                if (contents.equals(location_QR[target_id])) {
                    answer = 1;
                }

                Intent intent2 = new Intent(getApplicationContext(), QRFeedback.class);
                intent2.putExtra("answer", answer);
                startActivityForResult(intent2, NEXT_TARGET_RESULT);
            }
        }
        else if (requestCode == NEXT_TARGET_RESULT) {
            if (resultCode == RESULT_OK) {
                int answer = intent.getIntExtra("answer", 0);

                if (answer == 1) {
                    showNextClue();
                }
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
