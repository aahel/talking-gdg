package com.example.aahelguha.talking;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import stanford.androidlib.SimpleActivity;

import static android.R.id.list;


public class MainActivity extends SimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



String text;
    TextToSpeech tts;TextView textView;
    public void startspeech(View view) {

        tts=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        ConvertTextToSpeech();
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });




    }

    private void ConvertTextToSpeech() {


        if(text==null||"".equals(text))
        {
            text = "Content not available";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

        }else
            tts.speak(text+"is saved", TextToSpeech.QUEUE_FLUSH, null);

    }


    public void fetchjson(View view) {


        EditText editText=(EditText)findViewById(R.id.title);
        String str=editText.getText().toString();
        char upper=Character.toUpperCase(str.charAt(0));
        str=upper+str.substring(1);
        String url="https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&exintro=&titles=";
        str=url+str;

       Log.d("url",str);

        try {


            Ion.with(this)
                    .load(str)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                           

                                parseJSon(result);



                        }

                    });

        }
        catch(Exception e)
        {
            textView.setText("Page Not Found");
        }



    }

    public void parseJSon(JsonObject result)
    {
        JsonObject jsonObject = result.getAsJsonObject("query");
        JsonObject jsonObj=jsonObject.getAsJsonObject("pages");


        String pageid="";

        Map<String, Object> attributes = new HashMap<String, Object>();
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObj.entrySet();
        for(Map.Entry<String,JsonElement> entry : entrySet){
             pageid=entry.getKey();
            break;
        }
         JsonObject jsonOb=jsonObj.getAsJsonObject(pageid);



            text=android.text.Html.fromHtml(jsonOb.get("extract").getAsString()).toString();

       // Log.d("josndata",str);

         textView=(TextView) findViewById(R.id.et);
        textView.setText(text);






        }


    public void speechstop(View view) {

        tts.stop();

    }
}
