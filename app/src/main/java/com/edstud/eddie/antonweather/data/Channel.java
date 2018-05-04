package com.edstud.eddie.antonweather.data;

import org.json.JSONObject;

public class Channel implements JSONPopulator{
    //parse from JSON , object channel
    //at https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json

    private Units units;
    private Item item;
    private Location location;
    private Wind wind;
    private Atmosphere atmosphere;
    private Astronomy astronomy;

    public Units getUnits() {
        return units;
    }

    public Item getItem() {return item;}

    public Location getLocation() {return location;}

    public Wind getWind() {return wind;}

    public Atmosphere getAtmosphere() {return atmosphere;}

    public Astronomy getAstronomy() {return astronomy;}

    @Override
    public void populate(JSONObject data) {

        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

        location = new Location();
        location.populate(data.optJSONObject("location"));

        wind = new Wind();
        wind.populate(data.optJSONObject("wind"));

        atmosphere = new Atmosphere();
        atmosphere.populate(data.optJSONObject("atmosphere"));

        astronomy = new Astronomy();
        astronomy.populate(data.optJSONObject("astronomy"));
    }


}
