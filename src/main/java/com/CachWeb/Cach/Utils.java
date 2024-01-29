package com.CachWeb.Cach;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    @Value("${app.url}")
    private static String appUrl;

    public static String getAppUrl() {

        return appUrl;
    }
}
