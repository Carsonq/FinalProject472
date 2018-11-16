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
import csc472.depaul.edu.finalproject.db.Transaction;

public class TransactionLoadViewAdapter extends RecyclerView.Adapter<TransactionLoadViewAdapter.ViewHolder> {
    private Context context;
    private List<Transaction> transactionList;
    private TransactionLoadViewListener transactionLoadViewListener;

    public TransactionLoadViewAdapter(Context context) {
        this.context = context;
        this.transactionList = new ArrayList<>();
    }

    // if the row doesn't exist it will call this to create it!
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_transaction_detail_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // provides the data to each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.seller_name.setText(transaction.getTransactionName());
        holder.bill_amount.setText(Double.toString(transaction.getTransactionAmount()));
        holder.purchase_date.setText(transaction.getTransactionDate());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void setData(List<Transaction> newData) {
        if (this.transactionList != null) {
            PostDiffCallback postDiffCallback = new PostDiffCallback(this.transactionList, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);
            this.transactionList.clear();
            this.transactionList.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        } else {
            this.transactionList = newData;
        }
    }

    // The ViewHolder should map your recyler view element
    // it wil stores views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView seller_name;
        private TextView bill_amount;
        private TextView purchase_date;

        public ViewHolder(View itemView) {
            super(itemView);

            seller_name = itemView.findViewById(R.id.seller_name);
            bill_amount = itemView.findViewById(R.id.bill_amount);
            purchase_date = itemView.findViewById(R.id.purchase_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (transactionLoadViewListener != null) {
                transactionLoadViewListener.onColorClick(view, getAdapterPosition());
            }
        }
    }

    // getting value at click position
    public String getItem(int id) {
        return Integer.toString(id * 100000);
    }

    // allows clicks events to be caught
    public void setClickListener(TransactionLoadViewListener oTransactionLoadViewListener) {
        transactionLoadViewListener = oTransactionLoadViewListener;
    }


    class PostDiffCallback extends DiffUtil.Callback {

        private final List<Transaction> oldtransactions, newTransactions;

        public PostDiffCallback(List<Transaction> oldAccounts, List<Transaction> newAccounts) {
            this.oldtransactions = oldAccounts;
            this.newTransactions = newAccounts;
        }

        @Override
        public int getOldListSize() {
            return oldtransactions.size();
        }

        @Override
        public int getNewListSize() {
            return newTransactions.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldtransactions.get(oldItemPosition).getTransactionId() == newTransactions.get(newItemPosition).getTransactionId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldtransactions.get(oldItemPosition).equals(newTransactions.get(newItemPosition));
        }
    }
}

