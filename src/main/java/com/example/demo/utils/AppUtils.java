package com.example.demo.utils;

import com.example.demo.dto.PostDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.entity.Post;
import com.example.demo.entity.Product;
import org.springframework.beans.BeanUtils;

public class AppUtils {

    public static ProductDto entityToDto(Product product) {
        ProductDto dto = new ProductDto();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }

    public static Product dtoToEntity(ProductDto dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        return product;
    }

    public static PostDto postEntityToDto(Post post) {
        PostDto dto = new PostDto();
        BeanUtils.copyProperties(post, dto);
        return dto;
    }

    public static Post postDtoToEntity(PostDto dto) {
        Post post = new Post();
        BeanUtils.copyProperties(dto, post);
        return post;
    }
}
