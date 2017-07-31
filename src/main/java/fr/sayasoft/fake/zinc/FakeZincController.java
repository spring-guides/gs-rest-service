package fr.sayasoft.fake.zinc;

import com.google.gson.Gson;
import fr.sayasoft.zinc.sdk.domain.OrderRequest;
import fr.sayasoft.zinc.sdk.domain.ZincError;
import fr.sayasoft.zinc.sdk.enums.ZincErrorCode;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@Log4j
public class FakeZincController {

    private static final String template = "Hello, %s!";
    public static final String GET_ORDER_RESPONSE = "{\n" +
            "  \"_type\" : \"order_response\",\n" +
            "  \"price_components\" : {\n" +
            "    \"shipping\" : 0,\n" +
            "    \"subtotal\" : 1999,\n" +
            "    \"tax\" : 0,\n" +
            "    \"total\" : 1999\n" +
            "  },\n" +
            "  \"merchant_order_ids\" : [\n" +
            "    {\n" +
            "      \"merchant_order_id\" : \"112-1234567-7272727\",\n" +
            "      \"merchant\" : \"amazon\",\n" +
            "      \"account\" : \"timbeaver@gmail.com\",\n" +
            "      \"placed_at\" : \"2014-07-02T23:51:08.366Z\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"tracking\" : [\n" +
            "    {\n" +
            "      \"merchant_order_id\" : \"112-1234567-7272727\",\n" +
            "      \"carrier\" : \"Fedex\",\n" +
            "      \"tracking_number\" : \"9261290100129790891234\",\n" +
            "      \"obtained_at\" : \"2014-07-03T23:22:48.165Z\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static final String POST_ORDER_RESPONSE_TO_BE_REPLACED = "XXX";
    public static final String POST_ORDER_RESPONSE = "{\n" +
            "  \"request_id\": \"fakeRequestIdStart-" + POST_ORDER_RESPONSE_TO_BE_REPLACED + "-fakeRequestIdEnd\"\n" +
            "}";

    private final AtomicLong counter = new AtomicLong();

    @SuppressWarnings("unused")
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @SuppressWarnings("unused")
    @RequestMapping(
            value = "/v1/order/{request_id}",
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8"
    )
    public String getOrder(@PathVariable(value = "request_id") String requestId) {
        return GET_ORDER_RESPONSE;
    }

    /**
     * Fake method to test order posting
     * Conventions for testing:
     * <ul>
     * <li>if the unmarshalled OrderRequest has a field clientNotes that is a ZincErrorCode, then a ZincError will be returned.</li>
     * <li>else a response with the idemPotency in the requestId is returned</li>
     * </ul>
     */
    @SuppressWarnings("unused")
    @RequestMapping(
            value = "/v1/order",
            method = RequestMethod.POST,
            produces = "application/json; charset=UTF-8"
    )
    public ResponseEntity<?> postOrder(@RequestBody String json) {
        final Gson gson = new Gson();
        final OrderRequest orderRequest = gson.fromJson(json, OrderRequest.class);

        try {
            final ZincErrorCode zincErrorCode;
            zincErrorCode = ZincErrorCode.valueOf(orderRequest.getClientNotes().toString());
            final ZincError zincError = new ZincError(
                    zincErrorCode,
                    zincErrorCode.getMeaning(),
                    orderRequest.getIdempotencyKey()
            );
            log.info("Received request to generate error code, returning: " + zincError);
            /*
            Precision obtained from Zinc support: although an error message is returned, the HTTP header is a 200
            (and not 4XX as may be assumed)
            */
            return new ResponseEntity<>(
                    zincError,
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // nothing to do
            if (log.isInfoEnabled()){
                log.info("received clientNotes: " + orderRequest.getClientNotes() + " ; will go on");
            }
        } catch (Exception e) {
            log.error("An error occured", e);
        }

        return new ResponseEntity<>(
                POST_ORDER_RESPONSE.replace(POST_ORDER_RESPONSE_TO_BE_REPLACED, orderRequest.getIdempotencyKey()),
                HttpStatus.CREATED);
    }
}