package eg.edu.mans.csed;

import android.content.Context;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Links extends AppCompatActivity {
ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.links);

        TextView t = findViewById(R.id.linksText);
        scrollView = findViewById(R.id.linksScroll);
        try {
            t.setText(LinksToString("links.tsv"));
            NamesList.smoothScroll(scrollView, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



public static String LinksToString(String fileName) throws IOException {
    BufferedReader TSVFile = null;

    String str = "";

    try{
    TSVFile = new BufferedReader(
            new FileReader(new File("//data//data//eg.edu.mans.csed//files//" + fileName)));
    String cell = "";
    while ((cell = TSVFile.readLine())!=null){
        str = str + cell + "\n" + "-------------" + "\n";
    }
    return str;
    }catch (Exception e){
        return "Waiting For Internet Connection";
    }

}
}