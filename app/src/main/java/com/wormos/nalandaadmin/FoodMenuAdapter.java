package com.wormos.nalandaadmin;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class FoodMenuAdapter extends FirebaseRecyclerAdapter<FoodMenuModel,FoodMenuAdapter.foodMenuItemViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    String weekDay;

    public FoodMenuAdapter(@NonNull FirebaseRecyclerOptions<FoodMenuModel> options, String weekDay) {
        super(options);
        this.weekDay = weekDay;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull foodMenuItemViewHolder holder, int position, @NonNull FoodMenuModel model) {
        holder.foodList.setText(model.getMenu());
        holder.messTime.setText(model.getTime());

        if(Objects.equals(getRef(position).getKey(), "005lunch")){
            holder.dash.setBackgroundColor(Color.parseColor("#A6F6945D"));
            holder.edge.setBackgroundColor(Color.parseColor("#F6945D"));
            holder.foodList.setBackgroundColor(Color.parseColor("#66F6945D"));
        } else if(Objects.equals(getRef(position).getKey(), "010snacks")){
            holder.dash.setBackgroundColor(Color.parseColor("#A6E23424"));
            holder.edge.setBackgroundColor(Color.parseColor("#E23424"));
            holder.foodList.setBackgroundColor(Color.parseColor("#66E23424"));
        }else if(Objects.equals(getRef(position).getKey(), "015dinner")){
            holder.dash.setBackgroundColor(Color.parseColor("#A6ffb703"));
            holder.edge.setBackgroundColor(Color.parseColor("#ffb703"));
            holder.foodList.setBackgroundColor(Color.parseColor("#66ffb703"));
        }

        holder.editMenu.setOnClickListener(view -> {
            if(holder.editSaveTv.getText().equals("Edit")){
                holder.editSaveTv.setText("Save");
                holder.foodList.setFocusableInTouchMode(true);
            } else {
                holder.editSaveTv.setText("Saving");
                DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("Food Menu").child(weekDay);
                HashMap<String,Object> menuMap = new HashMap<>();
                menuMap.put("menu",holder.foodList.getText().toString());
                menuRef.child(Objects.requireNonNull(getRef(position).getKey())).updateChildren(menuMap)
                        .addOnSuccessListener(success->{
                            holder.editSaveTv.setText("Edit");
                            holder.foodList.setFocusableInTouchMode(false);
                        });
            }
        });

    }

    @NonNull
    @Override
    public foodMenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_menu_item,parent,false);
        return new foodMenuItemViewHolder(view);
    }

    static class foodMenuItemViewHolder extends RecyclerView.ViewHolder{

        TextView messTime,dash,edge,editSaveTv;
        EditText foodList;
        CardView editMenu;

        public foodMenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            messTime = itemView.findViewById(R.id.mess_time);
            foodList = itemView.findViewById(R.id.food_menu_list);
            edge = itemView.findViewById(R.id.food_item_sidebar);
            dash = itemView.findViewById(R.id.food_item_sidedash);
            editMenu = itemView.findViewById(R.id.food_menu_item_edit_cv);
            editSaveTv = itemView.findViewById(R.id.food_menu_save_edit_btn_txt);
        }
    }
}
