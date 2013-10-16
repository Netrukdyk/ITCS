package com.bak_traukinys;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.bak_traukinys.R;

public class Control_Train extends Activity implements OnClickListener {
	
	Button Button_Control_Train_Blue;
	
	RadioGroup trainGroup;
	
	Button Button_Control_Train_Forward;
	Button Button_Control_Train_Backward;
	Button Button_Control_Train_Stop;
	//Button Button_Control_Train_AutoMode;
	
	SeekBar Seek_Speed;
	TextView DisplaySpeed;
	Button Button_switches;
	View.OnTouchListener gestureListener;
	

	// ------------------------------
	int updateRate = 200; // Kas kiek atnaujinam stebėjimo langą.	
	TextView Status_Red;
	TextView Status_Blue;
	TextView Status_Green;	
	// ------------------------------
	
	int screen_live = 1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_train);
		
		DisplaySpeed = (TextView) findViewById(R.id.text_DisplaySpeed);
		Seek_Speed = (SeekBar) findViewById(R.id.seek_speed);
		DisplaySpeed.setText("0");
		Seek_Speed.setProgress(100);
		Button_Control_Train_Blue = (RadioButton) findViewById(R.id.control_Red);		
		trainGroup = (RadioGroup) findViewById(R.id.control_Group);
		
		// ------------------------------
		Status_Red = (TextView) findViewById(R.id.element_RedStatus);
		Status_Blue = (TextView) findViewById(R.id.element_BlueStatus);
		Status_Green = (TextView) findViewById(R.id.element_GreenStatus);
		// ------------------------------
		
		Button_switches = (Button) findViewById(R.id.button_switches);
		Button_switches.setOnClickListener(this);
		
		((CompoundButton) Button_Control_Train_Blue).setChecked(true);
				
		Button_Control_Train_Forward = (Button) findViewById(R.id.control_forward);
		Button_Control_Train_Forward.setOnClickListener(this);
		Button_Control_Train_Backward = (Button) findViewById(R.id.control_backward);
		Button_Control_Train_Backward.setOnClickListener(this);
		Button_Control_Train_Stop = (Button) findViewById(R.id.control_stop);
		Button_Control_Train_Stop.setOnClickListener(this);
		//Button_Control_Train_AutoMode = (Button) findViewById(R.id.control_auto);
		//Button_Control_Train_AutoMode.setOnClickListener(this);
		
		// ------------------------------
		try {
			atnaujintiInfo();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ------------------------------
				
			trainGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, final int checkedId) {
					// Raudonas - 2131230734 - 0 -> 2
					// Mėlynas - 2131230735 -  1 -> 0
					// Žalias - 2131230736 -   2 -> 1					
					Thread a = new Thread(new Runnable() {
						
						@Override
						public void run() {
							int train_id = trainGroup.getCheckedRadioButtonId() - 2131230734;
							int train = 0;
							if(train_id == 0) train=2;
							else if(train_id == 1) train = 0;
							else if(train_id == 2) train = 1;
							Log.v("Checkbox", train+" selected");
							try {
								atnaujintiGreiti(train+"");
							} catch (Exception e) {
								Log.v("JSON", e.toString());
							}
							
						}
					});
					a.start();
					
				}
			});
		
		
		Seek_Speed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int speed = progress - 100;
				DisplaySpeed.setText(speed+"");
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				Log.v("Seek", "Pradeta");
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int speed = seekBar.getProgress() - 100;
				View radioButton = trainGroup.findViewById(trainGroup.getCheckedRadioButtonId());
				int train_id = trainGroup.indexOfChild(radioButton);
				int train = 0;
				if(train_id == 0) train=2;
				else if(train_id == 1) train = 0;
				else if(train_id == 2) train = 1;
				
				Duomenys.siustiUzklausa(train, "speed", speed);
				Log.v("Seek", "Rezultatas"+ speed);
				
			}
		});
	}
	public void atnaujintiSeek(int num) {
		Seek_Speed.setProgress(num+100);
		DisplaySpeed.setText(num+"");
	}
	public void atnaujintiGreiti(String num) throws InterruptedException, JSONException {
		final int[] single = Duomenys.gautiTraukini(num);
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(single[1]!=0) {
					//Seek_Speed.setProgress(single[1]+100);
					//DisplaySpeed.setText(single[1]+"");
				}
				
			}
		});
		
		//Log.v("Greitis","Traukinys: "+num+" Greitis: "+ single[1]);
		//Thread.sleep(500);
		//if(this.hasWindowFocus()) atnaujintiGreiti(num);
	}
	public void onClick(View v) {
		// Raudonas - 2131230729 - 0 -> 2
		// Mėlynas - 2131230730 -  1 -> 0
		// Žalias - 2131230731 -   2 -> 1
		if(v == Button_switches) {
			Intent open = new Intent(v.getContext(), Control_Switch.class);
			Log.v("Naujas langas", "Iešmų valdymo langas");
			startActivity(open);
			return;
		}

		View radioButton = trainGroup.findViewById(trainGroup.getCheckedRadioButtonId());
		int train_id = trainGroup.indexOfChild(radioButton);
		
		Log.v("Train move", train_id+"");
		int train = 0;
		if(train_id == 0) train=2;
		else if(train_id == 1) train = 0;
		else if(train_id == 2) train = 1;
		
		//Log.e("Traukinys",train_id+"");
		int value = 0;
		if(v == Button_Control_Train_Forward) {
			value = 25;
		}
		else if(v == Button_Control_Train_Backward) {
			value = -25;
		}
		else if(v == Button_Control_Train_Stop) {
			value = 0;
		}
		//else if(v == Button_Control_Train_AutoMode) {
		//	value = 0;
		//}
		
		
		Duomenys.siustiUzklausa(train, "speed", value);
		atnaujintiSeek(value);
	}
	
	// --------------------------------------
	// Mėlynas 0
	// Žalias 1
	// Raudonas 2
	public void atnaujintiInfo() throws JSONException, InterruptedException {
		final int[] blue = Duomenys.gautiTraukini("0");
		final int[] green = Duomenys.gautiTraukini("1");
		final int[] red = Duomenys.gautiTraukini("2");
		blue[1] = blue[0]==1? blue[1]:0;
		green[1] = green[0]==1? green[1]:0;
		red[1] = red[0]==1? red[1]:0;
		//Log.v("Trauk","B: "+blue[2]);
		//Mėlynas
		Status_Blue.setText(blue[1] + " ("+blue[2]+"dB)"); // Greitis
		
		//Žalias
		Status_Green.setText(green[1] + " ("+green[2]+"dB)"); // Greitis
		
		//Raudonas
		Status_Red.setText(red[1] + " ("+red[2]+"dB)"); // Greitis
		
		//Log.v("JSON", blue[1]);
		//Thread.sleep(400);
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				try {
					atnaujintiInfo();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, updateRate);
		
	}
	
	// --------------------------------------
	

}
