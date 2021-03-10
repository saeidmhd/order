package com.mahak.order.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.mahak.order.BaseActivity;
import com.mahak.order.R;
import com.mahak.order.storage.DbSchema;

public class SortDialogActivity extends AppCompatActivity {
    RadioGroup sort_base;
    RadioGroup sort_direction;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_limit);

        if (getIntent() != null)
            type = getIntent().getStringExtra("type");

        sort_base = findViewById(R.id.sort_base);
        sort_direction = findViewById(R.id.sort_direction);

        if (BaseActivity.getPrefSortBase().equals(DbSchema.Productschema.COLUMN_NAME) || BaseActivity.getPrefSortBase_customer().equals(DbSchema.Customerschema.COLUMN_NAME))
            sort_base.check(R.id.NameRB);
        else
            sort_base.check(R.id.CodeRB);

        if (BaseActivity.getPrefSortDirection().equals("Asc"))
            sort_direction.check(R.id.ascRB);
        else
            sort_direction.check(R.id.descRB);

    }

    public void sort_done(View view) {

        if (sort_base.getCheckedRadioButtonId() == R.id.NameRB) {
            if (type.equals("product"))
                BaseActivity.setPrefSortBase(DbSchema.Productschema.COLUMN_NAME);
            else if (type.equals("customer"))
                BaseActivity.setPrefSortBase_customer(DbSchema.Customerschema.COLUMN_NAME);
        } else {
            if (type.equals("product"))
                BaseActivity.setPrefSortBase(DbSchema.Productschema.COLUMN_PRODUCT_CODE);
            else if (type.equals("customer"))
                BaseActivity.setPrefSortBase_customer(DbSchema.Customerschema.COLUMN_PersonCode);
        }
        if (sort_direction.getCheckedRadioButtonId() == R.id.ascRB)
            BaseActivity.setPrefSortDirection("Asc");
        else
            BaseActivity.setPrefSortDirection("Desc");

        setResult(RESULT_OK);
        finish();

    }

    public void cancel(View view) {
        finish();
    }
}
