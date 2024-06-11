package com.psd.RealTimeSensorDataAnalyticsBackend.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.mqtt")                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
public class CredentialsConfBean{
    
    public String host;
    public String port;
    public String serveridentity;
    public String protocol;
    public String username;
    public String password;

    public void setHost(String host){
        this.host = host;
    }

    public void setPort(String port){
        this.port = port;
    }

    public void setProtocol(String protocol){
        this.protocol = protocol;
    }

    public String getMqttServerURL(){
        StringBuffer url = new StringBuffer();
        url.append(protocol);
        url.append("://");
        url.append(host);
        url.append(":");
        url.append(port);
        return url.toString();
    }

    public String getServerID(){
        return serveridentity;
    }

    public void setServeridentity(String serverId){
        this.serveridentity = serverId;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String pass){
        this.password = pass;
    }

    public void setUsername(String user){
        this.username= user;
    }

}
