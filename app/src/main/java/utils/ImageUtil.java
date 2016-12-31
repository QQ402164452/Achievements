package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jason on 2016/12/27.
 */

public class ImageUtil {

    public ImageUtil(){

    }

    public static File compressImage(String imagePath,int displayWidth,int displayHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;//只测量image 不加载到内存
        BitmapFactory.decodeFile(imagePath,options);//测量image

        options.inPreferredConfig= Bitmap.Config.RGB_565;//设置565编码格式 省内存
        options.inSampleSize=calculateInSampleSize(options,displayWidth,displayHeight);//获取压缩比 根据当前屏幕宽高去压缩图片

        options.inJustDecodeBounds=false;
        Bitmap bitmap=BitmapFactory.decodeFile(imagePath,options);//按照Options配置去加载图片到内存

        ByteArrayOutputStream out=new ByteArrayOutputStream();//字节流输出
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,out);//压缩成JPEG格式 压缩像素质量为50%

        String fileName=imagePath.substring(imagePath.lastIndexOf("/")+1,imagePath.lastIndexOf("."));//获取文件名称
        File outFile=new File("/storage/emulated/0/PhotoPickTemp",fileName+"_temp.jpeg");//创建压缩后的image文件
        try {
            if(!outFile.exists()){//判断新文件是否存在
                if(outFile.createNewFile()){//判断创建新文件是否成功
                    FileOutputStream fos=new FileOutputStream(outFile);//创建一个文件输出流
                    byte[] bytes=out.toByteArray();//字节数组
                    int count=bytes.length;//字节数组的长度
                    fos.write(bytes,0,count);//写到文件中
                    fos.close();//关闭流
                    out.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outFile;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){//计算图片的压缩比
        final int height=options.outHeight;//图片的高度
        final int width=options.outWidth;//图片的宽度 单位1px 即像素点

        int inSampleSize=1;//压缩比

        if(height>reqHeight||width>reqWidth){
            final int halfHeight=height/2;
            final int halfWidth=width/2;
            while ((halfHeight/inSampleSize)>=reqHeight
                    &&(halfWidth/inSampleSize)>=reqWidth){
                inSampleSize*=2;
            }
        }
        return inSampleSize;
    }
}
