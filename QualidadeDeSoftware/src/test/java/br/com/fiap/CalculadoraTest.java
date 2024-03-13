package br.com.fiap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

//classe de teste não precisam ser públicas
class CalculadoraTest {

    private static Calculadora calculadora;

    //testes não tem construtores porém para reaproveitarmos uma variavel podemos
    //criar um método que antes de todos os outros faça algo (no caso instancie a variável)
    //para o BeforeAll o método deve ser static
    //temos também o BeforeEach que faz com que o método seja executado
    //antes de TODOS os testes e não precisa ser static
    @BeforeAll
    static void setup() {
        calculadora = new Calculadora();
    }

    //indicando que é um teste
    @Test
    void deveSomar() {
        //com tdd que é uma metodologia onde pensamos primeiro nos testes
        //devemos pensar em todos casos de uso
        //com isso ao irmos criando os testes (sem implementação)
        //jogamos um fail
        //fail("teste não implementado");
        Assertions.assertEquals(6, calculadora.somar(4, 2));
    }

    @Test
    void deveSubtrair() {
        //fail("teste não implementado");
        Assertions.assertEquals(2, calculadora.subtrair(4, 2));
    }

    @Test
    void deveMultiplicar() {
        //fail("teste não implementado");
        Assertions.assertEquals(8, calculadora.multiplicar(4, 2));
    }

    @Test
    void deveDividir() {
        //exemplo com assertJ (outros exemplos em junit)
        assertThat(calculadora.dividir(4, 2)).isEqualTo(2);
    }

    //testes podem validar exceções
    @Test
    void deveDividir_geraErroDivisaoPorZero() {
       //em junit
//        Assertions.assertThrows(ArithmeticException.class, () -> {
//            calculadora.dividir(4, 0);
//        });

        //em assertJ
        Throwable t = catchThrowable(() -> calculadora.dividir(4, 0));
        assertThat(t).isInstanceOf(ArithmeticException.class).hasMessage("/ by zero");
    }

}
