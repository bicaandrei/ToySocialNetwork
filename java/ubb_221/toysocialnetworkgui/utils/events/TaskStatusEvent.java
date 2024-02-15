package ubb_221.toysocialnetworkgui.utils.events;


import ubb_221.toysocialnetworkgui.domain.User;

public class TaskStatusEvent implements Event {
    private TaskExecutionStatusEventType type;
    private User user;
    public TaskStatusEvent(TaskExecutionStatusEventType type, User user) {
        this.user=user;
        this.type=type;
    }

    public User getTask() {
        return user;
    }

    public void setTask(User task) {
        this.user = task;
    }

    public TaskExecutionStatusEventType getType() {
        return type;
    }

    public void setType(TaskExecutionStatusEventType type) {
        this.type = type;
    }
}
