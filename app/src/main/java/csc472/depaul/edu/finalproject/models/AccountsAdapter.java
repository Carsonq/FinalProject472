package csc472.depaul.edu.finalproject.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.db.Account;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {

    private Context context;
    private List<Account> accountList;

    public AccountsAdapter(Context context) {
        this.context = context;
        this.accountList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_account_management_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = accountList.get(position);
        holder.accountInfo.setText(account.getInstitutionName().toUpperCase() + " -- " + account.getAccountSubtype().toUpperCase());

        switch (account.getInstitutionID().toString()) {
            case "ins_1":
                holder.bankLogo.setImageResource(R.drawable.bank_of_america);
                break;
            case "ins_2":
                holder.bankLogo.setImageResource(R.drawable.bbt);
                break;
            case "ins_3":
                holder.bankLogo.setImageResource(R.drawable.chase);
                break;
            case "ins_4":
                holder.bankLogo.setImageResource(R.drawable.wells_fargo);
                break;
            case "ins_5":
                holder.bankLogo.setImageResource(R.drawable.citi);
                break;
            case "ins_6":
                holder.bankLogo.setImageResource(R.drawable.us_bank);
                break;
            case "ins_9":
                holder.bankLogo.setImageResource(R.drawable.capone);
                break;
            case "ins_14":
                holder.bankLogo.setImageResource(R.drawable.tdbank);
                break;
            case "ins_15":
                holder.bankLogo.setImageResource(R.drawable.navy_federal);
                break;
            case "ins_16":
                holder.bankLogo.setImageResource(R.drawable.suntrust);
                break;
            case "ins_19":
                holder.bankLogo.setImageResource(R.drawable.regions);
                break;
            case "ins_20":
                holder.bankLogo.setImageResource(R.drawable.citizens);
                break;
            case "ins_21":
                holder.bankLogo.setImageResource(R.drawable.huntington);
                break;
            default:
                holder.bankLogo.setImageResource(R.drawable.usaa);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public List<Account> getAccountList() {
        return accountList;
    }


    public void setData(List<Account> newData) {
//        this.accountList = newData;
//        notifyDataSetChanged();

        if (this.accountList != null) {
            PostDiffCallback postDiffCallback = new PostDiffCallback(this.accountList, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);
            this.accountList.clear();
            this.accountList.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        } else {
            this.accountList = newData;
        }
    }

    public void removeAccount(int position) {
        accountList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreAccount(Account account, int position) {
        accountList.add(position, account);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView accountInfo;
        public ImageView bankLogo;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            accountInfo = itemView.findViewById(R.id.account_info);
            bankLogo = itemView.findViewById(R.id.account_logo);
            viewBackground = itemView.findViewById(R.id.recycler_accounts_background);
            viewForeground = itemView.findViewById(R.id.recycler_accounts_foreground);
        }
    }

    class PostDiffCallback extends DiffUtil.Callback {

        private final List<Account> oldAccounts, newAccounts;

        public PostDiffCallback(List<Account> oldAccounts, List<Account> newAccounts) {
            this.oldAccounts = oldAccounts;
            this.newAccounts = newAccounts;
        }

        @Override
        public int getOldListSize() {
            return oldAccounts.size();
        }

        @Override
        public int getNewListSize() {
            return newAccounts.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldAccounts.get(oldItemPosition).getAccountId() == newAccounts.get(newItemPosition).getAccountId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldAccounts.get(oldItemPosition).equals(newAccounts.get(newItemPosition));
        }
    }
}
