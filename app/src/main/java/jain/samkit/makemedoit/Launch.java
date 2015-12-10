package jain.samkit.makemedoit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Launch extends Activity{

    final String pref = "MyFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences setting = getSharedPreferences(pref, 0);

        // To check whether a task is set or not and launch the required activity
        switch(setting.getString("State", "New")) {
            case "New":
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
                finish();
                break;
            case "Remind":
                Intent rem = new Intent(getApplicationContext(), Remind.class);
                startActivity(rem);
                finish();
                break;
        }
    }
}