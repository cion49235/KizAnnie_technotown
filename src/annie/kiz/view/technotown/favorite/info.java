//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package annie.kiz.view.technotown.favorite;

import android.os.Bundle;
import annie.kiz.view.technotown.R;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class info extends SherlockActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_info);
      //액션바백버튼가져오기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
       
    }
      //빽백키 상단액션바
 	   @Override
 	    public boolean onOptionsItemSelected(MenuItem item) {
 	    switch (item.getItemId()) {
 	        case android.R.id.home:
 	            onBackPressed();
 	            return true;
 	        default:
 	            return super.onOptionsItemSelected(item);
 	    }
 	    
 	    
 	   }
    
}
