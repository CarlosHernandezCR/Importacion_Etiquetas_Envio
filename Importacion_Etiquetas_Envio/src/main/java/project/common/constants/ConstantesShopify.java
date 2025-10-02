package project.common.constants;

public class ConstantesShopify {
    // Headers
    public static final String HDR_TOKEN = "X-Shopify-Access-Token";

    // Paths
    public static final String P_ORDERS = "/orders";
    public static final String P_JSON   = ".json";
    public static final String P_FO     = "/fulfillment_orders";
    public static final String P_METAF  = "/metafields";

    // Query
    public static final String Q_STATUS_ANY = "status=any";
    public static final String Q_FIELDS_ORDER = "fields=id,order_number,name,shipping_address";
    public static final String Q_FIELDS_FO    = "fields=id,status,destination,line_items,assigned_location_id";

    // Metafields
    public static final String MF_NAMESPACE = "custom";
    public static final String MF_KEY_LABEL = "shipping_label";
    public static final String MF_TYPE_TEXT = "single_line_text_field";
}
