package com.wbt.companymicroservice.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"/api/v1/companies"})
public record CompanyController(CompanyService companyService) {

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> companies() {
        return new ResponseEntity<>(this.companyService.fetchAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<?> companyById(final @PathVariable(name = "id") Long id) {
        final var optionalCompanyResponse = this.companyService.findById(id);
        if (optionalCompanyResponse.isPresent())
            return new ResponseEntity<>(optionalCompanyResponse.get(), HttpStatus.OK);
        return new ResponseEntity<>("Company resource with id %s not found".formatted(id), HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String> create(final @RequestBody CompanyRequest companyRequest) {
        final var isCompanyCreated = this.companyService.create(companyRequest);
        if (isCompanyCreated) return new ResponseEntity<>("Company resource created successfully", HttpStatus.CREATED);
        return new ResponseEntity<>("Unable to create company resource", HttpStatus.BAD_REQUEST);
    }

    @PutMapping(path = {"/{id}"})
    public ResponseEntity<String> update(final @PathVariable(name = "id") Long id, final @RequestBody CompanyRequest updateRequest) {
        final var isCompanyUpdated = this.companyService.update(id, updateRequest);
        if (isCompanyUpdated)
            return new ResponseEntity<>("Company resource with Id %s successfully updated".formatted(id), HttpStatus.OK);
        return new ResponseEntity<>("Unable to update company resource with Id %s".formatted(id), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<String> delete(final @PathVariable(name = "id") Long id) {
        final var isDeleted = this.companyService.delete(id);
        if (isDeleted) return new ResponseEntity<>("Company resource successfully removed", HttpStatus.OK);
        return new ResponseEntity<>("Unable to delete company resource with Id %s".formatted(id), HttpStatus.BAD_REQUEST);
    }

}
