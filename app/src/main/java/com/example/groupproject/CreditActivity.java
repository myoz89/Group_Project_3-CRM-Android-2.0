package com.example.groupproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Model.AllUsers;
import Model.Customer;
import Model.Owner;

public class CreditActivity extends AppCompatActivity {
    AllUsers allUsers;
    Customer customer;
    Owner owner;
    double amount;
    private int CREDIT_ACTIVITY_REQUEST_CODE = 3;
    private int SEND_ACTIVITY_REQUEST_CODE = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        final Intent intent = getIntent();
        allUsers = (AllUsers)intent.getSerializableExtra("AllUsers");
        String Userid = (String) intent.getSerializableExtra("id");
        final Context context = this;

        owner = allUsers.getOwnerBasedOnID(Userid);
        customer = allUsers.getCustomerBasedOnID(Userid);
        String m_Text;
        final EditText result;

        TextView textView = (TextView) findViewById(R.id.credit_num);
        if(owner != null)
            textView.setText(Double.toString(owner.getCredit()));
        else
            textView.setText(Double.toString(customer.getCredit()));

        //beginning of deposit
        Button deposit = findViewById(R.id.deposit_but);
        result = findViewById(R.id.editTextResult);

        deposit.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.popup, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text
                                            result.setText(userInput.getText());
                                            amount = Double.parseDouble(result.getText().toString());
                                            if(owner!= null)
                                            {
                                                owner.addCredit(amount);
                                                TextView textView = (TextView) findViewById(R.id.credit_num);
                                                textView.setText(Double.toString(owner.getCredit()));

                                            }
                                            else
                                            {
                                                customer.addCredit(amount);
                                                TextView textView = (TextView) findViewById(R.id.credit_num);
                                                textView.setText(Double.toString(customer.getCredit()));
                                            }

                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();


            }
        }); //end of deposit

        //beginning of withdraw
        Button withdraw = findViewById(R.id.withdraw_but);
        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.popup, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        result.setText(userInput.getText());
                                        amount = Double.parseDouble(result.getText().toString());
                                        if(owner!= null)
                                        {
                                            owner.subCredit(amount);
                                            TextView textView = (TextView) findViewById(R.id.credit_num);
                                            textView.setText(Double.toString(owner.getCredit()));

                                        }
                                        else
                                        {
                                            customer.subCredit(amount);
                                            TextView textView = (TextView) findViewById(R.id.credit_num);
                                            textView.setText(Double.toString(customer.getCredit()));

                                        }

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();


        }
        }); //end of withdraw

        Button send = findViewById(R.id.send_but); //beginning of send
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(owner != null) //acting as owner
                {
                    Intent sendIntent = new Intent(CreditActivity.this, DisplayCustomerListActivity.class);
                    sendIntent.putExtra("ownerid", owner.getID());
                    sendIntent.putExtra("AllUsers", allUsers);
                    startActivityForResult(sendIntent,SEND_ACTIVITY_REQUEST_CODE);
                }
                else // acting as customer
                {
                    Toast.makeText(v.getContext(), "Sending to " + customer.getBus().getCompanyName(),Toast.LENGTH_LONG).show();
                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.popup, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text
                                            result.setText(userInput.getText());
                                            amount = Double.parseDouble(result.getText().toString());
                                                customer.subCredit(amount);
                                                TextView textView = (TextView) findViewById(R.id.credit_num);
                                                textView.setText(Double.toString(customer.getCredit()));
                                                Owner tempowner = customer.getBus();
                                                tempowner.addCredit(amount);
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            }
        });




        //return button
        Button butCancel = findViewById(R.id.return_but);
        butCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send data back
                /*Intent intent1 = new Intent(CreditActivity.this,OwnerMainMenuActivity.class);
                intent1.putExtra("AllUsers", allUsers);
                if(owner != null)
                {
                    intent1 = new Intent(CreditActivity.this,OwnerMainMenuActivity.class);
                    intent1.putExtra("AllUsers", allUsers);
                    intent1.putExtra("ownerID", owner.getID());

                }

                else
                {
                    intent1 = new Intent(CreditActivity.this,CustomerMainMenuActivity.class);
                    intent1.putExtra("AllUsers", allUsers);
                    intent1.putExtra("customer",customer);
                }
                setResult(RESULT_OK, intent1);
                startActivityForResult(intent1,SECOND_ACTIVITY_REQUEST_CODE);
                finish();*/

                intent.putExtra("AllUsers", allUsers);
                if(owner != null)
                    intent.putExtra("OwnerID",owner.getID());
                else
                    intent.putExtra("CustomerID",customer.getID());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                allUsers = (AllUsers) data.getSerializableExtra("AllUsers");
                String ownerID = data.getStringExtra("OwnerID");
                owner = allUsers.getOwnerBasedOnID(ownerID);
                String temp = Double.toString(owner.getCredit());
                TextView textCredit = (TextView) findViewById(R.id.credit_num);
                textCredit.setText(Double.toString(owner.getCredit()));

            }
        }


    }
}
