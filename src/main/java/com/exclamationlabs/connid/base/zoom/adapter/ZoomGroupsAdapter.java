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
