package xred.android.juancamilo.instatour.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import xred.android.juancamilo.instatour.Modelos.Ciudad;
import xred.android.juancamilo.instatour.Modelos.Usuario;
import xred.android.juancamilo.instatour.R;

public class CiudadesAdapter extends RecyclerView.Adapter<CiudadesAdapter.MyViewHolder>{
    private Context mContext;
    private List<Ciudad> albumList;
    private AlbumsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            cardView = view.findViewById(R.id.card_view);
        }
    }


    public CiudadesAdapter(Context mContext, List<Ciudad> albumList, AlbumsAdapterListener listener) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ciudad_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        Ciudad album = albumList.get(position);
        holder.title.setText(album.getNomCiu());
        holder.count.setText(album.getDescrip());

        // loading album cover using Glide library
        Glide.with(mContext).load(album.getImag()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, position);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCardSelected(position, holder.thumbnail);
            }
        });


        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCardSelected(position, holder.thumbnail);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position) {

        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_ciudad, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        int position;

        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    listener.onAddToFavoriteSelected(position);
                    return true;
                case R.id.action_play_next:
                    listener.onPlayNextSelected(position);
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public interface AlbumsAdapterListener {
        void onAddToFavoriteSelected(int position);

        void onPlayNextSelected(int position);

        void onCardSelected(int position, ImageView thumbnail);
    }
}
