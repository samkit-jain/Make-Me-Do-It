package jain.samkit.makemedoit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Remind extends ActionBarActivity implements View.OnClickListener {

    private TextView descrip;
    final String pref = "MyFile";
    private Button but;
    private AlarmManager scheduler;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

        descrip = (TextView) findViewById(R.id.textView3);
        but = (Button) findViewById(R.id.but);

        SharedPreferences setting = getSharedPreferences(pref, 0);

        // Text to be displayed on the Reminder screen with the ternary conditionals for leading zeroes
        descrip.setText("You need to " + setting.getString("Task", "something") + " at " + ((setting.getInt("Hour", 0) < 10 ) ? ("0" + setting.getInt("Hour", 0)) : (setting.getInt("Hour", 0))) + ":" + ((setting.getInt("Minute", 0) < 10 ) ? ("0" + setting.getInt("Minute", 0)) : (setting.getInt("Minute", 0))));

        but.setOnClickListener(this);

        if(scheduler == null) { // if alarm is not set
            scheduler = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), Notify.class);
            intent.putExtra("message", "It's about time you " + setting.getString("Task", "something"));
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

            // Time to remind user
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, setting.getInt("Hour", 0));
            calendar.set(Calendar.MINUTE, setting.getInt("Minute", 0));

            // Would notify user every 15 minutes until the task is done
            scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("I believe you're not lying, are you?");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "JUST DO IT!!!", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (scheduler != null) { //Task done. Cancel the alarm
                            scheduler.cancel(alarmIntent);
                        }

                        Toast.makeText(getApplicationContext(), "Congratulations!!!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        SharedPreferences setting = getSharedPreferences(pref, 0);
                        setting.edit().putString("State", "New").commit();

                        // Get back to Main Activity
                        startActivity(intent);
                        finish();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
    }
}
