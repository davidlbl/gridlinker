package com.inditex.mecc.mectlglnk.apirest.controller;

import java.util.ArrayList;
import java.util.List;

public class StoresDTO {

  private List<StoreDTO> stores = new ArrayList<>();

  public List<StoreDTO> getStores() {
    return stores;
  }

  public void setStores(List<StoreDTO> stores) {
    this.stores = stores;
  }
}
