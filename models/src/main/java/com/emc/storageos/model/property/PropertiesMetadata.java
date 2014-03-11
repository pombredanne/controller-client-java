/**
 * Copyright (c) 2012 EMC Corporation
 * All Rights Reserved
 *
 * This software contains the intellectual property of EMC Corporation
 * or is licensed to EMC Corporation from third parties.  Use of this
 * software and the intellectual property contained therein is expressly
 * limited to the terms and conditions of the License Agreement under which
 * it is provided by or on behalf of EMC.
 */
package com.emc.storageos.model.property;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;
import java.util.TreeMap;

@XmlRootElement(name = "properties_metadata")
public class PropertiesMetadata {
    // Singleton instance provides access to global metadata
    private static PropertiesMetadata INSTANCE;

    private Map<String, PropertyMetadata> metadata;

    public static PropertiesMetadata getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PropertiesMetadata();
        }
        return INSTANCE;
    }

    public static Map<String, PropertyMetadata> getGlobalMetadata() {
        return getInstance().getMetadata();
    }

    public void setMetadata(Map<String, PropertyMetadata> newMetadata) {
        System.out.println("Setting: " + newMetadata);
        this.metadata = new TreeMap<String, PropertyMetadata>(newMetadata);
        INSTANCE = this;
    }

    @XmlElementWrapper(name = "metadata")
    public Map<String, PropertyMetadata> getMetadata() {
        if (metadata == null) {
            metadata = new TreeMap<String, PropertyMetadata>();
        }
        return metadata;
    }
}