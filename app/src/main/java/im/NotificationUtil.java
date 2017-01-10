package im;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.example.jason.achievements.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import bean.RealmConversation;
import io.realm.RealmResults;

/**
 * Created by Jason on 2017/1/4.
 */

public class NotificationUtil {

    private static List<String> notificationTagList=new LinkedList<>();

    public static void addTag(String tag){
        if(!notificationTagList.contains(tag)){
            notificationTagList.add(tag);
        }
    }

    public static void addAllTag(RealmResults<RealmConversation> results){
        for(int i=0;i<results.size();i++){
            notificationTagList.add(results.get(i).getId());
        }
    }

    public static void removeAllTag(RealmResults<RealmConversation> results){
        for(int i=0;i<results.size();i++){
            notificationTagList.remove(results.get(i).getId());
        }
    }

    public static void removeTag(String tag){
        notificationTagList.remove(tag);
    }

    public static boolean isShowNotification(String tag){
        return !notificationTagList.contains(tag);
    }

    public static void showNotification(Context context,String title,String content,Intent intent){
        intent.setFlags(0);
        int notificationId=(new Random()).nextInt();
        PendingIntent contentIntent=PendingIntent.getBroadcast(context,notificationId,intent,0);
        NotificationCompat.Builder builder=
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentText(content);
        NotificationManager manager=
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=builder.build();
        manager.notify(notificationId,notification);
    }

}
