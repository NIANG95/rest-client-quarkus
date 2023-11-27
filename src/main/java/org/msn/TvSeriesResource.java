package org.msn;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.msn.model.Episode;
import org.msn.model.TVSerie;
import org.msn.proxy.EpisodeProxy;
import org.msn.proxy.TvSeriesProxy;

import java.util.ArrayList;
import java.util.List;

@Path("/tvseries")
public class TvSeriesResource {

    @RestClient
    TvSeriesProxy tvSeriesProxy;

    @RestClient
    EpisodeProxy episodeProxy;

    private List<TVSerie> tvSeries = new ArrayList<>();

/*    @GET
    @Path("series")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("title") String title) {
        TVSerie tvSerie = tvSeriesProxy.get(title);
        tvSeries.add(tvSerie);
        return Response.ok(tvSeries).build();
    }*/

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("title") String title) {
        TVSerie tvSerie = getTVSeries(title);
        List<Episode> episodes = getEpisodes(tvSerie.getId());
        return Response.ok(episodes).build();
    }

    @Fallback(fallbackMethod = "fallbackGetTvSeries")
    public TVSerie getTVSeries(String title) {
        return tvSeriesProxy.get(title);
    }

    private TVSerie fallbackGetTvSeries(String title) {
        TVSerie tvSerie = new TVSerie();
        tvSerie.setId(1L);
        return tvSerie;
    }

    @Fallback(fallbackMethod = "fallbackGetEpisode")
    public List<Episode> getEpisodes(Long id) {
        return episodeProxy.get(id);
    }

    private List<Episode> fallbackGetEpisode(Long id) {
        return new ArrayList<>();
    }

}
