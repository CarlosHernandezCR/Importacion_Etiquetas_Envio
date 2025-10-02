package project.domain.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import project.common.config.ShopifyConfig;
import project.common.constants.ConstantesError;
import project.common.constants.ConstantesShopify;
import project.domain.errors.CustomedException;
import project.domain.model.*;

import java.util.*;

@Service
public class ShopifyClient {
    private final RestTemplate rt = new RestTemplate();
    private final ShopifyConfig cfg;

    public ShopifyClient(ShopifyConfig cfg) { this.cfg = cfg; }

    private HttpEntity<?> auth() {
        HttpHeaders h = new HttpHeaders();
        h.set(ConstantesShopify.HDR_TOKEN, cfg.getAccessToken());
        h.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(h);
    }

    public ShopifyOrder buscarPedidoPorNumero(int num) {
        String url = cfg.base() + ConstantesShopify.P_ORDERS + ".json"
                + "?order_number=" + num + "&" + ConstantesShopify.Q_STATUS_ANY
                + "&" + ConstantesShopify.Q_FIELDS_ORDER;
        try {
            var r = rt.exchange(url, HttpMethod.GET, auth(), ShopifyOrderResponse.class);
            var body = r.getBody();
            return (body != null && body.getOrders()!=null && !body.getOrders().isEmpty())
                    ? body.getOrders().get(0) : null;
        } catch (HttpStatusCodeException e) {
            throw new CustomedException(ConstantesError.HTTP_FALLO_PREFIX + e.getStatusCode() + ": " + e.getResponseBodyAsString());
        }
    }

    public FulfillmentOrdersResponse fulfillmentOrders(long orderId) {
        String url = cfg.base() + ConstantesShopify.P_ORDERS + "/" + orderId
                + ConstantesShopify.P_FO + ConstantesShopify.P_JSON
                + "?" + ConstantesShopify.Q_FIELDS_FO;
        try {
            return rt.exchange(url, HttpMethod.GET, auth(), FulfillmentOrdersResponse.class).getBody();
        } catch (HttpStatusCodeException e) {
            throw new CustomedException(ConstantesError.HTTP_FALLO_PREFIX + e.getStatusCode() + ": " + e.getResponseBodyAsString());
        }
    }

    public void setEtiquetaEnPedido(long orderId, String etiqueta) {
        if (etiqueta == null || etiqueta.isBlank()) return;
        String url = cfg.base() + ConstantesShopify.P_METAF + ConstantesShopify.P_JSON;
        Map<String,Object> mf = Map.of(
                "namespace", ConstantesShopify.MF_NAMESPACE,
                "key",       ConstantesShopify.MF_KEY_LABEL,
                "type",      ConstantesShopify.MF_TYPE_TEXT,
                "value",     etiqueta,
                "owner_resource", "order",
                "owner_id",  orderId
        );
        Map<String,Object> body = Map.of("metafield", mf);
        try {
            rt.exchange(url, HttpMethod.POST, new HttpEntity<>(body, ((HttpEntity<?>)auth()).getHeaders()), Map.class);
        } catch (HttpStatusCodeException e) {
            throw new CustomedException(ConstantesError.HTTP_FALLO_PREFIX + e.getStatusCode() + ": " + e.getResponseBodyAsString());
        }
    }

    public void agregarTagPedido(long orderId, String tag) {
        if (tag == null || tag.isBlank()) return;

        final String K_ORDER = "order";
        final String K_TAGS  = "tags";

        try {
            String getUrl = cfg.base() + ConstantesShopify.P_ORDERS + "/" + orderId + ConstantesShopify.P_JSON;
            ResponseEntity<Map> resp = rt.exchange(getUrl, HttpMethod.GET, auth(), Map.class);
            @SuppressWarnings("unchecked")
            Map<String,Object> root = (Map<String,Object>) resp.getBody();
            @SuppressWarnings("unchecked")
            Map<String,Object> orderMap = (Map<String,Object>) (root != null ? root.get(K_ORDER) : Map.of());

            String cur = Objects.toString(orderMap.get(K_TAGS), "");

            Set<String> tags = new LinkedHashSet<>();
            if (!cur.isBlank()) {
                for (String t : cur.split(",")) {
                    String s = t.trim();
                    if (!s.isEmpty()) tags.add(s);
                }
            }
            tags.add(tag.trim());
            String nuevo = String.join(", ", tags);

            // PUT tags
            String putUrl = cfg.base() + ConstantesShopify.P_ORDERS + "/" + orderId + ConstantesShopify.P_JSON;
            Map<String,Object> body = Map.of(K_ORDER, Map.of("id", orderId, K_TAGS, nuevo));
            rt.exchange(putUrl, HttpMethod.PUT, new HttpEntity<>(body, ((HttpEntity<?>)auth()).getHeaders()), Map.class);

        } catch (HttpStatusCodeException e) {
            throw new CustomedException(ConstantesError.HTTP_FALLO_PREFIX + e.getStatusCode() + ": " + e.getResponseBodyAsString());
        }
    }
}
