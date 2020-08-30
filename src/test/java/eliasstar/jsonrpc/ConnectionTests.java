package eliasstar.jsonrpc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.net.http.HttpRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import eliasstar.gson.OptionalTypeAdapterFactory;
import eliasstar.jsonrpc.exceptions.ErrorException;
import eliasstar.jsonrpc.exceptions.IdMismatchException;
import eliasstar.jsonrpc.gson.RpcTypeAdapterFactory;
import eliasstar.jsonrpc.objects.Request;
import eliasstar.jsonrpc.objects.Response;
import eliasstar.utils.mocks.HttpClientMock;

public final class ConnectionTests {

    private static HttpClientMock client;
    private static Connection stringCon;
    private static Connection numberCon;
    private static Gson gson;

    @BeforeAll
    public static void initConnection() {
        client = new HttpClientMock();
        gson = new GsonBuilder().registerTypeAdapterFactory(RpcTypeAdapterFactory.instance()).registerTypeAdapterFactory(OptionalTypeAdapterFactory.instance()).serializeNulls().create();
        var reqBuilder = HttpRequest.newBuilder().uri(URI.create("https://www.example.com"));

        stringCon = new Connection("test", client, reqBuilder, gson);
        numberCon = new Connection(null, client, reqBuilder, gson);
    }

    // @RepeatedTest(3)
    // @DisplayName("requestIds should increment")
    // public void testRequestIds() {
    // var stringReqCount = stringCon.requestsMade();
    // var numberReqCount = numberCon.requestsMade();

    // assertDoesNotThrow(() -> {
    // client.setResponse("{\"jsonrpc\": \"2.0\", \"id\": \"test-" + stringReqCount
    // + "\", \"result\": \"test\"}");
    // stringCon.callRemoteProcedure("method");

    // client.setResponse("{\"jsonrpc\": \"2.0\", \"id\": " + numberReqCount + ",
    // \"result\": \"test\"}");
    // numberCon.callRemoteProcedure("method");
    // });

    // assertEquals(stringReqCount + 1, stringCon.requestsMade());
    // assertEquals(numberReqCount + 1, numberCon.requestsMade());
    // }

    @Test
    @DisplayName("request and response should correctly (de)serialize")
    public void testSendingRPC() {
        var stringResponse = "{\"jsonrpc\": \"2.0\", \"id\": \"test\", \"result\": \"test\"}";
        var stringRequest = new Request("test", "method");

        var numberResponse = "{\"jsonrpc\": \"2.0\", \"id\": 0, \"result\": \"test\"}";
        var numberRequest = new Request(0, "method");

        assertDoesNotThrow(() -> {
            client.setResponse(stringResponse);
            assertEquals(gson.fromJson(stringResponse, Response.class), stringCon.sendRequest(stringRequest));
            assertEquals(stringRequest, gson.fromJson(client.getRequest(), Request.class));

            client.setResponse(numberResponse);
            assertEquals(gson.fromJson(numberResponse, Response.class), numberCon.sendRequest(numberRequest));
            assertEquals(numberRequest, gson.fromJson(client.getRequest(), Request.class));
        });
    }

    @Test
    @DisplayName("wrong response id should throw exception")
    public void testWrongResponseId() {
        client.setResponse("{\"jsonrpc\": \"2.0\", \"id\": \"wrong\", \"result\": \"test\"}");

        assertThrows(IdMismatchException.class, () -> {
            stringCon.callRemoteProcedure("method");
        });

        assertThrows(IdMismatchException.class, () -> {
            numberCon.callRemoteProcedure("method");
        });
    }

    @Test
    @DisplayName("response with error should throw exception")
    public void testErrorResponse() {
        client.setResponse("{\"jsonrpc\": \"2.0\", \"id\": \"null\", \"error\": {\"code\": -32000, \"message\": \"test\"}}");

        assertThrows(ErrorException.class, () -> {
            stringCon.callRemoteProcedure("method");
        });

        assertThrows(ErrorException.class, () -> {
            numberCon.callRemoteProcedure("method");
        });
    }
}