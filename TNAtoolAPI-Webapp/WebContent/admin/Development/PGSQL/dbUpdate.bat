set PGPASSWORD=%1

%4 -U %2 -d %3 -a -f C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/Tripstableupdator_pgsql.sql  1> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdOut.txt 2> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdErr.txt
%4 -U %2 -d %3 -a -f C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/Stops_AddGeolocation.sql  1>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdOut.txt 2>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdErr.txt
%4 -U %2 -d %3 -a -f C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/StopsGeoCoder_PGSQL.sql  1>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdOut.txt 2>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdErr.txt
%4 -U %2 -d %3 -a -f C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/Stops-For-Route-query_pgsql.sql  1>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdOut.txt 2>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdErr.txt
%4 -U %2 -d %3 -a -f C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/servedStopsSelector-2.sql  1>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdOut.txt 2>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdErr.txt
%4 -U %2 -d %3 -a -f C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/Counties_trip_pgsql.sql  1>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdOut.txt 2>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdErr.txt
%4 -U %2 -d %3 -a -f C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/Tracts_trip_pgsql.sql  1>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdOut.txt 2>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdErr.txt
%4 -U %2 -d %3 -a -f C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/Urbans_trip_pgsql.sql  1>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdOut.txt 2>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdErr.txt
%4 -U %2 -d %3 -a -f C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/Places_trip_pgsql.sql  1>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdOut.txt 2>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdErr.txt
%4 -U %2 -d %3 -a -f C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/Congdists_trip_pgsql.sql  1>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdOut.txt 2>> C:/Users/Administrator/git/TNAsoftware/TNAtoolAPI-Webapp/WebContent/admin/Development/PGSQL/cmdErr.txt