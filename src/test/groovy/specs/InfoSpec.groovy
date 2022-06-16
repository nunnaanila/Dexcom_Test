package specs

import net.sf.json.JSON
import spock.lang.Shared
import spock.lang.Specification
import utils.CommonUtils

class InfoSpec extends Specification {
    def dexCmApiFoundRes;
    def apiResponse;
    def response;
    def subComps

    def "Info Endpoint Get request"() {
        given:
        def INFO_ENDPOINT = "/info"

        when: "Send request"
        response = CommonUtils.sendRequest("GET", INFO_ENDPOINT, path)
        println("********" + path)
        apiResponse = response.getData()
        def headerCheck = response.getHeaders("content-type").value.join(",")

        then: "Verify response code"
        assert response.status == 200
        assert headerCheck == "application/json"


        when: "Verify Product Name"
        boolean productFound = false;
        for (apiRes in apiResponse) {
            if (apiRes.'Product Name' == 'Dexcom API') {
                productFound = true;
                dexCmApiFoundRes = apiRes;
            }
        }

        then: "Product Name is found"
        assert productFound

        when: "Look for Device ID"
        boolean deviceNumberFound = false;
        if (dexCmApiFoundRes.'UDI / Device Identifier' == '00386270000668')
            deviceNumberFound = true;

        then: "Device ID found"
        assert deviceNumberFound


        when: "Check for Version"
        def status
        if (dexCmApiFoundRes.'UDI / Production Identifier'.'Version' == '3.1.0.0')
            status = "Versin found"
        else
            status = "Version not found"

        then: "Version matched"
        println status

        when: "Check for Part Number"
        def udiPN = dexCmApiFoundRes.'UDI / Production Identifier'

        then: "Part Number found"
        assert udiPN.'Part Number (PN)' == '350-0019'

        when: "Check for Name under Sub-components list"
        subComps = dexCmApiFoundRes.'UDI / Production Identifier'.'Sub-Components';
        boolean apiGatewayfound = false;
        for (subComp in subComps) {
            if (subComp.'Name' == 'api-gateway') {
                apiGatewayfound = true
                break;
            }
        }
        then: "Correct Name found"
        assert apiGatewayfound

        when: "Check for Insulin service under sub components list"
        subComps = dexCmApiFoundRes.'UDI / Production Identifier'.'Sub-Components';
        boolean insulinFuond = false;
        for (subComp in subComps) {
            if (subComp.'Name' == 'insulin-service') {
                insulinFuond = true;
                break;
            }
        }

        then: "Check for NAme"
        assert insulinFuond;

        where:
        path << ["https://api.dexcom.com", "https://sandbox-api.dexcom.com"]

    }


    /* assert response.getData()[0].'Product Name' == "Dexcom API"
     assert response.getData()[0].'UDI / Device Identifier' == "00386270000668"
     println response.getData()[0].'UDI / Production Identifier'.Version
     println response.getData()[0].'UDI / Production Identifier'.'Part Number (PN)'
     println response.getData()[0].'UDI / Production Identifier'.'Sub-Components'[0].Name*/

}



