package eg.edu.mans.csed;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

class DownloadFileFromURL extends AsyncTask<String, String, String> {
    String fileName;
    public static int done = 0;
    public static int newAnnouncementsDone=0;
    DownloadFileFromURL(String fileName){
        this.fileName = fileName;
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();



            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            OutputStream output = new FileOutputStream("//data//data//eg.edu.mans.csed//files//" + fileName);

            byte[] data = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {



                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            if (fileName.equals("new_announcements.tsv")){
                newAnnouncementsDone +=1;
            }else {
                done += 1;
            }
            Log.e("why", "finished downloading");
            if (done ==3) {
               // Log.e("why", "make alarms here based on time in announcements");

               // Alarms.makeAlarms();
            }
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }


}
