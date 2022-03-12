package com.mahak.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahak.order.common.Notification;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.RadaraDb;
import com.mahak.order.widget.FontAlertDialog;

import java.util.ArrayList;

public class NotificationActivity extends BaseActivity {

    public static final int REQUEST_CODE_DIALOG = 170;
    public static final int REQUEST_CODE_WEBVIEW = 171;
    private ArrayList<Notification> notifications;
    private RadaraDb db;
    private RecyclerView listTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        db = new RadaraDb(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.str_nav_close);

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        getSupportActionBar().setCustomView(view);
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText(R.string.str_notification_title);

        long userId = BaseActivity.getPrefUserMasterId(mContext);

        listTransactions = (RecyclerView) findViewById(R.id.recyclerView);
        db.open();
        notifications = db.getAllNotifications(String.valueOf(userId));
        db.close();
        listTransactions.setLayoutManager(new LinearLayoutManager(mContext));
        listTransactions.setAdapter(getAdapterListNotifications());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, R.string.str_delete)
                .setIcon(R.drawable.ic_ab_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            showDialogDelete(null);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DIALOG || requestCode == REQUEST_CODE_WEBVIEW) {
            if (resultCode == RESULT_OK)
                refreshList();
        }
    }

    private void showDialogDelete(final Notification notification) {
        if (notification == null && notifications.isEmpty()) {
            Toast.makeText(NotificationActivity.this, getString(R.string.str_error_no_message_for_delete), Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(FontAlertDialog.getFontTitle(getString(R.string.str_title_delete)));
        int mode = 0;
        if (notification != null)
            mode = 0;
        else if (notifications.size() > 1) {
            mode = 1;
        }
        if (mode == 1)
            builder.setMessage(getString(R.string.DeleteMessages));
        else
            builder.setMessage(getString(R.string.DeleteMessage));
        builder.setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.open();
                if (notification == null) {
                    db.DeleteAllNotification();
                } else
                    db.DeleteNotification(notification.get_id());
                db.close();
                refreshList();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void refreshList() {
        long userId = BaseActivity.getPrefUserMasterId(mContext);
        db.open();
        notifications = db.getAllNotifications(String.valueOf(userId));
        db.close();
        listTransactions.getAdapter().notifyDataSetChanged();
    }

    @NonNull
    private RecyclerView.Adapter getAdapterListNotifications() {
        return new RecyclerView.Adapter<MyHolder>() {
            @Override
            public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.lst_item_notification, parent, false);
                return new MyHolder(view);
            }

            @Override
            public void onBindViewHolder(MyHolder holder, int position) {
                final Notification notification = notifications.get(position);

                holder.tvTitle.setText(notification.getTitle());
                String message = notification.getFullMessage();
                if (message == null || message.equals(""))
                    message = notification.getMessage();
                holder.tvMessage.setText(message);
                holder.tvDate.setText(ServiceTools.ConvertTimestampToPersianFormat(mContext, notification.getDate()));

                if (notification.isRead())
                    holder.ivDontRead.setVisibility(View.INVISIBLE);
                else
                    holder.ivDontRead.setVisibility(View.VISIBLE);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (notification.getType().equals("web")) {
                            Intent intent = new Intent(mContext, WebViewActivity.class);
                            intent.putExtra("url", notification.getData());
                            intent.putExtra("Id", notification.get_id());
                            startActivityForResult(intent, REQUEST_CODE_WEBVIEW);
                        } else if (notification.getType().equals("dialog")) {
                            Intent intent = new Intent(mContext, NotificationDialogActivity.class);
                            intent.putExtra("url", notification.getData());
                            intent.putExtra("title", notification.getTitle());
                            intent.putExtra("message", notification.getFullMessage());
                            intent.putExtra("Id", notification.get_id());
                            startActivityForResult(intent, REQUEST_CODE_DIALOG);
                        }
                        db.open();
                        notification.setRead(true);
                        db.UpdateNotification(notification);
                        db.close();
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showDialogDelete(notification);
                        return true;
                    }
                });
            }

            @Override
            public int getItemCount() {
                return notifications == null ? 0 : notifications.size();
            }
        };
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvMessage, tvDate;
        ImageView ivDontRead;

        public MyHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            ivDontRead = (ImageView) itemView.findViewById(R.id.imgNotification);
        }
    }
}
