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

public class editarProducto extends AppCompatActivity {

    TextView jtvtvEmail;
    EditText jetPais,jetCiudad1,jetDireccion,jetHabitaciones1,jetPrecio1,jetDescripcion;
    Button jbtnActualizar,jbtnRegresar;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        jetPais=findViewById(R.id.etPais1);
        jetCiudad1=findViewById(R.id.etCiudad1);
        jetDireccion=findViewById(R.id.etDireccion);
        jetHabitaciones1=findViewById(R.id.etHabitaciones1);
        jetPrecio1=findViewById(R.id.etPrecio1);
        jetDescripcion=findViewById(R.id.etDescripcion);
        jbtnActualizar=findViewById(R.id.btnActualizar1);
        jbtnRegresar=findViewById(R.id.btnRegresar);
        jtvtvEmail=findViewById(R.id.tvEmail);



        String apto=getIntent().getStringExtra("id");


        DocumentReference docRef = db.collection("Apartaments").document(apto);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Mensaje1", "DocumentSnapshot data: " + document.getData());
                        String pais=document.getString("country");
                        String ciudad=document.getString("city");
                        String direccion=document.getString("adress");
                        String habitaciones=document.getString("bedrooms");
                        String precio=document.getString("priceNight");
                        String descripcion=document.getString("description");
                        String owner=document.getString("owner");

                        jetPais.setText(pais);
                        jetCiudad1.setText(ciudad);
                        jetDireccion.setText(direccion);
                        jetHabitaciones1.setText(habitaciones);
                        jetPrecio1.setText(precio);
                        jetDescripcion.setText(descripcion);
                        jtvtvEmail.setText(owner);


                    } else {
                        Log.d("Ese id no existe", "No such document");
                    }
                } else {
                    Log.d("No conecta a la Bd", "get failed with ", task.getException());
                }
            }
        });

    }

    public void actualizarApto (View view){

        final String country,city,adress,cost, bedrooms,description,owner;

        country= jetPais.getText().toString();
        city=jetCiudad1.getText().toString();
        adress=jetDireccion.getText().toString();
        cost=jetPrecio1.getText().toString();
        description=jetDescripcion.getText().toString();
        bedrooms=jetHabitaciones1.getText().toString();
        owner=jtvtvEmail.getText().toString();



        if (country.isEmpty() ){

            Toast.makeText(getApplicationContext(),"El campo 'Pais' debe ser diligenciado",Toast.LENGTH_LONG).show();
            jetPais.requestFocus();
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
            jetHabitaciones1.requestFocus();

        }

        else if (cost.isEmpty()){
            Toast.makeText(getApplicationContext(),"El campo 'Precio' debe ser diligenciado",Toast.LENGTH_LONG).show();
            jetPrecio1.requestFocus();

        }


        String apto=getIntent().getStringExtra("id");


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


        db.collection("Apartaments").document(apto)
                .set(apartament)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Apartamento actualizado",Toast.LENGTH_SHORT).show();

                        Intent intent= new Intent(getApplicationContext(),Vendedor.class);
                        intent.putExtra("coleccion",owner);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



    }

    public void regresar (View view){
        String owner=jtvtvEmail.getText().toString();

        Intent intent= new Intent(getApplicationContext(),Vendedor.class);
        intent.putExtra("coleccion",owner);
        startActivity(intent);

    }

}