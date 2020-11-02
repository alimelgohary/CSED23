package eg.edu.mans.csed;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import static eg.edu.mans.csed.Table.tsvToArray;

public class Loading extends AppCompatActivity {
    String[][] data;
    public static int section = 0;
    public String read;
    Context context = Loading.this;
    final Handler handler = new Handler();
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        check_remember_me();

        //downloadFiles();
        new DownloadFileFromURL("tables.tsv").execute(getString(R.string.tables));
        new DownloadFileFromURL("links.tsv").execute(getString(R.string.links));
        Log.e("why", "download file() reached");

        //on boot enable the receiver
        ComponentName receiver = new ComponentName(context, CSEDBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }

    public void check_remember_me() {
        try {
            read = readFile(context, "csed.txt");
        } catch (Exception e) {
            Log.d("why", "error reading csed.txt");
        }


        if (read.equals("")) {
            Log.e("why", "first time?");
            setContentView(R.layout.sections);
        } else if (read.equals("1")) {
            section = 1;
            setContentView(R.layout.test);
            getLive();
        } else if (read.equals("2")) {
            section = 2;
            setContentView(R.layout.test);
            getLive();
        } else if (read.equals("3")) {
            section = 3;
            setContentView(R.layout.test);
            getLive();
        } else if (read.equals("4")) {
            section = 4;
            setContentView(R.layout.test);
            getLive();
        }


        Log.e("why", "making alarms");

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Log.e("why", "made alarm manager");

        Intent intent = new Intent(this, eg.edu.mans.csed.AlarmMe.class);
        Log.e("why", "made intent");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        Log.e("why", "made pending intent");

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);

        Log.e("why", "made repeating");

    }
    public static void writeToFile(final String fileContents, Context context, String name) {
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), name));
            out.write(fileContents);
            out.close();
        } catch (Exception e) {
            Log.e("why", "write to string error");
        }
    }

    public static String readFile(Context context, String fileName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);
        } catch (Exception e) {
            Log.e("why", "error in readFile method"+ e.getMessage());
            return "";
        }

        return stringBuilder.toString();
    }

    public void section1(View m){
        section = 1;
        Switch rememberMe = findViewById(R.id.switch1);
        if(rememberMe.isChecked()){
            writeToFile("1", context, "csed.txt");
        }
        else{
            writeToFile("",context,"csed.txt");
        }
        setContentView(R.layout.test);
        getLive();
    }

    public void section2(View m){
        section = 2;
        Switch rememberMe = findViewById(R.id.switch1);
        if(rememberMe.isChecked()){
            writeToFile("2", context, "csed.txt");
        }
        else{
            writeToFile("", context, "csed.txt");
        }
        setContentView(R.layout.test);
        getLive();
    }

    public void section3(View m){
        Switch rememberMe = findViewById(R.id.switch1);
        section = 3;
        if(rememberMe.isChecked()){
            writeToFile("3", context, "csed.txt");
        }
        else{
            writeToFile("", context, "csed.txt");
        }
        setContentView(R.layout.test);
        getLive();
    }

    public void section4(View m) {
        Switch rememberMe = findViewById(R.id.switch1);
        section = 4;
        if (rememberMe.isChecked()) {
            writeToFile("4", context,"csed.txt");
        }
        else{
            writeToFile("", context, "csed.txt");
        }
        setContentView(R.layout.test);
        getLive();
    }
    public void getLive(){
        TextView liveNow = findViewById(R.id.live);
        Log.e("why", "get Live Reached");
        try{
            data = tsvToArray("tables.tsv", 7, 4, context);
            findHoursAndLive();
        }catch (Exception e){
            liveNow.setText("Live Now\n\n Waiting For Internet");
            new CountDownTimer(10000,1000){
                @Override
                public void onTick(long millisUntilFinished) {}
                @Override
                public void onFinish() {
                    try{
                        data = tsvToArray("tables.tsv", 7, 4, context);
                        findHoursAndLive();
                    }catch (Exception e){
                        TextView liveNow = findViewById(R.id.live);
                        liveNow.setText("No Internet. \n\n Try Refreshing.");
                        return;
                    }
                }
            }.start();
        }
    }
    public void findHoursAndLive() {
        TextView liveNow = findViewById(R.id.live);

        Calendar calendar = Calendar.getInstance();
        //get current day and time
        int dayFromCalendar = calendar.get(Calendar.DAY_OF_WEEK);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = calendar.get(Calendar.MINUTE);

        //if time = 13:30 then Convert to minutes to compare it with lecture time--> 13*60 + 30
        int currentHourWithMinutes = currentHour * 60 + currentMinutes;

        //Get Today's table with chosen Section
        String[] str = data[dayFromCalendar - 1][Loading.section - 1].split("=");
        int i; // initialized here to be used outside loop
        String currentLecture = "";
        int difference; // Difference between current time and lecture time
        int lecDuration;

        // A loop to find time in today's lectures
        for (i = 0; i < str.length; i++) {
            //For ex. 11:50 ==> find first the ':'
            int index = str[i].indexOf(':');

            if (index >= 0) {   //if ':' found
                String strHour = str[i].substring(index - 2, index); // "11"
                String strMinutes = str[i].substring(index + 1, index + 3); //"50"

                //Get lecture time
                int lecHour = Integer.parseInt(strHour); // 11
                int lecMinutes = Integer.parseInt(strMinutes); // 50

                //Convert Lecture time to minutes to compare it with current time--> 11*60 + 50
                int lecHourWithMinutes = lecHour * 60 + lecMinutes;

                //Difference Between lecture time and current time
                difference = currentHourWithMinutes - lecHourWithMinutes;

                //f2f Lecture length is 90 minutes and online is 60
                lecDuration = str[i].contains("أونلاين")
                        || str[i].contains("اونلاين")
                        || str[i].contains("online")
                        || str[i].contains("on line")
                        || str[i].contains("Online") ? 60 : 90;

                if (difference < lecDuration && difference >= 0) {                 //If Lecture is Live
                    currentLecture = str[i];
                    liveNow.setText("\uD83D\uDD34 Live Now \n\n " + currentLecture); //Circle Symbol
                    break;                                                          // Don't look anymore
                }
            }
        }
        if (i == str.length) {
            liveNow.setText("Live Now : \n\nNothing "); //if not found any Lecture now
        }

        TimerTask timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        Log.e("why", "TimerTask");
                        findHoursAndLive();
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 60 * 1000);

    }


    public void downloadFiles () {
            new DownloadFileFromURL("tables.tsv").execute(getString(R.string.tables));
            new DownloadFileFromURL("links.tsv").execute(getString(R.string.links));
            new DownloadFileFromURL("announcements.tsv").execute(getString(R.string.announcements));

        }

    public void backToSections(View m){
        writeToFile("", Loading.this, "csed.txt");
        setContentView(R.layout.sections);
        Log.e("why", "you clicked back to sections");
    }



    public void announcements(View m){
        startActivity(new Intent(Loading.this, eg.edu.mans.csed.Announcements.class));
    }
    public void timeTable(View m){
        startActivity(new Intent(Loading.this, eg.edu.mans.csed.Table.class));
        Log.e("why", "you clicked time table");
    }
    public void links(View m){
        startActivity(new Intent(Loading.this, eg.edu.mans.csed.Links.class));
        Log.e("why", "you clicked links");
    }
    public void list(View m){
        startActivity(new Intent (Loading.this, eg.edu.mans.csed.NamesList.class));
        Log.e("why", "you clicked list");
    }

    public void refresh(View m){
        DownloadFileFromURL.done = 0;
        downloadFiles();
        final Button refresh =findViewById(R.id.buttonRefresh);
        refresh.setText("\uD83D\uDD04 Refreshing . . .");

//        setContentView(R.layout.loading);
        new CountDownTimer(15000, 1000){
            public void onTick(long millisUntilFinished){
                Log.e("why", "onTick working");
                if(DownloadFileFromURL.done==3){
                    refresh.setText("\uD83D\uDD04 Refresh");
                    cancel();
                    check_remember_me();
                    DownloadFileFromURL.done=0;
                    Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                }
            }
            public  void onFinish(){
                if(DownloadFileFromURL.done!=3){
                    refresh.setText("\uD83D\uDD04 Refresh");
                    Toast.makeText(getApplicationContext(),"Check your internet connection.",Toast.LENGTH_LONG).show();
                    Log.e("why", "offline" + " done = " + DownloadFileFromURL.done);
                }

                check_remember_me();
            }
        }.start();
    }



    public void notes(View m){
        startActivity(new Intent(Loading.this, eg.edu.mans.csed.Notes.class));
    }

    public void about(View m){
        startActivity(new Intent(Loading.this, eg.edu.mans.csed.About.class));
    }


}


