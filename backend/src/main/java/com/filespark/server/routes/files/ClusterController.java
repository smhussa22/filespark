package com.filespark.server.routes.files;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filespark.server.api.mongodb.models.FileCluster;
import com.filespark.server.requests.CreateClusterRequest;
import com.filespark.server.responses.CreateClusterResponse;
import com.filespark.server.services.FileClusterService;

import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/clusters")
public class ClusterController {

    private final FileClusterService fileClusterService;

    public ClusterController(FileClusterService service) { 

        this.fileClusterService = service;

    }

    @PostMapping
    public CreateClusterResponse createCluster(@AuthenticationPrincipal Jwt jwt, @RequestBody CreateClusterRequest request) {

        String name = request.getName();
        if (name == null || name.isEmpty()) {

            throw new RuntimeException("Cluster name empty");

        }

        String googleId = jwt.getSubject();
        String email = jwt.getClaim("email");
        String nameFromToken = jwt.getClaim("name");
        String picture = jwt.getClaim("picture");

        FileCluster cluster = fileClusterService.createCluster(ownerId, name);

        return new CreateClusterResponse(cluster.getId(), cluster.getName());
        
    }
    
}

