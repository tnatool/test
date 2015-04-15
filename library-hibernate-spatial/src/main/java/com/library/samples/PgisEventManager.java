package com.library.samples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

import org.onebusaway.gtfs.impl.Databases;

import com.library.model.*;

public class PgisEventManager {
	//public static Connection connection;
	
	private static Connection makeConnection(int dbindex){
		String url = "";
		switch (dbindex){
		case 0:
			url = "gtfsdb";
			break;
		case 1:
			url = "gtfsdb1";
			break;
		}
		Connection response = null;
		try {
		Class.forName("org.postgresql.Driver");
		response = DriverManager
           .getConnection(Databases.connectionURLs[dbindex],
           Databases.usernames[dbindex], Databases.passwords[dbindex]);
		}catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }
		return response;
	}
	
	private static void dropConnection(Connection connection){
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	public static void main( String args[] )
    {
      Connection c = null;
      Statement stmt = null;
      try {
      Class.forName("org.postgresql.Driver");
        c = DriverManager
           .getConnection("jdbc:postgresql://localhost:5432/gtfsdb",
           "postgres", "123123");
        c.setAutoCommit(false);
        System.out.println("Opened database successfully");

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
        while ( rs.next() ) {
           int id = rs.getInt("id");
           String  name = rs.getString("name");
           int age  = rs.getInt("age");
           String  address = rs.getString("address");
           float salary = rs.getFloat("salary");
           System.out.println( "ID = " + id );
           System.out.println( "NAME = " + name );
           System.out.println( "AGE = " + age );
           System.out.println( "ADDRESS = " + address );
           System.out.println( "SALARY = " + salary );
           System.out.println();
        }
        rs.close();
        stmt.close();
        c.close();
      } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        System.exit(0);
      }
      System.out.println("Operation done successfully");
    } */
	public static long UrbanCensusbyPop(int pop, int dbindex) 
    {	
		Connection connection = makeConnection(dbindex);
      //Connection c = null;
      Statement stmt = null;
      long population = 0;
      try {
      /*Class.forName("org.postgresql.Driver");
        c = DriverManager
           .getConnection("jdbc:postgresql://localhost:5432/gtfsdb",
           "postgres", "123123");
        c.setAutoCommit(false);
        System.out.println("Opened database successfully");*/

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery( "select sum(population) as pop from "
        		+ "(select distinct block.blockid, population from census_blocks block join gtfs_stops stop on st_dwithin(block.location, stop.location, 161)=true "
        		+ "where stop.urbanid in (select urbanid from census_urbans where population>=50000) and block.urbanid in (select urbanid from census_urbans where population>=50000))"
        		+ "as pops;" );        
        while ( rs.next() ) {
           population = rs.getLong("pop");           
           //System.out.println( "population = " + population );           
        }
        rs.close();
        stmt.close();
        //c.close();
      } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        System.exit(0);
      }
      dropConnection(connection);
      //System.out.println("Operation done successfully");
      return population;
    }
	/**
	 *Queries agency clusters (connected transit networks) and returns a list of all transit agencies with their connected agencies
	 */
	public static List<agencyCluster> agencyCluster(double dist, int dbindex){
		List<agencyCluster> response = new ArrayList<agencyCluster>();
		Connection connection = makeConnection(dbindex);
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery( "select agency.name as name, aid as aid, size as size, aids as aids, names as names, min_gaps as min_gaps from gtfs_agencies agency inner join "
					+ "(select aid1 as aid, count(aid2) as size, array_agg(aid2) as aids, array_agg(name) as names, array_agg(dist) as min_gaps from "
					+ "(select stop1.agencyid as aid1, stop2.name as name, stop2.agencyid as aid2, min(st_distance(stop1.location,stop2.location)) as dist from "
					+"(select map.agencyid as agencyid, stop.location as location, stop.id as id from gtfs_stops stop inner join gtfs_stop_service_map map on "
					+ "map.agencyid_def=stop.agencyid and map.stopid=stop.id) as stop1 inner join "
					+ "(select agency.name as name, stp.agencyid as agencyid, stp.location as location, stp.id as id from gtfs_agencies agency inner join "
					+ "(select map.agencyid as agencyid, stop.location as location, stop.id as id from gtfs_stops stop inner join gtfs_stop_service_map map on"
					+ " map.agencyid_def=stop.agencyid and map.stopid=stop.id) as stp on stp.agencyid=agency.id) as stop2 on st_dwithin(stop1.location, stop2.location, "
					+ String.valueOf(dist)
					+ ")=true where stop1.agencyid!=stop2.agencyid group by aid1, aid2, stop2.name) as pairs group by aid1) as clusters on agency.id=clusters.aid order by size desc" );
			while ( rs.next() ) {
				agencyCluster instance = new agencyCluster();
				instance.agencyId = rs.getString("aid");
				instance.agencyName = rs.getString("name");
				instance.clusterSize = rs.getLong("size");
				String[] agencyIds = (String[]) rs.getArray("aids").getArray();
				instance.agencyIds= Arrays.asList(agencyIds);
				String[] agencyNames = (String[]) rs.getArray("names").getArray();
				instance.agencyNames= Arrays.asList(agencyNames);
				Double[] gaps = (Double[]) rs.getArray("min_gaps").getArray();
				instance.minGaps= Arrays.asList(gaps);
		        response.add(instance);
		        }
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }
		dropConnection(connection);
		return response;
	}
	/**
	 *Queries connected transit agencies and list of connections for a given transit agency
	 */
	public static List<agencyCluster> agencyClusterDetails(double dist, String agencyId, int dbindex){
		List<agencyCluster> response = new ArrayList<agencyCluster>();
		Connection connection = makeConnection(dbindex);
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery( "select name as name, aid2 as aid, count(aid2)as size, min(dist) as mingap, max(dist) as maxgap, round(avg(dist),2) as meangap, "
					+ "array_agg(sname1||'-'||sname2||':'||dist::text) as connection from "
					+ "(select stop2.name as name, stop2.agencyid as aid2, stop1.sname as sname1,stop2.sname as sname2, "
					+ "round((3.28084*st_distance(stop1.location,stop2.location))::numeric, 2) as dist from "
					+ "(select map.agencyid as agencyid, stop.location as location, stop.id as id, stop.name as sname from gtfs_stops stop inner join gtfs_stop_service_map map "
					+ "on map.agencyid_def=stop.agencyid and map.stopid=stop.id) as stop1 inner join "
					+ "(select agency.name as name, stp.name as sname, stp.agencyid as agencyid, stp.location as location, stp.id as id from gtfs_agencies agency inner join "
					+ "(select map.agencyid as agencyid, stop.location as location, stop.id as id, stop.name as name from gtfs_stops stop inner join gtfs_stop_service_map map "
					+ "on map.agencyid_def=stop.agencyid and map.stopid=stop.id) as stp on stp.agencyid=agency.id) as stop2 on st_dwithin(stop1.location, stop2.location,"
					+ String.valueOf(dist)
					+ ")=true where stop1.agencyid!=stop2.agencyid and stop1.agencyid='"
					+ agencyId
					+ "' order by stop2.agencyid,dist, stop2.sname) as recs group by name, aid2 order by aid2");
			while ( rs.next() ) {
				agencyCluster instance = new agencyCluster();
				instance.agencyId = rs.getString("aid");
				instance.agencyName = rs.getString("name");
				instance.clusterSize = rs.getLong("size");
				instance.minGap = rs.getFloat("mingap");
				instance.maxGap = rs.getFloat("maxgap");
				instance.meanGap = rs.getFloat("meangap");
				String[] connections = (String[]) rs.getArray("connection").getArray();
				instance.connections= Arrays.asList(connections);				
		        response.add(instance);
		        }
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }
		dropConnection(connection);
		return response;
	}
	
	/**
	 *returns service hours in seconds for the whole state for a single day in YYYYMMDD format and day of week in all lower case
	 */
	public static long ServiceHours(String date, String day, int dbindex){
		long response = 0;
		Connection connection = makeConnection(dbindex);
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery( "select sum(tlength) as svchours from gtfs_trips where serviceid_agencyid||serviceid_id in ("+
					"select  serviceid_agencyid||serviceid_id from gtfs_calendars where startdate::int<="
					+ date + " and enddate::int>=" + date + " and "	+ day + " = 1 "+
					"and serviceid_agencyid||serviceid_id not in (select serviceid_agencyid||serviceid_id from gtfs_calendar_dates where date='"
					+ date + "' and exceptiontype=2)"+ " union	select serviceid_agencyid||serviceid_id from gtfs_calendar_dates where date='"
					+ date + "' and exceptiontype=1)");	
			while ( rs.next() ) {
				response = rs.getLong("svchours");
			}
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }
		dropConnection(connection);
		return response;
	}
	
	/**
	 *returns service miles in miles for the whole state for a single day in YYYYMMDD format and day of week in all lower case
	 */
	public static double ServiceMiles(String date, String day, int dbindex){
		double response = 0;
		Connection connection = makeConnection(dbindex);
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery( "select sum(length+estlength) as svcmiles from gtfs_trips where serviceid_agencyid||serviceid_id in ("+
			"select  serviceid_agencyid||serviceid_id from gtfs_calendars where startdate::int<="
			+ date + " and enddate::int>=" + date + " and "	+ day + " = 1"+ 
			"and serviceid_agencyid||serviceid_id not in (select serviceid_agencyid||serviceid_id from gtfs_calendar_dates where date='"
			+ date + "' and exceptiontype=2)"+ "union select serviceid_agencyid||serviceid_id from gtfs_calendar_dates where date='"
			+ date + "' and exceptiontype=1)");	
			while ( rs.next() ) {
				response = rs.getDouble("svcmiles");
			}
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }
		dropConnection(connection);
		return response;
	}
	
	/**
	 *returns sum of population within x distance of all stops 
	 */
	public static long PopWithinX(double x, int dbindex){
		long response = 0;
		Connection connection = makeConnection(dbindex);
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery( "select sum(population) as pop from (select population from census_blocks block inner join gtfs_stops stop "
					+ "on st_dwithin(block.location,stop.location, "+ String.valueOf(x) + ")=true group by block.blockid) as pop");	
			while ( rs.next() ) {
				response = rs.getLong("pop");
			}
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }
		dropConnection(connection);
		return response;
	}
	
	/**
	 *returns sum of population served at specified level of service
	 */
	public static long PopServedatLOS(double x, String date, String day, int l, int dbindex){
		long response = 0;
		Statement stmt = null;
		Connection connection = makeConnection(dbindex);
		try{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery( "select sum(population) as pop from (select population from census_blocks block inner join "
					+ "(select stop.location as location, stimes.stop_agencyid as aid, stimes.stop_id as sid, count(stimes.stop_id) as frequency from "
					+ "gtfs_stops stop inner join gtfs_stop_times stimes on stop.id = stimes.stop_id and stop.agencyid = stimes.stop_agencyid inner join "
					+ "(select agencyid, id from gtfs_trips where serviceid_agencyid||serviceid_id in "
					+ "(select  serviceid_agencyid||serviceid_id from gtfs_calendars where startdate::int<="
					+ date + " and enddate::int>=" + date + " and "	+ day + " = 1 "
					+ "and serviceid_agencyid||serviceid_id not in (select serviceid_agencyid||serviceid_id from gtfs_calendar_dates where date='"
					+ date + "' and exceptiontype=2)union select serviceid_agencyid||serviceid_id from gtfs_calendar_dates where date='" + date
					+ "' and exceptiontype=1))as trips "
					+ "on trips.agencyid = stimes.trip_agencyid and trips.id = stimes.trip_id group by stop_agencyid, stop_id, stop.location having count(stimes.stop_id)>="
					+ l	+ ") as svstop on st_dwithin(svstop.location, block.location, " + String.valueOf(x)+ ")=true group by blockid) as pops");	
			while ( rs.next() ) {
				response = rs.getLong("pop");
			}
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }
		dropConnection(connection);
		return response;
	}
	
	/**
	 *returns population served by service and service stops. Keys are svcstops and svcpop
	 */
	public static HashMap<String, Long> ServiceStopsPop(double x, String date, String day, int dbindex){
		HashMap<String, Long> response = new HashMap<String, Long>();
		Statement stmt = null;
		Connection connection = makeConnection(dbindex);
		try{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery( "select sum(frequency) as svcstops, sum(svcpop) as svcpop from (select frequency, frequency*sum(population) as svcpop from "
					+ "census_blocks block inner join (select stop.location as location, stimes.stop_agencyid as aid, stimes.stop_id as sid, count(stimes.stop_id) as frequency "
					+ "from gtfs_stops stop inner join gtfs_stop_times stimes on stop.id = stimes.stop_id and stop.agencyid = stimes.stop_agencyid inner join "
					+ "(select agencyid, id from gtfs_trips where serviceid_agencyid||serviceid_id in (select  serviceid_agencyid||serviceid_id from gtfs_calendars where "
					+ "startdate::int<="+ date + " and enddate::int>=" + date + " and "	+ day+ " = 1 and serviceid_agencyid||serviceid_id not in "
					+ "(select serviceid_agencyid||serviceid_id from gtfs_calendar_dates where date='" + date + "' and exceptiontype=2) union select "
					+ "serviceid_agencyid||serviceid_id from gtfs_calendar_dates where date='" + date + "' and exceptiontype=1))as trips on trips.agencyid = stimes.trip_agencyid "
					+ "and trips.id = stimes.trip_id group by stop_agencyid, stop_id, stop.location) as svstop on st_dwithin(svstop.location, block.location, "
					+ String.valueOf(x)	+ ")=true group by "+ "svstop.aid, svstop.sid, frequency) as result");	
			while ( rs.next() ) {
				response.put("svcstops", (long)rs.getInt("svcstops")); 
				response.put("svcpop", rs.getLong("svcpop")); 
			}
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }
		dropConnection(connection);
		return response;
	}
	
	/**
	 *returns min and max Hours of service in int time (epoch) fromat for a given date and day of week in an integer array
	 */
	public static int[] HoursofService(String date, String day, int dbindex){
		int[] response = new int[2];
		Statement stmt = null;
		Connection connection = makeConnection(dbindex);
		try{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery( "select min(arrivaltime) as start, max(departuretime) as finish from gtfs_stop_times stimes inner join "
					+ "(select agencyid, id from gtfs_trips where serviceid_agencyid||serviceid_id in "
					+ "(select  serviceid_agencyid||serviceid_id from gtfs_calendars where startdate::int<=" + date	+ " and enddate::int>="	+ date + " and " + day + " = 1 "
					+ "and serviceid_agencyid||serviceid_id not in (select serviceid_agencyid||serviceid_id from gtfs_calendar_dates where date='" + date
					+ "' and exceptiontype=2) union select serviceid_agencyid||serviceid_id from gtfs_calendar_dates where date='" + date + "' and exceptiontype=1))as trips "
					+ "on trips.agencyid = stimes.trip_agencyid and trips.id = stimes.trip_id where arrivaltime>=0 and departuretime>=0");	
			while ( rs.next() ) {
				response[0] = rs.getInt("start"); 
				response[1] = rs.getInt("finish"); 
			}
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }
		dropConnection(connection);
		return response;
	}
	/**
	 *Queries stop clusters (connected transit networks) and returns a list of all transit agencies with their connected agencies
	 */
	public static TreeSet<StopCluster> stopClusters(String[] dates, String[] days, double dist, int dbindex){	
		HashMap<String, Integer> serviceMap = stopFrequency(dates, days, dbindex);
		TreeSet<StopCluster> response = new ClusterPriorityQueue();
		Connection connection = makeConnection(dbindex);
		String mainquery = "select cluster.cid as cid, cluster.sid as sid , cluster.aid as aid, cluster.name as name, map.agencies as agencies, map.routes as routes from "
				+ "(select stp1.agencyid||stp1.id as cid, stp2.id as sid, stp2.name as name, stp2.agencyid as aid from gtfs_stops stp1 inner join gtfs_stops stp2 "
				+ "on st_dwithin(stp1.location, stp2.location, ?)) as cluster inner join (select agencyid_def as aid, array_agg(distinct agencyid) as agencies, stopid as sid, "
				+ "array_agg(distinct routeid) as routes from gtfs_stop_route_map group by agencyid_def, stopid ) as map on map.sid = cluster.sid and map.aid = cluster.aid "
				+ "order by cluster.cid, cluster.sid";
		
		try{
			//System.out.println("Starting Query");
			PreparedStatement stmt = connection.prepareStatement(mainquery);
			stmt.setDouble(1, dist);
			ResultSet rs = stmt.executeQuery();
			//System.out.println("Query: "+stmt);
			String cid = "";
			int count = 0;
			StopCluster inst = new StopCluster();
			//System.out.println("Number of records in results: "+rs.getFetchSize());
			//System.out.println("Query Done, collecting results");
			while (rs.next()) {
				count++;
				ClusteredStop instance = new ClusteredStop();
				String clid = rs.getString("cid");	
				instance.agencyId = rs.getString("aid");				
				instance.id = rs.getString("sid");				
				instance.name = rs.getString("name");
				instance.visits = serviceMap.get(instance.agencyId+instance.id);
				String[] agencies = (String[]) rs.getArray("agencies").getArray();
				instance.agencies= Arrays.asList(agencies);
				String[] routes = (String[]) rs.getArray("routes").getArray();
				instance.routes= Arrays.asList(routes);								
				if (count ==1){
					cid =clid;
					inst.setClid(clid);
				}
				if (cid.equals(clid)){
					inst.addStop(instance);
				} else {
					cid=clid;					
					inst.syncParams();
					response.add(inst);					
					inst = new StopCluster();
					inst.setClid(clid);
					inst.addStop(instance);					
					}
		        }
			rs.close();
			stmt.close();
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }		
		dropConnection(connection);
		//System.out.println("Processing Clusters");
		return response;
	}
	/**
	 *Queries frequency of service for all stops in the database for a set of dates and days
	 */
	public static HashMap<String, Integer> stopFrequency(String[] date, String[] day, int dbindex){				
		HashMap<String, Integer> response = new HashMap<String, Integer>();
		Connection connection = makeConnection(dbindex);
		String[] mainquery = new String[date.length];
		String id = "";
		for (int i=0; i<date.length; i++){
			mainquery[i]= "select aid_def||stopid as id, svc from (select stimes.stop_id as stopid, stimes.stop_agencyid as aid_def, sum(service) as svc from "
					+ "(select trip.agencyid as aid, trip.id as tripid, count(svc.sid) as service from gtfs_trips trip left join "
					+ "(select  serviceid_agencyid as aid, serviceid_id as sid from gtfs_calendars where startdate::int<="+date[i]+" and enddate::int>="+date[i]+" and "+day[i]+" = 1 "
					+ "and serviceid_agencyid||serviceid_id not in (select serviceid_agencyid||serviceid_id from gtfs_calendar_dates where date='"+date[i]+"' and exceptiontype=2) "
					+ "union select serviceid_agencyid, serviceid_id from gtfs_calendar_dates where date='"+date[i]+"' and exceptiontype=1)as svc on "
					+ "trip.serviceid_id = svc.sid and trip.serviceid_agencyid= svc.aid group by trip.agencyid, trip.id ) as svcmap inner join gtfs_stop_times stimes on "
					+ "svcmap.aid = stimes.trip_agencyid and svcmap.tripid = stimes.trip_id group by aid_def, stopid) as stopids inner join gtfs_stops stop on "
					+ "stop.agencyid = stopids.aid_def and stop.id= stopids.stopid";
			try{
				PreparedStatement stmt = connection.prepareStatement(mainquery[i]);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//count++;
					if (i==0){
						response.put(rs.getString("id"), rs.getInt("svc"));
					} else {
						id = rs.getString("id");
						response.put(id, response.get(id)+rs.getInt("svc"));
					}					
			        }
				rs.close();
				stmt.close();
			} catch ( Exception e ) {
		        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
		        System.exit(0);
		      }
		}				
		dropConnection(connection);
		return response;
	}
}
