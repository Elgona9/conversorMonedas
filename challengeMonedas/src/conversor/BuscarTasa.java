package conversor;

import java.lang.module.Configuration;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

public class BuscarTasa {
	public TasaCambio buscarTasa(String codigoDivisa) {
		String apiKey = "c7a3f286e456eaf19b69c1c5";
		String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + codigoDivisa;
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest
					.newBuilder()
					.uri(URI.create(url))
					.build();
			
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String json = response.body();
			return new Gson().fromJson(json, TasaCambio.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
