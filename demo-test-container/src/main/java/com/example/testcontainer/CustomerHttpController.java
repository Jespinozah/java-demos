package com.example.testcontainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
class CustomerHttpController {

    private final CustomerService customerService;

    @Autowired
    CustomerHttpController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    Iterable<Customer> customers() {
        return this.customerService.findAll();
    }

    @GetMapping("/{name}")
    Iterable<Customer> byName(@PathVariable String name) {
        return this.customerService.byName(name);
    }


    @GetMapping("/test")
    String test() {
        return "Hola";
    }
}
