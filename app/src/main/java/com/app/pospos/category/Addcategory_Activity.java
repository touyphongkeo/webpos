package com.app.pospos.category;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.onlinesmartpos.R;
import com.app.pospos.Constant;
import com.app.pospos.model.Catgory;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.BaseActivity;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Addcategory_Activity extends BaseActivity {
    ProgressDialog loading;
    TextView btn_add;
    EditText category_name;
    ImageView img_back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        getSupportActionBar().hide();

        btn_add = findViewById(R.id.btn_add);
        category_name = findViewById(R.id.category_name);
        img_back = findViewById(R.id.img_back);



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Addcategory_Activity.this,CategoryActivity.class);
                startActivity(i);
                finish();
            }
        });



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = category_name.getText().toString().trim();
                if (categoryName.isEmpty()) {
                    category_name.setError("ກະລຸນາປ້ອນໜວດໝູ");
                    category_name.requestFocus();

                } else {
                    addCategory(categoryName);

                }
            }
        });


    }


    private void addCategory(String category_name) {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Catgory> call = apiInterface.addCategory(category_name);
        call.enqueue(new Callback<Catgory>() {
            @Override
            public void onResponse(@NonNull Call<Catgory> call, @NonNull Response<Catgory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String value = response.body().getValue();



                    if (value.equals(Constant.KEY_SUCCESS)) {

                        loading.dismiss();

                        Toasty.success(Addcategory_Activity.this, R.string.successfully_added, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Addcategory_Activity.this, CategoryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else if (value.equals(Constant.KEY_EXISTS)) {

                        loading.dismiss();

                        Toasty.error(Addcategory_Activity.this, getString(R.string.already_exists), Toast.LENGTH_SHORT).show();


                    } else if (value.equals(Constant.KEY_FAILURE)) {

                        loading.dismiss();

                        Toasty.error(Addcategory_Activity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        loading.dismiss();
                        Toasty.error(Addcategory_Activity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loading.dismiss();
                    Log.d("Error", "Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Catgory> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(Addcategory_Activity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
