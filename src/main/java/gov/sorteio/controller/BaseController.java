package gov.sorteio.controller;

import gov.sorteio.bo.BaseBO;
import gov.sorteio.entity.BaseEntity;
import gov.sorteio.entity.UsuarioEntity;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static gov.sorteio.constants.Constants.entidades;
import static gov.sorteio.constants.Constants.listEntidades;
import static gov.sorteio.constants.Constants.listModels;
import static gov.sorteio.constants.Constants.pathPai;

public abstract class BaseController<T extends BaseEntity, ID extends Serializable> {
    public final BaseBO<T, ID> baseBO;

    protected BaseController(BaseBO<T, ID> baseBO) {
        this.baseBO = baseBO;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ID> saveObject(@Valid @RequestBody T objectDTO) throws Exception {
        ID id = baseBO.manterObject(objectDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<ID> atualizarObject(@Valid @RequestBody T objectDTO) throws Exception {
        ID id = baseBO.manterObject(objectDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping(path = "/atualizar")
    public String atualizar(@RequestParam("id") ID id, @RequestParam("servico") String servico,
                            Model model) {
        model.addAttribute("lk"+pathPai.get(servico),true);
        model.addAttribute("lk_"+servico,true);
        model.addAttribute("metodo", "PUT");
        T object = baseBO.getObjectEntity(id);
        model.addAttribute(servico, object);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity user = (UsuarioEntity) auth.getPrincipal();
        Class<?> classe = object.getClass();
        Field[] campos = classe.getDeclaredFields();
        for (Field campo : campos) {
            try {
                campo.setAccessible(true);
                Class<?> type = campo.getType();
                if(type.getSimpleName().contains("Entity")) {
                    String entity = type.getSimpleName().replace("Entity","").toLowerCase(Locale.ENGLISH);
                    Object newClass = campo.get(object);
                    Field[] flds = newClass.getClass().getDeclaredFields();
                    for (Field newCampo : flds) {
                        newCampo.setAccessible(true);
                        String field = newCampo.getName();
                        if("id".equals(field)) {
                            Long uuid = (Long) newCampo.get(newClass);
                            model.addAttribute("id_".concat(entity),uuid);
                        }
                    }
                    BaseBO et = (BaseBO) listEntidades.get(entity);
                    model.addAttribute(listModels.get(entity), et.getAll());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return "/modulos/"+servico+"/form_"+servico;
    }

    @GetMapping(path = "/consultar")
    @ResponseBody
    public ResponseEntity<Object> consultarSolicitacaoMuda(@NotNull String servico,
                                                           @NotNull String campo,
                                                           @NotNull String valor, Model model) {
        Map<String, Object> map =new HashMap<>();
        map.put(campo, valor);
        Object dto = baseBO.getListObjectEntity(entidades.get(servico),map, true);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<Void> deletarObject(@RequestParam("id") ID id) throws Exception {
        baseBO.deleteObject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/inativar")
    @ResponseBody
    public ResponseEntity<Void> inativar(@RequestParam("id") @NotNull ID id,
                                              @RequestParam("acao") @NotNull Boolean acao,
                                              HttpSession session) throws Exception {
        baseBO.inativarObject(id, acao);
        session.setAttribute("title_mensagem","Operação de Inativar");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/listar")
    @ResponseBody
    public ResponseEntity<DataTablesOutput<T>> listarRegistros(DataTablesInput input){
        return new ResponseEntity<>(baseBO.listarRegistros(input),HttpStatus.OK);
    }
}
