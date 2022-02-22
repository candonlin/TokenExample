package com.denny.tokensample;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar = null;
    private ProgressDialog dialog = null;
    private Utils utils = null;
    private String Token = "";
    private String APIUrl = "http://192.168.0.42/WebApi_JWT/api/";
    private EditText et_ID,et_Word,et_Account,et_Password,et_Count;
    private Button btn_ID,btn_Word,btn_Member,btn_Next,btn_Count,btn_GetToken;
    private TextView tv_ID,tv_Word,tv_Member,tv_Count,tv_CountTitle,tv_ProgressMsg;
    private ArrayList<Integer> intArrays = new ArrayList<Integer>();
    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utils = new Utils(this, getApplicationContext());
        init();
    }

    public void init(){
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.setVisibility(View.GONE);
//        dialog = new ProgressDialog(this);

        et_ID = (EditText)findViewById(R.id.et_ID);
        et_Word = (EditText)findViewById(R.id.et_Word);
        et_Account = (EditText)findViewById(R.id.et_Account);
        et_Password = (EditText)findViewById(R.id.et_Password);
        et_Count = (EditText)findViewById(R.id.et_Count);

        btn_ID = (Button)findViewById(R.id.btn_ID);
        btn_Word = (Button)findViewById(R.id.btn_Word);
        btn_Member = (Button)findViewById(R.id.btn_Member);
        btn_Next = (Button)findViewById(R.id.btn_Next);
        btn_Count = (Button)findViewById(R.id.btn_Count);
        btn_GetToken = (Button)findViewById(R.id.btn_GetToken);

        tv_ID = (TextView)findViewById(R.id.tv_ID);
        tv_Word = (TextView)findViewById(R.id.tv_Word);
        tv_Member = (TextView)findViewById(R.id.tv_Member);
        tv_Count = (TextView)findViewById(R.id.tv_Count);
        tv_CountTitle = (TextView)findViewById(R.id.tv_CountTitle);
        tv_ProgressMsg = (TextView)findViewById(R.id.tv_ProgressMsg);
        tv_ProgressMsg.setVisibility(View.GONE);

        btn_ID.setOnClickListener(onClickListener);
        btn_Word.setOnClickListener(onClickListener);
        btn_Member.setOnClickListener(onClickListener);
        btn_Next.setOnClickListener(onClickListener);
        btn_Count.setOnClickListener(onClickListener);
        btn_GetToken.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_ID:
                    if (et_ID.getText().toString().trim().isEmpty()){
                        utils.basicErrorDialog("警告","請輸入ID!!!");
                        return;
                    }
//                    dialog.setMessage("資料讀取中。。。");
                    tv_ProgressMsg.setText("資料讀取中。。。");
                    getID(et_ID.getText().toString().trim());
                    break;
                case R.id.btn_Word:
                    if (et_Word.getText().toString().trim().isEmpty()){
                        utils.basicErrorDialog("警告","請輸入英文字!!!");
                        return;
                    }
//                    dialog.setMessage("資料讀取中。。。");
                    tv_ProgressMsg.setText("資料讀取中。。。");
                    getWord(et_Word.getText().toString().trim());
                    break;
                case R.id.btn_Member:
                    if (et_Account.getText().toString().trim().isEmpty()){
                        utils.basicErrorDialog("警告","請輸入帳號!!!");
                        return;
                    }
//                    dialog.setMessage("資料核對中。。。");
                    tv_ProgressMsg.setText("資料核對中。。。");
                    if (et_Password.getText().toString().trim().isEmpty()){
                        utils.basicErrorDialog("警告","請輸入密碼!!!");
                        return;
                    }
                    getMember(et_Account.getText().toString().trim(),et_Password.getText().toString().trim());
                    break;
                case R.id.btn_Next:
                    if (et_Count.getText().toString().trim().isEmpty()){
                        utils.basicErrorDialog("警告","請輸入數量!!!");
                        return;
                    }
                    intArrays.add(Integer.parseInt(et_Count.getText().toString().trim()));
                    et_Count.setText("");
                    tv_CountTitle.setText("輸入數量("+intArrays.size()+")");
                    break;
                case R.id.btn_Count:
//                    dialog.setMessage("資料處理中。。。");
                    tv_ProgressMsg.setText("資料處理中。。。");
                    if (et_Count.getText().toString().trim().isEmpty() && intArrays.size() < 1){
                        utils.basicErrorDialog("警告","請輸入數量!!!");
                        return;
                    }
                    if (!et_Count.getText().toString().trim().isEmpty()){
                        intArrays.add(Integer.parseInt(et_Count.getText().toString().trim()));
                    }
                    getCount();
                    break;
                case R.id.btn_GetToken:
                    if (et_Account.getText().toString().trim().isEmpty()){
                        utils.basicErrorDialog("警告","請輸入帳號!!!");
                        return;
                    }
//                    dialog.setMessage("資料核對中。。。");
                    tv_ProgressMsg.setText("資料核對中。。。");
                    if (et_Password.getText().toString().trim().isEmpty()){
                        utils.basicErrorDialog("警告","請輸入密碼!!!");
                        return;
                    }
                    getToken(et_Account.getText().toString().trim(),et_Password.getText().toString().trim());
                    break;
            }
        }
    };

    public void getID(String id){
//        dialog.show();
        progressBar.setVisibility(View.VISIBLE);
        tv_ProgressMsg.setVisibility(View.VISIBLE);
        String url = APIUrl + "values/Get1/ID/" + id;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + Token).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("data",e.toString());
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                message.what = 10;
                Bundle bundle = new Bundle();
                if (response.isSuccessful()){
                    bundle.putInt("status",0);
                }else {
                    bundle.putInt("status",1);
                }
                bundle.putString("data",response.body().string());
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
    }

    public void getWord(String word){
//        dialog.show();
        progressBar.setVisibility(View.VISIBLE);
        tv_ProgressMsg.setVisibility(View.VISIBLE);
        String url = APIUrl + "values/Get2/name/" + word;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + Token).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("data",e.toString());
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                message.what = 11;
                Bundle bundle = new Bundle();
                if (response.isSuccessful()){
                    bundle.putInt("status",0);
                }else {
                    bundle.putInt("status",1);
                }
                bundle.putString("data",response.body().string());
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
    }

    public void getToken(String account,String password){
        progressBar.setVisibility(View.VISIBLE);
        tv_ProgressMsg.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Name",account);
            jsonObject.put("Pwd",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("getToken",jsonObject.toString());
        String url = APIUrl + "LoginToken";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("data",e.toString());
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                message.what = 14;
                Bundle bundle = new Bundle();
                if (response.isSuccessful()){
                    bundle.putInt("status",0);
                }else {
                    bundle.putInt("status",1);
                }
                bundle.putString("data",response.body().string());
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
    }

    public void getMember(String account,String password){
//        dialog.show();
        progressBar.setVisibility(View.VISIBLE);
        tv_ProgressMsg.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Name",account);
            jsonObject.put("Pwd",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("getMember",jsonObject.toString());
        String url = APIUrl + "values/Post1";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + Token).post(formBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("data",e.toString());
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                message.what = 12;
                Bundle bundle = new Bundle();
                if (response.isSuccessful()){
                    bundle.putInt("status",0);
                }else {
                    bundle.putInt("status",1);
                }
                bundle.putString("data",response.body().string());
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
    }

    public void getCount(){
//        dialog.show();
        progressBar.setVisibility(View.VISIBLE);
        tv_ProgressMsg.setVisibility(View.VISIBLE);
        JSONArray jsonDataArray = new JSONArray();
        for (int value:intArrays) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Qty", (float)value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonDataArray.put(jsonObject);
        }
        Log.e("getCount",jsonDataArray.toString());
        String url = APIUrl + "values/Post2";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = RequestBody.create(JSON, jsonDataArray.toString());
        Request request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + Token).post(formBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("data",e.toString());
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                message.what = 13;
                Bundle bundle = new Bundle();
                if (response.isSuccessful()){
                    bundle.putInt("status",0);
                }else {
                    bundle.putInt("status",1);
                }
                bundle.putString("data",response.body().string());
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.getData().getString("data");
            int status = msg.getData().getInt("status");
            if (status == 1){
                Gson gson = new Gson();
                ErrorUnits errorUnits = gson.fromJson(data,ErrorUnits.class);
                data = errorUnits.getMessage();
            }
            if (msg.what == 0){
                utils.basicErrorDialog("警告",data);
            }else if (msg.what == 10){
                tv_ID.setText(data);
                et_ID.setText("");
            }else if (msg.what == 11){
                StringBuffer buffer = new StringBuffer();
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    Log.e("json","資料長度："+jsonArray);
                    Type type = new TypeToken<List<GetWordUnits>>(){}.getType();
                    Gson gson = new Gson();
                    List<GetWordUnits> wordsList = gson.fromJson(jsonArray.toString(),type);
                    Log.e("json","資料長度(1)："+wordsList);
                    for (GetWordUnits word:wordsList){
                        buffer.append("品名："+word.getName()+"，");
                        buffer.append("編號："+word.getNo()+"，");
                        buffer.append("數量："+word.getQty()+"\r\n");
                    }
                    tv_Word.setText(buffer.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status == 0) {
                    tv_Word.setText(buffer.toString());
                }else {
                    tv_Word.setText(data);
                }
                et_Word.setText("");
            }else if (msg.what == 12){
                tv_Member.setText(data);
                et_Account.setText("");
                et_Password.setText("");
            }else if (msg.what == 13){
                tv_Count.setText(data);
                et_Count.setText("");
                intArrays.clear();
                tv_CountTitle.setText("輸入數量(可多筆)");
            }else if (msg.what == 14) {
                StringBuffer buffer = new StringBuffer();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    Log.e("json", "資料長度：" + jsonObject.length());
                    Type type = new TypeToken<LoginResult>() {
                    }.getType();
                    Gson gson = new Gson();
                    LoginResult loginrst = gson.fromJson(data, type);
                    if(loginrst.getresult())
                        Token = loginrst.getrtntoken();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            dialog.dismiss();
            progressBar.setVisibility(View.GONE);
            tv_ProgressMsg.setVisibility(View.GONE);
        }
    };
}
