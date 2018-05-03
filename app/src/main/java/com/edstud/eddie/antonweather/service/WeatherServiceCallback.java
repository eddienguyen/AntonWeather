package com.edstud.eddie.antonweather.service;

import com.edstud.eddie.antonweather.data.Channel;

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
