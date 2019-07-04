package com.mg.spstocks.fragments;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.mg.spstocks.MainActivity;
import com.mg.spstocks.R;
import com.mg.spstocks.adapters.Coinadapter;
import com.mg.spstocks.models.Coin;

import java.util.List;

public class CoinFragment extends Fragment {
    private static final String TAG = "CoinFragment";
    List<Coin> coinList;
    private RecyclerView myRecyclerView;
    private Coinadapter mAdapter;
    private InterstitialAd interstitial;

    public CoinFragment() {
    }

    @Nullable
    @Override

    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.coins_fragment, container,false);
        Log.d("TSTS","entered");
        coinList= ((MainActivity)getActivity()).getCoinList();
        myRecyclerView =(RecyclerView) view.findViewById(R.id.coinrec);
        mAdapter =new Coinadapter(coinList,getActivity());
        myRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager =new LinearLayoutManager( getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(layoutManager);
        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(getActivity());
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });

        return view;

    }
    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
