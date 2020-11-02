package eg.edu.mans.csed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NamesList extends AppCompatActivity {

    ScrollView mScrollView;
    TextView t;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        String[] sec = {"sec1.txt", "sec2.txt", "sec3.txt", "sec4.txt"};

        title = findViewById(R.id.listTitle);
        t = findViewById(R.id.listText);

        title.setText( "Section "+Loading.section+" Names List" );
        t.setText(ListAssetsToString(sec[Loading.section-1]));

        mScrollView = findViewById(R.id.listScroll);
        smoothScroll(mScrollView, t);


    }
    public static void smoothScroll(final ScrollView scrollView, final TextView textView)
    {
        scrollView.post(new Runnable()
        {
            public void run()
            {
                scrollView.smoothScrollTo(0, textView.getTop());
            }
        });
    }

    public String ListAssetsToString(String location){
        BufferedReader txtFile = null;

        String str = "";
        String cell = "";
        try{
            txtFile = new BufferedReader(
                    new InputStreamReader(getAssets().open(location)));

            while ((cell = txtFile.readLine())!=null){
                    str = str + cell + "\n" + "\n";
            }
            return str;
            }catch (Exception e){
                return "error";
            }


    }
}
