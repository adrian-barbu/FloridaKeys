package com.floridakeys.network;

/**
 * @description Network Configuration
 *
 * @author      Adrian
 */
public class netConfig {
    // Network Timeout
    public final static int HTTP_REQUEST_TIMEOUT = 25 * 1000;
    public final static int HTTP_RESPONSE_TIMEOUT = 25 * 1000;

    // Web Server Configuration
    //public final static String SERVER_URL = "http://192.168.0.150"; //http://192.168.0.150";
    public final static String SERVER_URL = "http://monsterwc.com"; //http://192.168.0.150";
    public final static String SERVER_PORT = "80";

    // Web Service Url
    //public final static String SERVICE_ROOT = SERVER_URL + ":" + SERVER_PORT + "/venue/api" ;
    public final static String SERVICE_ROOT = SERVER_URL + ":" + SERVER_PORT + "/floridakeys/api" ;

    public final static String SERVICE_EVENTS = SERVICE_ROOT + "/event";  // Events
    public final static String SERVICE_NEARBY = SERVICE_ROOT + "/nearby";  // NearBy
    public final static String SERVICE_VENUES = SERVICE_ROOT + "/venue";  // Venues
    public final static String SERVICE_ARTISTS = SERVICE_ROOT + "/artist";  // Artists

    public final static String SERVICE_NEARBY_VENUES = SERVICE_ROOT + "/nearby/venue";  // NearBy Venues
    public final static String SERVICE_NEARBY_EVENTS = SERVICE_ROOT + "/nearby/event";  // NearBy Events

    public final static String SERVICE_ARTIST_INFORMATION = SERVICE_ROOT + "/artist/information";  // Venues
    public final static String SERVICE_ARTIST_MUSICS = SERVICE_ROOT + "/artist/musics";  // Venues
    public final static String SERVICE_ARTIST_BIOGRAPHY = SERVICE_ROOT + "/artist/biography";  // Venues
    public final static String SERVICE_ARTIST_EVENTS = SERVICE_ROOT + "/artist/events";  // Venues

    public final static String SERVICE_VENUE_INFORMATION = SERVICE_ROOT + "/venue/information";  // Venues
    public final static String SERVICE_VENUE_REVIEWS = SERVICE_ROOT + "/venue/reviews";  // Venues
    public final static String SERVICE_VENUE_MENUS = SERVICE_ROOT + "/venue/menus";  // Venues
    public final static String SERVICE_VENUE_EVENTS = SERVICE_ROOT + "/venue/events";  // Venues
    public final static String SERVICE_VENUE_SPECIALS = SERVICE_ROOT + "/venue/speicials";  // Venues

    public final static String SERVICE_EVENT_DETAILS = SERVICE_ROOT + "/event/details";  // Venues

    // Search
    public final static String SERVICE_SEARCH_BY_LOCATION = SERVICE_ROOT + "/search/location";  // Venues
    public final static String SERVICE_SEARCH_BY_FILTER = SERVICE_ROOT + "/search/filter";  // Venues

    // Login
    public final static String SERVICE_LOGIN_BY_EMAIL = SERVICE_ROOT + "/login/email";  // Venues
    public final static String SERVICE_LOGIN_BY_FACEBOOK = SERVICE_ROOT + "/login/facebook";  // Venues
    public final static String SERVICE_SIGNUP_BY_EMAIL = SERVICE_ROOT + "/signup/email";  // Venues

    // Filtering for autocomplete
    public final static String SERVICE_GET_KEYWORD_VENUES = SERVICE_ROOT + "/venue/keyword";  // Get Venues Suggestion Keyword
    public final static String SERVICE_GET_KEYWORD_ARTIST = SERVICE_ROOT + "/artist/keyword";  // Get Artist Suggestion Keyword
    public final static String SERVICE_GET_KEYWORD_EVENT = SERVICE_ROOT + "/event/keyword";  // Get Event Suggestion Keyword
}