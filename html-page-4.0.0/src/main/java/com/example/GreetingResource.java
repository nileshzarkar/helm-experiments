package com.example;

import java.io.IOException;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/page")
public class GreetingResource {

    // Inject the color property from application.properties
    @ConfigProperty(name = "page.color", defaultValue = "blue")
    String pageColor;

    @ConfigProperty(name = "image.tag", defaultValue = "blue")
    String imageTag;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String hello() throws IOException {


        // Returning an HTML string with inline CSS for blue-colored text in a table format
        return "<html>" +
            "<body>" +
            "<table border='1' style='width:50%; margin:auto;'>" +
                "<tr>" +
                    "<th colspan='2' style='text-align:center;'>Configurable Color HTML Page</th>" +
                "</tr>" +
                "<tr>" +
                    "<td>Page Color Configuration</td>" +
                    "<td style='color:" + pageColor + "; font-weight:bold;'>" + pageColor + "</td>" +
                "</tr>" +
                "<tr>" +
                    "<td>Image Tag</td>" +
                    "<td>" + imageTag + "</td>" +
                "</tr>" +
            "</table>" +
            "</body>" +
            "</html>";
    }

}
