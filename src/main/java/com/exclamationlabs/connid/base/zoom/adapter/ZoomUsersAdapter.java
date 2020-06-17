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

package com.exclamationlabs.connid.base.zoom.adapter;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseUsersAdapter;
import com.exclamationlabs.connid.base.zoom.model.ZoomGroup;
import com.exclamationlabs.connid.base.zoom.model.ZoomUser;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;

import java.util.Set;

import static com.exclamationlabs.connid.base.zoom.attribute.ZoomUserAttribute.*;

public class ZoomUsersAdapter extends BaseUsersAdapter<ZoomUser, ZoomGroup> {
    @Override
    protected ZoomUser constructUser(Set<Attribute> attributes, boolean creation) {
        ZoomUser user = new ZoomUser();
        user.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));

        user.setFirstName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, FIRST_NAME));
        user.setLastName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, LAST_NAME));
        user.setEmail(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, EMAIL));
        user.setLanguage(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, LANGUAGE));
        user.setTimezone(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, TIME_ZONE));
        user.setStatus(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, STATUS));
        user.setType(AdapterValueTypeConverter.getSingleAttributeValue(Integer.class, attributes, TYPE));
        return user;
    }

    @Override
    protected ConnectorObject constructConnectorObject(ZoomUser user) {
        return getConnectorObjectBuilder(user)
                .addAttribute(AttributeBuilder.build(USER_ID.name(), user.getId()))
                .addAttribute(AttributeBuilder.build(EMAIL.name(), user.getEmail()))
                .addAttribute(AttributeBuilder.build(FIRST_NAME.name(), user.getFirstName()))
                .addAttribute(AttributeBuilder.build(LAST_NAME.name(), user.getLastName()))
                .addAttribute(AttributeBuilder.build(LANGUAGE.name(), user.getLanguage()))
                .addAttribute(AttributeBuilder.build(TIME_ZONE.name(), user.getTimezone()))
                .addAttribute(AttributeBuilder.build(STATUS.name(), user.getStatus()))
                .addAttribute(AttributeBuilder.build(TYPE.name(), user.getType()))
                .addAttribute(AttributeBuilder.build(PHONE_NUMBER.name(), user.getPhoneNumber()))
                .addAttribute(AttributeBuilder.build(CREATED_AT.name(), user.getCreatedAt()))
                .addAttribute(AttributeBuilder.build(LAST_LOGIN_TIME.name(), user.getLastLoginTime()))
                .addAttribute(AttributeBuilder.build(VERIFIED.name(), user.getVerified()))
                .addAttribute(AttributeBuilder.build(PERSONAL_MEETING_ID.name(), user.getPersonalMeetingId()))
                .addAttribute(AttributeBuilder.build(GROUP_IDS.name(), user.getGroupIds()))
                .build();
    }
}
