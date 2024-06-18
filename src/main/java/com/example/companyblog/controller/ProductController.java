package com.example.companyblog.controller;

import com.example.companyblog.entity.Post;
import com.example.companyblog.entity.Product;
import com.example.companyblog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }


    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestParam ("name") String name,
                                                 @RequestParam ("description") String description,
                                                 @RequestParam ("image") MultipartFile image
    ) throws IOException {
        String upLoadfile = "./uploads";
        File dir = new File(upLoadfile);
        if(!dir.exists()){
            dir.mkdirs();
        }
        Path filePath = Paths.get(upLoadfile+image.getOriginalFilename());
        Files.write(filePath, image.getBytes());
        String pathfile = "/upload/"+image.getOriginalFilename();
        // Tạo đường dẫn đầy đủ của ảnh
        String imgPath = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(image.getOriginalFilename())
                .toUriString();

        System.out.println("This is path1: "+imgPath);
        System.out.println("This is path2:"+ pathfile);

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setImg(pathfile);
        Product createPost = productService.createProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(createPost);
    }
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.updateProduct(id, productDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}