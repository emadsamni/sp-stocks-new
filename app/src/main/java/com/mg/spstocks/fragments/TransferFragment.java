package com.mg.spstocks.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mg.spstocks.MainActivity;
import com.mg.spstocks.R;
import com.mg.spstocks.models.Coin;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import android.graphics.Matrix;
import android.widget.ImageView;
import android.widget.Toast;

public class TransferFragment  extends Fragment {
    Spinner fromSpinner , toSpinner;
    ArrayAdapter<String> fromAdapter;
    ArrayAdapter<String> toAadapter;
    List<Coin> coinList;
    List <String> fromlist;
    List <String> tolist;
    TextView fromCoin , toCoin ,sellRes ,buyRes;
    EditText input;
    int from ,to ;
    ImageView imageView;
    Animation animation;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.transfer_fragment, null);
        fromSpinner = (Spinner) view.findViewById(R.id.fromspid);
        toSpinner = (Spinner) view.findViewById(R.id.tospid);
        imageView = (ImageView) view.findViewById(R.id.convert_button);
        input =(EditText)view.findViewById(R.id.button_input) ;
        sellRes = (TextView) view.findViewById(R.id.sellRes);
        buyRes = (TextView) view.findViewById(R.id.BuyRes);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValidation() ) {
                    animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
                    imageView.startAnimation(animation);
                     cal();
                }
                else
                {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.inputEmpty), Toast.LENGTH_SHORT).show();
                }
            }
        });
        coinList= ((MainActivity)getActivity()).getCoinList();
        fromlist = new ArrayList<>();
        tolist = new ArrayList<>();
        fromCoin = view.findViewById(R.id.fromCoin);
        toCoin = view.findViewById(R.id.toCoin);
        initFacebookLogin(view);

        fromlist.add(getActivity().getResources().getString(R.string.from));
        fromlist.add("الليرة السورية");
        tolist.add( getActivity().getResources().getString(R.string.to));
        tolist.add("الليرة السورية");

        for (int i=0;i<coinList.size();i++)
        {
            fromlist.add(coinList.get(i).getCoin_name());
        }
        for (int i=0;i<coinList.size();i++)
        {
            tolist.add(coinList.get(i).getCoin_name());
        }
        from =0;
        to = 0;

        fromAdapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner ,fromlist) {
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(getResources().getColor(R.color.white));

                return tv;
            }
            @Override
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View view;
                if (position == 0) {
                    TextView selectView = new TextView(getContext());
                    selectView.setHeight(0);
                    selectView.setVisibility(View.GONE);
                    view = selectView;
                } else
                    view = super.getDropDownView(position, null, parent);

                return view;
            }
        };
        toAadapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner ,tolist) {
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(getResources().getColor(R.color.white));

                return tv;
            }

            @Override
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View view;
                if (position == 0) {
                    TextView selectView = new TextView(getContext());
                    selectView.setHeight(0);
                    selectView.setVisibility(View.GONE);
                    view = selectView;
                } else
                    view = super.getDropDownView(position, null, parent);

                return view;
            }
        };
        fromSpinner.setAdapter(fromAdapter);
        toSpinner.setAdapter(toAadapter);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (fromSpinner.getSelectedItemPosition() != 0) {
                    from = fromSpinner.getSelectedItemPosition();
                    fromCoin.setText(fromSpinner.getSelectedItem().toString());
                    //fromSpinner.setSelection(0);
                    fromSpinner.setAdapter(fromAdapter);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (toSpinner.getSelectedItemPosition() != 0) {
                    to = toSpinner.getSelectedItemPosition();
                    toCoin.setText(toSpinner.getSelectedItem().toString());
                    toSpinner.setSelection(0);
                }


                //  cal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private boolean isInputValidation() {
        boolean check = true;
        String message="";
        if (input.getText().toString().isEmpty()){
            check =false;
        }
        if (fromCoin.getText().toString().isEmpty()){

            check =false;
        }
        if (toCoin.getText().toString().isEmpty()){

            check =false;
        }

        return check;

    }
    public  void cal()
    {
          double x=0;
          String fromText= input.getText().toString();
          x= Double.parseDouble(fromText);

           double res =0;
           double res2 =0;
           int tempFrom;
           int tempTO;
           tempFrom = from;
           tempTO =to;
           from--;
           to --;
               if (from == 0) {
                   if (to == 0) {
                       res = x;
                   } else {
                       res = x / coinList.get(to - 1).getLog().get(0).getBuy();
                       res2 = x / coinList.get(to - 1).getLog().get(0).getSell();
                   }
               } else {
                   if (to == 0) {
                       res = x * coinList.get(from - 1).getLog().get(0).getBuy();
                       res2 = x * coinList.get(from - 1).getLog().get(0).getSell();
                   } else {
                       res = x * coinList.get(from - 1).getLog().get(0).getBuy();
                       res = res / coinList.get(to - 1).getLog().get(0).getBuy();

                       res2 = x * coinList.get(from - 1).getLog().get(0).getSell();
                       res2 = res2 / coinList.get(to - 1).getLog().get(0).getSell();
                   }
               }

           res = round(res,6);
         NumberFormat nf = NumberFormat.getNumberInstance(Locale.CANADA);
         nf.setMaximumFractionDigits(6);
         String rounded = nf.format(res);
         res2 = round(res2,6);
          nf = NumberFormat.getNumberInstance(Locale.CANADA);
         nf.setMaximumFractionDigits(6);
         String rounded2 = nf.format(res2);
         sellRes.setText(rounded2);
         buyRes.setText(rounded);
         from =tempFrom;
         to =tempTO;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private void initFacebookLogin(View container) {
       // LoginManager.getInstance().logOut();

        loginButton = container.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.setFragment(this);

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
