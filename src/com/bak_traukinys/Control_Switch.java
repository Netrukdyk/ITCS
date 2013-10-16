package com.bak_traukinys;
 

import java.io.IOException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;
import com.example.bak_traukinys.R;

public class Control_Switch extends Activity implements OnClickListener {
	
	private static final int iupdateRate = 200;
	Spinner Switch_Num;
	Button Button_Switch;
	Button Button_trains;
	LinearLayout layout_switches;
	Drawable drawable_main;

	ToggleButton tgl1, tgl2, tgl3, tgl4, tgl5, tgl6, tgl7, tgl8, tgl9;
	
	Boolean tglUpdate = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_control_switch);
		
		Button_trains = (Button) findViewById(R.id.button_trains);
		
		CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
		    @Override
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        String key = null;
		        switch(buttonView.getId()) {
		            case R.id.toggleButton1:
		                key = "F1.0";
		                break;
		            case R.id.toggleButton2:
		                key = "F0.0";
		                break;
		            case R.id.toggleButton3:
		                key = "F1.1";
		                break;
		            case R.id.toggleButton4:
		                key = "F0.1";
		                break;
		            case R.id.toggleButton5:
		                key = "F2.1";
		                break;
		            case R.id.toggleButton6:
		                key = "F3.0";
		                break;
		            case R.id.toggleButton7:
		                key = "F4.0";
		                break;
		            case R.id.toggleButton8:
		                key = "F3.1";
		                break;
		            case R.id.toggleButton9:
		                key = "F2.0";
		                break;
		            default:
		                return;
		        }
		        
		        // Save the state here using key
		        
		        try {
		        	if(!tglUpdate) {
		        		Log.v("Toggle", key+"="+isChecked);
		        		Duomenys.keistiIesma(key, isChecked);
		        	}
		        	tglUpdate = false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
				
		    }
		};
		
		tgl1 = (ToggleButton) findViewById(R.id.toggleButton1);
		tgl2 = (ToggleButton) findViewById(R.id.toggleButton2);
		tgl3 = (ToggleButton) findViewById(R.id.toggleButton3);
		tgl4 = (ToggleButton) findViewById(R.id.toggleButton4);
		tgl5 = (ToggleButton) findViewById(R.id.toggleButton5);
		tgl6 = (ToggleButton) findViewById(R.id.toggleButton6);
		tgl7 = (ToggleButton) findViewById(R.id.toggleButton7);
		tgl8 = (ToggleButton) findViewById(R.id.toggleButton8);
		tgl9 = (ToggleButton) findViewById(R.id.toggleButton9);
		
		tgl1.setOnCheckedChangeListener(listener);
		tgl2.setOnCheckedChangeListener(listener);
		tgl3.setOnCheckedChangeListener(listener);
		tgl4.setOnCheckedChangeListener(listener);
		tgl5.setOnCheckedChangeListener(listener);
		tgl6.setOnCheckedChangeListener(listener);
		tgl7.setOnCheckedChangeListener(listener);
		tgl8.setOnCheckedChangeListener(listener);
		tgl9.setOnCheckedChangeListener(listener);
		
		Button_trains.setOnClickListener(this);
		
		try {
			atnaujintiIesmus();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onClick(View v) {
		if(v == Button_trains) {
			Intent open = new Intent(v.getContext(), Control_Train.class);
			Log.v("Naujas langas", "Traukinių valdymo langas");
			startActivity(open);
		}
	}
	
	public void atnaujintiIesmus() throws JSONException, InterruptedException {
		Boolean iesmoBusena;
		
		if(tgl1.isChecked() != (iesmoBusena = Duomenys.gautiIesmaBool("F1.0"))) {tglUpdate = true; tgl1.setChecked(iesmoBusena);}
		if(tgl2.isChecked() != (iesmoBusena = Duomenys.gautiIesmaBool("F0.0"))) {tglUpdate = true; tgl2.setChecked(iesmoBusena);}
		if(tgl3.isChecked() != (iesmoBusena = Duomenys.gautiIesmaBool("F1.1"))) {tglUpdate = true; tgl3.setChecked(iesmoBusena);}
		if(tgl4.isChecked() != (iesmoBusena = Duomenys.gautiIesmaBool("F0.1"))) {tglUpdate = true; tgl4.setChecked(iesmoBusena);}
		if(tgl5.isChecked() != (iesmoBusena = Duomenys.gautiIesmaBool("F2.1"))) {tglUpdate = true; tgl5.setChecked(iesmoBusena);}
		if(tgl6.isChecked() != (iesmoBusena = Duomenys.gautiIesmaBool("F3.0"))) {tglUpdate = true; tgl6.setChecked(iesmoBusena);}
		if(tgl7.isChecked() != (iesmoBusena = Duomenys.gautiIesmaBool("F4.0"))) {tglUpdate = true; tgl7.setChecked(iesmoBusena);}
		if(tgl8.isChecked() != (iesmoBusena = Duomenys.gautiIesmaBool("F3.1"))) {tglUpdate = true; tgl8.setChecked(iesmoBusena);}
		if(tgl9.isChecked() != (iesmoBusena = Duomenys.gautiIesmaBool("F2.0"))) {tglUpdate = true; tgl9.setChecked(iesmoBusena);}
		
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				try {
					atnaujintiIesmus();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}, iupdateRate);
		
	}

}
