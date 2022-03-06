package com.mahak.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbSchema;
import com.mahak.order.widget.FontDialog;
import com.mahak.order.widget.FontPopUp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mahak.order.common.ServiceTools.getFileName;
import static com.mahak.order.storage.DbSchema.DATABASE_NAME;

public class BackupListActivity extends BaseActivity {

    private static File[] files;
    private static List<String> lstFilesArray = new ArrayList<String>();
    TextView tvPageTitle;
    private ListView lstShowBackup;
    private Activity mActivity;
    public static final File DATABASE_DIRECTORY = new File(Environment.getExternalStorageDirectory(), ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_BACKUPS);
    private static final File DATA_DIRECTORY_DATABASE = new File(Environment.getDataDirectory() + "/data/" + "com.mahak.order" + "/databases/" + DATABASE_NAME);
    String BackupFileName = "";
    private ShowBackupsArrayAdapter adapter;
    private int Position;
    private ConstraintLayout empty;
    Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_list);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.item_actionbar_backup);
            actionBar.getCustomView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, 7);
                }
            });
        }

        mActivity = this;
        init();
        RefreshView();

        lstShowBackup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String filename = (String) parent.getItemAtPosition(position);
                BackupFileName = filename.substring(0, filename.length() - 3) + ".db";
                showRestoreDialog();
            }
        });

        //_______________________________________________________________

    }//End Of OnCreate

    @Override
    protected void onResume() {

        if (CopyDatabase()) {
            RefreshView();
            showRestoreDialog();
        }

        super.onResume();
    }

    private void showRestoreDialog() {
        FontDialog fontDialog = new FontDialog();
        final AlertDialog dialog = fontDialog.CustomeDialog(mContext, getString(R.string.str_message_restore));
        fontDialog.getPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importDB(BackupFileName);
                dialog.dismiss();
            }
        });
        fontDialog.getNegative().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean CopyDatabase() {

        boolean boolCopyDatabase = false;

        Intent intent = getIntent();
        String action = intent.getAction();

        if (action == null) return false;
        if (action.compareTo(Intent.ACTION_VIEW) == 0) {
            String scheme = intent.getScheme();
            ContentResolver resolver = getContentResolver();

            if (scheme != null) {
                if (scheme.compareTo(ContentResolver.SCHEME_CONTENT) == 0) {
                    try {
                        Uri uri = intent.getData();
                        ContentResolver cr = getContentResolver();
                        InputStream in = null;
                        if (uri != null) {
                            in = cr.openInputStream(uri);
                        }
                        String name = getName(resolver, uri);
                        BackupFileName = name;
                        String filePath = DATABASE_DIRECTORY + "/" + name;

                        if (in == null || name == null) return false;

                        if (!ExistFile(filePath)) {
                            //create file
                            new File(filePath.substring(0, filePath.lastIndexOf("/"))).mkdirs();
                            File file = new File(filePath);
                            ////////////////////////////////////////
                            //write in file
                            OutputStream output = new FileOutputStream(file);
                            try {
                                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                                int read;
                                while ((read = in.read(buffer)) != -1) {
                                    output.write(buffer, 0, read);
                                }
                                output.flush();
                            } finally {
                                output.close();
                            }
                            ///////////////////////
                        }//end of if exist file
                        boolCopyDatabase = true;
                    } catch (Exception ex) {
                        FirebaseCrashlytics.getInstance().recordException(ex);
                        Log.e("Tag SCHEME_CONTENT", ex.getMessage().toString());
                    }
                } else if (scheme.compareTo(ContentResolver.SCHEME_FILE) == 0) {

                    try {
                        Uri uri = intent.getData();
                        String strPathSrc = null;
                        if (uri != null) {
                            strPathSrc = uri.getPath();
                        }
                        File fileSource = null;
                        if (strPathSrc != null) {
                            fileSource = new File(strPathSrc);
                        }

                        String path[] = new String[0];
                        if (strPathSrc != null) {
                            path = strPathSrc.split("/");
                        }
                        String strNameDestination = path[path.length - 1];
                        BackupFileName = strNameDestination;
                        if (!ExistFile(DATABASE_DIRECTORY + "/" + strNameDestination)) {
                            File fileDestination = new File(DATABASE_DIRECTORY + "/" + strNameDestination);
                            copyFile(fileSource, fileDestination);
                        }
                        boolCopyDatabase = true;

                    } catch (Exception ex) {
                        FirebaseCrashlytics.getInstance().recordException(ex);
                        Log.e("Tag SCHEME_FILE", ex.getMessage().toString());
                    }
                } else if (scheme.compareTo("http") == 0) {
                    // TODO Import from HTTP!
                } else if (scheme.compareTo("ftp") == 0) {
                    // TODO Import from FTP!
                }
            }
        }
        return boolCopyDatabase;
    }

    public static boolean ExistFile(String Pathfile) {
        File file = new File(Pathfile);
        return file.exists();
    }

    private String getName(ContentResolver resolver, Uri uri) {
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int dataIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            if (dataIndex >= 0) {
                return cursor.getString(dataIndex);
            } else {
                return null;
            }
        }
        return "";
    }

    public void init() {
        lstShowBackup = (ListView) findViewById(R.id.lstShowBackup);
        empty = (ConstraintLayout) findViewById(R.id.empty);
        if (empty != null) {
            empty.setVisibility(View.VISIBLE);
            lstShowBackup.setEmptyView(empty);
        }
    }

    public void RefreshView() {

        String state = Environment.getExternalStorageState();
        final File BACKUPS_DIRECTORY = new File(Environment.getExternalStorageDirectory(), ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_BACKUPS);
        if (!BACKUPS_DIRECTORY.exists()) {
            BACKUPS_DIRECTORY.mkdirs();
        }
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //files = Environment.getExternalStorageDirectory().listFiles();
            files = BACKUPS_DIRECTORY.listFiles();
        }
        lstFilesArray.clear();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String strFileDir = files[i].toString();
                String[] strFiles = strFileDir.split("/");
                if (strFiles[strFiles.length - 1].endsWith(".db"))
                    lstFilesArray.add(strFiles[strFiles.length - 1]);
            }
        }
        //Set count
        int count = lstFilesArray.size();
        Collections.reverse(lstFilesArray);
        setAdapter();


    }

    public void Backup(View view) {

        if (ServiceTools.Backup(mContext))
            Toast.makeText(mActivity, R.string.str_backup_was_created, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mActivity, R.string.error_in_create_db_file, Toast.LENGTH_SHORT).show();

        /*String format = "HH_mm_ss";
        SimpleDateFormat simpleDate = new SimpleDateFormat(format, Locale.US);
        String strTime = simpleDate.format(new Date().getTime());
        String date = getBackUpDate(new Date().getTime());
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String backupDBPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_BACKUPS ;
        File backupDB = new File(sd, backupDBPath);
        if(!backupDB.exists())
            backupDB.mkdirs();
        String currentDBPath = "/data/"+ "com.mahak.order" +"/databases/"+DbSchema.DATABASE_NAME;
        String backDBPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_BACKUPS +  "/" + "MahakOrder" + getVersion() + "_" + getPrefUsername() + "_" + date +".db" ;
        File currentDB = new File(data, currentDBPath);
        File backup = new File(sd, backDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backup).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch(IOException e) {
            e.printStackTrace();
            Toast.makeText(mActivity, R.string.error_in_create_db_file, Toast.LENGTH_SHORT).show();
        }*/

        RefreshView();
    }

    private String getVersion() {
        PackageInfo pInfo;
        String version = "Unknown";
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return version;
    }

    private void setAdapter() {

        adapter = new ShowBackupsArrayAdapter(BackupListActivity.this, android.R.layout.simple_list_item_1, lstFilesArray);
        lstShowBackup.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private class ShowBackupsArrayAdapter extends ArrayAdapter<String> {
        private final Activity context;
        List<String> files = new ArrayList<String>();

        public ShowBackupsArrayAdapter(Activity context, int textViewResourceId, List<String> files) {
            super(context, textViewResourceId, files);
            this.context = context;
            this.files = files;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.item_lst_backup, null);
            final TextView TitleLabel = (TextView) rowView.findViewById(R.id.tvName);
            final LinearLayout btnmenu = (LinearLayout) rowView.findViewById(R.id.btnmenu);
            TitleLabel.setText(files.get(position).substring(0, files.get(position).length() - 3));
            TitleLabel.setTextSize(14.0f);
            btnmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, btnmenu);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.pmenu_backup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mnuDelete:
                                    BackupFileName = TitleLabel.getText().toString() + ".db";
                                    Position = position;
                                    FontDialog fontDialog = new FontDialog();
                                    final AlertDialog dialog = fontDialog.CustomeDialog(mContext, getString(R.string.str_message_delete));
                                    fontDialog.getPositive().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            deleteBackupDb(BackupFileName);
                                            lstFilesArray.remove(Position);
                                            adapter.notifyDataSetChanged();
                                            dialog.dismiss();
                                        }
                                    });
                                    fontDialog.getNegative().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                    break;
                                case R.id.mnuRestore:
                                    BackupFileName = TitleLabel.getText().toString() + ".db";
                                    showRestoreDialog();
                                    break;
                                case R.id.mnuSend:
                                    BackupFileName = TitleLabel.getText().toString() + ".db";
                                    Position = position;
                                    sendBackup(BackupFileName);
                                    break;

                            }
                            return false;
                        }
                    });
                    popup.show();
                    Menu menu = popup.getMenu();
                    for (int i = 0; i < menu.size(); i++) {
                        MenuItem mi = menu.getItem(i);
                        FontPopUp.applyFontToMenuItem(mi, mContext);
                    }
                }
            });
            return rowView;
        }


    }

    private void sendBackup(String BackupFileName) {

        File file = new File(DATABASE_DIRECTORY, BackupFileName);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

        Uri uri = FileProvider.getUriForFile(mContext,
                BuildConfig.APPLICATION_ID + ".provider",
                file);

        share.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(share, ""));
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Toast.makeText(BackupListActivity.this, getString(R.string.MSG_NotSuccess), Toast.LENGTH_LONG).show();
        }
    }

    protected void deleteBackupDb(String BackupFileName) {

        File DELETE_FILE = new File(DATABASE_DIRECTORY, BackupFileName);
        if (!DELETE_FILE.exists()) {
            return;
        }
        try {
            DELETE_FILE.delete();
            Toast.makeText(BackupListActivity.this, R.string.successful_delete, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            // TODO: handle exception
        }
    }


    public void importDB(String inFileName) {

        final String outFileName = mContext.getDatabasePath(DATABASE_NAME).toString();

        try {
            File dbFile = new File(DATABASE_DIRECTORY, inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, "Import Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, "Unable to import database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    protected void restoreBackupDb(String BackupFileName) {

        File IMPORT_FILE = new File(DATABASE_DIRECTORY, BackupFileName);
        File exportFile = DATA_DIRECTORY_DATABASE;

        //if( ! checkDbIsValid(importFile) ) return false;

        if (!IMPORT_FILE.exists()) {
            return;
        }

        try {
            exportFile.createNewFile();
            copyFile(IMPORT_FILE, exportFile);
            Toast.makeText(BackupListActivity.this, R.string.file_recovered_login_again, Toast.LENGTH_SHORT).show();
            RefreshPreferenceUser();
            Intent intent = new Intent(BackupListActivity.this, LoginActivityRestApi.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (IOException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            outChannel.close();
        }
    }

    private String getBackUpDate(long date) {
        return getFileName(date);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /*Drawable browse = ContextCompat.getDrawable(mContext, R.drawable.ic_find_in_page_black_24dp);
        menu.add(0, 0, 0, R.string.browse)
                .setTitle(R.string.browse)
                .setIcon(browse)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);*/

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case 0:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            ContentResolver resolver = getContentResolver();
            try {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                InputStream in = null;
                if (uri != null) {
                    in = cr.openInputStream(uri);
                }
                String name = getName(resolver, uri);
                BackupFileName = name;
                String filePath = DATABASE_DIRECTORY + "/" + name;
                if (in != null || name != null) {
                    if (!ExistFile(filePath)) {
                        //create file
                        new File(filePath.substring(0, filePath.lastIndexOf("/"))).mkdirs();
                        File file = new File(filePath);
                        ////////////////////////////////////////
                        //write in file
                        OutputStream output = new FileOutputStream(file);
                        try {
                            byte[] buffer = new byte[4 * 1024]; // or other buffer size
                            int read;
                            if (in != null) {
                                while ((read = in.read(buffer)) != -1) {
                                    output.write(buffer, 0, read);
                                }
                            }
                            output.flush();
                        } finally {
                            output.close();
                            RefreshView();
                            showRestoreDialog();
                        }
                        ///////////////////////
                    }//end of if exist file
                }
            } catch (Exception ex) {
                FirebaseCrashlytics.getInstance().recordException(ex);
                Log.e("Tag SCHEME_CONTENT", ex.getMessage().toString());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
