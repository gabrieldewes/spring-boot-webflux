package io.dewes.springbootwebflux.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(final CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Flux<Car> findAll() {
        return this.carRepository.findAll();
    }

    public Mono<Car> findById(final UUID id) {
        return this.carRepository.findById(id);
    }

    public Mono<Car> save(Car car) {
        return Mono.fromSupplier(() -> {
            this.carRepository.insert(car).subscribe();
            return car;
        });
    }

    public Mono<Car> update(Car car) {
        return Mono.fromSupplier(() -> {
            this.carRepository.save(car).subscribe();
            return car;
        });
    }

    public Mono<Void> delete(Car car) {
        return this.carRepository.delete(car);
    }

}
