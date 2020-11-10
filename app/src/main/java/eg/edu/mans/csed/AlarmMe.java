package eg.edu.mans.csed;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmMe extends BroadcastReceiver {
    //String link = " https://docs.google.com/spreadsheets/d/e/2PACX-1vSxo0O040tq64Xg3O3zOfIxWMbo65oZ9jk1DTgqXijJqsAfpiLFzmt1kDQ7RMPyQqlf8_EXAadHihEl/pub?output=tsv";
    Context context;
    Handler handler = new Handler();
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        DownloadFileFromURL.newAnnouncementsDone = 0;
        Log.e("why", "triggered Alarm me");
        new DownloadFileFromURL("new_announcements.tsv").execute(context.getString(R.string.announcements));

//        new CountDownTimer(5000,1000){
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish()
//            {
//                checkIfDownloaded();
//            }
//        }.start();
//

        //wait 5 seconds to download new_announcements
        TimerTask timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        Log.e("why", "TimerTaskToCheckIfDownloaded");
                        checkIfDownloaded();
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 5 * 1000);
    }


//    public void sendNotification(String title, String message, int id) {
//        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "Announcements")
//                .setContentTitle(title)
//                .setContentText(message)
//                .setSmallIcon(R.drawable.notify)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setPriority(NotificationCompat.PRIORITY_MAX);
//
//        notificationManager.notify(id, notification.build());
//
//    }
    public List<String> compareLists(List<String> new_ann , List<String> old_ann){
        List<String> result= new ArrayList<>();
        Log.e("why", "\n\n old list size "+old_ann.size() + " = " + old_ann.toString());
        Log.e("why",   "\n\n new list size "+new_ann.size() +"= " + new_ann.toString());
        if(new_ann.equals(old_ann)){
            return result;
        }

        for(int i = 0; i<new_ann.size(); i++){
            //iterate over new , if found values not in old, add to result

            if( !  (old_ann.contains(new_ann.get(i)))   ){
                result.add(new_ann.get(i));
            }

        }

        //renew the old announcements
        writeToAnnouncements(new_ann);


        Log.e("why", "result = "+result.toString());
        return result;
    }
//    public String readNewAnnouncements(){
//        String line;
//        BufferedReader in = null;
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            in = new BufferedReader(new FileReader(new File("//data//data//eg.edu.mans.csed//files//" + "new_announcements.tsv")));
//            while ((line = in.readLine()) != null) stringBuilder.append(line);
//        }
//        catch (Exception e){
//
//        }
//        return stringBuilder.toString();
//    }
    public void writeToAnnouncements(List <String> fileContents) {
        try {
            FileWriter out = new FileWriter(new File("//data//data//eg.edu.mans.csed//files//" + "announcements.tsv"));
            for(int i=0; i<fileContents.size(); i++){
                out.write(fileContents.get(i));
                out.write(System.getProperty("line.separator")); // print new Line
            }
            out.close();
        } catch (Exception e) {
            Log.e("why", "write to string error");
        }
    }

    public static List<String> tsvToList(String fileName){
        List<String> list = new ArrayList<>();
        BufferedReader TSVFile = null;


        try{
            TSVFile = new BufferedReader(
                    new FileReader(new File("//data//data//eg.edu.mans.csed//files//" + fileName)));
            String cell = "";
            while ((cell = TSVFile.readLine())!=null){
                list.add(cell);
            }
            return list;
        }catch (Exception e){
            e.getMessage();
        }
        return list;
    }


    public void checkIfDownloaded(){
        //if (DownloadFileFromURL.newAnnouncementsDone == 1){
        Log.e("why", "checkIfDownloaded here");
        try {
            List<String> new_ann = tsvToList("new_announcements.tsv");
            List<String> old_ann = tsvToList("announcements.tsv");

            List<String> compare = compareLists(new_ann, old_ann);
            if (compare.size() != 0) {
                for (int i = 0; i < compare.size(); i++) {
                    NotificationHelper notificationHelper = new NotificationHelper(context);

                    //Split the announcement to Sender,Message (will be written in sheet in that format)
                    String [] announcement = compare.get(i).split("\\$");
                    if (announcement.length == 2){
                        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(announcement[0], announcement[1]);
                        notificationHelper.getManager().notify(i, nb.build());
                    }
                    else{
                        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("New Announcement", announcement[0]);
                        notificationHelper.getManager().notify(i, nb.build());
                    }
                }
            }
        }catch (Exception e){
            //if no announcements file
            Log.e("why", "error in check if downloaded new announcements to get notifications" + e.getMessage());
        }
        //}
    }

}
