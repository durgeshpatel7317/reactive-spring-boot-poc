package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.repo.ProductRepo;
import com.example.demo.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ProductService {
    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Flux<ProductDto> getProducts() {
        return productRepo.findAll().map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProduct(String id) {
        return productRepo.findById(id).map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductInRange(Double min, Double max) {
        return productRepo.findByPriceBetween(Range.closed(min, max))
            .onBackpressureBuffer(5)
            .map(AppUtils::entityToDto);

//        resp.subscribe(new CustomSubscriber<>());

//        return resp;
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono.map(AppUtils::dtoToEntity)
            .flatMap(productRepo::insert)
            .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id) {
        return productRepo.findById(id)
            .flatMap(p -> productDtoMono.map(AppUtils::dtoToEntity))
            .doOnNext(e -> e.setId(id))
            .flatMap(productRepo::save)
            .map(AppUtils::entityToDto);

    }

    public Mono<Void> deleteProduct(String id) {
        return productRepo.deleteById(id);
    }
}
