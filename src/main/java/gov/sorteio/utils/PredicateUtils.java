package gov.sorteio.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class PredicateUtils {
    private static final String ativo = "ativo";
    private static final String inativo = "inativo";
    public static Predicate getPredicatesObject(Object object, Root<?> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        Map<String, Object> objectMap = ConvertUtils.convertObjectToMap(object);
        List<String> removerCampos = Arrays.asList("criado","atualizado");
        objectMap.entrySet().forEach(campo -> {
            if(Objects.nonNull(campo.getValue()) && !removerCampos.contains(campo.getKey())) {
                Class type = campo.getValue().getClass();
                if(type.getSimpleName().equals("String")) {
                    predicates.add(cb.like(cb.lower(root.get(campo.getKey())), "%" + campo.getValue().toString().toLowerCase() + "%"));
                } else {
                    predicates.add(cb.equal(root.get(campo.getKey()), campo.getValue()));
                }
            }
        });
        predicates.add(cb.equal(root.get("ativo"), "Ativo"));
        return cq.where(cb.and(predicates.toArray(new Predicate[0]))).distinct(true).getRestriction();
    }

    public static Predicate getPredicate(String campo, Object valor, Root<?> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        String type = valor.getClass().getSimpleName();
        Predicate predicate;
        if("String".equals(type)) {
            predicate = cb.like(cb.lower(root.get(campo)), "%" + valor.toString().toLowerCase() + "%");
        }else{
            predicate = cb.equal(root.get(campo), valor);
        }
        return cq.where(predicate).distinct(true).getRestriction();
    }
}