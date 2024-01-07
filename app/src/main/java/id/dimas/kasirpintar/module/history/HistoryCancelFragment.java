package id.dimas.kasirpintar.module.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.model.Orders;
import id.dimas.kasirpintar.model.Profit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryCancelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryCancelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private List<Orders> orderList;
    private HistoryAdapter historyAdapter;

    public HistoryCancelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryCancelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryCancelFragment newInstance(String param1, String param2) {
        HistoryCancelFragment fragment = new HistoryCancelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_cancel, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        appDatabase = MyApp.getAppDatabase();
        orderList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(requireContext(), orderList, new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Orders orders) {

            }

            @Override
            public void onDeleteClick(Orders orders) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(historyAdapter);


        return view;
    }

    private void retrieveOrders() {
        new Thread(() -> {
            List<Orders> allOrdersByStatus = appDatabase.ordersDao().getAllOrdersByStatus(MyApp.Status.PENDING.name());

            List<Orders> activeOrders = new ArrayList<>();
            for (Orders entity : allOrdersByStatus) {
                if (entity.getId() != -1) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

                    try {
                        // Parse the input string to LocalDateTime
                        LocalDateTime dateTime = LocalDateTime.parse(entity.orderDate, formatter);

                        // Calculate the duration in hours
                        long hours = ChronoUnit.HOURS.between(dateTime, LocalDateTime.now());

                        // Validate if the duration is greater than 24 hours
                        if (hours > 24) {
                            Profit profit = appDatabase.ordersDetailDao().getAllOrdersDetailItem(entity.getId());
                            entity.setProfitItem(profit);
                            activeOrders.add(entity);
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid date format. Please use dd-MM-yyyy HH:mm format.");
                    }


                }
            }

            // Update the noteList and notify the adapter
            orderList.clear();
            orderList.addAll(activeOrders);

            requireActivity().runOnUiThread(() ->
                    historyAdapter.notifyDataSetChanged()
            );
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveOrders();
    }
}