package jain.samkit.makemedoit;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Button submit;
    private EditText mTask;
    private EditText mTime;
    private int hour;
    private int minute;
    private String eTask;
    final String pref = "MyFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = (Button) findViewById(R.id.submit);
        mTask = (EditText) findViewById(R.id.task);
        mTime = (EditText) findViewById(R.id.time);

        hour = -1;
        eTask = "";

        submit.setOnClickListener(this);

        mTime.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                eTask = mTask.getText().toString().trim(); // Removing leading and trailing whitespaces

                if(hour != -1 && !eTask.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    if(hour < 10 && minute < 10) {
                        builder.setMessage("Remind you to " + eTask + " at 0" + hour + ":0" + minute).setTitle("Are you sure?");
                    } else if (hour < 10) {
                        builder.setMessage("Remind you to " + eTask + " at 0" + hour + ":" + minute).setTitle("Are you sure?");
                    } else if (minute < 10) {
                        builder.setMessage("Remind you to " + eTask + " at " + hour + ":0" + minute).setTitle("Are you sure?");
                    } else {
                        builder.setMessage("Remind you to " + eTask + " at " + hour + ":" + minute).setTitle("Are you sure?");
                    }

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getApplicationContext(), "I'll make you " + eTask, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), Remind.class);

                            // Saving reminder details
                            SharedPreferences setting = getSharedPreferences(pref, 0);
                            setting.edit().putString("Task", eTask).commit();
                            setting.edit().putInt("Hour", hour).commit();
                            setting.edit().putInt("Minute", minute).commit();
                            setting.edit().putString("State", "Remind").commit();

                            // Task set.
                            startActivity(intent);
                            finish();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (hour == -1 && eTask.equals("")) {
                    Toast.makeText(getApplicationContext(), "What and when am I supposed to remind you?", Toast.LENGTH_SHORT).show();
                } else if (hour == -1) {
                    Toast.makeText(getApplicationContext(), "When am I supposed to remind you?", Toast.LENGTH_SHORT).show();
                } else if (eTask.equals("")) {
                    Toast.makeText(getApplicationContext(), "What am I supposed to remind you?", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.time:
                DialogFragment timePicker = new TimePick();
                timePicker.show(getSupportFragmentManager(), "timePicker");
                break;

            case R.id.task:
                break;
        }
    }

    public void setmTime(int h, int m) {

        if(h < 10 && m < 10) {
            mTime.setText("0" + h + ":0" + m);
        } else if (h < 10) {
            mTime.setText("0" + h + ":" + m);
        } else if (m < 10) {
            mTime.setText(h + ":0" + m);
        } else {
            mTime.setText(h + ":" + m);
        }

        hour = h;
        minute = m;
    }
}
