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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import Modelos.ProductoModelo;

public class Vendedor extends AppCompatActivity {

    TextView jtvrol,jTvNombre;
    RecyclerView jrvFirestoreApartamentsList;

    String email,rol,password;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);

        getSupportActionBar().hide();

        jTvNombre=findViewById(R.id.etNombre);
        jtvrol=findViewById(R.id.rol);

        jrvFirestoreApartamentsList=findViewById(R.id.rvFirestoreApartamentsList);

        email=getIntent().getStringExtra("coleccion");

        rol=getIntent().getStringExtra("rol");
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
                        Log.d("no encuentra", "No such document");
                    }
                } else {
                    Log.d("Mensaje3", "get failed with ", task.getException());
                }
            }
        });


        Query query=db.collection("Apartaments").whereEqualTo("owner",email);


        FirestoreRecyclerOptions<ProductoModelo> options= new FirestoreRecyclerOptions.Builder<ProductoModelo>()
                .setQuery(query, ProductoModelo.class).build();



        adapter= new FirestoreRecyclerAdapter<ProductoModelo,ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductoModelo model) {


                String owner= model.getOwner();

                holder.tvPais.setText(model.getCountry());
                holder.tvciudad.setText(model.getCity());
                holder.tvDireccion.setText(model.getAdress());
                holder.tvDescripcion.setText(model.getDescription());
                holder.TvEstado.setText(model.getState());
                holder.tvNhabitaciones.setText(model.getBedrooms());
                holder.tvVnoche.setText(model.getPriceNight());

                final String id=getSnapshots().getSnapshot(position).getId();

                holder.jbtnEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(UsuarioActivity.this);
                        builder.setTitle("Eliminar Apartamento.");
                        builder.setMessage("Seguro que desea eliminar el Apartamento?")
                                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        borrarApto(id);

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
                holder.jbtnEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(UsuarioActivity.this,editarAptoActivity.class);
                        intent.putExtra("id",id);

                        startActivity(intent);
                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.apartament_list,parent,false);


                return new ProductViewHolder (view);
            }
        };

        jrvFirestoreApartamentsList.setHasFixedSize(true);
        jrvFirestoreApartamentsList.setLayoutManager(new LinearLayoutManager(this));
        jrvFirestoreApartamentsList.setAdapter(adapter);
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView tvPais,tvciudad,tvDireccion,tvNhabitaciones,tvVnoche,TvEstado,tvDescripcion;
        Button jbtnEliminar, jbtnEditar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPais=itemView.findViewById(R.id.tvPais);
            tvciudad= itemView.findViewById(R.id.tvciudad);
            tvDireccion=itemView.findViewById(R.id.tvDireccion);
            tvNhabitaciones=itemView.findViewById(R.id.tvNhabitaciones);
            tvVnoche=itemView.findViewById(R.id.tvVnoche2);
            TvEstado=itemView.findViewById(R.id.TvEstado);
            tvDescripcion=itemView.findViewById(R.id.tvDescripcion);
            jbtnEliminar= itemView.findViewById(R.id.btnEliminar);
            jbtnEditar=itemView.findViewById(R.id.btnEditar);


        }
    }

    public void ir_registro_apto(View view){

        Intent intent= new Intent(getApplicationContext(),RegistroAptoActivity.class);
        intent.putExtra("coleccion",email);
        startActivity(intent);


    }

    public void borrarApto (String id){

        db.collection("Apartaments").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {


                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Apartamento eliminado",Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error al eliminnar Apartamento",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void AcutualizarDatos (View view){



        Intent intent= new Intent(UsuarioActivity.this, acutalizarUsuarioAnfitrionActivity.class);
        intent.putExtra("coleccion",email);
        intent.putExtra("rol",rol);
        intent.putExtra("password",password);
        startActivity(intent);

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

}