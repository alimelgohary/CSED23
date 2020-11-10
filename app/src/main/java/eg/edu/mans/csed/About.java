package eg.edu.mans.csed;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

public class About extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        // Make links long clickable for copying link
        TextView textView = (TextView) findViewById(R.id.about);
        // make links long clickable for copying link
        textView.setMovementMethod(BetterLinkMovementMethod.newInstance().setOnLinkLongClickListener(new BetterLinkMovementMethod.OnLinkLongClickListener() {
            @Override
            public boolean onLongClick(TextView textView, String url) {

                //copy to clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("text", url);
                clipboard.setPrimaryClip(cData);

                Toast.makeText(getApplicationContext(),"Copied link to clipboard.", Toast.LENGTH_SHORT).show();
                return true;
            }

        }));
        Linkify.addLinks(textView, Linkify.WEB_URLS);




    }
}