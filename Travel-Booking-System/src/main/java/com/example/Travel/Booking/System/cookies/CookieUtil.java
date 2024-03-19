package com.example.Travel.Booking.System.cookies;

import com.example.Travel.Booking.System.entities.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String getValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void delete(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void addCookieForUser(HttpServletResponse response, User user) {
        CookieUtil.addCookie(response, "email", user.getEmail(), 3600);
        CookieUtil.addCookie(response, "firstName", user.getFirstName(), 3600);
        CookieUtil.addCookie(response, "lastName", user.getLastName(), 3600);
    }

    public static void addCookieForAdmin(HttpServletResponse response, String adminName) {
        CookieUtil.addCookie(response, "email", adminName, 3600);
    }

    public static void clearCookies(HttpServletResponse response) {
        CookieUtil.delete(response, "email");
        CookieUtil.delete(response, "firstName");
        CookieUtil.delete(response, "lastName");
    }
}
