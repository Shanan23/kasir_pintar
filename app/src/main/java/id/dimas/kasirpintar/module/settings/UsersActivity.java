package id.dimas.kasirpintar.module.settings;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import id.dimas.kasirpintar.model.Users;

public class UsersActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private LinearLayout searchLayout;
    private ImageView searchIcon;
    private EditText editTextSearch;
    private ImageButton clearButton;
    private RecyclerView recyclerViewUser;
    private Context mContext;
    private AppDatabase appDatabase;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private List<Users> userList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);
        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        searchIcon = (ImageView) findViewById(R.id.searchIcon);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        clearButton = (ImageButton) findViewById(R.id.clearButton);
        recyclerViewUser = (RecyclerView) findViewById(R.id.recyclerViewUser);


        mContext = this;
        appDatabase = MyApp.getAppDatabase();
        sharedPreferenceHelper = new SharedPreferenceHelper(this);

        cvBack.setOnClickListener(v -> finish());
        tvLeftTitle.setText("Kelola User");
        tvRightTitle.setVisibility(View.GONE);

        userList = new ArrayList<>();

        userAdapter = new UserAdapter(mContext, userList, new UserAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Users users) {

            }

            @Override
            public void onDeleteClick(Users users) {

                Date currentDate = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(currentDate);

                users.setActive(false);
                users.setDeletedAt(formattedDate);
                new Thread(() -> appDatabase.usersDao().upsertUsers(users)).start();
            }
        });

        recyclerViewUser.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUser.setAdapter(userAdapter);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");
            }
        });

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
                filterList(searchText);
                updateClearButtonVisibility();
            }
        });
    }

    private void updateClearButtonVisibility() {
        clearButton.setVisibility(editTextSearch.getText().length() > 0 ? View.VISIBLE : View.GONE);
    }

    private void filterList(String query) {
        // Perform filtering logic based on the query
        List<Users> filteredList = new ArrayList<>();

        for (Users users : userList) {
            if (users.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(users);
            }
        }

        userAdapter.setFilter(filteredList);
    }


    private void retrieveUsers() {
        new Thread(() -> {
            List<Users> allUsersByOutlet = appDatabase.usersDao().getAllUsersByOutlet(sharedPreferenceHelper.getShopId());
            List<Users> activeUsers = new ArrayList<>();
            for (Users entity : allUsersByOutlet) {
                if (entity.getDeletedAt() == null) {
                    activeUsers.add(entity);
                }
            }

            // Update the noteList and notify the adapter
            userList.clear();
            userList.addAll(activeUsers);

            runOnUiThread(() ->
                    userAdapter.setFilter(userList)
            );
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveUsers();

    }
}