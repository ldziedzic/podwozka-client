package podwozka.podwozka.Passenger;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import podwozka.podwozka.R;
import podwozka.podwozka.entity.Travel;

public class BrowseTravelsActivityAdapter extends RecyclerView.Adapter<BrowseTravelsActivityAdapter.MyViewHolder> {

    private List<Travel> travelsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, freeSpace;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name);
            date = (TextView) view.findViewById(R.id.startTime);
            freeSpace = (TextView) view.findViewById(R.id.freeSpace);
        }
    }


    public BrowseTravelsActivityAdapter(List<Travel> travelsList) {
        this.travelsList = travelsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_browse_travels_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Travel travel = travelsList.get(position);
        String title = travel.getStartPlace() + " - " + travel.getEndPlace();
        holder.title.setText(title);
        String startDatetime = "Czas odjazdu: " + travel.getStartDatetime();
        holder.date.setText(startDatetime);
        String freeSpace = "Wolne miejsca: " + travel.getPassengersCount();
        holder.freeSpace.setText(freeSpace);
    }

    @Override
    public int getItemCount() {
        return travelsList.size();
    }
}