package csc472.depaul.edu.finalproject.models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import csc472.depaul.edu.finalproject.R;

public class TransactionLoadViewAdapter extends RecyclerView.Adapter<TransactionLoadViewAdapter.ViewHolder>
{
    private LayoutInflater layoutInflater;
    private TransactionLoadViewListener transactionLoadViewListener;

    public TransactionLoadViewAdapter(Context context)
    {
        layoutInflater = LayoutInflater.from(context);
    }

    // if the row doesn't exist it will call this to create it!
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = layoutInflater.inflate(R.layout.content_transaction_detail_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // provides the data to each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
//        String sPosition = String.format("Position: %03d", position);
//        holder.seller_name.setText(sPosition);
//
//        String colorCode = String.format("#%06X", (position * 100000));
//        holder.line2.setText("Color: " + colorCode);
//        holder.color.setBackgroundColor(Color.parseColor(colorCode));
    }

    // total number of rows ` //RGB --> 0xFFFFFF is 16711680 - so we take 16711680 / 100000 - 167 rows
    @Override
    public int getItemCount()
    {
        return 0xFFFFFF / 100000; //~167 rows
    }

    // The ViewHolder should map your recyler view element
    // it wil stores views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView seller_name;
        private TextView bill_amount;
        private TextView purchase_date;

        public ViewHolder(View itemView)
        {
            super(itemView);

            seller_name = itemView.findViewById(R.id.seller_name);
            bill_amount = itemView.findViewById(R.id.bill_amount);
            purchase_date = itemView.findViewById(R.id.purchase_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (transactionLoadViewListener != null)
            {
                transactionLoadViewListener.onColorClick(view, getAdapterPosition());
            }
        }
    }

    // getting value at click position
    public String getItem(int id)
    {
        return Integer.toString(id*100000);
    }

    // allows clicks events to be caught
    public void setClickListener(TransactionLoadViewListener oTransactionLoadViewListener)
    {
        transactionLoadViewListener = oTransactionLoadViewListener;
    }
}

