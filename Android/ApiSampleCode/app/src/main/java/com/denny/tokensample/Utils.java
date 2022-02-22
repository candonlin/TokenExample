package com.denny.tokensample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {


    private Activity mActivity;
    private Context mContext;
    private ProgressDialog dialog;
    private int alertError,alertInfo;
    private SoundPool soundPool;

    public Utils(Activity activity, Context context) {
        this.mActivity=activity;
        this.mContext=context;
        //音效初始化
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        alertError = soundPool.load(mContext, R.raw.error, 1);
        alertInfo= soundPool.load(mContext, R.raw.info, 2);
    }

    //01-設定Toolbar
    public void setToolbar(Toolbar toolbar, String title){
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);
        ((AppCompatActivity)mActivity).getSupportActionBar().setHomeButtonEnabled(true); //設置返回鍵可用
        ((AppCompatActivity)mActivity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)mActivity).getSupportActionBar().setTitle(title);//標題
        toolbar.setTitleTextColor(Color.WHITE);//顏色
    }

    //02-EditText Focus 獲取焦點
    public void requestFocus(final EditText editText){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //延遲執行避免無法Focus
                editText.requestFocus();
            }}, 100);
    }

    //03-隱藏虛擬鍵盤
    public void hideKeyboard() {
        final View view = mActivity.getCurrentFocus();
        if (view != null) {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }}, 100);

        }
    }

    //04-顯示虛擬鍵盤
    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    //05-ProgressDialog 顯示
    public void progressDialog(String message){
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();

    }

    //06-ProgressDialog 取消
    public void dialogDismiss(){
        try {
            dialog.dismiss();
        }catch (Exception e){

        }
    }

    //07-錯誤震動及音效
    public void setVibrateSound(int time){
        //需加入權限- <uses-permission android:name="android.permission.VIBRATE" />
        Vibrator myVibrator = (Vibrator) mActivity.getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);

        //錯誤音效
        soundPool.play(alertError, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    //多選開窗音效
    public void infoSound(){
        //通知音效
        soundPool.play(alertInfo, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    //08-取得當天日期
    public String getNowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis()) ;
        String date = formatter.format(curDate);
        return date;
    }

    //09-取得目前時間
    public String getNowTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        Date curTime = new Date(System.currentTimeMillis()) ;
        String time=formatter.format(curTime);
        return time;
    }


    //基本錯誤 音效&震動 Dialog
    public void basicErrorDialog(String title, String message){
        setVibrateSound(400); //震動
        new AlertDialog.Builder(mActivity)
                .setTitle(title)
                .setIcon(R.drawable.ico_error)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.Enter,null)
                .show();
    }

    //基本錯誤 音效&震動 Dialog
    public void basicSuccessDialog(String title, String message){
        new AlertDialog.Builder(mActivity)
                .setTitle(title)
                .setIcon(R.drawable.success)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.Enter,null)
                .show();
    }

    //基本Dialog
    public void basicDialog(String title, String message) {
        infoSound();
        new AlertDialog.Builder(mActivity)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(message)
                .setIcon(R.drawable.ico_info)
                .setPositiveButton(R.string.Enter,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideKeyboard();
                    }
                })
                .show();
    }

    //取得 app 的版本號
    public int getVersionCode(){
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pkgInfo.versionCode;
    }

    //取得 app 的版本名
    public String getVersionName(){
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pkgInfo.versionName;
    }

    //判斷使否為Debug模式
    public Boolean isDebug(){
        if(BuildConfig.DEBUG) {
            return true;
        }else{
            return false;
        }
    }

    //取得本地asset資料夾內JSON檔文字
    public String getLocalJson(String fileName){
        try {
            InputStreamReader isr = new InputStreamReader(mActivity.getAssets().open(fileName),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            br.close();
            isr.close();
            String jsonString=builder.toString();//builder讀取JSON中的數據。
            return jsonString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //回收SoundPool資源
    public void Utils_Destroy(){
        soundPool.release();
    }

}
