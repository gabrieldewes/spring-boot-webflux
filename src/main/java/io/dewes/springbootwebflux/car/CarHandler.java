package io.dewes.springbootwebflux.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
public class CarHandler {

    private final CarService carService;

    @Autowired
    public CarHandler(final CarService carService) {
        this.carService = carService;
    }

    public Mono<ServerResponse> all(ServerRequest serverRequest) {
        return ok().contentType(APPLICATION_JSON)
                .body(fromPublisher(this.carService.findAll(), Car.class));
    }

    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        final UUID id = UUID.fromString(serverRequest.pathVariable("id"));
        final Mono<Car> carMono = this.carService.findById(id);
        return carMono
                .flatMap(car -> ok().contentType(APPLICATION_JSON)
                        .body(fromPublisher(carMono, Car.class))
                        .switchIfEmpty(notFound().build()));
    }

    public Mono<ServerResponse> post(ServerRequest serverRequest) {
        final Mono<Car> carMono = serverRequest.bodyToMono(Car.class);
        final UUID id = UUID.randomUUID();
        return created(UriComponentsBuilder.fromPath("/car" + id).build().toUri())
                .contentType(APPLICATION_JSON)
                .body(fromPublisher(carMono
                        .map(car -> this.buildCar(id, car))
                        .flatMap(this.carService::save), Car.class));
    }

    public Mono<ServerResponse> put(ServerRequest serverRequest) {
        final UUID id = UUID.fromString(serverRequest.pathVariable("id"));
        final Mono<Car> car = serverRequest.bodyToMono(Car.class);
        return this.carService.findById(id)
                .flatMap(old ->
                    ok().contentType(APPLICATION_JSON)
                            .body(fromPublisher(car
                                    .map(c -> this.buildCar(id, c))
                                    .flatMap(this.carService::save), Car.class))
                            .switchIfEmpty(notFound().build()));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        final UUID id = UUID.fromString(request.pathVariable("id"));
        return this.carService
                .findById(id)
                .flatMap(p -> noContent().build(this.carService.delete(p)))
                .switchIfEmpty(notFound().build());
    }

    private Car buildCar(UUID id, Car car) {
        return Car.builder().id(id).model(car.getModel()).plate(car.getPlate()).yearModel(car.getYearModel())
                .yearManufacture(car.getYearManufacture()).manufacturer(car.getManufacturer()).build();
    }

}

