package com.project_moviles2.shop_homesweethome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import Modelos.ProductoModelo;

public class usuario extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter adapter;

    TextView jtvrol,jTvNombre;
    RecyclerView jrvFirestoreApartamentsList;

    String email,rol,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        getSupportActionBar().hide();

        jTvNombre=findViewById(R.id.etNombre);
        jtvrol=findViewById(R.id.rol);

        jrvFirestoreApartamentsList=findViewById(R.id.rvFirestoreApartamentsList);


        email=getIntent().getStringExtra("coleccion");
        password=getIntent().getStringExtra("password");

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

                        jTvNombre.setText(nombre);
                        jtvrol.setText(rol);

                    } else {
                        Log.d("mensaje2", "No such document");
                    }
                } else {
                    Log.d("Mensaje3", "get failed with ", task.getException());
                }
            }
        });


        Query query=db.collection("Apartaments").whereEqualTo("state","Disponible");

        FirestoreRecyclerOptions<ProductoModelo> options= new FirestoreRecyclerOptions.Builder<ProductoModelo>()
                .setQuery(query, ProductoModelo.class).build();



        adapter= new FirestoreRecyclerAdapter<ProductoModelo, Vendedor.ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Vendedor.ProductViewHolder holder, int position, @NonNull ProductoModelo model) {

                holder.tvPais.setText(model.getCountry());
                holder.tvciudad.setText(model.getCity());
                holder.tvDireccion.setText(model.getAdress());
                holder.tvDescripcion.setText(model.getDescription());
                holder.tvNhabitaciones.setText(model.getBedrooms());
                holder.tvVnoche.setText(model.getPriceNight());
                holder.TvEstado.setText(model.getState());

                final String id=getSnapshots().getSnapshot(position).getId();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(usuario.this);
                        builder.setTitle("Reservar Apartamento.");
                        builder.setMessage("Deesea Reservar este Apartamento?")
                                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {



                                        Map<String, Object> reserva = new HashMap<>();

                                        reserva.put("state","Reservado" );
                                        reserva.put("client",email );


                                        db.collection("Apartaments").document(id).update(reserva);
                                        Toast.makeText(getApplicationContext(),"Apartamento reservado"
                                                ,Toast.LENGTH_LONG).show();

                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();

                    }
                });
            }



            @NonNull
            @Override
            public Vendedor.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_usuario,parent,false);


                return new Vendedor.ProductViewHolder(view);
            }
        };

        jrvFirestoreApartamentsList.setHasFixedSize(true);
        jrvFirestoreApartamentsList.setLayoutManager(new LinearLayoutManager(this));
        jrvFirestoreApartamentsList.setAdapter(adapter);
    }

    private class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView tvPais,tvciudad,tvDireccion,tvNhabitaciones,tvVnoche,TvEstado2,tvDescripcion;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPais=itemView.findViewById(R.id.tvPais);
            tvciudad= itemView.findViewById(R.id.tvciudad);
            tvDireccion=itemView.findViewById(R.id.tvDireccion);
            tvNhabitaciones=itemView.findViewById(R.id.tvNhabitaciones);
            tvVnoche=itemView.findViewById(R.id.tvVnoche2);
            TvEstado2=itemView.findViewById(R.id.TvEstado);
            tvDescripcion=itemView.findViewById(R.id.tvDescripcion);

        }
    }

    @Override
    protected void onStart() {

        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();
    }

    public void irAptosReservados(View view){

        Intent intent= new Intent(getApplicationContext(),
                VerProductosGuardados.class);
        intent.putExtra("usuario",email);

        startActivity(intent);


    }

    public void actualizarDatos (View view){

        Intent intent= new Intent(getApplicationContext(),
                actualizarUsuario.class);
        intent.putExtra("usuario",email);
        intent.putExtra("password",password);

        startActivity(intent);


    }
}