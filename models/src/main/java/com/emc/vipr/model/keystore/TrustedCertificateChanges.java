/**
 *  Copyright (c) 2014 EMC Corporation
 * All Rights Reserved
 *
 * This software contains the intellectual property of EMC Corporation
 * or is licensed to EMC Corporation from third parties.  Use of this
 * software and the intellectual property contained therein is expressly
 * limited to the terms and conditions of the License Agreement under which
 * it is provided by or on behalf of EMC.
 */
package com.emc.vipr.model.keystore;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "trusted_certificate_changes")
public class TrustedCertificateChanges {

    private List<String> add;
    private List<String> remove;

    @XmlElementWrapper(name = "add")
    @XmlElement(name = "certificate")
    public List<String> getAdd() {
        return add;
    }

    public void setAdd(List<String> add) {
        this.add = add;
    }

    @XmlElementWrapper(name = "remove")
    @XmlElement(name = "certificate")
    public List<String> getRemove() {
        return remove;
    }

    public void setRemove(List<String> remove) {
        this.remove = remove;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("ADD=");
        builder.append(getAdd());

        builder.append(", REMOVE=");
        builder.append(getRemove());

        return builder.toString();
    }

}
