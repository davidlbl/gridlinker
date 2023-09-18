package com.inditex.mecc.mectlglnk.apirest.controller;

import com.inditex.mecc.mectlglnk.domain.usecase.LinkGridsUseCase;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Product api controller.
 */
@RestController
public class GridController {

  private final LinkGridsUseCase linkGridsUseCase;

  public GridController(final LinkGridsUseCase linkGridsUseCase) {
    this.linkGridsUseCase = linkGridsUseCase;
  }

  @PostMapping(value = "/gridlink", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> linkGrids(@RequestBody final StoresDTO stores) {
    this.linkGridsUseCase.linkGrids(stores.getStores().stream().map(StoreDTO::getStoreId).toList());
    return ResponseEntity.ok().build();
  }

}
