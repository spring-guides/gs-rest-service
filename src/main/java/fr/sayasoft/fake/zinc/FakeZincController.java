package fr.sayasoft.fake.zinc;

import fr.sayasoft.zinc.sdk.domain.OrderRequest;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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

    @SuppressWarnings("unused")
    @RequestMapping(
            value = "/v1/order",
            method = RequestMethod.POST,
            produces = "application/json; charset=UTF-8"
    )
    public ResponseEntity<?> postOrder(@RequestBody OrderRequest orderRequest) {
// FIXME: at this point, the orderRequest, for an unknown reason, is not well deserialized
        // (except the field 'retailer'). But it *was* well serialized on the client side...

//        if (log.isDebugEnabled()) {
//            log.debug(ToStringBuilder.reflectionToString(orderRequest));
//        }
        System.out.println(ToStringBuilder.reflectionToString(orderRequest, ToStringStyle.MULTI_LINE_STYLE));
        return new ResponseEntity<>(POST_ORDER_RESPONSE, HttpStatus.CREATED);
//        return new ResponseEntity<>(POST_ORDER_RESPONSE.replace(POST_ORDER_RESPONSE_TO_BE_REPLACED, orderRequest.getIdempotencyKey()), HttpStatus.CREATED);
    }
}