package com.bak_traukinys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.bak_traukinys.R;

public class Main extends Activity implements OnClickListener {
	
	Button Button_Screen_Obs;
	Button Button_Screen_Control;
	Button Button_setIP;
	static String IPaddr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pagrindinis);
		
		Button_setIP = (Button) findViewById(R.id.button_setip);
		Button_setIP.setOnClickListener(this);		
		Button_Screen_Obs = (Button) findViewById(R.id.button_screen_obs);
		Button_Screen_Obs.setOnClickListener(this);
		Button_Screen_Control = (Button) findViewById(R.id.button_screen_control);
		Button_Screen_Control.setOnClickListener(this);
		
		SharedPreferences settings = getApplicationContext().getSharedPreferences("main", 0);
		IPaddr = settings.getString("IPaddr", "192.168.1.10");
				
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Duomenys.atnaujintiJson();				
				} catch (Exception e) {
					Log.v("JSON", e.toString());
				}
			}
		}).start();
		
	}
	
	public void onClick(View v) {
		if(v == Button_Screen_Obs) { // Eiti i stebėjimo langą
			Intent open = new Intent(v.getContext(), TestBox.class);
			Log.v("Naujas langas", "Atidaromas stebejimo langas");
			startActivity(open);
		}
		else if(v == Button_Screen_Control) {
			if(Duomenys.all_data != null) {
				Intent open = new Intent(v.getContext(), Control_Train.class);
				startActivity(open);
			}
			else {
				showAlert("Nėra ryšio. IP: "+IPaddr);
			}
		}
		else if(v == Button_setIP) {
			nustatytiIP(v);
		}
	}
	
	public void nustatytiIP(final View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Nurodykite IP");
		final EditText input = new EditText(this);
		input.setText(IPaddr);
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ivestas_ip = input.getText().toString();
				SharedPreferences settings = getApplicationContext().getSharedPreferences("main", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("IPaddr", ivestas_ip);
				Main.IPaddr = ivestas_ip;
				//Log.v("SetIP", "Nustatytas i: "+Pagrindinis.IPaddr);
				Duomenys.serveris = "http://"+Main.IPaddr+":8000/";
				editor.commit();
				Thread a = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Duomenys.atnaujintiJson();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Log.v("JSON", e.toString());
						}				
					}
				});
				a.start();
				
			}
		});
		builder.setNegativeButton("Uždaryti", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		builder.show();
		
		
	}
		
	public void showAlert(String message) {
		
		final AlertDialog.Builder error = new AlertDialog.Builder(this).setTitle("Klaida!").setMessage(message).setNeutralButton("Uždaryti", null);
		error.show();
	}

}
