package com.example.jakobsuell.spotd;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import controllers.ImageController;
import enums.AnimalStatus;
import models.Pet;

public class PetsRecyclerAdapter extends RecyclerView.Adapter<PetsRecyclerAdapter.ViewHolder>  {

    private final String TAG = "PetsRecyclerAdapter";
    private final String storageURI;
    private List<Pet> pets;


    public PetsRecyclerAdapter(String storageURI, List<Pet> petsToShow) {
        pets = petsToShow;
        this.storageURI = storageURI;
    }

    public void add(int position, Pet item) {
        pets.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        pets.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public PetsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (this method is invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Pet pet = pets.get(position);
        holder.txtHeader.setText(getPetNameLine(pet));
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "pet " + position + "clicked");
                pets.get(position).show();
                // TODO: Replace this line with actual click functionality
                remove(position);
            }
        });
        holder.txtFooter.setText(getPetInfoLine(pet));
        ImageController.putImageIntoView(storageURI, holder.image, pet.getPetID());
    }

    // Return the size of the dataset (this method is invoked by the layout manager)
    @Override
    public int getItemCount() {
        return pets.size();
    }

    private String getPetInfoLine(Pet pet) {
        StringBuilder info = new StringBuilder();
        if (pet.getStatus() != AnimalStatus.Home) {
            // prefix info with status
            info.append(pet.getStatus().description());
        }
        info.append(" ");
        info.append(pet.getAnimalType().description());
        return info.toString();
    }

    private String getPetNameLine(Pet pet) {
        StringBuilder name = new StringBuilder();
        if (pet.getName() == null || pet.getName() == "") {
            name.append("Name Unknown");
        } else {
            name.append(pet.getName());
        }
        return name.toString();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView image;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = v.findViewById(R.id.firstLine);
            txtFooter = v.findViewById(R.id.secondLine);
            image = v.findViewById(R.id.list_image);
        }
    }
}
