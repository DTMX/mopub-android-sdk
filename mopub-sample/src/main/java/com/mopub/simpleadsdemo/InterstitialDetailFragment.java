// Copyright 2018 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// http://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.simpleadsdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import static com.mopub.mobileads.MoPubInterstitial.InterstitialAdListener;
import static com.mopub.simpleadsdemo.Utils.hideSoftKeyboard;
import static com.mopub.simpleadsdemo.Utils.logToast;

public class InterstitialDetailFragment extends Fragment implements InterstitialAdListener {
    private MoPubInterstitial mMoPubInterstitial;
    private Button mShowButton;
    private DetailFragmentViewHolder views;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final AdUnitDataSource adUnitDataSource = new AdUnitDataSource(this.getContext());
        final MoPubSampleAdUnit adConfiguration = adUnitDataSource.getDefaultAdUnits().get(0);
        final View view = inflater.inflate(R.layout.interstitial_detail_fragment, container, false);
        views = DetailFragmentViewHolder.fromView(view);

        final String adUnitId = adConfiguration.getAdUnitId();
        views.mDescriptionView.setText(adConfiguration.getDescription());
        views.mAdUnitIdView.setText(adUnitId);
        if (mMoPubInterstitial == null) {
            mMoPubInterstitial = new MoPubInterstitial(getActivity(), adUnitId);
            mMoPubInterstitial.setInterstitialAdListener(InterstitialDetailFragment.this);
        }
        mMoPubInterstitial.load();

        mShowButton = views.mShowButton;
        mShowButton.setEnabled(false);
        views.mLoadButton.setEnabled(false);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mMoPubInterstitial != null) {
            mMoPubInterstitial.destroy();
            mMoPubInterstitial = null;
        }
    }

    // InterstitialAdListener implementation
    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        mMoPubInterstitial.show();
        logToast(getActivity(), "Interstitial loaded.");
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
        final String errorMessage = (errorCode != null) ? errorCode.toString() : "";
        logToast(getActivity(), "Interstitial failed to load: " + errorMessage);
        mMoPubInterstitial.load();
    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {
        logToast(getActivity(), "Interstitial shown.");
    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {
        logToast(getActivity(), "Interstitial clicked.");
    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {
        logToast(getActivity(), "Interstitial dismissed.");
    }
}
