package osc.ada.tomislavgazica.taskie.util;

import osc.ada.tomislavgazica.taskie.model.Task;

public interface DialogClickListener {
    void onEditClick(Task task);
    void onDeleteClick(Task task);
}
