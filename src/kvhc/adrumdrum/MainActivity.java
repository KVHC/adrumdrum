package kvhc.adrumdrum;

import java.util.ArrayList;

import kvhc.gui.GUIController;
import kvhc.player.Channel;
import kvhc.player.Player;
import kvhc.player.Song;
import kvhc.player.Sound;
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
	//private Player player;
	private GUIController guic;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView)findViewById(R.id.textView1);
        
        //player = new Player(getBaseContext(), this);
        guic = new GUIController(this);
    }

    public Object getFromR(int id){
    	return findViewById(id);
    }

    //public void setRadioButtonToActiveStep(int id) {
//    	guic.setActiveStep(id);
    //}
    
    public void onStop() {
    	super.onStop();
    	//player.Stop();
    }
    
    public void onDestroy() {
    	super.onDestroy();
    	//player.Stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
