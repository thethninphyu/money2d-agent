package com.example.useraccountmanagement.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.useraccountmanagement.R;


public class AgentEditAdapter extends RecyclerView.Adapter<AgentEditAdapter.PlaceHolder> implements TextWatcher {
    Context con;
    String email;
    Boolean check=null;
    CVItemClick cvItemClick;
    Dialog dialog;
    EditText point;
    int points;

    public AgentEditAdapter(Context con, String email,int points) {
        this.con = con;
        this.email = email;
        this.points=points;
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(con).inflate(R.layout.custom_agent_edit,viewGroup,false);
        PlaceHolder p=new PlaceHolder(v);
        return p;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlaceHolder placeHolder, final int i) {
        placeHolder.takepoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(email,"Take");

            }
        });

        placeHolder.givepoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(email,"Give");
            }
        });

        placeHolder.changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvItemClick.onChangeClick(email);
            }
        });

        placeHolder.userList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvItemClick.onUserList(email);
            }
        });

        placeHolder.btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialog=new AlertDialog.Builder(con);
                alertdialog.setTitle("Block");
                alertdialog.setMessage("Are you want to block!");
                alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     cvItemClick.onBlockList(email,placeHolder.getAdapterPosition());
                    }
                });
                alertdialog.setNeutralButton("Cancle",null);
                alertdialog.show();
            }
        });
    }

    private void createDialog(final String email, final String status) {
        AlertDialog.Builder builder=new AlertDialog.Builder(con);
        View newView= LayoutInflater.from(con).inflate(R.layout.custom_take_dialog,null);
        point=newView.findViewById(R.id.edt_points);
        Button ok=newView.findViewById(R.id.btnsubmit);
        Button cancel=newView.findViewById(R.id.btnCancel);
        final CheckBox givecheck=newView.findViewById(R.id.give_check);

        if (status.equals("Take")){
            givecheck.setVisibility(View.INVISIBLE);
            point.addTextChangedListener(this);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (givecheck.isChecked()){
                    check=true;
                }else {
                    check = false;
                }

                if(point.length()==0){
                    Toast.makeText(con, "Pls add points!", Toast.LENGTH_SHORT).show();
                    return;
                }
                cvItemClick.onItemClick(email,Integer.parseInt(point.getText().toString()),check,status);
                dialog.dismiss();
                Toast.makeText(con, "Success", Toast.LENGTH_SHORT).show();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.setView(newView);
        dialog=new Dialog(con,R.style.ThemeWithCorners);
        dialog=builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_shape);
        dialog.show();
        Window dialogWindow=dialog.getWindow();
        editDialogDesign(dialogWindow);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (point.length()>0 && Integer.valueOf(point.getText().toString())>points){
            point.setText("");
            point.setHint("Enough agent point!");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public class PlaceHolder extends RecyclerView.ViewHolder {
        ImageView userList,takepoints,givepoints,changepass,btnBlock;

        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            takepoints=itemView.findViewById(R.id.btnSub);
            givepoints=itemView.findViewById(R.id.btnAdd);
            userList=itemView.findViewById(R.id.myUserList);
            changepass=itemView.findViewById(R.id.btnAgentChangePass);
            btnBlock=itemView.findViewById(R.id.btnAgentBlock);
        }
    }

    public interface CVItemClick{
        public void onItemClick(String email, int points, Boolean check, String status);
        public void onBlockList(String email, int i);
        public void onChangeClick(String email);
        public void onUserList(String email);
    }
    public void setAdapterIteamClickListener(AgentEditAdapter.CVItemClick cvItemClick){
        this.cvItemClick=cvItemClick;
    }
    private void editDialogDesign(Window dialogwidow) {
        dialogwidow.setBackgroundDrawableResource(R.drawable.dialog_shape);
        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm= (WindowManager) con.getSystemService(con.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams param = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        param.copyFrom(dialogwidow.getAttributes());

        // Set alert dialog width equal to screen width 70%
        int width = (int) (displayWidth * 0.85);
        // Set alert dialog height equal to screen height 70%
        int height = (int) (displayHeight * 0.31f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        param.width = width;
        param.height = height;

        // Apply the newly created layout parameters to the alert dialog window
        dialogwidow.setAttributes(param);

    }
}
