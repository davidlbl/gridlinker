package com.inditex.mecc.mectlglnk.infrastructure.repository.db2;

public class JdbcQueryConstants {

  public static final String STORE_ID = "STORE_ID";

  public static final String CATGROUP_ID = "CATGROUP_ID";


  public static final String GET_DISTINCT_GRID_IDS_QUERY =
      "SELECT DISTINCT g.GRID_ID,CATGROUP_ID  FROM UEZARA.XCATGROUP_GRID g WHERE g.STATUS = 'LIVE' AND g.STORE_ID IN (:" + STORE_ID + ") AND g.CATGROUP_ID IN (:" + CATGROUP_ID + ")";

  public static final String XCATGROUP_GRID_INSERT_STATEMENT =
      "INSERT INTO UEZARA.XCATGROUP_GRID (STORE_ID, CATGROUP_ID, GRID_ID, STATUS, CATALOG_ID, SETTINGS) VALUES (%d, %d, '%s', '%s', %d, "
          + "%s);";
}
