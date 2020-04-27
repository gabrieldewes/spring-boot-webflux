package io.dewes.springbootwebflux.car;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CarRouter {

    @Bean
    public RouterFunction<ServerResponse> route(CarHandler carHandler) {
        return RouterFunctions
                .route(GET("/car").and(accept(APPLICATION_JSON)), carHandler::all)

                .andRoute(GET("/car/{id}").and(accept(APPLICATION_JSON)), carHandler::get)

                .andRoute(POST("/car").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), carHandler::post)

                .andRoute(PUT("/car/{id}").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), carHandler::put)

                .andRoute(DELETE("/car/{id}"), carHandler::delete);
    }

}
