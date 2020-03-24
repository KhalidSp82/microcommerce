package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.web.dao.ProductDao;
import com.ecommerce.microcommerce.web.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @RequestMapping(value="/Produits", method = RequestMethod.GET)
    public List<Product> listeProduits() {
        return this.productDao.findAll();
    }

    @GetMapping(value="/Produits/{id}")
    public Optional<Product> afficherUnProduit(@PathVariable int id) {
        return this.productDao.findById(id);
    }

    @GetMapping(value = "test/produits/prixLimit/{prixLimit}")
    public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
        return productDao.findByPrixGreaterThan(prixLimit);
    }

    @GetMapping(value = "test/produits/nom/{recherche}")
    public List<Product> testeDeRequetes(@PathVariable String recherche) {
        return productDao.findByNomLike("%"+recherche+"%");
    }

    /**
     * Add product
     * @param product
     * @return
     */
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@RequestBody Product product) {

        Product productAdded =  productDao.save(product);

        if (productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Update product
     * @param product
     */
    @PutMapping (value = "/Produits")
    public void updateProduit(@RequestBody Product product) {
        productDao.save(product);
    }

    /**
     * Delete product
     * @param id
     */
    @DeleteMapping (value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {

        Optional<Product> product = this.productDao.findById(id);
        if(product.isPresent())
            productDao.delete(product.get());
    }

    /**
     * NOT WORKING
     * @param prix
     * @return
     */
    @GetMapping(value = "test/produits/prixEgale/{prix}")
    public List<Product> prixEgaleA(@PathVariable int prix) {
        return productDao.chercherUnProduitAprixEgale(prix);
    }
}
