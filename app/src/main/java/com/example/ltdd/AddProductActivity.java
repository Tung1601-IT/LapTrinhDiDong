package com.example.ltdd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ltdd.database.DatabaseSanPham;
import com.example.ltdd.model.Product;

public class AddProductActivity extends AppCompatActivity {

    private Spinner spProductLine, spRam, spRom;
    private EditText etModel, etImei, etScreenSize, etScreenQuality, etProcessor, etPin, etColor, etQuantity, etPrice;
    private Button btnSave, btnChooseImage;
    private TextView tvImageStatus;
    private ImageView ivSelectedImage;
    private DatabaseSanPham dbHelper;
    private Product existingProduct;
    private String selectedImagePath = "";

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        try {
                            getContentResolver().takePersistableUriPermission(imageUri, 
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            selectedImagePath = imageUri.toString();
                            tvImageStatus.setText("Đã chọn: " + imageUri.getLastPathSegment());
                            ivSelectedImage.setImageURI(imageUri);
                            ivSelectedImage.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Toast.makeText(this, "Không thể lấy quyền truy cập ảnh", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        dbHelper = new DatabaseSanPham(this);
        initViews();
        setupSpinners();

        Intent intent = getIntent();
        if (intent.hasExtra("product")) {
            existingProduct = (Product) intent.getSerializableExtra("product");
            fillData(existingProduct);
            ((TextView)findViewById(R.id.tvFormTitle)).setText("Sửa sản phẩm");
        }

        btnChooseImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
            galleryIntent.setType("image/*");
            galleryIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imagePickerLauncher.launch(galleryIntent);
        });

        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void initViews() {
        spProductLine = findViewById(R.id.spProductLine);
        spRam = findViewById(R.id.spRam);
        spRom = findViewById(R.id.spRom);
        etModel = findViewById(R.id.etModel);
        etImei = findViewById(R.id.etImei);
        etScreenSize = findViewById(R.id.etScreenSize);
        etScreenQuality = findViewById(R.id.etScreenQuality);
        etProcessor = findViewById(R.id.etProcessor);
        etPin = findViewById(R.id.etPin);
        etColor = findViewById(R.id.etColor);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        tvImageStatus = findViewById(R.id.tvImageStatus);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupSpinners() {
        String[] lines = {"iPhone", "iPad", "MacBook", "Watch"};
        String[] rams = {"4 GB", "6 GB", "8 GB", "12 GB", "16 GB"};
        String[] roms = {"64 GB", "128 GB", "256 GB", "512 GB", "1024 GB", "2048 GB"};

        spProductLine.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lines));
        spRam.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rams));
        spRom.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roms));
    }

    private void fillData(Product p) {
        setSpinnerValue(spProductLine, p.getProductLine());
        setSpinnerValue(spRam, p.getRam());
        setSpinnerValue(spRom, p.getRom());
        etModel.setText(p.getModel());
        etImei.setText(p.getImei());
        etScreenSize.setText(p.getScreenSize());
        etScreenQuality.setText(p.getScreenQuality());
        etProcessor.setText(p.getProcessor());
        etPin.setText(p.getPin());
        etColor.setText(p.getColor());
        etQuantity.setText(String.valueOf(p.getQuantity()));
        etPrice.setText(String.valueOf(p.getPrice()));
        if (p.getImagePath() != null && !p.getImagePath().isEmpty()) {
            selectedImagePath = p.getImagePath();
            tvImageStatus.setText("Đã có ảnh cũ");
            try {
                ivSelectedImage.setImageURI(Uri.parse(selectedImagePath));
                ivSelectedImage.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        if (adapter == null || value == null) return;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private boolean validateInputs() {
        if (etModel.getText().toString().trim().isEmpty()) {
            etModel.setError("Vui lòng nhập Model");
            return false;
        }
        if (etImei.getText().toString().trim().isEmpty()) {
            etImei.setError("Vui lòng nhập IMEI");
            return false;
        }
        if (etPrice.getText().toString().trim().isEmpty()) {
            etPrice.setError("Vui lòng nhập Giá bán");
            return false;
        }
        if (etQuantity.getText().toString().trim().isEmpty()) {
            etQuantity.setError("Vui lòng nhập Số lượng");
            return false;
        }
        if (selectedImagePath.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ảnh sản phẩm", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveProduct() {
        if (!validateInputs()) {
            return;
        }

        try {
            Product p = existingProduct != null ? existingProduct : new Product();
            // Đã xóa Brand và OldPrice khỏi Product class theo yêu cầu
            p.setProductLine(spProductLine.getSelectedItem().toString());
            p.setModel(etModel.getText().toString().trim());
            p.setImei(etImei.getText().toString().trim());
            p.setRam(spRam.getSelectedItem().toString());
            p.setRom(spRom.getSelectedItem().toString());
            p.setScreenSize(etScreenSize.getText().toString().trim());
            p.setScreenQuality(etScreenQuality.getText().toString().trim());
            p.setProcessor(etProcessor.getText().toString().trim());
            p.setPin(etPin.getText().toString().trim());
            p.setColor(etColor.getText().toString().trim());
            p.setQuantity(Integer.parseInt(etQuantity.getText().toString().trim()));
            p.setPrice(Double.parseDouble(etPrice.getText().toString().trim()));
            p.setImagePath(selectedImagePath);

            if (existingProduct == null) {
                dbHelper.addProduct(p);
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.updateProduct(p);
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            }
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
