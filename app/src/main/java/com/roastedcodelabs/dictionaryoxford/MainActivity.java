package com.roastedcodelabs.dictionaryoxford;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
TextView te;
EditText tv;
FloatingActionButton fab;
Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        te = (TextView) findViewById(R.id.editText);
          tv = (EditText) findViewById(R.id.textView);

    }

    private String inflections() {
        final String language = "en";

         final String word = tv.getText().toString();
        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }

    public void clicked(View view) {
        new CallbackTask().execute(inflections());


    }

    public void clicked1(View view) {

        new CallbackTask().execute(inflections());

    }


    //in android calling network requests on the main thread forbidden by default
    //create class to do async job
    private class CallbackTask extends AsyncTask<String, Integer, ArrayList<DictinoryData>> {
        DictinoryData d1;
      public   MediaPlayer mediaPlayer= new MediaPlayer();
        @Override
        protected ArrayList<DictinoryData> doInBackground(String... params) {

            //TODO: replace with your own app id and app key
            final String app_id = "adc0285b";
            final String app_key = "8135a43940bb5877b79b8c127bc1c99c";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id",app_id);
                urlConnection.setRequestProperty("app_key",app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
urlConnection.disconnect();
                /////json parse

                String def="eror";
                ArrayList<DictinoryData> dictinoryData=new ArrayList<>();
                try {
                    JSONObject js = new JSONObject(stringBuilder.toString());
                    JSONArray results = js.getJSONArray("results");

                    JSONObject lentries = results.getJSONObject(0);
                    //add
                    String id= (String) lentries.get("id");
                    JSONArray la = lentries.getJSONArray("lexicalEntries");

                    JSONObject entries = la.getJSONObject(0);
                    //retriving url
                    JSONArray urls =entries.getJSONArray(  "pronunciations");

                    JSONObject get_url = urls.getJSONObject(0);
                    String audio_url= get_url.getString("audioFile");

                    String Deriv="bhai ja";

                 /*  JSONArray derivate = entries.getJSONArray("derivatives");
                    JSONObject jobjDer= derivate.getJSONObject(0);
                    //add
                    String Deriv = jobjDer.getString("text");*/

                    JSONArray e = entries.getJSONArray("entries");

                    JSONObject senses = e.getJSONObject(0);
                    JSONArray s = senses.getJSONArray("senses");//s==j4
                    for(int i=0;i<s.length();i++) {
                        JSONObject d = s.getJSONObject(i);
                        JSONArray de = d.getJSONArray("definitions");

                        String mean= de.getString(0);

                        JSONArray examp= d.getJSONArray("examples");
                        ArrayList<String> expary = new ArrayList<>();
                        for(int j=0;j<examp.length();j++)
                        {
                            JSONObject text = examp.getJSONObject(j);
                            expary.add(text.getString("text"));

                        }
                        dictinoryData.add(new DictinoryData(id,Deriv,mean,expary,audio_url));


                    }


                   ;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

////json parse


                return dictinoryData;

            }
            catch (Exception e) {
                e.printStackTrace();
                ArrayList<DictinoryData> ki = null;
            return ki;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<DictinoryData> arrayList) {
            super.onPostExecute(arrayList);

          if(arrayList.size()!=0||arrayList!=null) {
             d1 = arrayList.get(0);
             String se= d1.getMean_wrd();
              te.setText(d1.getMean_wrd());

              fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
              fab.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                     // mediaPlayer.start();


                      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                      try {
                          mediaPlayer.setDataSource(d1.getAudio_url());
                          mediaPlayer.prepare();
                          mediaPlayer.start();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }

                  }
              });


          }else {
              te.setText("word not found");

          }

        }
    }




}