package com.mahak.order.InvoiceFragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.mahak.order.BaseActivity;
import com.mahak.order.PeopleListActivity;
import com.mahak.order.ManageCustomerActivity;
import com.mahak.order.MapViewActivity;
import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.R;
import com.mahak.order.VisitorListActivity;
import com.mahak.order.common.GPSTracker;
import com.mahak.order.common.MyCalendar;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.Reasons;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.Visitor;
import com.mahak.order.interfaces.FragmentLifecycle;
import com.mahak.order.libs.DatePicker;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.mahak.order.BaseActivity.COORDINATE;
import static com.mahak.order.BaseActivity.CUSTOMERID_KEY;
import static com.mahak.order.BaseActivity.CUSTOMER_CLIENT_ID_KEY;
import static com.mahak.order.BaseActivity.CUSTOMER_GROUP_KEY;
import static com.mahak.order.BaseActivity.MODE_EDIT;
import static com.mahak.order.BaseActivity.MODE_NEW;
import static com.mahak.order.BaseActivity.MODE_PAGE;
import static com.mahak.order.BaseActivity.PAGE;
import static com.mahak.order.BaseActivity.PAGE_ADD_INVOICE;
import static com.mahak.order.BaseActivity.PAGE_ADD_ORDER;
import static com.mahak.order.BaseActivity.PAGE_ADD_RETURN;
import static com.mahak.order.BaseActivity.PAGE_ADD_SEND_TRANSFER;
import static com.mahak.order.BaseActivity.TYPE_KEY;
import static com.mahak.order.BaseActivity._Key_StoreCode;
import static com.mahak.order.BaseActivity._Key_VisitorID;
import static com.mahak.order.InvoiceDetailActivity.Address;
import static com.mahak.order.InvoiceDetailActivity.CustomerClientId;
import static com.mahak.order.InvoiceDetailActivity.CustomerId;
import static com.mahak.order.InvoiceDetailActivity.CustomerName;
import static com.mahak.order.InvoiceDetailActivity.Description;
import static com.mahak.order.InvoiceDetailActivity.GroupId;
import static com.mahak.order.InvoiceDetailActivity.Immediate;
import static com.mahak.order.InvoiceDetailActivity.InvoiceCode;
import static com.mahak.order.InvoiceDetailActivity.LastName;
import static com.mahak.order.InvoiceDetailActivity.MarketName;
import static com.mahak.order.InvoiceDetailActivity.Mode;
import static com.mahak.order.InvoiceDetailActivity.Name;
import static com.mahak.order.InvoiceDetailActivity.OrderType;
import static com.mahak.order.InvoiceDetailActivity.ReasonCode;
import static com.mahak.order.InvoiceDetailActivity.SettlementType;
import static com.mahak.order.InvoiceDetailActivity.StrLatitude;
import static com.mahak.order.InvoiceDetailActivity.StrLongitude;
import static com.mahak.order.InvoiceDetailActivity.Tell;
import static com.mahak.order.InvoiceDetailActivity.btnSave;
import static com.mahak.order.InvoiceDetailActivity.customer;
import static com.mahak.order.InvoiceDetailActivity.lngDeliveryDate;
import static com.mahak.order.InvoiceDetailActivity.lngOrderDate;
import static com.mahak.order.InvoiceDetailActivity.strDeliveryDate;
import static com.mahak.order.InvoiceDetailActivity.strOrderDate;
import static com.mahak.order.InvoiceDetailActivity.tvPageTitle;
import static com.mahak.order.InvoiceDetailActivity.visitor;
import static com.mahak.order.InvoiceDetailActivity.visitorId;

//import static com.mahak.order.InvoiceDetailActivity.returnOfSale;


public class InvoiceCustomerDetail extends Fragment implements FragmentLifecycle {

    private static final int REQUEST_CUSTOMER_LIST = 1;
    private static final int REQUEST_USER_LIST = 3;

    private GPSTracker gpsTracker;
    private ArrayList<LatLng> positions = new ArrayList<>();

    private ArrayList<Reasons> arrayReasons = new ArrayList<>();
    private final ArrayList<String> arrayTitles = new ArrayList<>();

    //customer View Detail
    private static Button orderDatePicker;
    private TextView tvOrderDate;
    private TextView tvInvoiceCode;
    private TextView tvCustomerName;
    private static TextView tvDeliveryDate;
    private TextView CustomerNameText;
    private ImageButton moreInfo;
    private Button btnSelectCustomer;
    private EditText txtName, txtMarketName, txtTell, txtAddress, txtLastName;
    private EditText txtDescription, txtLongitude, txtLatitude;

    private DbAdapter db;

    private LinearLayout llOrderDate,
            newllDeliveryDate, llGuestDetails,
            llTypeOrder,
            llTypeSettle,
            llDiscount, llReturnCause, llDeliveryDate;
    private CheckBox chkImmediate;
    private Spinner spnSettlementType, spnOrderType, spnReturnCause;
    private Button btnDeliveryDate, btnGetLocation;
    private String[] arraySettlement;

    public InvoiceCustomerDetail() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static InvoiceCustomerDetail newInstance(String param1, String param2) {
        InvoiceCustomerDetail fragment = new InvoiceCustomerDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_detail, container, false);
        initView(v);
        db.open();
        FillSpinnerWithReasonCode();
        FillSpinnerSettelment();
        FillSpinnerOrderType();
        //////////

        if (Mode == MODE_NEW) {
            llTypeOrder.setVisibility(View.VISIBLE);
        } else if (Mode == MODE_EDIT && OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            llTypeOrder.setVisibility(View.GONE);
            spnReturnCause.setSelection(getCategoryPos(ReasonCode));
        } else {
            llTypeOrder.setVisibility(View.GONE);
        }

        if (InvoiceCode.equals(""))
            InvoiceCode = ServiceTools.getGenerationCode();
        if (strDeliveryDate.equals("")) {
            lngDeliveryDate = new Date().getTime();
            strDeliveryDate = ServiceTools.getDateForLong(getActivity(), lngDeliveryDate);
            tvDeliveryDate.setText(strDeliveryDate);
        }
        if (strOrderDate.equals("")) {
            tvOrderDate.setText(strOrderDate);
        }
        if (CustomerId == ProjectInfo.CUSTOMERID_GUEST) {
            moreInfo.setVisibility(View.GONE);
        }

        if (CustomerId != ProjectInfo.CUSTOMERID_GUEST) {
            customer = db.getCustomerWithPersonId(CustomerId);
            CustomerName = customer.getName();
            tvCustomerName.setText(CustomerName);
        } else {
            customer = db.getCustomerWithPersonClientId(CustomerClientId);
            CustomerName = customer.getName();
            tvCustomerName.setText(CustomerName);
        }

        if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            CustomerName = visitor.getName();
            tvCustomerName.setText(visitor.getName());
        }

        ////////
        FillView();

        if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE || OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {

            llTypeSettle.setVisibility(View.GONE);
            llDiscount.setVisibility(View.GONE);

            if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                btnSelectCustomer.setText(R.string.select_visitor);
                if (tvCustomerName.getText().toString().equals(getString(R.string.guest))) {
                    tvCustomerName.setText("");
                }
                CustomerNameText.setText(R.string.visitor_name);
            } else {
                btnSelectCustomer.setText(R.string.select_customer);
                CustomerNameText.setText(R.string.customer_name);
            }

        }

        lngDeliveryDate = new Date().getTime();
        strDeliveryDate = ServiceTools.getDateForLong(getActivity(), lngDeliveryDate);
        orderDatePicker.setText(strDeliveryDate);

        if (Mode == MODE_EDIT && OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            strDeliveryDate = ServiceTools.getDateForLong(getActivity(), lngDeliveryDate);
            orderDatePicker.setText(strDeliveryDate);
        }

        spnSettlementType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SettlementType = position;
                //validPromotionList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnSettlementType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SettlementType = position;
                //validPromotionList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnReturnCause.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ReasonCode = arrayReasons.get(position).getReturnReasonCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnOrderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

                if (position == 0) {
                    if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {

                        tvPageTitle.setText(getString(R.string.str_save_Transference));
                        btnSave.setText(getString(R.string.str_save_Transference));
                        btnDeliveryDate.setVisibility(View.VISIBLE);

                    } else if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
                        tvPageTitle.setText(getString(R.string.str_add_return));
                        btnSave.setText(getString(R.string.str_add_return));
                        btnDeliveryDate.setVisibility(View.VISIBLE);
                    } else {
                        OrderType = ProjectInfo.TYPE_ORDER;
                        btnDeliveryDate.setVisibility(View.VISIBLE);
                        lngDeliveryDate = new Date().getTime();
                        strDeliveryDate = ServiceTools.getDateForLong(getActivity(), lngDeliveryDate);
                        tvDeliveryDate.setText(strDeliveryDate);
                        tvPageTitle.setText(getString(R.string.str_save_order));
                        btnSave.setText(getString(R.string.str_save_order));
                    }

                } else if (position == 1) {
                    OrderType = ProjectInfo.TYPE_INVOCIE;
                    btnDeliveryDate.setVisibility(View.INVISIBLE);
                    tvPageTitle.setText(getString(R.string.str_save_invoice));
                    btnSave.setText(getString(R.string.str_save_invoice));
                    lngDeliveryDate = new Date().getTime();
                    strDeliveryDate = ServiceTools.getDateForLong(getActivity(), lngDeliveryDate);
                    tvDeliveryDate.setText(strDeliveryDate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        lngDeliveryDate = new Date().getTime();
        strDeliveryDate = ServiceTools.getDateForLong(getActivity(), lngDeliveryDate);
        orderDatePicker.setText(strDeliveryDate);

        if (Mode == MODE_EDIT && OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            strDeliveryDate = ServiceTools.getDateForLong(getActivity(), lngDeliveryDate);
            orderDatePicker.setText(strDeliveryDate);
        }

        orderDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MyCalendar myCalendar = new MyCalendar(lngOrderDate, getActivity(), getActivity().getFragmentManager(), new MyCalendar.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker date) {
                        lngOrderDate = date.getDate().getTime();
                        orderDatePicker.setText(ServiceTools.getDateForLong(getActivity(), lngOrderDate));
                    }

                    @Override
                    public void onGregorianSet(int year, int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        lngOrderDate = calendar.getTimeInMillis();
                        orderDatePicker.setText(ServiceTools.getDateForLong(getActivity(), lngOrderDate));

                    }

                });
                myCalendar.showDialog();
            }
        });

        btnSelectCustomer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
                    Intent intent = new Intent(getActivity(), VisitorListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_SEND_TRANSFERENCE);
                    intent.putExtra(PAGE, PAGE_ADD_SEND_TRANSFER);
                    startActivityForResult(intent, REQUEST_USER_LIST);
                } else if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
                    Intent intent = new Intent(getActivity(), PeopleListActivity.class);
                    intent.putExtra(TYPE_KEY, ProjectInfo.TYPE_RETURN_OF_SALE);
                    intent.putExtra(PAGE, PAGE_ADD_RETURN);
                    startActivityForResult(intent, REQUEST_CUSTOMER_LIST);
                } else if (OrderType == ProjectInfo.TYPE_INVOCIE) {
                    Intent intent = new Intent(getActivity(), PeopleListActivity.class);
                    intent.putExtra(PAGE, PAGE_ADD_INVOICE);
                    startActivityForResult(intent, REQUEST_CUSTOMER_LIST);
                } else if (OrderType == ProjectInfo.TYPE_ORDER) {
                    Intent intent = new Intent(getActivity(), PeopleListActivity.class);
                    intent.putExtra(PAGE, PAGE_ADD_ORDER);
                    startActivityForResult(intent, REQUEST_CUSTOMER_LIST);
                }
            }
        });


        btnGetLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                double Latitude;
                double Longitude;
                gpsTracker = new GPSTracker(getActivity());
                if (gpsTracker.canGetLocation()) {
                    Latitude = gpsTracker.getLatitude();
                    Longitude = gpsTracker.getLongitude();

                    if (Latitude == 0 && Longitude == 0)
                        Toast.makeText(getActivity(), getResources().getString(R.string.str_message_dont_connect_gps), Toast.LENGTH_SHORT).show();
                    else {
                        positions = new ArrayList<>();
                        LatLng pos = new LatLng(Latitude, Longitude);
                        positions = new ArrayList<>();
                        positions.add(pos);

                        StrLatitude = String.valueOf(Latitude);
                        StrLongitude = String.valueOf(Longitude);
                        txtLatitude.setText(String.valueOf(Latitude));
                        txtLongitude.setText(String.valueOf(Longitude));

                        Intent intent = new Intent(getActivity(), MapViewActivity.class);
                        intent.putParcelableArrayListExtra(COORDINATE, positions);
                        startActivity(intent);
                    }
                } else
                    Toast.makeText(getActivity(), getResources().getString(R.string.str_message_dont_connect_gps), Toast.LENGTH_SHORT).show();
            }
        });

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManageCustomerActivity.class);
                intent.putExtra(MODE_PAGE, MODE_EDIT);
                intent.putExtra(CUSTOMERID_KEY, customer.getPersonId());
                intent.putExtra(CUSTOMER_CLIENT_ID_KEY, customer.getPersonClientId());
                intent.putExtra("GroupId", GroupId);
                startActivity(intent);
            }
        });

        btnDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCalendar myCalendar = new MyCalendar(lngDeliveryDate, getActivity(), getActivity().getFragmentManager(), new MyCalendar.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker date) {
                        lngDeliveryDate = date.getDate().getTime();
                        tvDeliveryDate.setText(ServiceTools.getDateForLong(getActivity(), lngDeliveryDate));
                    }

                    @Override
                    public void onGregorianSet(int year, int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        lngDeliveryDate = calendar.getTimeInMillis();
                        tvDeliveryDate.setText(ServiceTools.getDateForLong(getActivity(), lngDeliveryDate));

                    }
                });
                myCalendar.showDialog();
            }
        });


        txtDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Description = s.toString();
            }
        });

        txtName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Name = s.toString();
            }
        });

        txtLastName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                LastName = s.toString();
            }
        });
        txtMarketName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MarketName = s.toString();
            }
        });

        txtTell.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Tell = s.toString();
            }
        });
        txtAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Address = s.toString();
            }
        });

        txtLatitude.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                StrLatitude = s.toString();
            }
        });
        txtLongitude.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                StrLongitude = s.toString();
            }
        });

        chkImmediate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Immediate = ProjectInfo.IMMEDIATE;
                else
                    Immediate = ProjectInfo.DONT_IMMEDIATE;
            }
        });

        return v;
    }

    private void FillSpinnerSettelment() {
        arraySettlement = getResources().getStringArray(R.array.array_settlement_type);
        AdapterSpinner adapterSpinner = new AdapterSpinner(getActivity(), R.layout.item_spinner, arraySettlement);
        spnSettlementType.setAdapter(adapterSpinner);
    }

    private void FillSpinnerOrderType() {
        arraySettlement = getResources().getStringArray(R.array.array_type_order);
        AdapterSpinner adapterSpinner = new AdapterSpinner(getActivity(), R.layout.item_spinner, arraySettlement);
        spnOrderType.setAdapter(adapterSpinner);
    }

    public class AdapterSpinner extends ArrayAdapter<String> {

        String[] Objects;

        public AdapterSpinner(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            Objects = objects;

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View row = inflater.inflate(R.layout.item_spinner, parent, false);
            TextView tvName = (TextView) row.findViewById(R.id.tvName);
            tvName.setText(Objects[position]);

            return row;
        }
    }// End of AdapterSpnAssetProduct

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }


    public static class TransferDialogDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            final Calendar c = Calendar.getInstance();
            c.setTimeInMillis(lngDeliveryDate);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return dialog;


            /*Date dt = new Date();
            dt.setTime(lngDeliveryDate);
            CharSequence title;
            LayoutInflater inflater;
            View dialogLayout;
            AlertDialog.Builder builder;
            title = getString(R.string.str_datepicker);
            inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            dialogLayout = inflater.inflate(R.layout.calendar, null);

            *//*final DatePicker dp = (DatePicker) dialogLayout.findViewById(R.id.datePicker1);
            dp.setDate(dt);*//*

            builder = new AlertDialog.Builder(getActivity());
            builder.setView(dialogLayout)
                    .setTitle(title)
                    .setIcon(R.drawable.ic_datepicker_inverse)
                    .setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            *//*lngDeliveryDate = dp.getDate().getTime();
                            strDeliveryDate = dp.getPersianDate().getDayOfMonth() + " " + dp.getPersianDate().getMonthName() + " " + dp.getPersianDate().getYear();*//*
                            orderDatePicker.setText(strDeliveryDate);
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }
                    })

                    .setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialog.cancel();
                        }
                    });
            return builder.create();*/
        }

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {


        }
    }

    private void FillSpinnerWithReasonCode() {

        arrayReasons = new ArrayList<>();
        arrayReasons = db.getAllReasonByType(BaseActivity.ReturnReason);

        for (Reasons reasons : arrayReasons) {
            arrayTitles.add(reasons.getName());
        }

        ArrayAdapter<String> adp = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrayTitles);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnReturnCause.setAdapter(adp);
        if (arrayReasons.size() > 0)
            spnReturnCause.setSelection(0);
    }

    private int getCategoryPos(int masterID) {
        int i = 0;
        for (Reasons reasons : arrayReasons) {
            if (reasons.getReturnReasonCode() == masterID)
                return i;
            else i++;
        }
        return i;
    }

    private void initView(View v) {
        db = new DbAdapter(getActivity());
        tvCustomerName = (TextView) v.findViewById(R.id.tvCustomerName);
        tvDeliveryDate = (TextView) v.findViewById(R.id.tvDeliveryDate);
        tvOrderDate = (TextView) v.findViewById(R.id.tvOrderDate);
        tvInvoiceCode = (TextView) v.findViewById(R.id.tvInvoiceCode);
        CustomerNameText = (TextView) v.findViewById(R.id.CustomerNameText);
        moreInfo = (ImageButton) v.findViewById(R.id.moreInfo);
        txtDescription = (EditText) v.findViewById(R.id.txtDescription);
        txtMarketName = (EditText) v.findViewById(R.id.txtMarketName);
        txtName = (EditText) v.findViewById(R.id.txtFirstName);
        txtTell = (EditText) v.findViewById(R.id.txtTell);
        txtAddress = (EditText) v.findViewById(R.id.txtAddress);
        txtLastName = (EditText) v.findViewById(R.id.txtLastName);
        txtLatitude = (EditText) v.findViewById(R.id.txtLatitude);
        txtLongitude = (EditText) v.findViewById(R.id.txtLongitude);
        orderDatePicker = (Button) v.findViewById(R.id.TransferbtnDatePicker);
        btnSelectCustomer = (Button) v.findViewById(R.id.btnSelectCustomer);
        llOrderDate = (LinearLayout) v.findViewById(R.id.llOrderDate);
        newllDeliveryDate = (LinearLayout) v.findViewById(R.id.newllDeliveryDate);
        llReturnCause = (LinearLayout) v.findViewById(R.id.llReturnCause);
        llGuestDetails = (LinearLayout) v.findViewById(R.id.llGuestDetails);
        btnGetLocation = (Button) v.findViewById(R.id.btnGetGeographicallocation);
        llTypeOrder = (LinearLayout) v.findViewById(R.id.llTypeOrder);
        llDiscount = (LinearLayout) v.findViewById(R.id.llDiscount);
        llTypeSettle = (LinearLayout) v.findViewById(R.id.llTypeSettle);
        llDeliveryDate = (LinearLayout) v.findViewById(R.id.llDeliveryDate);
        spnSettlementType = (Spinner) v.findViewById(R.id.spnSettlementType);
        spnOrderType = (Spinner) v.findViewById(R.id.spnOrderType);
        spnReturnCause = (Spinner) v.findViewById(R.id.spnReturnCause);
        chkImmediate = (CheckBox) v.findViewById(R.id.chkImmediate);
        btnDeliveryDate = (Button) v.findViewById(R.id.btnDatePicker);
    }

    private void FillView() {
        tvCustomerName.setText(CustomerName);
        tvOrderDate.setText(strOrderDate);
        tvInvoiceCode.setText(InvoiceCode);
        tvDeliveryDate.setText(strDeliveryDate);
        txtDescription.setText(Description);
        txtName.setText(Name);
        txtLastName.setText(LastName);
        txtMarketName.setText(MarketName);
        txtTell.setText(Tell);
        txtAddress.setText(Address);
        txtLatitude.setText(StrLatitude);
        txtLongitude.setText(StrLongitude);
        spnSettlementType.setSelection(SettlementType);

        if (OrderType == ProjectInfo.TYPE_INVOCIE) {
            btnDeliveryDate.setVisibility(View.VISIBLE);
            spnOrderType.setSelection(1);
        } else if (OrderType == ProjectInfo.TYPE_ORDER) {
            btnDeliveryDate.setVisibility(View.INVISIBLE);
            spnOrderType.setSelection(0);
        } else if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            llOrderDate.setVisibility(View.GONE);
            newllDeliveryDate.setVisibility(View.VISIBLE);
            llDeliveryDate.setVisibility(View.GONE);
            spnOrderType.setSelection(0);
        } else if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            llOrderDate.setVisibility(View.GONE);
            newllDeliveryDate.setVisibility(View.VISIBLE);
            llDeliveryDate.setVisibility(View.GONE);
            llReturnCause.setVisibility(View.VISIBLE);
            spnOrderType.setSelection(0);
        }
        if (Immediate == ProjectInfo.IMMEDIATE)
            chkImmediate.setChecked(true);
        else if (Immediate == ProjectInfo.DONT_IMMEDIATE)
            chkImmediate.setChecked(false);
        /* if (CustomerId == ProjectInfo.CUSTOMERID_GUEST && OrderType != ProjectInfo.TYPE_SEND_TRANSFERENCE){
         *//*llGuestDetails.setVisibility(View.VISIBLE);
            txtName.requestFocus();*//*
        }

        else*/
        llGuestDetails.setVisibility(View.GONE);
        if (Mode == MODE_EDIT)
            llTypeOrder.setVisibility(View.GONE);
        else if (Mode == MODE_NEW)
            llTypeOrder.setVisibility(View.VISIBLE);

        if (OrderType == ProjectInfo.TYPE_SEND_TRANSFERENCE) {
            btnDeliveryDate.setVisibility(View.VISIBLE);
        } else if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {
            btnDeliveryDate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CUSTOMER_LIST:

                    CustomerId = data.getIntExtra(CUSTOMERID_KEY, 0);
                    CustomerClientId = data.getLongExtra(CUSTOMER_CLIENT_ID_KEY, 0);
                    GroupId = data.getLongExtra(CUSTOMER_GROUP_KEY, 0);

                    if (CustomerId != ProjectInfo.CUSTOMERID_GUEST) {
                        customer = db.getCustomerWithPersonId(CustomerId);
                    } else {
                        customer = db.getCustomerWithPersonClientId(CustomerClientId);
                    }

                    ProductPickerListActivity.CustomerId = CustomerId;
                    ProductPickerListActivity.GroupId = GroupId;

                    CustomerName = customer.getName();
                    tvCustomerName.setText(customer.getName());
                    moreInfo.setVisibility(View.VISIBLE);

                    break;

                case REQUEST_USER_LIST:

                    visitorId = data.getLongExtra(_Key_VisitorID, 0);
                    long storeCode = data.getLongExtra(_Key_StoreCode, 0);
                    Visitor visitor = db.getVisitor();
                    if (visitor.getStoreCode() == 0) {
                        Toast.makeText(getActivity(), R.string.store_is_not_dedicated, Toast.LENGTH_SHORT).show();
                    } else if (storeCode == visitor.getStoreCode()) {
                        Toast.makeText(getActivity(), R.string.store_is_equal, Toast.LENGTH_SHORT).show();
                    } else if (storeCode == 0) {
                        Toast.makeText(getActivity(), R.string.no_store, Toast.LENGTH_SHORT).show();
                    } else {
                        visitor = db.getVisitor(visitorId);
                        CustomerName = visitor.getName();
                        tvCustomerName.setText(visitor.getName());
                    }
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
