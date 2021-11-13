package com.project_moviles2.shop_homesweethome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class registroProducto extends AppCompatActivity {

    TextView jtvRol,jtvNombre,jtvEmail;
    EditText jetPais1,jetCiudad1,jetDireccion,jetPrecio,jetDescripcion,jetHabitaciones;
    Button jbtnCrear,jbtnRegresar;
    String email;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore db2 = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_producto);

        getSupportActionBar().hide();

        jtvRol= findViewById(R.id.tvRol);
        jtvNombre=findViewById(R.id.tvNombre);
        jtvEmail=findViewById(R.id.tvEmail);
        jbtnRegresar=findViewById(R.id.btnRegresar);
        jetPais1=findViewById(R.id.etPais1);
        jetCiudad1=findViewById(R.id.etCiudad1);
        jetPrecio=findViewById(R.id.etPrecio);
        jetDescripcion=findViewById(R.id.etDescripcion);
        jetDireccion=findViewById(R.id.etDireccion);
        jbtnRegresar=findViewById(R.id.btnRegresar);
        jbtnCrear=findViewById(R.id.btnActualizar1);
        jetHabitaciones=findViewById(R.id.etHabitaciones);



        jtvEmail.setText("Usuraio: "+getIntent().getStringExtra("coleccion"));

        DocumentReference docRef = db.collection("users").document(getIntent().getStringExtra("coleccion"));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Mensaje1", "DocumentSnapshot data: " + document.getData());
                        String nombre=document.getString("Name");
                        String rol=document.getString("Rol");

                        jtvNombre.setText(nombre);
                        jtvRol.setText("Rol: "+rol);

                    } else {
                        Log.d("mensaje2", "No such document");
                    }
                } else {
                    Log.d("Mensaje3", "get failed with ", task.getException());
                }
            }
        });
    }

    public void Regresar(View view){

        Intent intent= new Intent(getApplicationContext(),Vendedor.class);

        startActivity(intent);

    }

    public void CrearApto(View view){


        final String country,city,adress,cost, bedrooms,description,owner;

        country= jetPais1.getText().toString();
        city=jetCiudad1.getText().toString();
        adress=jetDireccion.getText().toString();
        cost=jetPrecio.getText().toString();
        description=jetDescripcion.getText().toString();
        bedrooms=jetHabitaciones.getText().toString();

        owner=getIntent().getStringExtra("coleccion");


        if (country.isEmpty() ){

            Toast.makeText(getApplicationContext(),"El campo 'Pais' debe ser diligenciado",Toast.LENGTH_LONG).show();
            jetPais1.requestFocus();
        }
        else if (city.isEmpty()){
            Toast.makeText(getApplicationContext(),"El campo 'Ciudad' debe ser diligenciado",Toast.LENGTH_LONG).show();
            jetCiudad1.requestFocus();
        }

        else if (adress.isEmpty()){
            Toast.makeText(getApplicationContext(),"El campo 'Direccion' debe ser diligenciado",Toast.LENGTH_LONG).show();
            jetDireccion.requestFocus();

        }
        else if (bedrooms.isEmpty()){
            Toast.makeText(getApplicationContext(),"Debe ingresar la cantidad de habitaciones",Toast.LENGTH_LONG).show();
            jetHabitaciones.requestFocus();

        }

        else if (cost.isEmpty()){
            Toast.makeText(getApplicationContext(),"El campo 'Precio' debe ser diligenciado",Toast.LENGTH_LONG).show();
            jetPrecio.requestFocus();

        }


        DocumentReference docRef = db2.collection("Products").document(owner);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                    }
                    else {

                        Map<String, Object> apartament = new HashMap<>();

                        apartament.put("country",country );
                        apartament.put("city", city);
                        apartament.put("bedrooms", bedrooms);
                        apartament.put("adress", adress);
                        apartament.put("priceNight", cost);
                        apartament.put("description", description);
                        apartament.put("state", "Disponible");
                        apartament.put("client", "");
                        apartament.put("owner", owner);

                        db.collection("Apartaments")
                                .add(apartament)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("ingresado correctamente", "DocumentSnapshot added with ID: " + documentReference.getId());
                                        Toast.makeText(getApplicationContext(),"Datos ingresados correctamente",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Hola2", "Error adding document", e);
                                    }
                                });


                       /* db.collection("Apartaments").document(owner)
                                .set(apartament)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Registro ok", "DocumentSnapshot successfully written!");
                                        Toast.makeText(getApplicationContext(),"Apartamento creado",Toast.LENGTH_LONG).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Registro ko", "Error writing document", e);
                                    }
                                });*/


                    }
                }


            }


        });

    }
}