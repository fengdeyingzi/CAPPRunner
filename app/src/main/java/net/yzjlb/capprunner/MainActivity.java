package net.yzjlb.capprunner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.text_info);

    }


    @Override
    protected void onResume()
    {
        // TODO: Implement this method
        super.onResume();
        String filename = "/mnt/sdcard/mythroad/libcapp.so";
        String sopath = "/data/data/"+getPackageName()+"/lib/libcapp.so";
        File file = new File(sopath);
        if(!file.exists()){
            Toast.makeText(this,"文件不存在"+sopath,0).show();
        }
        String[] list = new File("/data/data/"+getPackageName()+"/lib/").list();
        for(String item:list)
            tv.append(item.toString());
        // unZipAssets("/lib/armeabi-v7a/libcapp.so", filename);
        copyFile(sopath, filename);
        try {
            writeStreamToFile(getAssets().open("Android.mk"), new File("/mnt/sdcard/mythroad/Android.mk"));
        } catch (IOException e) {
            Log.e(TAG, "onResume: 写入Android.mk失败" );
            e.printStackTrace();
        }
        runCapp(filename);
    }



    /* A native method that is implemented by the
     * 'hello-jni' native library, which is packaged
     * with this application.
     */
    //  public native String  stringFromJNI();

    /* This is another native method declaration that is *not*
     * implemented by 'hello-jni'. This is simply to show that
     * you can declare as many native methods in your Java code
     * as you want, their implementation is searched in the
     * currently loaded native libraries only the first time
     * you call them.
     *
     * Trying to call this function will result in a
     * java.lang.UnsatisfiedLinkError exception !
     */
    //  public native String  unimplementedStringFromJNI();

    /* this is used to load the 'hello-jni' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.example.hellojni/lib/libhello-jni.so at
     * installation time by the package manager.
     */


    //运行手机C
    public void runCapp(String filename){
        Intent intent = new Intent();
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri,"application/mrc_picoc");
        ComponentName component = new ComponentName("com.xl.capp","com.xl.runC.ofToApk1.runActivity" );
        intent.setComponent(component);
        startActivity(intent);

    }

    //解压apk并安装
    public void unZipAssets(String assersFileName, String folder) {
        AssetManager assets = getAssets();
        try {
            //获取assets资源目录下的himarket.mp3,实际上是himarket.apk,为了避免被编译压缩，修改后缀名。
            InputStream stream = assets.open(assersFileName);
            if (stream == null) {
                //Log.v(TAG,"no file");
                return;
            }


            File f = new File(folder);
            if (!f.exists()) {
                f.mkdirs();
            }

            File file = new File(f, "gcc.zip");
            //创建apk文件
            file.createNewFile();
            //将资源中的文件重写到sdcard中
            //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
            writeStreamToFile(stream, file);
            //安装apk
            //<uses-permission android:name="android.permission.INSTALL_PACKAGES" />
            //installApk(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this,e.toString(),0).show();
            // error = e.getMessage();
        }
    }

    private void writeStreamToFile(InputStream stream, File file) {
        try {
            //
            OutputStream output = null;
            try {
                output = new FileOutputStream(file);
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                try {
                    final byte[] buffer = new byte[2048 * 10];
                    int read;

                    while ((read = stream.read(buffer)) != -1)
                        output.write(buffer, 0, read);

                    output.flush();
                } finally {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "writeStreamToFile: "+e.getMessage() );
            }
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "writeStreamToFile: "+e.getMessage() );
            }
        }
    }

    /** 
          *  复制单个文件 
          *  @param  oldPath  String  原文件路径  如：c:/fqf.txt 
          *  @param  newPath  String  复制后路径  如：f:/fqf.txt 
          *  @return  boolean 
          */
    public static void copyFile(String oldPath,String newPath)
    {
        try
        {
//           int  bytesum  =  0; 
            int byteread = 0;
            File oldfile = new File(oldPath);
            if(oldfile.isFile())
            {//文件存在时 
                InputStream inStream=new FileInputStream(oldPath);//读入原文件 
                FileOutputStream out = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
//               int  length; 
                while((byteread=inStream.read(buffer))!=-1)
                {
//                   bytesum  +=  byteread;  //字节数  文件大小 
//                   System.out.println(bytesum); 
                    out.write(buffer, 0, byteread);
                }
                inStream.close();

                out.flush();
                out.close();
            }
        }
        catch(Exception e)
        {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }
//    /**
//     * A native method that is implemented by the 'native-lib' native library,
//     * which is packaged with this application.
//     */
//    public native String stringFromJNI();
//
//    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
}
