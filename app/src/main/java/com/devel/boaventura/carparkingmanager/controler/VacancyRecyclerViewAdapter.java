package com.devel.boaventura.carparkingmanager.controler;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.model.Park;
import com.devel.boaventura.carparkingmanager.model.Vacancy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scavenger on 4/18/18.
 *
 */

public class VacancyRecyclerViewAdapter
        extends RecyclerView.Adapter<VacancyRecyclerViewAdapter.VacancyViewHolder>
        implements Filterable {

    private List<Vacancy> m_itemList;
    private List<Vacancy> m_filteredList;

    private Picasso m_imageLoader;

    /*quem vai implementar essa interface sera o fragment!*/
    private OnParkingItemClickListener m_singleClickListener;
    private int m_selectedPosition, m_LongClickSelectedPosition;


    public VacancyRecyclerViewAdapter(Context context, ArrayList<Vacancy> list){
        m_itemList = list;
        m_filteredList = list;
        m_imageLoader = Picasso.with(context);
       // m_context = context;
    }

    public void setParkingItemClickListener(OnParkingItemClickListener listener){
        m_singleClickListener = listener;
    }

    @Override
    public VacancyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemVacancyView = LayoutInflater.from( parent.getContext())
                .inflate(R.layout.item_vaga_layout, parent, false);

        return new VacancyViewHolder( itemVacancyView );
    }

    @Override
    public void onBindViewHolder(VacancyViewHolder holder, int position) {

        Vacancy currentVacancy = m_filteredList.get(position);
        holder.m_vacancyNumber.setText( currentVacancy.getNumber() );

        switch ( currentVacancy.getStatus() ){
            case Vacancy.STATUS_AVAILABLE:
                holder.m_vacancyPlate.setText("");

                switch ( currentVacancy.getType() ){
                    case Vacancy.TYPE_PRIVATE:
                        m_imageLoader
                                .load(R.drawable.private_available)
                                .into(holder.m_vacancyImage);
                        break;

                    case Vacancy.TYPE_ROTARY:
                        m_imageLoader
                                .load(R.drawable.rotary_available)
                                .into(holder.m_vacancyImage);
                        break;
                }
                break;

            case Vacancy.STATUS_UNAVAILABLE:
                holder.m_vacancyPlate.setText( currentVacancy.
                        getCurrentService().getVehicle().getLicensePlate() );

                holder.m_vacancyPlate.setTextColor(Color.RED);

                switch ( currentVacancy.getType() ){
                    case Vacancy.TYPE_PRIVATE:
                        m_imageLoader
                                .load(R.drawable.private_unavailable)
                                .into(holder.m_vacancyImage);
                        break;

                    case Vacancy.TYPE_ROTARY:
                        m_imageLoader
                                .load(R.drawable.rotary_unavailable)
                                .into(holder.m_vacancyImage);
                        break;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return m_filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()){
                    Log.i("DBG", "charString is Empty()");
                    m_filteredList = m_itemList;
                } else {

                    m_filteredList = new ArrayList<Vacancy>();
                    for(Vacancy vacancy: m_itemList){
                        if (vacancy.getStatus() == Vacancy.STATUS_UNAVAILABLE){
                             if (vacancy.getCurrentService()
                                    .getVehicle().getLicensePlate().toUpperCase().contains(
                                            charString.toUpperCase()
                                     ) ){
                                 m_filteredList.add(vacancy);
                             }
                        }
                    } // end of for!

                }
                FilterResults results = new FilterResults();
                results.values = m_filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                m_filteredList = (ArrayList<Vacancy>) results.values;

                Log.i("DBG", "TOTAL SEARCH RESULTS: " + m_filteredList.size());
                notifyDataSetChanged();
            }
        };
    }

    /*Pode ser uma unica interface?? PODE SIM!!*/
    public interface OnParkingItemClickListener{
        void onParkingItemClicked(Vacancy item, int position);
    }


    public int getSelectedPosition(){
        return m_LongClickSelectedPosition;
    }


    /*Metodo utilizado para fornecer ao recyclerView
    * varios layouts*/
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }

    @Override
    public void onViewRecycled(VacancyViewHolder holder) {
        super.onViewRecycled(holder);
    }


    /*****************
     *
     *
     * INCIO DA CLASSE VIEW HOLDER
     *
     *
     * ***************/

    class VacancyViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener,
            View.OnCreateContextMenuListener {

        private ImageView m_vacancyImage;
        private TextView m_vacancyPlate, m_vacancyNumber;

        public VacancyViewHolder(View itemView) {
            super(itemView);

            m_vacancyPlate = (TextView) itemView.findViewById(R.id.itemVacancyTxtPlate);
            m_vacancyNumber = (TextView) itemView.findViewById(R.id.itemVacancyTxtNumber);
            m_vacancyImage = (ImageView) itemView.findViewById(R.id.itemVacancyImage);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (m_singleClickListener != null) {
                int position = this.getAdapterPosition();
                m_singleClickListener.onParkingItemClicked(
                        m_filteredList.get(position), position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Log.i("DBG", "VacancyViewHolder::onLongClick"
                    + " View id: " +v.getId() + " position " + this.getAdapterPosition());
            m_LongClickSelectedPosition = this.getAdapterPosition();
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu,
                                        View v, ContextMenu.ContextMenuInfo menuInfo) {


            MenuItem changeVacancy = menu.add(Menu.NONE, 1,1,
                    R.string.option_change_vacancy);

            MenuItem vacancyDetails = menu.add(Menu.NONE, 2,2,
                    R.string.option_show_vacancy_details);

            MenuItem removeVacancy = menu.add(Menu.NONE, 3, 3,
                    R.string.option_remove_vacancy);
        }

    } // fim da classe ViewHolder

}