package org.itheima.edu.tutorials.web.service.impl;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Poplar on 2017/6/16.
 */
@Service
public class RunServiceDockerImpl extends BaseRunServiceImpl{

    @Value("${config.dir.root}")
    String rootPath;

    @Value("${docker.uri}")
    String docker_uri;

    @Value("${docker.image}")
    String docker_image;

    private DockerClient docker;

    @PostConstruct
    public void postConstruct() {
        System.out.println("I'm  init  method  using  @PostConstrut....");
        try {
            init();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    public void init() throws ServletException {
        try {
            DefaultDockerClient.Builder builder = DefaultDockerClient
                    .fromEnv();
            if(!TextUtils.isEmpty(docker_uri)){
                builder.uri(docker_uri);
            }

            docker = builder.build();
        } catch (DockerCertificateException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("I'm  destory method  using  @PreDestroy.....");
    }

    @Override
    boolean runTest(String command, String reportPath) {
        String id = null;

        try {
            String[] commands = StringUtils.split(command, " ");

            final HostConfig hostConfig = HostConfig.builder()
                    .appendBinds(HostConfig.Bind.from(rootPath).to(rootPath).readOnly(true).build())
                    .appendBinds(HostConfig.Bind.from(reportPath).to(reportPath).readOnly(false).build())
                    .build();
            // Create container with exposed ports
            final ContainerConfig containerConfig = ContainerConfig.builder().hostConfig(hostConfig)
                    .image(docker_image).tty(true).attachStderr(true).attachStdin(true)
                    .attachStdout(true).build();

            final ContainerCreation creation = docker.createContainer(containerConfig);
            id = creation.id();

            // Inspect container
            final ContainerInfo info = docker.inspectContainer(id);
            System.out.println("ContainerInfo: " + info);

            // Start container
            docker.startContainer(id);

            final ExecCreation execCreation = docker.execCreate(id, commands,
                    DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr());
            final LogStream output = docker.execStart(execCreation.id());
            System.out.println("ready go ");
            closeContainerDelay(id);

            final String execOutput = output.readFully();
            System.out.println("==> execOutput: " + execOutput);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if(!StringUtils.isEmpty(id)){
                try {
                    // Kill container
                    docker.killContainer(id);
                    System.out.println("killed :" + id);
                    // Remove container
                    docker.removeContainer(id, true);
                    // Close the docker client
                    //docker.close();
                } catch (DockerException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void closeContainerDelay(String id) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    //docker timer make sure docker stop after run 15seconds
                    docker.stopContainer(id, 0);
                    docker.removeContainer(id, true);
                } catch (Exception e) {

                }
            }
        }, 15 * 1000);
    }

}
