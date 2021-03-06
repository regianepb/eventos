package br.com.eventos.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public final class Utils { 
            
    private Utils() {        
        throw new UnsupportedOperationException("Classe não pode ser instanciada por ser um utilitário.");
    }
    
    public static LocalDate parseDate(String value) {
        return isEmpty(value) ? null : LocalDate.parse(value);
    }
    
    public static LocalTime parseTime(String value) {
        return isEmpty(value) ? null : LocalTime.parse(value);        
    }    

    public static BigDecimal parseDecimal(String value) {
        return isEmpty(value) ? null : new BigDecimal(value);
    }
    
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty(); 
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static Long parseLong(String value) {
        return isEmpty(value) ? null : Long.parseLong(value);
    }

    public static String convertListToString(List<?> itens) { 
        List<String> itensString = new ArrayList<>();
        for (Object registro : itens) {
            itensString.add(registro.toString());
        }        
        return itensString.toString();
    }

    public static Map<String, String> getParameterMap(HttpServletRequest req) {
        Map<String, String> dados = new HashMap<>();
        for (String key : req.getParameterMap().keySet()) {
            dados.put(key, req.getParameter(key));
        }
        return dados;        
    }

    public static boolean isNull(Object... values) {
        for (Object value : values) {
            if (value == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotNull(Object... values) {
        return !isNull(values);
    }
    
    public static String nullString(Object value) {
        return isNull(value) ? "" : value.toString();
    }

}
