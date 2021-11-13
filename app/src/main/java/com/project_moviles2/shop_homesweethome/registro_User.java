package com.project_moviles2.shop_homesweethome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registro_User extends AppCompatActivity {

    EditText jetnombre,jetEmail,jetPais,jetCiudad,jetClave1,jetClave2;
    RadioButton jrbAnfitrion,jrbInvitado;
    Button jbtnRegistrar;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_user);

        jetnombre=findViewById(R.id.etNombre);
        jetEmail=findViewById(R.id.etEmail);
        jetPais=findViewById(R.id.etPais);
        jetCiudad=findViewById(R.id.etCiudad);
        jetClave1=findViewById(R.id.etClaveActual);
        jetClave2=findViewById(R.id.etClaveNueva);
        jrbAnfitrion=findViewById(R.id.rbAnfitrion);
        jrbInvitado=findViewById(R.id.rbInvitado);
        jbtnRegistrar=findViewById(R.id.btnActualizar);


        jbtnRegistrar.setCursorVisible(false);
    }

    public void Registrar (View view){

        //Map<String, Object> user = new HashMap<>();

        final String name,email,country,city,pass1,pass2;

        name= jetnombre.getText().toString();
        email=jetEmail.getText().toString();
        country=jetPais.getText().toString();
        city=jetCiudad.getText().toString();
        pass1=jetClave1.getText().toString();
        pass2=jetClave2.getText().toString();

        if (name.isEmpty() ){

            Toast.makeText(getApplicationContext(),"El campo 'Nombre' debe ser diligenciado",Toast.LENGTH_LONG).show();
            jetnombre.requestFocus();
        }
        else if (email.isEmpty()){
            Toast.makeText(getApplicationContext(),"El campo 'E-mail' debe ser diligenciado",Toast.LENGTH_LONG).show();
            jetEmail.requestFocus();
        }

        else if (country.isEmpty()){
            Toast.makeText(getApplicationContext(),"El campo 'Pais' debe ser diligenciado",Toast.LENGTH_LONG).show();
            jetPais.requestFocus();

        }
        else if (city.isEmpty()){
            Toast.makeText(getApplicationContext(),"El campo 'Ciudad' debe ser diligenciado",Toast.LENGTH_LONG).show();
            jetCiudad.requestFocus();

        }
        else if (pass1.isEmpty()){

            Toast.makeText(getApplicationContext(),"Se debe ingresar clave",Toast.LENGTH_LONG).show();
            jetClave1.requestFocus();

        }

        else if (pass1.length()<6){

            Toast.makeText(getApplicationContext(),"La contraseña debe contener  6 o más caracteres",Toast.LENGTH_LONG).show();
            jetClave1.requestFocus();
            jetClave1.setText("");
            jetClave2.setText("");

        }

        else if (pass2.isEmpty()){

            Toast.makeText(getApplicationContext(),"Se debe confirmar su clave",Toast.LENGTH_LONG).show();
            jetClave2.requestFocus();

        }


        else if(!pass1.equals(pass2)){


            Toast.makeText(this,"Contraseñas no coinciden",Toast.LENGTH_SHORT).show();

            jetClave1.setText("");
            jetClave2.setText("");
            jetClave1.requestFocus();

        }
        else {

            DocumentReference docRef = db.collection("users").document(email);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                            builder.setTitle("Usuario ya existe.");
                            builder.setMessage("Deseas iniciar Sesion con:\n "+email+"?")
                                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("coleccion", email);
                                            startActivity(intent);

                                        }
                                    })
                                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getApplicationContext(),"Por favor crear cuenta con otro nombre de usuario",Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        }
                                    }).show();

                        }
                        else {
                            Map<String, Object> user = new HashMap<>();

                            user.put("Password", pass1);


                            if (jrbAnfitrion.isChecked()) {
                                user.put("Rol", "Anfitrion");

                            } else if (jrbInvitado.isChecked()) {
                                user.put("Rol", "Invitado");

                            }

                            user.put("Name", name);
                            user.put("Country", country);
                            user.put("City", city);

                            user.put("Name", name);
                            user.put("Country", country);
                            user.put("City", city);

                            db.collection("users").document(email)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Registro ok", "DocumentSnapshot successfully written!");
                                            Toast.makeText(getApplicationContext(),"Usuario resgistrado correctamente",Toast.LENGTH_LONG).show();
                                            Intent intent= new Intent(getApplicationContext(),UsuarioActivity.class);



                                            intent.putExtra("coleccion",email);
                                            startActivity(intent);

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Registro ko", "Error writing document", e);
                                        }
                                    });

                        }
                    }

                }

            });

        }
    }

    public void Autenticar (){


        String email= jetEmail.getText().toString();
        String password=jetClave1.getText().toString();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Usuario Registrado Correctamente", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(RegistroActivity.this,"no funciona esta mierda",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}