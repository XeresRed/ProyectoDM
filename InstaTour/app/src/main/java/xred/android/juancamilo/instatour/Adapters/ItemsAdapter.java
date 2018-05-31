package xred.android.juancamilo.instatour.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import xred.android.juancamilo.instatour.Json.ItemsTipo1;
import xred.android.juancamilo.instatour.Modelos.Api;
import xred.android.juancamilo.instatour.R;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<ItemsTipo1> apiList;
    private List<ItemsTipo1> apiListFiltered;
    private apiAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, subtitle1, subtitle2,Subtitle3;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tipo_name_1);
            subtitle1 = view.findViewById(R.id.tipo_sub_1);
            subtitle2 = view.findViewById(R.id.tipo_sub_2);
            Subtitle3 = view.findViewById(R.id.tipo_sub_3);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onApiSelected(apiListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public ItemsAdapter(Context context, List<ItemsTipo1> apiList, apiAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.apiList = apiList;
        this.apiListFiltered = apiList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sitios_tipo1_rows, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ItemsTipo1 api = apiListFiltered.get(position);
        String mystring=new String("Ver más");
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        holder.name.setText(api.getNombre());
        holder.subtitle1.setText(api.getDato1());
        holder.subtitle2.setText(api.getDato2());
        holder.Subtitle3.setText(content);
        holder.Subtitle3.setTextColor(holder.Subtitle3.getContext().getResources().getColor(R.color.accent));

    }

    @Override
    public int getItemCount() {
        return apiListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    apiListFiltered = apiList;
                } else {
                    List<ItemsTipo1> filteredList = new ArrayList<>();
                    for (ItemsTipo1 row : apiList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    apiListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = apiListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                apiListFiltered = (ArrayList<ItemsTipo1>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface apiAdapterListener {
        void onApiSelected(ItemsTipo1 contact);
    }

}
