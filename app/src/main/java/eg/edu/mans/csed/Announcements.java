package eg.edu.mans.csed;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

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
            t.setText(LinksToString("announcements.tsv").replaceAll("\\$", " :\n"));
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


    }



}