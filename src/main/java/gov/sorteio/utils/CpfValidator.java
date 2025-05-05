package gov.sorteio.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String> {
    private final int[] PESO_CPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {

        String cpfSomenteDigitos = cpf.replaceAll("\\D", "");

        if (cpfSomenteDigitos == null || !cpfSomenteDigitos.matches("\\d{11}") || cpfSomenteDigitos.matches("(\\d)\\1{10}")) {
            return false;
        }

        Integer digito1 = calcularDigito(cpfSomenteDigitos.substring(0, 9), PESO_CPF);
        Integer digito2 = calcularDigito(cpfSomenteDigitos.substring(0, 9) + digito1, PESO_CPF);

        return cpfSomenteDigitos.equals(cpfSomenteDigitos.substring(0, 9) + digito1.toString() + digito2.toString());
    }

    private int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }
}
