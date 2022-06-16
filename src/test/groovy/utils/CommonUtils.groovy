package utils

import groovyx.net.http.RESTClient

class CommonUtils {

    static sendRequest(String method,String endpoint, String path) {
        def client = new RESTClient(path)
        def response


        if (method == "GET") {
            response = client.get(
                    path: path + endpoint

            )
        }
        return response == null ? [] : response
    }
}