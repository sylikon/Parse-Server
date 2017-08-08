/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

  EditText editTextUsername;
  EditText editTextPassword;
  Button buttonLogin;
  TextView textViewLogin;
  RelativeLayout relativeLayout;

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){

      addUser(view);

    }
    return false;
  }

  @Override
  public void onClick(View view) {


    if(view.getId() == R.id.relativeLayout){

      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    editTextUsername = (EditText)findViewById(R.id.editTextUsername);
    editTextPassword = (EditText)findViewById(R.id.editTextPassword);
    buttonLogin = (Button)findViewById(R.id.buttonLogin);
    textViewLogin = (TextView)findViewById(R.id.textViewLogin);
    editTextPassword.setOnKeyListener(this);
    relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);

    relativeLayout.setOnClickListener(this);

    if(ParseUser.getCurrentUser() != null){

        changeStage();

    }



    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  public void createNewUser(String username, String password){

    ParseUser user = new ParseUser();
    user.setUsername(username);
    user.setPassword(password);

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {

        if(e == null){

          Log.i("UserInput", "rejerstracja udana");
          Toast.makeText(MainActivity.this, "Jerestracja udana możesz się zalogować", Toast.LENGTH_SHORT).show();
          textViewLogin.setText("or Signup");
          buttonLogin.setText("Login");
          editTextPassword.getText().clear();
          editTextUsername.getText().clear();

        }else{

          Toast.makeText(MainActivity.this, "Jerestracja nieudana " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

      }
    });
  }

  public void changeStage(){

      Intent intent = new Intent(getApplicationContext(), UserList.class);
      startActivity(intent);

  }

  public void logIn(String username, String password){

    ParseUser.logInInBackground(username, password, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {

        if(user != null){

          Log.i("UserInput", "Zalogowano");
          changeStage();

        }else{

          Log.i("UserInput", "bład " + e.toString());

        }

      }
    });


  }

  public void addUser(View view){

    String username = editTextUsername.getText().toString();
    String password = editTextPassword.getText().toString();




    switch (buttonLogin.getText().toString()) {

      case "Signup":
        if (username.isEmpty() || password.isEmpty()) {

        Toast.makeText(this, "Weno podaj tekst", Toast.LENGTH_SHORT).show();

        } else {

        createNewUser(username, password);

        }
        break;

      case "Login":

        if (username.isEmpty() || password.isEmpty()) {

          Toast.makeText(this, "Weno podaj tekst", Toast.LENGTH_SHORT).show();

        } else {

          logIn(username, password);
          Log.i("currentUser", "User logged in "+ ParseUser.getCurrentUser().getUsername());

        }
        break;

    }


  }

  public void changeToLog(View view){

    if(textViewLogin.getText().toString() == "or Login"){

      textViewLogin.setText("or Signup");
      buttonLogin.setText("Login");
      editTextUsername.getText().clear();
      editTextPassword.getText().clear();
      editTextUsername.requestFocus();

    }else{

      textViewLogin.setText("or Login");
      buttonLogin.setText("Signup");
      editTextUsername.getText().clear();
      editTextPassword.getText().clear();
      editTextUsername.requestFocus();
    }



  }



}