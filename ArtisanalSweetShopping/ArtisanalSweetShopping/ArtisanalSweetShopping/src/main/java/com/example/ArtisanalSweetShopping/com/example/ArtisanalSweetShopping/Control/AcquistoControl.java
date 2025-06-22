package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Control;

import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Database.*;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity.*;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DAOException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.DBConnectionException;
import com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Exception.OperationException;

import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;

@RestController
public class AcquistoControl {

    private static final Map<Integer, List<DettagliOrdineEntity>> carrelliInAttesa = new HashMap<>();
    private static final Map<Integer, OrdineEntity> ordiniTemporanei = new HashMap<>();
    private static final Map<Integer, String> cartaCreditoTemporanea = new HashMap<>();
    private static int nextIdCarrello = 1;
    private static final int SOGLIA_CLIENTE_ABITUALE = 3;

    public static class ProdottoQuantita {
        public String codiceProdotto;
        public int quantita;
    }

    public static class InputOrdine {
        public String nomeUtente;
        public List<ProdottoQuantita> prodotti;
        public String codiceSconto;
        public String indirizzo;
        public String cartaCredito;
    }

    public static class OutputOrdine {
        public List<DettagliOrdineEntity> dettagli;
        public float totale;
        public String messaggio;
        public int idCarrello;

        public OutputOrdine(List<DettagliOrdineEntity> dettagli, float totale, String messaggio, int idCarrello) {
            this.dettagli = dettagli;
            this.totale = totale;
            this.messaggio = messaggio;
            this.idCarrello = idCarrello;
        }
    }

    public static class OutputPagamentoFallito {
        public String messaggio;
        public String suggerimento;

        public OutputPagamentoFallito(String messaggio, String suggerimento) {
            this.messaggio = messaggio;
            this.suggerimento = suggerimento;
        }
    }

    @PostMapping("/checkout")
    public Object avviaOrdine(@RequestBody InputOrdine input) throws OperationException {
        try {
            float totale = 0f;
            List<DettagliOrdineEntity> dettagli = new ArrayList<>();

            ClienteRegistratoEntity cliente = ClienteRegistratoDAO.leggiCliente(input.nomeUtente);
            if (cliente == null) {
                return new OutputPagamentoFallito("Utente non trovato", "Effettua il login o registrati.");
            }

            for (ProdottoQuantita p : input.prodotti) {
                ProdottoEntity prod = ProdottoDAO.leggiProdotto(p.codiceProdotto);
                if (prod == null || prod.getQuantitaDisponibile() < p.quantita) {
                    return new OutputPagamentoFallito("Prodotto non disponibile: " + p.codiceProdotto,
                            "Riduci la quantità o scegli un altro prodotto.");
                }
                totale += prod.getPrezzo() * p.quantita;
                dettagli.add(new DettagliOrdineEntity(0, p.codiceProdotto, p.quantita));
            }

            if (input.codiceSconto != null && !input.codiceSconto.trim().isEmpty()) {
                if (cliente.getNumeroOrdini() < SOGLIA_CLIENTE_ABITUALE) {
                    return new OutputPagamentoFallito("Per usare uno sconto devi essere un cliente abituale.",
                            "Completa almeno " + SOGLIA_CLIENTE_ABITUALE + " ordini per attivarlo.");
                }

                ScontoEntity sconto = ScontoDAO.leggiScontoValido(input.codiceSconto);
                if (sconto == null) {
                    return new OutputPagamentoFallito("Codice sconto non valido.",
                            "Controlla errori di battitura o usa un altro codice.");
                }

                totale -= totale * sconto.getPercentuale() / 100;
            }

            String carta = cliente.getNumeroCarta() != null && !cliente.getNumeroCarta().isEmpty()
                    ? cliente.getNumeroCarta()
                    : input.cartaCredito;

            if (carta == null || carta.length() < 12) {
                return new OutputPagamentoFallito("Carta non valida o non fornita.",
                        "Inserisci almeno 12 cifre di una carta valida.");
            }

            int id = nextIdCarrello++;
            OrdineEntity ordine = new OrdineEntity(0,
                    new Date(System.currentTimeMillis()),
                    totale,
                    "in_corso",
                    input.nomeUtente,
                    input.codiceSconto,
                    input.indirizzo);

            ordiniTemporanei.put(id, ordine);
            carrelliInAttesa.put(id, dettagli);
            cartaCreditoTemporanea.put(id, carta);

            return new OutputOrdine(dettagli, totale, "Ordine in attesa di conferma", id);

        } catch (DAOException | DBConnectionException e) {
            throw new OperationException("Errore nella preparazione dell’ordine: " + e.getMessage());
        }
    }

    @PostMapping("/checkout/conferma/{id}")
    public Object confermaOrdine(@PathVariable int id) throws OperationException {
        try {
            if (!ordiniTemporanei.containsKey(id)) {
                return new OutputPagamentoFallito("Carrello non trovato", "Ricomincia la procedura.");
            }

            OrdineEntity ordine = ordiniTemporanei.get(id);
            String carta = cartaCreditoTemporanea.get(id);

            if (!PagamentoService.eseguiPagamento(carta, ordine.getCostoTotale())) {
                return new OutputPagamentoFallito("Pagamento rifiutato", "Verifica i dati della carta.");
            }

            ordine.setStato("ordinata");
            ordine = OrdineDAO.creaOrdine(ordine);
            int ordineId = ordine.getIdOrdine();

            for (DettagliOrdineEntity d : carrelliInAttesa.get(id)) {
                d.setIdOrdine(ordineId);
                DettaglioOrdineDAO.aggiungiDettaglio(d);

                ProdottoEntity p = ProdottoDAO.leggiProdotto(d.getCodiceProdotto());
                p.setQuantitaDisponibile(p.getQuantitaDisponibile() - d.getQuantita());
                ProdottoDAO.aggiornaProdotto(p);
            }

            if (ordine.getCodiceSconto() != null) {
                ScontoDAO.segnaScontoComeUtilizzato(ordine.getCodiceSconto());
            }

            ClienteRegistratoDAO.incrementaNumeroOrdini(ordine.getNomeUtente());
            ClienteRegistratoEntity cliente = ClienteRegistratoDAO.leggiCliente(ordine.getNomeUtente());

            if (cliente != null) {
                System.out.println("Email inviata a " + cliente.getEmail());
                System.out.println("SMS inviato a " + cliente.getNumeroTelefono());

                if (cliente.getNumeroOrdini() >= SOGLIA_CLIENTE_ABITUALE) {
                    System.out.println("Cliente abituale confermato: " + cliente.getNomeUtente());
                }
            }

            ordiniTemporanei.remove(id);
            carrelliInAttesa.remove(id);
            cartaCreditoTemporanea.remove(id);

            return "Ordine confermato con successo! ID: " + ordineId;

        } catch (DAOException | DBConnectionException e) {
            throw new OperationException("Errore nella conferma dell’ordine: " + e.getMessage());
        }
    }

    @PostMapping("/checkout/annulla/{id}")
    public String annullaOrdine(@PathVariable int id) throws OperationException {
        if (!ordiniTemporanei.containsKey(id)) {
            throw new OperationException("Nessun ordine temporaneo corrispondente all’ID indicato.");
        }
        ordiniTemporanei.remove(id);
        carrelliInAttesa.remove(id);
        cartaCreditoTemporanea.remove(id);
        return "Carrello annullato con successo.";
    }

    static class PagamentoService {
        public static boolean eseguiPagamento(String cartaCredito, float importo) {
            return cartaCredito != null && cartaCredito.length() >= 12 && !cartaCredito.contains("0000");
        }
    }
}
