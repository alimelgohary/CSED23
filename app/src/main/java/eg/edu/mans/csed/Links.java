package eg.edu.mans.csed;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

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

        // make links long clickable for copying link
        t.setMovementMethod(BetterLinkMovementMethod.newInstance().setOnLinkLongClickListener(new BetterLinkMovementMethod.OnLinkLongClickListener() {
            @Override
            public boolean onLongClick(TextView textView, String url) {

                //copy to clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("text", url);
                clipboard.setPrimaryClip(cData);

                // when long clicked, text is selected so this code is to deselect text
                textView.setSelected(false);

                Toast.makeText(getApplicationContext(),"Link copied to clipboard.", Toast.LENGTH_SHORT).show();
                return true;
            }

        }));
        Linkify.addLinks(t, Linkify.WEB_URLS);



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