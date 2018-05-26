package osc.ada.tomislavgazica.taskie.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.util.DialogClickListener;

public class AlertDialogFragment extends DialogFragment {

        private Task task;

        private DialogClickListener listener;

        public void setTask(Task task) {
            this.task = task;
        }

        public void setListener(DialogClickListener listener) {
            this.listener = listener;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.alert_dialog_fragment, container, false);

            TextView edit = v.findViewById(R.id.textview_dialog_edit);
            TextView delete = v.findViewById(R.id.textview_dialog_delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onEditClick(task);
                        dismiss();
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeleteClick(task);
                        dismiss();
                    }
                }
            });

            return v;
        }
    }
