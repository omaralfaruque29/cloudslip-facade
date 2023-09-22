package com.cloudslip.facade.dto;


import com.cloudslip.facade.enums.DockerRegistryType;
import org.bson.types.ObjectId;

public class SaveCompanyDockerHubInfoInputDTO extends BaseInputDTO {
    private DockerRegistryType dockerRegistryType = DockerRegistryType.DOCKER_HUB;
    private String dockerRegistryServer;
    private String dockerhubId;
    private String dockerhubPassword;
    private String dockerhubEmail;

    public SaveCompanyDockerHubInfoInputDTO() {
    }

    public DockerRegistryType getDockerRegistryType() {
        return dockerRegistryType;
    }

    public void setDockerRegistryType(DockerRegistryType dockerRegistryType) {
        this.dockerRegistryType = dockerRegistryType;
    }

    public String getDockerRegistryServer() {
        return dockerRegistryServer;
    }

    public void setDockerRegistryServer(String dockerRegistryServer) {
        this.dockerRegistryServer = dockerRegistryServer;
    }

    public String getDockerhubId() {
        return dockerhubId;
    }

    public void setDockerhubId(String dockerhubId) {
        this.dockerhubId = dockerhubId;
    }

    public String getDockerhubPassword() {
        return dockerhubPassword;
    }

    public void setDockerhubPassword(String dockerhubPassword) {
        this.dockerhubPassword = dockerhubPassword;
    }

    public String getDockerhubEmail() {
        return dockerhubEmail;
    }

    public void setDockerhubEmail(String dockerhubEmail) {
        this.dockerhubEmail = dockerhubEmail;
    }
}
