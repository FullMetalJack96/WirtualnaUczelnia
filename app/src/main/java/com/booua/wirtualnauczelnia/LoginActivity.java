package com.booua.wirtualnauczelnia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import custom_font.MyEditText;
import custom_font.MyTextView;

public class LoginActivity extends Activity {



    ArrayList<HashMap<String, String>> arraylist;
    ProgressDialog mProgressDialog;

    static String DATE = "date";
    static String FROM = "from";
    static String TO = "to";
    static String SUBJECT = "subject";
    static String LECTURER = "lecturer";
    static String ROOM = "room";
    static String ADDRESS = "address";
    static String TYPE = "type";
    static String PASSFORM = "passForm";


    TextView title;
    MyTextView login;
    public MyEditText usernameTextBox;
    public MyEditText passwdTextBox;
    CheckBox rememberMe;
    public StringXORer xor = new StringXORer();
    public String usr;
    public String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        login = (MyTextView)findViewById(R.id.login);
        usernameTextBox = (MyEditText)findViewById(R.id.username);
        passwdTextBox = (MyEditText)findViewById(R.id.passwd);
        rememberMe = (CheckBox)findViewById(R.id.rememberMe);

        File rememberedUser = new File(getCacheDir(), "XOR_user_cache");
        if(rememberedUser.exists()){
            BufferedReader input;
            File file;
            try {
                file = new File(getCacheDir(), "XOR_user_cache");
                input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = input.readLine()) != null) {
                    buffer.append(line);
                }
                try {
                    JSONObject obj = new JSONObject(buffer.toString());
                    HttpUrlConnection http = new HttpUrlConnection();
                    http.execute("auto_login");
                    passwdTextBox.setText(xor.encryptDecrypt(obj.get("password").toString()));
                    usernameTextBox.setText(obj.get("usrName").toString());
                    usr = obj.get("usrName").toString();
                    password = xor.encryptDecrypt(obj.get("password").toString());

                    Log.d("My App", "login succesful");
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + buffer.toString() + "\"");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Typeface custom_fonts = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams.ttf");
        title = (TextView)findViewById(R.id.zoo);
        title.setTypeface(custom_fonts);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rememberMe.isChecked()){
                    JSONObject userDetails = new JSONObject();
                    try {

                        userDetails.put("usrName",  usernameTextBox.getText().toString());
                        userDetails.put("password",  xor.encryptDecrypt(passwdTextBox.getText().toString()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String content = userDetails.toString();
                    File file;
                    FileOutputStream outputStream;
                    try {
                        file = new File(getCacheDir(), "XOR_user_cache");
                        outputStream = new FileOutputStream(file);
                        outputStream.write(content.getBytes());
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                    HttpUrlConnection http = new HttpUrlConnection();
                    http.execute("user_login");
                    usr = usernameTextBox.getText().toString();
                    password = passwdTextBox.getText().toString();

            }
        });
    }

    class HttpUrlConnection extends AsyncTask<String, Void, String> {

        private List<String> cookies;
        private HttpsURLConnection conn;
        private final String USER_AGENT = "Mozilla/5.0";
        public String result = "asd";
        public JSONArray dateMap;

        @Override
        protected String doInBackground(String... params) {

            String url = "https://dziekanat.agh.edu.pl/Logowanie2.aspx";
            String gmail = "https://dziekanat.agh.edu.pl/PodzGodzin.aspx";

            HttpUrlConnection http = new HttpUrlConnection();
            try {

                CookieHandler.setDefault(new CookieManager());
                String page = http.GetPageContent(url);
                String postParams = http.getFormParams(page, usr , password);
                http.sendPost(url, postParams);
                result = http.GetPageContent(gmail);
                arraylist = new ArrayList<HashMap<String, String>>();
                Document doc = Jsoup.parse(result);
                Element table = doc.getElementById("ctl00_ctl00_ContentPlaceHolder_RightContentPlaceHolder_dgDane");
                dateMap = new JSONArray();

                    for (Element row : table.select("tr.gridDane")) {

                        HashMap<String, String> map = new HashMap<String, String>();
                        Elements tds = row.select("td");
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("date", tds.get(0).text());
                            dateMap.put(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        map.put("from", tds.get(1).text());
                        map.put("to", tds.get(2).text());
                        map.put("subject", tds.get(3).text());
                        map.put("lecturer", tds.get(4).text());
                        map.put("room", tds.get(5).text());
                        map.put("address", tds.get(8).text());
                        map.put("type", tds.get(9).text());
                        map.put("passForm", tds.get(10).text());

                        arraylist.add(map);
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setTitle("Please wait");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            it.putExtra("result", arraylist.toString());
            it.putExtra("dateMap", dateMap.toString());
            startActivity(it);
            mProgressDialog.dismiss();

        }

        public void sendPost(String url, String postParams) throws Exception {

            URL obj = new URL(url);
            conn = (HttpsURLConnection) obj.openConnection();

            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Host", "dziekanat.agh.edu.pl");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "pl-PL,pl;q=0.8,en-US;q=0.6,en;q=0.4");
            for (String cookie : this.cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Referer", "https://dziekanat.agh.edu.pl/PodzGodzin.aspx");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + postParams);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());

        }

        private String GetPageContent(String url) throws Exception {

            URL obj = new URL(url);
            conn = (HttpsURLConnection) obj.openConnection();

            conn.setRequestMethod("GET");

            conn.setUseCaches(false);

            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            if (cookies != null) {
                for (String cookie : this.cookies) {
                    conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
                }
            }
            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            setCookies(conn.getHeaderFields().get("Set-Cookie"));

            return response.toString();

        }

        public String getFormParams(String html, String username, String password)
                throws UnsupportedEncodingException {

            System.out.println("Extracting form's data...");

            Document doc = Jsoup.parse(html);

            Element loginform = doc.getElementById("aspnetForm");
            Elements inputElements = loginform.getElementsByTag("input");
            List<String> paramList = new ArrayList<String>();
            for (Element inputElement : inputElements) {
                String key = inputElement.attr("name");
                String value = inputElement.attr("value");

                if (key.equals("ctl00$ctl00$ContentPlaceHolder$MiddleContentPlaceHolder$txtIdent"))
                    value = username;
                else if (key.equals("ctl00$ctl00$ContentPlaceHolder$MiddleContentPlaceHolder$txtHaslo"))
                    value = password;
                paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
            }

            StringBuilder result = new StringBuilder();
            for (String param : paramList) {
                if (result.length() == 0) {
                    result.append(param);
                } else {
                    result.append("&" + param);
                }
            }
            return result.toString();
        }

        public List<String> getCookies() {
            return cookies;
        }

        public void setCookies(List<String> cookies) {
            this.cookies = cookies;
        }

    }
}
