package com.emc.storageos.model.protection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "protection_system_update")
public class ProtectionSystemUpdateRequestParam {

    private String ipAddress;
    private Integer portNumber;
    private String userName;
    private String password;

    public ProtectionSystemUpdateRequestParam() {}
    
    public ProtectionSystemUpdateRequestParam(String ipAddress,
            Integer portNumber, String userName, String password) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Updated IP Address of the Protection System device
     * @valid IPv4 only
     * @valid example: 10.27.100.99
     */
    @XmlElement(name = "ip_address")
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Updated Management Port Number of the Protection System device
     * @valid Numerical value 1 through 65535 
     * @valid example: 7225
     */
    @XmlElement(name = "port_number")
    public Integer getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * The updated user name to connect to the Protection System device management port
     * @valid example: user1
     */
    @XmlElement(name = "user_name", nillable = true)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * The updated password to connect to the Protection System device management port
     * @valid example: abc123
     */
    @XmlElement(name = "password", nillable = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
