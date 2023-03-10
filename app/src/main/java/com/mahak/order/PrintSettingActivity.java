package com.mahak.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.mahak.order.common.Printer;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;

public class PrintSettingActivity extends Activity {

    private int PICK_FROM_GALLERY = 1001;
    private int PICTURE_CROP = 1002;
    private Button btnBrowse, btnDelete , btnSave_close;
    private ImageView imgLogo;
    private Bitmap myLogo;
    private CheckBox chkShowDescription, chkShowLogo, chkShowTitle;
    private EditText txtDescription, txtTitle,txtDescription1,txtDescription2;
    String fPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_IMAGES + "/" + ProjectInfo.DIRECTORY_ASSETS;
    Context mContext;
    private String pageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light_Dialog);
        setTitle(getString(R.string.str_print_setting));
        setContentView(R.layout.activity_print_setting);

        mContext = this;


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pageName = extras.getString(ProjectInfo._TAG_PAGE_NAME);
        }

        init();
        initData();

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, PICK_FROM_GALLERY);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Printer.deleteFile(ProjectInfo.PRINT_LOGO_FILE_NAME, fPath)) {
                    btnDelete.setEnabled(false);
                    imgLogo.setImageDrawable(null);
                }
            }
        });

        btnSave_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description = txtDescription.getText().toString();
                String description1 = txtDescription1.getText().toString();
                String description2 = txtDescription2.getText().toString();
                String txtTitleString = txtTitle.getText().toString();

                if (description.length() != 0) {
                    BaseActivity.setUnderPrintText(mContext, pageName, description);
                } else {
                    BaseActivity.setUnderPrintText(mContext, pageName, "");
                }

                if (description1.length() != 0) {
                    BaseActivity.setUnderPrintText1(mContext, pageName, description1);
                } else {
                    BaseActivity.setUnderPrintText1(mContext, pageName, "");
                }

                if (description2.length() != 0) {
                    BaseActivity.setUnderPrintText2(mContext, pageName, description2);
                } else {
                    BaseActivity.setUnderPrintText2(mContext, pageName, "");
                }

                if (txtTitleString.length() != 0) {
                    BaseActivity.setTitleText(mContext, txtTitleString);
                } else {
                    BaseActivity.setTitleText(mContext, "");
                }
                finish();
            }
        });

        chkShowDescription.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                BaseActivity.setUnderPrintTextStatus(mContext, pageName, b);
            }
        });
        chkShowTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                BaseActivity.setTitleTextStatus(mContext, b);
            }
        });

        chkShowLogo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                BaseActivity.setPrintLogoStatus(mContext, pageName, b);
            }
        });

    }//end of onCreate

    public void initData() {

        txtDescription.setText(BaseActivity.getUnderPrintText(mContext, pageName));
        txtDescription1.setText(BaseActivity.getUnderPrintText1(mContext, pageName));
        txtDescription2.setText(BaseActivity.getUnderPrintText2(mContext, pageName));

        txtTitle.setText(BaseActivity.getTitleText(mContext));
        chkShowDescription.setChecked(BaseActivity.getUnderPrintTextStatus(mContext, pageName));
        chkShowTitle.setChecked(BaseActivity.getTitleStatus(mContext));
        chkShowLogo.setChecked(BaseActivity.getPrintLogoStatus(mContext, pageName));
        myLogo = ServiceTools.getPrintLogo();
        if (myLogo != null) {
            imgLogo.setImageBitmap(myLogo);
            btnDelete.setEnabled(true);
        }
    }

    public void init() {
        btnBrowse = (Button) findViewById(R.id.btnBrowse);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnSave_close = (Button) findViewById(R.id.btnSave_close);
        btnDelete.setEnabled(false);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        chkShowDescription = (CheckBox) findViewById(R.id.chkShowDescription);
        chkShowTitle = (CheckBox) findViewById(R.id.chkShowTitle);
        chkShowLogo = (CheckBox) findViewById(R.id.chkShowLogo);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtDescription1 = (EditText) findViewById(R.id.txtDescription1);
        txtDescription2 = (EditText) findViewById(R.id.txtDescription2);
        txtTitle = (EditText) findViewById(R.id.txtTitle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                cropImage(data);
            }
        } else if (requestCode == PICTURE_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    myLogo = extras.getParcelable("data");
                    imgLogo.setImageBitmap(myLogo);
                    btnDelete.setEnabled(true);
                    Printer.CreateFile(myLogo, ProjectInfo.PRINT_LOGO_FILE_NAME, fPath);
                }
            }
        }
    }

    public void cropImage(Intent data) {
        Uri selectedImage = data.getData();
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(selectedImage, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 100);
        cropIntent.putExtra("outputY", 100);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, PICTURE_CROP);
    }
}
