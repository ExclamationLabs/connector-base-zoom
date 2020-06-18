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

package com.exclamationlabs.connid.base.zoom;

import com.exclamationlabs.connid.base.connector.BaseConnector;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeMapBuilder;
import com.exclamationlabs.connid.base.connector.authenticator.JWTHS256Authenticator;
import com.exclamationlabs.connid.base.zoom.adapter.ZoomGroupsAdapter;
import com.exclamationlabs.connid.base.zoom.adapter.ZoomUsersAdapter;
import com.exclamationlabs.connid.base.zoom.attribute.ZoomGroupAttribute;
import com.exclamationlabs.connid.base.zoom.attribute.ZoomUserAttribute;
import com.exclamationlabs.connid.base.zoom.configuration.ZoomConfiguration;
import com.exclamationlabs.connid.base.zoom.driver.rest.ZoomDriver;
import com.exclamationlabs.connid.base.zoom.model.ZoomGroup;
import com.exclamationlabs.connid.base.zoom.model.ZoomUser;
import org.identityconnectors.framework.spi.ConnectorClass;

import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.INTEGER;
import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.STRING;
import static com.exclamationlabs.connid.base.zoom.attribute.ZoomGroupAttribute.*;
import static com.exclamationlabs.connid.base.zoom.attribute.ZoomUserAttribute.*;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.NOT_UPDATEABLE;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.REQUIRED;

@ConnectorClass(displayNameKey = "zoom.connector.display", configurationClass = ZoomConfiguration.class)
public class ZoomConnector extends BaseConnector<ZoomUser, ZoomGroup> {

    public ZoomConnector() {

        setAuthenticator(new JWTHS256Authenticator());
        setDriver(new ZoomDriver());
        setUsersAdapter(new ZoomUsersAdapter());
        setGroupsAdapter(new ZoomGroupsAdapter());
        setUserAttributes( new ConnectorAttributeMapBuilder<>(ZoomUserAttribute.class)
                .add(USER_ID, STRING, NOT_UPDATEABLE)
                .add(FIRST_NAME, STRING)
                .add(LAST_NAME, STRING)
                .add(EMAIL, STRING)
                .add(PASSWORD, STRING, NOT_UPDATEABLE)
                .add(LANGUAGE, STRING)
                .add(TIME_ZONE, STRING)
                .add(PHONE_NUMBER, STRING)
                .add(STATUS, STRING)
                .add(TYPE, INTEGER, NOT_UPDATEABLE)
                .add(CREATED_AT, STRING, NOT_UPDATEABLE)
                .add(LAST_LOGIN_TIME, STRING, NOT_UPDATEABLE)
                .add(VERIFIED, STRING, NOT_UPDATEABLE)
                .add(PERSONAL_MEETING_ID, STRING, NOT_UPDATEABLE)
                .add(GROUP_IDS, STRING, NOT_UPDATEABLE)
                .build());
        setGroupAttributes(new ConnectorAttributeMapBuilder<>(ZoomGroupAttribute.class)
                .add(GROUP_ID, STRING, NOT_UPDATEABLE)
                .add(GROUP_NAME, STRING, REQUIRED)
                .add(TOTAL_MEMBERS, INTEGER, NOT_UPDATEABLE)
                .build());
    }

}
