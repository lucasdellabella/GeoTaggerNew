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

import com.hci.geotagger.R;
import com.hci.geotagger.Objects.UserAccount;
import com.hci.geotagger.common.AlertHandler;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AccountHandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import org.json.JSONObject;

public class LoginActivity extends Activity 
{
	Button loginBtn;
	TextView regBtn;
	EditText unameTxt;
	EditText pwTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		final Context c = LoginActivity.this;
		SharedPreferences app_settings = c.getSharedPreferences(Constants.LOGIN_DATAFILE, Constants.MODE_PRIVATE);
		
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
		unameTxt = (EditText) findViewById(R.id.login_unameTxt);
		pwTxt = (EditText) findViewById(R.id.login_pwTxt);
		loginBtn = (Button) findViewById(R.id.login_btnLogin);
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
		
	}//end oncreate


	/*
	 * Login task: Asynchronous task to make the web request and get the 
	 * json account object. This is called from the LoginBtn_Click handler
	 */
	class LoginTask extends AsyncTask<String, Void, JSONObject> 
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
		protected void onPostExecute(JSONObject response) {
			
			if(response != null)
			{
				try
				{
					String returnCode = response.getString(Constants.SUCCESS);
					//if success = 1, create a user account object from the JSON returned from the database,
					//set the loggedin flag to true, and open the Home page
					if (returnCode != null)
					{
						if (Integer.parseInt(returnCode) == 1)
						{
							AccountHandler handler = new AccountHandler();
							//CREATE USER ACCOUNT OBJECT
							Log.d("LoginPostExecute", response.toString());
							UserAccount loginAccount = handler.CreateAccountFromJSON(response);
							if(loginAccount != null)
							{
								UserSession.login(c.getApplicationContext(), loginAccount);
								
								Log.d("LoginPostExecute", "Login Success");
								// create link to home screen
								Intent i = new Intent(getBaseContext(), HomeActivity.class);
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(i);
								progressDialog.dismiss();
								Log.d("LoginPostExecute", "Login Success 2");
								finish();
							}
							else
							{
								progressDialog.dismiss();
								Log.d("LoginPostExecute", "Could not create User Account Object");
								//AlertHandler alert = new AlertHandler();
								//alert.showAlert(c, null, getString(R.string.unknown_error));
								String msg = getString(R.string.unknown_error);
								Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
							}		
						}
						else
						{	
							progressDialog.dismiss();
							//invalid username or password
							String errorMsg = response.getString(Constants.ERROR_MSG);
							//AlertHandler alert = new AlertHandler();
							//alert.showAlert(c, null, getString(R.string.invalid_login));
							String msg = getString(R.string.invalid_login);
							Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
							Log.d("LoginPostExecute", "Logon error: " + errorMsg);				
						}		
					}
					else
					{	
						progressDialog.dismiss();
						String msg = getString(R.string.unknown_error);
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
						Log.d("LoginPostExecute", "Null response, Logon Error.");
					}
				}
				catch (Exception ex)
				{	
					progressDialog.dismiss();
					//parser error
					AlertHandler alert = new AlertHandler();
					alert.showAlert(c, null, getString(R.string.unknown_error));
					//debug
					Log.d("LoginPostExecute", "Parsing returned JSON object failed.");
					ex.printStackTrace();
				}			 
			}		
			else
			{
				progressDialog.dismiss();
				//parser error
				AlertHandler alert = new AlertHandler();
				alert.showAlert(c, null, getString(R.string.unknown_error));
				//debug
				Log.d("LoginPostExecute", "Parsing returned JSON object failed.");
			}
		}//end onPostExecute
		
		/*
		 * Create an account handler and attempt to log in with 
		 * the provided credentials. 
		 */
		@Override
		protected JSONObject doInBackground(String...loginParams)
		{
			String mode = loginParams[0];
			String pw = loginParams[2];

			// detect whether id or name is being used to login

			int iMode = Integer.parseInt(mode);
			if (iMode == Constants.LOGIN_BYID) 
			{
				int id = Integer.parseInt(loginParams[1]);
				// attempt login with id and password
				AccountHandler handler = new AccountHandler();
				try 
				{
					JSONObject response = handler.login(id, pw);
					Log.d("LoginTask ID", "Got response, returncode = "+ response.getString(Constants.SUCCESS));
					return response;
				} 
				catch (Exception ex)
				{
					Log.d("LoginTask ID", "Exception authenticating with ID");
					ex.printStackTrace();
					return null;
				}
			}
			// default is to attempt login with username
			else
			{
				// attempt login with uname and password
				AccountHandler handler = new AccountHandler();
				String uName = loginParams[1];
				try 
				{
					JSONObject response = handler.login(uName, pw);
					if (response != null) 
					{
						Log.d("LoginTask", "Got response, returncode = "
								+ response.getString(Constants.SUCCESS));
						return response;
					} 
					else
						return null;
				} 
				catch (Exception ex) 
				{
					Log.d("LoginTask", "Exception authenticating with Username");
					ex.printStackTrace();
					return null;
				}
			}
		}// end doInBackground
	}//end LoginTask
	
}//end LoginActivity
