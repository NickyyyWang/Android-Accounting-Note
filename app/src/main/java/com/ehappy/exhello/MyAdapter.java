package com.ehappy.exhello;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {

    interface OnItemClickHandler {
        // 提供onItemClick方法作為點擊事件，括號內為接受的參數
        void onItemClick(String id,String title,String content);
        // 提供onItemRemove做為移除項目的事件
        void onItemRemove(int position, subjects text);
    }

    String id;

    public OnItemClickHandler mClickHandler;

    Button btnRemove;

    List<subjects> subjects;

    public MyAdapter(List<subjects> getDataAdapter,OnItemClickHandler clickHandler){

        super();

        this.subjects = getDataAdapter;

        mClickHandler = clickHandler;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        subjects getDataAdapter1 =  subjects.get(position);

        holder.SubjectName.setText(getDataAdapter1.getTitle());

    }

    @Override
        public int getItemCount() {

            return subjects.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public TextView SubjectName;


        public ViewHolder(View itemView) {

            super(itemView);

            btnRemove = (Button) itemView.findViewById(R.id.btnRemove);

            SubjectName = (TextView) itemView.findViewById(R.id.txtItem) ;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sinId,sinTitle,sinContent;

                    subjects getDataAdapter1 =  subjects.get(getAdapterPosition());

                    sinId=getDataAdapter1.getId();

                    sinTitle=getDataAdapter1.getTitle();

                    sinContent=getDataAdapter1.getContent();

                    mClickHandler.onItemClick(sinId,sinTitle,sinContent);

                }
            });

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 4. 呼叫interface的method

                    removeItem(getAdapterPosition());

                }
            });
        }
    }

    public void removeItem(int position){

        subjects.remove(position);

        notifyItemRemoved(position);

    }
}

