package kvhc.adrumdrum;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;


public class MainActivity extends Activity {
	

	Runnable r;
	AndroidTimer mTimer;
	TextView tv1;
	private Player player;
	
	//CheckBox cb[][] = new CheckBox[4][16];
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView)findViewById(R.id.textView1);
       
        initBars();
        
        //nåt sånt här skulle man kunna göra
        /*
        for (int chan=0;chan<4;chan++) {
        	for(int step=0;step<16;step++) {
        		String cbid = "checkbox" + "chan" + "_" + "step";
        		int resID = getResources().getIdentifier(cbid, "id", "kvhc.adrumdrum");
        		cb[chan][step] = ((CheckBox) findViewById(resID));
        	}
    	}*/
        
        
        //waitTime = 500;
        player = new Player(getBaseContext());
        
        /*
        player.SetStep(0, 0, true);
        player.SetStep(0, 3, true);
        player.SetStep(0, 7, true);
        player.SetStep(0, 11, true);
        
        player.SetStep(1, 0, true);
        player.SetStep(1, 3, true);
        player.SetStep(1, 5, true);
        player.SetStep(1, 11, true);
        */
    	
        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener( btnListener);
    }
    
    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        // LOL det här är fulkod

        int id = view.getId();
        
        if (id<2131230732) {
        	int num = id-2131230723;
        	tv1.setText("chan 0, step "+num+" "+checked);
        	player.SetStep(0, num, checked);
        	
        }
        else if (id>2131230731 && id<2131230740) {
        	int num = id-2131230732;
        	tv1.setText("chan 1, step "+num+" "+checked);
        	player.SetStep(1, num, checked);
        }
        else if (id>2131230740 && id<2131230749){
        	int num = id-2131230741;
        	tv1.setText("chan 2, step "+num+" "+checked);
        	player.SetStep(2, num, checked);
        }
        else {
        	int num = id-2131230750;
        	tv1.setText("chan 3, step "+num+" "+checked);
        	player.SetStep(3, num, checked);
        }

    }
    
    
    private OnClickListener btnListener = new OnClickListener()
    {
		public void onClick(View v) {
			if(player.IsPlaying()) {
    			player.Stop();
    			tv1.setText("Not playing anymore");
    			
    		} else {
    			player.Play();
    			tv1.setText("Playing");
    		}
		}
    };
    
    
    
    private void initBars(){
    	SeekBar bpmBar = (SeekBar)findViewById(R.id.bpmbar);
    	SeekBar panningBar = (SeekBar)findViewById(R.id.panningbar);
    	TextView bpmtext = (TextView) findViewById(R.id.bpmtext);
    	
    	panningBar.setProgress(50);
    	
    	bpmBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				player.setBPMInRange(progress);
				tv1.setText("BPM is: " + (60 + progress*6));
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				//TODO
				
			}
        });
    	
    	panningBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				if(progress >= 50){					
					player.panning(1.0, 1.0 - ((progress - 50) * 0.02));
					tv1.setText("Panned: R=1.0 L=" + (1.0 -(progress - 50) * 0.02));
				}
				else if (progress < 50){
					player.panning(1.0 - (50 - progress) * 0.02, 1.0);
					tv1.setText("Panned: R=" +((1.0 - (50 - progress) * 0.02)) + "L=1.0");
				}

				
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				//TODO
				
			}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
