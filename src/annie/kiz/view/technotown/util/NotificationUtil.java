package annie.kiz.view.technotown.util;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import annie.kiz.view.technotown.R;

public class NotificationUtil {
	public static NotificationManager notificationManager;
	public static Notification notification;
	public static int noti_state = 1;
	public static int SDK_INT = android.os.Build.VERSION.SDK_INT;
	
	
	
	public static void setNotification_favorite(Context context, String title, String user_explain, String des, Intent intent){
		if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){ 
			try{
				notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
				PendingIntent content = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		        Notification.Builder builder = new Notification.Builder(context)
		                .setContentIntent(content)
		                .setSmallIcon(R.drawable.icon128)
		                .setContentTitle(title)
		                .setContentText(user_explain + " " + des)
//		                .setDefaults(Notification.FLAG_AUTO_CANCEL)
//			      	    .setDefaults(Notification.DEFAULT_ALL)
		                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
		                .setTicker(user_explain + " " + des);
		        notification = builder.build();
		        notification.flags = Notification.FLAG_AUTO_CANCEL;
		        notificationManager.notify(noti_state, notification);
		        
			}catch(NullPointerException e){
			}
		}
	}
	
	
	
	
	public static void setNotification_Cancel(){
		if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
			if(notificationManager != null) notificationManager.cancel(noti_state);	
		}
    }
}
