package gov.sorteio.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@AllArgsConstructor
public class ConvertUtils {
    public static Map<String, Object> convertObjectToMap(Object object1) {
        Class<?> classe = object1.getClass();
        Field[] campos = classe.getDeclaredFields();
        Map<String, Object> object2 = new HashMap<>();
        for (Field campo : campos) {
            try {
                campo.setAccessible(true);
                String field = campo.getName();
                Object valorAtributo = campo.get(object1);
                if(Objects.nonNull(valorAtributo)) {
                    object2.put(field, valorAtributo);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return object2;
    }
}
