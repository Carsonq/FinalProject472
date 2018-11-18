package csc472.depaul.edu.finalproject.models;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.db.Receipt;

public class ReceiptLoadViewAdapter extends RecyclerView.Adapter<ReceiptLoadViewAdapter.ViewHolder> {
    private Context context;
    private List<Receipt> receiptList;
    private ReceiptLoadViewListener receiptLoadViewListener;

    public ReceiptLoadViewAdapter(Context context) {
        this.context = context;
        this.receiptList = new ArrayList<>();
    }

    // if the row doesn't exist it will call this to create it!
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_receipt_detail_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // provides the data to each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Receipt receipt = receiptList.get(position);
        holder.receipt_amount.setText(Double.toString(receipt.getReceiptTotal()));
        holder.receipt_date.setText(receipt.getReceiptDate());
    }

    @Override
    public int getItemCount() {
        return receiptList.size();
    }

    public void setData(List<Receipt> newData) {
        if (this.receiptList != null) {
            PostDiffCallback postDiffCallback = new PostDiffCallback(this.receiptList, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);
            this.receiptList.clear();
            this.receiptList.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        } else {
            this.receiptList = newData;
        }
    }

    // The ViewHolder should map your recyler view element
    // it wil stores views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView receipt_amount;
        private TextView receipt_date;

        public ViewHolder(View itemView) {
            super(itemView);

            receipt_amount = itemView.findViewById(R.id.receipt_amount);
            receipt_date = itemView.findViewById(R.id.receipt_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (receiptLoadViewListener != null) {
                receiptLoadViewListener.onColorClick(view, getAdapterPosition());
            }
        }
    }

    // getting value at click position
    public String getItem(int id) {
        return Integer.toString(id * 100000);
    }

    // allows clicks events to be caught
    public void setClickListener(ReceiptLoadViewListener oReceiptLoadViewListener) {
        receiptLoadViewListener = oReceiptLoadViewListener;
    }


    class PostDiffCallback extends DiffUtil.Callback {

        private final List<Receipt> oldreceipts, newreceipts;

        public PostDiffCallback(List<Receipt> oldAccounts, List<Receipt> newAccounts) {
            this.oldreceipts = oldAccounts;
            this.newreceipts = newAccounts;
        }

        @Override
        public int getOldListSize() {
            return oldreceipts.size();
        }

        @Override
        public int getNewListSize() {
            return newreceipts.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldreceipts.get(oldItemPosition).getReceiptId() == newreceipts.get(newItemPosition).getReceiptId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldreceipts.get(oldItemPosition).equals(newreceipts.get(newItemPosition));
        }
    }
}

