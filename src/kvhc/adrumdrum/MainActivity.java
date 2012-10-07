package kvhc.adrumdrum;

import kvhc.gui.GUIController;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
    
	/**
	 * Listener to the menu
	 */
	public boolean onOptionsItemSelected (MenuItem item){
		switch(item.getItemId()) {
			case R.id.licenses: 
				guic.createAndShowLicensesDialog();
				return true;
			case R.id.clear_steps:
				guic.clearAllSteps();
			default:
				return super.onOptionsItemSelected(item);
		}
	}
    
}
