package id.dimas.kasirpintar.module.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.helper.dao.ProductsDao;
import id.dimas.kasirpintar.model.Products;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewProduct;
    private ProductAdapter productAdapter;
    private List<Products> productList;
    private EditText editTextSearch;
    private ImageButton clearButton;
    private CardView ivAddProduct;
    private AppDatabase appDatabase;
    private SharedPreferenceHelper sharedPreferenceHelper;

    public ProductFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        // Initialize RecyclerView and adapter
        recyclerViewProduct = view.findViewById(R.id.recyclerViewProduct);
        appDatabase = MyApp.getAppDatabase();
        sharedPreferenceHelper = new SharedPreferenceHelper(requireContext());

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(requireContext(), productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Products products) {

                // Create an Intent to start the detail activity
                Intent intent = new Intent(requireContext(), ProductDetailActivity.class);

                // Put the 'Products' object as an extra in the Intent
                intent.putExtra("PRODUCT_EXTRA", products);

                // Start the activity
                requireContext().startActivity(intent);
            }

            @Override
            public void onDeleteClick(Products products) {
                Date currentDate = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(currentDate);

                products.setDeletedAt(formattedDate);
products.setIdOutlet(sharedPreferenceHelper.getShopId());
                new Thread(() -> appDatabase.productsDao().upsertProducts(products)).start();
            }
        });
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProduct.setAdapter(productAdapter);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        clearButton = view.findViewById(R.id.clearButton);
        ivAddProduct = view.findViewById(R.id.ivAddProduct);

        // Set up the clear button click listener
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");
            }
        });

        // Set up the text change listener for the search EditText
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString().trim();
                filterProducts(searchText);
                updateClearButtonVisibility();
            }
        });

        ivAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void updateClearButtonVisibility() {
        clearButton.setVisibility(editTextSearch.getText().length() > 0 ? View.VISIBLE : View.GONE);
    }

    private void filterProducts(String query) {
        // Perform filtering logic based on the query
        List<Products> filteredList = new ArrayList<>();

        for (Products product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }

        productAdapter.setFilter(filteredList);
    }

    private void retrieveProducts() {
        new Thread(() -> {
            ProductsDao productsDao = appDatabase.productsDao();
            List<Products> productsList = productsDao.getAllProducts(sharedPreferenceHelper.getShopId());
            List<Products> activeProduct = new ArrayList<>();
            for (Products entity : productsList) {
                if (entity.getDeletedAt() == null) {
                    activeProduct.add(entity);
                }
            }

            // Update the noteList and notify the adapter
            productList.clear();
            productList.addAll(activeProduct);

            requireActivity().runOnUiThread(() ->
                    productAdapter.setFilter(productList)
            );
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveProducts();
    }
}