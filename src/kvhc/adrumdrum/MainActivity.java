package kvhc.adrumdrum;

import kvhc.gui.GUIController;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView tv1;
	private GUIController guic;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView)findViewById(R.id.textView1);
        
        guic = new GUIController(this);
    }

    public void onStop() {
    	guic.onStop();
    	
    	super.onStop();
    }
    
    public void onDestroy() {
    	guic.onDestroy();
    	
    	super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
