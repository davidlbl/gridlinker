package com.inditex.mecc.mectlglnk.domain.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Grid {

  GridId gridId;
  CatGroupId catalogId;

}