package com.bak_traukinys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Duomenys {
	
	static int atnaujinimo_daznis = 200; // Kas kiek atnaujinam JSON
	static int prisijungimo_timeout = 2000;
	
	static String serveris = "http://"+Main.IPaddr+":8000/";
	static JSONObject all_data = null;
	static JSONObject traukiniai = null;
	static JSONObject iesmai = null;
	
	public static void keistiIesma(final String num, final Boolean state) throws IOException, JSONException {
		new Thread(new Runnable() {
			public void run() {	
				try {
					int value = (state) ? 1 : 0 ;
					URL url = new URL(serveris + "?switch="+num+"&state="+value);
					//Log.v("Sent to Server", url.toString());
					url.openStream();
					
					JSONObject iesmas = (JSONObject) Duomenys.iesmai.get(num);
					iesmas.put("state", value);
					
					//Log.v("BUG", iesmai.get(num)+"");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}).start();
	}
	
	public static void siustiUzklausa(final int tr_num, final String action, final int value) {		
			new Thread(new Runnable() {
				public void run() {
					try {						
						URL url = new URL(Duomenys.serveris + "?tr="+tr_num+"&"+action+"="+value);
						Log.v("Uzklausa", url.toString());
						url.openStream();
						//URLConnection c = url.openConnection();
						//c.connect(); 
						//c.setConnectTimeout(10000);
						//c.setReadTimeout(10000);						
					}
					catch (Exception e) {
						Log.e("Klaida", e.toString());
					}
				}
			}).start();		
	}

	public static int[] gautiTraukini(String num) throws JSONException {
		JSONObject traukinys = (JSONObject) Duomenys.traukiniai.get(num);
		int[] single = new int[3];
		single[0] = traukinys.getBoolean("active")? 1:0;
		single[1] = traukinys.getInt("speed");
		//Log.v("Greitis", ""+single[1]);
		single[2] = traukinys.getInt("signal");
		return single;
	}
	public static int gautiIesma(String num) throws JSONException {
		JSONObject iesmas = (JSONObject) Duomenys.iesmai.get(num);
		return iesmas.getInt("state");
	}
	
	public static boolean gautiIesmaBool(String num) throws JSONException {
		JSONObject iesmas = (JSONObject) Duomenys.iesmai.get(num);
		return (iesmas.getInt("state")>0) ? true : false;
	}
	
	public static void atnaujintiJson() {
		Thread a = new Thread(new Runnable() {
			InputStream stream;
			public void run() {
				try {
					//*
					//Log.v("IP","Dabartinis: " + Duomenys.serveris);
					URL url = null;
					try {
						url = new URL(serveris + "?getstatus");
					}
					catch(Exception e) {
						url = new URL("0.0.0.0" + "?getstatus");
					}
					URLConnection con = url.openConnection();
					con.setReadTimeout(1000);
					con.setConnectTimeout(1000);
					con.connect();
					stream = con.getInputStream();
					
					String content = "";
					String line = "";
					BufferedReader br = new BufferedReader(new InputStreamReader(stream));
					while ((line = br.readLine()) != null) {
						content += line;
					}
					stream.close();
					
					//Duomenys.all_data = null;
					Duomenys.all_data = new JSONObject(content);			
					JSONObject traukiniai = (JSONObject) all_data.get("traukiniai");
					//Duomenys.traukiniai = null;
					Duomenys.traukiniai = traukiniai;
					
					JSONObject iesmai = (JSONObject) all_data.get("iesmai");
					//Duomenys.iesmai = null;
					Duomenys.iesmai = iesmai;				
					//Log.v("JSON", "Updated");
					Thread.sleep(atnaujinimo_daznis);
					//run();
					
				} catch (Exception e) {
					try {
						Log.v("JSON", "Prisijungimas nepavyko");
						Thread.sleep(prisijungimo_timeout);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						Log.e("JSON", e.toString());
					}
					
					// TODO Auto-generated catch block
					Log.e("JSON", e.toString());
					//e.printStackTrace();
					//Pagrindinis.showAlert(this, "Negaunam JSON.");
				}
				finally {
				    try{if(stream != null)stream.close();}catch(Exception squish){}
				}
				atnaujintiJson();
			}});
			a.start();		
	}
	
}
