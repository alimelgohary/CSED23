package eg.edu.mans.csed;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;

public class Table extends AppCompatActivity {
   // public int section = Loading.section;
    String[][] data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);


        Log.e("why", "we arrived at table.java");

        try {
            data = tsvToArray("tables.tsv", 7, 4, Table.this);
        } catch (Exception e) {
            Log.e("why", e.getMessage());
        }

            Calendar cal = Calendar.getInstance();
            int dayFromCalendar = cal.get(Calendar.DAY_OF_WEEK);
            try {
                getResult(dayFromCalendar);
            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    public void tomorrow(View m) throws Exception {
        Calendar calendar = Calendar.getInstance();
        int dayFromCalendar = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayFromCalendar == 7){getResult(1);}
        else{getResult(dayFromCalendar+1);}
    }
    public void today(View m) throws Exception {
        Calendar calendar = Calendar.getInstance();
        int dayFromCalendar = calendar.get(Calendar.DAY_OF_WEEK);
        getResult(dayFromCalendar);
    }
    public void saturday(View m) throws Exception {
        getResult(7);
    }public void sunday(View m) throws Exception {
        getResult(1);
    }public void monday(View m) throws Exception {
        getResult(2);
    }
    public void tuesday(View m) throws Exception {
        getResult(3);
    }
    public void wednesday(View m) throws Exception {
        getResult(4);
    }
    public void thursday(View m) throws Exception {
        getResult(5);
    }
    public void friday(View m) throws Exception {
        getResult(6);
    }

    public void getResult(int day) throws Exception {

        TextView sec = findViewById(R.id.sec);
        TextView t = findViewById(R.id.tableText);

        //get week number for online or f2f thing
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        if(day==7 && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 6){weekNumber++;} //if pressed tomorrow from friday
        Log.e("why", ""+weekNumber);
        String evenOrOdd =  weekNumber%2==0? "أسبوع زوجي" : "أسبوع فردي";

        String formatted = "";
        try{
            //get lectures as an array [lec1, lec2, lec3, ...]  (lectures is split by '=')
            //data is a 2d array representing table sheet
            //Section number (static) is found in the first activity (Loading)
            String [] str = data[day-1][Loading.section-1].split("=");

            str = format24to12(str); //replace 18:00 to 06:00pm

            for(int i= 1; i<str.length; i++){
                formatted = formatted + str[i] + "\n\n";  //format table
            }

            sec.setText(""+str[0]+ "\n" + evenOrOdd);  //sec n day NNN
            t.setText(formatted); // table
        }
        catch (Exception e){
            t.setText("Check Internet Connection");
        }
    }
    public static String[] format24to12(String [] str){
        //for loop to change 24 format to 12 format
        for(int i=0;i<str.length;i++){
            int index = str[i].indexOf(':');

            if(index>=0) {   //if ':' found
                String strHour = str[i].substring(index - 2, index);
                int hour = Integer.parseInt(strHour);
                if (hour > 12) {
                    String part1 = str[i].substring(0, index);
                    String part2 = str[i].substring(index, index+3);
                    String part3 = str[i].substring(index+3);

                    if (hour - 12 < 10) {
                        part1 = part1.replace(strHour, "0"+ (hour - 12)); // 01 instead of 1
                    } else {
                        part1 = part1.replace(strHour, String.valueOf(hour-12));
                    }
                    str[i] = part1 + part2 + " م " + part3; //مساءا

                }
                else{
                    String part1 = str[i].substring(0, index+3);
                    String part2 = str[i].substring(index+3);

                    str[i] = part1 + " ص " + part2; //صباحا
                }
            }
        }
        return str;
    }

    public static String[][] tsvToArray(String fileName, int r, int c, Context context) throws Exception {

        BufferedReader TSVFile = null;

        String[][] card = new String[r][c];
        TSVFile = new BufferedReader(
                new FileReader(new File(context.getFilesDir(), fileName)));
        int j=0;
        String dataRow = "";


        while ((dataRow = TSVFile.readLine())!=null){

            String[] dataArray = dataRow.split("\t");

            for (int i=0; i<dataArray.length; i++) {
                card [j][i] = dataArray[i];
            }

            //dataRow = TSVFile.readLine();
            j++;
        }

        TSVFile.close();
        return card;
    }


}