﻿DROP TABLE IF EXISTS lodes_blocks_rac;
CREATE TABLE lodes_blocks_rac(
blockid character varying(15),
C000 int,
CA01 int,
CA02 int,
CA03 int,
CE01 int,
CE02 int,
CE03 int,
CNS01 int,
CNS02 int,
CNS03 int,
CNS04 int,
CNS05 int,
CNS06 int,
CNS07 int,
CNS08 int,
CNS09 int,
CNS10 int,
CNS11 int,
CNS12 int,
CNS13 int,
CNS14 int,
CNS15 int,
CNS16 int,
CNS17 int,
CNS18 int,
CNS19 int,
CNS20 int,
CR01 int,
CR02 int,
CR03 int,
CR04 int,
CR05 int,
CR07 int,
CT01 int,
CT02 int,
CD01 int,
CD02 int,
CD03 int,
CD04 int,
CS01 int,
CS02 int,
createdate character varying(8),
PRIMARY KEY(blockid)
);

COPY lodes_blocks_rac 
FROM 'C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/demographic/or_rac_S000_JT00_2010.csv' DELIMITER ',' CSV HEADER;