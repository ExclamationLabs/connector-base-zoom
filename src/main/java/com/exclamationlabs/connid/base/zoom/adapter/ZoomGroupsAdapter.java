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
import com.exclamationlabs.connid.base.connector.adapter.BaseGroupsAdapter;
import com.exclamationlabs.connid.base.zoom.model.ZoomGroup;
import com.exclamationlabs.connid.base.zoom.model.ZoomUser;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;

import java.util.Set;

import static com.exclamationlabs.connid.base.zoom.attribute.ZoomGroupAttribute.*;

public class ZoomGroupsAdapter extends BaseGroupsAdapter<ZoomUser, ZoomGroup> {


    @Override
    protected ZoomGroup constructGroup(Set<Attribute> attributes, boolean creation) {
        ZoomGroup group = new ZoomGroup();
        group.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));

        group.setName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, GROUP_NAME));
        return group;
    }

    @Override
    protected ConnectorObject constructConnectorObject(ZoomGroup group) {
        return getConnectorObjectBuilder(group)
                .addAttribute(AttributeBuilder.build(GROUP_ID.name(), group.getId()))
                .addAttribute(AttributeBuilder.build(GROUP_NAME.name(), group.getName()))
                .addAttribute(AttributeBuilder.build(TOTAL_MEMBERS.name(), group.getTotalMembers()))
                .build();
    }
}
