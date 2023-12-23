package id.dimas.kasirpintar.module.product;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.ImageUtils;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.Categories;
import id.dimas.kasirpintar.model.Products;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private CardView cvImgProduct;
    private ImageView ivImgProduct;
    private TextInputEditText etProductName;
    private TextInputEditText etStock;
    private CheckBox cbStock;
    private TextInputEditText etType;
    private TextInputEditText etBuyPrice;
    private TextInputEditText etSellPrice;
    private AppCompatAutoCompleteTextView actCategory;
    private Button addCategory;
    private CardView ivAddProduct;
    private AppDatabase appDatabase;
    private Products products;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);
        cvImgProduct = (CardView) findViewById(R.id.cvImgProduct);
        ivImgProduct = (ImageView) findViewById(R.id.ivImgProduct);
        etProductName = (TextInputEditText) findViewById(R.id.etProductName);
        etStock = (TextInputEditText) findViewById(R.id.etStock);
        cbStock = (CheckBox) findViewById(R.id.cbStock);
        etType = (TextInputEditText) findViewById(R.id.etType);
        etBuyPrice = (TextInputEditText) findViewById(R.id.etBuyPrice);
        etSellPrice = (TextInputEditText) findViewById(R.id.etSellPrice);
        actCategory = (AppCompatAutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        addCategory = (Button) findViewById(R.id.addCategory);
        ivAddProduct = (CardView) findViewById(R.id.ivAddProduct);


        cvBack.setOnClickListener(v -> finish());
        tvLeftTitle.setText("Kelola Produk");
        tvRightTitle.setVisibility(View.GONE);

        appDatabase = MyApp.getAppDatabase();
        sharedPreferenceHelper = new SharedPreferenceHelper(this);

        products = new Products();

        // Retrieve the 'Products' object from the extras
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_EXTRA")) {
            products = (Products) intent.getSerializableExtra("PRODUCT_EXTRA");
            etProductName.setText(products != null ? products.getName() : "");
            etStock.setText(products != null ? String.valueOf(products.getStock()) : "");
            cbStock.setActivated(Boolean.parseBoolean(products != null ? products.getIsStock() : "false"));
            etType.setText(products != null ? String.valueOf(products.getType()) : "");
            etBuyPrice.setText(products != null ? String.valueOf(products.getBuyPrice()) : "");
            etSellPrice.setText(products != null ? String.valueOf(products.getSellPrice()) : "");
            actCategory.setText(products != null ? String.valueOf(products.getIdCategory()) : "");
            if (products != null) {
                if (products.getImage() != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(products.image, 0, products.image.length);

                    Glide.with(this)
                            .load(bitmap)
                            .placeholder(R.drawable.ic_upload) // Optional placeholder
                            .error(R.drawable.ic_upload) // Optional error placeholder
                            .into(ivImgProduct);
                }
            }
        }

        addCategory.setOnClickListener(v -> {
            if (actCategory.getText().toString().isEmpty()) {
                actCategory.setError("Kategori tidak boleh kosong");
                return;
            }

            Date currentDate = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            Categories categories = new Categories();
            categories.setName(actCategory.getText().toString().toUpperCase(Locale.ROOT));
            categories.setCreatedAt(formattedDate);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    categories.setIdOutlet(sharedPreferenceHelper.getShopId());
                    long upsertCategories = appDatabase.categoriesDao().upsertCategories(categories);
                    if (upsertCategories > 0) {
                        Log.d("upsertCategories", "berhasil upsertCategories");

                    } else {
                        Log.e("upsertCategories", "gagal upsertCategories");
                    }
                }
            }).start();
        });

        // Sample data for AutoCompleteTextView suggestions
        AtomicReference<List<String>> suggestions = new AtomicReference<>(new ArrayList<>());

        new Thread(() -> {
            List<String> categoriesName = appDatabase.categoriesDao().getAllCategoriesName();
            suggestions.set(categoriesName);
            // Set up AutoCompleteTextView with suggestions
            ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, suggestions.get());
            runOnUiThread(() -> actCategory.setAdapter(autoCompleteAdapter));
        }).start();


        ivAddProduct.setOnClickListener(v -> {
            addCategory.performClick();

            int stock = 0;
            if (etProductName.getText().toString().isEmpty()) {
                etProductName.setError("Nama Produk tidak boleh kosong");
                return;
            }
            if (!cbStock.isChecked()) {
                if (etStock.getText().toString().isEmpty()) {
                    etStock.setError("Stok tidak boleh kosong");
                    return;
                } else {
                    stock = Integer.parseInt(etStock.getText().toString());
                }
            } else {
                if (etStock.getText().toString().isEmpty()) {
                    stock = 0;
                } else {
                    stock = Integer.parseInt(etStock.getText().toString());
                }
            }
            if (etType.getText().toString().isEmpty()) {
                etType.setError("Satuan tidak boleh kosong");
                return;
            }
            if (etBuyPrice.getText().toString().isEmpty()) {
                etBuyPrice.setError("Harga beli tidak boleh kosong");
                return;
            }
            if (etSellPrice.getText().toString().isEmpty()) {
                etSellPrice.setError("Harga jual tidak boleh kosong");
                return;
            }
            if (actCategory.getText().toString().isEmpty()) {
                actCategory.setError("Kategori tidak boleh kosong");
                return;
            }

            products.setName(etProductName.getText().toString());
            products.setStock(stock);
            products.setIsStock(String.valueOf(cbStock.isChecked()));
            products.setType(etType.getText().toString().toLowerCase(Locale.ROOT));
            products.setBuyPrice(etBuyPrice.getText().toString());
            products.setSellPrice(etSellPrice.getText().toString());
            products.setIdCategory(actCategory.getText().toString().toUpperCase(Locale.ROOT));

            Date currentDate = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            products.setCreatedAt(formattedDate);

            new Thread(() -> {
                products.setIdOutlet(sharedPreferenceHelper.getShopId());
                long upsertProduct = appDatabase.productsDao().upsertProducts(products);
                if (upsertProduct > 0) {
                    Log.d("upsertProduct", "berhasil upsertProduct");

                } else {
                    Log.e("upsertProduct", "gagal upsertProduct");
                }
                finish();
            }).start();
        });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleImagePickerResult(result.getResultCode(), result.getData()));

        cvImgProduct.setOnClickListener(view -> openImagePicker());
    }

    private void handleImagePickerResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            byte[] imageByteArray = ImageUtils.uriToByteArray(this, selectedImageUri);

            if (imageByteArray != null) {
                validateAndSaveImage(imageByteArray);
            } else {
                // Handle the case when conversion fails
                Toast.makeText(this, "Image size exceeds the allowed limit (500 KB)", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void validateAndSaveImage(byte[] imageData) {
        // Perform size validation
        long maxSize = 500 * 1024; // 500 KB

        if (imageData.length > maxSize) {

            Glide.with(this)
                    .load(R.drawable.ic_upload)
                    .placeholder(R.drawable.ic_upload) // Optional placeholder
                    .error(R.drawable.ic_upload) // Optional error placeholder
                    .into(ivImgProduct);

            // Show an error message or take appropriate action
            Toast.makeText(this, "Image size exceeds the allowed limit (500 KB)", Toast.LENGTH_SHORT).show();
        } else {
            // Save the image to Room
            products.setImage(imageData);

            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            Glide.with(this)
                    .load(bitmap)
                    .placeholder(R.drawable.ic_upload) // Optional placeholder
                    .error(R.drawable.ic_upload) // Optional error placeholder
                    .into(ivImgProduct);
        }
    }
}