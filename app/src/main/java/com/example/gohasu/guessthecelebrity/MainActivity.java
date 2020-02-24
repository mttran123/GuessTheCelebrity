package com.example.gohasu.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ImageDownloader task;
    Bitmap myImage;
    Pattern p;
    Matcher m;

    List<String> urlList = new ArrayList();
    List<String> nameList = new ArrayList();

    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Random r = new Random();
    int random;

    String result;

    public void answerChosen(View view) {
        Button buttonClicked = (Button) view;

        String buttonText = buttonClicked.getText().toString();

        if(buttonText.equals(result)) {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong. It was " + result, Toast.LENGTH_SHORT).show();
        }
        imageDownloading();
    }

    public void imageDownloading(){
        try {
            random = r.nextInt(52);   // 52 can be replaced with urlList.size()
            task = new ImageDownloader();

            try {
                myImage = task.execute(urlList.get(random)).get();
                imageView.setImageBitmap(myImage);

            } catch (Exception e) {
                e.printStackTrace();
            }

            button0 = findViewById(R.id.button0);
            button1 = findViewById(R.id.button1);
            button2 = findViewById(R.id.button2);
            button3 = findViewById(R.id.button3);

            Button[] arrayButton = {button0, button1, button2, button3};

            int randomButton = r.nextInt(3) + 0;
            result = nameList.get(random);


            for (int j = 0; j < 4; j++) {
                if (randomButton == j) {
                    arrayButton[j].setText(result);

                } else {
                    random = r.nextInt(52);
                    String wrongAnswer = nameList.get(random);
                    while (wrongAnswer.equals(result)) {
                        random = r.nextInt(52);
                        wrongAnswer = nameList.get(random);
                    }
                    arrayButton[j].setText(wrongAnswer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);


        DownloadWebContent webContent = new DownloadWebContent();

        String htmlContent = null;


        try {
            htmlContent = webContent.execute("http://www.posh24.se/kandisar").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] stringSplit = htmlContent.split("<div class=\"title\">Lista:</div>");


        p = Pattern.compile("<img src=\"(.*?)\"");
        m = p.matcher(stringSplit[1]);


        while(m.find()) {
            urlList.add(m.group(1));
        }

        p = Pattern.compile("alt=\"(.*?)\"");
        m = p.matcher(stringSplit[1]);


        while(m.find()){
            nameList.add(m.group(1));
        }
        imageDownloading();
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);

                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class DownloadWebContent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result ="";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }
    }
}
