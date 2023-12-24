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
import id.dimas.kasirpintar.model.Categories;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;
    private List<Categories> categoriesList;
    private EditText editTextSearch;
    private ImageButton clearButton;
    private CardView ivAddCategory;
    private AppDatabase appDatabase;
    private SharedPreferenceHelper sharedPreferenceHelper;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        recyclerViewCategory = view.findViewById(R.id.recyclerViewUser);
        ivAddCategory = view.findViewById(R.id.ivAddCategory);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        clearButton = view.findViewById(R.id.clearButton);

        // Initialize RecyclerView and adapter
        appDatabase = MyApp.getAppDatabase();
        sharedPreferenceHelper = new SharedPreferenceHelper(requireContext());

        categoriesList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(requireContext(), categoriesList, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Categories categories) {

                // Create an Intent to start the detail activity
                Intent intent = new Intent(requireContext(), ProductDetailActivity.class);

                // Put the 'Products' object as an extra in the Intent
                intent.putExtra("PRODUCT_EXTRA", categories);

                // Start the activity
                requireContext().startActivity(intent);
            }

            @Override
            public void onDeleteClick(Categories categories) {
                Date currentDate = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(currentDate);

                categories.setDeletedAt(formattedDate);
                categories.setIdOutlet(sharedPreferenceHelper.getShopId());
                new Thread(() -> appDatabase.categoriesDao().upsertCategories(categories)).start();
            }
        });
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCategory.setAdapter(categoryAdapter);


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

        ivAddCategory.setOnClickListener(v -> {
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
        List<Categories> filteredList = new ArrayList<>();

        for (Categories categories : categoriesList) {
            if (categories.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(categories);
            }
        }

        categoryAdapter.setFilter(filteredList);
    }

    private void retrieveCategories() {
        new Thread(() -> {
            List<Categories> allCategories = appDatabase.categoriesDao().getAllCategoriesById(sharedPreferenceHelper.getShopId());
            List<Categories> activeProduct = new ArrayList<>();
            for (Categories entity : allCategories) {
                if (entity.getDeletedAt() == null) {
                    activeProduct.add(entity);
                }
            }

            // Update the noteList and notify the adapter
            categoriesList.clear();
            categoriesList.addAll(activeProduct);

            requireActivity().runOnUiThread(() ->
                    categoryAdapter.setFilter(categoriesList)
            );
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveCategories();
    }
}