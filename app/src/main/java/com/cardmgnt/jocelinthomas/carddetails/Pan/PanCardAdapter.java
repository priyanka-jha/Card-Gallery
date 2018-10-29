package com.cardmgnt.jocelinthomas.carddetails.Pan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cardmgnt.jocelinthomas.carddetails.Aadhaar.DataModel;
import com.cardmgnt.jocelinthomas.carddetails.DBHelper;
import com.cardmgnt.jocelinthomas.carddetails.R;

import java.util.List;

/**
 * Created by jocelinthomas on 03/05/18.
 */

public class PanCardAdapter extends RecyclerView.Adapter<PanCardAdapter.Myholder> {
    Context context;
    List<PanModel> dataModelArrayList;

    public PanCardAdapter(Context context,List<PanModel> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    class Myholder extends RecyclerView.ViewHolder {
        TextView pan_name, pan_number;
        Button sharebtn,deletebtn;

        public Myholder(View itemView) {
            super(itemView);

            pan_name = (TextView) itemView.findViewById(R.id.name);
            pan_number = (TextView) itemView.findViewById(R.id.number);
            sharebtn = (Button) itemView.findViewById(R.id.sharebtn);
            deletebtn = (Button) itemView.findViewById(R.id.deletebtn);


        }
    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview, null);
        return new Myholder(view);

    }

    @Override
    public void onBindViewHolder(final Myholder holder, int position) {
        final PanModel panModel = dataModelArrayList.get(position);
        holder.pan_name.setText(panModel.getPan_name());
        holder.pan_number.setText(panModel.getPan_number());

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // alertDialogBuilder.setTitle("Exit");
                alertDialogBuilder.setMessage("Are you sure you want to delete?");
                //alertDialogBuilder.setIcon(R.drawable.pwd);
                alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // delete the item from recycler view and db.
                        deletePanData(holder.getAdapterPosition());

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        holder.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String pan_name = panModel.getPan_name();
                    String pan_num = panModel.getPan_number();

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Hello,\n");
                    sb.append("PAN Card number of "+pan_name+" is '"+pan_num+"'");
                    intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                    intent.setType("text/plain");
                    context.startActivity(Intent.createChooser(intent, "Send To"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }


    public void deletePanData(int pos) {
        //GET ID
        PanModel p = dataModelArrayList.get(pos);
        int id1=p.getPanid();
        System.out.println("id1:::::::::" +id1);

        //DELETE FROM DB
        DBHelper db = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        if (db.deletepan(id1))
        {
            dataModelArrayList.remove(pos);
            Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();


        }
        else
        {
            Toast.makeText(context,"Unable To Delete",Toast.LENGTH_SHORT).show();
        }

        db.close();

        this.notifyItemRemoved(pos);

    }

}
