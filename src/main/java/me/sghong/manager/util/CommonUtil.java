package me.sghong.manager.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import me.sghong.manager.app.common.dto.MessageDto;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Slf4j
@Component
public class CommonUtil {
    public static String getTodayOfWeek() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        return today.getYear() + "년 " + today.getMonthValue() + "월 " + today.getDayOfMonth() + "일 " + dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREA) + "요일";
    }

    public static String getNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public static String getRemoteIP() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null)
            ip = request.getHeader("X-Forwarded-For");

        if (ip == null)
            ip = request.getRemoteAddr();

        return ip;
    }

    public static void AlertMessage(String alert, String location) throws IOException {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        assert response != null;

        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();
        String strScript;

        if (location.equals("ajax")) {
            strScript = "FAIL|||||" + alert;
        } else {
            strScript = "<script>";

            if (!alert.isEmpty()) {
                strScript += "alert('" + alert + "');";
            }

            if (!location.isEmpty()) {
                strScript += location;
            }

            strScript += "</script>";
        }

        out.println(strScript);
        out.flush();
    }

    public static void setCookie(String name, String value, int MaxAge) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        assert response != null;

        AES256 aes256 = new AES256();
        try {
            value = aes256.encrypt(value);
        } catch (Exception ex) {
            value = "";
        }
        Cookie cookie = new Cookie(name, value);
        if (MaxAge > 0) {
            cookie.setMaxAge(MaxAge);
        }
        response.addCookie(cookie);
    }

    public static String getCookie(String name) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String strVal = "";
        String cookiename;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie key : cookies) {
                cookiename = key.getName();
                if (cookiename.equals(name)) {
                    strVal = key.getValue();
                    break;
                }
            }
        }

        AES256 aes256 = new AES256();
        try {
            strVal = aes256.decrypt(strVal);
        } catch (Exception ex) {
            strVal = "";
        }

        return strVal;
    }

    public static void delCookie(String name){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        assert response != null;

        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static String getSession(String sessionName) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String sessionValue = (String) request.getSession().getAttribute(sessionName);
        String retVal = "";

        try {
            if (sessionValue != null && !sessionValue.isEmpty()) {
                AES256 aes256 = new AES256();
                retVal = aes256.decrypt(sessionValue);
            }
        } catch (Exception e) {
            log.error("Exception [Err_Msg] : {}", e.getStackTrace()[0]);
            throw new RuntimeException(e);
        }

        return retVal;
    }

    public static void setSession(String sessionName, String sessionValue) {
        try {
            if (sessionValue != null && !sessionValue.isEmpty()) {
                AES256 aes256 = new AES256();
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                HttpSession session = request.getSession();
                session.setAttribute(sessionName, aes256.encrypt(sessionValue));
            }
        } catch (Exception e) {
            log.error("Exception [Err_Msg] : {}", e.getStackTrace()[0]);
            throw new RuntimeException(e);
        }
    }

    public static String ArrayToStringForComma(String[] array) {
        String result = "";

        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();

            for (String s : array) {
                sb.append(s).append(",");
            }

            result = sb.deleteCharAt(sb.length() - 1).toString();
        }

        return result;
    }

    public static String Left(String str, int len) {
        if (str.length() < len) {
            len = str.length();
        }
        return str.substring(0, len);
    }

    public static String Right(String str, int len) {
        if (str.length() < len) {
            len = str.length();
        }
        return str.substring(str.length() - len);
    }

    public static String MaketoZero(String str, int len) {
        StringBuilder sb = new StringBuilder(str);
        for (int i=0; i<len+1; i++) {
            sb.insert(0, "0");
        }
        str = Right(sb.toString(), len);
        return str;
    }

    public static String ArrarytoStringForComma(String[] array) {
        String result = "";

        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();

            for (String s : array) {
                sb.append(s).append(",");
            }

            result = sb.deleteCharAt(sb.length() - 1).toString();
        }

        return result;
    }

    public static String getProductClassName(String type) {
        switch (type) {
            case "P" -> {
                return "단일상품";
            }
            case "S" -> {
                return "세트상품";
            }
            case "G" -> {
                return "묶음상품";
            }
            case "N" -> {
                return "비매품";
            }
            default -> {
                return "";
            }
        }
    }
}
