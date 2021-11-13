package com.project_moviles2.shop_homesweethome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class actualizarVendedor extends AppCompatActivity {

    CheckBox jcbhabilidar;
    EditText jetClaveActual,jetClaveNueva,jetClaveNueva2,jetNombre1,jetPais,jetCiudad;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String rol, password,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_vendedor);

        //getActionBar().hide();

        jcbhabilidar=findViewById(R.id.cbhabilitar);
        jetClaveActual=findViewById(R.id.etClaveActual);
        jetClaveNueva=findViewById(R.id.etClaveNueva);
        jetClaveNueva2=findViewById(R.id.etClaveNueva2);
        jetNombre1=findViewById(R.id.etNombre);
        jetPais=findViewById(R.id.etPais);
        jetCiudad=findViewById(R.id.etCiudad);

        email=getIntent().getStringExtra("coleccion");

        DocumentReference docRef = db.collection("users").document(getIntent().getStringExtra("coleccion"));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Mensaje1", "DocumentSnapshot data: " + document.getData());
                        String nombre=document.getString("Name");
                        String pais=document.getString("Country");
                        String ciudad=document.getString("City");


                        jetNombre1.setText(nombre);
                        jetPais.setText(pais);
                        jetCiudad.setText(ciudad);

                    } else {
                        Log.d("mensaje2", "No such document");
                    }
                } else {
                    Log.d("Mensaje3", "get failed with ", task.getException());
                }
            }
        });
    }

    public void habilitarCambioPasword (View view){

        if (jcbhabilidar.isChecked()){
            jetClaveActual.setEnabled(true);
            jetClaveNueva.setEnabled(true);
            jetClaveNueva2.setEnabled(true);
        }
        else {
            jetClaveActual.setEnabled(false);
            jetClaveNueva.setEnabled(false);
            jetClaveNueva2.setEnabled(false);


        }
    }

    public void actualizarAfitrion(View view){

        rol=getIntent().getStringExtra("rol");
        password=getIntent().getStringExtra("password");



        final String country,city,name,passwordOld,passw1,passw2,passwordNew;

        name=jetNombre1.getText().toString();
        country= jetPais.getText().toString();
        city=jetCiudad.getText().toString();
        passwordOld=jetClaveActual.getText().toString();
        passw1=jetClaveNueva.getText().toString();
        passw2=jetClaveNueva2.getText().toString();
        passwordNew = jetClaveNueva.getText().toString();



        Map<String, Object> user = new HashMap<>();

        if (jcbhabilidar.isChecked()) {

            if (passwordOld.isEmpty() || passw1.isEmpty() || passw2.isEmpty()) {

                Toast.makeText(getApplicationContext(), "Los campos: 'Contraseña actual','Nueva contraseña'" +
                                "y 'Confirmación nueva contraseña' deben ser diligenciados"
                        , Toast.LENGTH_LONG).show();
            }
            else {

                if (passwordOld.equals(password)) {

                    if (!passw1.equals(passw2)) {

                        Toast.makeText(getApplicationContext(), "Clave nueva y confirmación no coinciden"
                                , Toast.LENGTH_LONG).show();

                        jetClaveNueva.setText("");
                        jetClaveNueva2.setText("");

                        jetClaveNueva.requestFocus();

                    } else {
                        user.put("Password", passw1);
                        user.put("Country",country );
                        user.put("Name", name);
                        user.put("City", city);
                        user.put("Rol", rol);



                        db.collection("users").document(getIntent().getStringExtra("coleccion"))
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Datos actualizados",Toast.LENGTH_SHORT).show();

                                        Intent intent= new Intent(getApplicationContext(),Usuario_invitado_Activity.class);
                                        intent.putExtra("coleccion",email);
                                        intent.putExtra("rol",rol);
                                        intent.putExtra("password",password);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Log.w(TAG, "Error writing document", e);
                                    }
                                });

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta",
                            Toast.LENGTH_LONG).show();
                    jetClaveActual.setText("");
                    jetClaveActual.requestFocus();

                }

            }

        }else{

            user.put("Country",country );
            user.put("Name", name);
            user.put("City", city);
            user.put("Rol", rol);
            user.put("Password", password);

            db.collection("users").document(getIntent().getStringExtra("coleccion"))
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Datos actualizados",Toast.LENGTH_SHORT).show();

                            Intent intent= new Intent(getApplicationContext(),Usuario_invitado_Activity.class);
                            intent.putExtra("coleccion",email);
                            intent.putExtra("rol",rol);
                            intent.putExtra("password",password);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error writing document", e);
                        }
                    });


        }


      /*  if (jcbhabilidar.isChecked()){

            if(passwordOld.isEmpty()||passw1.isEmpty()||passw2.isEmpty()){

                Toast.makeText(getApplicationContext(),"Los campos: 'Contraseña actual','Nueva contraseña'" +
                                "y 'Confirmación nueva contraseña' deben ser diligenciados"
                        ,Toast.LENGTH_LONG).show();

            }
            else {
                if (passwordOld.equals(password)) {
                    if (!passw1.equals(passw2)) {

                        Toast.makeText(getApplicationContext(), "Clave nueva y confirmación no coinciden"
                                , Toast.LENGTH_LONG).show();

                        jetClaveNueva.setText("");
                        jetClaveNueva2.setText("");

                        jetClaveNueva.requestFocus();

                    } else {
                        user.put("Password", passw1);

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Contraseña incorrecta",
                            Toast.LENGTH_LONG);
                    jetClaveActual.setText("");
                    jetClaveActual.requestFocus();


                }


            }

         }
         else{

            user.put("Password", password);
        }*/

    }

}