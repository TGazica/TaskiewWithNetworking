package osc.ada.tomislavgazica.taskie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Task extends RealmObject implements Serializable {

    @Expose
    @SerializedName("id")
    @Required
    @PrimaryKey
    private String ID;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("content")
    private String description;
    @Expose
    @SerializedName("taskPriority")
    private int priority;
    @Expose
    @SerializedName("dueDate")
    private String dueDate;
    @Expose (serialize = false)
    @SerializedName("isCompleted")
    private boolean isCompleted;
    @Expose (serialize = false)
    @SerializedName("isFavorite")
    private boolean isFavorite;
    private boolean isUploaded = false;
    private boolean isUpdated = false;

    public Task() {
    }

    public Task(String title, String description, int priority, String dueDate) {
        ID = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public Task(String title, String description, int priority, String dueDate, boolean isCompleted, boolean isFavorite) {
        ID = UUID.randomUUID().toString();
        this.title = title;
        this.isCompleted = isCompleted;
        this.isFavorite = isFavorite;
        this.description = description;
        this.priority = priority+1;
        this.dueDate = dueDate;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public void setTaskPriorityEnum(TaskPriority taskPriority) {
        this.priority = taskPriority.ordinal();
    }

    public TaskPriority getTaskPriorityEnum() {
        return TaskPriority.values()[priority];
    }

    public TaskPriority getTaskPriorityInt(){
        return TaskPriority.values()[priority];
    }

    public String convertTaskPriorityEnumToString(TaskPriority taskPriority) {
        return String.valueOf(taskPriority.toString());
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}
