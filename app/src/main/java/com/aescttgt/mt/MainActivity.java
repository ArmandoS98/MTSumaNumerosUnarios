package com.aescttgt.mt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MAQUINA_COLGADA = "MAQUINA COLGADA";
    private TextInputEditText cinta;
    private Button iniciar;
    private TextView visualizarCinta;
    private char[] conjuntoCinta;
    private ArrayList<Trancisiones> trancisiones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cinta = findViewById(R.id.etCinta);
        iniciar = findViewById(R.id.btnIniciar);
        visualizarCinta = findViewById(R.id.tvCambios);

        iniciar.setOnClickListener(this);
    }

    //Estados
    /*
    0 -> #, L, 1
    1 -> 1, #, 2
    1 -> #, #, h

    2 -> #, L, 3

    3 -> 1, L, 3
    3 -> #, 1, 4

    4 -> 1, R, 4
    4 -> #, #, h
    h -> !
     */


    @Override
    public void onClick(View v) {

        //Cargo las transiciones
        trancisiones.add(new Trancisiones("0", "#", "L", "1"));
        trancisiones.add(new Trancisiones("1", "1", "#", "2"));
        trancisiones.add(new Trancisiones("1", "#", "#", "h"));
        trancisiones.add(new Trancisiones("2", "#", "L", "3"));
        trancisiones.add(new Trancisiones("3", "1", "L", "3"));
        trancisiones.add(new Trancisiones("3", "#", "1", "4"));
        trancisiones.add(new Trancisiones("4", "1", "R", "4"));
        trancisiones.add(new Trancisiones("4", "#", "#", "h"));
        trancisiones.add(new Trancisiones("h", "#", "#", "h"));
        String cintaCompleta = cinta.getText().toString();
        conjuntoCinta = cintaCompleta.toCharArray();
        int posicion = conjuntoCinta.length;

        String valResultante = new String(tm(trancisiones.get(0), conjuntoCinta, (posicion - 1)));

        if (!cintaCompleta.equals("##")) {
            String[] valores = cintaCompleta.split("#");
            String calculoMT = valResultante.replace("#", "");
            valores[0] = (isNullOrEmpty(valores[0])) ? "0" : valores[0];
            valores[1] = (isNullOrEmpty(valores[1])) ? "0" : valores[1];
            int calculo = calculoMT.length();
            visualizarCinta.setText("Valores Ingresados: " + valores[0] + "(" + valores[0].length() + ") + " + valores[1] + "(" + valores[1].length() + ")\nResultado: " + valResultante.replace("#", "") + "(" + calculo + ")");
//        visualizarCinta.setText(valResultante);
        } else {
            visualizarCinta.setText(valResultante);
        }
    }

    public boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

    /**
     * L = Izquierda
     * R = Derecha
     * # = vacui
     *
     * @param mTrancisiones
     * @param conjuntoCinta
     * @param posision
     * @return
     */
    private char[] computo(Trancisiones mTrancisiones, char[] conjuntoCinta, int posision) {
        try {
            if (!mTrancisiones.getNombreEstado().equals("h")) {
                if (mTrancisiones.getLectura().equals(String.valueOf(conjuntoCinta[posision]))) {
                    char val = conjuntoCinta[posision];
                    String temp = String.valueOf(val);
                    if (temp.equals(mTrancisiones.getLectura())) {
                        //Intercambiamos por el nuevo signo

                        if (mTrancisiones.getCambios().equals("R") || mTrancisiones.getCambios().equals("L")) {
                            //nada
                        } else {
                            conjuntoCinta[posision] = mTrancisiones.getCambios().charAt(0);
                        }

                        //Movemos nuestro lector a la posision siguiente
                        if (mTrancisiones.getCambios().equals("#")) {
                            //no hacer nada
                        } else {
                            if (mTrancisiones.getCambios().equals("L"))
                                posision--;
                            else if (mTrancisiones.getCambios().equals("R"))
                                posision++;
                        }

                        for (int j = 0; j < trancisiones.size(); j++) {
                            boolean estadoSiguiente = mTrancisiones.getEstadoSiguiente().equals(trancisiones.get(j).getNombreEstado());
                            boolean validarSimboloLectura = String.valueOf(conjuntoCinta[posision]).equals(trancisiones.get(j).getLectura());
                            if (estadoSiguiente && validarSimboloLectura) {
                                computo(trancisiones.get(j), conjuntoCinta, posision);
                                break;
                            }
                        }
                        return conjuntoCinta;
                    } else {
                        return "MAQUINA COLGADA".toCharArray();
                    }
                } else {
                    return "MAQUINA COLGADA".toCharArray();
                }
            } else {
                return conjuntoCinta;
            }
        } catch (Exception ex) {
            return "MAQUINA COLGADA".toCharArray();
        }
    }

    public static final String ESTADO_FINAL = "h";
    public static final String ESPACION_EN_BLANCO = "#";
    public static final String DESPLAZAR_IZQUIERDA = "L";
    public static final String DESPLAZAR_DERECHA = "R";

    private char[] tm(Trancisiones mTrancisiones, char[] conjuntoCinta, int posision) {
        String nombreEstadoActual = mTrancisiones.getNombreEstado();
        boolean esElEstadoFinal = nombreEstadoActual.equals(ESTADO_FINAL);
        if (!esElEstadoFinal) {
            char valorDeLaPilaActual = conjuntoCinta[posision];
            String simboloLectura = mTrancisiones.getLectura();
            boolean esElValorEsperado = simboloLectura.equals(String.valueOf(valorDeLaPilaActual));
            if (esElValorEsperado) {
                String siguienteEstado = mTrancisiones.getEstadoSiguiente();
                String accion = mTrancisiones.getCambios();
                boolean esDesplazamiento = accion.equals(DESPLAZAR_DERECHA) || accion.equals(DESPLAZAR_IZQUIERDA);
                boolean hayTransicionEncontrada = false;

                //Intepretamos la accion de las transiciones.
                if (!esDesplazamiento) {
                    //Aqui Reemplazamos el caracer de la posicion actual
                    conjuntoCinta[posision] = accion.charAt(0);
                } else {
                    //Con esta logica, nos movemos hace adelante o atras!
                    switch (accion) {
                        case DESPLAZAR_DERECHA:
                            //<----
                            posision++;
                            break;
                        case DESPLAZAR_IZQUIERDA:
                            //---->
                            posision--;
                            break;
                        default:
                            break;
                    }
                }

                for (Trancisiones findTrancision : trancisiones) {
                    String identificadorSiguienteEstado = "";
                    String simboloDeLectura = "";
                    String valorSiguiente = "";
                    identificadorSiguienteEstado = findTrancision.getNombreEstado();
                    simboloDeLectura = findTrancision.getLectura();
                    boolean esEstadoFinal = identificadorSiguienteEstado.equals(ESTADO_FINAL);
                    if (!esEstadoFinal) {
                        char tempo = conjuntoCinta[posision];
                        valorSiguiente = String.valueOf(tempo);
                        boolean identidicadorDeEstado = identificadorSiguienteEstado.equals(siguienteEstado);
                        boolean esElSimboloDeLecturaEnCola = simboloDeLectura.equals(valorSiguiente);

                        if (identidicadorDeEstado && esElSimboloDeLecturaEnCola) {
                            hayTransicionEncontrada = true;
                            tm(findTrancision, conjuntoCinta, posision);
                            break;
                        }
                    } else {
                        hayTransicionEncontrada = true;
                    }
                }

                //Buscar la trancision esperada se valida el No. de estado siguiente.
                //Pero tamben se compara el digito de lectura esperado, si no se encuentra
                return (hayTransicionEncontrada) ? conjuntoCinta : MAQUINA_COLGADA.toCharArray();
            } else {
                return MAQUINA_COLGADA.toCharArray();
            }
        } else {
            return conjuntoCinta;
        }
    }
}