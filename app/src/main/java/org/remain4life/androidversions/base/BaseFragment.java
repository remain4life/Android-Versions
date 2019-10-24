package org.remain4life.androidversions.base;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.remain4life.androidversions.R;

import static org.remain4life.androidversions.helpers.Helper.ERROR_TAG;

public abstract class BaseFragment<B extends ViewDataBinding> extends Fragment {
    protected B binding;
    private Dialog dialog;

    // abstract methods for binding
    public abstract @IdRes
    int getVariable(); // returns variable from data->variable tag of xml
    public abstract @LayoutRes
    int getLayoutId(); // returns xml layout id

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.executePendingBindings();
    }

    /**
     * Universal base method to bind activity to remove this code from heirs
     */
    public void bind(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        binding.setVariable(getVariable(), this);
        binding.executePendingBindings();
    }


    public void onError(String error) {
        showDialog(R.string.error, error);
    }

    public void onError(Throwable throwable) {
        String errorMessage = throwable.getMessage();
        Log.e(ERROR_TAG, "Error: " + errorMessage);
        onError(errorMessage);
    }

    public void setDialog(Dialog dialog) {
        dismissDialog();
        this.dialog = dialog;
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void showDialog(int title,String message) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            setDialog(new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

}
