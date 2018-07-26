package sadba.lab.com.smart;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sadba.lab.com.smart.Model.PostUser;
import sadba.lab.com.smart.Model.PostVerifUser;
import sadba.lab.com.smart.Model.User;
import sadba.lab.com.smart.Model.VerifUser;
import sadba.lab.com.smart.Remote.Common;
import sadba.lab.com.smart.Remote.IMyAPI;

public class MainActivity extends AppCompatActivity {

    TextView txt_verif;
    EditText edt_ien, edt_password;
    Button btn_login;
    SharedPreferences sp;
    IMyAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Service
        mService = Common.getAPI();

        //Init View
        txt_verif = findViewById(R.id.txt_verif);
        edt_ien = findViewById(R.id.edt_ien);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);

        sp = getSharedPreferences("btn_login", MODE_PRIVATE);

        if (sp.getBoolean("logged", false)){
            gotToHomeActivity();
        }

        //Event
        txt_verif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, VerifActivity.class));
                showVerifDialog();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edt_ien.getText().toString();
                String password = edt_password.getText().toString();
                if (TextUtils.isEmpty(password)){
                    edt_password.setError("Mot de passe ne pas etre vide");
                } else if (TextUtils.isEmpty(username)){
                    edt_ien.setError("Ien ne doit pas etre vide");
                } else if (!isPasswordValid(password)){
                    edt_password.setError("Le mot de passe ne doit pas être inférieur a 8 caractères");
                }
                else {
                    authenticateUser(username, password);
                    //edt_ien.getText().clear();
                   // edt_password.getText().clear();
                }

            }
        });

    }

    private void authenticateUser(String username, String password) {
        PostUser postUser = new PostUser();
        postUser.setPassword(password);
        postUser.setUsername(username);
        mService.loginUser(postUser)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        User result = response.body();
                        String token = result.getToken();
                        if (result.getCode().equals("1")) {
                            Toast.makeText(MainActivity.this, "Paramétres de connection incorrectes", Toast.LENGTH_LONG).show();
                        } else {
                            if (token == result.getToken()){
                                gotToHomeActivity();
                                sp.edit().putBoolean("logged", true).apply();
                            } else {
                                sp.edit().putBoolean("logged", false).apply();
                            }



                        }


                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean isPasswordValid(String password) {

        return password.length() >= 8;
    }

    private void showVerifDialog() {

        View view = getLayoutInflater().inflate(R.layout.verif_layout, null);

        final MaterialEditText edt_ien = (MaterialEditText) view.findViewById(R.id.edt_ienChild);
        //final MaterialEditText edt_cni = (MaterialEditText) view.findViewById(R.id.edt_id_card);

        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(view)
                .setTitle("Verification des droits d'accés")
                .setPositiveButton("VERIFIER", null)
                .setNegativeButton("ANNULER", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //alertDialog.dismiss();
                        String ien = edt_ien.getText().toString();
                        //String cni = edt_cni.getText().toString();

                        if (TextUtils.isEmpty(ien)){
                            edt_ien.setError("L'IEN ne doit pas etre vide");
                            // Toast.makeText(MainActivity.this, "yup", Toast.LENGTH_SHORT).show();
                            return;

                        } else {
                            PostVerifUser postVerifUser = new PostVerifUser();
                            postVerifUser.setUsername(ien);
                            //postVerifUser.setCni(cni);

                            final android.app.AlertDialog watingDialog = new SpotsDialog(MainActivity.this);
                            watingDialog.show();
                            watingDialog.setTitle("En cours...");
                            mService.verifUser(postVerifUser)
                                    .enqueue(new Callback<VerifUser>() {
                                        @Override
                                        public void onResponse(Call<VerifUser> call, Response<VerifUser> response) {
                                            VerifUser result = response.body();
                                            if (result.getCode().equals("1")){
                                                //progressDoalog.dismiss();
                                                Toast.makeText(MainActivity.this, "Vous n'avez pas les droits pour accéder a cette application", Toast.LENGTH_LONG).show();
                                            } else {

                                                alertDialog.dismiss();
                                                watingDialog.dismiss();
                                                Toast.makeText(MainActivity.this, "Veuillez vérifier votre boite mail professionnelle", Toast.LENGTH_SHORT).show();


                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<VerifUser> call, Throwable t) {
                                            // progressDoalog.dismiss();
                                            watingDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Veuillez vérifier votre connection internet", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });

                Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void gotToHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }




}
