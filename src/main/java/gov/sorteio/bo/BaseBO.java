package gov.sorteio.bo;

import gov.sorteio.entity.BaseEntity;
import gov.sorteio.entity.UsuarioEntity;
import gov.sorteio.enums.ErrorCodesEnum;
import gov.sorteio.exceptions.BusinessException;
import gov.sorteio.exceptions.GenericException;
import gov.sorteio.exceptions.InvalidRequestException;
import gov.sorteio.repository.BaseRepository;
import gov.sorteio.utils.PredicateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static gov.sorteio.constants.Constants.MSG_GENERIC;
import static gov.sorteio.constants.Constants.MSG_OBJ_DUPLICADO;
import static gov.sorteio.constants.Constants.TITLE_GENERIC;
import static gov.sorteio.constants.Constants.ZONE_ID;
import static gov.sorteio.utils.ConvertUtils.convertObjectToMap;

public class BaseBO<T extends BaseEntity, ID extends Serializable> {
    private final BaseRepository<T, ID> baseRepository;
    public final ModelMapper mapper;

    @Autowired
    public BaseBO(BaseRepository<T, ID> baseRepository) {
        this.baseRepository = baseRepository;
        this.mapper = new ModelMapper();
    }

    public ID manterObject(T object) throws Exception {
        try{
            ID id = getID(object);
            if(Objects.isNull(id)){
                Map<String, Object> newObject = convertObjectToMap(object);
                String hash = gerarHash(newObject.toString());
                validarHash(hash);
                object.setHash(hash);
                carregarRelacao(object);
                return getID(persistir(object, Boolean.TRUE));
            }else {
                T newEntity = validarUpdate(object, id);
                carregarRelacao(newEntity);
                persistir(newEntity, Boolean.FALSE);
                return id;
            }
        }catch (Exception e){
            throw getException(e);
        }
    }

    public ID manterSemAutenticacao(T object) throws Exception {
        try{
            ID id = getID(object);
            if(Objects.isNull(id)){
                Map<String, Object> newObject = convertObjectToMap(object);
                String hash = gerarHash(newObject.toString());
                validarHash(hash);
                object.setHash(hash);
                carregarRelacao(object);
                return getID(persistirSemAutenticacao(object, Boolean.TRUE));
            }else {
                T newEntity = validarUpdate(object, id);
                carregarRelacao(newEntity);
                persistirSemAutenticacao(newEntity, Boolean.FALSE);
                return id;
            }
        }catch (Exception e){
            throw getException(e);
        }
    }

    private T persistirSemAutenticacao(T entity, Boolean flag) {
        LocalDateTime dataTime = LocalDateTime.now(ZoneId.of(ZONE_ID));
        entity.setIdUserAtualizou(0L);
        entity.setAtualizado(dataTime);
        if(flag){
            entity.setIdUser(0L);
            entity.setCriado(dataTime);
            entity.setAtivo(true);
        }
        return baseRepository.save(entity);
    }

    public List<T> getListObjectEntity(Object object, Object consulta, Boolean flag) {
        List<T> entity = getObjectByField(object,consulta);
        if(!CollectionUtils.isEmpty(entity)){
            return entity;
        }
        if(flag){
            return entity;
        }
        throw InvalidRequestException.builder()
                .code("COD-001")
                .title("Consulta não encontrada")
                .detail("Valor consultado não encontrado no Banco de Dados")
                .trace(null)
                .build();
    }

    public void deleteObject(ID id) throws Exception {
        try{
            existObject(id);
            baseRepository.deleteById(id);
        }catch (Exception e){
            getException(e);
        }
    }

    public void inativarObject(ID id, Boolean status) throws Exception {
        try{
            existObject(id);
            baseRepository.inativarById(id, status);
        }catch (Exception e){
            getException(e);
        }
    }

    private void existObject(ID id) {
        Optional<T> entity = baseRepository.findById(id);
        if (entity.isEmpty()) {
            throw InvalidRequestException.builder()
                    .code(ErrorCodesEnum.COD_PERSISTIR.getCodigo())
                    .title(TITLE_GENERIC)
                    .detail("Objeto não encontrado para efetuar a operação.")
                    .trace(null)
                    .build();
        }
    }

    public DataTablesOutput<T> listarRegistros(DataTablesInput input) {
        Specification<T> specification = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity user = (UsuarioEntity) auth.getPrincipal();
        if(!"ROLE_ADMINISTRADOR".equals(user.getTipo())){
            if (Objects.isNull(specification)) {
                specification = (root, query, cb) -> cb.and(cb.equal(root.get("idUser"), user.getId()));
            }else{
                specification.and((root, query, cb) -> cb.and(cb.equal(root.get("idUser"), user.getId())));
            }

        }
        return baseRepository.findAll(input, specification);
    }

    public DataTablesOutput<T> listarRegistrosSemAutenticacao(DataTablesInput input) {
        return baseRepository.findAll(input);
    }

    private List<T> getObjectByField(Object object, Object consulta) {
        Specification<T> specification = null;
        if(Objects.nonNull(consulta)) {
            mapper.map(consulta, object);
            specification = (root, query, cb) -> PredicateUtils.getPredicatesObject(object, root, query, cb);
        }
        return baseRepository.findAll(specification);
    }

    public List<T> getAll() {
//        Specification<T> specification = (root, query, cb) -> cb.equal(root.get("ativo"), "Ativo");
//        return baseRepository.findAll(specification);
        return new ArrayList<>();
    }

    private T persistir(T entity, Boolean flag) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity user = (UsuarioEntity) auth.getPrincipal();
        LocalDateTime dataTime = LocalDateTime.now(ZoneId.of(ZONE_ID));
        entity.setIdUserAtualizou(user.getId());
        entity.setAtualizado(dataTime);
        if(flag){
            entity.setIdUser(user.getId());
            entity.setCriado(dataTime);
            entity.setAtivo(true);
        }
        return baseRepository.save(entity);
    }

    private ID getID(T object){
        Class<?> classe = object.getClass();
        Field[] campos = classe.getDeclaredFields();
        for (Field campo : campos) {
            try {
                campo.setAccessible(true);
                String field = campo.getName();
                Object valor = campo.get(object);
                if("id".equals(field)) {
                    return (ID) valor;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public Exception getException(Exception e) throws Exception {
        if(e instanceof InvalidRequestException || e instanceof GenericException ||
                e instanceof BusinessException){
            throw e;
        }
        throw e;
    }

    private void validarHash(String hash) {
        Specification<T> specification = (root, query, cb) -> cb.and(cb.equal(root.get("hash"), hash));
        DataTablesInput input = new DataTablesInput();
        DataTablesOutput<T> paginas = baseRepository.findAll(input, specification);
        if(paginas.getData().size() > 0){
            throw new BusinessException(ErrorCodesEnum.COD_GENERICO.getCodigo(), TITLE_GENERIC
                    , MSG_OBJ_DUPLICADO, null, null);
        }
    }

    private void carregarRelacao(T object) {
        Class<?> classe = object.getClass();
        Field[] campos = classe.getDeclaredFields();
        for (Field campo : campos) {
            try {
                campo.setAccessible(true);
                Class<?> type = campo.getType();
                if(type.getSimpleName().contains("Entity")) {
                    String fieldIdRelacao = "id_"+type.getSimpleName().replace("Entity","").toLowerCase(Locale.ENGLISH);
                    Field fieldRelacao = object.getClass().getDeclaredField(fieldIdRelacao);
                    fieldRelacao.setAccessible(true);
                    Long id = (Long) fieldRelacao.get(object);
                    Object newClass = mapper.map(type.getDeclaredFields(),type);
                    Field[] flds = type.getDeclaredFields();
                    for (Field newCampo : flds) {
                        newCampo.setAccessible(true);
                        String field = newCampo.getName();
                        if("id".equals(field)) {
                            newCampo.set(newClass, id);
                        }
                    }
                    campo.set(object,newClass);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public T getObjectEntity(ID id) {
        Optional<T> optional = baseRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }else{
            throw InvalidRequestException.builder()
                    .code(ErrorCodesEnum.COD_PERSISTIR.getCodigo())
                    .title(TITLE_GENERIC)
                    .detail("Objeto não encontrado no Banco de Dados.")
                    .trace(null)
                    .build();
        }
    }

    private T validarUpdate(Object object, ID id){
        T newEntity = getObjectEntity(id);
        Class<?> classe = object.getClass();
        Field[] campos = classe.getDeclaredFields();
        for (Field campo : campos) {
            try {
                campo.setAccessible(true); //Necessário por conta do encapsulamento das variáveis (private)
                String field = campo.getName();
                Column unique = campo.getAnnotation(Column.class);
                if((Objects.nonNull(unique) && unique.unique() && (!"id".equals(field) && !"email".equals(field)))
                        || "senha".equals(field)) {
                    campo.set(object, null);
                }
            } catch (Exception e) {
                throw GenericException.builder()
                        .code(ErrorCodesEnum.COD_GENERICO.getCodigo())
                        .title(TITLE_GENERIC)
                        .detail(MSG_GENERIC)
                        .trace(e)
                        .build();
            }
        }
        Map<String, Object> newObject = convertObjectToMap(object);
        mapper.map(newObject, newEntity);
        return newEntity;
    }

    private String gerarHash(String object) {
        byte[] hash;
        try{
            hash = MessageDigest.getInstance("SHA-256").digest(object.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, hash));
        } catch (NoSuchAlgorithmException e){
            throw InvalidRequestException.builder()
                    .code(ErrorCodesEnum.COD_PERSISTIR.getCodigo())
                    .title(TITLE_GENERIC)
                    .detail("Não foi possível validar o objeto para efetuar operação de salvar.")
                    .trace(null)
                    .build();
        }
    }
}
