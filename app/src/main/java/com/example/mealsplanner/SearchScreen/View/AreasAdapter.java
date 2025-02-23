package com.example.mealsplanner.SearchScreen.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mealsplanner.R;
import com.example.mealsplanner.model.Area;

import java.util.List;

public class AreasAdapter extends RecyclerView.Adapter<AreasAdapter.AreaViewHolder>
{
    private final List<Area> areas;
    private OnAreaClickListener listener;

    public AreasAdapter(List<Area> areas) {
        this.areas = areas;
    }


    public OnAreaClickListener getListener() {
        return listener;
    }

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position) {
        holder.bind(areas.get(position));
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public void setListener(OnAreaClickListener listener) {
        this.listener = listener;
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvArea;

        public AreaViewHolder(View itemView) {
            super(itemView);
            tvArea = itemView.findViewById(R.id.tv_area);
        }

        public void bind(Area area) {
            tvArea.setText(area.getName());
            itemView.setOnClickListener(v -> listener.onAreaClick(area));
        }
    }

    public interface OnAreaClickListener {
        void onAreaClick(Area area);
    }


}
