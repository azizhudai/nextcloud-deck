package it.niedermann.nextcloud.deck.ui.login;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.nextcloud.android.sso.AccountImporter;
import com.nextcloud.android.sso.exceptions.AndroidGetAccountsPermissionNotGranted;
import com.nextcloud.android.sso.exceptions.NextcloudFilesAppNotInstalledException;
import com.nextcloud.android.sso.model.SingleSignOnAccount;
import com.nextcloud.android.sso.ui.UiExceptionManager;

import java.util.Objects;

import it.niedermann.nextcloud.deck.ui.DrawerActivity;

public class LoginDialogFragment extends DialogFragment {
    private static final int FETCH_PERMISSION_RESULT_ID = 1;
    private DrawerActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        activity = ((DrawerActivity) Objects.requireNonNull(getActivity()));
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            // Already have permission
            showDialog();
        } else {
            // Should we show an explanation?
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.GET_ACCOUNTS)) {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, FETCH_PERMISSION_RESULT_ID);
            } else {
                UiExceptionManager.showDialogForException(getContext(), new AndroidGetAccountsPermissionNotGranted());
                Log.w("Deck", "=============================================================");
                Log.w("Deck", "Permission not granted!");
            }
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FETCH_PERMISSION_RESULT_ID) {
            showDialog();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showDialog() {
        try {
            AccountImporter.pickNewAccount(this);
        } catch (NextcloudFilesAppNotInstalledException e) {
            UiExceptionManager.showDialogForException(getContext(), e);
            Log.w("Deck", "=============================================================");
            Log.w("Deck", "Nextcloud app is not installed. Cannot choose account");
            e.printStackTrace();
        } catch (AndroidGetAccountsPermissionNotGranted e) {
            UiExceptionManager.showDialogForException(getContext(), e);
            Log.w("Deck", "=============================================================");
            Log.w("Deck", "Permission not granted!");
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AccountImporter.onActivityResult(requestCode, resultCode, data, LoginDialogFragment.this, (SingleSignOnAccount account) -> {
            activity.onAccountChoose(account);
        });
    }
}
