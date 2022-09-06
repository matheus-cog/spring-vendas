package com.matheusguedes.api.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@Log4j2
public class UIController {

    @Value("classpath:springfox.css")
    private Resource cssFile;

    @ApiIgnore
    @GetMapping(value = "/webjars/springfox-swagger-ui/springfox.css")
    public void resourceCSS(HttpServletResponse response) {
        try {
            response.setHeader("content-type", "text/css;charset=UTF-8");
            byte[] file = IOUtils.toByteArray(Objects.requireNonNull(cssFile.getURI()));
            response.getOutputStream().write(file);
        } catch(Exception e) {
            log.error("Error loading customized springfox CSS file.", e);
        }
    }

}