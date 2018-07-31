package sadba.lab.com.smart;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import sadba.lab.com.smart.Model.User;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView structure;

    private CardView recensementCard, stockCard, inventaireCard, campagneCard;
    String[] listItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar =  findViewById(R.id.toolbarHome);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        collapsingToolbar.setTitle("SMART");

        recensementCard = findViewById(R.id.recensement_card);
        stockCard = findViewById(R.id.stock_card);
        inventaireCard = findViewById(R.id.inventaire_card);
        campagneCard = findViewById(R.id.campagne_card);


        //Add Click Listener to the cards
        recensementCard.setOnClickListener(this);
        stockCard.setOnClickListener(this);
        inventaireCard.setOnClickListener(this);
        campagneCard.setOnClickListener(this);


    }


    //Permet de quitter l'application lorsque le bouton Back est pressed
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.recensement_card :
                listItems = new String[]{"Recensement Agent", "Recensement Bureau", "Recensement Magasin"};
                AlertDialog.Builder  mBuilder = new AlertDialog.Builder(HomeActivity.this);
                mBuilder.setTitle("Choisir un type de recensement");
                mBuilder.setIcon(R.drawable.list);
                mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int itemPosition = i;
                        if (itemPosition == 0) {
                            //Toast.makeText(HomeActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, AgentsActivity.class);
                            startActivity(intent);
                            dialogInterface.dismiss();
                        } else if (itemPosition == 1){
                            Intent intent = new Intent(HomeActivity.this, BureauActivity.class);
                            startActivity(intent);
                            dialogInterface.dismiss();
                        } else {
                            Intent intent = new Intent(HomeActivity.this, MagasinActivity.class);
                            startActivity(intent);
                            dialogInterface.dismiss();
                        }
                    }
                });
                mBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                break;

            case R.id.stock_card :

                break;

            case R.id.inventaire_card :

                break;
            case R.id.campagne_card :

                break;

            default: break;
        }
    }

    private void openDialog() {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
