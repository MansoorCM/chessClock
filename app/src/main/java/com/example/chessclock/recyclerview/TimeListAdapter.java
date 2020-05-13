package com.example.chessclock.recyclerview;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chessclock.R;
import com.example.chessclock.data.TimeControl;

import java.util.List;

public class TimeListAdapter extends RecyclerView.Adapter<TimeListAdapter.TimeViewHolder>{
    private List<TimeControl> mAlltimes;
    public onClickItemListener onClickItemListener;
    private View selectedView;

    public TimeListAdapter(onClickItemListener onClickItemListener)
    {
        this.onClickItemListener =onClickItemListener;
    }


    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recyclerviewitem,parent,false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mAlltimes !=null)
        {
            return mAlltimes.size();
        }
        return 0;
    }

    public void setmAlltimes(List<TimeControl> mAlltimes) {
        this.mAlltimes = mAlltimes;
        notifyDataSetChanged();
    }

    class TimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView wordTextView;
        TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView=itemView.findViewById(R.id.wordlistitem);
            itemView.setOnClickListener(this);
        }

        void bind(int position)
        {
            if(mAlltimes.size()!=0)
            {
                String data= mAlltimes.get(position).getName();
                wordTextView.setText(data);
                Log.d("TAG","new data");
            }else
            {
                wordTextView.setText(R.string.no_controls);
            }

        }

        @Override
        public void onClick(View view) {
            TimeControl timeControl=mAlltimes.get(getLayoutPosition());
            int color= Color.parseColor("#A5D6A7");
            if(selectedView!=null )
            {
                if(view!=selectedView)
                {
                    selectedView.setBackgroundColor(0x00000000);
                    view.setBackgroundColor(color);
                }

            }else
            {
                view.setBackgroundColor(color);
            }
            selectedView=view;
            onClickItemListener.onClickrowItem(timeControl);
        }
    }
    public interface onClickItemListener
    {
        void onClickrowItem(TimeControl name);
    }

}
