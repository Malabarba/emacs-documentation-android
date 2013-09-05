package com.malabarba.emacsdocumentation;
import java.io.InputStream;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Artur
 *
 */
public class App extends Application {
    private static final String tg = "MyApp";
    private static Context mContext;
    private static Toast toast;
    private static long lastTime;
    private static long firstTime;
    private static String timeReport;
    private static boolean timing = true;
    private static boolean toastTiming = false;
    private static boolean toasting = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
    
    public static void browseUrl(String u) {browseUrl(u,true,"/:");}
    public static void browseUrl(String u, Boolean doEncode, String allow) {
        final String url = doEncode ? Uri.encode(u, allow) : u;
        i("Browsing to: " + url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(browserIntent); 
    }

    @SuppressWarnings("deprecation")
	public static String getSystemInformation() {
        return "OS Version: " + System.getProperty("os.version")
            + "\n OS Incremental: " + android.os.Build.VERSION.INCREMENTAL
            + "\n OS API Level (and Int): " + android.os.Build.VERSION.SDK + " ("+android.os.Build.VERSION.SDK+")"
            + "\n OS Codename: " + android.os.Build.VERSION.CODENAME
            + "\n OS Release: " + android.os.Build.VERSION.RELEASE
            + "\n Device: " + android.os.Build.DEVICE
            + "\n Model (Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")\n\n";
    }
    
    public static void emailIntent(String ad, int sub, int body) {
        emailIntent(ad,
                    (sub == 0)? null : string(sub),
                    (body == 0)? null : string(body));}
    public static Intent emailIntent(String ad, String sub, String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",ad, null));
        // emailIntent.setType("text/html");
        // emailIntent.putExtra(Intent.EXTRA_EMAIL, ad);
        if (sub != null) emailIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
        if (body != null) emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        // getContext().startActivity(Intent.createChooser(emailIntent, "Send Email"));
        return Intent.createChooser(emailIntent, "Send Email");
    }
    
    public static Context getContext(){
        checkContext();
        return mContext;
    }
    
    public static long startTiming(String s) {
        if (timing) {
            firstTime = lastTime = System.currentTimeMillis();
            timeReport = s + ": " + lastTime;
            return lastTime;
        }
        return -1;
    }
    public static long stepTiming(String s){
        if (timing && lastTime > 0) {
            long step = System.currentTimeMillis() - lastTime;
            timeReport += "\n" + s + ": " + step;
            lastTime += step;
            return step;
        } else if (timing) return -2;
        return -1;
    }
    public static long finishTiming(String s){
        if (timing) {
            long step = System.currentTimeMillis() - lastTime;
            timeReport += "\n" + s + ": " + step
                +"\n\nFinished Counting: " + (lastTime - firstTime);
        
            if (toastTiming) toast(timeReport);
            i(timeReport);
        
            lastTime = 0;
            timeReport = null;
        
            return lastTime - firstTime;
        }
        return -1;
    }
    public static String string(int id){
        checkContext();
        return getContext().getString(id);
    }
       
    public static InputStream contextOpenRawResources(int id){
        return getContext().getResources().openRawResource(id);
    }
    
    public static void toast(String text, Boolean isLong) {
    	if (!toasting) return;
        Integer length = Toast.LENGTH_LONG;
        if (!isLong) length = Toast.LENGTH_SHORT;

        checkContext();
        if(toast != null) toast.cancel();
        toast = Toast.makeText(getContext(), text, length);
        toast.show();
    }
    public static void toast(String text) {toast(text, true);}    
    public static void toast(Integer id) {toast(string(id), true);}    
    // TODO (98072)
    public static void dialog(String text) {toast(text, true);}    
    
    public static Integer e(String text)             {return Log.e(tg,text);}
    public static Integer e(String text, Throwable e){return Log.e(tg,text,e);}
    public static Integer d(String text)             {return Log.d(tg,text);  }
    public static Integer d(String text, Throwable e){return Log.d(tg,text,e);}
    public static Integer i(String text)             {return Log.i(tg,text);  }
    public static Integer i(String text, Throwable e){return Log.i(tg,text,e);}
    public static Integer v(String text)             {return Log.v(tg,text);  }
    public static Integer v(String text, Throwable e){return Log.v(tg,text,e);}
    
    private static void checkContext() {
        if (mContext == null) {
            e("Tried getting a context that isn't defined.");
//            throw new Exception("Context isn't defined inside App yet!");
        }
    }
}
