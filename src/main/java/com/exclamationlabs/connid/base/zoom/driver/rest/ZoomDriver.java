/*
    Copyright 2020 Exclamation Labs
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.exclamationlabs.connid.base.zoom.driver.rest;

import com.exclamationlabs.connid.base.connector.configuration.ConnectorProperty;
import com.exclamationlabs.connid.base.connector.driver.rest.BaseRestDriver;
import com.exclamationlabs.connid.base.connector.driver.rest.RestFaultProcessor;
import com.exclamationlabs.connid.base.zoom.model.ZoomGroupMember;
import com.exclamationlabs.connid.base.zoom.model.ZoomUserCreationType;
import com.exclamationlabs.connid.base.zoom.model.ZoomGroup;
import com.exclamationlabs.connid.base.zoom.model.ZoomUser;
import com.exclamationlabs.connid.base.zoom.model.request.GroupMembersRequest;
import com.exclamationlabs.connid.base.zoom.model.request.UserCreationRequest;
import com.exclamationlabs.connid.base.zoom.model.response.GroupMembersResponse;
import com.exclamationlabs.connid.base.zoom.model.response.ListGroupsResponse;
import com.exclamationlabs.connid.base.zoom.model.response.ListUsersResponse;
import org.apache.commons.lang3.StringUtils;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ZoomDriver extends BaseRestDriver<ZoomUser, ZoomGroup> {

    @Override
    protected RestFaultProcessor getFaultProcessor() {
        return ZoomFaultProcessor.getInstance();
    }

    @Override
    protected String getBaseServiceUrl() {
        return "https://api.zoom.us/v2";
    }

    @Override
    protected boolean usesBearerAuthorization() {
        return true;
    }

    @Override
    public Set<ConnectorProperty> getRequiredPropertyNames() {
        return null;
    }

    @Override
    public void test() throws ConnectorException {
        try {
            executeGetRequest("/users/me/settings", null);
        } catch (Exception ce) {
            // Disregard usage rage limit exceed when Midpoint calls test() method repeatedly for unknown reason
            if (!StringUtils.containsIgnoreCase(ce.getMessage(),"Code: 429")) {
                throw new ConnectorException("Self-identification for Zoom connection user failed." + ce.getMessage(), ce);
            }
        }
    }

    @Override
    public void close() {
        configuration = null;
        authenticator = null;
    }

    @Override
    public String createUser(ZoomUser userModel) throws ConnectorException {
        ZoomUserCreationType defaultZoomUserCreationType = ZoomUserCreationType.CREATE;
        UserCreationRequest requestData = new UserCreationRequest(
                defaultZoomUserCreationType.getZoomName(), userModel);
        ZoomUser newUser = executePostRequest("/users", ZoomUser.class, requestData);

        if (newUser == null) {
            throw new ConnectorException("Response from user creation was invalid");
        }
        return newUser.getId();
    }

    @Override
    public String createGroup(ZoomGroup groupModel) throws ConnectorException {
        ZoomGroup newGroup = executePostRequest("/groups", ZoomGroup.class, groupModel);
        if (newGroup == null || newGroup.getId() == null) {
            throw new ConnectorException("Response from group creation was invalid");
        }

        return newGroup.getId();
    }

    @Override
    public void updateUser(String userId, ZoomUser userModel) throws ConnectorException {
        executePatchRequest("/users/" + userId, null, userModel);
    }

    @Override
    public void updateGroup(String groupId, ZoomGroup groupModel) throws ConnectorException {
        executePatchRequest("/groups/" + groupId, null, groupModel);
    }

    @Override
    public void deleteUser(String userId) throws ConnectorException {
        executeDeleteRequest("/users/" + userId, null);
    }

    @Override
    public void deleteGroup(String groupId) throws ConnectorException {
        executeDeleteRequest("/groups/" + groupId, null);
    }

    @Override
    public List<ZoomUser> getUsers() throws ConnectorException {
        ListUsersResponse response = executeGetRequest("/users", ListUsersResponse.class);
        return response.getUsers();
    }

    @Override
    public List<ZoomGroup> getGroups() throws ConnectorException {
        ListGroupsResponse response = executeGetRequest("/groups", ListGroupsResponse.class);
        return response.getGroups();
    }

    @Override
    public ZoomUser getUser(String userId) throws ConnectorException {
        return executeGetRequest("/users/" + userId, ZoomUser.class);
    }

    @Override
    public ZoomGroup getGroup(String groupId) throws ConnectorException {
        return executeGetRequest("/groups/" + groupId, ZoomGroup.class);
    }

    @Override
    public void addGroupToUser(String groupId, String userId) throws ConnectorException {
        List<ZoomGroupMember> memberList = new ArrayList<>();
        memberList.add(new ZoomGroupMember(userId));
        GroupMembersRequest membersRequest = new GroupMembersRequest(memberList);
        GroupMembersResponse response = executePostRequest("/groups/" +
                groupId + "/members", GroupMembersResponse.class, membersRequest);
        if (response == null || response.getAddedAt() == null) {
            throw new ConnectorException("Unexpected response received while adding user to group");
        }
    }

    @Override
    public void removeGroupFromUser(String groupId, String userId) throws ConnectorException {
        executeDeleteRequest("/groups/" + groupId + "/members/" + userId, null);
    }
}