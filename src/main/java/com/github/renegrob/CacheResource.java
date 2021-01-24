package com.github.renegrob;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.Body;

@Path("/cache")
public class CacheResource {

    @Inject
    InfinispanCacheBean cacheBean;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> get() {
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> entry: cacheBean.getMyCache().entrySet()) {
            result.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return result;
    }

    @GET
    @Path("{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByKey(@PathParam("key") String key) {
        if (!cacheBean.getMyCache().containsKey(key)) {
            throw new NotFoundException();
        }
        return Objects.toString(cacheBean.getMyCache().get(key), null);
    }

    @POST
    @Path("{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public void store(@PathParam("key") String key, String value) {
        cacheBean.getMyCache().put(key, value);
    }
}