package com.inditex.mecc.mectlglnk.infrastructure.repository.db2;

public class JdbcQueryConstants {

  public static final String STORE_ID = "STORE_ID";

  public static final String XCATGROUP_GRID_STATEMENT =
      "select distinct CATGROUP_ID, GRID_ID, STORE_ID from uezara.XCATGROUP_GRID where STORE_ID in ({" + STORE_ID + "}) and status in 'LIVE'\n"
          + " AND CATGROUP_ID in  (SELECT A.CATGROUP_ID_CHILD FROM UEZARA.CATGRPREL A INNER JOIN UEZARA.CATGROUP P ON (P.CATGROUP_ID = A"
          + ".CATGROUP_ID_CHILD)\n"
          + "WHERE a.CATALOG_ID IN (SELECT CATALOG_ID FROM UEZARA.XTIENDA_CATALOGO WHERE STORE_ID IN ({" + STORE_ID + "})) AND P.MARKFORDELETE = 0\n"
          + ") AND CATGROUP_ID NOT IN (SELECT CATGROUP_ID FROM UEZARA.XCATGROUP_ATRIB WHERE ID_XATRIBUTO_CATEGORIA  IN ('HIDDEN',"
          + "'HIDDEN-PRO','ACCESSIBLE'))\n"
          + "  AND CATGROUP_ID NOT IN  (SELECT CATGROUP_ID FROM UEZARA.XCATGPR_EXCLUSION WHERE  STORE_ID IN ({" + STORE_ID + "}))";
}
