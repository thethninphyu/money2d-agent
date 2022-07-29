package com.example.useraccountmanagement;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.useraccountmanagement.model.AgentLogin;
import com.example.useraccountmanagement.model.Server;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

;

public class LoginActivity extends AppCompatActivity {

    EditText edtUserName,edtPassword;
    ProgressBar pb;
    Dialog dialog;
    CheckBox saveCheckBox;
    // private KProgressHUD hud;

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";

    private String username;
    private String userpassword;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        edtUserName=findViewById(R.id.edtUsername);
        edtPassword=findViewById(R.id.edtPassword);
        saveCheckBox=findViewById(R.id.checkboxSave);

        ApiRequest apiRequest= ApiUtil.getApiRequest();
        apiRequest.checkserver(token).enqueue(new Callback<Server>(){
            public void onResponse(Call<Server> call, Response<Server> response){
                if (response.body().getServer()==false){
                    System.exit(0);
                }
                else if (response.body().getServer()==true&&response.body().getNoti()==false){
                    android.app.AlertDialog.Builder alert =new android.app.AlertDialog.Builder(LoginActivity.this);

                    alert.setCancelable(false);
                    alert.setIcon(R.drawable.alart_dialog);
                    alert.setMessage(response.body().getMessage());
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(LoginActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<Server> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Mistake", Toast.LENGTH_SHORT).show();
            }
        });


        loadPreferences();


    }

    private void loadPreferences() {

        SharedPreferences sharedpreferences = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        username = sharedpreferences.getString(PREF_UNAME, username);
        userpassword = sharedpreferences.getString(PREF_PASSWORD, userpassword);
        edtUserName.setText(username);
        edtPassword.setText(userpassword);
        if(edtUserName.length()!=0)
            saveCheckBox.setChecked(true);



    }

    public void btnLogin(View view) {

        username=edtUserName.getText().toString();
        userpassword=edtPassword.getText().toString();
        if (username.isEmpty() && !userpassword.isEmpty()){
            Toast.makeText(this,"Username is empty!",Toast.LENGTH_LONG).show();
            return;
        }else if (!username.isEmpty() && userpassword.isEmpty()){
            Toast.makeText(this,"Password is empty!",Toast.LENGTH_LONG).show();
            return;
        }else if (username.isEmpty() && userpassword.isEmpty()){
            Toast.makeText(this,"Username and password is empty!",Toast.LENGTH_LONG).show();
            return;
        }

        new LoadingLogin().execute();

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View newView= LayoutInflater.from(LoginActivity.this).inflate(R.layout.custom_loadingdialog,null);

        pb=newView.findViewById(R.id.progressBar);
        TextView tvNoti=newView.findViewById(R.id.tv_noti);

        builder.setView(newView);
        dialog=new Dialog(this,R.style.ThemeWithCorners);
        dialog=builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_shape);
        dialog.show();
        Window dialogWindow=dialog.getWindow();
        editDialogDesign(dialogWindow);
        new LoadingLogin().execute();

     /*   hud = KProgressHUD.create(LoginActivity.this)
                .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false);
        hud.show();
        new LoadingLogin().execute();*/



    }




    public class LoadingLogin extends AsyncTask<Void,Void,Void> {
        String email=edtUserName.getText().toString()+"@real2d.com";
        String password=edtPassword.getText().toString();

        @Override
        protected Void doInBackground(Void... voids) {


            ApiRequest apiRequest= ApiUtil.getApiRequest();
            apiRequest.adminLogin(email,password).enqueue(new Callback<AgentLogin>() {
                @Override
                public void onResponse(Call<AgentLogin> call, Response<AgentLogin> response) {

                    Boolean error=response.body().getError();

                    if (!error){

                        SharedPreferences sharedpreferences = getSharedPreferences(PREFS_NAME,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        if(saveCheckBox.isChecked()){
                            editor.putString(PREF_UNAME, username);
                            editor.putString(PREF_PASSWORD, userpassword);
                            editor.commit();
                        }else {
                            editor.putString(PREF_UNAME, "");
                            editor.putString(PREF_PASSWORD, "");
                            editor.commit();
                            saveCheckBox.setChecked(false);
                        }
                        String status=response.body().getStatus();
                        String email=response.body().getName()+"@real2d.com";
                        String token=response.body().getToken();
                        String agentName=response.body().getName();
                        /*if (status.equals("NEED")){
                            Intent intent=new Intent(LoginActivity.this, ChangePassword.class);
                            intent.putExtra("token",token);
                            intent.putExtra("email",email);
                            startActivity(intent);
                        }else if (status.equals("!ACTIVE")){
                            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("token",token);
                            intent.putExtra("email",email);
                            startActivity(intent);

                        }*/

                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("token",token);
                        intent.putExtra("email",email);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    }else {

                        dialog.dismiss();
                        System.out.println("Something went wrong!");
                        Toast.makeText(LoginActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();

                        AlertDialog.Builder newbuilder=new AlertDialog.Builder(LoginActivity.this);
                        View view= LayoutInflater.from(LoginActivity.this).inflate(R.layout.custom_messagedialog,null);

                        Button btnMOk=view.findViewById(R.id.btnMOk);

                        newbuilder.setView(view);
                        final Dialog Message_dialog=newbuilder.create();
                        dialog.setCancelable(false);
                        btnMOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Message_dialog.dismiss();
                            }
                        });
                        Message_dialog.show();
                        Window dialogwidow =Message_dialog.getWindow();
                        editDialogDesign(dialogwidow);
                    }
                }

                @Override
                public void onFailure(Call<AgentLogin> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Bad connection!",Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }


        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str= String.valueOf(s);
            if (edtUserName.length()>0 && Character.isWhitespace(s.charAt(start))){
                if (str.equals(edtUserName.getText().toString())){
                    edtUserName.setText(edtUserName.getText().toString().replaceAll(" ",""));
                    edtUserName.setSelection(edtUserName.getText().length());
                }else if (edtPassword.length()>0 && str.equals(edtPassword.getText().toString())){
                    edtPassword.setText(edtPassword.getText().toString().replaceAll(" ",""));
                    edtPassword.setSelection(edtPassword.getText().length());
                }
                Toast.makeText(LoginActivity.this, "Space is not allowed!", Toast.LENGTH_SHORT).show();
            }
        }


        public void afterTextChanged(Editable s) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void editDialogDesign(Window dialogwidow) {
        dialogwidow.setBackgroundDrawableResource(R.drawable.dialog_shape);
        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams param = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        param.copyFrom(dialogwidow.getAttributes());

        // Set alert dialog width equal to screen width 70%
        int width = (int) (displayWidth * 0.7f);
        // Set alert dialog height equal to screen height 70%
        int height = (int) (displayHeight * 0.28f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        param.width = width;
        param.height = height;

        // Apply the newly created layout parameters to the alert dialog window
        dialogwidow.setAttributes(param);

    }


}
