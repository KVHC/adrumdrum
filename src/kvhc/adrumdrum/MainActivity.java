package kvhc.adrumdrum;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	

	int i = 0;
	Runnable r;
	AndroidTimer mTimer;
	TextView tv1;
		
	private Player player;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView)findViewById(R.id.textView1);
        
        
        //waitTime = 500;
        player = new Player(getBaseContext());
        
        player.SetStep(0, 0, true);
        player.SetStep(0, 3, true);
        player.SetStep(0, 7, true);
        player.SetStep(0, 11, true);
        
        player.SetStep(1, 0, true);
        player.SetStep(1, 3, true);
        player.SetStep(1, 5, true);
        player.SetStep(1, 11, true);
        
    	
        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener( btnListener);
    }
    
    private OnClickListener btnListener = new OnClickListener()
    {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(player.IsPlaying()) {
    			player.Stop();
    			tv1.setText("Not playing anymore");
    			
    		} else {
    			player.Play();
    			tv1.setText("Playing");
    		}
		}
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
