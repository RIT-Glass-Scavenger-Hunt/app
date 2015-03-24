package com.javacodegeeks.androidqrcodeexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidBarcodeQrExample extends Activity {
	/** Called when the activity is first created. */

	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    public String[] location_clues;
    public String[] location_QR;
    public int target_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
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
       /* SharedPreferences app_data = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = app_data.edit();
        editor.putInt("targetNumber", 0);
        editor.commit();*/

        //then add number to the target header
        TextView target_label = (TextView)findViewById(R.id.current_target);
        target_label.setText("Target 1:");

        //then show clue
        TextView clue = (TextView)findViewById(R.id.clue_text);
        showClue(clue);
	}

/*	public void scanBar(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(AndroidBarcodeQrExample.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}*/

	public void scanQR(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(AndroidBarcodeQrExample.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}

    public void showClue(View v) {
        TextView clue = (TextView)v;

        /*SharedPreferences app_data = PreferenceManager.getDefaultSharedPreferences(this);
        int target_number = app_data.getInt("targetNumber", 0);*/

        clue.setText(location_clues[target_id]);

    }

    public void showNextClue(View v) {
        if (target_id < 2) {
            TextView clue = (TextView) findViewById(R.id.clue_text);
            TextView feedback = (TextView) findViewById(R.id.feedback);
            target_id++;

            clue.setText(location_clues[target_id]);
            feedback.setText("");

            TextView target_label = (TextView) findViewById(R.id.current_target);
            String target_string = String.valueOf(target_id + 1);
            target_label.setText("Target " + target_string + ":");
        }
        else {
            TextView target_label = (TextView) findViewById(R.id.current_target);
            target_label.setText("Done!");
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
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");


                //for our app, the qr code should send back a value that identifies the location id
                //then this value is checked against the database, which would then give a confirmation message
                //and then the app can move on
                //perhaps can send plain text that has location name
                //(rather than this toast pop-up)
				/*Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
				toast.show();*/

                TextView feedback = (TextView)findViewById(R.id.feedback);

                if (contents.equals(location_QR[target_id])) {
                    feedback.setText("Correct!");
                }
                else {
                    feedback.setText("Sorry, incorrect! Try a different location!");
                }
			}
		}
	}
}