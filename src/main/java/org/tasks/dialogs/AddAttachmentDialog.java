package org.tasks.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import org.tasks.R;
import org.tasks.injection.DialogFragmentComponent;
import org.tasks.injection.InjectingDialogFragment;
import org.tasks.preferences.Device;

import java.util.List;

import javax.inject.Inject;

import static com.google.common.collect.Lists.newArrayList;

public class AddAttachmentDialog extends InjectingDialogFragment {

    @Override
    protected void inject(DialogFragmentComponent component) {
        component.inject(this);
    }

    public interface AddAttachmentCallback {
        void takePicture();

        void recordNote();

        void pickFromGallery();

        void pickFromStorage();
    }

    @Inject DialogBuilder dialogBuilder;
    @Inject Device device;

    private AddAttachmentCallback callback;
    private DialogInterface.OnCancelListener onCancelListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> entries = newArrayList();
        final List<Runnable> actions = newArrayList();
        if (device.hasCamera()) {
            entries.add(getString(R.string.take_a_picture));
            actions.add(new Runnable() {
                @Override
                public void run() {
                    callback.takePicture();
                }
            });
        }
        entries.add(getString(R.string.premium_record_audio));
        actions.add(new Runnable() {
            @Override
            public void run() {
                callback.recordNote();
            }
        });
        if (device.hasGallery()) {
            entries.add(getString(R.string.pick_from_gallery));
            actions.add(new Runnable() {
                @Override
                public void run() {
                    callback.pickFromGallery();
                }
            });
        }
        entries.add(getString(R.string.pick_from_storage));
        actions.add(new Runnable() {
            @Override
            public void run() {
                callback.pickFromStorage();
            }
        });
        return dialogBuilder.newDialog()
                .setItems(entries, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actions.get(which).run();
                    }
                })
                .show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        if (onCancelListener != null) {
            onCancelListener.onCancel(dialog);
        }
    }

    public void setAddAttachmentCallback(AddAttachmentCallback callback) {
        this.callback = callback;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }
}
