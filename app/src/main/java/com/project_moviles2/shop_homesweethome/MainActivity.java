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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    TextView jtvRegistrarse;
    EditText jetUsuario,jetClave;
    Button jbtnloguear;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jtvRegistrarse=findViewById(R.id.tvRegistrarse);
        jetUsuario=findViewById(R.id.etUsuario);
        jetClave=findViewById(R.id.etClave);
        jbtnloguear=findViewById(R.id.btnloquear);


        String email_retorno=getIntent().getStringExtra("coleccion");

        if( email_retorno == null) {


            jetUsuario.setText("");
        }
        else{
            jetUsuario.setText(email_retorno);
        }
    }

    public void logueo (View view) {

        final String usuario, clave;

        usuario = jetUsuario.getText().toString();
        clave = jetClave.getText().toString();

        if (usuario.isEmpty()) {

            Toast.makeText(getApplicationContext(), "Usuario no ingresado.", Toast.LENGTH_SHORT).show();
            jetUsuario.requestFocus();
        } else if (clave.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe ingresar la contraseña.", Toast.LENGTH_SHORT).show();
            jetClave.requestFocus();
        }
        else {

            DocumentReference docRef = db.collection("users").document(usuario);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Mensaje1", "DocumentSnapshot data: " + document.getData());
                            String password = document.getString("Password");

                            if (password.equals(clave)) {
                                String rol = document.getString("Rol");
                                if (rol.equals("Anfitrion")){

                                    Intent intent = new Intent(getApplicationContext(), UsuarioActivity.class);
                                    intent.putExtra("coleccion", usuario);
                                    intent.putExtra("rol", rol);
                                    intent.putExtra("password", password);
                                    //guardarPreferencias();
                                    startActivity(intent);

                                }
                                else if (rol.equals("Invitado")){
                                    Intent intent = new Intent(getApplicationContext(), Usuario_invitado_Activity.class);
                                    intent.putExtra("coleccion", usuario);
                                    intent.putExtra("rol", rol);
                                    intent.putExtra("password", password);
                                    startActivity(intent);

                                }
                                jetClave.setText("");

                            } else {
                                Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                jetClave.setText("");
                                jetClave.requestFocus();
                            }

                        } else {
                            Log.d("mensaje2", "No such document");
                            Toast.makeText(getApplicationContext(), "Usuario no extiste, por favor confirmar", Toast.LENGTH_LONG).show();

                            jetUsuario.requestFocus();
                            jetClave.setText("");
                        }
                    } else {

                        Log.d("Mensaje3", "get failed with ", task.getException());
                        // Toast.makeText(getApplicationContext(), "Usuario no extiste, por favor confirmar", Toast.LENGTH_LONG).show();

                        jetUsuario.requestFocus();
                        jetClave.setText("");
                    }

                }


            });

        }
    }

    public void irRegistro (View view){

        Intent intent= new Intent(getApplicationContext(),RegistroActivity.class);
        startActivity(intent);

    }
}