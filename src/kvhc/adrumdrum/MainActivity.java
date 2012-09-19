package kvhc.adrumdrum;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
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

    	r = new Runnable(){
    		
    		public void run() {
    			tv1.setText("dörp"+i);
    			i++;
    		}
    		
    	};
    	mTimer = new AndroidTimer(r,500);
        
        /*
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
        */
    	/*
        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener((android.view.View.OnClickListener) btnListener);
        */
    	mTimer.start();
        
    }
    
    private OnClickListener btnListener = new OnClickListener()
    {
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			
			
			//mTimer.start();
			/*
			if(player.IsPlaying()) {
    			player.Play();
    		} else {
    			player.Stop();
    		}
    		*/
		}
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
