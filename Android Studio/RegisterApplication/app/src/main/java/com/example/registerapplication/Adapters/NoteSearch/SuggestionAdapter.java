package com.example.registerapplication.Adapters.NoteSearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.registerapplication.Entity.NoteSearch.SuggestionItem;
import com.example.registerapplication.R;

import java.util.List;

import lombok.NonNull;


public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {
    private List<SuggestionItem> suggestionList;
    private OnItemClickListener listener;

    public SuggestionAdapter(List<SuggestionItem> suggestionList) {
        this.suggestionList = suggestionList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suggestion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SuggestionItem item = suggestionList.get(position);
        holder.title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.suggestion_title);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(suggestionList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SuggestionItem item);
    }
}