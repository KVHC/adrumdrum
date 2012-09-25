package kvhc.adrumdrum;

import kvhc.gui.GUIController;
import kvhc.player.Player;
import kvhc.util.AndroidTimer;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;


public class MainActivity extends Activity {
	
	
	TextView tv1;
	RadioGroup activeSteps;
	private Player player;
	private GUIController guic;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView)findViewById(R.id.textView1);
        activeSteps = (RadioGroup)findViewById(R.id.radiogroup);
        
        player = new Player(getBaseContext(), this);
        guic = new GUIController(player, this);
    
    }
    
    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        // LOL det här är fulkod

        int id = view.getId();
        int first = ((CheckBox)findViewById(R.id.checkbox0_0)).getId();
        
        if (id<(first+9)) {
        	int num = id-first;
        	tv1.setText("chan 0, step "+num+" "+checked);
        	player.SetStep(0, num, checked);
        	
        }
        else if (id>(first+8) && id<(first+17)) {
        	int num = id-(first+9);
        	tv1.setText("chan 1, step "+num+" "+checked);
        	player.SetStep(1, num, checked);
        }
        else if (id>(first+17) && id<(first+26)){
        	int num = id-(first+18);
        	tv1.setText("chan 2, step "+num+" "+checked);
        	player.SetStep(2, num, checked);
        }
        else {
        	int num = id-(first+27);
        	tv1.setText("chan 3, step "+num+" "+checked);
        	player.SetStep(3, num, checked);
        }

    }
    

    public Object getFromR(int id){
    	return findViewById(id);
    }
    
    public void setRadioButtonToActiveStep(int n) {
    	activeSteps.check(activeSteps.getId()+n+1);
    }
    
    public void onStop() {
    	player.Stop();
    	super.onStop();
    	super.onDestroy();
    }
    
    public void onDestroy() {
    	player.Stop();
    	super.onStop();
    	super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
