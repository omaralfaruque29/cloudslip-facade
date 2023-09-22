package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

public class RemoveUsersFromTeamDTO implements Serializable {

    private ObjectId teamId;
    private List<ObjectId> userList;

    public RemoveUsersFromTeamDTO() {
    }


    public RemoveUsersFromTeamDTO(ObjectId teamId, List<ObjectId> userList) {
        this.teamId = teamId;
        this.userList = userList;
    }

    public ObjectId getTeamId() {
        return teamId;
    }

    public void setTeamId(ObjectId teamId) {
        this.teamId = teamId;
    }

    public List<ObjectId> getUserList() {
        return userList;
    }

    public void setUserList(List<ObjectId> userList) {
        this.userList = userList;
    }
}
