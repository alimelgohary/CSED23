package eg.edu.mans.csed;


import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import static eg.edu.mans.csed.Links.LinksToString;

public class Announcements extends AppCompatActivity {
    ScrollView scrollView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcements);
        TextView t = findViewById(R.id.announcementsText);
        scrollView = findViewById(R.id.announcementsScroll);
        try {
            t.setText(LinksToString("announcements.tsv"));
            NamesList.smoothScroll(scrollView, t);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}