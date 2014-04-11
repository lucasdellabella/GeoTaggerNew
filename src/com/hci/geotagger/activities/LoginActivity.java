/* 
 * LoginActivity class contains the code for the login screen
 * This validates a user's login credentials and directs them
 * to the home screen, or links new users to the Registration
 * page. 
 * 
 * Chris Loeschorn
 * Spring 2013
 */
package com.hci.geotagger.activities;

import com.actionbarsherlock.app.SherlockActivity;
import com.hci.geotagger.R;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.MyUserAccount;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AccountHandler;
import com.hci.geotagger.connectors.ReturnInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

public class LoginActivity extends SherlockActivity 
{
	
	private Button loginBtn; //log in button
	private Typeface titleFont; //font used in the title
	private Typeface font; //font used in everything but the title
	private TextView title; //the title of the application
	private EditText unameTxt; //input for user's username
	private EditText pwTxt; //input for user's password
	private AlphaAnimation titleFadeIn; //animation for the title's fade in
	private AlphaAnimation itemsFadeIn; //animation for the widgets' fade in

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		final Context c = LoginActivity.this;
		SharedPreferences app_settings = c.getSharedPreferences(Constants.LOGIN_DATAFILE, Constants.MODE_PRIVATE);

		//keyboard is hidden upon starting the application
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		//find the fonts
		titleFont = Typeface.createFromAsset(getAssets(), "steelfish rg.ttf");
		font = Typeface.createFromAsset(getAssets(), "Gravity-Book.ttf");
		
		//find the widgets
		title = (TextView) findViewById(R.id.application_title);
		unameTxt = (EditText) findViewById(R.id.login_unameTxt);
		pwTxt = (EditText) findViewById(R.id.login_pwTxt);
		loginBtn = (Button) findViewById(R.id.login_btnLogin);
		
		
		//apply fonts to widgets
		title.setTypeface(titleFont);
		unameTxt.setTypeface(font);
		loginBtn.setTypeface(font);
		
		//animation for the title's fade in
		titleFadeIn = new AlphaAnimation(0.0f, 1.0f); 
		titleFadeIn.setDuration(1200);
		title.startAnimation(titleFadeIn);
		
		//animation for the EditTexts' and button's fade in
		itemsFadeIn = new AlphaAnimation(0.0f, 1.0f);
		itemsFadeIn.setDuration(2400);
		unameTxt.startAnimation(itemsFadeIn);
		pwTxt.startAnimation(itemsFadeIn);
		loginBtn.startAnimation(itemsFadeIn);
		
		//First Check user session see if a user is already logged in
		if (UserSession.LOGGED_IN == true && UserSession.CURRENT_USER != null)
		{
			//already logged in, redirect to home
			// create link to home screen
			Intent i = new Intent(getBaseContext(), HomeActivity.class);
			startActivity(i);
		}
		//next check shared preferences to see if there is a current login
		else if (app_settings.getBoolean(Constants.KEY_LOGGEDIN, false) == true)
		{
			int id = app_settings.getInt(Constants.KEY_UID, 0);
			String pass = app_settings.getString(Constants.KEY_PASS, "");
			if (id != 0 && !pass.isEmpty())
			{
				//if shared preferences login = true, and id and pw are present, login user
				Log.d("Logging In With ID", "ID: " + Integer.toString(id) + 
						"Pass: " + pass);
				new LoginTask(c).execute(Integer.toString(Constants.LOGIN_BYID), Integer.toString(id), pass);
			}
		}
		//get form controls
		//unameTxt = (EditText) findViewById(R.id.login_unameTxt);
		//pwTxt = (EditText) findViewById(R.id.login_pwTxt);
		//loginBtn = (Button) findViewById(R.id.login_btnLogin);
		//Set onClick action for login button
		loginBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				//attempt to log in user
				String uName = unameTxt.getText().toString();
				String pw = pwTxt.getText().toString();

				if (! uName.isEmpty() && ! pw.isEmpty())
				{
					//execute a new LoginTask
					new LoginTask(c).execute(Integer.toString(Constants.LOGIN_BYNAME), uName, pw); 	
				}
				else
				{
					String msg = "Please enter a Username and Password to Log In.";
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					Log.d("LoginOnClick","Username or password fields cant be empty.");
				}
			}
		});

		/*
		//Register Button Action
		regBtn = (TextView) findViewById(R.id.login_lblRegister);
		regBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// create link to home screen
				Intent j = new Intent(getBaseContext(), RegisterActivity.class);
				startActivity(j);

			}
		});
		if (Constants.LIMIT_HOME_TO_ADVENTURE)
		{
			regBtn.setVisibility(Button.INVISIBLE);
			regBtn.setEnabled(false);
		}*/
		
	}//end oncreate


	/*
	 * Login task: Asynchronous task to make the web request and get the 
	 * account object. This is called from the LoginBtn_Click handler
	 */
	class LoginTask extends AsyncTask<String, Void, ReturnInfo> 
	{
		ProgressDialog progressDialog;
		Context c;
		//get context from the parent activity for opening dialogs
		public LoginTask(Context context)
		{
			this.c = context;		
		}

		//Setup progress dialog before execution
		@Override
		public void onPreExecute() 
		{
			progressDialog = new ProgressDialog(c);
			progressDialog.setMessage("Loading..Please wait..");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		/*
		 * After login task is finished, get response and
		 * determine if the login was successful. If so, close dialog 
		 * and move to next activity, if not show error. 
		 */
		@Override
		protected void onPostExecute(ReturnInfo response) {
			String msg = "success";

			if (response != null && response.success) {
				if (response.object != null && response.object instanceof MyUserAccount) {
					//set the loggedin flag to true, and open the Home page
					MyUserAccount loginAccount = (MyUserAccount)response.object;
					UserSession.login(c.getApplicationContext(), loginAccount);

					Log.d("LoginPostExecute", "Login Success");
					// create link to home screen
					Intent i = new Intent(getBaseContext(), HomeActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					progressDialog.dismiss();
					Log.d("LoginPostExecute", "Login Success 2");
					finish();
					return;
				} else { msg = "Could not create User Account Object"; }
			} else { msg = "Null successful response, Logon Error."; }
			
			progressDialog.dismiss();
			//TODO under what conditions do we want to notify the user of a login error?
			//				AlertHandler alert = new AlertHandler();
			//				alert.showAlert(c, null, getString(R.string.unknown_error_try_catch));
			String msg2 = getString(R.string.unknown_error);
			Toast.makeText(getApplicationContext(), msg2, Toast.LENGTH_SHORT).show();
			Log.d("LoginPostExecute", msg);

		}//end onPostExecute

		/*
		 * Create an account handler and attempt to log in with 
		 * the provided credentials. 
		 */
		@Override
		protected ReturnInfo doInBackground(String...loginParams)
		{
			String mode = loginParams[0];
			String pw = loginParams[2];

			// detect whether id or name is being used to login

			int iMode = Integer.parseInt(mode);
			if (iMode == Constants.LOGIN_BYID) 
			{
				int id = Integer.parseInt(loginParams[1]);
				// attempt login with id and password
				AccountHandler handler = new AccountHandler(c);
				ReturnInfo response = handler.login(id, pw);
				response.print("LoginTask");
				return response;
			}
			// default is to attempt login with username
			else
			{
				// attempt login with uname and password
				AccountHandler handler = new AccountHandler(c);
				String uName = loginParams[1];
				ReturnInfo response = handler.login(uName, pw);
				if (response != null) 
				{
					response.print("LoginTask");
					return response;
				} 
				else
					return null;
			}
		}// end doInBackground
	}//end LoginTask

}//end LoginActivity
