package org.remain4life.androidversions.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.remain4life.androidversions.R;

import static org.remain4life.androidversions.helpers.Helper.ERROR_TAG;

public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity implements Observable {
    protected B binding;
    private Dialog dialog;
    private PropertyChangeRegistry mCallbacks;

    // abstract methods for binding
    public abstract @IdRes
    int getVariable(); // returns variable from data->variable tag of xml
    public abstract @LayoutRes
    int getLayoutId(); // returns xml layout id

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(savedInstanceState);
    }

    /**
     * Universal base method to bind activity to remove this code from heirs
     */
    public void bind(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
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
        setDialog(new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show());
    }

    public void showDialog(String title, String message,
                           DialogInterface.OnClickListener okListener,
                           DialogInterface.OnClickListener cancelListener) {
        setDialog(new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, okListener)
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .show());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    /*
       Observable methods implementation
     */

    @Override
    public void addOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                mCallbacks = new PropertyChangeRegistry();
            }
        }
        mCallbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.remove(callback);
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    public void notifyChange() {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.notifyCallbacks(this, 0, null);
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with {@link Bindable} to generate a field in
     * <code>BR</code> to be used as <code>fieldId</code>.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    public void notifyPropertyChanged(int fieldId) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.notifyCallbacks(this, fieldId, null);
    }

}
