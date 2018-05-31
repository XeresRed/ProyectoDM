package xred.android.juancamilo.instatour.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import xred.android.juancamilo.instatour.Modelos.Api;
import xred.android.juancamilo.instatour.R;

public class ApisAdapter extends RecyclerView.Adapter<ApisAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Api> apiList;
    private List<Api> apiListFiltered;
    private apiAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onApiSelected(apiListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public ApisAdapter(Context context, List<Api> apiList, apiAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.apiList = apiList;
        this.apiListFiltered = apiList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sitios_interes_rows, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Api api = apiListFiltered.get(position);
        holder.name.setText(api.getCate());
        holder.phone.setText("Haz click para ver mas detalles");
        Glide.with(context)
                .load(api.getImg())
                .into(holder.thumbnail);
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
                    List<Api> filteredList = new ArrayList<>();
                    for (Api row : apiList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCate().toLowerCase().contains(charString.toLowerCase())) {
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
                apiListFiltered = (ArrayList<Api>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface apiAdapterListener {
        void onApiSelected(Api contact);
    }

}
