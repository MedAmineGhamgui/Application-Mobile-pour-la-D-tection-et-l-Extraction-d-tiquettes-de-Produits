package org.pytorch.demo.objectdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    Button button;
    EditText magasin;
    EditText localisation;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button=findViewById(R.id.button2);
        magasin=findViewById(R.id.magasin);
        localisation=findViewById(R.id.localisation);
        ImageView imageView = findViewById(R.id.imageView2);
        imageView.setImageResource(R.mipmap.locali);



        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String mag = magasin.getText().toString();
                                          String loc = localisation.getText().toString();
                                          if (mag.isEmpty()&&loc.isEmpty()) {
                                              magasin.setError("Veuillez remplir ce champ");
                                              localisation.setError("Veuillez remplir ce champ");
                                          }
                                          else if (loc.isEmpty()){
                                              localisation.setError("Veuillez remplir ce champ");
                                          }
                                          else if (mag.isEmpty()){
                                              magasin.setError("Veuillez remplir ce champ");
                                          }
                                          else {
                                              Intent detail_article2= new Intent(getApplicationContext(),MainActivity.class);
                                              Toast toast = Toast.makeText(MainActivity2.this,""+localisation.getText().toString(), Toast.LENGTH_SHORT);

                                              detail_article2.putExtra("magasin",magasin.getText().toString() );
                                              detail_article2.putExtra("localisation",localisation.getText().toString());

                                              //Toast toast = Toast.makeText(MainActivity2.this,magasin.toString(), Toast.LENGTH_SHORT);
                                              toast.show();


                                              System.out.println(magasin.toString() + "dddd"+ localisation.toString());
                                              startActivity(detail_article2);
                                          }

                                      }
                                  }
                                  );

    }
}