package com.example.ltdd.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ltdd.model.Product;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSanPham extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LTDD_Products";
    private static final int DATABASE_VERSION = 6;

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRODUCT_LINE = "product_line";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_IMEI = "imei";
    public static final String COLUMN_RAM = "ram";
    public static final String COLUMN_ROM = "rom";
    public static final String COLUMN_SCREEN_SIZE = "screen_size";
    public static final String COLUMN_SCREEN_QUALITY = "screen_quality";
    public static final String COLUMN_PROCESSOR = "processor";
    public static final String COLUMN_PIN = "pin";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_PATH = "image_path";

    public static final String TABLE_IMPORT_HISTORY = "import_history";
    public static final String COLUMN_IMP_ID = "imp_id";
    public static final String COLUMN_IMP_CODE = "bill_code";
    public static final String COLUMN_IMP_PRODUCT_NAME = "product_name";
    public static final String COLUMN_IMP_SUPPLIER_NAME = "supplier_name";
    public static final String COLUMN_IMP_SUPPLIER_PHONE = "supplier_phone";
    public static final String COLUMN_IMP_QUANTITY = "quantity";
    public static final String COLUMN_IMP_PRICE = "import_price";
    public static final String COLUMN_IMP_DATE = "import_date";

    public DatabaseSanPham(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_LINE + " TEXT, " +
                COLUMN_MODEL + " TEXT, " +
                COLUMN_IMEI + " TEXT, " +
                COLUMN_RAM + " TEXT, " +
                COLUMN_ROM + " TEXT, " +
                COLUMN_SCREEN_SIZE + " TEXT, " +
                COLUMN_SCREEN_QUALITY + " TEXT, " +
                COLUMN_PROCESSOR + " TEXT, " +
                COLUMN_PIN + " TEXT, " +
                COLUMN_COLOR + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_IMAGE_PATH + " TEXT" +
                ");";
        db.execSQL(createTable);

        String createImportTable = "CREATE TABLE " + TABLE_IMPORT_HISTORY + " (" +
                COLUMN_IMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMP_CODE + " TEXT, " +
                COLUMN_IMP_PRODUCT_NAME + " TEXT, " +
                COLUMN_IMP_SUPPLIER_NAME + " TEXT, " +
                COLUMN_IMP_SUPPLIER_PHONE + " TEXT, " +
                COLUMN_IMP_QUANTITY + " INTEGER, " +
                COLUMN_IMP_PRICE + " REAL, " +
                COLUMN_IMP_DATE + " TEXT" +
                ");";
        db.execSQL(createImportTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMPORT_HISTORY);
        onCreate(db);
    }

    // Lấy tổng nhập của 1 sản phẩm theo tháng
    public int getMonthlyImportQtyByProduct(String model, String monthYear) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT SUM(" + COLUMN_IMP_QUANTITY + ") FROM " + TABLE_IMPORT_HISTORY + 
                     " WHERE " + COLUMN_IMP_PRODUCT_NAME + " = ? AND " + COLUMN_IMP_DATE + " LIKE ?";
        Cursor cursor = db.rawQuery(sql, new String[]{model, "%" + monthYear + "%"});
        int total = 0;
        if (cursor.moveToFirst()) total = cursor.getInt(0);
        cursor.close();
        return total;
    }

    public int getMonthlyImportQuantity(String monthYear) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT SUM(" + COLUMN_IMP_QUANTITY + ") FROM " + TABLE_IMPORT_HISTORY + 
                     " WHERE " + COLUMN_IMP_DATE + " LIKE ?";
        Cursor cursor = db.rawQuery(sql, new String[]{"%" + monthYear + "%"});
        int total = 0;
        if (cursor.moveToFirst()) total = cursor.getInt(0);
        cursor.close();
        return total;
    }

    public double getMonthlyImportValue(String monthYear) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT SUM(" + COLUMN_IMP_QUANTITY + " * " + COLUMN_IMP_PRICE + ") FROM " + TABLE_IMPORT_HISTORY + 
                     " WHERE " + COLUMN_IMP_DATE + " LIKE ?";
        Cursor cursor = db.rawQuery(sql, new String[]{"%" + monthYear + "%"});
        double total = 0;
        if (cursor.moveToFirst()) total = cursor.getDouble(0);
        cursor.close();
        return total;
    }

    public int getCurrentTotalStock() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT SUM(" + COLUMN_QUANTITY + ") FROM " + TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(sql, null);
        int total = 0;
        if (cursor.moveToFirst()) total = cursor.getInt(0);
        cursor.close();
        return total;
    }

    public long addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_LINE, product.getProductLine());
        values.put(COLUMN_MODEL, product.getModel());
        values.put(COLUMN_IMEI, product.getImei());
        values.put(COLUMN_RAM, product.getRam());
        values.put(COLUMN_ROM, product.getRom());
        values.put(COLUMN_SCREEN_SIZE, product.getScreenSize());
        values.put(COLUMN_SCREEN_QUALITY, product.getScreenQuality());
        values.put(COLUMN_PROCESSOR, product.getProcessor());
        values.put(COLUMN_PIN, product.getPin());
        values.put(COLUMN_COLOR, product.getColor());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE_PATH, product.getImagePath());
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public void addImportRecord(String billCode, String productName, String supplierName, String phone, int qty, double price, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMP_CODE, billCode);
        values.put(COLUMN_IMP_PRODUCT_NAME, productName);
        values.put(COLUMN_IMP_SUPPLIER_NAME, supplierName);
        values.put(COLUMN_IMP_SUPPLIER_PHONE, phone);
        values.put(COLUMN_IMP_QUANTITY, qty);
        values.put(COLUMN_IMP_PRICE, price);
        values.put(COLUMN_IMP_DATE, date);
        db.insert(TABLE_IMPORT_HISTORY, null, values);
        db.close();
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_LINE, product.getProductLine());
        values.put(COLUMN_MODEL, product.getModel());
        values.put(COLUMN_IMEI, product.getImei());
        values.put(COLUMN_RAM, product.getRam());
        values.put(COLUMN_ROM, product.getRom());
        values.put(COLUMN_SCREEN_SIZE, product.getScreenSize());
        values.put(COLUMN_SCREEN_QUALITY, product.getScreenQuality());
        values.put(COLUMN_PROCESSOR, product.getProcessor());
        values.put(COLUMN_PIN, product.getPin());
        values.put(COLUMN_COLOR, product.getColor());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE_PATH, product.getImagePath());
        return db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
    }

    public void updateProductStock(int id, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, newQuantity);
        db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
        if (cursor.moveToFirst()) {
            do {
                Product p = new Product();
                p.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                p.setProductLine(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_LINE)));
                p.setModel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL)));
                p.setImei(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMEI)));
                p.setRam(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RAM)));
                p.setRom(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROM)));
                p.setScreenSize(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SCREEN_SIZE)));
                p.setScreenQuality(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SCREEN_QUALITY)));
                p.setProcessor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROCESSOR)));
                p.setPin(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIN)));
                p.setColor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR)));
                p.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)));
                p.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                p.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)));
                productList.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Product> searchProducts(String query) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_MODEL + " LIKE ? OR " +
                COLUMN_PRODUCT_LINE + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + (query == null ? "" : query) + "%", "%" + (query == null ? "" : query) + "%"};
        Cursor cursor = db.rawQuery(searchQuery, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                Product p = new Product();
                p.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                p.setProductLine(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_LINE)));
                p.setModel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL)));
                p.setImei(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMEI)));
                p.setRam(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RAM)));
                p.setRom(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROM)));
                p.setScreenSize(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SCREEN_SIZE)));
                p.setScreenQuality(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SCREEN_QUALITY)));
                p.setProcessor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROCESSOR)));
                p.setPin(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIN)));
                p.setColor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR)));
                p.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)));
                p.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                p.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)));
                productList.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }
}
