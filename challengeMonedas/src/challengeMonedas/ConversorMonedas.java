
package challengeMonedas;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import conversor.TasaCambio;
import conversor.BuscarTasa;
import conversor.Conversion;

public class ConversorMonedas {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // código de moneda actual
            String divisaActual = divisaActual(scanner, "Ingresa el código de tu moneda actual (por ejemplo, MXN)");

            if (laDivisaActualEsValida(divisaActual)) {

                TasaCambio tasaCambioActual = obtenerTasaCambioActual(divisaActual);

                if (tasaCambioActual.getResult().equalsIgnoreCase("error")) {
                    System.out.println("Error en el código de la moneda actual, intenta de nuevo");
                    continue;
                }

                // Obtener la moneda deseada y la cantidad de dinero a convertir
                Conversion conversionInput = promptForConversionInput(scanner, tasaCambioActual);

                // Realizar la conversión aquí
                convertedAmount(conversionInput, tasaCambioActual);

                System.out.println();
                System.out.println("Si NO deseas continuar con nueva conversion ingrese N, de lo contrario ingrese cualquier otra letra  ");
                String exit = scanner.nextLine();
                if (exit.equalsIgnoreCase("N")) {
                    break;
                }
            }
        }
        System.out.println("Finalizó la ejecución");
    }

    // Conversor de monto
    private static void convertedAmount(Conversion conversion, TasaCambio divisaActual) {

        var currentCurrencyCode = divisaActual.getBase_code();

        var desiredCurrency = conversion.divisa();
        var amount = conversion.cantidad();

        double convertedAmount = amount * divisaActual.getConversion_rates().get(desiredCurrency);

        double roundedAmount = Math.round(convertedAmount * 100.0) / 100.0;

        System.out.println();
        System.out.println("El valor de " + amount + "[" + currentCurrencyCode + "]" +
                " corresponse al valor final de: " + roundedAmount + " [" + desiredCurrency + "]");
    }

    // Solicitar entrada para la conversión
    private static Conversion promptForConversionInput(Scanner scanner, TasaCambio tasaCambio) {
        do {
            // Aquí se introduce la moneda deseada a convertir
            String divisaDeseada = divisaActual(scanner, "Ingresa el código de la moneda deseada (por ejemplo, USD)");

            if (laDivisaActualEsValida(divisaDeseada) &&
                    tasaCambio.getConversion_rates().containsKey(divisaDeseada)) {

                //aquí se introduce la cantidad de dinero
                double cantidadDeDinero = Cantidad(scanner, tasaCambio.getBase_code(), divisaDeseada);

                return new Conversion(divisaDeseada, cantidadDeDinero);
            } else {
                System.out.println("Error en el código de la moneda deseada, intenta de nuevo!");
            }
        } while (true);
    }

    // Solicitar código de moneda
    private static String divisaActual(Scanner scanner, String mensaje) {
        System.out.println(mensaje);
        return scanner.nextLine().toUpperCase();
    }

    // Función que valida si un código de moneda es válido
    private static boolean laDivisaActualEsValida(String divisa) {
        try {
            // Intenta convertir el código de moneda a un número
            Integer.parseInt(divisa);
            // Si no se lanza una excepción, significa que es un número, por lo que el código de moneda no es válido
            return false;
        } catch (NumberFormatException e) {
            // Si se lanza una excepción, significa que no es un número, por lo que el código de moneda es válido
            return true;
        }
    }

    // Función que obtiene la tasa de cambio actual para una moneda dada
    private static TasaCambio obtenerTasaCambioActual(String divisaCodigo) {
        BuscarTasa BuscarTasa = new BuscarTasa();
        return BuscarTasa.buscarTasa(divisaCodigo);
    }

    // Solicitar cantidad de dinero
    private static double Cantidad(Scanner scanner, String codigoActualDivisa, String divisaDeseada) {
        while (true) {
            System.out.println("Ingresa la cantidad de " + codigoActualDivisa + " para convertir a " + divisaDeseada + ":");

            if (scanner.hasNextDouble()) {
                double cantidadDeDinero = scanner.nextDouble();
                scanner.nextLine(); // Consumir el salto de línea pendiente
                return cantidadDeDinero;
            } else {
                System.out.println("Entrada inválida. Por favor, ingresa una cantidad válida.");
                scanner.nextLine(); // Limpiar la entrada del scanner
            }
        }
    }
}
