package com.github.renegrob.resteasyjackson;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestPath;
import com.github.renegrob.MyDescription;
import com.github.renegrob.MyDescriptionTable;
import com.github.renegrob.MyExample;
import com.github.renegrob.MyJsonResponse;
import com.github.renegrob.MyRequirePrivileges;
import com.github.renegrob.MyTableRow;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;

@Path("/resteasy-jackson/quarks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JacksonResource {

    private final Set<Quark> quarks = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    public JacksonResource() {
        quarks.add(new Quark("Up", "The up quark or u quark (symbol: u) is the lightest of all quarks, a type of elementary particle, and a major constituent of matter."));
        quarks.add(new Quark("Strange", "The strange quark or s quark (from its symbol, s) is the third lightest of all quarks, a type of elementary particle."));
        quarks.add(new Quark("Charm", "The charm quark, charmed quark or c quark (from its symbol, c) is the third most massive of all quarks, a type of elementary particle."));
        quarks.add(new Quark("???", null));
    }

    @MyDescription(desc = "Custom Description")
    @GET
    // @SecurityScheme(scheme = "bearerAuth", securitySchemeName = "Authorization", in = SecuritySchemeIn.HEADER, type = SecuritySchemeType.HTTP, bearerFormat = "opaque")
    public Set<Quark> list() {
        return quarks;
    }

    @GET
    @Path("{name}")
    @MyJsonResponse(responseCode = "404", description = "Name not found.")
    public Quark getByName(@RestPath @MyExample("Up") @MyDescription(desc = "The name of the quark.") String name) {
        return quarks.stream().filter(q -> q.name.equals(name)).findFirst().orElse(null);
    }

    @POST
    @MyRequirePrivileges("Test1")
    public Set<Quark> add(Quark quark) {
        quarks.add(quark);
        return quarks;
    }

    @DELETE
    @MyDescriptionTable(rows = {
            @MyTableRow(cols = { "Key1", "value1" }),
            @MyTableRow(cols = { "Key2", "value2" }),
            @MyTableRow(cols = { "Key3", "value3" })
    })
    public Set<Quark> delete(Quark quark) {
        quarks.removeIf(existingQuark -> existingQuark.name.contentEquals(quark.name));
        return quarks;
    }

    public static class Quark {
        public String name;
        public String description;

        public Quark() {
        }

        public Quark(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}
